package com.app.amtadminapp.fragment.booking

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.TourBookingFormActivity
import com.app.amtadminapp.adapter.AddMorePlaceAdapter
import com.app.amtadminapp.adapter.dialog.DialogHotelAdapter
import com.app.amtadminapp.adapter.dialog.DialogMealAdapter
import com.app.amtadminapp.adapter.dialog.DialogPlaceAdapter
import com.app.amtadminapp.fragment.BaseFragment
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.interFase.RecyclerMultipleItemClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.model.response.booking.*
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.adapter_add_more_place.*
import kotlinx.android.synthetic.main.fragment_place.view.*
import kotlinx.android.synthetic.main.fragment_place.view.LLcardButtonNext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlaceFragment  : BaseFragment(), RecyclerMultipleItemClickListener{
    private var views: View? = null
    var sharedPreference: SharedPreference? = null

    var arrayListP: ArrayList<AllCityDataModel>? = ArrayList()
    lateinit var adapterPlace: AddMorePlaceAdapter

    var arrayListPlace: ArrayList<AllCityListModel>? = ArrayList()
    var positionListItemPlace: Int = 0
    var selectedPlaceTypeposition = 0

    var calDate = Calendar.getInstance()
    var arrayListMeal: ArrayList<MealListModel>? = ArrayList()

    private var arrPlaceList: ArrayList<TourPlaceDateModel> = ArrayList()

    var Sectorid = 0
    var tourid = 0
    var tourdate = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_place, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {

        views!!.txtTourBookingNo.text = "Tour Booking No : "+ AppConstant.BookingNo

        sharedPreference = SharedPreference(activity!!)

        showProgress()

        GetAllMealAPI(1)

        callDetailApi(AppConstant.TOURBookingID)

        views!!.rvPlace.layoutManager = LinearLayoutManager(
            activity!!.applicationContext,
            RecyclerView.VERTICAL, false
        )

        views!!.rvPlace.isNestedScrollingEnabled = false

        views!!.LLcardButtonNext.setOnClickListener {

            preventTwoClick(it)
//            val isvalidate = isValidationPage()
//            if(isvalidate) {
                CallStep2AddAPI()
//            }
        }

        views!!.LLcardADDPLACE.setOnClickListener {
            if (::adapterPlace.isInitialized) {

                val arra = adapterPlace.getAdapterArrayList()
                val c = Calendar.getInstance()
                c.time = SimpleDateFormat("dd/MM/yyyy").parse(arra!![0].checkindate)
                c.add(Calendar.DATE, 1)
                val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                val mcheckoutdate = sdf1.format(c.time)

                adapterPlace.addItem(0, AllCityDataModel(checkindate = arra!![0].checkindate , nights = "1" , checkoutdate = mcheckoutdate))

            } else {
                setDefaultDataPlace(0)
            }
        }

    }

    private fun CallStep2AddAPI() {
        if (isConnectivityAvailable(activity!!)) {
            CallAddAPI()
        } else {
            activity!!.toast(getString(R.string.str_msg_no_internet), AppConstant.TOAST_SHORT)
        }
    }

    private fun CallAddAPI() {

        showProgress()

        val jsonArray = JSONArray()
        if (::adapterPlace.isInitialized) {
            val arrayList1 = adapterPlace.getAdapterArrayList()
            for(i in 0 until arrayList1!!.size) {

                val position = i + 1

                val mcheckinDate = convertDateStringToString(
                    arrayList1!![i].checkindate,
                    AppConstant.dd_MM_yyyy_Slash,
                    AppConstant.yyyy_MM_dd_Dash
                )!!
                val mcheckoutDate = convertDateStringToString(
                    arrayList1!![i].checkoutdate,
                    AppConstant.dd_MM_yyyy_Slash,
                    AppConstant.yyyy_MM_dd_Dash
                )!!

                val jsonObjectEducation = JSONObject()
                jsonObjectEducation.put("TourID", tourid)
                jsonObjectEducation.put("TourBookingID", AppConstant.TOURBookingID)
                jsonObjectEducation.put("HotelID", arrayList1!![i].hotelid)
                jsonObjectEducation.put("PlaceID", arrayList1!![i].placeid)
                jsonObjectEducation.put("MealPlanID", arrayList1!![i].planid)
                jsonObjectEducation.put("CheckinDate", mcheckinDate)
                jsonObjectEducation.put("NoOfNights", arrayList1!![i].nights.toInt())
                jsonObjectEducation.put("CheckOutDate",mcheckoutDate)
                jsonObjectEducation.put("Position", position)
                jsonArray.put(jsonObjectEducation)
            }
        }

        val jsonObject = JSONObject()


        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        jsonObject.put("CreatedBy", CreatedBy)
        jsonObject.put("IsActive", true)
        jsonObject.put("TourBookingID", AppConstant.TOURBookingID)
        jsonObject.put("TourPlaceBooking",jsonArray)

        val call = ApiUtils.apiInterface2.AddTourBookingPlace(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.code == 200) {

                        var tourFormActivity: TourBookingFormActivity = activity as TourBookingFormActivity
                        tourFormActivity.updateStepsColor(2)
                        tourFormActivity.CURRENT_STEP_POSITION = 2

                        val fragment = PersonalInfoFragment()
                        replaceFragment(R.id.container, fragment, PersonalInfoFragment::class.java.simpleName)

                        hideProgress()

                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.message!!, AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)

            }
        })
    }

    private fun setDefaultDataPlace(pos: Int) {
        arrayListP = ArrayList()
        arrayListP?.add(pos, AllCityDataModel())
        setAdapterDataPlace(arrayListP)
    }

    private fun setAdapterDataPlace(arrayList: ArrayList<AllCityDataModel>?) {
        views!!.llContent.visible()

        adapterPlace = AddMorePlaceAdapter(arrayList, this)
        views!!.rvPlace.adapter = adapterPlace

        Handler().postDelayed({  hideProgress() }, 1500)

    }

    override fun onItemClickEvent(view: View, position: Int, type: Int, name: String) {
        hideKeyboard(activity!!.applicationContext, view)

        when (view.id)
        {
            R.id.imgDelete -> {
                if (::adapterPlace.isInitialized) {
                    adapterPlace.remove(position)
                }
            }
            R.id.LLcardButtonAddMore -> {
                preventTwoClick(view)
                if (::adapterPlace.isInitialized) {
                    if(name != "") {
                        //Calculate date from no of night
                        val c = Calendar.getInstance()
                        c.time = SimpleDateFormat("dd/MM/yyyy").parse(name)
                        c.add(Calendar.DATE, type)
                        val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                        val mcheckoutdate = sdf1.format(c.time)

                        adapterPlace.addItem(position + 1, AllCityDataModel(checkindate = name , nights = type.toString() , checkoutdate = mcheckoutdate))
                    }
                }
            }
            R.id.etPlace -> {
                preventTwoClick(view)
                if (::adapterPlace.isInitialized) {
                    if (!etPlace.text.toString().isEmpty()) {
                        for (i in arrayListPlace!!.indices) {
                            if (name in arrayListPlace!![i].City!!) {
                                selectedPlaceTypeposition = i
                                positionListItemPlace = position

                            }
                        }
                        showPlaceDialog()
                    } else {
                        selectedPlaceTypeposition = 0
                        positionListItemPlace = position
                        showPlaceDialog()
                    }
                }
            }
            R.id.etHotel -> {
                preventTwoClick(view)
                if (::adapterPlace.isInitialized) {
                    callHotelDetailApi(type,position)
                }
            }
            R.id.etPlan -> {
                preventTwoClick(view)
                if (::adapterPlace.isInitialized) {
                    selectMealDialog(position)
                }
            }
            R.id.etCheckInDate -> {
                if (::adapterPlace.isInitialized) {
                    positionListItemPlace = position
                    showDatePickerDialog(position, 1, type)
                }
            }
            R.id.etNights -> {
                if (::adapterPlace.isInitialized) {
                    positionListItemPlace = position
                    adapterPlace.updateItemNight()

//                    if (name != "") {
//                        //Calculate date from no of night
//                        val c = Calendar.getInstance()
//                        c.time = SimpleDateFormat("dd/MM/yyyy").parse(name)
//                        c.add(Calendar.DATE, type)
//                        val sdf1 = SimpleDateFormat("dd/MM/yyyy")
//                        val checkoutdate = sdf1.format(c.time)
//
//                        adapterPlace.updateItemDate1(positionListItemPlace, checkoutdate)
//                    }
                }
            }
        }
    }

    private fun showDatePickerDialog(position: Int, i: Int, night: Int) {
        val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calDate.set(Calendar.YEAR, year)
            calDate.set(Calendar.MONTH, monthOfYear)
            calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val checkindate = SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(calDate.time)

            //Calculate date from no of night
            val c = Calendar.getInstance()
            c.time = SimpleDateFormat("dd/MM/yyyy").parse(checkindate)
            c.add(Calendar.DATE, night)
            val sdf1 = SimpleDateFormat("dd/MM/yyyy")
            val checkoutdate = sdf1.format(c.time)

            adapterPlace.updateItemDate(positionListItemPlace, checkindate , checkoutdate)
        },
            calDate.get(Calendar.YEAR),
            calDate.get(Calendar.MONTH),
            calDate.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show()
    }

    /* AI005 */
    private fun GetAllCityBYSectorAPI() {

        val jsonObject = JSONObject()
        jsonObject.put("SectorID", Sectorid)

        val call = ApiUtils.apiInterface.getAllCityBySector(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<AllCityListResponse> {
            override fun onResponse(call: Call<AllCityListResponse>, response: Response<AllCityListResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        arrayListPlace = response.body()?.Data!!
                    }
                }
            }

            override fun onFailure(call: Call<AllCityListResponse>, t: Throwable) {
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    /** AI005
     * This method is to open Pax Type dialog
     */
    private fun showPlaceDialog() {
        var dialogSelectPlace = Dialog(activity!!)
        dialogSelectPlace.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectPlace.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectPlace.window!!.attributes)

        dialogSelectPlace.window!!.attributes = lp
        dialogSelectPlace.setCancelable(true)
        dialogSelectPlace.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectPlace.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectPlace.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectPlace.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectPlace.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogPlaceAdapter(activity!!, arrayListPlace!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                val id = arrayListPlace!![pos].CityID!!
                val name = arrayListPlace!![pos].City!!
                adapterPlace.updateItemPlace(positionListItemPlace, name, id)
                dialogSelectPlace!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectPlace!!.show()
    }

    // region Get Tour Detail Api
    private fun callTourPlaceDateApi() {

        val jsonObject = JSONObject()

        jsonObject.put("TourID", tourid)
        jsonObject.put("TourDate", tourdate)

        val call = ApiUtils.apiInterface.GetTourPlaceDate(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<TourPlaceDateResponse> {
            override fun onResponse(call: Call<TourPlaceDateResponse>, response: Response<TourPlaceDateResponse>) {
                if (response.code() == 200) {
                    val arrayList =  response.body()?.Data!![0]
                    if (response.body()?.Status == 200) {

                        if(arrPlaceList.isNullOrEmpty()) {
                            arrPlaceList = response.body()?.Data!!
                        } else {
                            arrPlaceList.clear()
                            arrPlaceList = response.body()?.Data!!
                        }

                        if(arrPlaceList.size > 0) {
                            var arrayListP = ArrayList<AllCityDataModel>()
                            for(i in 0 until arrPlaceList.size) {
                                val checkindate = convertDateStringToString(
                                    arrPlaceList!![i].CheckinDate!!,
                                    AppConstant.yyyy_MM_dd_Dash,
                                    AppConstant.dd_MM_yyyy_Slash
                                )!!

                                arrayListP.add(i,AllCityDataModel(placeid = arrPlaceList[i].PlaceID!!,
                                    place = arrPlaceList[i].Place!!,
                                    checkindate = checkindate,
                                    nights = arrPlaceList[i].NoOfNights.toString()!!,
                                    checkoutdate = ""))
                            }

                            if (arrayListP.size > 0) {
                                setAdapterDataPlace(arrayListP)
                            } else {
                                setDefaultDataPlace(0)
                            }
                        } else {
                            setDefaultDataPlace(0)
                        }

                    }

                }
            }
            override fun onFailure(call: Call<TourPlaceDateResponse>, t: Throwable) {

                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    // endregion

    private fun callHotelDetailApi(placeid: Int, position: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.getHotelByPlace(placeid)

        call.enqueue(object : Callback<HotelListResponse> {
            override fun onResponse(call: Call<HotelListResponse>, response: Response<HotelListResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        showHotelDialog(arrayList, position)
                        hideProgress()
                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.Message!!, AppConstant.TOAST_SHORT)
                    }

                }
            }
            override fun onFailure(call: Call<HotelListResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    private fun showHotelDialog(arrayList: ArrayList<HotelListModel>, position: Int) {
        var dialogSelectHotel = Dialog(activity!!)
        dialogSelectHotel.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectHotel.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectHotel.window!!.attributes)

        dialogSelectHotel.window!!.attributes = lp
        dialogSelectHotel.setCancelable(true)
        dialogSelectHotel.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectHotel.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectHotel.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectHotel.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectHotel.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogHotelAdapter(activity!!, arrayList)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                val id = arrayList!![pos].ID!!
                val name = arrayList!![pos].HotelName!!
                adapterPlace.updateItemHotel(position, name, id)
                dialogSelectHotel!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectHotel!!.show()
    }

    // region meal
    /** AI005
     * This method is to retrived meal plan data from api
     */
    private fun GetAllMealAPI(mode: Int) {

        val call = ApiUtils.apiInterface.getAllMeal()
        call.enqueue(object : Callback<MealListResponse> {
            override fun onResponse(call: Call<MealListResponse>, response: Response<MealListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {

                        arrayListMeal = response.body()?.Data!!

                    } else {

                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<MealListResponse>, t: Throwable) {

                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open Company dialog
     */
    private fun selectMealDialog(position: Int) {
        var dialogSelectMeal = Dialog(activity!!)
        dialogSelectMeal.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectMeal.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectMeal.window!!.attributes)

        dialogSelectMeal.window!!.attributes = lp
        dialogSelectMeal.setCancelable(true)
        dialogSelectMeal.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectMeal.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectMeal.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectMeal.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectMeal.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogMealAdapter(activity!!, arrayListMeal!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                val id = arrayListMeal!![pos].ID!!
                val name = arrayListMeal!![pos].Title!!
                adapterPlace.updateItemPlan(position, name, id)
                dialogSelectMeal!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectMeal!!.show()
    }
    // endregion
    private fun callDetailApi(tourbookingid: Int) {

        val call = ApiUtils.apiInterface.getTourBookingInfo(tourbookingid)

        call.enqueue(object : Callback<TourBookingInfoResponse> {
            override fun onResponse(call: Call<TourBookingInfoResponse>, response: Response<TourBookingInfoResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

                        Sectorid = arrayList[0].SectorID!!
                        tourid = arrayList[0].TourID!!
                        tourdate = arrayList[0].TourDate!!

                        GetAllCityBYSectorAPI()

                        val arralPlace = arrayList[0].TourPlaceBooking!!
                        if(arralPlace.size > 0) {

                            var arrayListP = ArrayList<AllCityDataModel>()
                            for(i in 0 until arralPlace.size) {
                                val checkindate = convertDateStringToString(
                                    arralPlace!![i].CheckinDate!!,
                                    AppConstant.yyyy_MM_dd_Dash,
                                    AppConstant.dd_MM_yyyy_Slash
                                )!!

                                var mealid = 0
                                var mealname = ""
                                for(j in 0 until arrayListMeal!!.size) {
                                  if(arralPlace[i].MealPlanID != null) {
                                      if (arrayListMeal!![j].ID == arralPlace!![i].MealPlanID!!) {
                                          mealid = arrayListMeal!![j].ID!!
                                          mealname = arrayListMeal!![j].Title!!
                                      }
                                  }
                                }

                                var hotelid = 0
                                if(arralPlace[i].HotelID != null) {
                                    hotelid = arralPlace[i].HotelID!!
                                }
                                var hotelname = ""
                                if(arralPlace[i].HotelName != null) {
                                    hotelname = arralPlace[i].HotelName!!
                                }
                                arrayListP.add(i,AllCityDataModel(placeid = arralPlace[i].PlaceID!!,
                                    place = arralPlace[i].City!!,
                                    hotelid = hotelid,
                                    hotel = hotelname,
                                    checkindate = checkindate,
                                    nights = arralPlace[i].NoOfNights.toString()!!,
                                    checkoutdate = "",
                                    planid = mealid,
                                    plan = mealname))
                            }

                            if (arrayListP.size > 0) {
                                setAdapterDataPlace(arrayListP)
                            } else {
                                setDefaultDataPlace(0)
                            }

                        } else {
                            callTourPlaceDateApi()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<TourBookingInfoResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
}