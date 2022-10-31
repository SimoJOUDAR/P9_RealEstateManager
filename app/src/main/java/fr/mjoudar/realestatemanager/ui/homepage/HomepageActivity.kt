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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.databinding.ActivityHomepageBinding
import fr.mjoudar.realestatemanager.domain.models.Agent
import fr.mjoudar.realestatemanager.ui.addEditAgent.AddEditAgentFragment.Companion.AGENT_ARG
import fr.mjoudar.realestatemanager.ui.addEditOffer.AddEditOfferFragment.Companion.OFFER_ARG
import fr.mjoudar.realestatemanager.utils.DataState
import timber.log.Timber
import java.util.*
import kotlin.concurrent.schedule

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

    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment }
    private val navController by lazy { navHostFragment.navController }
    private val appBarConfiguration by lazy { AppBarConfiguration(navController.graph) }

    /**********************************************************************************************
     ** Core functions
     **********************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomepageBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        setupDemoData()
        setObserver()
        initGraphics()
        setupButtonsListeners()
        setupDestinationChangedListener()
    }

    override fun onResume() {
        super.onResume()
        setupData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /**********************************************************************************************
     ** Initializers
     **********************************************************************************************/
    // Initializes the animations
    private fun initGraphics() {
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
        binding.fabAddAgent.setOnClickListener { addEditAgent()}
        binding.fabAddOffer.setOnClickListener { addEditOffer() }
        binding.bottomBtnAdd.setOnClickListener {
            when (isFabVisible) {
                false -> startFabOpeningAnimation()
                true -> startFabClosingAnimation()
            }
        }
        binding.fabBackgroundLayout.setOnClickListener {
            startFabClosingAnimation()
        }
    }

    private fun sorting() {
        navController.navigate(R.id.sortingDialogFragment)
    }

    private fun applyFilter() {
        navController.navigate(R.id.filterDialogFragment)
    }

    private fun navigateToLoanSimulator() {
        navController.navigate(R.id.loadSimulatorFragment)
    }

    private fun switchCurrency() {
        homepageViewModel.toggleCurrency()
        setCurrencyButtonIcon()
        saveUpData()
    }

    private fun clearFilter() {
        when (homepageViewModel.dataFiltered) {
            true -> {
                homepageViewModel.fetchOffers()
                Toast.makeText(this, R.string.filter_turned_off, Toast.LENGTH_LONG).show()
            }
            false -> Toast.makeText(this, R.string.filter_already_off, Toast.LENGTH_LONG).show()
        }
    }

    private fun addEditAgent() {
        val bundle = Bundle()
        bundle.putParcelable(AGENT_ARG, null)
        navController.navigate(R.id.addEditAgentFragment, bundle)
    }

    private fun addEditOffer() {
        val bundle = Bundle()
        bundle.putParcelable(OFFER_ARG, null)
        navController.navigate(R.id.addEditOfferFragment, bundle)
    }

    // Sets the fab behavior for Homepage - To deploy the fab
    private fun startFabOpeningAnimation() {
        binding.fabAddAgent.startAnimation(fabOpen)
        binding.fabAddOffer.startAnimation(fabOpen)
        showFab()

    }

    // Sets the fab behavior for Homepage - To close the fab
    private fun startFabClosingAnimation() {
        binding.fabAddAgent.startAnimation(fabClose)
        binding.fabAddOffer.startAnimation(fabClose)
        hideFab()
    }

    private fun showFab() {
        binding.fabAddAgent.isClickable = true
        binding.fabAddOffer.isClickable = true
        binding.fabAddAgent.alpha = 1F
        binding.fabAddOffer.alpha = 1F
        binding.fabBackgroundLayout.alpha = 0.5F
        binding.fabContainer.visibility = View.VISIBLE
        binding.fabBackgroundLayout.visibility = View.VISIBLE
        isFabVisible = true
    }

    private fun hideFab() {
        binding.fabAddAgent.isClickable = false
        binding.fabAddOffer.isClickable = false
        binding.fabBackgroundLayout.alpha = 0F
        binding.fabBackgroundLayout.visibility = View.GONE
        isFabVisible = false
        Timer().schedule(800) {
            runOnUiThread {
                binding.fabAddAgent.alpha = 0F
                binding.fabAddOffer.alpha = 0F
                binding.fabContainer.visibility = View.GONE
            }
        }
    }

    private fun setCurrencyButtonIcon() {
        if (homepageViewModel.isCurrencyEuro.value!!) binding.bottomBtnCurrencyConverter.setImageResource(R.drawable.ic_currency_converter_on)
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
        startFabClosingAnimation()
        binding.bottomAppBar.visibility = View.GONE
        binding.bottomAppBar.performHide()
    }
    /**********************************************************************************************
     ** Data observers
     **********************************************************************************************/
    // Observers homepageViewModel' states
    private fun setObserver() {
        lifecycleScope.launchWhenStarted {
            val value = homepageViewModel.agentsState
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
     ** SharedPreferences
     **********************************************************************************************/

    private fun setupData() {
        val sharedPreference =  getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        homepageViewModel.isCurrencyEuro.value = sharedPreference.getBoolean("isCurrencyEuro", false)
        setCurrencyButtonIcon()
    }

    private fun saveUpData() {
        val sharedPreference =  getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean("isCurrencyEuro", homepageViewModel.isCurrencyEuro.value!!)
        editor.commit()
    }

    /**********************************************************************************************
     ** Demo Data
     **********************************************************************************************/
    private fun setupDemoData() {
        val sharedPreference =  getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val isDemoDataSetup = sharedPreference.getBoolean("isDemoDataSetup", false)
        if (!isDemoDataSetup) {
            homepageViewModel.generateDemoData()
            val editor = sharedPreference.edit()
            editor.putBoolean("isDemoDataSetup", true)
            editor.commit()
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