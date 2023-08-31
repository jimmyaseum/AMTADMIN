package com.app.amtadminapp.activity.Reports.ReportView

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Report.VehicleAllotmentReportAdapter
import com.app.amtadminapp.model.response.VehicleAllotmentReportModel
import com.app.amtadminapp.model.response.VehicleAllotmentReportResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_pending_payment_report_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class VehicleAllotmentReportViewActivity : BaseActivity(), View.OnClickListener {

    var TourDateCode = ""

    lateinit var adapter: VehicleAllotmentReportAdapter
    private var arrVehicleAllotmentReportList: ArrayList<VehicleAllotmentReportModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_allotment_report_view)

        TourDateCode = intent.getStringExtra("TourDateCode").toString()

        initializeView()
    }

    override fun initializeView() {
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
        jsonObject.put("TourDateCode", TourDateCode)

        val call = ApiUtils.apiInterface.getVehicleAllotmentDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<VehicleAllotmentReportResponse> {
            override fun onResponse(call: Call<VehicleAllotmentReportResponse>, response: Response<VehicleAllotmentReportResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        arrVehicleAllotmentReportList.clear()
                        arrVehicleAllotmentReportList = arrayList

                        if(arrVehicleAllotmentReportList.size > 0) {
                            adapter = VehicleAllotmentReportAdapter(this@VehicleAllotmentReportViewActivity, arrVehicleAllotmentReportList)
                            rvPendingPaymentList.adapter = adapter
                        }
                        hideProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<VehicleAllotmentReportResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
}