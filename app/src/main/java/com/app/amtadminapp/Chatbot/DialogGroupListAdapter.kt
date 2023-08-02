package com.app.amtadminapp.Chatbot

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import kotlinx.android.synthetic.main.row_select.view.*
import java.util.*

class DialogGroupListAdapter(private val mContext: Context, private val arrData: ArrayList<GroupMemberList>) : RecyclerView.Adapter<DialogGroupListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_select, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrData[position])

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            model: GroupMemberList
        ) {
            itemView.txtName.text = model.Name

//            itemView.setOnClickListener { view ->
//                recyclerClickListener!!.onItemClickEvent(view, position, 1)
//            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemCount(): Int {
        return arrData.size
    }
}