package com.app.amtadminapp.Chatbot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.fragment.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_cb_chat.view.*

class ChatFragment : BaseFragment() {

    private var views: View? = null

    private var UsersRef: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private val userList: ArrayList<ChatListModel> = ArrayList()
    private var current_User_id: String? = null
    lateinit var adapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        views = inflater.inflate(R.layout.fragment_cb_chat, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {

        val layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        views!!.rvUserList.layoutManager = layoutManager

        showProgress()

        adapter = ChatListAdapter(activity!!, userList)
        views!!.rvUserList.adapter = adapter

        UsersRef = FirebaseDatabase.getInstance().reference.child(ChatConstant.F_EMPLOYEE)
        mAuth = FirebaseAuth.getInstance()
        current_User_id = mAuth!!.getCurrentUser()!!.uid

        LoadFCMData()

    }

    private fun LoadFCMData() {
        UsersRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userList.clear()
                    for (dn in snapshot.children) {
                        for(postsnapchat in snapshot.children) {
                            val message = postsnapchat.getValue(ChatListModel::class.java)
                            if (!userList.contains(message)) {
                                if(!message!!.Uid.equals(current_User_id)) {
                                    userList.add(message!!)
                                    adapter.notifyDataSetChanged()
                                    hideProgress()
                                } else {
                                    hideProgress()
                                }
                            } else {
                                hideProgress()
                            }
                        }
                    }
                } else {
                    hideProgress()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                hideProgress()
            }
        })
    }
}