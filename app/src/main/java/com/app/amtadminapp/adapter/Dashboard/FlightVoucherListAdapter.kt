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
import com.app.amtadminapp.model.response.FlightVoucherListModel
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.visible
import kotlinx.android.synthetic.main.row_flight_voucher_list.view.*
import kotlinx.android.synthetic.main.row_flight_voucher_list.view.LL_More
import kotlinx.android.synthetic.main.row_flight_voucher_list.view.card
import kotlinx.android.synthetic.main.row_flight_voucher_list.view.txtBookBy
import kotlinx.android.synthetic.main.row_flight_voucher_list.view.txtBranch
import kotlinx.android.synthetic.main.row_flight_voucher_list.view.txtCompanyName
import kotlinx.android.synthetic.main.row_flight_voucher_list.view.txtReadLess
import kotlinx.android.synthetic.main.row_flight_voucher_list.view.txtReadMore
import java.util.*

class FlightVoucherListAdapter(private val mContext: Context, private val arrData: ArrayList<FlightVoucherListModel>) : RecyclerView.Adapter<FlightVoucherListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_flight_voucher_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrData[position], mContext)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(
            model: FlightVoucherListModel,
            mContext: Context
        ) {
            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.AirlineVoucherNo != null) {
                itemView.txtFlightVoucherNo.text = model.AirlineVoucherNo
            }
            if(model.TourBookingNo != null) {
                itemView.txtTourBookingNo.text = model.TourBookingNo
                itemView.txtTourBookingNo.setPaintFlags(itemView.txtTourBookingNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }

            if(model.CompanyName != null) {
                itemView.txtCompanyName.text = model.CompanyName
            }
            if(model.TicketPurchasedDate != null) {
                itemView.txtTicketPurchaseDate.text = model.TicketPurchasedDate
            }
            if(model.NoOfPax != null) {
                itemView.txtNoOfPax.text = model.NoOfPax.toString()
            }
            if(model.Journey != null) {
                itemView.txtJourney.text = model.Journey
            }
            if(model.TotalPrice != null) {
                itemView.txtTotalPrice.text = model.TotalPrice.toString()
            }
            if(model.BookBy != null) {
                itemView.txtBookBy.text = model.BookBy
            }
            if(model.Branch != null) {
                itemView.txtBranch.text = model.Branch
            }

            itemView.txtTourBookingNo.setOnClickListener {
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