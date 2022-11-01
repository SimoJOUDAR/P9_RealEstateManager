package fr.mjoudar.realestatemanager.ui.addEditOffer

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.BuildConfig
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.databinding.FragmentAddEditOfferBinding
import fr.mjoudar.realestatemanager.domain.models.*
import fr.mjoudar.realestatemanager.notification.NotificationHandler
import fr.mjoudar.realestatemanager.ui.adapters.AddEditOfferPicturesAdapter
import fr.mjoudar.realestatemanager.utils.compressImageFile
import fr.mjoudar.realestatemanager.utils.setUpPermissionsUtil
import fr.mjoudar.realestatemanager.utils.GeocodeUtils
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.util.*

private const val REQ_CAPTURE = 100
private const val RES_IMAGE = 100
private const val FILE_PROVIDER_NAME = ".fileprovider"

@AndroidEntryPoint
class AddEditOfferFragment : Fragment(), CoroutineScope by MainScope(){

    private var _binding: FragmentAddEditOfferBinding? = null
    private val binding get() = _binding!!
    private var argOffer = Offer("")
    lateinit var navController: NavController

    private val viewModel: AddEditOfferViewModel by viewModels()
    private lateinit var adapter: AddEditOfferPicturesAdapter
    private var isPermissionsAllowed: Boolean = false
    private var imageUri: Uri? = null
    private var imgPath: String = ""
    private var queryImageUrl: String = ""

