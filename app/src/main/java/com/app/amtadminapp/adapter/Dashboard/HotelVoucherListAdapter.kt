package com.app.amtadminapp.adapter.Dashboard

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
import com.app.amtadminapp.model.response.HotelVoucherListModel
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.visible
import kotlinx.android.synthetic.main.row_hotel_voucher_list.view.*
import kotlinx.android.synthetic.main.row_hotel_voucher_list.view.LL_More
import kotlinx.android.synthetic.main.row_hotel_voucher_list.view.card
import kotlinx.android.synthetic.main.row_hotel_voucher_list.view.rvbookingNo
import kotlinx.android.synthetic.main.row_hotel_voucher_list.view.txtBookBy
import kotlinx.android.synthetic.main.row_hotel_voucher_list.view.txtBranch
import kotlinx.android.synthetic.main.row_hotel_voucher_list.view.txtCompanyName
import kotlinx.android.synthetic.main.row_hotel_voucher_list.view.txtReadLess
import kotlinx.android.synthetic.main.row_hotel_voucher_list.view.txtReadMore
import java.util.*

class HotelVoucherListAdapter(private val mContext: Context, private val arrData: ArrayList<HotelVoucherListModel>) : RecyclerView.Adapter<HotelVoucherListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_hotel_voucher_list, parent, false)
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
            model: HotelVoucherListModel,
            mContext: Context
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.HotelVoucher != null) {
                itemView.txtHotelVoucher.text = model.HotelVoucher
                itemView.txtHotelVoucher.setPaintFlags(itemView.txtHotelVoucher.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }

            if(model.TourName != null) {
                itemView.txtTourName.text = model.TourName
            }
            if(model.TourDate != null) {
                itemView.txtTourDate.text = model.TourDate
            }
            if(model.CompanyName != null) {
                itemView.txtCompanyName.text = model.CompanyName
            }
            if(model.NoOfNights != null) {
                itemView.txtNoOfNights.text = model.NoOfNights.toString()
            }
            if(model.CompanyName != null) {
                itemView.txtVehicleSharing.text = model.CompanyName
            }

            if(model.BookBy != null) {
                itemView.txtBookBy.text = model.BookBy
            }
            if(model.Branch != null) {
                itemView.txtBranch.text = model.Branch
            }

            itemView.txtHotelVoucher.setOnClickListener {
                val fullpath =  model.HotelVoucherLink + model.HotelVoucher
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

            var arrBookingList: ArrayList<String> = ArrayList()

            val splitList2 = model.TourBookingNo!!.split(",")
            for (j in splitList2.indices) {
                arrBookingList.add(splitList2[j])
            }
            if(arrBookingList.size > 0) {
                var adapter = BookingNoAdapter(mContext, arrBookingList)
                itemView.rvbookingNo.adapter = adapter
                adapter.setRecyclerRowClick(object : RecyclerClickListener {
                    override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                        val fullpath = model.TourbookingLink + arrBookingList[pos]
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullpath))
                        mContext.startActivity(browserIntent)
                    }
                })
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