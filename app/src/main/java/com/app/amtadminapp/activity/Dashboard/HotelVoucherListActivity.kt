package com.app.amtadminapp.activity.Dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Dashboard.HotelVoucherListAdapter
import com.app.amtadminapp.model.response.HotelVoucherListModel
import com.app.amtadminapp.model.response.HotelVoucherListResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_hotel_voucher_list.*
import kotlinx.android.synthetic.main.layout_no_data.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotelVoucherListActivity : BaseActivity() {

    lateinit var adapter: HotelVoucherListAdapter
    private var arrHotelVoucherList: ArrayList<HotelVoucherListModel> = ArrayList()

    var pageno = 1
    var isloading = true

    var FromDate: String = ""
    var ToDate: String = ""

    var sharedPreference: SharedPreference? = null

    //Hotel Voucher - Filter
    var HotelVoucher = ""
    var TourBookingNo = ""
    var TourID = 0
    var TourName = ""
    var TourDate = ""
    var CompanyID = 0
    var CompanyName = ""
    var NoOfNights = ""
    var VehicleSharing = ""
    var BookByID = 0
    var BookBy = ""
    var BranchID = 0
    var Branch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_voucher_list)
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
            val intent = Intent(this, HotelVoucherFilterActivity::class.java)
            intent.putExtra("HotelVoucher", HotelVoucher)
            intent.putExtra("TourBookingNo",TourBookingNo)
            intent.putExtra("TourID", TourID)
            intent.putExtra("TourName", TourName)
            intent.putExtra("TourDate", TourDate)
            intent.putExtra("CompanyID",CompanyID)
            intent.putExtra("CompanyName", CompanyName)
            intent.putExtra("NoOfNights", NoOfNights)
            intent.putExtra("VehicleSharing", VehicleSharing)
            intent.putExtra("BookByID", BookByID)
            intent.putExtra("BookBy", BookBy)
            intent.putExtra("BranchID", BranchID)
            intent.putExtra("Branch", Branch)
            startActivityForResult(intent, 1000)
        }

        rvHotelVoucherList.setLayoutManager(LinearLayoutManager(this))
        arrHotelVoucherList.clear()

        adapter = HotelVoucherListAdapter(this@HotelVoucherListActivity, arrHotelVoucherList)
        rvHotelVoucherList.adapter = adapter

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
            CallHotelVoucherListAPI()
        } else {
            showHideDesignView(3)
        }
    }

    private fun CallHotelVoucherListAPI() {

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
        jsonObject.put("HotelVoucher",HotelVoucher.trim())
        jsonObject.put("BookingNo",TourBookingNo.trim())
        jsonObject.put("TourName",TourName.trim())
        jsonObject.put("TourDate",TourDate.trim())
        jsonObject.put("CompanyName",CompanyName.trim())
        jsonObject.put("NoOfNights",NoOfNights.trim())
        jsonObject.put("VehicleSharingPax",VehicleSharing.trim())
        jsonObject.put("BookBy",BookBy.trim())
        jsonObject.put("Branch",Branch.trim())
        jsonObject.put("Pagesize",10)
        jsonObject.put("PageNo",pageno)

        val call = ApiUtils.apiInterface2.GetDashboardHotelVoucherList(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<HotelVoucherListResponse> {
            override fun onResponse(call: Call<HotelVoucherListResponse>, response: Response<HotelVoucherListResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        isloading = false
                        if(pageno == 1) {
                            arrHotelVoucherList.clear()
                            arrHotelVoucherList.addAll(arrayList)
                            idNestedSV.scrollTo(0,0)
                        } else {
                            arrHotelVoucherList.addAll(arrayList)
                        }

                        if (arrHotelVoucherList.size > 0) {
//                            adapter = HotelVoucherListAdapter(this@HotelVoucherListActivity, arrHotelVoucherList)
//                            rvHotelVoucherList.adapter = adapter
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
            override fun onFailure(call: Call<HotelVoucherListResponse>, t: Throwable) {
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

                    HotelVoucher = data!!.getStringExtra("HotelVoucher").toString()
                    TourBookingNo = data!!.getStringExtra("TourBookingNo").toString()
                    TourID = data!!.getIntExtra("TourID",0)
                    TourName = data!!.getStringExtra("TourName").toString()
                    TourDate = data!!.getStringExtra("TourDate").toString()
                    CompanyID = data!!.getIntExtra("CompanyID",0)
                    CompanyName = data!!.getStringExtra("CompanyName").toString()
                    NoOfNights = data!!.getStringExtra("NoOfNights").toString()
                    VehicleSharing = data!!.getStringExtra("VehicleSharing").toString()
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
                imgNoData.setImageResource(R.drawable.hotel_voucher)

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