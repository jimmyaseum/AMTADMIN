package com.app.amtadminapp.activity.Dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.Dashboard.PendingFormListAdapter
import com.app.amtadminapp.model.response.PendingFormListModel
import com.app.amtadminapp.model.response.PendingFormListResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_pending_form_list.*
import kotlinx.android.synthetic.main.activity_pending_form_list.idNestedSV
import kotlinx.android.synthetic.main.activity_pending_form_list.idPBLoading
import kotlinx.android.synthetic.main.activity_pending_form_list.imgBack
import kotlinx.android.synthetic.main.layout_no_data.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingFormListActivity : BaseActivity() {

    lateinit var adapter: PendingFormListAdapter
    private var arrPendingFormList: ArrayList<PendingFormListModel> = ArrayList()

    var pageno = 1
    var isloading = true

    var FromDate: String = ""
    var ToDate: String = ""

    var sharedPreference: SharedPreference? = null

    //Pending Form - Filter
    var TourBookingNo = ""
    var Name = ""
    var MobileNo = ""
    var SectorID = 0
    var Sector = ""
    var TravelType = ""
    var TourID = 0
    var Tour = ""
    var TourDateCode = ""
    var TourStartDate = ""
    var NoOfNights = ""
    var PendingStepFrom = ""
    var BookByID = 0
    var BookBy = ""
    var BranchID = 0
    var Branch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_form_list)
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
            val intent = Intent(this, PendingFormFilterActivity::class.java)
            intent.putExtra("TourBookingNo", TourBookingNo)
            intent.putExtra("Name", Name)
            intent.putExtra("MobileNo", MobileNo)
            intent.putExtra("SectorID", SectorID)
            intent.putExtra("Sector", Sector)
            intent.putExtra("TravelType", TravelType)
            intent.putExtra("TourID", TourID)
            intent.putExtra("Tour", Tour)
            intent.putExtra("TourDateCode", TourDateCode)
            intent.putExtra("TourStartDate", TourStartDate)
            intent.putExtra("NoOfNights", NoOfNights)
            intent.putExtra("PendingStepFrom",PendingStepFrom)
            intent.putExtra("BookByID", BookByID)
            intent.putExtra("BookBy", BookBy)
            intent.putExtra("BranchID", BranchID)
            intent.putExtra("Branch", Branch)
            startActivityForResult(intent, 1000)
        }

        rvPendingFormList.setLayoutManager(LinearLayoutManager(this))
        arrPendingFormList.clear()

        adapter = PendingFormListAdapter(this@PendingFormListActivity, arrPendingFormList)
        rvPendingFormList.adapter = adapter

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
            CallPendingFormListAPI()
        } else {
            showHideDesignView(3)
        }
    }

    private fun CallPendingFormListAPI() {

        if(isloading) {
            showProgress()
        }

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }
        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        var traveltype = ""
        if(TravelType.equals("SHOW ALL")) {
            traveltype = ""
        } else {
            traveltype = TravelType
        }

        val jsonObject = JSONObject()
        jsonObject.put("CreatedBy",CreatedBy)
        jsonObject.put("FromDate",FromDate.trim())
        jsonObject.put("ToDate",ToDate.trim())
        jsonObject.put("BookingNo",TourBookingNo.trim())
        jsonObject.put("Name",Name.trim())
        jsonObject.put("MobileNo",MobileNo.trim())
        jsonObject.put("Sector",Sector.trim())
        jsonObject.put("TravelType",traveltype.trim())
        jsonObject.put("Tour",Tour.trim())
        jsonObject.put("TourDateCode",TourDateCode.trim())
        jsonObject.put("NoOfNights",NoOfNights.trim())
        jsonObject.put("TourStartDate",TourStartDate.trim())
        jsonObject.put("BookingStep",PendingStepFrom.trim())
        jsonObject.put("BookBy",BookBy.trim())
        jsonObject.put("Branch",Branch.trim())
        jsonObject.put("Pagesize",10)
        jsonObject.put("PageNo",pageno)

        val call = ApiUtils.apiInterface2.GetDashboardPendingFormList(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<PendingFormListResponse> {
            override fun onResponse(call: Call<PendingFormListResponse>, response: Response<PendingFormListResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        isloading = false
                        if(pageno == 1) {
                            arrPendingFormList.clear()
                            arrPendingFormList.addAll(arrayList)
                            idNestedSV.scrollTo(0,0)
                        } else {
                            arrPendingFormList.addAll(arrayList)
                        }

                        if (arrPendingFormList.size > 0) {
//                            adapter = PendingFormListAdapter(this@PendingFormListActivity, arrPendingFormList)
//                            rvPendingFormList.adapter = adapter

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
            override fun onFailure(call: Call<PendingFormListResponse>, t: Throwable) {
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
                    TourBookingNo = data!!.getStringExtra("TourBookingNo").toString()
                    Name = data!!.getStringExtra("Name").toString()
                    MobileNo = data!!.getStringExtra("MobileNo").toString()
                    SectorID = data!!.getIntExtra("SectorID",0)
                    Sector = data!!.getStringExtra("Sector").toString()
                    TravelType = data!!.getStringExtra("TravelType").toString()
                    TourID = data!!.getIntExtra("TourID",0)
                    Tour = data!!.getStringExtra("Tour").toString()
                    TourDateCode = data!!.getStringExtra("TourDateCode").toString()
                    TourStartDate = data!!.getStringExtra("TourStartDate").toString()
                    NoOfNights = data!!.getStringExtra("NoOfNights").toString()
                    PendingStepFrom = data!!.getStringExtra("PendingStepFrom").toString()
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
                imgNoData.setImageResource(R.drawable.pending_forms)

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