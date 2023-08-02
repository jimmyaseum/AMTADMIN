package com.app.amtadminapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.NotificationListModel
import com.app.amtadminapp.utils.convertDateStringToString
import kotlinx.android.synthetic.main.adapter_notification_list.view.*

class NotificationListAdapter(val context: Context?, private val arrData: ArrayList<NotificationListModel>,
                              val recyclerClickListener: RecyclerClickListener) : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_notification_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, position, arrData, recyclerClickListener)
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<NotificationListModel>,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context

            if(model[position].Description != null) {
                itemView.txt_Description.text = model[position].Description
            }

            if(model[position].Title != null) {
                itemView.txt_Title.text = model[position].Title
            }

            if(model[position].NotificationDate != null) {
                val mDate = convertDateStringToString(model[position].NotificationDate!!,"MM/dd/yyyy hh:mm:ss", "dd/MM/yyyy")
                itemView.txt_Date.text = mDate
            }

            if(model[position].MessageType != null) {

                if(model[position].MessageType.equals("HOTELVOUCHER"))
                {
                    itemView.image.setImageResource(R.drawable.hotel_voucher)
                    itemView.image.setColorFilter(ContextCompat.getColor(context, R.color.colorYellow200))
                    itemView.txt_Title.setTextColor(context.resources.getColor(R.color.colorYellow200))
                    itemView.txt_Date.setTextColor(context.resources.getColor(R.color.colorYellow200))
                } else if(model[position].MessageType.equals("ROUTEVOUCHER"))
                {
                    itemView.image.setImageResource(R.drawable.route_voucher)
                    itemView.image.setColorFilter(ContextCompat.getColor(context, R.color.green))
                    itemView.txt_Title.setTextColor(context.resources.getColor(R.color.green))
                    itemView.txt_Date.setTextColor(context.resources.getColor(R.color.green))
                } else if(model[position].MessageType.equals("AIRLINEVOUCHER"))
                {
                    itemView.image.setImageResource(R.drawable.flight_voucher)
                    itemView.image.setColorFilter(ContextCompat.getColor(context, R.color.colorgreenish400))
                    itemView.txt_Title.setTextColor(context.resources.getColor(R.color.colorgreenish400))
                    itemView.txt_Date.setTextColor(context.resources.getColor(R.color.colorgreenish400))
                } else if(model[position].MessageType.equals("CANCELLATIONBOOKING"))
                {
                    itemView.image.setImageResource(R.drawable.ic_close)
                    itemView.image.setColorFilter(ContextCompat.getColor(context, R.color.colorRed))
                    itemView.txt_Title.setTextColor(context.resources.getColor(R.color.colorRed))
                    itemView.txt_Date.setTextColor(context.resources.getColor(R.color.colorRed))
                }
            }

            itemView.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, position, 108)
            }
        }
    }
}

