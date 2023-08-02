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
import com.app.amtadminapp.model.response.BookingListModel
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.visible
import kotlinx.android.synthetic.main.row_booking_list.view.*
import kotlinx.android.synthetic.main.row_booking_list.view.LL_More
import kotlinx.android.synthetic.main.row_booking_list.view.card
import kotlinx.android.synthetic.main.row_booking_list.view.txtBokingNo
import kotlinx.android.synthetic.main.row_booking_list.view.txtBookBy
import kotlinx.android.synthetic.main.row_booking_list.view.txtBranch
import kotlinx.android.synthetic.main.row_booking_list.view.txtContactNo
import kotlinx.android.synthetic.main.row_booking_list.view.txtNights
import kotlinx.android.synthetic.main.row_booking_list.view.txtReadLess
import kotlinx.android.synthetic.main.row_booking_list.view.txtReadMore
import kotlinx.android.synthetic.main.row_booking_list.view.txtSector
import kotlinx.android.synthetic.main.row_booking_list.view.txtStatus
import kotlinx.android.synthetic.main.row_booking_list.view.txtTour
import kotlinx.android.synthetic.main.row_booking_list.view.txtTourCode
import kotlinx.android.synthetic.main.row_booking_list.view.txtTourType
import kotlinx.android.synthetic.main.row_booking_list.view.txtTouristName
import java.util.*

class BookingListAdapter(private val mContext: Context, private val arrData: ArrayList<BookingListModel>) : RecyclerView.Adapter<BookingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_booking_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrData[position], mContext)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(
            model: BookingListModel,
            mContext: Context
        ) {

            var colors = arrayOf(R.drawable.bg_shadow_blue,
                R.drawable.bg_shadow_orange, R.drawable.bg_shadow_purple)

            if(model.TourBookingNo != null) {
                itemView.txtBokingNo.text = model.TourBookingNo
                itemView.txtBokingNo.setPaintFlags(itemView.txtBokingNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }
            if(model.Name != null) {
                itemView.txtTouristName.text = model.Name
            }
            if(model.Sector != null) {
                itemView.txtSector.text = model.Sector
            }
            if(model.Tour != null) {
                itemView.txtTour.text = model.Tour
            }
            if(model.MobileNo != null) {
                itemView.txtContactNo.text = model.MobileNo
            }
            if(model.TravelType != null) {
                itemView.txtTourType.text = model.TravelType
            }
            if(model.TourDateCode != null) {
                itemView.txtTourCode.text = model.TourDateCode
            }
            if(model.TourStartDate != null) {
                itemView.txtTourStartDate.text = model.TourStartDate
            }
            if(model.NoOfNights != null) {
                itemView.txtNights.text = model.NoOfNights.toString()
            }
            if(model.TourBookingStatus != null) {
                itemView.txtStatus.text = model.TourBookingStatus
            }
            if(model.BookBy != null) {
                itemView.txtBookBy.text = model.BookBy
            }
            if(model.Branch != null) {
                itemView.txtBranch.text = model.Branch
            }

            itemView.txtBokingNo.setOnClickListener {
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

            itemView.txtContactNo.setOnClickListener {
                    if (!model.MobileNo.isNullOrEmpty()) {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${model.MobileNo}")
                        mContext.startActivity(intent)
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