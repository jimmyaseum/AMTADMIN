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
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.RouteVoucherListModel
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.visible
import kotlinx.android.synthetic.main.row_route_voucher_list.view.*
import kotlinx.android.synthetic.main.row_route_voucher_list.view.LL_More
import kotlinx.android.synthetic.main.row_route_voucher_list.view.card
import kotlinx.android.synthetic.main.row_route_voucher_list.view.txtBookBy
import kotlinx.android.synthetic.main.row_route_voucher_list.view.txtBranch
import kotlinx.android.synthetic.main.row_route_voucher_list.view.txtReadLess
import kotlinx.android.synthetic.main.row_route_voucher_list.view.txtReadMore
import kotlinx.android.synthetic.main.row_route_voucher_list.view.txtSector
import kotlinx.android.synthetic.main.row_route_voucher_list.view.txtTour
import kotlinx.android.synthetic.main.row_route_voucher_list.view.txtTourDateCode
import kotlin.collections.ArrayList

class RouteVoucherListAdapter(private val mContext: Context, private val arrData: ArrayList<RouteVoucherListModel>) : RecyclerView.Adapter<RouteVoucherListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_route_voucher_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrData[position],mContext)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            model: RouteVoucherListModel,
            mContext: Context
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.RouteVoucher != null) {
                itemView.txtRouteVoucherNo.text = model.RouteVoucher
                itemView.txtRouteVoucherNo.setPaintFlags(itemView.txtRouteVoucherNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }
            if(model.Sector != null) {
                itemView.txtSector.text = model.Sector
            }
            if(model.Tour != null) {
                itemView.txtTour.text = model.Tour
            }
            if(model.TourDateCode != null) {
                itemView.txtTourDateCode.text = model.TourDateCode
            }
            if(model.VehicleType != null) {
                itemView.txtVehicleType.text = model.VehicleType
            }
            if(model.BookBy != null) {
                itemView.txtBookBy.text = model.BookBy
            }
            if(model.Branch != null) {
                itemView.txtBranch.text = model.Branch
            }

            itemView.txtRouteVoucherNo.setOnClickListener {
                val fullpath =  model.RouteVoucherLink + model.RouteVoucher
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