package com.app.amtadminapp.activity.Reports.ReportView

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Report.PendingHotelVoucherReportAdapter
import com.app.amtadminapp.model.response.PendingHotelVoucherReportModel
import com.app.amtadminapp.model.response.PendingHotelVoucherReportResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_pending_hotel_voucher_report_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class PendingHotelVoucherReportViewActivity : BaseActivity(), View.OnClickListener {

    var DateType = ""
    var Date_Type = ""

    var Tour_Id = ""

    var FromDate = ""
    var ToDate = ""

    lateinit var adapter: PendingHotelVoucherReportAdapter
    private var arrPendingHotelVoucherReportList: ArrayList<PendingHotelVoucherReportModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_hotel_voucher_report_view)

        Date_Type = intent.getStringExtra("DateType").toString()
        FromDate = intent.getStringExtra("FromDate").toString()
        ToDate = intent.getStringExtra("ToDate").toString()
        Tour_Id = intent.getIntExtra("SelectTour", 0).toString()

        initializeView()
    }

    override fun initializeView() {
        imgBack.setOnClickListener(this)

        rvPendingPaymentList.setLayoutManager(LinearLayoutManager(this))
        arrPendingHotelVoucherReportList.clear()


        if(isOnline(this)) {
            GetPendingHotelVoucherReportListAPI()
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

    private fun GetPendingHotelVoucherReportListAPI() {

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

        val call = ApiUtils.apiInterface.getPendingHotelVoucherDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<PendingHotelVoucherReportResponse> {
            override fun onResponse(call: Call<PendingHotelVoucherReportResponse>, response: Response<PendingHotelVoucherReportResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        arrPendingHotelVoucherReportList.clear()
                        arrPendingHotelVoucherReportList = arrayList

                        if(arrPendingHotelVoucherReportList.size > 0) {
                            adapter = PendingHotelVoucherReportAdapter(this@PendingHotelVoucherReportViewActivity, arrPendingHotelVoucherReportList)
                            rvPendingPaymentList.adapter = adapter
                        }
                        hideProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<PendingHotelVoucherReportResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
}