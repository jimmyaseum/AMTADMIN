package com.app.amtadminapp.interFase

import android.view.View

/**
 * Created by Jimmy
 */
interface RecyclerClickListener {
    fun onItemClickEvent(view: View, position: Int, type: Int)
}