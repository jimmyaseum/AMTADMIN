package com.app.amtadminapp.interFase

import android.view.View

/**
 * Created by YashKapasi on 16/11/19.
 */
interface RecyclerMultipleItemClickListener {
        fun onItemClickEvent(view: View, position: Int, type: Int, year:String)

}