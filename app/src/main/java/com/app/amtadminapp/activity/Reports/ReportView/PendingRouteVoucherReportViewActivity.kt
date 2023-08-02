package com.app.amtadminapp.activity.Reports.ReportView

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Report.PendingPaymentReportAdapter
import com.app.amtadminapp.adapter.Report.PendingRouteVoucherReportAdapter
import com.app.amtadminapp.model.response.PendingPaymentReportModel
import com.app.amtadminapp.model.response.PendingPaymentReportResponse
import com.app.amtadminapp.model.response.PendingRouteVoucherReportModel
import com.app.amtadminapp.model.response.PendingRouteVoucherReportResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_pending_payment_report_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class PendingRouteVoucherReportViewActivity : BaseActivity(), View.OnClickListener {

    var DateType = ""
    var Date_Type = ""

    var Tour_Id = ""

    var FromDate = ""
    var ToDate = ""

    lateinit var adapter: PendingRouteVoucherReportAdapter
    private var arrPendingRouteVoucherReportList: ArrayList<PendingRouteVoucherReportModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_route_voucher_report_view)

        Date_Type = intent.getStringExtra("DateType").toString()
        Tour_Id = intent.getIntExtra("TourID", 0).toString()
        FromDate = intent.getStringExtra("FromDate").toString()
        ToDate = intent.getStringExtra("ToDate").toString()

        initializeView()
    }

    override fun initializeView() {
        imgBack.setOnClickListener(this)

        rvPendingPaymentList.setLayoutManager(LinearLayoutManager(this))
        arrPendingRouteVoucherReportList.clear()


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

        jsonObject.put("DateType", DateType)
        jsonObject.put("TourID", Tour_Id)
        jsonObject.put("FromDate", FromDate)
        jsonObject.put("ToDate", ToDate)

        val call = ApiUtils.apiInterface.getPendingRouteVoucherDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<PendingRouteVoucherReportResponse> {
            override fun onResponse(call: Call<PendingRouteVoucherReportResponse>, response: Response<PendingRouteVoucherReportResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        arrPendingRouteVoucherReportList.clear()
                        arrPendingRouteVoucherReportList = arrayList

                        if(arrPendingRouteVoucherReportList.size > 0) {
                            adapter = PendingRouteVoucherReportAdapter(this@PendingRouteVoucherReportViewActivity, arrPendingRouteVoucherReportList)
                            rvPendingPaymentList.adapter = adapter
                        }
                        hideProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<PendingRouteVoucherReportResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
}