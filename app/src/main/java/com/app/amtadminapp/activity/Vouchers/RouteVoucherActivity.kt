package com.app.amtadminapp.activity.Vouchers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.adapter.voucher.RouteVoucherAdapter
import com.app.amtadminapp.model.response.RouteVoucherModel
import com.app.amtadminapp.model.response.RouteVoucherResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_route_voucher_list.*
import kotlinx.android.synthetic.main.activity_route_voucher_list.idNestedSV
import kotlinx.android.synthetic.main.activity_route_voucher_list.idPBLoading
import kotlinx.android.synthetic.main.activity_route_voucher_list.imgBack
import kotlinx.android.synthetic.main.activity_route_voucher_list.imgFilter
import kotlinx.android.synthetic.main.layout_no_data.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteVoucherActivity : BaseActivity(), RecyclerClickListener {

    lateinit var adapter: RouteVoucherAdapter
    private var arrRouteVoucherList: ArrayList<RouteVoucherModel> = ArrayList()

    var pageno = 1
    var isloading = true

    var sharedPreference: SharedPreference? = null

    var RouteVoucher = ""
    var TourBookingNo = ""
    var Name = ""
    var SectorID = 0
    var Sector = ""
    var TourID = 0
    var Tour = ""
    var TourDateCode = ""
    var VehicleType = ""
    var BookByID = 0
    var BookBy = ""
    var BranchID = 0
    var Branch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_voucher)
        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener {
            finish()
        }

        imgFilter.setOnClickListener {
            val intent = Intent(this, RouteVoucherListFilterActivity::class.java)
            intent.putExtra("RouteVoucher", RouteVoucher)
            intent.putExtra("TourBookingNo", TourBookingNo)
            intent.putExtra("Name", Name)
            intent.putExtra("SectorID", SectorID)
            intent.putExtra("Sector", Sector)
            intent.putExtra("TourID", TourID)
            intent.putExtra("Tour", Tour)
            intent.putExtra("TourDateCode", TourDateCode)
            intent.putExtra("VehicleType", VehicleType)
            intent.putExtra("BookByID", BookByID)
            intent.putExtra("BookBy", BookBy)
            intent.putExtra("BranchID", BranchID)
            intent.putExtra("Branch", Branch)
            startActivityForResult(intent, 1000)
        }

        rvRouteVoucherList.setLayoutManager(LinearLayoutManager(this))
        arrRouteVoucherList.clear()

        adapter = RouteVoucherAdapter(this@RouteVoucherActivity, arrRouteVoucherList, this@RouteVoucherActivity)
        rvRouteVoucherList.adapter = adapter

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
            CallRouteVoucherListAPI()
        } else {
            showHideDesignView(3)
        }
    }

    private fun CallRouteVoucherListAPI() {

        if(isloading) {
            showProgress()
        }
        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }
        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toString()

        val jsonObject = JSONObject()
        jsonObject.put("CreatedBy",CreatedBy)
        jsonObject.put("RouteVoucherNo",RouteVoucher)
        jsonObject.put("TourBookingNo",TourBookingNo)
        jsonObject.put("Name",Name)
        jsonObject.put("Sector",Sector)
        jsonObject.put("TourName",Tour)
        jsonObject.put("TourDateCode",TourDateCode.trim())
        jsonObject.put("VehicleType",VehicleType.trim())
        jsonObject.put("BookBy",BookBy.trim())
        jsonObject.put("Branch",Branch.trim())
        jsonObject.put("Pagesize",10)
        jsonObject.put("PageNo",pageno)

        val call = ApiUtils.apiInterface2.GetAllRouteVoucherList(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<RouteVoucherResponse> {
            override fun onResponse(call: Call<RouteVoucherResponse>, response: Response<RouteVoucherResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        isloading = false
                        if(pageno == 1) {
                            arrRouteVoucherList.clear()
                            arrRouteVoucherList.addAll(arrayList)
                            idNestedSV.scrollTo(0,0)
                        } else {
                            arrRouteVoucherList.addAll(arrayList)
                        }

                        if (arrRouteVoucherList.size > 0) {
//                            adapter = RouteVoucherAdapter(this@RouteVoucherActivity, arrRouteVoucherList)
//                            rvRouteVoucherList.adapter = adapter
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
            override fun onFailure(call: Call<RouteVoucherResponse>, t: Throwable) {
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
                    RouteVoucher = data!!.getStringExtra("RouteVoucherNo").toString()
                    TourBookingNo = data!!.getStringExtra("TourBookingNo").toString()
                    Name = data!!.getStringExtra("Name").toString()
                    SectorID = data!!.getIntExtra("SectorID",0)
                    Sector = data!!.getStringExtra("Sector").toString()
                    TourID = data!!.getIntExtra("TourID",0)
                    Tour = data!!.getStringExtra("Tour").toString()
                    TourDateCode = data!!.getStringExtra("TourDateCode").toString()
                    VehicleType = data!!.getStringExtra("VehicleType").toString()
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
                imgNoData.setImageResource(R.drawable.route_voucher)

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


    override fun onItemClickEvent(view: View, position: Int, type: Int) {

    }


}