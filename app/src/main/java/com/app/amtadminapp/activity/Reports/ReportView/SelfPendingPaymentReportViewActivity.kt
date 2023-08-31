package com.app.amtadminapp.activity.Reports.ReportView

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Report.SelfPendingPaymentReportAdapter
import com.app.amtadminapp.model.response.SelfPendingPaymentReportModel
import com.app.amtadminapp.model.response.SelfPendingPaymentReportResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_self_pending_payment_report_view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class SelfPendingPaymentReportViewActivity : BaseActivity(), View.OnClickListener {

    var sharedPreference: SharedPreference? = null

    var EmployeeID = ""

    lateinit var adapter: SelfPendingPaymentReportAdapter
    private var arrVehicleAllotmentReportList: ArrayList<SelfPendingPaymentReportModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_pending_payment_report_view)

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

        jsonObject.put("CreatedBy", EmployeeID)

        val call = ApiUtils.apiInterface.getSelfPendingPaymentReportDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<SelfPendingPaymentReportResponse> {
            override fun onResponse(call: Call<SelfPendingPaymentReportResponse>, response: Response<SelfPendingPaymentReportResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        arrVehicleAllotmentReportList.clear()
                        arrVehicleAllotmentReportList = arrayList

                        if(arrVehicleAllotmentReportList.size > 0) {
                            adapter = SelfPendingPaymentReportAdapter(this@SelfPendingPaymentReportViewActivity, arrVehicleAllotmentReportList)
                            rvPendingPaymentList.adapter = adapter
                        }
                        hideProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<SelfPendingPaymentReportResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
}