package com.app.amtadminapp.adapter

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
import com.app.amtadminapp.model.response.TourBookingListModel
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.visible
import kotlinx.android.synthetic.main.row_tour_booking_list.view.*
import java.util.*

class TourBookingListAdapter(private val mContext: Context, private val arrData: ArrayList<TourBookingListModel>,
                             val recyclerItemClickListener: RecyclerClickListener
) : RecyclerView.Adapter<TourBookingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_tour_booking_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrData[position], mContext, recyclerItemClickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(
            model: TourBookingListModel,
            mContext: Context,
            recyclerItemClickListener: RecyclerClickListener
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
            if(model.GroupBookingNo != null) {
                itemView.txtGroupBooking.text = model.GroupBookingNo
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

            itemView.txtReadLess.setOnClickListener {
                itemView.LL_More.gone()
                itemView.txtReadMore.visible()
            }

            itemView.txtReadMore.setOnClickListener {
                itemView.LL_More.visible()
                itemView.txtReadMore.gone()
            }

            itemView.txtContactNo.setOnClickListener {
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

            itemView.cardEdit.setOnClickListener {
                recyclerItemClickListener.onItemClickEvent(it, adapterPosition, 1)
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