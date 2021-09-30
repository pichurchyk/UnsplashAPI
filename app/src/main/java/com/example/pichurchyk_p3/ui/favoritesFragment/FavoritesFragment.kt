package com.example.pichurchyk_p3.ui.favoritesFragment

import android.os.Bundle
import android.view.View
import com.example.pichurchyk_p3.R
import com.example.pichurchyk_p3.adapter.FavoritesPagerAdapter
import com.example.pichurchyk_p3.base.baseFragment.BaseFragment
import com.example.pichurchyk_p3.databinding.FragmentFavoritesBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {

    private lateinit var tabLayout: TabLayout

    override fun getFragmentView(): Int {
        return R.layout.fragment_favorites
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTabs()

        binding.favoritesTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                changeIconColor(
                    binding.favoritesTabLayout.getTabAt(tab.position)!!.icon!!,
                    R.color.blue
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                changeIconColor(
                    binding.favoritesTabLayout.getTabAt(tab.position)!!.icon!!,
                    R.color.gray
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun setUpTabs() {
        val adapter = FavoritesPagerAdapter(childFragmentManager)
        tabLayout = binding.favoritesTabLayout
        adapter.addFragment(FavoritesImagesFragment())
        adapter.addFragment(FavoritesQueriesFragment())
        binding.favoritesViewPager.adapter = adapter
        tabLayout.setupWithViewPager(binding.favoritesViewPager)

        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_image)
        changeIconColor(binding.favoritesTabLayout.getTabAt(0)!!.icon!!, R.color.blue)

        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_bookmark)
    }
}