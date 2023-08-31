package com.app.amtadminapp.activity.Reports.ReportView

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Report.UserWiseTourBookingReportAdapter
import com.app.amtadminapp.model.response.UserWiseTourBookingReportModel
import com.app.amtadminapp.model.response.UserWiseTourBookingReportResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_user_wise_tour_booking_report_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class UserWiseTourBookingReportView : BaseActivity(), View.OnClickListener {

    /*Branch*/
    var BranchID = ""

    /*Employee*/
    var EmployeeID = ""

    /*FromDate And ToDate*/
    var FromDate = ""
    var ToDate = ""

    lateinit var adapter: UserWiseTourBookingReportAdapter
    private var arrBranchWiseTourBookingReportList: ArrayList<UserWiseTourBookingReportModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wise_tour_booking_report_view)

        BranchID = intent.getIntExtra("BranchID",0).toString()
        EmployeeID = intent.getIntExtra("EmployeeID",0).toString()
        FromDate = intent.getStringExtra("FromDate").toString()
        ToDate = intent.getStringExtra("ToDate").toString()

        initializeView()
    }

    override fun initializeView() {
        imgBack.setOnClickListener(this)

        rvPendingPaymentList.setLayoutManager(LinearLayoutManager(this))
        arrBranchWiseTourBookingReportList.clear()


        if(isOnline(this)) {
            GetPendingBookingReportListAPI()
        } else {
            toast(getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
        }
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
        }
    }

    private fun GetPendingBookingReportListAPI() {

        showProgress()

        val jsonObject = JSONObject()

        jsonObject.put("BranchID", BranchID)
        jsonObject.put("EmployeeID", EmployeeID)
        jsonObject.put("FromDate", FromDate)
        jsonObject.put("ToDate", ToDate)


        val call = ApiUtils.apiInterface.getUserWiseTourBookingDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<UserWiseTourBookingReportResponse> {
            override fun onResponse(call: Call<UserWiseTourBookingReportResponse>, response: Response<UserWiseTourBookingReportResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        arrBranchWiseTourBookingReportList.clear()
                        arrBranchWiseTourBookingReportList = arrayList

                        if(arrBranchWiseTourBookingReportList.size > 0) {
                            adapter = UserWiseTourBookingReportAdapter(this@UserWiseTourBookingReportView, arrBranchWiseTourBookingReportList)
                            rvPendingPaymentList.adapter = adapter
                        }
                        hideProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<UserWiseTourBookingReportResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
}