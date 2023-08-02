package com.app.amtadminapp.activity.Reports.ReportView

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Report.SelfTourBookingReportAdapter
import com.app.amtadminapp.model.response.SelfTourBookingReportModel
import com.app.amtadminapp.model.response.SelfTourBookingReportResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_self_tour_booking_report_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class SelfTourBookingReportViewActivity : BaseActivity(), View.OnClickListener {

    /*Date Type*/
    var DateType = ""
    var Date_Type = ""

    /*FromDate And ToDate*/
    var FromDate = ""
    var ToDate = ""

    var sharedPreference: SharedPreference? = null

    var EmployeeID = ""

    lateinit var adapter: SelfTourBookingReportAdapter
    private var arrVehicleAllotmentReportList: ArrayList<SelfTourBookingReportModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_tour_booking_report_view)

        Date_Type = intent.getStringExtra("DateType").toString()
        FromDate = intent.getStringExtra("FromDate").toString()
        ToDate = intent.getStringExtra("ToDate").toString()

        initializeView()
    }

    override fun initializeView() {

        sharedPreference = SharedPreference(this)
        EmployeeID = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!

        imgBack.setOnClickListener(this)

        rvPendingPaymentList.setLayoutManager(LinearLayoutManager(this))
        arrVehicleAllotmentReportList.clear()


        if(isOnline(this)) {
            GetPendingPaymentReportListAPI()
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

    private fun GetPendingPaymentReportListAPI() {

        showProgress()

        val jsonObject = JSONObject()


        if (Date_Type == "Booking Date"){
            DateType = "1"
        } else if (Date_Type == "Travel Date"){
            DateType = "2"
        }

        jsonObject.put("EmployeeID", EmployeeID)
        jsonObject.put("FromDate", FromDate)
        jsonObject.put("ToDate", ToDate)
        jsonObject.put("DateType", DateType)

        val call = ApiUtils.apiInterface.getSelfTourBookingDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<SelfTourBookingReportResponse> {
            override fun onResponse(call: Call<SelfTourBookingReportResponse>, response: Response<SelfTourBookingReportResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        arrVehicleAllotmentReportList.clear()
                        arrVehicleAllotmentReportList = arrayList

                        if(arrVehicleAllotmentReportList.size > 0) {
                            adapter = SelfTourBookingReportAdapter(this@SelfTourBookingReportViewActivity, arrVehicleAllotmentReportList)
                            rvPendingPaymentList.adapter = adapter
                        }
                        hideProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<SelfTourBookingReportResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
}