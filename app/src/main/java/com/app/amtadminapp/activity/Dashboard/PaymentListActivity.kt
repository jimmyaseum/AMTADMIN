package com.app.amtadminapp.activity.Dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Dashboard.PaymentListAdapter
import com.app.amtadminapp.model.response.PaymentListModel
import com.app.amtadminapp.model.response.PaymentListResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_payment_list.*
import kotlinx.android.synthetic.main.layout_no_data.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentListActivity : BaseActivity() {

    lateinit var adapter: PaymentListAdapter
    private var arrPaymentList: ArrayList<PaymentListModel> = ArrayList()

    var pageno = 1
    var isloading = true

    var FromDate: String = ""
    var ToDate: String = ""

    var sharedPreference: SharedPreference? = null

    //Payment - Filter
    var ReceiptNo = ""
    var TourBookingNo = ""
    var PaymentDate = ""
    var PaymentFor = ""
    var Name = ""
    var CompanyID = 0
    var CompanyName = ""
    var PaymentType = ""
    var ReceivedID = 0
    var ReceivedBy = ""
    var BranchID = 0
    var Branch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_list)
        getIntentData()
        initializeView()
    }

    private fun getIntentData() {
        FromDate = intent.getStringExtra("FromDate").toString()
        ToDate = intent.getStringExtra("ToDate").toString()
    }

    override fun initializeView() {

        imgBack.setOnClickListener {
            finish()
        }

        imgFilter.setOnClickListener {
            val intent = Intent(this, PaymentFilterActivity::class.java)
            intent.putExtra("ReceiptNo", ReceiptNo)
            intent.putExtra("TourBookingNo", TourBookingNo)
            intent.putExtra("PaymentDate", PaymentDate)
            intent.putExtra("PaymentFor", PaymentFor)
            intent.putExtra("Name", Name)
            intent.putExtra("CompanyID",CompanyID)
            intent.putExtra("CompanyName", CompanyName)
            intent.putExtra("PaymentType", PaymentType)
            intent.putExtra("ReceivedID", ReceivedID)
            intent.putExtra("ReceivedBy", ReceivedBy)
            intent.putExtra("BranchID", BranchID)
            intent.putExtra("Branch", Branch)
            startActivityForResult(intent, 1000)
        }

        rvPaymentList.setLayoutManager(LinearLayoutManager(this))
        arrPaymentList.clear()

        adapter = PaymentListAdapter(this@PaymentListActivity, arrPaymentList)
        rvPaymentList.adapter = adapter

        CallAPI()

        idNestedSV.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight) {
                pageno++
                idPBLoading.visible()
                CallAPI()
            }
        }

        tvRetry.setOnClickListener {
            CallAPI()
        }
    }

    private fun CallAPI() {
        if (isConnectivityAvailable(this)) {
            CallPaymentListAPI()
        } else {
            showHideDesignView(3)
        }
    }

    private fun CallPaymentListAPI() {

        if(isloading) {
            showProgress()
        }

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }
        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()


        val jsonObject = JSONObject()
        jsonObject.put("CreatedBy",CreatedBy)
        jsonObject.put("FromDate",FromDate.trim())
        jsonObject.put("ToDate",ToDate.trim())
        jsonObject.put("Pagesize",10)
        jsonObject.put("PageNo",pageno)
        jsonObject.put("ReceiptNo",ReceiptNo.trim())
        jsonObject.put("TourBookingNo",TourBookingNo.trim())
        jsonObject.put("PaymentDate",PaymentDate.trim())
        jsonObject.put("PaymentFor",PaymentFor.trim())
        jsonObject.put("Name",Name.trim())
        jsonObject.put("CompanyName",CompanyName.trim())
        jsonObject.put("PaymentType",PaymentType.trim())
        jsonObject.put("ReceivedBy",ReceivedBy.trim())
        jsonObject.put("Branch",Branch.trim())

        val call = ApiUtils.apiInterface2.GetDashboardPaymentList(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<PaymentListResponse> {
            override fun onResponse(call: Call<PaymentListResponse>, response: Response<PaymentListResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        isloading = false
                        if(pageno == 1) {
                            arrPaymentList.clear()
                            arrPaymentList.addAll(arrayList)
                            idNestedSV.scrollTo(0,0)
                        } else {
                            arrPaymentList.addAll(arrayList)
                        }

                        if (arrPaymentList.size > 0) {
//                            adapter = PaymentListAdapter(this@PaymentListActivity, arrPaymentList)
//                            rvPaymentList.adapter = adapter
                            adapter.notifyDataSetChanged()
                            showHideDesignView(1)
                            idPBLoading.gone()

                            Handler().postDelayed({  hideProgress() }, 1500)

                        } else {
                            showHideDesignView(2)
                            idPBLoading.gone()
                        }

                    } else {
                        idPBLoading.gone()
                        if(pageno == 1) {
                            hideProgress()
                            showHideDesignView(2)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<PaymentListResponse>, t: Throwable) {
                hideProgress()
                showHideDesignView(4)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                1000 -> {

                    ReceiptNo = data!!.getStringExtra("ReceiptNo").toString()
                    TourBookingNo = data!!.getStringExtra("TourBookingNo").toString()
                    PaymentDate = data!!.getStringExtra("PaymentDate").toString()
                    PaymentFor = data!!.getStringExtra("PaymentFor").toString()
                    Name = data!!.getStringExtra("Name").toString()
                    CompanyID = data!!.getIntExtra("CompanyID",0)
                    CompanyName = data!!.getStringExtra("CompanyName").toString()
                    PaymentType = data!!.getStringExtra("PaymentType").toString()
                    ReceivedID = data!!.getIntExtra("ReceivedID",0)
                    ReceivedBy = data!!.getStringExtra("ReceivedBy").toString()
                    BranchID = data!!.getIntExtra("BranchID",0)
                    Branch = data!!.getStringExtra("Branch").toString()

                    //Pass above data to API

                    pageno = 1
                    isloading = true
                    CallAPI()

                }
            }
        }
    }

    private fun showHideDesignView(mode: Int) {

        when (mode) {
            1 -> {
                idNestedSV.visible()
                rlMainNoData.gone()
            }
            2 -> {

                tvNoData.text = "No Data Found"
                imgNoData.setImageResource(R.drawable.payment)

                idNestedSV.gone()
                rlMainNoData.visible()
                tvRetry.gone()
            }
            3 -> {
                tvNoData.text = getString(R.string.msg_no_internet)
                imgNoData.setImageResource(R.drawable.ic_no_internet)

                idNestedSV.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
            4 -> {
                tvNoData.text = getString(R.string.msg_oops_something_went_wrong)
                imgNoData.setImageResource(R.drawable.ic_oops)

                idNestedSV.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
        }
    }
}