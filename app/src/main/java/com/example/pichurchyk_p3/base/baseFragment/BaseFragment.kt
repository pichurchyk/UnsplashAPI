package com.example.pichurchyk_p3.base.baseFragment

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.pichurchyk_p3.R
import com.example.pichurchyk_p3.base.baseFragment.util.SnackBar
import com.example.pichurchyk_p3.base.baseFragment.util.showDialog
import com.example.pichurchyk_p3.base.baseFragment.util.toast
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getFragmentView(), container, false)
        return binding.root
    }

    fun toast(message: String) {
        requireContext().toast(message)
    }

    fun dialog(title: String, message: String, context: Context) {
        showDialog(title, message, context)
    }

    fun snackBar(text: String) {
        SnackBar(binding.root ,text)
    }

    fun menuVisibility(isVisible: Boolean) {
        if (isVisible) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_menu).visibility =
                View.VISIBLE
        } else {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_menu).visibility =
                View.GONE
        }
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun changeIconColor(icon: Drawable ,color: Int) {
        DrawableCompat.setTint(
            icon,
            ContextCompat.getColor(
                binding.root.context,
                color
            )
        )
    }

    abstract fun getFragmentView(): Int

}
