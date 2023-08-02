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
import com.app.amtadminapp.model.response.VehicleAllotmentReportModel
import kotlinx.android.synthetic.main.row_vehicel_allotment_report.view.*


import java.util.*

class VehicleAllotmentReportAdapter(private val mContext: Context, private val arrData: ArrayList<VehicleAllotmentReportModel>) : RecyclerView.Adapter<VehicleAllotmentReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_vehicel_allotment_report, parent, false)
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
            model: VehicleAllotmentReportModel,
            mContext: Context
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.BookingNo != null && model.BookingNo!!.isNotEmpty()) {
                itemView.txtBookingNo.text = model.BookingNo
                itemView.llAge.visibility = View.VISIBLE
            }
            else{
                itemView.llBookingNo.visibility = View.GONE
            }

            if(model.Name != null && model.Name!!.isNotEmpty()) {
                itemView.txtName.text = model.Name
                itemView.llAge.visibility = View.VISIBLE
            }
            else{
                itemView.llName.visibility = View.GONE
            }

            if(model.Age != null) {
                itemView.txtAge.text = model.Age.toString()
                itemView.llAge.visibility = View.VISIBLE
            }
            else{
                itemView.llAge.visibility = View.GONE
            }

            if(model.HotelVoucherNo != null && model.HotelVoucherNo!!.isNotEmpty()) {
                itemView.txtHVN.text = model.HotelVoucherNo
                itemView.llHVN.visibility = View.VISIBLE
            }
            else{
                itemView.llHVN.visibility = View.GONE
            }

            if(model.MobileNo != null && model.MobileNo!!.isNotEmpty()) {
                itemView.txtMobileNo.text = model.MobileNo
                itemView.llMobileNo.visibility = View.VISIBLE
            }
            else{
                itemView.llMobileNo.visibility = View.GONE
            }

            if(model.RouteVoucherNo != null && model.RouteVoucherNo!!.isNotEmpty()) {
                itemView.txtRVN.text = model.RouteVoucherNo
                itemView.llRVN.visibility = View.VISIBLE
            }
            else{
                itemView.llRVN.visibility = View.GONE
            }

            if(model.Gender != null && model.Gender!!.isNotEmpty()) {
                itemView.txtGender.text = model.Gender
                itemView.llGender.visibility = View.VISIBLE
            }
            else{
                itemView.llGender.visibility = View.GONE
            }

            if(model.Remarks != null && model.Remarks!!.isNotEmpty()) {
                itemView.txtRemark.text = model.Remarks
                itemView.llRemarks.visibility = View.VISIBLE
            }
            else{
                itemView.llRemarks.visibility = View.GONE
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