package com.app.amtadminapp.activity.Dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Dashboard.FlightVoucherListAdapter
import com.app.amtadminapp.model.response.FlightVoucherListModel
import com.app.amtadminapp.model.response.FlightVoucherListResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_flight_voucher_list.*
import kotlinx.android.synthetic.main.layout_no_data.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FlightVoucherListActivity : BaseActivity() {

    lateinit var adapter: FlightVoucherListAdapter
    private var arrFlightVoucherList: ArrayList<FlightVoucherListModel> = ArrayList()

    var pageno = 1
    var isloading = true

    var FromDate: String = ""
    var ToDate: String = ""

    var sharedPreference: SharedPreference? = null

    // Flight Voucher - Filter
    var AirlineVoucherNo = ""
    var TourBookingNo = ""
    var CompanyID = 0
    var CompanyName = ""
    var TicketPurchaseDate = ""
    var Journey = ""
    var NoOfPax = ""
    var TotalPrice = ""
    var BookByID = 0
    var BookBy = ""
    var BranchID = 0
    var Branch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_voucher_list)
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
            val intent = Intent(this, FlightVoucherFilterActivity::class.java)
            intent.putExtra("AirlineVoucherNo", AirlineVoucherNo)
            intent.putExtra("TourBookingNo", TourBookingNo)
            intent.putExtra("TicketPurchaseDate", TicketPurchaseDate)
            intent.putExtra("CompanyID",CompanyID)
            intent.putExtra("CompanyName", CompanyName)
            intent.putExtra("Journey", Journey)
            intent.putExtra("NoOfPax", NoOfPax)
            intent.putExtra("TotalPrice", TotalPrice)
            intent.putExtra("BookByID", BookByID)
            intent.putExtra("BookBy", BookBy)
            intent.putExtra("BranchID", BranchID)
            intent.putExtra("Branch", Branch)
            startActivityForResult(intent, 1000)
        }

        rvFlightVoucherList.setLayoutManager(LinearLayoutManager(this))
        arrFlightVoucherList.clear()

        adapter = FlightVoucherListAdapter(this@FlightVoucherListActivity, arrFlightVoucherList)
        rvFlightVoucherList.adapter = adapter
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
            CallFlightVoucherListAPI()
        } else {
            showHideDesignView(3)
        }
    }

    private fun CallFlightVoucherListAPI() {

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
        jsonObject.put("AirlineVoucherNo",AirlineVoucherNo.trim())
        jsonObject.put("TourBookingNo",TourBookingNo.trim())
        jsonObject.put("CompanyName",CompanyName.trim())
        jsonObject.put("TicketPurchasedDate",TicketPurchaseDate.trim())
        jsonObject.put("Journey",Journey.trim())
        jsonObject.put("NoOfPax",NoOfPax.trim())
        jsonObject.put("TotalPrice",TotalPrice.trim())
        jsonObject.put("BookBy",BookBy.trim())
        jsonObject.put("Branch",Branch.trim())
        jsonObject.put("Pagesize",10)
        jsonObject.put("PageNo",pageno)

        val call = ApiUtils.apiInterface2.GetDashboardFlightVoucherList(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<FlightVoucherListResponse> {
            override fun onResponse(call: Call<FlightVoucherListResponse>, response: Response<FlightVoucherListResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        isloading = false
                        if(pageno == 1) {
                            arrFlightVoucherList.clear()
                            arrFlightVoucherList.addAll(arrayList)
                            idNestedSV.scrollTo(0,0)
                        } else {
                            arrFlightVoucherList.addAll(arrayList)
                        }

                        if (arrFlightVoucherList.size > 0) {
//                            adapter = FlightVoucherListAdapter(this@FlightVoucherListActivity, arrFlightVoucherList)
//                            rvFlightVoucherList.adapter = adapter

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
            override fun onFailure(call: Call<FlightVoucherListResponse>, t: Throwable) {
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
                    AirlineVoucherNo = data!!.getStringExtra("AirlineVoucherNo").toString()
                    TourBookingNo = data!!.getStringExtra("TourBookingNo").toString()
                    TicketPurchaseDate = data!!.getStringExtra("TicketPurchaseDate").toString()
                    CompanyID = data!!.getIntExtra("CompanyID",0)
                    CompanyName = data!!.getStringExtra("CompanyName").toString()
                    Journey = data!!.getStringExtra("Journey").toString()
                    NoOfPax = data!!.getStringExtra("NoOfPax").toString()
                    TotalPrice = data!!.getStringExtra("TotalPrice").toString()
                    BookByID = data!!.getIntExtra("BookByID",0)
                    BookBy = data!!.getStringExtra("BookBy").toString()
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
                imgNoData.setImageResource(R.drawable.flight_voucher)

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