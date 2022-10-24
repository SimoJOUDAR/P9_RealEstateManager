package fr.mjoudar.realestatemanager.ui.homepage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.databinding.ActivityHomepageBinding
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.repositories.AgentRepository
import fr.mjoudar.realestatemanager.repositories.OfferRepository
import fr.mjoudar.realestatemanager.utils.DataState
import fr.mjoudar.realestatemanager.utils.DatabaseSampleDataGenerator
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomepageActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private var _binding: ActivityHomepageBinding? = null
    private val binding get() = _binding!!

    //Fab Animation
    private var fabOpen: Animation? = null
    private var fabClose: Animation? = null
    private var isFabVisible = false

    private val homepageViewModel: HomepageViewModel by viewModels()
    private var agentList: List<Agent>? = arrayListOf()
    private var isEuroCurrency = false

    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment }
    private val navController by lazy { navHostFragment.navController }
    private val appBarConfiguration by lazy { AppBarConfiguration(navController.graph) }

    //TODO: Test------------------------------------------------------------------------------------
    @Inject lateinit var agentRepo : AgentRepository
    @Inject lateinit var offerRepo : OfferRepository
    val dataSample = DatabaseSampleDataGenerator()
    //TODO: Test------------------------------------------------------------------------------------

    /**********************************************************************************************
     ** Core functions
     **********************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomepageBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        hideStatusBar()
        setObserver()
        initAnimation()
        setupButtonsListeners() //TODO: To include in addOnDestinationChangedListener()
        setupDemoData() //TODO: Test <<-----------------------------------------
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /**********************************************************************************************
     ** Initializers
     **********************************************************************************************/
    private fun hideStatusBar() {
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }
    // Initializes the animations
    private fun initAnimation() {
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim)
        fabClose = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim)
    }

    /**********************************************************************************************
     ** Button listeners
     **********************************************************************************************/
    private fun setupButtonsListeners() {
        binding.bottomBtnListSorting.setOnClickListener { sorting() }
        binding.bottomBtnListFilter.setOnClickListener { applyFilter() }
        binding.bottomBtnLoanSimulator.setOnClickListener { navigateToLoanSimulator() }
        binding.bottomBtnCurrencyConverter.setOnClickListener { switchCurrency() }
        binding.bottomBtnListFilterOff.setOnClickListener { clearFilter() }
        binding.bottomBtnAdd.setOnClickListener {
            when (isFabVisible) {
                false -> openFab()
                true -> closeFab()
            }
        }
        binding.fabBackgroundLayout.setOnClickListener {
            closeFab()
        }
        setupDestinationChangedListener()
    }

    private fun sorting() {
        //TODO: to implement
    }

    private fun applyFilter() {
        //TODO: to implement
    }

    private fun navigateToLoanSimulator() {
        //TODO: to implement
    }

    private fun switchCurrency() {
        //TODO: to implement
        toggleCurrencyButtonIcon()
    }

    private fun clearFilter() {
        //TODO: to implement
    }

    // Sets the fab behavior for Homepage - To deploy the fab
    private fun openFab() {
        binding.fabAddAgent.startAnimation(fabOpen)
        binding.fabAddOffer.startAnimation(fabOpen)
        binding.fabAddAgent.isClickable = true
        binding.fabAddOffer.isClickable = true
        binding.fabBackgroundLayout.visibility = View.VISIBLE
        binding.fabBackgroundLayout.alpha = 0.5F
        isFabVisible = true
    }

    // Sets the fab behavior for Homepage - To close the fab
    private fun closeFab() {
        binding.fabAddAgent.startAnimation(fabClose)
        binding.fabAddOffer.startAnimation(fabClose)
        binding.fabAddAgent.isClickable = false
        binding.fabAddOffer.isClickable = false
        binding.fabBackgroundLayout.alpha = 0F
        binding.fabBackgroundLayout.visibility = View.GONE
        isFabVisible = false
    }


    private fun toggleCurrencyButtonIcon() {
        homepageViewModel.toggleCurrency()
        if (isEuroCurrency) binding.bottomBtnCurrencyConverter.setImageResource(R.drawable.ic_currency_converter_on)
        else binding.bottomBtnCurrencyConverter.setImageResource(R.drawable.ic_currency_converter_off)
    }

    /**********************************************************************************************
     ** Navigation
     **********************************************************************************************/

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when(destination.id) {
            R.id.mainViewpagerFragment -> setBottomAppBarForHome()
            else -> removeBottomAppBar()
        }
    }

    private fun setupDestinationChangedListener() {
        binding.run {
            navController.addOnDestinationChangedListener(
                this@HomepageActivity
            )
        }
    }

    private fun setBottomAppBarForHome() {
        binding.bottomAppBar.visibility = View.VISIBLE
        binding.bottomAppBar.performShow()
    }

    private fun removeBottomAppBar() {
        closeFab()
        binding.bottomAppBar.visibility = View.GONE
        binding.bottomAppBar.performHide()
    }
    /**********************************************************************************************
     ** Data observers
     **********************************************************************************************/
    // Observers homepageViewModel' states
    private fun setObserver() {
        lifecycleScope.launchWhenStarted {
            val value = homepageViewModel.state
            value.collect {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        it.data?.let { agents -> renderAgentList(agents) }
                        displayLoading(false)
                    }
                    DataState.Status.LOADING -> {
                        displayLoading(false)
                    }
                    DataState.Status.ERROR -> {
                        displayLoading(false)
                        displayError(it.message)
                        Timber.d("LIST_OBSERVER: ${it.message}")
                    }
                }
            }
        }
        homepageViewModel.isEuroCurrency.observe(this@HomepageActivity) {
            this.isEuroCurrency = it
        }
    }

    // Checks the agents' list at reception (null or notnull)
    private fun renderAgentList(agents: List<Agent>) {
        agentList = agents
        binding.fabAddOffer.isClickable = agents.isNotEmpty()
    }

    // Handles the loading progressBar display
    private fun displayLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
    /**********************************************************************************************
     ** Demo Data
     **********************************************************************************************/
    private fun setupDemoData() {
        val sharedPreference =  getSharedPreferences("DemoData", Context.MODE_PRIVATE)
        val isDemoDataSetup = sharedPreference.getBoolean("isDemoDataSetup", false)
        if (!isDemoDataSetup) {
            generateDemoData()
            val editor = sharedPreference.edit()
            editor.putBoolean("isDemoDataSetup", true)
            editor.commit()
        }

    }

    private fun generateDemoData() {
        lifecycleScope.launchWhenCreated {
            agentRepo.saveAgent(dataSample.agent1)
            agentRepo.saveAgent(dataSample.agent2)
            agentRepo.saveAgent(dataSample.agent3)
            offerRepo.saveOffer(dataSample.offer1)
            offerRepo.saveOffer(dataSample.offer2)
            offerRepo.saveOffer(dataSample.offer3)
            offerRepo.saveOffer(dataSample.offer4)
            offerRepo.saveOffer(dataSample.offer5)
            offerRepo.saveOffer(dataSample.offer6)
            offerRepo.saveOffer(dataSample.offer7)
            offerRepo.saveOffer(dataSample.offer8)
            offerRepo.saveOffer(dataSample.offer9)
            offerRepo.saveOffer(dataSample.offer10)
            offerRepo.saveOffer(dataSample.offer11)
            offerRepo.saveOffer(dataSample.offer12)
        }
    }

    /**********************************************************************************************
     ** Error handling
     **********************************************************************************************/
    private fun displayError(message: String?) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show()
        }
    }

}