    // Date picker listeners
    private val publicationDateListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.publicationDate.value = calendar
        }
    }
    private val closureDateListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.closureDate.value = calendar
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {navController = findNavController()}
        onBackPressedHandler()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddEditOfferBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        setObservers()
        setListener()
        setSoftKeyboardOff()
        isPermissionsAllowed = setUpPermissionsUtil(requireContext())
        retrievedArguments()
    }

    /***********************************************************************************************
     ** Data retrieval
     ***********************************************************************************************/
    private fun retrievedArguments() {
        arguments?.let { it ->
            it.getParcelable<Offer>(OFFER_ARG)?.let { it2 ->
                argOffer = it2
                viewModel.loadData(it2)
            }
        }
    }

    /***********************************************************************************************
     ** Adapters
     ***********************************************************************************************/
    private fun setAdapters() {
        setTypeAdapters()
        setPhotosAdapter()
    }

    private fun setTypeAdapters() {
        binding.sectionAddEditType.addEditOfferPropertyType.setAdapter(ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, PropertyType.values()))
        binding.sectionAddEditType.addEditOfferPropertyType.threshold = 100 // to prevent the dropdown menu from collapsing to only one single value
        binding.sectionAddEditType.addEditOfferOfferType.setAdapter(ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, OfferType.values()))
        binding.sectionAddEditType.addEditOfferOfferType.threshold
    }

    private fun setPhotosAdapter() {
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Photo
            val list = viewModel.photos.value!!
            list.remove(item)
            viewModel.photos.value = list
        }
        val onContextClickListener = View.OnContextClickListener { true }
        adapter = AddEditOfferPicturesAdapter(onClickListener, onContextClickListener)
    }

    private fun setAgentsAdapter(data: List<Agent>) {
        binding.sectionAddEditAgent.addEditOfferAgent.setAdapter(ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, data))
        binding.sectionAddEditAgent.addEditOfferAgent.threshold = 100
        binding.sectionAddEditAgent.addEditOfferAgent.setOnItemClickListener { parent, _, position, _ ->
            viewModel.agent.value = parent.getItemAtPosition(position) as Agent
        }
    }

    /***********************************************************************************************
     ** Observers
     ***********************************************************************************************/
    private fun setObservers() {
        setAgentListObserver()
        setPhotosObserver()
        setIsOfferSavedObserver()
        setIsAgentListEmptyObserver()
    }

    // Observes agentList to setup the agents' autocomplete adapter
    private fun setAgentListObserver() {
        viewModel.agentList.observe(viewLifecycleOwner) { it1 ->
            it1?.let { it2 -> if (it2.isNotEmpty()) setAgentsAdapter(it2) }
        }
    }

    // Observes photos to setup the photos RecyclerView adapter
    private fun setPhotosObserver() {
        viewModel.photos.observe(viewLifecycleOwner) { it1 ->
            it1?.let { it2 ->
                adapter.setData(it2)
                binding.sectionAddEditPictures.picturesRecyclerView.adapter = adapter
                setRecyclerViewBackground(it1)
            }
        }
    }

    private fun setIsOfferSavedObserver() {
        viewModel.isOfferSaved.observe(viewLifecycleOwner) {
            if (it) {
                launchNotification()
                navigateBack(viewModel.offer!!)
            }
        }
    }

    private fun setIsAgentListEmptyObserver() {
        viewModel.isAgentListEmpty.observe(viewLifecycleOwner) { it1 ->
            if (it1) {
                viewModel.errorMessage?.let { it2 ->
                    emptyAgentListHandler(it2)
                }?: run {
                    emptyAgentListHandler()
                }
            }
        }
    }

    /***********************************************************************************************
     ** Back navigation
     ***********************************************************************************************/
    private fun onBackPressedHandler() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack(argOffer)
            }
        })
    }

    private fun navigateBack(data: Offer) {
        when (viewModel.isNewOffer) {
            true -> navController.navigate(AddEditOfferFragmentDirections.actionAddEditOfferFragmentToMainViewpagerFragment())
            false -> popBackStackWithArgument(data)
        }
    }

    private fun popBackStackWithArgument(data : Offer) {
        lifecycleScope.launchWhenResumed {
            navController.previousBackStackEntry?.savedStateHandle?.set(OFFER_ARG, data)
            navController.popBackStack()
        }
    }

    /***********************************************************************************************
     ** Listeners
     ***********************************************************************************************/
    private fun setListener() {
        setDatePickersListeners()
        setupAddPictureListener()
        saveButtonListener()
        deleteButtonListener()
    }

    private fun setDatePickersListeners() {
        binding.sectionAddEditDates.addEditOfferPublicationDate.setOnClickListener {
            datePickerLauncher(publicationDateListener, viewModel.publicationDate.value!!)
        }
        binding.sectionAddEditDates.addEditOfferClosureDate.setOnClickListener {
            datePickerLauncher(closureDateListener, viewModel.closureDate.value!!, viewModel.publicationDate.value!!.timeInMillis)
        }
    }

    private fun datePickerLauncher(listener:  DatePickerDialog.OnDateSetListener, calendar: Calendar, minDate: Long? = null) {
        val picker = DatePickerDialog(
            requireContext(),
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        picker.datePicker.maxDate = Calendar.getInstance().timeInMillis
        minDate?.let { picker.datePicker.minDate = minDate }
        picker.show()
    }

    private fun saveButtonListener() {
        binding.btnSaveOffer.setOnClickListener {
            saveOffer()
        }
    }

    private fun deleteButtonListener() {
        binding.btnDeleteOffer.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.delete_offer_not_allowed), Toast.LENGTH_SHORT).show()
        }
    }

    /***********************************************************************************************
     ** Photos adding
     ***********************************************************************************************/
    private fun setupAddPictureListener() {
        binding.sectionAddEditPictures.buttonPicture.setOnClickListener {
            chooseImage()
            if (isPermissionsAllowed) {
                chooseImage()
            }
        }
    }

    // when permission granted
    private fun chooseImage() {
        startActivityForResult(getPickImageIntent(), RES_IMAGE)
    }

    // Opens the image selector
    private fun getPickImageIntent(): Intent? {
        var chooserIntent: Intent? = null
        var intentList: MutableList<Intent> = ArrayList()
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri())
        intentList = addIntentsToList(requireContext(), intentList, pickIntent)
        intentList = addIntentsToList(requireContext(), intentList, takePhotoIntent)
        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(
                intentList.removeAt(intentList.size - 1),
                getString(R.string.select_capture_image)
            )
            chooserIntent!!.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                intentList.toTypedArray<Parcelable>()
            )
        }
        return chooserIntent
    }

    private fun addIntentsToList(context: Context, list: MutableList<Intent>, intent: Intent): MutableList<Intent> {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
        }
        return list
    }

    private fun setImageUri(): Uri {
        val folder = File("${requireContext().getExternalFilesDir(Environment.DIRECTORY_DCIM)}")
        folder.mkdirs()
        val file = File(folder, "Image_Tmp.jpg")
        if (file.exists())
            file.delete()
        file.createNewFile()
        imageUri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + FILE_PROVIDER_NAME, file)
        imgPath = file.absolutePath
        return imageUri!!
    }

    // when picture has been selected
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RES_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    handleImageRequest(data)
                }
            }
        }
    }

    // when picture has been selected => handle the results (the picture)
    private fun handleImageRequest(data: Intent?) {
        val exceptionHandler = CoroutineExceptionHandler { _, t ->
            t.printStackTrace()
            Toast.makeText(requireContext(), t.localizedMessage ?: getString(R.string.unknown_error_msg), Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch(Dispatchers.Main + exceptionHandler) {
            if (data?.data != null) {
                //Selected photos from Gallery
                imageUri = data.data
                queryImageUrl = imageUri?.path!!
                queryImageUrl = requireActivity().compressImageFile(queryImageUrl, false, imageUri!!)
            } else {
                // Camera picture
                queryImageUrl = imgPath
                requireActivity().compressImageFile(queryImageUrl, uri = imageUri!!)
            }
            imageUri = Uri.fromFile(File(queryImageUrl))

            if (queryImageUrl.isNotEmpty()) {
                submitPhotoToList(Photo(UUID.randomUUID().toString(),imageUri.toString(), ""))
            }
        }
    }

    private fun submitPhotoToList(photo: Photo) {
        val list = viewModel.photos.value!!
        list.add(photo)
        viewModel.photos.value = list
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQ_CAPTURE -> {
                if (isPermissionsAllowed) {
                    chooseImage()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /***********************************************************************************************
     ** Utils
     ***********************************************************************************************/

    private fun setRecyclerViewBackground(data:  MutableList<Photo>) {
        when (data.isEmpty()) {
            true -> {
                binding.sectionAddEditPictures.recyclerviewBackground.visibility = View.VISIBLE
                binding.sectionAddEditPictures.picturesRecyclerView.visibility = View.INVISIBLE
            }
            false -> {
                binding.sectionAddEditPictures.recyclerviewBackground.visibility = View.INVISIBLE
                binding.sectionAddEditPictures.picturesRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun setSoftKeyboardOff() {
        binding.sectionAddEditType.addEditOfferPropertyType.inputType = InputType.TYPE_NULL
        binding.sectionAddEditType.addEditOfferOfferType.inputType = InputType.TYPE_NULL
        binding.sectionAddEditAgent.addEditOfferAgent.inputType = InputType.TYPE_NULL
        binding.sectionAddEditDates.addEditOfferPublicationDate.inputType = InputType.TYPE_NULL
        binding.sectionAddEditDates.addEditOfferClosureDate.inputType = InputType.TYPE_NULL
    }

    private fun emptyAgentListHandler(error: String? = null) {
        error?.let {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }?: run {
            Toast.makeText(requireContext(), getString(R.string.empty_agent_list), Toast.LENGTH_SHORT).show()
        }
        findNavController().navigate(R.id.mainViewpagerFragment)
    }

    private fun launchNotification() {
        NotificationHandler.createNotification(
            requireContext(),
            "Offer saved",
            "The offer has been successfully saved.",
            "",
            autoCancel = false
        )
    }

    private fun isValidInput(): Boolean {
        return viewModel.propertyType.value!!.isNotEmpty() &&
                viewModel.offerType.value!!.isNotEmpty() &&
                viewModel.price.value!!.isNotEmpty() &&
                viewModel.surface.value!!.isNotEmpty() &&
                viewModel.rooms.value!!.isNotEmpty() &&
                viewModel.bathrooms.value!!.isNotEmpty() &&
                viewModel.description.value!!.isNotEmpty() &&
                viewModel.photos.value!!.isNotEmpty() &&
                viewModel.address.value!!.isNotEmpty()&&
                viewModel.city.value!!.isNotEmpty() &&
                viewModel.zipCode.value!!.isNotEmpty() &&
                viewModel.country.value!!.isNotEmpty() &&
                viewModel.agent.value != null &&
                viewModel.publicationDate.value != null &&
                (if (viewModel.isOfferClosed.value!!) viewModel.closureDate.value != null else true)
    }

    /***********************************************************************************************
     ** Save offer
     ***********************************************************************************************/
    private fun saveOffer() {
        when (isValidInput()) {
            true -> fetchLanLng()
            false -> Toast.makeText(requireContext(), R.string.invalid_input, Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchLanLng() {
        val address = if (viewModel.state.value!!.isNotEmpty()) "${viewModel.address.value}, ${viewModel.city.value}, ${viewModel.state.value}, ${viewModel.state.value}, ${viewModel.country.value}" else "${viewModel.address.value}, ${viewModel.city.value}, ${viewModel.zipCode.value}, ${viewModel.country.value}"
        val geocode = GeocodeUtils
        geocode.getLatLngFromAddress(address, requireContext(), GeoCoderHandler(this))
    }


    companion object {

        const val OFFER_ARG = "offer"

        private class GeoCoderHandler(private val fragment: AddEditOfferFragment) : Handler() {
            override fun handleMessage(message: Message) {
                val result: LatLng? = when (message.what) {
                    1 -> {
                        val bundle = message.data
                        bundle.getParcelable("latLng")
                    }
                    else -> null
                }
                Timber.tag("LatLng").d("LatLng - geocode fun called")
                result?.let {
                    fragment.viewModel.handleOfferCreation(result)
                } ?: run {
                    Toast.makeText(fragment.requireContext(), R.string.invalid_address, Toast.LENGTH_LONG).show()
                    fragment.viewModel.handleOfferCreation(LatLng(0.0, 0.0))
                }
            }
        }
    }
}
