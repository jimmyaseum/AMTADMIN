package com.app.amtadminapp.Chatbot

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.loadUrlRoundedCorner2
import com.app.amtadminapp.utils.visible
import kotlinx.android.synthetic.main.adapter_employee.view.*

class ChatListAdapter(val context: Context?, private val arrData: ArrayList<ChatListModel>) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_employee, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, position,arrData)
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<ChatListModel>
        ) {
            this.context = context

            if(model[position].state != null) {
                if (model[position].state.equals("online")) {
                    itemView.status.visible()
                } else {
                    itemView.status.gone()
                }
            }
            if(model[position].name != "") {
                itemView.txt_Name.text = model[position].name
            }

            if(model[position].mobile != "") {
                itemView.txt_Mobile.text = model[position].mobile
            }

            if(model[position].usertype != "") {
                itemView.txt_Usertype.text = model[position].usertype
            }
            if( model[position].image != "") {
                itemView.pro_image.loadUrlRoundedCorner2(
                    model[position].image,
                    R.drawable.ic_profile,
                    5
                )
            } else {
                itemView.pro_image.loadUrlRoundedCorner2(
                    "",
                    R.drawable.ic_profile,
                    5
                )
            }

            itemView.setOnClickListener {
                val chatIntent = Intent(context, ConversationActivity::class.java)
                chatIntent.putExtra("visit_user_id", model[position].Uid)
                chatIntent.putExtra("visit_user_name", model[position].name)
                chatIntent.putExtra("visit_user_image", model[position].image)
                chatIntent.putExtra("visit_user_device_token", model[position].device_token)
                context.startActivity(chatIntent)

            }
        }
    }
}
