package com.app.amtadminapp.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import com.app.amtadminapp.R

class MyProgressDialog(context: Context) : Dialog(context, R.style.FullScreenDialogStyle) {
    init {
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = wlmp
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null)
        setContentView(view)
    }
}