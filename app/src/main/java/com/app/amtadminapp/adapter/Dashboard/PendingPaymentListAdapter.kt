package com.app.amtadminapp.adapter.Dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.model.response.PendingPaymentListModel
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.visible
import kotlinx.android.synthetic.main.row_pending_payment_list.view.*
import kotlinx.android.synthetic.main.row_pending_payment_list.view.LL_More
import kotlinx.android.synthetic.main.row_pending_payment_list.view.card
import kotlinx.android.synthetic.main.row_pending_payment_list.view.txtName
import kotlinx.android.synthetic.main.row_pending_payment_list.view.txtReadLess
import kotlinx.android.synthetic.main.row_pending_payment_list.view.txtReadMore
import kotlinx.android.synthetic.main.row_pending_payment_list.view.txtSector
import kotlinx.android.synthetic.main.row_pending_payment_list.view.txtTour
import kotlinx.android.synthetic.main.row_pending_payment_list.view.txtTourBokingNo
import java.util.*

class PendingPaymentListAdapter(private val mContext: Context, private val arrData: ArrayList<PendingPaymentListModel>) : RecyclerView.Adapter<PendingPaymentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_pending_payment_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrData[position],mContext)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            model: PendingPaymentListModel,
            mContext: Context
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.TourBookingNo != null) {
                itemView.txtTourBokingNo.text = model.TourBookingNo
                itemView.txtTourBokingNo.setPaintFlags(itemView.txtTourBokingNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }

            if(model.OLDTourBookingNo != null) {
                itemView.txtOldTourBokingNo.text = model.OLDTourBookingNo
            }
            if(model.MobileNo != null) {
                itemView.txtMobileNo.text = model.MobileNo
            }
            if(model.Sector != null) {
                itemView.txtSector.text = model.Sector
            }
            if(model.TravelType != null) {
                itemView.txtTravelType.text = model.TravelType
            }
            if(model.Tour != null) {
                itemView.txtTour.text = model.Tour
            }
            if(model.TourDateCode != null) {
                itemView.txtTourDateCode.text = model.TourDateCode
            }
            if(model.TourStartDate != null) {
                itemView.txtTourStartDate.text = model.TourStartDate
            }
            if(model.Name != null) {
                itemView.txtName.text = model.Name
            }
            if(model.NoOfNights != null) {
                itemView.txtNoOfNights.text = model.NoOfNights.toString()
            }
            if(model.TotalAmount != null) {
                itemView.txtTotalAmount.text = model.TotalAmount
            }
            if(model.PendingAmount != null) {
                itemView.txtPendingAmount.text = model.PendingAmount
            }

            itemView.txtTourBokingNo.setOnClickListener {
                val fullpath =  model.TourbookingLink + model.TourBookingNo
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullpath))
                mContext.startActivity(browserIntent)
            }

            itemView.txtReadMore.setOnClickListener {
                    itemView.LL_More.visible()
                    itemView.txtReadMore.gone()
            }
            itemView.txtReadLess.setOnClickListener {
                itemView.LL_More.gone()
                itemView.txtReadMore.visible()
            }
            itemView.txtMobileNo.setOnClickListener {
                if (!model.MobileNo.isNullOrEmpty()) {

                    if (!model.MobileNo.isNullOrEmpty()) {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${model.MobileNo}")
                        mContext.startActivity(intent)
                    }
                }
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