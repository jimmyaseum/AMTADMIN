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
import com.app.amtadminapp.activity.EditProfileActivity
import com.app.amtadminapp.activity.Vouchers.Airline.AddAirlineVoucherActivity
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.adapter.voucher.AirLineVoucherAdapter
import com.app.amtadminapp.model.response.AirlineVoucherModel
import com.app.amtadminapp.model.response.AirlineVoucherResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_airline_voucher.*
import kotlinx.android.synthetic.main.activity_airline_voucher.idNestedSV
import kotlinx.android.synthetic.main.activity_airline_voucher.idPBLoading
import kotlinx.android.synthetic.main.activity_airline_voucher.imgBack
import kotlinx.android.synthetic.main.activity_airline_voucher.imgFilter
import kotlinx.android.synthetic.main.layout_no_data.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AirlineVoucherActivity : BaseActivity(), RecyclerClickListener {

    lateinit var adapter: AirLineVoucherAdapter
    private var arrAirLineVoucherList: ArrayList<AirlineVoucherModel> = ArrayList()

    var pageno = 1
    var isloading = true

    var sharedPreference: SharedPreference? = null

    var AirlineVoucherNo = ""
    var TourBookingNo = ""
    var Name = ""
    var PNR = ""
    var TicketPurchased = ""
    var Journey = ""
    var NoOfPax = ""
    var TotalPrice = ""
    var Status = ""
    var BookByID = 0
    var BookBy = ""
    var BranchID = 0
    var Branch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airline_voucher)
        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener {
            finish()
        }

        imgFilter.setOnClickListener {
            val intent = Intent(this, AirLineVoucherListFilterActivity::class.java)
            intent.putExtra("AirlineVoucherNo", AirlineVoucherNo)
            intent.putExtra("TourBookingNo", TourBookingNo)
            intent.putExtra("Name", Name)
            intent.putExtra("PNR", PNR)
            intent.putExtra("TicketPurchased", TicketPurchased)
            intent.putExtra("Journey", Journey)
            intent.putExtra("NoOfPax", NoOfPax)
            intent.putExtra("TotalPrice", TotalPrice)
            intent.putExtra("Status", Status)
            intent.putExtra("BookByID", BookByID)
            intent.putExtra("BookBy", BookBy)
            intent.putExtra("BranchID", BranchID)
            intent.putExtra("Branch", Branch)
            startActivityForResult(intent, 1000)
        }

        rvAirLineVoucherList.setLayoutManager(LinearLayoutManager(this))
        arrAirLineVoucherList.clear()

        adapter = AirLineVoucherAdapter(this@AirlineVoucherActivity, arrAirLineVoucherList, this@AirlineVoucherActivity)
        rvAirLineVoucherList.adapter = adapter

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

        fabAddAirlineVoucher.setOnClickListener {
            val intent = Intent(this, AddAirlineVoucherActivity::class.java)
            intent.putExtra("State","Add")
            startActivityForResult(intent, 1001)
        }
    }

    private fun CallAPI() {
        if (isConnectivityAvailable(this)) {
            CallAirLineVoucherListAPI()
        } else {
            showHideDesignView(3)
        }
    }

    private fun CallAirLineVoucherListAPI() {

        if(isloading) {
            showProgress()
        }
        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }
        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toString()

        val jsonObject = JSONObject()
        jsonObject.put("CreatedBy",CreatedBy)
        jsonObject.put("AirlineVoucherNo",AirlineVoucherNo)
        jsonObject.put("TourBookingNo",TourBookingNo)
        jsonObject.put("Name",Name)
        jsonObject.put("PNR",PNR)
        jsonObject.put("TicketPurchased",TicketPurchased)
        jsonObject.put("Journey",Journey)
        jsonObject.put("NoOfPax",NoOfPax)
        jsonObject.put("TotalPrice",TotalPrice.trim())
        jsonObject.put("BookBy",BookBy.trim())
        jsonObject.put("Branch",Branch.trim())
        jsonObject.put("Status",Status)
        jsonObject.put("Pagesize",10)
        jsonObject.put("PageNo",pageno)

        val call = ApiUtils.apiInterface2.GetAllAirlineVoucherList(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<AirlineVoucherResponse> {
            override fun onResponse(call: Call<AirlineVoucherResponse>, response: Response<AirlineVoucherResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        isloading = false
                        if(pageno == 1) {
                            arrAirLineVoucherList.clear()
                            arrAirLineVoucherList.addAll(arrayList)
                            idNestedSV.scrollTo(0,0)
                        } else {
                            arrAirLineVoucherList.addAll(arrayList)
                        }

                        if (arrAirLineVoucherList.size > 0) {
//                            adapter = AirLineVoucherAdapter(this@AirLineVoucherActivity, arrAirLineVoucherList)
//                            rvAirLineVoucherList.adapter = adapter
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
            override fun onFailure(call: Call<AirlineVoucherResponse>, t: Throwable) {
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
                    Name = data!!.getStringExtra("Name").toString()
                    PNR = data!!.getStringExtra("PNR").toString()
                    TicketPurchased = data!!.getStringExtra("TicketPurchased").toString()
                    Journey = data!!.getStringExtra("Journey").toString()
                    NoOfPax = data!!.getStringExtra("NoOfPax").toString()
                    TotalPrice = data!!.getStringExtra("TotalPrice").toString()
                    Status = data!!.getStringExtra("Status").toString()
                    BookByID = data!!.getIntExtra("BookByID",0)
                    BookBy = data!!.getStringExtra("BookBy").toString()
                    BranchID = data!!.getIntExtra("BranchID",0)
                    Branch = data!!.getStringExtra("Branch").toString()

                    //Pass above data to API
                    pageno = 1
                    isloading = true
                    CallAPI()

                }
                1001 -> {
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

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        when ( type ) {
            1 -> {
                val intent = Intent(this, AddAirlineVoucherActivity::class.java)
                intent.putExtra("State","Update")
                intent.putExtra("AVID",arrAirLineVoucherList[position].ID)
                intent.putExtra("TPDate",arrAirLineVoucherList[position].TicketPurchasedDate)
                intent.putExtra("TBNO",arrAirLineVoucherList[position].TourBookingNo)
                intent.putExtra("TotalPrice",arrAirLineVoucherList[position].TotalPrice.toString())
                intent.putExtra("AVDocument",arrAirLineVoucherList[position].AirlineVoucherTicket)
                intent.putExtra("DeparturePNR",arrAirLineVoucherList[position].DeparturePNRNo)
                intent.putExtra("ReturnPNR",arrAirLineVoucherList[position].ArrivalPNRNo)
                startActivityForResult(intent, 1001)
            }
        }
    }


}