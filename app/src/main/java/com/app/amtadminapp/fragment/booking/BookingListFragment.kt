package com.app.amtadminapp.fragment.booking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.TourBookingFormActivity
import com.app.amtadminapp.adapter.TourBookingListAdapter
import com.app.amtadminapp.fragment.BaseFragment
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.TourBookingListModel
import com.app.amtadminapp.model.response.TourBookingListResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.fragment_booking_list.view.*
import kotlinx.android.synthetic.main.layout_no_data.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingListFragment : BaseFragment(), View.OnClickListener , RecyclerClickListener {
    private var views: View? = null

    lateinit var adapter: TourBookingListAdapter
    private var arrTourBookingList: ArrayList<TourBookingListModel> = ArrayList()

    var pageno = 1
    var isloading = true

    var sharedPreference: SharedPreference? = null

    //Booking - Filter
    var TourBookingNo = ""
    var Name = ""
    var MobileNo = ""
    var SectorID = 0
    var Sector = ""
    var TravelType = ""
    var TourID = 0
    var Tour = ""
    var TourDateCode = ""
    var NoOfNights = ""
    var GroupBooking = ""
    var Status = ""
    var BookByID = 0
    var BookBy = ""
    var BranchID = 0
    var Branch = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_booking_list, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        initListeners()
    }

    private fun initListeners() {
        views!!.fabAddBooking.setOnClickListener(this)

        views!!.rvBookingList.setLayoutManager(LinearLayoutManager(activity!!))
        arrTourBookingList.clear()

        adapter = TourBookingListAdapter(activity!!, arrTourBookingList, this@BookingListFragment)
        views!!.rvBookingList.adapter = adapter

        CallAPI()

        views!!.idNestedSV.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight) {
                pageno++
                views!!.idPBLoading.visible()
                CallAPI()
            }
        }

        views!!.tvRetry.setOnClickListener {
            pageno = 1
            isloading = true
            CallAPI()
        }

        views!!.swiperefreshlayout.setOnRefreshListener {
            pageno = 1
            isloading = true
            CallAPI()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabAddBooking -> {
                val intent = Intent(activity!!, TourBookingFormActivity::class.java)
                intent.putExtra("state","add")
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                //TourBookingFilterActivity - coming from HomeActivity - onActivityResult
                1003 -> {
                    TourBookingNo = data!!.getStringExtra("TourBookingNo").toString()
                    Name = data!!.getStringExtra("Name").toString()
                    MobileNo = data!!.getStringExtra("MobileNo").toString()
                    SectorID = data!!.getIntExtra("SectorID",0)
                    Sector = data!!.getStringExtra("Sector").toString()
                    TravelType = data!!.getStringExtra("TravelType").toString()
                    TourID = data!!.getIntExtra("TourID",0)
                    Tour = data!!.getStringExtra("Tour").toString()
                    TourDateCode = data!!.getStringExtra("TourDateCode").toString()
                    GroupBooking = data!!.getStringExtra("GroupBooking").toString()
                    NoOfNights = data!!.getStringExtra("NoOfNights").toString()
                    Status = data!!.getStringExtra("Status").toString()
                    BookByID = data!!.getIntExtra("BookByID",0)
                    BookBy = data!!.getStringExtra("BookBy").toString()
                    BranchID = data!!.getIntExtra("BranchID",0)
                    Branch = data!!.getStringExtra("Branch").toString()


                    pageno = 1
                    isloading = true
                    CallAPI()
                }
            }
        }
    }

    private fun CallAPI() {
        if (isConnectivityAvailable(activity!!)) {
            CallBookingListAPI()
        } else {
            showHideDesignView(3)
        }
    }

    private fun CallBookingListAPI() {

        if(isloading) {
            showProgress()
        }

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(activity!!)
        }
        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!

        var traveltype = ""
        if(TravelType.equals("SHOW ALL")) {
            traveltype = ""
        } else {
            traveltype = TravelType
        }

        val jsonObject = JSONObject()
        jsonObject.put("CreatedBy",CreatedBy)
        jsonObject.put("Pagesize",10)
        jsonObject.put("PageNo",pageno)
        jsonObject.put("BookingNo",TourBookingNo.trim())
        jsonObject.put("Name",Name.trim())
        jsonObject.put("MobileNo",MobileNo.trim())
        jsonObject.put("Sector",Sector.trim())
        jsonObject.put("GroupBooking",GroupBooking.trim())
        jsonObject.put("TravelType",traveltype.trim())
        jsonObject.put("Status",Status.trim())
        jsonObject.put("Tour",Tour.trim())
        jsonObject.put("TourDateCode",TourDateCode.trim())
        jsonObject.put("Nights",NoOfNights.trim())
        jsonObject.put("BookBy",BookBy.trim())
        jsonObject.put("Branch",Branch.trim())

        val call = ApiUtils.apiInterface2.GetTourBookingList(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<TourBookingListResponse> {
            override fun onResponse(call: Call<TourBookingListResponse>, response: Response<TourBookingListResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        isloading = false
                        if(pageno == 1) {
                            arrTourBookingList.clear()
                            arrTourBookingList.addAll(arrayList)
                            views!!.idNestedSV.scrollTo(0,0)
                        } else {
                            arrTourBookingList.addAll(arrayList)
                        }

                        if (arrTourBookingList.size > 0) {
//                            adapter = TourBookingListAdapter(activity!!, arrTourBookingList, this@BookingListFragment)
//                            views!!.rvBookingList.adapter = adapter
                            adapter.notifyDataSetChanged()
                            showHideDesignView(1)
                            views!!.idPBLoading.gone()
                            views!!.swiperefreshlayout.isRefreshing = false
                        } else {
                            showHideDesignView(2)
                            hideProgress()
                            views!!.idPBLoading.gone()
                            views!!.swiperefreshlayout.isRefreshing = false
                        }

                    } else {
                        views!!.idPBLoading.gone()
                        if(pageno == 1) {
                            views!!.swiperefreshlayout.isRefreshing = false
                            hideProgress()
                            showHideDesignView(2)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<TourBookingListResponse>, t: Throwable) {
                hideProgress()
                showHideDesignView(4)
            }
        })
    }

    private fun showHideDesignView(mode: Int) {

        when (mode) {
            1 -> {
                views!!.idNestedSV.visible()
                views!!.rlMainNoData.gone()

                Handler().postDelayed({  hideProgress() }, 1500)

            }
            2 -> {

                views!!.tvNoData.text = "No Data Found"
                views!!.imgNoData.setImageResource(R.drawable.booking)

                views!!.idNestedSV.gone()
                views!!.rlMainNoData.visible()
                views!!.tvRetry.gone()
            }
            3 -> {
                views!!.tvNoData.text = getString(R.string.msg_no_internet)
                views!!.imgNoData.setImageResource(R.drawable.ic_no_internet)

                views!!.idNestedSV.gone()
                views!!.rlMainNoData.visible()
                views!!.tvRetry.visible()
            }
            4 -> {
                views!!.tvNoData.text = getString(R.string.msg_oops_something_went_wrong)
                views!!.imgNoData.setImageResource(R.drawable.ic_oops)

                views!!.idNestedSV.gone()
                views!!.rlMainNoData.visible()
                views!!.tvRetry.visible()
            }
        }
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        hideKeyboard(activity!!.applicationContext, view)

        when (view.id) {
            R.id.cardEdit -> {
                val intent = Intent(activity!!, TourBookingFormActivity::class.java)
                intent.putExtra("state","edit")
                intent.putExtra("TourBookingNo",arrTourBookingList[position].TourBookingNo)
                intent.putExtra("ID",arrTourBookingList[position].ID)
                intent.putExtra("CustomerID",arrTourBookingList[position].CustomerID)
                startActivity(intent)
            }
        }
    }
}