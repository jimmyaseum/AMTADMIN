package com.app.amtadminapp.adapter.Report

import androidx.core.text.HtmlCompat
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
import com.app.amtadminapp.model.response.ConfirmedBookingReportModel
import kotlinx.android.synthetic.main.row_confirmed_hotel_voucher_report.view.*


import java.util.*

class ConfirmedBookingReportAdapter(private val mContext: Context, private val arrData: ArrayList<ConfirmedBookingReportModel>) : RecyclerView.Adapter<ConfirmedBookingReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_confirmed_booking_report, parent, false)
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
            model: ConfirmedBookingReportModel,
            mContext: Context
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.TourDateCode != null) {
                itemView.txtTourCode.text = model.TourDateCode
            }

            if(model.CityName != null) {
                itemView.txtCityName.text = model.CityName
            }

            if(model.CheckInDate != null) {
                itemView.txtCheckIn.text = model.CheckInDate
            }

            if(model.NoOfNights != null) {
                itemView.txtStay.text = model.NoOfNights
            }

            if(model.CheckOutDate != null) {
                itemView.txtCheckOut.text = model.CheckOutDate
            }

            if(model.TotalRooms != null) {
                itemView.txtConfirmedRooms.text = model.TotalRooms.toString()
            }

            if(model.TotalPax != null) {
                itemView.txtTotalPax.text = model.TotalPax.toString()
            }

            if(model.ExtraBed != null) {
                itemView.txtTotalExtraBed.text = model.ExtraBed.toString()
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

    /*fun convertHtmlToText(htmlText: String): String {
        if (htmlText.isNullOrEmpty()) {
            return ""
        }

        // Convert HTML to plain text
        return HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }*/

}