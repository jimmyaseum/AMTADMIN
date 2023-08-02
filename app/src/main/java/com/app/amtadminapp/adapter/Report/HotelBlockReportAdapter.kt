package com.app.amtadminapp.adapter.Report

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.model.response.HotelBlockListModel
import kotlinx.android.synthetic.main.row_hotel_block_report.view.*
import kotlinx.android.synthetic.main.row_hotel_block_report.*


import java.util.*

class HotelBlockReportAdapter(private val mContext: Context, private val arrData: ArrayList<HotelBlockListModel>) : RecyclerView.Adapter<HotelBlockReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_hotel_block_report, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrData[position],mContext)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @RequiresApi(Build.VERSION_CODES.P)
        fun bindItems(
            model: HotelBlockListModel,
            mContext: Context
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.TourDateCode != null) {
                itemView.txtTourCode.text = model.TourDateCode
            }
            if(model.CheckInDate != null) {
                itemView.txtCheckIn.text = model.CheckInDate
            }
            if(model.TotalNights != null) {
                itemView.txtStay.text = model.TotalNights
            }
            if(model.CheckOutDate != null) {
                itemView.txtCheckOut.text = model.CheckOutDate.toString()
            }
            if(model.NoOfRooms != null) {
                itemView.txtTentativeRooms.text = model.NoOfRooms
            }

            if(model.ConfirmedRooms != null) {
                itemView.txtConfirmedRooms.text = model.ConfirmedRooms
            }

            val reminder = position % 3
            itemView.card.setBackgroundDrawable(ContextCompat.getDrawable(mContext, colors[reminder]))
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

}