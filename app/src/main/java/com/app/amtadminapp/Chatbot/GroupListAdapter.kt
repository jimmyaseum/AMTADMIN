package com.app.amtadminapp.Chatbot

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.utils.visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.adapter_group.view.*


class GroupListAdapter(val context: Context?, private val arrData: ArrayList<GroupList>) : RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {

    var mAuth: FirebaseAuth? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_group, parent, false)
        mAuth = FirebaseAuth.getInstance()
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, position,arrData, mAuth!!)
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<GroupList>,
            mAuth: FirebaseAuth
        ) {
            this.context = context

            if(model[position].Name != "") {
                itemView.txt_Name.text = model[position].Name
            }
            val UID: String = mAuth.getCurrentUser()!!.getUid()

            var GroupNameRef1 = FirebaseDatabase.getInstance().reference.child(ChatConstant.F_GROUP)
            GroupNameRef1.child(model[position].Name!!).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if(snapshot.hasChild(ChatConstant.F_GROUP_MESSAGE)) {
                                GroupNameRef1.child(model[position].Name!!).child(ChatConstant.F_GROUP_MESSAGE).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (snap in snapshot.children) {
                                            GroupNameRef1.child(model[position].Name!!).child(ChatConstant.F_GROUP_MESSAGE).child(snap.key!!)
                                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    if(snapshot.hasChild("unreaduid")) {
                                                        val unreaduid = snapshot.child("unreaduid").getValue().toString();
                                                        Log.e("unreaduid","==>"+unreaduid)
                                                        if (!unreaduid.equals("")) {
                                                            if (unreaduid.contains(UID)) {
                                                                itemView.txt_newmsg.visible()
                                                            }
                                                        }
                                                    }
                                                }
                                                override fun onCancelled(error: DatabaseError) {}
                                            })
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {}
                                })
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })

            itemView.setOnClickListener {

                GroupNameRef1.child(model[position].Name!!).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if(snapshot.hasChild(ChatConstant.F_GROUP_MESSAGE)) {
                                GroupNameRef1.child(model[position].Name!!).child(ChatConstant.F_GROUP_MESSAGE).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (snap in snapshot.children) {
                                            GroupNameRef1.child(model[position].Name!!).child(ChatConstant.F_GROUP_MESSAGE).child(snap.key!!)
                                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        if(snapshot.hasChild("unreaduid")) {

                                                            var unreaduid11 = snapshot.child("unreaduid").getValue().toString();
                                                            Log.e("unreaduid","==>"+unreaduid11)

                                                            var unreaduid = unreaduid11.replace(UID, "")

                                                            if(unreaduid.startsWith(",")) {
                                                                unreaduid = unreaduid.substring(1)
                                                            }
                                                            else if(unreaduid.endsWith(",")) {
                                                                unreaduid = unreaduid.substring(unreaduid.length)
                                                            }
                                                            else if(unreaduid.contains(", ,")) {
                                                                unreaduid = unreaduid.replace(", ,",",")
                                                            }

                                                            GroupNameRef1.child(model[position].Name!!).child(ChatConstant.F_GROUP_MESSAGE).child(snap.key!!).child("unreaduid").setValue(unreaduid)

                                                        }
                                                    }
                                                    override fun onCancelled(error: DatabaseError) {}
                                                })
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {}
                                })
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })

                val chatIntent = Intent(context, GroupChatActivity::class.java)
                chatIntent.putExtra("GroupName", model[position].Name)
                context.startActivity(chatIntent)
                ((this.context) as ChatBoatActivity).finish()

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

//    protected fun getAnimators(view: View): Array<Animator?>? {
//        return arrayOf<Animator?>(
//            ObjectAnimator.ofFloat(view, "translationY", view.measuredHeight.toFloat(), 0f)
//        )
//    }
}
