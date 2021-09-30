package com.example.pichurchyk_p3.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.pichurchyk_p3.R
import com.example.pichurchyk_p3.base.baseFragment.BaseFragment
import com.example.pichurchyk_p3.databinding.FragmentBrowserBinding


@SuppressLint("SetJavaScriptEnabled")
class BrowserFragment : BaseFragment<FragmentBrowserBinding>() {

    private val args by navArgs<BrowserFragmentArgs>()

    override fun getFragmentView(): Int {
        return R.layout.fragment_browser
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.loadUrl(args.url)
    }
}