package fr.mjoudar.realestatemanager.ui.addEditOffer

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.BuildConfig
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.databinding.FragmentAddEditOfferBinding
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.domain.models.Offer
import fr.mjoudar.realestatemanager.domain.models.Photo
import fr.mjoudar.realestatemanager.notification.NotificationHandler
import fr.mjoudar.realestatemanager.ui.adapters.AddEditOfferPicturesAdapter
import fr.mjoudar.realestatemanager.utils.compressImageFile
import fr.mjoudar.realestatemanager.utils.setUpPermissionsUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.util.*

private const val REQ_CAPTURE = 100
private const val RES_IMAGE = 100
private const val FILE_PROVIDER_NAME = ".fileprovider"

@AndroidEntryPoint
class AddEditOfferFragment : Fragment() {

    private var _binding: FragmentAddEditOfferBinding? = null
    private val binding get() = _binding!!
    private var argOffer = Offer("")
    lateinit var navController: NavController

    private val viewModel: AddEditOfferViewModel by viewModels()
    private var photos: List<Photo> = emptyList()
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
        retrievedArguments()
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
    }

    //TODO: Test----------------------------------------------------------------------------------------------------------------------------------------------------

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
        binding.sectionAddEditType.addEditOfferPropertyType.setAdapter(ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, viewModel.propertyTypeValues))
        binding.sectionAddEditType.addEditOfferOfferType.setAdapter(ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, viewModel.offerTypeValues))
    }

    private fun setPhotosAdapter() {
        // TODO: Set up delete listener
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Photo
            // TODO: Setup onDeletePicture() method
        }
        val onContextClickListener = View.OnContextClickListener { true }
        adapter = AddEditOfferPicturesAdapter(onClickListener, onContextClickListener)
        binding.sectionAddEditPictures.picturesRecyclerView.adapter = adapter
    }

    /***********************************************************************************************
     ** Observers
     ***********************************************************************************************/
    private fun setObservers() {
        setAgentListObserver()
        setPhotosObserver()
        setInputIncompleteObserver()
        setIsOfferSavedObserver()
        setIsAgentListEmptyObserver()

        viewModel.isOfferClosed.observe(requireActivity()) {
            Timber.tag("isOfferClosed_Test").d("viewModel.isOfferClosed = ${viewModel.isOfferClosed.value}")
            Timber.tag("isOfferClosed_Test").d("viewModel.offer.isOfferClosed = ${!(viewModel.offer?.availability)!!}")
        }
    }

    // Observes agentList to setup the agents' autocomplete adapter
    private fun setAgentListObserver() {
        viewModel.agentList.observe(viewLifecycleOwner) { it1 ->
            it1?.let {
                binding.sectionAddEditAgent.addEditOfferAgent.setAdapter(ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, it1))
                binding.sectionAddEditAgent.addEditOfferAgent.setOnItemClickListener { parent, _, position, _ ->
                    viewModel.agent.value = parent.getItemAtPosition(position) as Agent
                }
            }
        }
    }

    // Observes photos to setup the photos RecyclerView adapter
    private fun setPhotosObserver() {
        viewModel.photos.observe(viewLifecycleOwner) { it1 ->
            it1?.let { it2 ->
                photos = it2 // TODO : To remove ?
                adapter.setData(it2)
                binding.sectionAddEditPictures.picturesRecyclerView.adapter = adapter
                //TODO : update the recyclerView adapter
            }
        }
    }

    private fun setInputIncompleteObserver() {
        viewModel.inputIncomplete.observe(viewLifecycleOwner) {
            if (it) {
//                Snackbar.make(requireActivity().findViewById(R.id.root_layout), R.string.agent_input_incomplete, Snackbar.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), R.string.agent_input_incomplete, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setIsOfferSavedObserver() {
        viewModel.isOfferSaved.observe(viewLifecycleOwner) {
            if (it) {
                launchNotification()
                when (viewModel.isNewOffer) {
                    true -> clearBackStack()
                    false -> popBackStackWithArgument(viewModel.offer!!)
                }
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
     ** popBackStack handlers
     ***********************************************************************************************/
    private fun onBackPressedHandler() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                popBackStackWithArgument(argOffer)
            }
        })
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

    private fun clearBackStack() {
        Timber.tag("popBack").d("clearBackStack")
        val entry = parentFragmentManager.getBackStackEntryAt(0)
        parentFragmentManager.popBackStack(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun popBackStackWithArgument(data : Offer) {
        Timber.tag("popBack").d("popBackStackWithArgument")
        lifecycleScope.launchWhenResumed {
            navController.previousBackStackEntry?.savedStateHandle?.set(OFFER_ARG, data)
            navController.popBackStack()
            //val id = navController.previousBackStackEntry?.destination?.id
            //navController.popBackStack(id!!, true)
        }
    }

    /***********************************************************************************************
     ** Listeners
     ***********************************************************************************************/
    private fun setListener() {
        setDatePickersListeners()
        setupAddPictureListener()
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


    /***********************************************************************************************
     ** Utils
     ***********************************************************************************************/
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

    companion object {
        const val OFFER_ARG = "offer"
    }


    //TODO: Photo----------------------------------------------------------------------------------------------------------------------------------------------------

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
                submitPhotoToList(Photo(UUID.randomUUID().toString(),imageUri.toString(), "")) // TODO: To add description here
            }
        }
    }

    private fun submitPhotoToList(photo: Photo) {
        viewModel.photos.value!!.add(photo)
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




}