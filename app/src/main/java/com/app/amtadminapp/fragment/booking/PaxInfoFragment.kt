package com.app.amtadminapp.fragment.booking

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.TourBookingFormActivity
import com.app.amtadminapp.adapter.AddMorePaxUpdate
import com.app.amtadminapp.adapter.dialog.DialogPaxTypeAdapter
import com.app.amtadminapp.fragment.BaseFragment
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.model.response.booking.PaxTypeListModel
import com.app.amtadminapp.model.response.booking.PaxTypeListResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.adapter_add_more_pax.*
import kotlinx.android.synthetic.main.fragment_pax_info.view.*
import kotlinx.android.synthetic.main.fragment_pax_info.view.LLcardButtonNext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaxInfoFragment   : BaseFragment(), RecyclerClickListener {
    private var views: View? = null
    var sharedPreference: SharedPreference? = null

    private var arrPaxList: ArrayList<TourPaxInformationModel> = ArrayList()

    var arrayList: ArrayList<TPIM>? = null
    lateinit var adapter: AddMorePaxUpdate

    var arrayListPaxType: ArrayList<PaxTypeListModel>? = ArrayList()
    var positionListItem: Int = 0
    var selectedPaxTypeposition = 0

    var PaxTypeID = 0
    var RateNoOfNights = 0
    var RoomTypeID = 0
    var SpecialityTypeID = 0
    var TourBookingID = 0
    var TourID = 0
    var VehicleSharingPaxID = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_pax_info, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(activity!!)

        views!!.txtTourBookingNo.text = "Tour Booking No : "+ AppConstant.BookingNo

        views!!.rvPaxInfo.layoutManager = LinearLayoutManager(
            activity!!.applicationContext,
            RecyclerView.VERTICAL, false
        )
        views!!.rvPaxInfo.isNestedScrollingEnabled = false

        GetAllPaxTypeAPI()

        views!!.LLcardButtonNext.setOnClickListener {

//            val jsonArray = JSONArray()
//            if (::adapter.isInitialized) {
//                val arrayList1 = adapter.getAdapterArrayList()
//                Log.e("arrayList1","===>"+arrayList1)
//            }

            var tourFormActivity: TourBookingFormActivity = activity as TourBookingFormActivity
            tourFormActivity.updateStepsColor(4)
            tourFormActivity.CURRENT_STEP_POSITION = 4

            val fragment = OthersFragment()
            replaceFragment(R.id.container, fragment, OthersFragment::class.java.simpleName)
        }
    }

    private fun callDetailApi(tourbookingid: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.getTourBookingInfo(tourbookingid)

        call.enqueue(object : Callback<TourBookingInfoResponse> {
            override fun onResponse(call: Call<TourBookingInfoResponse>, response: Response<TourBookingInfoResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                         RateNoOfNights = arrayList[0].NoOfNights!!
                         RoomTypeID = arrayList[0].RoomTypeID!!
                         SpecialityTypeID = arrayList[0].SpecialityTypeID!!
                         TourBookingID = arrayList[0].ID!!
                         TourID = arrayList[0].TourID!!
                         VehicleSharingPaxID = arrayList[0].VehicleSharingPaxID!!

                        AppConstant.Travel_Type = arrayList[0].TravelType!!
                        arrPaxList.clear()
                        arrPaxList = arrayList[0].roomPaxes!!

//                        if(arrPaxList.size > 0) {
//                            setAdapterData(arrPaxList)
//                        }

                        if(arrPaxList.size > 0) {
                            var arrayListP = ArrayList<TPIM>()
                            for(i in 0 until arrPaxList.size) {

                                var paxid = 0
                                var paxname = ""
                                for(j in 0 until arrayListPaxType!!.size) {
                                    if(arrayListPaxType!![j].ID == arrPaxList!![i].PaxTypeID!!) {
                                        paxid = arrayListPaxType!![j].ID!!
                                        paxname = arrayListPaxType!![j].PaxType!!
                                    }
                                }

                                arrayListP.add(TPIM(roomNo = arrPaxList[i].RoomNo!!,
                                    paxTypeID =  paxid,
                                    paxType = paxname,
                                    paxData = arrPaxList[i].paxData!!))
                            }

                            if (arrayListP.size > 0) {
                                setAdapterDataPlace(arrayListP)
                            } else {
                                setDefaultDataPlace()
                            }
                        } else {
                            setDefaultDataPlace()
                        }
                    }
                    hideProgress()
                }
            }
            override fun onFailure(call: Call<TourBookingInfoResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun setDefaultDataPlace() {
        arrayList = ArrayList()
        val arr = ArrayList<paxDataModel>()
        arrayList?.add(TPIM("",0,"", arr))
        setAdapterDataPlace(arrayList)
    }

    private fun setAdapterDataPlace(arrayList: ArrayList<TPIM>?) {
        adapter = AddMorePaxUpdate(arrayList, this)
        views!!.rvPaxInfo.adapter = adapter
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        hideKeyboard(activity!!.applicationContext, view)

        when (view.id)
        {
            R.id.imgDelete -> {
                if (::adapter.isInitialized) {
                    adapter.remove(position)
                }
            }
            R.id.tvAddMore -> {
                preventTwoClick(view)
                if (::adapter.isInitialized) {
                    val arr : ArrayList<paxDataModel> = ArrayList()
                    adapter.addItem(TPIM("",0,"", arr), position)

                }
            }
            R.id.etPaxType -> {
                preventTwoClick(view)
                if (::adapter.isInitialized) {

                    if (!etPaxType.text.toString().isEmpty()) {
                        for (i in arrayListPaxType!!.indices) {
                            if (type == arrayListPaxType!![i].ID!!) {
                                selectedPaxTypeposition = i
                                positionListItem = position
                            } else {
                                positionListItem = position
                            }
                        }
                        showPaxTypeDialog()
                    } else {
                        selectedPaxTypeposition = 0
                        positionListItem = position
                        showPaxTypeDialog()
                    }
                }
            }
        }
    }

    // region PAX Type

    /** AI005
     * This method is to retrived GetAllPaxType List data from api
     */
    private fun GetAllPaxTypeAPI() {

        showProgress()
        val call = ApiUtils.apiInterface.getAllPaxType()

        call.enqueue(object : Callback<PaxTypeListResponse> {
            override fun onResponse(call: Call<PaxTypeListResponse>, response: Response<PaxTypeListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        hideProgress()
                        arrayListPaxType = response.body()?.Data!!
                        callDetailApi(AppConstant.TOURBookingID)

                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<PaxTypeListResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open Pax Type dialog
     */
    private fun showPaxTypeDialog() {
        var dialogSelectPaxType = Dialog(activity!!)
        dialogSelectPaxType.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectPaxType.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectPaxType.window!!.attributes)

        dialogSelectPaxType.window!!.attributes = lp
        dialogSelectPaxType.setCancelable(true)
        dialogSelectPaxType.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectPaxType.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectPaxType.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectPaxType.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectPaxType.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogPaxTypeAdapter(activity!!, arrayListPaxType!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                val pax = arrayListPaxType!![pos].PaxType!!
                val paxid = arrayListPaxType!![pos].ID!!
                val paxcount = arrayListPaxType!![pos].PaxCount!!

                PaxTypeID = paxid

                CallPaxTourCostAPI(pax, paxid, paxcount)
//                adapter.updateItem(positionListItem, pax, paxid, paxcount)
                dialogSelectPaxType!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectPaxType!!.show()
    }

    private fun CallPaxTourCostAPI(pax : String , paxid: Int , paxcount: Int) {
        showProgress()
        val jsonObject = JSONObject()
        jsonObject.put("PaxTypeID",PaxTypeID)
        jsonObject.put("RateNoOfNights",RateNoOfNights)
        jsonObject.put("RoomTypeID",RoomTypeID)
        jsonObject.put("SpecialityTypeID",SpecialityTypeID)
        jsonObject.put("TourBookingID",TourBookingID)
        jsonObject.put("TourID",TourID)
        jsonObject.put("VehicleSharingPaxID",VehicleSharingPaxID)

        val call = ApiUtils.apiInterface.Getpaxtourcost(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<PaxTourCostResponse> {
            override fun onResponse(call: Call<PaxTourCostResponse>, response: Response<PaxTourCostResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        adapter.updateItem(positionListItem, pax, paxid, paxcount , arrayList)
                        hideProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
            override fun onFailure(call: Call<PaxTourCostResponse>, t: Throwable) {
                hideProgress()
            }
        })
    }

    // endregion

}