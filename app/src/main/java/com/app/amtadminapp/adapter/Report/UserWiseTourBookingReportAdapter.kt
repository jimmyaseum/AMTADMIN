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
import com.app.amtadminapp.model.response.UserWiseTourBookingReportModel
import kotlinx.android.synthetic.main.row_user_wise_tour_booking_report.view.*


import java.util.*

class UserWiseTourBookingReportAdapter(private val mContext: Context, private val arrData: ArrayList<UserWiseTourBookingReportModel>) : RecyclerView.Adapter<UserWiseTourBookingReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_user_wise_tour_booking_report, parent, false)
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
            model: UserWiseTourBookingReportModel,
            mContext: Context
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.TourBookingNo != null) {
                itemView.txtTourBookingNo.text = model.TourBookingNo
            }

            if(model.TourName != null) {
                itemView.txtTourName.text = model.TourName
            }

            if(model.CustomerName != null) {
                itemView.txtCustomerName.text = model.CustomerName
            }

            if(model.MobileNo != null) {
                itemView.txtMobileNo.text = model.MobileNo
            }

            if(model.TourStartDate != null) {
                itemView.txtTourStartDate.text = model.TourStartDate
            }

            if(model.NoOfNights != null) {
                itemView.txtNoOfNights.text = model.NoOfNights
            }

            if(model.BookBy != null) {
                itemView.txtBookBy.text = model.BookBy
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