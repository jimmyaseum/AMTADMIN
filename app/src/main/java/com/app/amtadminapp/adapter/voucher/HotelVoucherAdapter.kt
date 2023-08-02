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
import com.app.amtadminapp.adapter.Dashboard.BookingNoAdapter
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.HotelVoucherModel
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.visible
import kotlinx.android.synthetic.main.row_hotel_voucher.view.*
import kotlinx.android.synthetic.main.row_hotel_voucher.view.LL_More
import kotlinx.android.synthetic.main.row_hotel_voucher.view.card
import kotlinx.android.synthetic.main.row_hotel_voucher.view.cardEdit
import kotlinx.android.synthetic.main.row_hotel_voucher.view.rvbookingNo
import kotlinx.android.synthetic.main.row_hotel_voucher.view.txtBookBy
import kotlinx.android.synthetic.main.row_hotel_voucher.view.txtBranch
import kotlinx.android.synthetic.main.row_hotel_voucher.view.txtCompanyName
import kotlinx.android.synthetic.main.row_hotel_voucher.view.txtName
import kotlinx.android.synthetic.main.row_hotel_voucher.view.txtNoOfNights
import kotlinx.android.synthetic.main.row_hotel_voucher.view.txtReadLess
import kotlinx.android.synthetic.main.row_hotel_voucher.view.txtReadMore
import kotlinx.android.synthetic.main.row_hotel_voucher.view.txtTourDate
import kotlinx.android.synthetic.main.row_hotel_voucher.view.txtTourDateCode
import kotlinx.android.synthetic.main.row_hotel_voucher.view.txtVehicleSharing
import java.util.*

class HotelVoucherAdapter(private val mContext: Context, private val arrData: ArrayList<HotelVoucherModel>,
                          val recyclerItemClickListener: RecyclerClickListener
) : RecyclerView.Adapter<HotelVoucherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_hotel_voucher, parent, false)
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
            model: HotelVoucherModel,
            mContext: Context,
            recyclerItemClickListener: RecyclerClickListener
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.HotelVoucher != null) {
                itemView.txtHotelBookingNo.text = model.HotelVoucher
                itemView.txtHotelBookingNo.setPaintFlags(itemView.txtHotelBookingNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }

            if(model.Name != null) {
                itemView.txtName.text = model.Name
            }
            if(model.TourName != null) {
                itemView.txtPackageName.text = model.TourName
            }
            if(model.TourDateCode != null) {
                itemView.txtTourDateCode.text = model.TourDateCode
            }
            if(model.NoOfNights != null) {
                itemView.txtNoOfNights.text = model.NoOfNights.toString()
            }

            if(model.TourDate != null) {
                itemView.txtTourDate.text = model.TourDate
            }
            if(model.CompanyName != null) {
                itemView.txtCompanyName.text = model.CompanyName
            }
            if(model.VehicleSharingPax != null) {
                itemView.txtVehicleSharing.text = model.VehicleSharingPax
            }
            if(model.BookByName != null) {
                itemView.txtBookBy.text = model.BookByName
            }
            if(model.Branch != null) {
                itemView.txtBranch.text = model.Branch
            }


            var arrBookingList: ArrayList<String> = ArrayList()

            if(model.TourBookingNo!!.contains(",")) {
                val splitList2 = model.TourBookingNo.split(",")
                for (j in splitList2.indices) {
                    arrBookingList.add(splitList2[j])
                }
            } else {
                arrBookingList.add(model.TourBookingNo)
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

            itemView.txtHotelBookingNo.setOnClickListener {
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

            val reminder = position % 3
            itemView.card.setBackgroundDrawable(ContextCompat.getDrawable(mContext, colors[reminder]))

            if(model.IsCreatedFromApp!!) {

                itemView.cardEdit.visible()
                itemView.cardEdit.setOnClickListener {
                    recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1)
                }
            } else {
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