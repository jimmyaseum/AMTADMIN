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
import com.app.amtadminapp.model.response.BranchWiseTourBookingSummaryReportModel
import kotlinx.android.synthetic.main.row_b_w_t_b_s_report.view.*


import java.util.*

class BranchWiseTourBookingSummaryReportAdapter(private val mContext: Context, private val arrData: ArrayList<BranchWiseTourBookingSummaryReportModel>) : RecyclerView.Adapter<BranchWiseTourBookingSummaryReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_b_w_t_b_s_report, parent, false)
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
            model: BranchWiseTourBookingSummaryReportModel,
            mContext: Context
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.BranchName != null) {
                itemView.txtBranchName.text = model.BranchName
            }

            if(model.TotalBooking != null) {
                itemView.txtTotalBooking.text = model.TotalBooking.toString()
            }

            if(model.TotalPax != null) {
                itemView.txtTotalPax.text = model.TotalPax.toString()
            }

            if(model.ConfirmBooking != null) {
                itemView.txtConfirmBooking.text = model.ConfirmBooking.toString()
            }

            if(model.ConfirmPax != null) {
                itemView.txtConfirmPax.text = model.ConfirmPax.toString()
            }

            if(model.CancellBooking != null) {
                itemView.txtCancelBooking.text = model.CancellBooking.toString()
            }

            if(model.CancellPax != null) {
                itemView.txtCancelPax.text = model.CancellPax.toString()
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