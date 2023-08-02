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
import com.app.amtadminapp.model.response.SelfPendingPaymentReportModel
import kotlinx.android.synthetic.main.row_self_pending_payment_report.view.*
import java.util.*

class SelfPendingPaymentReportAdapter(private val mContext: Context, private val arrData: ArrayList<SelfPendingPaymentReportModel>) : RecyclerView.Adapter<SelfPendingPaymentReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_self_pending_payment_report, parent, false)
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
            model: SelfPendingPaymentReportModel,
            mContext: Context
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.TourBookingNo != null && model.TourBookingNo!!.isNotEmpty()) {
                itemView.txtBookingNo.text = model.TourBookingNo
            }

            if(model.Name != null && model.Name!!.isNotEmpty()) {
                itemView.txtName.text = model.Name
            }

            if(model.MobileNo != null && model.MobileNo!!.isNotEmpty()) {
                itemView.txtMobileNo.text = model.MobileNo
            }

            if(model.TourCode != null) {
                itemView.txtTourCode.text = model.TourCode.toString()
            }

            if(model.StartDate != null && model.StartDate!!.isNotEmpty()) {
                itemView.txtStartDate.text = model.StartDate
            }

            if(model.RemainAmount != null && model.RemainAmount!!.isNotEmpty()) {
                itemView.txtRemainAmount.text = model.RemainAmount
            }

            if(model.BookBy != null && model.BookBy!!.isNotEmpty()) {
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