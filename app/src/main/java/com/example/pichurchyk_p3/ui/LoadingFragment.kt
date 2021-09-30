package com.example.pichurchyk_p3.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.example.pichurchyk_p3.R
import com.example.pichurchyk_p3.base.baseFragment.BaseFragment
import com.example.pichurchyk_p3.databinding.FragmentLoadingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingFragment : BaseFragment<FragmentLoadingBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            delay(5000)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_loadingFragment_to_wallpapersFeedFragment)
        }
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_loading
    }
}