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
import com.app.amtadminapp.model.response.PendingPaymentReportModel
import com.app.amtadminapp.model.response.PendingRouteVoucherReportModel
import kotlinx.android.synthetic.main.row_pending_payment_report.view.*
import kotlinx.android.synthetic.main.row_pending_payment_report.view.card
import kotlinx.android.synthetic.main.row_pending_payment_report.view.txtBookingNo
import kotlinx.android.synthetic.main.row_pending_payment_report.view.txtName
import kotlinx.android.synthetic.main.row_pending_payment_report.view.txtTourCode
import kotlinx.android.synthetic.main.row_pending_route_voucher_report.view.*


import java.util.*

class PendingRouteVoucherReportAdapter(private val mContext: Context, private val arrData: ArrayList<PendingRouteVoucherReportModel>) : RecyclerView.Adapter<PendingRouteVoucherReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_pending_route_voucher_report, parent, false)
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
            model: PendingRouteVoucherReportModel,
            mContext: Context
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.PBN != null) {
                itemView.txtBookingNo.text = model.PBN
            }

            if(model.TourName != null) {
                itemView.txtTourName.text = model.TourName
            }

            if(model.Name != null) {
                itemView.txtName.text = model.Name
            }

            if(model.TourDateCode != null) {
                itemView.txtTourCode.text = model.TourDateCode
            }

            if(model.NoOfNights != 0 && model.NoOfNights != null) {
                itemView.txtNoOfNights.text = model.NoOfNights.toString()
            }

            if(model.TourStartDate != null) {
                itemView.txtTourStartDate.text = model.TourStartDate
            }

            if(model.TourEndDate != null) {
                itemView.txtTourEndDate.text = model.TourEndDate
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