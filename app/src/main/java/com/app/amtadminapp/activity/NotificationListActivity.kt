package com.app.amtadminapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.adapter.NotificationListAdapter
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.NotificationListModel
import com.app.amtadminapp.model.response.NotificationListResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_notification_list.*
import kotlinx.android.synthetic.main.layout_no_data.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class NotificationListActivity : BaseActivity(), View.OnClickListener, RecyclerClickListener {
    var sharedPreference: SharedPreference? = null

    private var arrList: ArrayList<NotificationListModel>? = null
    lateinit var adapter: NotificationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)
        initializeView()
    }
    override fun initializeView() {
        sharedPreference = SharedPreference(this)
        initListner()
    }

    private fun initListner() {

        imgBack.setOnClickListener(this)
        tvRetry.setOnClickListener(this)

        arrList = ArrayList()
        rvNotificationList.layoutManager = LinearLayoutManager(this)

        if (isOnline(this@NotificationListActivity)) {
            CallNotificationAPI()
        } else {
            showHideDesignView(3)
        }

    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.tvRetry -> {
                if (isOnline(this@NotificationListActivity)) {
                    CallNotificationAPI()
                } else {
                    showHideDesignView(3)
                }
            }
        }
    }

    private fun CallNotificationAPI() {

        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }

        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        var jsonObject = JSONObject()
        jsonObject.put("UserID",CreatedBy)
        jsonObject.put("UserType",2)

        val call = ApiUtils.apiInterface2.getNotificationList(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<NotificationListResponse> {
            override fun onFailure(call: Call<NotificationListResponse>, t: Throwable) {
                hideProgress()
                showHideDesignView(4)
            }
            override fun onResponse(call: Call<NotificationListResponse>, response: Response<NotificationListResponse>) {
                if (response.code() == 200) {
                    if (response.body()!!.Status == 200) {
                        val arrayListdashboard = response.body()?.Data

                        arrList!!.clear()
                        arrList = arrayListdashboard!!

                        adapter = NotificationListAdapter(this@NotificationListActivity,
                            arrList!!,this@NotificationListActivity)
                        rvNotificationList.adapter = adapter

                        hideProgress()
                        showHideDesignView(1)
                    } else if (response.body()!!.Status == 1010 ||  response.body()?.Status == 201) {
                        hideProgress()
                        showHideDesignView(2)
                    }
                } else {
                    hideProgress()
                    showHideDesignView(2)
                }
            }
        })
    }

    private fun showHideDesignView(mode: Int) {

        when (mode) {
            1 -> {
                rvNotificationList.visible()
                rlMainNoData.gone()
            }
            2 -> {

                tvNoData.text = "No Data Found"
                imgNoData.setImageResource(R.drawable.ic_notifications)

                rvNotificationList.gone()
                rlMainNoData.visible()
                tvRetry.gone()
            }
            3 -> {
                tvNoData.text = getString(R.string.msg_no_internet)
                imgNoData.setImageResource(R.drawable.ic_no_internet)

                rvNotificationList.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
            4 -> {
                tvNoData.text = getString(R.string.msg_oops_something_went_wrong)
                imgNoData.setImageResource(R.drawable.ic_oops)

                rvNotificationList.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
        }
    }

    override fun onItemClickEvent(view: View, position: Int, typ: Int) {
        when(typ) {
            108 -> {

                val type = arrList!![position].MessageType
                if(type.equals("TOURBOOKING")) {

                    val intent = Intent(this, TourBookingFormActivity::class.java)
                    intent.putExtra("state","edit")
                    intent.putExtra("TourBookingNo",arrList!![position].TourBookingNo)
                    intent.putExtra("ID",arrList!![position].ReferenceID)
                    intent.putExtra("CustomerID",arrList!![position].CustomerID)
                    intent.putExtra("NotificationType",type)
                    startActivity(intent)
                }
            }
        }
    }


}