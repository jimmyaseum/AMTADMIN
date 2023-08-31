package com.app.amtadminapp.Chatbot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.fragment.BaseFragment
import com.app.amtadminapp.utils.SharedPreference
import com.app.amtadminapp.utils.PrefConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_booking_list.view.*
import kotlinx.android.synthetic.main.fragment_cb_group.view.*
import kotlinx.android.synthetic.main.fragment_cb_group.view.swiperefreshlayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GroupFragment: BaseFragment() {
    private var views: View? = null

    var sharedPreference: SharedPreference? = null
    private var UsersRef: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private val userList: ArrayList<GroupList> = ArrayList()
    private var current_User_id: String? = null
    lateinit var adapter: GroupListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        views = inflater.inflate(R.layout.fragment_cb_group, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {

        val layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        views!!.rvGroupList.layoutManager = layoutManager

        sharedPreference = SharedPreference(activity!!)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        showProgress()

        adapter = GroupListAdapter(activity!!, userList)
        views!!.rvGroupList.adapter = adapter

        UsersRef = FirebaseDatabase.getInstance().reference.child(ChatConstant.F_GROUP)
        mAuth = FirebaseAuth.getInstance()
        current_User_id = mAuth!!.getCurrentUser()!!.uid

        views!!.swiperefreshlayout.setOnRefreshListener {
            userList.clear()
            UsersRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    for (snap in dataSnapshot.children) {
                        UsersRef!!.child(snap.key!!).addListenerForSingleValueEvent(object : ValueEventListener {

                            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {

                                if(dataSnapshot.hasChild("ISLatest")) {
                                    val IsLatest = dataSnapshot.child("ISLatest").getValue().toString();
                                    for (snap1 in dataSnapshot.children) {
                                        if(current_User_id == snap1.key) {
                                            userList.add(GroupList(snap.key!!,IsLatest))
                                            if(userList.size > 0) {
                                                userList.sortByDescending{it.DateTime}
                                                adapter.notifyDataSetChanged()

                                                hideProgress()
                                                views!!.swiperefreshlayout.isRefreshing = false
                                            } else {
                                                hideProgress()
                                                views!!.swiperefreshlayout.isRefreshing = false
                                            }
                                        } else {
                                            hideProgress()
                                            views!!.swiperefreshlayout.isRefreshing = false
                                        }
                                    }
                                } else {
                                    val calForDate = Calendar.getInstance()
                                    val currentDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                                    val currentDateTime = currentDateFormat.format(calForDate.time)
                                    UsersRef!!.child(snap.key!!).child("ISLatest").setValue(currentDateTime)
                                }
                            }
                            override fun onCancelled(@NonNull databaseError: DatabaseError) {
                                //enter code here
                                hideProgress()
                            }
                        })
                    }
                }
                override fun onCancelled(@NonNull databaseError: DatabaseError) {
                    //enter code here
                    hideProgress()
                }
            })
        }

        UsersRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                for (snap in dataSnapshot.children) {

                    UsersRef!!.child(snap.key!!).addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {

                            if(dataSnapshot.hasChild("ISLatest")) {
                                val IsLatest = dataSnapshot.child("ISLatest").getValue().toString();
                                for (snap1 in dataSnapshot.children) {
                                    if(current_User_id == snap1.key) {
                                        userList.add(GroupList(snap.key!!,IsLatest))
                                        if(userList.size > 0) {
                                            userList.sortByDescending{it.DateTime}
                                            adapter.notifyDataSetChanged()
                                            views!!.swiperefreshlayout.isRefreshing = false
                                            hideProgress()
                                        } else {
                                            views!!.swiperefreshlayout.isRefreshing = false
                                            hideProgress()
                                        }
                                    } else {
                                        views!!.swiperefreshlayout.isRefreshing = false
                                        hideProgress()
                                    }
                                }
                            } else {
                                val calForDate = Calendar.getInstance()
                                val currentDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                                val currentDateTime = currentDateFormat.format(calForDate.time)
                                UsersRef!!.child(snap.key!!).child("ISLatest").setValue(currentDateTime)
                            }
                        }

                        override fun onCancelled(@NonNull databaseError: DatabaseError) {
                            //enter code here
                            hideProgress()
                        }
                    })
                }
            }
            override fun onCancelled(@NonNull databaseError: DatabaseError) {
                //enter code here
                hideProgress()
            }
        })
    }
}