package com.app.amtadminapp.activity.Reports.ReportView

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Report.HotelBlockReportAdapter
import com.app.amtadminapp.model.response.HotelBlockListModel
import com.app.amtadminapp.model.response.HotelBlockReportResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_hotel_block_report_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class HotelBlockReportViewActivity : BaseActivity(), View.OnClickListener {

    var TourID = 0
    var DateType = 0
    var HotelID = 0
    var Type = 0
    var FromDate = ""
    var ToDate = ""
    lateinit var adapter: HotelBlockReportAdapter
    private var arrHotelVoucherList: ArrayList<HotelBlockListModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_block_report_view)

        TourID = intent.getIntExtra("Tour", 0)
        HotelID = intent.getIntExtra("Hotel", 0)
        FromDate = intent.getStringExtra("FromDate").toString()
        ToDate = intent.getStringExtra("ToDate").toString()

        initializeView()
    }

    override fun initializeView() {
        imgBack.setOnClickListener(this)

        rvHotelVoucherList.setLayoutManager(LinearLayoutManager(this))
        arrHotelVoucherList.clear()

//        adapter = HotelBlockReportAdapter(this@HotelBlockReportViewActivity, arrHotelVoucherList)
//        rvHotelVoucherList.adapter = adapter

        if(isOnline(this)) {
            GetHotelBlockReportListAPI()
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

    private fun GetHotelBlockReportListAPI() {

        showProgress()

        val From_Date = convertDateStringToString(FromDate, AppConstant.dd_MM_yyyy_Slash, AppConstant.yyyy_MM_dd_Dash)!!
        val To_Date = convertDateStringToString(ToDate, AppConstant.dd_MM_yyyy_Slash, AppConstant.yyyy_MM_dd_Dash)!!

        val jsonObject = JSONObject()
        jsonObject.put("TourID", TourID.toString())
        jsonObject.put("HotelID", HotelID.toString())
        jsonObject.put("FromDate", From_Date)
        jsonObject.put("ToDate", To_Date)

        val call = ApiUtils.apiInterface.getHotelBlockDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<HotelBlockReportResponse> {
            override fun onResponse(call: Call<HotelBlockReportResponse>, response: Response<HotelBlockReportResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        arrHotelVoucherList.clear()
                        arrHotelVoucherList = arrayList.hotelLists!!

                        if(arrHotelVoucherList.size > 0) {
                            adapter = HotelBlockReportAdapter(this@HotelBlockReportViewActivity, arrHotelVoucherList)
                            rvHotelVoucherList.adapter = adapter
                        }
                        hideProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<HotelBlockReportResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
}