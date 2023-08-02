package com.app.amtadminapp.activity.Reports.ReportView

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Report.ConfirmedBookingReportAdapter
import com.app.amtadminapp.model.response.ConfirmedBookingReportModel
import com.app.amtadminapp.model.response.ConfirmedBookingReportResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_confirmed_hotel_voucher_report_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ConfirmedBookingReportViewActivity : BaseActivity(), View.OnClickListener {

    /*Date Type And Travel Type*/
    var DateType = ""
    var TravelType = ""
    var Date_Type = ""
    var Travel_Type = ""

    var SelectTour = ""

    var CityID = ""

    var FromDate = ""
    var ToDate = ""

    lateinit var adapter: ConfirmedBookingReportAdapter
    private var arrConfirmedBookingReportList: ArrayList<ConfirmedBookingReportModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmed_booking_report_view)

        Date_Type = intent.getStringExtra("DateType").toString()
        Travel_Type = intent.getStringExtra("TravelType").toString()
        SelectTour = intent.getIntExtra("SelectTour",0).toString()
        CityID = intent.getIntExtra("Place", 0).toString()
        FromDate = intent.getStringExtra("FromDate").toString()
        ToDate = intent.getStringExtra("ToDate").toString()

        initializeView()
    }

    override fun initializeView() {
        imgBack.setOnClickListener(this)

        rvPendingPaymentList.setLayoutManager(LinearLayoutManager(this))
        arrConfirmedBookingReportList.clear()


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
        jsonObject.put("TourID", SelectTour)
        jsonObject.put("FromDate", FromDate)
        jsonObject.put("ToDate", ToDate)
        jsonObject.put("CityID", CityID)


        val call = ApiUtils.apiInterface.getConfirmedBookingDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<ConfirmedBookingReportResponse> {
            override fun onResponse(call: Call<ConfirmedBookingReportResponse>, response: Response<ConfirmedBookingReportResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        arrConfirmedBookingReportList.clear()
                        arrConfirmedBookingReportList = arrayList

                        if(arrConfirmedBookingReportList.size > 0) {
                            adapter = ConfirmedBookingReportAdapter(this@ConfirmedBookingReportViewActivity, arrConfirmedBookingReportList)
                            rvPendingPaymentList.adapter = adapter
                        }
                        hideProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<ConfirmedBookingReportResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
}