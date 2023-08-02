package com.app.amtadminapp.activity.Reports.ReportView

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Report.BranchWiseTourBookingSummaryReportAdapter
import com.app.amtadminapp.model.response.BranchWiseTourBookingSummaryReportModel
import com.app.amtadminapp.model.response.BranchWiseTourBookingSummaryReportResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_b_w_t_b_s_report_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class BWTBSRViewActivity : BaseActivity(), View.OnClickListener {

    /*Date Type*/
    var DateType = ""
    var Date_Type = ""

    /*FromDate And ToDate*/
    var FromDate = ""
    var ToDate = ""

    lateinit var adapter: BranchWiseTourBookingSummaryReportAdapter
    private var arrBranchWiseTourBookingReportList: ArrayList<BranchWiseTourBookingSummaryReportModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_w_t_b_s_report_view)

        Date_Type = intent.getStringExtra("DateType").toString()
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

        if (Date_Type == "Booking Date"){
            DateType = "1"
        } else if (Date_Type == "Travel Date"){
            DateType = "2"
        }

        jsonObject.put("DateType", DateType)
        jsonObject.put("FromDate", FromDate)
        jsonObject.put("ToDate", ToDate)


        val call = ApiUtils.apiInterface.getBranchWiseBookingDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<BranchWiseTourBookingSummaryReportResponse> {
            override fun onResponse(call: Call<BranchWiseTourBookingSummaryReportResponse>, response: Response<BranchWiseTourBookingSummaryReportResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        arrBranchWiseTourBookingReportList.clear()
                        arrBranchWiseTourBookingReportList = arrayList

                        if(arrBranchWiseTourBookingReportList.size > 0) {
                            adapter = BranchWiseTourBookingSummaryReportAdapter(this@BWTBSRViewActivity, arrBranchWiseTourBookingReportList)
                            rvPendingPaymentList.adapter = adapter
                        }
                        hideProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<BranchWiseTourBookingSummaryReportResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
}