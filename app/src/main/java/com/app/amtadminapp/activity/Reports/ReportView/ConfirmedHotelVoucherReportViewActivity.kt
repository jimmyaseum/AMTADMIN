package com.app.amtadminapp.activity.Reports.ReportView

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Report.ConfirmedHotelVoucherReportAdapter
import com.app.amtadminapp.model.response.ConfirmedHotelVoucherReportModel
import com.app.amtadminapp.model.response.ConfirmedHotelVoucherReportResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_confirmed_hotel_voucher_report_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ConfirmedHotelVoucherReportViewActivity : BaseActivity(), View.OnClickListener {

    var SelectTour = ""

    var CityID = ""

    var HotelID = ""

    var FromDate = ""
    var ToDate = ""

    lateinit var adapter: ConfirmedHotelVoucherReportAdapter
    private var arrConfirmedHotelVoucherReportList: ArrayList<ConfirmedHotelVoucherReportModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmed_hotel_voucher_report_view)

        SelectTour = intent.getIntExtra("SelectTour",0).toString()
        CityID = intent.getIntExtra("Place", 0).toString()
        HotelID = intent.getIntExtra("Hotel", 0).toString()
        FromDate = intent.getStringExtra("FromDate").toString()
        ToDate = intent.getStringExtra("ToDate").toString()

        initializeView()
    }

    override fun initializeView() {
        imgBack.setOnClickListener(this)

        rvPendingPaymentList.setLayoutManager(LinearLayoutManager(this))
        arrConfirmedHotelVoucherReportList.clear()


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

        jsonObject.put("TourID", SelectTour)
        jsonObject.put("FromDate", FromDate)
        jsonObject.put("ToDate", ToDate)
        jsonObject.put("CityID", CityID)
        jsonObject.put("HotelID", HotelID)


        val call = ApiUtils.apiInterface.getConfirmedHotelVoucherDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<ConfirmedHotelVoucherReportResponse> {
            override fun onResponse(call: Call<ConfirmedHotelVoucherReportResponse>, response: Response<ConfirmedHotelVoucherReportResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        arrConfirmedHotelVoucherReportList.clear()
                        arrConfirmedHotelVoucherReportList = arrayList

                        if(arrConfirmedHotelVoucherReportList.size > 0) {
                            adapter = ConfirmedHotelVoucherReportAdapter(this@ConfirmedHotelVoucherReportViewActivity, arrConfirmedHotelVoucherReportList)
                            rvPendingPaymentList.adapter = adapter
                        }
                        hideProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<ConfirmedHotelVoucherReportResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
}