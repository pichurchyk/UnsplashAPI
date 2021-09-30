package com.example.pichurchyk_p3.base.baseFragment.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun SnackBar(view: View, text: String) {
    val snack = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
    snack.show()
}