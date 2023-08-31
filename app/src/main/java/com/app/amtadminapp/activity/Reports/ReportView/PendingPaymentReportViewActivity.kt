package com.app.amtadminapp.activity.Reports.ReportView

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Report.PendingPaymentReportAdapter
import com.app.amtadminapp.model.response.PendingPaymentReportModel
import com.app.amtadminapp.model.response.PendingPaymentReportResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_pending_payment_report_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class PendingPaymentReportViewActivity : BaseActivity(), View.OnClickListener {

    /*Date Type And Travel Type*/
    var DateType = ""
    var TravelType = ""
    var Date_Type = ""
    var Travel_Type = ""

    var FromDate = ""
    var ToDate = ""
    lateinit var adapter: PendingPaymentReportAdapter
    private var arrPendingPaymentReportList: ArrayList<PendingPaymentReportModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_payment_report_view)

        Date_Type = intent.getStringExtra("Date").toString()
        Travel_Type = intent.getStringExtra("Travel").toString()

        Log.d("Hello==>",""+DateType)
        Log.d("Hello==>",""+TravelType)

        FromDate = intent.getStringExtra("FromDate").toString()
        ToDate = intent.getStringExtra("ToDate").toString()

        initializeView()
    }

    override fun initializeView() {
        imgBack.setOnClickListener(this)

        rvPendingPaymentList.setLayoutManager(LinearLayoutManager(this))
        arrPendingPaymentReportList.clear()


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

        val From_Date = convertDateStringToString(FromDate, AppConstant.dd_MM_yyyy_Slash, AppConstant.yyyy_MM_dd_Dash)!!
        val To_Date = convertDateStringToString(ToDate, AppConstant.dd_MM_yyyy_Slash, AppConstant.yyyy_MM_dd_Dash)!!

        val jsonObject = JSONObject()
        if (Travel_Type == "TOUR"){
            TravelType = "1"
        } else if (Travel_Type == "PACKAGE"){
            TravelType = "2"
        } else if (Travel_Type == "ALL"){
            TravelType = "3"
        }

        if (Date_Type == "Booking Date"){
            DateType = "1"
        } else if (Date_Type == "Travel Date"){
            DateType = "2"
        }

        jsonObject.put("DateType", DateType)
        jsonObject.put("Type", TravelType)
        jsonObject.put("FromDate", From_Date)
        jsonObject.put("ToDate", To_Date)

        val call = ApiUtils.apiInterface.getPendingPaymentDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<PendingPaymentReportResponse> {
            override fun onResponse(call: Call<PendingPaymentReportResponse>, response: Response<PendingPaymentReportResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        arrPendingPaymentReportList.clear()
                        arrPendingPaymentReportList = arrayList

                        if(arrPendingPaymentReportList.size > 0) {
                            adapter = PendingPaymentReportAdapter(this@PendingPaymentReportViewActivity, arrPendingPaymentReportList)
                            rvPendingPaymentList.adapter = adapter
                        }
                        hideProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<PendingPaymentReportResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
}