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
import kotlinx.android.synthetic.main.row_pending_payment_report.view.*


import java.util.*

class PendingPaymentReportAdapter(private val mContext: Context, private val arrData: ArrayList<PendingPaymentReportModel>) : RecyclerView.Adapter<PendingPaymentReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_pending_payment_report, parent, false)
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
            model: PendingPaymentReportModel,
            mContext: Context
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.TourBookingNo != null) {
                itemView.txtBookingNo.text = model.TourBookingNo
            }
            if(model.Name != null) {
                itemView.txtName.text = model.Name
            }
            if(model.MobileNo != null) {
                itemView.txtMobileNo.text = model.MobileNo
            }
            if(model.TourCode != null) {
                itemView.txtTourCode.text = model.TourCode
            }
            if(model.StartDate != null) {
                itemView.txtStartDate.text = model.StartDate
            }

            if(model.RemainAmount != null) {
                itemView.txtRemainAmount.text = model.RemainAmount
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