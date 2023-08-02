package com.app.amtadminapp.adapter.voucher

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.AirlineVoucherModel
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.row_airline_voucher.view.*
import kotlinx.android.synthetic.main.row_airline_voucher.*
import java.util.*

class AirLineVoucherAdapter(private val mContext: Context, private val arrData: ArrayList<AirlineVoucherModel>,
                            val recyclerItemClickListener: RecyclerClickListener
) : RecyclerView.Adapter<AirLineVoucherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_airline_voucher, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrData[position],mContext, recyclerItemClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @RequiresApi(Build.VERSION_CODES.P)
        fun bindItems(
            model: AirlineVoucherModel,
            mContext: Context,
            recyclerItemClickListener: RecyclerClickListener
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.AirlineVoucherNo != null) {
                itemView.txtAVN.text = model.AirlineVoucherNo
            }

            if(model.TourBookingNo != null) {
                itemView.txtTourBookingNo.text = model.TourBookingNo
                itemView.txtTourBookingNo.setPaintFlags(itemView.txtTourBookingNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }

            if(model.Name != null) {
                itemView.txtName.text = model.Name
            }
            if(model.PNR != null) {
                itemView.txtPNR.text = model.PNR
            }
            if(model.TicketPurchasedDate != null) {
                itemView.txtPurchaseDate.text = model.TicketPurchasedDate
            }
            if(model.Journey != null) {
                itemView.txtJourneyType.text = model.Journey
            }

            if(model.NoOfPax != null) {
                itemView.txtNoOfPax.text = model.NoOfPax.toString()
            }
            if(model.TotalPrice != null) {
                itemView.txtTotalPrice.text = model.TotalPrice.toString()
            }
            if(model.Status != null) {
                itemView.txtStatus.text = model.Status
            }
            if(model.BookBy != null) {
                itemView.txtBookBy.text = model.BookBy
            }
            if(model.Branch != null) {
                itemView.txtBranch.text = model.Branch
            }

            itemView.txtTourBookingNo.setOnClickListener {
                val fullpath =  model.TourBookingLink + model.TourBookingNo
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

            if(model.IsCreatedFromApp!!) {
                itemView.llJourneyType.gone()
                itemView.llPNR.gone()

                itemView.llAirlineDocument.visible()
                itemView.cardEdit.visible()

                itemView.cardEdit.setOnClickListener {
                    recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1)
                }

                if(model.AirlineVoucherTicket != null && model.AirlineVoucherTicket != "") {
                    itemView.txtAirlineDocument.setOnClickListener {
                        if(isOnline(mContext)) {
                            if(model.AirlineVoucherTicket.contains(".pdf")) {
                                var format = "https://docs.google.com/gview?embedded=true&url=%s"
                                val fullPath: String = java.lang.String.format(Locale.ENGLISH, format, model.AirlineVoucherTicket)
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
                                mContext.startActivity(browserIntent)
                            } else {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.AirlineVoucherTicket))
                                mContext.startActivity(browserIntent)
                            }
                        } else {
                            mContext.toast(mContext.resources.getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
                        }
                    }
                } else {
                    itemView.txtAirlineDocument.gone()
                }

                if(model.DeparturePNRNo != null && model.DeparturePNRNo != "") {
                    itemView.llDeparturePNR.visible()
                    itemView.txtDeparturePNR.text = model.DeparturePNRNo
                } else {
                    itemView.llDeparturePNR.gone()
                }

                if(model.ArrivalPNRNo != null && model.ArrivalPNRNo != "") {
                    itemView.llReturnPNR.visible()
                    itemView.txtReturnPNR.text = model.ArrivalPNRNo
                } else {
                    itemView.llReturnPNR.gone()
                }

            } else {
                itemView.llJourneyType.visible()
                itemView.llPNR.visible()
                itemView.llDeparturePNR.gone()
                itemView.llReturnPNR.gone()
                itemView.llAirlineDocument.gone()
                itemView.cardEdit.gone()
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

}