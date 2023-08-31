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


import com.app.amtadminapp.activity.Vouchers.Airline.AddAirlineVoucherActivity
import com.app.amtadminapp.activity.Vouchers.Hotels.AddHotelsVouchersActivity
import com.app.amtadminapp.activity.Vouchers.Hotels.EditHotelsVouchersActivity

import com.app.amtadminapp.activity.Vouchers.hotel.AddHotelVoucherActivity
import com.app.amtadminapp.adapter.voucher.HotelVoucherAdapter
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.HotelVoucherModel
import com.app.amtadminapp.model.response.HotelVoucherResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*


import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.*

import kotlinx.android.synthetic.main.activity_hotel_voucher.*
import kotlinx.android.synthetic.main.activity_hotel_voucher_list.idNestedSV
import kotlinx.android.synthetic.main.activity_hotel_voucher_list.idPBLoading
import kotlinx.android.synthetic.main.activity_hotel_voucher_list.imgBack
import kotlinx.android.synthetic.main.activity_hotel_voucher_list.rvHotelVoucherList
import kotlinx.android.synthetic.main.layout_no_data.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotelVoucherActivity : BaseActivity(), RecyclerClickListener {

    lateinit var adapter: HotelVoucherAdapter
    private var arrHotelVoucherList: ArrayList<HotelVoucherModel> = ArrayList()

    var sharedPreference: SharedPreference? = null
    var pageno = 1
    var isloading = true

    var TourBookingNo = ""
    var Name = ""
    var TourDateCode = ""
    var HotelVoucher = ""
    var TourID = 0
    var TourName = ""
    var TourDate = ""
    var CompanyName = ""
    var NoOfNights = ""
    var VehicleSharing = ""
    var BookBy = ""
    var Branch = ""
    var CompanyID = 0
    var BranchID = 0
    var BookByID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_voucher)
        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener {
            finish()
        }



        // visible gone not used

        fabAddHotelVoucher.setOnClickListener {
            val intent = Intent(this, AddHotelVoucherActivity::class.java)
            intent.putExtra("state","add")
            startActivity(intent)
        }



        fabAddHotelVoucher1.setOnClickListener {
            val intent = Intent(this, AddHotelsVouchersActivity::class.java)
            intent.putExtra("state","add")
            startActivityForResult(intent, 1002)
        }


        imgFilter.setOnClickListener {
            val intent = Intent(this, HotelVoucherListFilterActivity::class.java)
            intent.putExtra("TourBookingNo", TourBookingNo)
            intent.putExtra("Name", Name)
            intent.putExtra("TourDateCode", TourDateCode)
            intent.putExtra("HotelVoucher", HotelVoucher)
            intent.putExtra("TourID",TourID)
            intent.putExtra("TourName", TourName)
            intent.putExtra("TourDate", TourDate)
            intent.putExtra("CompanyName", CompanyName)
            intent.putExtra("NoOfNights", NoOfNights)
            intent.putExtra("VehicleSharing", VehicleSharing)
            intent.putExtra("BookBy", BookBy)
            intent.putExtra("Branch", Branch)
            intent.putExtra("BranchID", BranchID)
            intent.putExtra("CompanyID", CompanyID)
            intent.putExtra("BookByID", BookByID)
            startActivityForResult(intent, 1001)

        }

        rvHotelVoucherList.setLayoutManager(LinearLayoutManager(this))
        arrHotelVoucherList.clear()

        adapter = HotelVoucherAdapter(this@HotelVoucherActivity, arrHotelVoucherList, this)
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
        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!

        val jsonObject = JSONObject()
        jsonObject.put("CreatedBy",CreatedBy)
        jsonObject.put("HotelVoucherNo",HotelVoucher.trim())
        jsonObject.put("TourBookingNo",TourBookingNo.trim())
        jsonObject.put("TourName",TourName.trim())
        jsonObject.put("TourDateCode",TourDateCode.trim())
        jsonObject.put("TourDate",TourDate.trim())
        jsonObject.put("Name",Name.trim())
        jsonObject.put("CompanyName",CompanyName.trim())
        jsonObject.put("NoOfNights",NoOfNights.trim())
        jsonObject.put("VehicleSharing",VehicleSharing.trim())
        jsonObject.put("BookBy",BookBy.trim())
        jsonObject.put("Branch",Branch.trim())
        jsonObject.put("Pagesize",10)
        jsonObject.put("PageNo",pageno)

        val call = ApiUtils.apiInterface2.GetAllHotelVoucherList(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<HotelVoucherResponse> {
            override fun onResponse(call: Call<HotelVoucherResponse>, response: Response<HotelVoucherResponse>) {

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
//                            adapter = HotelVoucherAdapter(this@HotelVoucherActivity, arrHotelVoucherList)
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
            override fun onFailure(call: Call<HotelVoucherResponse>, t: Throwable) {
                hideProgress()
                showHideDesignView(4)
            }
        })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                1001 -> {

                    TourBookingNo = data!!.getStringExtra("TourBookingNo").toString()
                    Name = data!!.getStringExtra("Name").toString()
                    TourDateCode = data!!.getStringExtra("TourDateCode").toString()
                    HotelVoucher = data!!.getStringExtra("HotelVoucher").toString()
                    TourID = data!!.getIntExtra("TourID",0)
                    TourName = data!!.getStringExtra("TourName").toString()
                    TourDate = data!!.getStringExtra("TourDate").toString()
                    CompanyName = data!!.getStringExtra("CompanyName").toString()
                    NoOfNights = data!!.getStringExtra("NoOfNights").toString()
                    VehicleSharing = data!!.getStringExtra("VehicleSharing").toString()
                    BookBy = data!!.getStringExtra("BookBy").toString()
                    Branch = data!!.getStringExtra("Branch").toString()
                    CompanyID = data!!.getIntExtra("CompanyID",0)
                    BookByID = data!!.getIntExtra("BookByID",0)
                    BranchID = data!!.getIntExtra("BranchID",0)
                    //Pass above data to API
                    pageno = 1
                    isloading = true
                    CallAPI()

                }


                1002 -> {
                    CallAPI()
                }

            }
        }
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        hideKeyboard(this.applicationContext, view)

        when (view.id) {
            R.id.cardEdit -> {

                val intent = Intent(this, EditHotelsVouchersActivity::class.java)
                intent.putExtra("state","edit")
                intent.putExtra("TourBookingNos",arrHotelVoucherList[position].TourBookingNo)
                intent.putExtra("ID",arrHotelVoucherList[position].ID)
                startActivityForResult(intent, 1002)

            }
        }
    }
}