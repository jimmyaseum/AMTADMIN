package com.app.amtadminapp.adapter.Dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.interFase.RecyclerClickListener
import kotlinx.android.synthetic.main.adapter_tourbooking.view.*
import java.util.*

class BookingNoAdapter(private val mContext: Context, private val arrData: ArrayList<String>) : RecyclerView.Adapter<BookingNoAdapter.ViewHolder>() {

    private var recyclerRowClick: RecyclerClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_tourbooking, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrData[position], recyclerRowClick!!)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            model: String,
            recyclerClickListener: RecyclerClickListener
        ) {

            itemView.txtBokingNo.text = model
            itemView.txtBokingNo.setPaintFlags(itemView.txtBokingNo.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            itemView.setOnClickListener { view ->
                recyclerClickListener!!.onItemClickEvent(view, position, 1)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    fun setRecyclerRowClick(recyclerRowClick: RecyclerClickListener) {
        this.recyclerRowClick = recyclerRowClick
    }

}