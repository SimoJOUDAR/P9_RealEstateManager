package fr.mjoudar.realestatemanager.ui.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.databinding.ActivityHomepageBinding
import fr.mjoudar.realestatemanager.databinding.FragmentMainViewpagerBinding
import fr.mjoudar.realestatemanager.ui.adapters.MainViewpagerAdapter
import fr.mjoudar.realestatemanager.ui.list.ListViewFragment
import fr.mjoudar.realestatemanager.ui.map.MapViewFragment


class MainViewpagerFragment : Fragment() {


    private var _binding: FragmentMainViewpagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MainViewpagerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainViewpagerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewpager()
    }

    private fun setViewpager() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set("key", true)
        val fragmentList = arrayListOf(ListViewFragment(), MapViewFragment())
        adapter = MainViewpagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = " List"
                    tab.setIcon(R.drawable.ic_list_24)
                }
                1 -> {
                    tab.text = "Map"
                    tab.setIcon(R.drawable.ic_map_24)
                }
            }
        }.attach()
    }
}