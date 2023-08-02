package com.app.amtadminapp.adapter.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.booking.DateDataModel
import com.app.amtadminapp.utils.AppConstant
import com.app.amtadminapp.utils.convertDateStringToString
import kotlinx.android.synthetic.main.row_select.view.*
import java.util.*

class DialogDateAdapter(private val mContext: Context, private val arrData: ArrayList<DateDataModel>) : RecyclerView.Adapter<DialogDateAdapter.ViewHolder>() {

    private var recyclerRowClick: RecyclerClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_select, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrData[position], recyclerRowClick!!)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            model: DateDataModel,
            recyclerClickListener: RecyclerClickListener
        ) {

            val date = convertDateStringToString(
                model.TourDate!!,
                AppConstant.yyyy_MM_dd_Dash,
                AppConstant.dd_MM_yyyy_Slash
            )!!

            itemView.txtName.text = date

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