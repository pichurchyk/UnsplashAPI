package com.example.pichurchyk_p3.base.baseFragment.util

import android.app.AlertDialog
import android.content.Context

fun showDialog(title: String, message: String, context: Context) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton("Admit", null)
    val dialog: AlertDialog = builder.create()
    dialog.show()
}
