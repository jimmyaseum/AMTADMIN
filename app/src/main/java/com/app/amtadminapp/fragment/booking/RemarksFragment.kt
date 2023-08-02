package com.app.amtadminapp.fragment.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.adapter.RemarksListAdapter
import com.app.amtadminapp.fragment.BaseFragment
import com.app.amtadminapp.model.response.CommonResponse
import com.app.amtadminapp.model.response.RemarksListModel
import com.app.amtadminapp.model.response.RemarksListResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.fragment_remarks.view.*
import kotlinx.android.synthetic.main.fragment_remarks.view.txtTourBookingNo
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemarksFragment  : BaseFragment() {
    private var views: View? = null
    var sharedPreference: SharedPreference? = null

    var arrRemarksList: ArrayList<RemarksListModel>? = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_remarks, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(activity!!)

        views!!.txtTourBookingNo.text = "Tour Booking No : "+ AppConstant.BookingNo

        views!!.rvRemarksList.layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)

        views!!.LLcardButtonSave.setOnClickListener {
            if(views!!.edtRemarks.text.toString().equals("")) {
                activity!!.toast("Please enter remarks",Toast.LENGTH_SHORT)
            } else {
                if (isConnectivityAvailable(activity!!)) {
                    CallInsertRemarksAPI()
                } else {
                    activity!!.toast(getString(R.string.str_msg_no_internet), AppConstant.TOAST_SHORT)
                }
            }
        }

        if (isOnline(activity!!)) {
            GetRemarksList()
        }
    }

    private fun GetRemarksList() {

        val jsonObject = JSONObject()
        jsonObject.put("TourBookingID", AppConstant.TOURBookingID)

        val call = ApiUtils.apiInterface.GetRemarkList(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<RemarksListResponse> {
            override fun onResponse(call: Call<RemarksListResponse>, response: Response<RemarksListResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        arrRemarksList = arrayList

                        if(arrayList.size > 0) {
                            views!!.rvRemarksList.adapter = RemarksListAdapter(activity!!,arrayList)
                        }
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<RemarksListResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun CallInsertRemarksAPI() {
        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(activity!!)
        }
        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        val jsonObject = JSONObject()

        jsonObject.put("CreatedBy", CreatedBy)
        jsonObject.put("TourBookingID", AppConstant.TOURBookingID)
        jsonObject.put("Remarks",views!!.edtRemarks.text.toString())

        val call = ApiUtils.apiInterface.InserRemarks(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.code == 200) {
                        hideProgress()
                        views!!.edtRemarks.setText("")
                        GetRemarksList()
                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

}