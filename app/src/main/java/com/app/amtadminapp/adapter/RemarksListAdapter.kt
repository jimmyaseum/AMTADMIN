package com.app.amtadminapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.model.response.RemarksListModel
import kotlinx.android.synthetic.main.adapter_remarks_list.view.*
import java.util.*

class RemarksListAdapter(private val mContext: Context, private val arrData: ArrayList<RemarksListModel>) : RecyclerView.Adapter<RemarksListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_remarks_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrData[position])

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            model: RemarksListModel
        ) {

            itemView.txtRemarks.text = model.Remarks
            itemView.txtRemarksBy.text = "Remarks By : "+ model.RemarksBy
            itemView.txtRemarksDate.text = "Remarks Date : "+ model.RemarksDate

        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

}