package com.app.amtadminapp.Chatbot

import android.os.Bundle
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat_boat.*

class ChatBoatActivity : BaseActivity() {

    private var mAuth: FirebaseAuth? = null
    private var RootRef: DatabaseReference? = null

    private var currentUsetID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_boat)

        mAuth = FirebaseAuth.getInstance()
        RootRef = FirebaseDatabase.getInstance().reference
        currentUsetID = mAuth!!.getCurrentUser()!!.uid

        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener {
            finish()
        }

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Group"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Chat"))

        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = ChatBoatAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        viewPager!!.currentItem = 0
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            updateUserStatus("online")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            updateUserStatus("offline")
        }
    }

    private fun updateUserStatus(state: String) {
        val onlineStateMap = HashMap<String, Any>()
        onlineStateMap["state"] = state
        currentUsetID = mAuth!!.currentUser!!.uid
        RootRef!!.child(ChatConstant.F_EMPLOYEE).child(currentUsetID!!).updateChildren(onlineStateMap)
    }

}