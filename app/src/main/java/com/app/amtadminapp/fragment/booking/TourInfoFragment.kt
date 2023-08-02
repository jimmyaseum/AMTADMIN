package com.app.amtadminapp.fragment.booking

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.Chatbot.ChatConstant
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.TourBookingFormActivity
import com.app.amtadminapp.adapter.AddMorePaxAdapter
import com.app.amtadminapp.adapter.dialog.*
import com.app.amtadminapp.fragment.BaseFragment
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.interFase.RecyclerMultipleItemClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.model.response.booking.*
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.adapter_add_more_pax.*
import kotlinx.android.synthetic.main.activity_tour_booking.view.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TourInfoFragment(var state: String) : BaseFragment(), RecyclerMultipleItemClickListener, View.OnClickListener {

    private var views: View? = null
    var sharedPreference: SharedPreference? = null

    var arrayList: ArrayList<PaxDataModel>? = null
    lateinit var adapter: AddMorePaxAdapter

    var arrayListPaxType: ArrayList<PaxTypeListModel>? = null
    var positionListItem: Int = 0
    var selectedPaxTypeposition = 0

    private val arrLTCList: ArrayList<String> = ArrayList()
    var LTCName : String = ""

    private val arrDiscountTypeList: ArrayList<String> = ArrayList()
    var DiscountTypeName : String = ""

    private var arrMonthList: ArrayList<MonthDataModel> = ArrayList()
    var MonthId : Int = 0
    var MonthName : String = ""

    private var arrDateList: ArrayList<DateDataModel> = ArrayList()
    var DateName : String = ""

    private val arrTravelTypeList: ArrayList<String> = ArrayList()
    var TravelTypeName : String = ""

    private val arrVehicleSharingList: ArrayList<String> = ArrayList()
    var VehicleSharingName : String = ""

    var arrayListSector: ArrayList<SectorListModel>? = null
    var SectorId : Int = 0
    var SectorName : String = ""
    var SectorType : String = ""

    var arrayListSubSector: ArrayList<SubSectorListModel>? = null
    var SubSectorId : Int = 0
    var SubSectorName : String = ""

    var arrayListTourPackage: ArrayList<TourPackageListModel>? = null
    var TourPackageId : Int = 0
    var TourPackageName : String = ""
    var TourCode : String = ""

    var arrayListCompany: ArrayList<CompanyListModel>? = null
    var CompanyId : Int = 0
    var CompanyName : String = ""

    var arrayListSpecialityType: ArrayList<SpecialityTypeListModel>? = null
    var SpecialityTypeId : Int = 0
    var SpecialityTypeName : String = ""

    var arrayListVehicleSharingTourCost: ArrayList<VehicleSharingListModel>? = null
    var VehicleSharingTourCostId : Int = 0
    var VehicleSharingTourCostName : String = ""

    var arrayListRoomType: ArrayList<RoomTypeListModel>? = null
    var RoomTypeId : Int = 0
    var RoomTypeName : String = ""

    var calDate = Calendar.getInstance()

    var IsApplyOnServiceTax : Boolean = false
    var GSTPer : Int = 0
    var TCSPer : Int = 0

    var TotalRoom : Int = 0
    var BalanceRoom : Int = 0

    private var mAuth: FirebaseAuth? = null
    private var RootRef: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.activity_tour_booking, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(activity!!)

        mAuth = FirebaseAuth.getInstance()
        RootRef = FirebaseDatabase.getInstance().reference

        views!!.rvPAX.layoutManager = LinearLayoutManager(
            activity!!.applicationContext,
            RecyclerView.VERTICAL, false
        )
        views!!.rvPAX.isNestedScrollingEnabled = false


        GetAllPaxTypeAPI()
        GetAllSectorAPI(1)
        GetAllSpecialityAPI(1)
        GetAllVehicleSharingAPI(1)
        GetAllRoomTypeAPI(1)

        views!!.rl_tourist_info.setOnClickListener {
            if(views!!.LL_TouristInfo.isVisible()) {
                views!!.LL_TouristInfo.gone()
                views!!.img_tourist_info.setImageResource(R.drawable.ic_keyboard_down)
            } else {
                views!!.LL_TouristInfo.visible()
                views!!.img_tourist_info.setImageResource(R.drawable.ic_keyboard_up)
            }
        }
        views!!.rl_booking_info.setOnClickListener {
            if(views!!.LL_BookingInfo.isVisible()) {
                views!!.LL_BookingInfo.gone()
                views!!.img_booking_info.setImageResource(R.drawable.ic_keyboard_down)
            } else {
                views!!.LL_BookingInfo.visible()
                views!!.img_booking_info.setImageResource(R.drawable.ic_keyboard_up)
            }
        }
        views!!.rl_date_info.setOnClickListener {
            if(views!!.LL_DateInfo.isVisible()) {
                views!!.LL_DateInfo.gone()
                views!!.img_date_info.setImageResource(R.drawable.ic_keyboard_down)
            } else {
                views!!.LL_DateInfo.visible()
                views!!.img_date_info.setImageResource(R.drawable.ic_keyboard_up)
            }
        }
        views!!.rl_pax_info.setOnClickListener {
            if(views!!.LL_PaxInfo.isVisible()) {
                views!!.LL_PaxInfo.gone()
                views!!.img_pax_info.setImageResource(R.drawable.ic_keyboard_down)
            } else {
                views!!.LL_PaxInfo.visible()
                views!!.img_pax_info.setImageResource(R.drawable.ic_keyboard_up)
            }
        }
        views!!.rl_amount_cal.setOnClickListener {
            if(views!!.LL_AmountCal.isVisible()) {
                views!!.LL_AmountCal.gone()
                views!!.img_amount_cal.setImageResource(R.drawable.ic_keyboard_down)
            } else {
                views!!.LL_AmountCal.visible()
                views!!.img_amount_cal.setImageResource(R.drawable.ic_keyboard_up)
            }
        }

        views!!.LLcardButtonNext.setOnClickListener {
            preventTwoClick(it)
            val isvalidate = isValidationPage()
            if(isvalidate) {
                if(state.equals("add")) {
                    CallStep1AddAPI()
                } else {
                    CallStep1UpdateAPI()
                }
            }
        }

        // AI005 IsLTC Type YES Or No
        arrLTCList.add("YES")
        arrLTCList.add("NO")

        // AI005 TravelType Tour Or Package
        arrTravelTypeList.add("TOUR")
        arrTravelTypeList.add("PACKAGE")

        // AI005 Vehicle Sharing Tour Or Package
        arrVehicleSharingList.add("SHARING")
        arrVehicleSharingList.add("SEPRATE")

        // AI005 Discount Type Flat Or Persent
        arrDiscountTypeList.add("FLAT")
        views!!.etDiscountType.setText(arrDiscountTypeList[0])
        DiscountTypeName = arrDiscountTypeList[0]

        views!!.etIsLTC.setOnClickListener(this)
        views!!.etSector.setOnClickListener(this)
        views!!.etSubSector.setOnClickListener(this)
        views!!.etTravelType.setOnClickListener(this)
        views!!.etTourPackgeName.setOnClickListener(this)
        views!!.etCompanyName.setOnClickListener(this)
        views!!.etVehicleSharing.setOnClickListener(this)
        views!!.etTourMonth.setOnClickListener(this)
        views!!.etTourDate.setOnClickListener(this)
        views!!.etTourStartDate.setOnClickListener(this)
        views!!.etTourEndDate.setOnClickListener(this)
        views!!.etSpecialityType.setOnClickListener(this)
        views!!.etRoomType.setOnClickListener(this)
        views!!.etVehicleSharingTourCost.setOnClickListener(this)
        views!!.etDiscountType.setOnClickListener(this)

        views!!.etNights.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    if(views!!.etTourStartDate.text.toString() != "") {
                        //Calculate date from no of night
                        val c = Calendar.getInstance()
                        c.time = SimpleDateFormat("dd/MM/yyyy").parse(views!!.etTourStartDate.text.toString())
                        c.add(Calendar.DATE, char.toString().toInt())
                        val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                        val output = sdf1.format(c.time)
                        views!!.etTourEndDate.setText(output)
                    } else {
                        //Calculate date from no of night
                        val c = Calendar.getInstance()
                        c.add(Calendar.DATE, char.toString().toInt())
                        val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                        val output = sdf1.format(c.time)
                        views!!.etTourEndDate.setText(output)
                    }

                    if(VehicleSharingTourCostId != 0 && TourPackageId != 0 && SpecialityTypeId != 0
                        && RoomTypeId != 0 && views!!.etNights.text.toString().trim() != "0") {
                        var check = true
                        if (::adapter.isInitialized) {
                            val arrayList1 = adapter.getAdapterArrayList()
                            for (i in 0 until arrayList1!!.size) {
                                if (TextUtils.isEmpty(arrayList1[i].noofroom)) {
                                    check = false
                                }
                            }
                            if(check) {
                                callGetTourCostApi()
                            }
                        }
                    }
                } else {
                    views!!.etNights.setText("0")

                    if(VehicleSharingTourCostId != 0 && TourPackageId != 0 && SpecialityTypeId != 0
                        && RoomTypeId != 0 && views!!.etNights.text.toString().trim() != "0") {
                        var check = true
                        if (::adapter.isInitialized) {
                            val arrayList1 = adapter.getAdapterArrayList()
                            for (i in 0 until arrayList1!!.size) {
                                if (TextUtils.isEmpty(arrayList1[i].noofroom)) {
                                    check = false
                                }
                            }
                            if(check) {
                                callGetTourCostApi()
                            }
                        }
                    }
                }
            }
        })

        views!!.etNoofRooms.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    if(BalanceRoom != 0) {
                        if(TravelTypeName.equals("TOUR")) {
                            if(char.toString().toInt() > BalanceRoom) {
                                activity!!.toast("Rooms are not available",Toast.LENGTH_LONG)
                                views!!.etNoofRooms.text!!.clear()
                            }
                        }
                    }
                }
            }
        })

        views!!.etTotalAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    if(views!!.etTotalAmount.text.toString() != "") {

                        CalculateAmount()
                    }

                }
            }
        })

        views!!.etTotalExtraCost.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    if(views!!.etTotalAmount.text.toString() != "") {

                        CalculateAmount()
                    }

                }
            }
        })

        views!!.etDiscountValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    views!!.etDiscountAmount.setText(strSearch)
                    if(views!!.etTotalAmount.text.toString() != "") {
                        CalculateAmount()
                    }
                } else {
                    views!!.etDiscountValue.setText("0")
                    views!!.etDiscountAmount.setText("0")
                    CalculateAmount()
                }
            }
        })

        views!!.etAdminDiscountAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    if(views!!.etTotalAmount.text.toString() != "") {
                        CalculateAmount()
                    }
                } else {
                    views!!.etAdminDiscountAmount.setText("0")
                    CalculateAmount()
                }
            }
        })

        views!!.etKasar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    if(views!!.etTotalAmount.text.toString() != "") {

                        CalculateAmount()
                    }

                }
            }
        })

        views!!.etServiceCharge.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    if(views!!.etTotalAmount.text.toString() != "") {

                        CalculateAmount()
                    }

                }
            }
        })

        if(state.equals("edit")) {
            callDetailApi(AppConstant.TOURBookingID)
        }

    }

    private fun CallStep1AddAPI() {
        if (isConnectivityAvailable(activity!!)) {
            CallAddAPI()
        } else {
            activity!!.toast(getString(R.string.str_msg_no_internet), AppConstant.TOAST_SHORT)
        }
    }

    private fun CallStep1UpdateAPI() {
        if (isConnectivityAvailable(activity!!)) {
            CallUpdateAPI()
        } else {
            activity!!.toast(getString(R.string.str_msg_no_internet), AppConstant.TOAST_SHORT)
        }
    }

    private fun CallAddAPI() {

        showProgress()

        val jsonArray = JSONArray()
        if (::adapter.isInitialized) {
            val arrayList1 = adapter.getAdapterArrayList()

            for(i in 0 until arrayList1!!.size) {

                val jsonObjectEducation = JSONObject()
                jsonObjectEducation.put("PaxTypeID", arrayList1!![i].paxid)
                jsonObjectEducation.put("NoOfRoom", arrayList1!![i].noofroom.toInt())
                jsonArray.put(jsonObjectEducation)
            }
        }

//      IsActive , IsDelete, OTP, OldTourBookingNo , TotalAdults , TotalExtraAdults , TotalCWB ,
//      TotalCNB , TotalInfants , TotalNoOfPax

        val jsonObject = JSONObject()

        var isLTC : Boolean
        if(LTCName.equals("YES")) {
            isLTC = true
        } else {
            isLTC = false
        }
        var DiscountType : Int
        if(DiscountTypeName.equals("FLAT")) {
            DiscountType = 1
        } else {
            DiscountType = 0
        }

        val mDateName = convertDateStringToString(
            views!!.etTourDate.text.toString(),
            AppConstant.dd_MM_yyyy_Slash,
            AppConstant.yyyy_MM_dd_Dash
        )!!

        val mStartDate = convertDateStringToString(
            views!!.etTourStartDate.text.toString(),
            AppConstant.dd_MM_yyyy_Slash,
            AppConstant.yyyy_MM_dd_Dash
        )!!

        val mEndDate = convertDateStringToString(
            views!!.etTourEndDate.text.toString(),
            AppConstant.dd_MM_yyyy_Slash,
            AppConstant.yyyy_MM_dd_Dash
        )!!

        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()


        jsonObject.put("CreatedBy", CreatedBy)
        jsonObject.put("IsLTCTour", isLTC)
        jsonObject.put("SectorID", SectorId)
        jsonObject.put("SubSectorID", SubSectorId)
        jsonObject.put("TourID", TourPackageId)
        jsonObject.put("RoomTypeID", RoomTypeId)
        jsonObject.put("CompanyID",CompanyId)
        jsonObject.put("TravelType",TravelTypeName)
        jsonObject.put("TourMonth",MonthId.toString())

        if(TravelTypeName.equals("TOUR")) {
            jsonObject.put("TourDate",mDateName)
        } else {
            jsonObject.put("TourDate",mStartDate)
        }

        jsonObject.put("AlongWith",views!!.etAlongWith.text)
        jsonObject.put("TourDateCode",views!!.etTourCode.text)
        jsonObject.put("TourStartDate",mStartDate)
        jsonObject.put("VehicleSharing",views!!.etVehicleSharing.text)
        jsonObject.put("NoOfNights",views!!.etNights.text)
        jsonObject.put("TourEndDate",mEndDate)
        jsonObject.put("FirstName",views!!.etFirstName.text)
        jsonObject.put("LastName",views!!.etLastName.text)
        jsonObject.put("MobileNo",views!!.etMobile.text)
        jsonObject.put("EmailID",views!!.etEmailId.text)
        jsonObject.put("TotalNoOfRooms",views!!.etNoofRooms.text)
        jsonObject.put("TotalNoOfSeats",views!!.etNoofSeats.text)
        jsonObject.put("VehicleSharingPaxID",VehicleSharingTourCostId)
        jsonObject.put("SpecialityTypeID",SpecialityTypeId)
        jsonObject.put("ExtraBed",views!!.etExtraBed.text)
        jsonObject.put("TotalAmount",views!!.etTotalAmount.text)
        jsonObject.put("TotalCost",views!!.etTotalCost.text)
        jsonObject.put("DiscountType",DiscountType)
        jsonObject.put("DiscountValue",views!!.etDiscountValue.text)
        jsonObject.put("DiscountAmount",views!!.etDiscountAmount.text)
        jsonObject.put("AdminDiscountAmount",views!!.etAdminDiscountAmount.text)
        jsonObject.put("TotalDiscountAmount",views!!.etTotalDiscountAmount.text)
        jsonObject.put("TotalTCSAmount",views!!.etTotalTCS.text)
        jsonObject.put("TotalGSTAmount",views!!.etTotalGST.text)
        jsonObject.put("TotalExtraCost",views!!.etTotalExtraCost.text)
        jsonObject.put("Kasar",views!!.etKasar.text)
        jsonObject.put("ServiceChargeAmount",views!!.etServiceCharge.text)
        jsonObject.put("PaxTypeList",jsonArray)

        val call = ApiUtils.apiInterface2.AddTourBooking(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CommonResponse2> {
            override fun onResponse(call: Call<CommonResponse2>, response: Response<CommonResponse2>) {
                if (response.code() == 200) {
                    if (response.body()?.code == 200) {

                        hideProgress()

                        val data = response.body()?.Data!!
                        AppConstant.TOURBookingID = data.ID
                        AppConstant.CustomerID = data.CustomerID
                        AppConstant.BookingNo = data.BookingNo

                        val tourFormActivity: TourBookingFormActivity = activity!! as TourBookingFormActivity
                        tourFormActivity.updateStepsColor(1)
                        tourFormActivity.CURRENT_STEP_POSITION = 1
                        tourFormActivity.isAPICallFirst = true
                        tourFormActivity.state = "edit"

                        val fragment = PlaceFragment()
                        replaceFragment(R.id.container, fragment, PlaceFragment::class.java.simpleName)

//                        CreateNewUserFirebase(views!!.etEmailId.text.toString(),views!!.etFirstName.text.toString(),views!!.etLastName.text.toString(),views!!.etMobile.text.toString(), data.CustomerID)
                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.message.toString() , Toast.LENGTH_SHORT)
                    }
                } else {
                    hideProgress()
                    activity!!.toast(response.body()?.message.toString() , Toast.LENGTH_SHORT)
                }
            }
            override fun onFailure(call: Call<CommonResponse2>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun CallUpdateAPI() {

        showProgress()

        val jsonArray = JSONArray()
        if (::adapter.isInitialized) {
            val arrayList1 = adapter.getAdapterArrayList()
            for(i in 0 until arrayList1!!.size) {
                val jsonObjectEducation = JSONObject()
                jsonObjectEducation.put("PaxTypeID", arrayList1!![i].paxid)
                jsonObjectEducation.put("NoOfRoom", arrayList1!![i].noofroom.toInt())
                jsonArray.put(jsonObjectEducation)
            }
        }
        val jsonObject = JSONObject()

        var isLTC : Boolean
        if(LTCName.equals("YES")) {
            isLTC = true
        } else {
            isLTC = false
        }
        var DiscountType : Int
        if(DiscountTypeName.equals("FLAT")) {
            DiscountType = 1
        } else {
            DiscountType = 0
        }

        val mDateName = convertDateStringToString(
            DateName,
            AppConstant.dd_MM_yyyy_Slash,
            AppConstant.yyyy_MM_dd_Dash
        )!!

        val mStartDate = convertDateStringToString(
            views!!.etTourStartDate.text.toString(),
            AppConstant.dd_MM_yyyy_Slash,
            AppConstant.yyyy_MM_dd_Dash
        )!!

        val mEndDate = convertDateStringToString(
            views!!.etTourEndDate.text.toString(),
            AppConstant.dd_MM_yyyy_Slash,
            AppConstant.yyyy_MM_dd_Dash
        )!!

        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        jsonObject.put("ID", AppConstant.TOURBookingID)
        jsonObject.put("UpdatedBy", CreatedBy)
        jsonObject.put("IsLTCTour", isLTC)
        jsonObject.put("SectorID", SectorId)
        jsonObject.put("SubSectorID", SubSectorId)
        jsonObject.put("TourID", TourPackageId)
        jsonObject.put("RoomTypeID", RoomTypeId)
        jsonObject.put("CompanyID",CompanyId)
        jsonObject.put("TravelType",TravelTypeName)
        jsonObject.put("TourMonth",MonthId.toString())

        if(TravelTypeName.equals("TOUR")) {
            jsonObject.put("TourDate",mDateName)
        } else {
            jsonObject.put("TourDate",mStartDate)
        }

        jsonObject.put("AlongWith",views!!.etAlongWith.text)
        jsonObject.put("TourDateCode",views!!.etTourCode.text)
        jsonObject.put("TourStartDate",mStartDate)
        jsonObject.put("VehicleSharing",views!!.etVehicleSharing.text)
        jsonObject.put("NoOfNights",views!!.etNights.text)
        jsonObject.put("TourEndDate",mEndDate)
        jsonObject.put("FirstName",views!!.etFirstName.text)
        jsonObject.put("LastName",views!!.etLastName.text)
        jsonObject.put("MobileNo",views!!.etMobile.text)
        jsonObject.put("EmailID",views!!.etEmailId.text)
        jsonObject.put("TotalNoOfRooms",views!!.etNoofRooms.text)
        jsonObject.put("TotalNoOfSeats",views!!.etNoofSeats.text)
        jsonObject.put("VehicleSharingPaxID",VehicleSharingTourCostId)
        jsonObject.put("SpecialityTypeID",SpecialityTypeId)
        jsonObject.put("ExtraBed",views!!.etExtraBed.text)
        jsonObject.put("TotalAmount",views!!.etTotalAmount.text)
        jsonObject.put("TotalCost",views!!.etTotalCost.text)
        jsonObject.put("DiscountType",DiscountType)
        jsonObject.put("DiscountValue",views!!.etDiscountValue.text)
        jsonObject.put("DiscountAmount",views!!.etDiscountAmount.text)
        jsonObject.put("AdminDiscountAmount",views!!.etAdminDiscountAmount.text)
        jsonObject.put("TotalDiscountAmount",views!!.etTotalDiscountAmount.text)
        jsonObject.put("TotalTCSAmount",views!!.etTotalTCS.text)
        jsonObject.put("TotalGSTAmount",views!!.etTotalGST.text)
        jsonObject.put("TotalExtraCost",views!!.etTotalExtraCost.text)
        jsonObject.put("Kasar",views!!.etKasar.text)
        jsonObject.put("ServiceChargeAmount",views!!.etServiceCharge.text)
//        jsonObject.put("PaxTypeList",jsonArray)

        val call = ApiUtils.apiInterface2.UpdateTourBooking(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CommonResponse2> {
            override fun onResponse(call: Call<CommonResponse2>, response: Response<CommonResponse2>) {
                if (response.code() == 200) {
                    if (response.body()?.code == 200) {

                        hideProgress()

                        val tourFormActivity: TourBookingFormActivity = activity!! as TourBookingFormActivity
                        tourFormActivity.updateStepsColor(1)
                        tourFormActivity.CURRENT_STEP_POSITION = 1
                        tourFormActivity.isAPICallFirst = true

                        val fragment = PlaceFragment()
                        replaceFragment(R.id.container, fragment, PlaceFragment::class.java.simpleName)
                    }
                    else {
                        hideProgress()
                        activity!!.toast(response.body()?.message.toString() , Toast.LENGTH_SHORT)
                    }
                } else {
                    hideProgress()
                    activity!!.toast(response.body()?.message.toString() , Toast.LENGTH_SHORT)
                }
            }
            override fun onFailure(call: Call<CommonResponse2>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun CalculateAmount() {
        if(IsApplyOnServiceTax) {
            if(views!!.etTotalAmount.text.toString() != "") {
                val totalamount = views!!.etTotalAmount.text.toString().toDouble()
                val totalextraamount = views!!.etTotalExtraCost.text.toString().toDouble()
                val discountamount = views!!.etDiscountAmount.text.toString().toDouble()
                val admindiscountamount = views!!.etAdminDiscountAmount.text.toString().toDouble()
                val totaldiscountamt = discountamount + admindiscountamount

                views!!.etTotalDiscountAmount.setText(totaldiscountamt.toString())

                val kasar = views!!.etKasar.text.toString().toDouble()

                val total_amount = (totalamount + totalextraamount) - totaldiscountamt

                val serviceTax = views!!.etServiceCharge.text.toString().toDouble()
                val finalamount_gst = (serviceTax * GSTPer) / 100
                var finalamount_tcs = 0.0

                if(SectorType.equals("INTERNATIONAL") || SectorType.equals("International")) {
                    finalamount_tcs = (serviceTax * TCSPer) / 100
                }

                views!!.etTotalGST.setText(finalamount_gst.toString())
                views!!.etTotalTCS.setText(finalamount_tcs.toString())

                val finalAmount = (total_amount) - kasar
                views!!.etTotalCost.setText(finalAmount.toInt().toString())
            }
        } else {
            if(views!!.etTotalAmount.text.toString() != "") {
                val totalamount = views!!.etTotalAmount.text.toString().toDouble()
                val totalextraamount = views!!.etTotalExtraCost.text.toString().toDouble()
                val discountamount = views!!.etDiscountAmount.text.toString().toDouble()
                val admindiscountamount = views!!.etAdminDiscountAmount.text.toString().toDouble()
                val totaldiscountamt = discountamount + admindiscountamount

                views!!.etTotalDiscountAmount.setText(totaldiscountamt.toString())

                val kasar = views!!.etKasar.text.toString().toDouble()

                val total_amount = (totalamount + totalextraamount) - totaldiscountamt
                val finalamount_gst = (total_amount * GSTPer) / 100
                var finalamount_tcs = 0.0

                if(SectorType.equals("INTERNATIONAL") || SectorType.equals("International")) {
                     finalamount_tcs = (total_amount * TCSPer) / 100
                }

                views!!.etTotalGST.setText(finalamount_gst.toString())
                views!!.etTotalTCS.setText(finalamount_tcs.toString())

                val finalAmount = (total_amount + finalamount_gst + finalamount_tcs) - kasar
                views!!.etTotalCost.setText(finalAmount.toInt().toString())
            }
        }

    }

    private fun setDefaultData() {
        arrayList = ArrayList()
        arrayList?.add(PaxDataModel(paxid = 0, pax = "", noofroom = ""))
        setAdapterData(arrayList,state)
    }

    private fun setAdapterData(arrayList: ArrayList<PaxDataModel>?, state: String) {
        adapter = AddMorePaxAdapter(arrayList, this, state)
        views!!.rvPAX.adapter = adapter
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int, name: String) {
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
                    adapter.addItem(PaxDataModel(), 1)
                }
            }
            R.id.etPaxType -> {
                preventTwoClick(view)
                if (::adapter.isInitialized) {

                    if (!etPaxType.text.toString().isEmpty()) {
                        for (i in arrayListPaxType!!.indices) {
                            if (name in arrayListPaxType!![i].PaxType!!) {
                                selectedPaxTypeposition = i
                                positionListItem = position

                            }
                        }

                        val isvalidate = isValidation()
                        if(isvalidate) {
                            showPaxTypeDialog()
                        }
                    } else {
                        selectedPaxTypeposition = 0
                        positionListItem = position
                        val isvalidate = isValidation()
                        if(isvalidate) {
                            showPaxTypeDialog()
                        }
                    }
                }
            }
            R.id.etNoofRooms1 -> {
                if (::adapter.isInitialized) {
                    positionListItem = position
                    callGetTourCostApi()
                }
            }
        }
    }

    private fun isValidation(): Boolean {
        var check = true

        if (views!!.etNights.text.toString().isEmpty()) {
            check = false
            views!!.etNights.setError("Please enter no of nights")
        }
        if (VehicleSharingTourCostId == 0) {
            check = false
            views!!.etVehicleSharingTourCost.setError("Please select Vehicle sharing for Tour Cost")
        }
        if(SpecialityTypeId == 0) {
            check = false
            views!!.etSpecialityType.setError("Please select Speciality Type")
        }
        if(RoomTypeId == 0) {
            check = false
            views!!.etRoomType.setError("Please select Room Type")
        }
        if(TourPackageId == 0) {
            check = false
            views!!.etTourPackgeName.setError("Please select Tour Package")
        }
        if(TravelTypeName.equals("TOUR")) {
            if(MonthId == 0) {
                check = false
                views!!.etTourMonth.setError("Please select Tour Month")
            }
            if(DateName == "") {
                check = false
                views!!.etTourDate.setError("Please select Tour Date")
            }
        }

        return check
    }

    private fun isValidationPage(): Boolean {
        var check = true

        if (views!!.etFirstName.text.toString().isEmpty()) {
            check = false
            views!!.etFirstName.setError("Please enter first name")
        }
        if (views!!.etLastName.text.toString().isEmpty()) {
            check = false
            views!!.etLastName.setError("Please enter last name")
        }
        if (views!!.etMobile.text.toString().isEmpty()) {
            check = false
            views!!.etMobile.setError("Please enter mobile number")
        }
        if (views!!.etEmailId.text.toString().isEmpty()) {
            check = false
            views!!.etEmailId.setError("Please enter email id")
        }
        if (!views!!.etEmailId.text.toString().isValidEmail()) {
            check = false
            views!!.etEmailId.setError("Please enter valid email id")
        }
        if (SectorName == "") {
            check = false
            views!!.etSector.setError("Please select Sector")
        }
        if (TravelTypeName == "") {
            check = false
            views!!.etTravelType.setError("Please select Travel Type")
        }
        if(TourPackageId == 0) {
            check = false
            views!!.etTourPackgeName.setError("Please select Tour/Package Name")
        }
        if(CompanyId == 0) {
            check = false
            views!!.etCompanyName.setError("Please select Company Name")
        }
        if (views!!.etNoofRooms.text.toString().isEmpty()) {
            check = false
            views!!.etNoofRooms.setError("Please enter no of rooms")
        }
        if (views!!.etTotalAmount.text.toString().isEmpty()) {
            check = false
            views!!.etTotalAmount.setError("Please enter total amount")
        }
        if (::adapter.isInitialized) {
            val arrayList1 = adapter.getAdapterArrayList()
            for (i in 0 until arrayList1!!.size) {
                if (TextUtils.isEmpty(arrayList1[i].pax)) {
                    check = false
                    activity!!.toast("Please enter Pax Information",Toast.LENGTH_SHORT)
                }
                if (TextUtils.isEmpty(arrayList1[i].noofroom)) {
                    check = false
                    activity!!.toast("Please enter Pax Information",Toast.LENGTH_SHORT)
                }
            }
        }

        return check
    }

    override fun onClick(v: View?) {
        hideKeyboard(activity!!, v)
        when (v?.id) {
            R.id.etIsLTC -> {
                preventTwoClick(v)
                selectLTCDialog()
            }
            R.id.etSector -> {
                preventTwoClick(v)
                if(!arrayListSector.isNullOrEmpty()) {
                    selectSectorDialog()
                } else {
                    GetAllSectorAPI(2)
                }
            }
            R.id.etSubSector -> {
                preventTwoClick(v)
                if(SectorId != 0) {
                    if (!arrayListSubSector.isNullOrEmpty()) {
                        selectSubSectorDialog()
                    } else {
                        GetAllSubSectorAPI(2)
                    }
                } else {
                    activity!!.toast("Please Select Sector Name.",Toast.LENGTH_SHORT)
                }
            }
            R.id.etTravelType -> {
                preventTwoClick(v)
                selectTravelTypeDialog()
            }
            R.id.etTourPackgeName -> {
                preventTwoClick(v)
                if(SectorId != 0 && TravelTypeName != "") {
                    if (!arrayListTourPackage.isNullOrEmpty()) {
                        selectTourPackageDialog()
                    } else {
                        GetAllTourPackageAPI(2)
                    }
                } else {
                    activity!!.toast("Please Select Sector & Travel Type.",Toast.LENGTH_SHORT)
                }
            }
            R.id.etCompanyName -> {
                preventTwoClick(v)
                if(!arrayListCompany.isNullOrEmpty()) {
                    selectCompanyDialog()
                } else {
                    GetAllCompanyAPI(2)
                }
            }
            R.id.etTourMonth  -> {
                preventTwoClick(v)
                if(!arrMonthList.isNullOrEmpty()) {
                    selectMonthDialog()
                } else {
                    activity!!.toast("Tour Month Not Available.", AppConstant.TOAST_SHORT)
                }
            }
            R.id.etTourDate  -> {
                preventTwoClick(v)
                if(!arrDateList.isNullOrEmpty()) {
                    views!!.txterror.gone()
                    selectDateDialog()
                } else {
                    activity!!.toast("Tour Data Not Available.", AppConstant.TOAST_SHORT)
                }
            }
            R.id.etTourStartDate  -> {

                preventTwoClick(v)
                if(views!!.etTourDate.text.toString() != "") {
                    calDate.time = SimpleDateFormat("dd/MM/yyyy").parse(views!!.etTourDate.text.toString())
                    val dpd = DatePickerDialog(
                        activity!!,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            calDate.set(Calendar.YEAR, year)
                            calDate.set(Calendar.MONTH, monthOfYear)
                            calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                            val date =
                                SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(
                                    calDate.time
                                )
                            views!!.etTourStartDate.setText(date)

                            //Calculate date from no of night
                            val c = Calendar.getInstance()
                            c.time = SimpleDateFormat("dd/MM/yyyy").parse(date)
                            c.add(Calendar.DATE, views!!.etNights.text.toString().toInt())
                            val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                            val output = sdf1.format(c.time)
                            views!!.etTourEndDate.setText(output)
                        },
                        calDate.get(Calendar.YEAR),
                        calDate.get(Calendar.MONTH),
                        calDate.get(Calendar.DAY_OF_MONTH)
                    )
                    dpd.show()
                } else {
                    val dpd = DatePickerDialog(
                        activity!!,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            calDate.set(Calendar.YEAR, year)
                            calDate.set(Calendar.MONTH, monthOfYear)
                            calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                            val date =
                                SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(
                                    calDate.time
                                )
                            views!!.etTourStartDate.setText(date)

                            //Calculate date from no of night
                            val c = Calendar.getInstance()
                            c.time = SimpleDateFormat("dd/MM/yyyy").parse(date)
                            c.add(Calendar.DATE, views!!.etNights.text.toString().toInt())
                            val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                            val output = sdf1.format(c.time)
                            views!!.etTourEndDate.setText(output)
                        },
                        calDate.get(Calendar.YEAR),
                        calDate.get(Calendar.MONTH),
                        calDate.get(Calendar.DAY_OF_MONTH)
                    )
                    dpd.show()
                }
            }
            R.id.etSpecialityType  -> {
                preventTwoClick(v)
                if(!arrayListSpecialityType.isNullOrEmpty()) {
                    selectSpecialityTypeDialog()
                } else {
                    GetAllSpecialityAPI(2)
                }
            }
            R.id.etRoomType  -> {
                preventTwoClick(v)
                if(!arrayListRoomType.isNullOrEmpty()) {
                    selectRoomTypeDialog()
                } else {
                    GetAllRoomTypeAPI(2)
                }
            }
            R.id.etVehicleSharingTourCost  -> {
                preventTwoClick(v)
                if(!arrayListVehicleSharingTourCost.isNullOrEmpty()) {
                    selectVehicleSharingTourCostDialog()
                } else {
                    GetAllVehicleSharingAPI(2)
                }
            }
            R.id.etDiscountType -> {
                preventTwoClick(v)
                selectDiscountTypeDialog()
            }
            R.id.etVehicleSharing -> {
                preventTwoClick(v)
                selectVehicleSharingDialog()
            }
        }
    }

    // region Is LTC

    /** AI005
     * This method is to open LTC dialog
     */
    private fun selectLTCDialog() {
        var dialogSelectLTC = Dialog(activity!!)
        dialogSelectLTC.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectLTC.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectLTC.window!!.attributes)

        dialogSelectLTC.window!!.attributes = lp
        dialogSelectLTC.setCancelable(true)
        dialogSelectLTC.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectLTC.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectLTC.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectLTC.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectLTC.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogInitialAdapter(activity!!, arrLTCList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                LTCName = arrLTCList!![pos]
                views!!.etIsLTC.setText(LTCName)
                dialogSelectLTC!!.dismiss()

                if(LTCName.equals("YES")) {
                    CompanyId = 1
                    GetAllCompanyAPI(1)
                } else {
                    if(TravelTypeName.equals("PACKAGE")) {
                        CompanyId = 2
                        GetAllCompanyAPI(1)
                    } else {
                        CompanyId = 1
                        GetAllCompanyAPI(1)
                    }
                }
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectLTC!!.show()
    }

    // endregion

    // region Is DiscountType

    /** AI005
     * This method is to open DiscountType dialog
     */
    private fun selectDiscountTypeDialog() {
        var dialogSelectDiscountType = Dialog(activity!!)
        dialogSelectDiscountType.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectDiscountType.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectDiscountType.window!!.attributes)

        dialogSelectDiscountType.window!!.attributes = lp
        dialogSelectDiscountType.setCancelable(true)
        dialogSelectDiscountType.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectDiscountType.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectDiscountType.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectDiscountType.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectDiscountType.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogInitialAdapter(activity!!, arrDiscountTypeList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                DiscountTypeName = arrDiscountTypeList!![pos]
                views!!.etDiscountType.setText(DiscountTypeName)
                dialogSelectDiscountType!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectDiscountType!!.show()
    }

    // endregion

    // region Travel Type

    /** AI005
     * This method is to open Travel Type dialog
     */
    private fun selectTravelTypeDialog() {
        var dialogSelectTravelType = Dialog(activity!!)
        dialogSelectTravelType.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectTravelType.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectTravelType.window!!.attributes)

        dialogSelectTravelType.window!!.attributes = lp
        dialogSelectTravelType.setCancelable(true)
        dialogSelectTravelType.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectTravelType.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectTravelType.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectTravelType.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectTravelType.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogInitialAdapter(activity!!, arrTravelTypeList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                TravelTypeName = arrTravelTypeList!![pos]
                views!!.etTravelType.setText(TravelTypeName)

                TourPackageId = 0
                TourPackageName = ""
                TourCode = ""
                views!!.etTourPackgeName.setText("")
                views!!.etTourCode.setText("")

                MonthId = 0
                MonthName = ""
                views!!.etTourMonth.setText("")
                arrDateList = ArrayList()

                views!!.etTourDate.setText("")
                DateName = ""

                views!!.etTourStartDate.setText("")
                views!!.etTourEndDate.setText("")
                views!!.etNights.setText("0")

                if(LTCName.equals("NO")) {
                    if (TravelTypeName.equals("TOUR")) {
                        CompanyId = 1
                        GetAllCompanyAPI(1)
                    } else {
                        CompanyId = 2
                        GetAllCompanyAPI(1)
                    }
                } else {
                    CompanyId = 1
                    GetAllCompanyAPI(1)
                }

                if(SectorId != 0 && TravelTypeName != "") {
                    GetAllTourPackageAPI(1)
                }

                if(TravelTypeName.equals("TOUR")) {
                    views!!.LL_TourMonth.visible()
                    views!!.LL_TourDate.visible()
                    views!!.etTotalAmount.isEnabled = false
                } else {
                    views!!.LL_TourMonth.gone()
                    views!!.LL_TourDate.gone()
                    views!!.txterror.gone()
                    views!!.txtroomavailable.gone()
                    views!!.etTotalAmount.isEnabled = true
                }

                dialogSelectTravelType!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectTravelType!!.show()
    }

    // endregion

    // region Vehicle Sharing

    /** AI005
     * This method is to open Travel Type dialog
     */
    private fun selectVehicleSharingDialog() {
        var dialogSelectVehicleSharing = Dialog(activity!!)
        dialogSelectVehicleSharing.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectVehicleSharing.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectVehicleSharing.window!!.attributes)

        dialogSelectVehicleSharing.window!!.attributes = lp
        dialogSelectVehicleSharing.setCancelable(true)
        dialogSelectVehicleSharing.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectVehicleSharing.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectVehicleSharing.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectVehicleSharing.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectVehicleSharing.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogInitialAdapter(activity!!, arrVehicleSharingList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                VehicleSharingName = arrVehicleSharingList!![pos]
                views!!.etVehicleSharing.setText(VehicleSharingName)
                dialogSelectVehicleSharing!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectVehicleSharing!!.show()
    }

    // endregion

    // region Month selection

    /** AI005
     * This method is to open Month dialog
     */
    private fun selectMonthDialog() {
        var dialogSelectMonth = Dialog(activity!!)
        dialogSelectMonth.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectMonth.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectMonth.window!!.attributes)

        dialogSelectMonth.window!!.attributes = lp
        dialogSelectMonth.setCancelable(true)
        dialogSelectMonth.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectMonth.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectMonth.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectMonth.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectMonth.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogMonthAdapter(activity!!, arrMonthList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                MonthId = arrMonthList!![pos].Month!!
                MonthName = arrMonthList!![pos].MonthName!!
                views!!.etTourMonth.setText(MonthName)
                arrDateList = arrMonthList!![pos].DateData!!

                if(!arrDateList.isNullOrEmpty()) {
                    views!!.etTourCode.setText(arrDateList!![0].TourDateCode)
                    CallCheckHotelBlock(arrDateList!![0].TourDateCode!!)
                }

                dialogSelectMonth!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectMonth!!.show()
    }

    // endregion

    // region date selection
    /** AI005
     * This method is to open Date dialog
     */
    private fun selectDateDialog() {
        var dialogSelectDate = Dialog(activity!!)
        dialogSelectDate.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectDate.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectDate.window!!.attributes)

        dialogSelectDate.window!!.attributes = lp
        dialogSelectDate.setCancelable(true)
        dialogSelectDate.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectDate.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectDate.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectDate.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectDate.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogDateAdapter(activity!!, arrDateList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                val date = convertDateStringToString(
                    arrDateList!![pos].TourDate!!,
                    AppConstant.yyyy_MM_dd_Dash,
                    AppConstant.dd_MM_yyyy_Slash
                )!!

                val dateformat = SimpleDateFormat("dd/MM/yyyy")
                val GetDate = dateformat.format(Date())
                val datediff = calculateDays(GetDate,date,"dd/MM/yyyy")

                val usertypeId = sharedPreference?.getPreferenceInt(PrefConstants.PREF_USER_TYPE_ID)
                if(usertypeId == 1 || usertypeId == 2) {
                    DateName = date
                    views!!.etTourDate.setText(date)
                    views!!.etTourCode.setText(arrDateList!![pos].TourDateCode)

                    CallCheckHotelBlock(arrDateList!![pos].TourDateCode!!)

                    views!!.etTourStartDate.setText(date)

                    //Calculate date from no of night
                    val c = Calendar.getInstance()
                    c.time = SimpleDateFormat("dd/MM/yyyy").parse(date)
                    c.add(Calendar.DATE, views!!.etNights.text.toString().toInt())
                    val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                    val output = sdf1.format(c.time)
                    views!!.etTourEndDate.setText(output)
                }
                else {
                    if(datediff > 7) {
                        DateName = date
                        views!!.etTourDate.setText(date)
                        views!!.etTourCode.setText(arrDateList!![pos].TourDateCode)

                        CallCheckHotelBlock(arrDateList!![pos].TourDateCode!!)

                        views!!.etTourStartDate.setText(date)

                        //Calculate date from no of night
                        val c = Calendar.getInstance()
                        c.time = SimpleDateFormat("dd/MM/yyyy").parse(date)
                        c.add(Calendar.DATE, views!!.etNights.text.toString().toInt())
                        val sdf1 = SimpleDateFormat("dd/MM/yyyy")
                        val output = sdf1.format(c.time)
                        views!!.etTourEndDate.setText(output)

                    } else {
                        views!!.txterror.visible()
                    }
                }
                dialogSelectDate!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectDate!!.show()
    }

    // endregion

    // region PAX Type

    /** AI005
     * This method is to retrived GetAllPaxType List data from api
     */
    private fun GetAllPaxTypeAPI() {

        val call = ApiUtils.apiInterface.getAllPaxType()

        call.enqueue(object : Callback<PaxTypeListResponse> {
            override fun onResponse(call: Call<PaxTypeListResponse>, response: Response<PaxTypeListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        arrayListPaxType = response.body()?.Data!!
                        if(state.equals("add")) {
                            if(arrayListPaxType!!.size > 0) {
                                val arrayListPax = ArrayList<PaxDataModel>()
                                if (arrayListPax.size > 0) {
                                    setAdapterData(arrayListPax,state)
                                } else {
                                    setDefaultData()
                                }
                            }
                        }
                    } else {
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<PaxTypeListResponse>, t: Throwable) {
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
                adapter.updateItem(positionListItem, pax, paxid)
                dialogSelectPaxType!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectPaxType!!.show()
    }

    // endregion

    //    region Sector

    /** AI005
     * This method is to retrived Sector List data from api
     */
    private fun GetAllSectorAPI(mode: Int) {
        val call = ApiUtils.apiInterface.getAllSector()
        call.enqueue(object : Callback<SectorListResponse> {
            override fun onResponse(call: Call<SectorListResponse>, response: Response<SectorListResponse>) {
                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        arrayListSector = response.body()?.Data!!
                        if(arrayListSector!!.size > 0) {
                            if(mode == 2) {
                                selectSectorDialog()
                            }
                        } else {
                            activity!!.toast("Sector Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<SectorListResponse>, t: Throwable) {
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }
    /** AI005
     * This method is to open Sector dialog
     */
    private fun selectSectorDialog() {
        var dialogSelectSector = Dialog(activity!!)
        dialogSelectSector.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectSector.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectSector.window!!.attributes)

        dialogSelectSector.window!!.attributes = lp
        dialogSelectSector.setCancelable(true)
        dialogSelectSector.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectSector.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectSector.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectSector.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectSector.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogSectorAdapter(activity!!, arrayListSector!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                SectorId = arrayListSector!![pos].ID!!
                SectorName = arrayListSector!![pos].SectorName!!
                SectorType = arrayListSector!![pos].SectorType!!
                views!!.etSector.setText(SectorName)

                dialogSelectSector!!.dismiss()

                SubSectorId = 0
                SubSectorName = ""
                views!!.etSubSector.setText("")

                TourPackageId = 0
                TourPackageName = ""
                TourCode = ""
                views!!.etTourPackgeName.setText("")
                views!!.etTourCode.setText("")

                MonthId = 0
                MonthName = ""
                views!!.etTourMonth.setText("")
                arrDateList = ArrayList()

                views!!.etTourDate.setText("")
                DateName = ""

                views!!.etTourStartDate.setText("")
                views!!.etTourEndDate.setText("")
                views!!.etNights.setText("0")

                GetAllSubSectorAPI(1)

                if(SectorId != 0 && TravelTypeName != "") {
                    GetAllTourPackageAPI(1)
                }
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrItemsFinal1: java.util.ArrayList<SectorListModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrayListSector!!) {
                        if (model.SectorName!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogSectorAdapter(activity!!, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            SectorId = arrItemsFinal1!![pos].ID!!
                            SectorName = arrItemsFinal1!![pos].SectorName!!
                            SectorType = arrItemsFinal1!![pos].SectorType!!

                            views!!.etSector.setText(SectorName)

                            SubSectorId = 0
                            SubSectorName = ""
                            views!!.etSubSector.setText("")

                            TourPackageId = 0
                            TourPackageName = ""
                            TourCode = ""
                            views!!.etTourPackgeName.setText("")
                            views!!.etTourCode.setText("")

                            MonthId = 0
                            MonthName = ""
                            views!!.etTourMonth.setText("")
                            arrDateList = ArrayList()

                            views!!.etTourDate.setText("")
                            DateName = ""

                            views!!.etTourStartDate.setText("")
                            views!!.etTourEndDate.setText("")
                            views!!.etNights.setText("0")

                            GetAllSubSectorAPI(1)

                            if(SectorId != 0 && TravelTypeName != "") {
                                GetAllTourPackageAPI(1)
                            }
                            dialogSelectSector!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogSectorAdapter(activity!!, arrayListSector!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            SectorId = arrayListSector!![pos].ID!!
                            SectorName = arrayListSector!![pos].SectorName!!
                            SectorType = arrayListSector!![pos].SectorType!!

                            views!!.etSector.setText(SectorName)

                            SubSectorId = 0
                            SubSectorName = ""
                            views!!.etSubSector.setText("")

                            TourPackageId = 0
                            TourPackageName = ""
                            TourCode = ""
                            views!!.etTourPackgeName.setText("")
                            views!!.etTourCode.setText("")

                            MonthId = 0
                            MonthName = ""
                            views!!.etTourMonth.setText("")
                            arrDateList = ArrayList()

                            views!!.etTourDate.setText("")
                            DateName = ""

                            views!!.etTourStartDate.setText("")
                            views!!.etTourEndDate.setText("")
                            views!!.etNights.setText("0")

                            GetAllSubSectorAPI(1)

                            if(SectorId != 0 && TravelTypeName != "") {
                                GetAllTourPackageAPI(1)
                            }
                            dialogSelectSector!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectSector!!.show()
    }

    //  endregion

    // region SubSector

    /** AI005
     * This method is to retrived Sub Sector List data from sector id
     */
    private fun GetAllSubSectorAPI(mode: Int) {


        val jsonObject = JSONObject()
        jsonObject.put("DestinationID", SectorId)

        val call = ApiUtils.apiInterface.getAllSubSector(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<SubSectorListResponse> {
            override fun onResponse(call: Call<SubSectorListResponse>, response: Response<SubSectorListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        if(!arrayListSubSector.isNullOrEmpty()) {
                            arrayListSubSector!!.clear()
                        }

                        arrayListSubSector = response.body()?.Data!!.SubSectorList!!

                        if(arrayListSubSector!!.size > 0) {
                            if(mode == 2) {
                                selectSubSectorDialog()
                            }
                        } else {
                            activity!!.toast("Sub Sector Not Available.", AppConstant.TOAST_SHORT)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SubSectorListResponse>, t: Throwable) {
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }
    /** AI005
     * This method is to open Sub Sector dialog
     */
    private fun selectSubSectorDialog() {
        var dialogSelectSubSector = Dialog(activity!!)
        dialogSelectSubSector.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectSubSector.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectSubSector.window!!.attributes)

        dialogSelectSubSector.window!!.attributes = lp
        dialogSelectSubSector.setCancelable(true)
        dialogSelectSubSector.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectSubSector.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectSubSector.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectSubSector.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectSubSector.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogSubSectorAdapter(activity!!, arrayListSubSector!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                SubSectorId = arrayListSubSector!![pos].ID!!
                SubSectorName = arrayListSubSector!![pos].SubSectorName!!
                views!!.etSubSector.setText(SubSectorName)

                dialogSelectSubSector!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrItemsFinal1: java.util.ArrayList<SubSectorListModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrayListSubSector!!) {
                        if (model.SubSectorName!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogSubSectorAdapter(activity!!, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            SubSectorId = arrItemsFinal1!![pos].ID!!
                            SubSectorName = arrItemsFinal1!![pos].SubSectorName!!
                            views!!.etSubSector.setText(SubSectorName)
                            dialogSelectSubSector!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogSubSectorAdapter(activity!!, arrayListSubSector!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            SubSectorId = arrayListSubSector!![pos].ID!!
                            SubSectorName = arrayListSubSector!![pos].SubSectorName!!
                            views!!.etSubSector.setText(SubSectorName)

                            dialogSelectSubSector!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectSubSector!!.show()
    }

    // endregion

    // region Tour/Package

    /** AI005
     * This method is to retrived TourPackage List data from sectorid and travel type
     */
    private fun GetAllTourPackageAPI(mode: Int) {

        val jsonObject = JSONObject()
        jsonObject.put("SectorID", SectorId)
        jsonObject.put("TravelType", TravelTypeName)

        val call = ApiUtils.apiInterface.getAllTourPackage(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<TourPackageListResponse> {
            override fun onResponse(call: Call<TourPackageListResponse>, response: Response<TourPackageListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {

                        if(!arrayListTourPackage.isNullOrEmpty()) {
                            arrayListTourPackage!!.clear()
                        }

                        arrayListTourPackage = response.body()?.Data!!
                        if(arrayListTourPackage!!.size > 0) {
                            if(mode == 2) {
                                selectTourPackageDialog()
                            }
                        } else {
                            activity!!.toast("Tour/Package Not Available.", AppConstant.TOAST_SHORT)
                        }

                    }
                }
            }

            override fun onFailure(call: Call<TourPackageListResponse>, t: Throwable) {
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }

    /** AI005
     * This method is to open Tour Package dialog
     */
    private fun selectTourPackageDialog() {
        var dialogSelectTourPackage = Dialog(activity!!)
        dialogSelectTourPackage.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectTourPackage.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectTourPackage.window!!.attributes)

        dialogSelectTourPackage.window!!.attributes = lp
        dialogSelectTourPackage.setCancelable(true)
        dialogSelectTourPackage.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectTourPackage.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectTourPackage.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectTourPackage.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectTourPackage.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogTourPackageAdapter(activity!!, arrayListTourPackage!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                TourPackageId = arrayListTourPackage!![pos].TourID!!
                TourPackageName = arrayListTourPackage!![pos].TourName!!
                TourCode = arrayListTourPackage!![pos].TourCode!!

                views!!.etTourPackgeName.setText(TourPackageName)

                if(!TravelTypeName.equals("TOUR")) {
                    views!!.etTourCode.setText(TourCode)
                }

                MonthId = 0
                MonthName = ""
                views!!.etTourMonth.setText("")
                arrDateList = ArrayList()

                views!!.etTourDate.setText("")
                DateName = ""

                views!!.etTourStartDate.setText("")
                views!!.etTourEndDate.setText("")
                views!!.etNights.setText("0")


                callTourDetailApi(TourPackageId)

                if(VehicleSharingTourCostId != 0 && TourPackageId != 0 && SpecialityTypeId != 0
                    && RoomTypeId != 0 && views!!.etNights.text.toString().trim() != "0") {
                    var check = true
                    if (::adapter.isInitialized) {
                        val arrayList1 = adapter.getAdapterArrayList()
                        for (i in 0 until arrayList1!!.size) {
                            if (TextUtils.isEmpty(arrayList1[i].noofroom)) {
                                check = false
                            }
                        }
                        if(check) {
                            callGetTourCostApi()
                        }
                    }
                }

                dialogSelectTourPackage!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrItemsFinal1: ArrayList<TourPackageListModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrayListTourPackage!!) {
                        if (model.TourName!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogTourPackageAdapter(activity!!, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            TourPackageId = arrItemsFinal1!![pos].TourID!!
                            TourPackageName = arrItemsFinal1!![pos].TourName!!
                            TourCode = arrayListTourPackage!![pos].TourCode!!

                            views!!.etTourPackgeName.setText(TourPackageName)
                            if(!TravelTypeName.equals("TOUR")) {
                                views!!.etTourCode.setText(TourCode)
                            }

                            MonthId = 0
                            MonthName = ""
                            views!!.etTourMonth.setText("")
                            arrDateList = ArrayList()

                            views!!.etTourDate.setText("")
                            DateName = ""

                            views!!.etTourStartDate.setText("")
                            views!!.etTourEndDate.setText("")
                            views!!.etNights.setText("0")

                            callTourDetailApi(TourPackageId)

                            if(VehicleSharingTourCostId != 0 && TourPackageId != 0 && SpecialityTypeId != 0
                                && RoomTypeId != 0 && views!!.etNights.text.toString().trim() != "0") {
                                var check = true
                                if (::adapter.isInitialized) {
                                    val arrayList1 = adapter.getAdapterArrayList()
                                    for (i in 0 until arrayList1!!.size) {
                                        if (TextUtils.isEmpty(arrayList1[i].noofroom)) {
                                            check = false
                                        }
                                    }
                                    if(check) {
                                        callGetTourCostApi()
                                    }
                                }
                            }

                            dialogSelectTourPackage!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogTourPackageAdapter(activity!!, arrayListTourPackage!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            TourPackageId = arrayListTourPackage!![pos].TourID!!
                            TourPackageName = arrayListTourPackage!![pos].TourName!!
                            TourCode = arrayListTourPackage!![pos].TourCode!!

                            views!!.etTourPackgeName.setText(TourPackageName)
                            if(!TravelTypeName.equals("TOUR")) {
                                views!!.etTourCode.setText(TourCode)
                            }

                            MonthId = 0
                            MonthName = ""
                            views!!.etTourMonth.setText("")
                            arrDateList = ArrayList()

                            views!!.etTourDate.setText("")
                            DateName = ""

                            views!!.etTourStartDate.setText("")
                            views!!.etTourEndDate.setText("")
                            views!!.etNights.setText("0")

                            callTourDetailApi(TourPackageId)

                            if(VehicleSharingTourCostId != 0 && TourPackageId != 0 && SpecialityTypeId != 0
                                && RoomTypeId != 0 && views!!.etNights.text.toString().trim() != "0") {
                                var check = true
                                if (::adapter.isInitialized) {
                                    val arrayList1 = adapter.getAdapterArrayList()
                                    for (i in 0 until arrayList1!!.size) {
                                        if (TextUtils.isEmpty(arrayList1[i].noofroom)) {
                                            check = false
                                        }
                                    }
                                    if(check) {
                                        callGetTourCostApi()
                                    }
                                }
                            }

                            dialogSelectTourPackage!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectTourPackage!!.show()
    }

    // endregion

    // region Company
    /** AI005
     * This method is to retrived Sector List data from api
     */
    private fun GetAllCompanyAPI(mode: Int) {

        val call = ApiUtils.apiInterface.getAllCompany()
        call.enqueue(object : Callback<CompanyListResponse> {
            override fun onResponse(call: Call<CompanyListResponse>, response: Response<CompanyListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {

                        arrayListCompany = response.body()?.Data!!
                        if(arrayListCompany!!.size > 0) {
                            for(i in 0 until  arrayListCompany!!.size) {
                                if(CompanyId == arrayListCompany!![i].ID!!) {
                                    CompanyId = arrayListCompany!![i].ID!!
                                    CompanyName = arrayListCompany!![i].CompanyName!!
                                    views!!.etCompanyName.setText(arrayListCompany!![i].CompanyName!!)
                                }
                            }
                            if(mode == 2) {
                                selectCompanyDialog()
                            }
                        } else {
                            activity!!.toast("Company Name Not Available.", AppConstant.TOAST_SHORT)
                        }

                    } else {

                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<CompanyListResponse>, t: Throwable) {
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open Company dialog
     */
    private fun selectCompanyDialog() {
        var dialogSelectCompany = Dialog(activity!!)
        dialogSelectCompany.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectCompany.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectCompany.window!!.attributes)

        dialogSelectCompany.window!!.attributes = lp
        dialogSelectCompany.setCancelable(true)
        dialogSelectCompany.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectCompany.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectCompany.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectCompany.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectCompany.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogCompanyAdapter(activity!!, arrayListCompany!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                CompanyId = arrayListCompany!![pos].ID!!
                CompanyName = arrayListCompany!![pos].CompanyName!!
                views!!.etCompanyName.setText(CompanyName)

                dialogSelectCompany!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrItemsFinal1: java.util.ArrayList<CompanyListModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrayListCompany!!) {
                        if (model.CompanyName!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogCompanyAdapter(activity!!, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {
                            CompanyId = arrItemsFinal1!![pos].ID!!
                            CompanyName = arrItemsFinal1!![pos].CompanyName!!
                            views!!.etCompanyName.setText(CompanyName)
                            dialogSelectCompany!!.dismiss()
                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogCompanyAdapter(activity!!, arrayListCompany!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            CompanyId = arrayListCompany!![pos].ID!!
                            CompanyName = arrayListCompany!![pos].CompanyName!!
                            views!!.etCompanyName.setText(CompanyName)
                            dialogSelectCompany!!.dismiss()

                        }
                    })
                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectCompany!!.show()
    }
    // endregion

    // region Get Tour Detail Api
    private fun callTourDetailApi(tourid: Int) {

        val call = ApiUtils.apiInterface.getTourDates(tourid)
        call.enqueue(object : Callback<TourInfoResponse> {
            override fun onResponse(call: Call<TourInfoResponse>, response: Response<TourInfoResponse>) {
                if (response.code() == 200) {
                    val arrayList =  response.body()?.Data!![0]
                    if (response.body()?.Status == 200) {

                        IsApplyOnServiceTax = arrayList.IsApplyOnServiceTax!!
                        GSTPer = arrayList.GSTPer!!
                        TCSPer = arrayList.TCSPer!!

                        if(state.equals("add")) {
                            if (arrMonthList.isNullOrEmpty()) {
                                arrMonthList = response.body()?.Data!![0].monthData!!
                            } else {
                                arrMonthList.clear()
                                arrMonthList = response.body()?.Data!![0].monthData!!
                            }
                            if (arrayList.NoOfNights != null) {
                                views!!.etNights.setText(arrayList.NoOfNights.toString())
                            }
                        } else {
                            arrMonthList.clear()
                            arrMonthList = response.body()?.Data!![0].monthData!!

                            for(i in 0 until arrMonthList.size) {
                                if(arrMonthList[i].Month == MonthId) {
                                    MonthId = arrMonthList[i].Month!!
                                    MonthName = arrMonthList[i].MonthName!!
                                    views!!.etTourMonth.setText(MonthName)
                                    arrDateList = arrMonthList[i].DateData!!
                                }
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<TourInfoResponse>, t: Throwable) {
            }
        })
    }

    // endregion

    // region speciality type
    /** AI005
     * This method is to retrived Speciality type data from api
     */
    private fun GetAllSpecialityAPI(mode: Int) {
        val call = ApiUtils.apiInterface.getAllSpecialityType()
        call.enqueue(object : Callback<SpecialityTypeListResponse> {
            override fun onResponse(call: Call<SpecialityTypeListResponse>, response: Response<SpecialityTypeListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {

                        arrayListSpecialityType = response.body()?.Data!!
                        if(arrayListSpecialityType!!.size > 0) {

                            if(mode == 2) {
                                selectSpecialityTypeDialog()
                            }
                        } else {
                            activity!!.toast("Speciality Type Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<SpecialityTypeListResponse>, t: Throwable) {
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open dialog
     */
    private fun selectSpecialityTypeDialog() {
        var dialogSelectSpecialityType = Dialog(activity!!)
        dialogSelectSpecialityType.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectSpecialityType.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectSpecialityType.window!!.attributes)

        dialogSelectSpecialityType.window!!.attributes = lp
        dialogSelectSpecialityType.setCancelable(true)
        dialogSelectSpecialityType.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectSpecialityType.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectSpecialityType.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectSpecialityType.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectSpecialityType.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogSpecialityTypeAdapter(activity!!, arrayListSpecialityType!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                SpecialityTypeId = arrayListSpecialityType!![pos].ID!!
                SpecialityTypeName = arrayListSpecialityType!![pos].Title!!
                views!!.etSpecialityType.setText(SpecialityTypeName)

                if(VehicleSharingTourCostId != 0 && TourPackageId != 0 && SpecialityTypeId != 0
                    && RoomTypeId != 0 && views!!.etNights.text.toString().trim() != "0") {
                    var check = true
                    if (::adapter.isInitialized) {
                        val arrayList1 = adapter.getAdapterArrayList()
                        for (i in 0 until arrayList1!!.size) {
                            if (TextUtils.isEmpty(arrayList1[i].noofroom)) {
                                check = false
                            }
                        }
                        if(check) {
                            callGetTourCostApi()
                        }
                    }
                }

                dialogSelectSpecialityType!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectSpecialityType!!.show()
    }
    // endregion

    // region vehicle sharing
    /** AI005
     * This method is to retrived VehicleSharing type data from api
     */
    private fun GetAllVehicleSharingAPI(mode: Int) {

        val call = ApiUtils.apiInterface.getAllVehicleSharingPax()
        call.enqueue(object : Callback<VehicleSharingListResponse> {
            override fun onResponse(call: Call<VehicleSharingListResponse>, response: Response<VehicleSharingListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {

                        arrayListVehicleSharingTourCost = response.body()?.Data!!

                        if(state.equals("edit")) {
                            for(i in 0 until arrayListVehicleSharingTourCost!!.size) {
                                if(arrayListVehicleSharingTourCost!![i].ID == VehicleSharingTourCostId) {
                                    VehicleSharingTourCostId = arrayListVehicleSharingTourCost!![i].ID!!
                                    VehicleSharingTourCostName = arrayListVehicleSharingTourCost!![i].Title!!
                                    views!!.etVehicleSharingTourCost.setText(VehicleSharingTourCostName)
                                }
                            }
                        }
                        if(arrayListVehicleSharingTourCost!!.size > 0) {

                            if(mode == 2) {
                                selectVehicleSharingTourCostDialog()
                            }
                        } else {
                            activity!!.toast("Vehicle Sharing Pax Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<VehicleSharingListResponse>, t: Throwable) {
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open dialog
     */
    private fun selectVehicleSharingTourCostDialog() {
        var dialogSelectVehicleSharing = Dialog(activity!!)
        dialogSelectVehicleSharing.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectVehicleSharing.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectVehicleSharing.window!!.attributes)

        dialogSelectVehicleSharing.window!!.attributes = lp
        dialogSelectVehicleSharing.setCancelable(true)
        dialogSelectVehicleSharing.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectVehicleSharing.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectVehicleSharing.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectVehicleSharing.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectVehicleSharing.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogVehicleSharingPaxAdapter(activity!!, arrayListVehicleSharingTourCost!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                VehicleSharingTourCostId = arrayListVehicleSharingTourCost!![pos].ID!!
                VehicleSharingTourCostName = arrayListVehicleSharingTourCost!![pos].Title!!
                views!!.etVehicleSharingTourCost.setText(VehicleSharingTourCostName)

                dialogSelectVehicleSharing!!.dismiss()

                if(VehicleSharingTourCostId != 0 && TourPackageId != 0 && SpecialityTypeId != 0
                    && RoomTypeId != 0 && views!!.etNights.text.toString().trim() != "0") {
                    var check = true
                    if (::adapter.isInitialized) {
                        val arrayList1 = adapter.getAdapterArrayList()
                        for (i in 0 until arrayList1!!.size) {
                            if (TextUtils.isEmpty(arrayList1[i].noofroom)) {
                                check = false
                            }
                        }
                        if(check) {
                            callGetTourCostApi()
                        }
                    }
                }
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectVehicleSharing!!.show()
    }
    // endregion

    // region roomtype
    /** AI005
     * This method is to retrived VehicleSharing type data from api
     */
    private fun GetAllRoomTypeAPI(mode: Int) {

        val call = ApiUtils.apiInterface.getAllRoomTypes()
        call.enqueue(object : Callback<RoomTypeListResponse> {
            override fun onResponse(call: Call<RoomTypeListResponse>, response: Response<RoomTypeListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {

                        arrayListRoomType = response.body()?.Data!!
                        if(arrayListRoomType!!.size > 0) {

                            if(mode == 2) {
                                selectRoomTypeDialog()
                            }
                        } else {
                            activity!!.toast("Room Type Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<RoomTypeListResponse>, t: Throwable) {
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open Company dialog
     */
    private fun selectRoomTypeDialog() {
        var dialogSelectRoomType = Dialog(activity!!)
        dialogSelectRoomType.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectRoomType.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectRoomType.window!!.attributes)

        dialogSelectRoomType.window!!.attributes = lp
        dialogSelectRoomType.setCancelable(true)
        dialogSelectRoomType.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectRoomType.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectRoomType.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectRoomType.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectRoomType.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogRoomTypeAdapter(activity!!, arrayListRoomType!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                RoomTypeId = arrayListRoomType!![pos].ID!!
                RoomTypeName = arrayListRoomType!![pos].Title!!
                views!!.etRoomType.setText(RoomTypeName)

                if(VehicleSharingTourCostId != 0 && TourPackageId != 0 && SpecialityTypeId != 0
                    && RoomTypeId != 0 && views!!.etNights.text.toString().trim() != "0") {

                    var check = true
                    if (::adapter.isInitialized) {
                        val arrayList1 = adapter.getAdapterArrayList()
                        for (i in 0 until arrayList1!!.size) {
                            if (TextUtils.isEmpty(arrayList1[i].noofroom)) {
                                check = false
                            }
                        }
                        if(check) {
                            callGetTourCostApi()
                        }
                    }
                }

                dialogSelectRoomType!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectRoomType!!.show()
    }
    // endregion

    private fun callGetTourCostApi() {

        val jsonObject = JSONObject()

        val jsonArrayEducation = JSONArray()
        if (::adapter.isInitialized) {
            val arrayList1 = adapter.getAdapterArrayList()

            if (!arrayList1.isNullOrEmpty()) {
                for (i in 0 until arrayList1!!.size) {
                    val jsonObjectEducation = JSONObject()
                    jsonObjectEducation.put("PaxTypeID", arrayList1!![i].paxid)
                    jsonObjectEducation.put("NoOfRoom", arrayList1!![i].noofroom)

                    jsonArrayEducation.put(jsonObjectEducation)
                }
            }
        }

        jsonObject.put("RateNoOfNights", views!!.etNights.text.toString().trim())
        jsonObject.put("RoomTypeID", RoomTypeId)
        jsonObject.put("SpecialityTypeID", SpecialityTypeId)
        jsonObject.put("TourID", TourPackageId)
        jsonObject.put("VehicleSharingPaxID", VehicleSharingTourCostId)
        jsonObject.put("PaxTypeList",jsonArrayEducation)

        val call = ApiUtils.apiInterface.GetTourCost(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<TourCostResponse> {
            override fun onResponse(call: Call<TourCostResponse>, response: Response<TourCostResponse>) {
                if (response.code() == 200) {
                    val arrayList =  response.body()?.Data
                    if (response.body()?.Status == 200) {
                        views!!.etTotalAmount.setText(arrayList!!.TotalAmount.toString())
                        CalculateAmount()
                    }
                }
            }
            override fun onFailure(call: Call<TourCostResponse>, t: Throwable) {
            }
        })
    }

    private fun callDetailApi(tourbookingid: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.GetTourBookingView(tourbookingid)

        call.enqueue(object : Callback<TourBookingViewResponse> {
            override fun onResponse(call: Call<TourBookingViewResponse>, response: Response<TourBookingViewResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        SetAPIData(arrayList)
                    }
                }
            }
            override fun onFailure(call: Call<TourBookingViewResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun SetAPIData(arrayList : ArrayList<TourBookingViewModel>) {

        val model = arrayList[0]

        if(model.FirstName != null && model.FirstName != "") {
            views!!.etFirstName.setText(model.FirstName)
        }
        if(model.LastName != null && model.LastName != "") {
            views!!.etLastName.setText(model.LastName)
        }
        if(model.EmailID != null && model.EmailID != "") {
            views!!.etEmailId.setText(model.EmailID)
        }
        if(model.MobileNo != null && model.MobileNo != "") {
            views!!.etMobile.setText(model.MobileNo)
        }
        if(model.TourDate != null && model.TourDate != "") {
            val mDateName = convertDateStringToString(
                model.TourDate!!,
                AppConstant.yyyy_MM_dd_Dash,
                AppConstant.dd_MM_yyyy_Slash
            )!!
            views!!.etTourDate.setText(mDateName)
            DateName = mDateName
        }
        if(model.TourStartDate != null && model.TourStartDate != "") {
            val mStartDate = convertDateStringToString(
                model.TourStartDate!!,
                AppConstant.yyyy_MM_dd_Dash,
                AppConstant.dd_MM_yyyy_Slash
            )!!
            views!!.etTourStartDate.setText(mStartDate)
        }
        if(model.TourEndDate != null && model.TourEndDate != "") {
            val mEndDate = convertDateStringToString(
                model.TourEndDate!!,
                AppConstant.yyyy_MM_dd_Dash,
                AppConstant.dd_MM_yyyy_Slash
            )!!
            views!!.etTourEndDate.setText(mEndDate)
        }
        if(model.IsLTCTour != null) {
            if(model.IsLTCTour!!) {
                LTCName = "YES"
                views!!.etIsLTC.setText(LTCName)
            } else {
                LTCName = "NO"
                views!!.etIsLTC.setText(LTCName)
            }
        }
        if(model.SectorID != null && model.SectorID != 0) {
            SectorId = model.SectorID!!
            SectorName = model.Sector!!
            SectorType = model.SectorType!!
            views!!.etSector.setText(SectorName)

            GetAllSubSectorAPI(1)
        }
        if(model.SubSectorID != null && model.SubSectorID != 0) {
            SubSectorId = model.SubSectorID!!
            SubSectorName = model.SubSector!!
            views!!.etSubSector.setText(SubSectorName)
        }
        if(model.TravelType != null && model.TravelType != "") {
            TravelTypeName = model.TravelType!!
            views!!.etTravelType.setText(TravelTypeName)

            if(TravelTypeName.equals("TOUR")) {
                views!!.LL_TourMonth.visible()
                views!!.LL_TourDate.visible()
                views!!.etTotalAmount.isEnabled = false
            } else {
                views!!.LL_TourMonth.gone()
                views!!.LL_TourDate.gone()
                views!!.etTotalAmount.isEnabled = true
            }
        }
        if(model.TourDateCode != null && model.TourDateCode != "") {
            views!!.etTourCode.setText(model.TourDateCode)
            if(TravelTypeName.equals("TOUR")) {
                BalanceRoom = model.TotalNoOfRooms!!
                TotalRoom = model.TotalNoOfRooms
                CallCheckHotelBlock(model.TourDateCode)
            }
        }
        if(model.TourID != null && model.TourID != 0) {
            TourPackageId = model.TourID!!
            TourPackageName = model.Tour!!

            views!!.etTourPackgeName.setText(TourPackageName)
        }
        if(model.TourMonth != null && model.TourMonth != "") {
            MonthId = model.TourMonth!!.toInt()
            VehicleSharingTourCostId = model.VehicleSharingPaxID!!
        }
        if(model.VehicleSharing != null && model.VehicleSharing != "") {
            VehicleSharingName = model.VehicleSharing!!
            views!!.etVehicleSharing.setText(VehicleSharingName)
        }
        if(model.SpecialityTypeID != null && model.SpecialityTypeID != 0) {
            SpecialityTypeId = model.SpecialityTypeID!!
            SpecialityTypeName = model.SpecialityType!!
            views!!.etSpecialityType.setText(SpecialityTypeName)
        }
        if(model.RoomTypeID != null && model.RoomTypeID != 0) {
            RoomTypeId = model.RoomTypeID!!
            RoomTypeName = model.RoomType!!
            views!!.etRoomType.setText(RoomTypeName)
        }
        if(model.NoOfNights != null && model.NoOfNights != 0) {
            views!!.etNights.setText(model.NoOfNights.toString())
        }
        if(model.DiscountType != null) {
            if(model.DiscountType!!.equals(1)) {
                views!!.etDiscountType.setText("FLAT")
                DiscountTypeName = "FLAT"
            }
        }
        if(model.TotalNoOfRooms != null) {
            views!!.etNoofRooms.setText(model.TotalNoOfRooms.toString())
        }
        if(model.NoOfSeats != null) {
            views!!.etNoofSeats.setText(model.NoOfSeats.toString())
        }

        if(model.ExtraBed != null) {
            views!!.etExtraBed.setText(model.ExtraBed.toString())
        }

        if(model.GroupBookingNo != null && model.GroupBookingNo != "") {
            views!!.etAlongWith.setText(model.GroupBookingNo)

        }
        if(model.TotalAmount != null) {
            views!!.etTotalAmount.setText(model.TotalAmount.toString())
        }
        if(model.TotalExtraCost != null) {
            views!!.etTotalExtraCost.setText(model.TotalExtraCost.toString())
        }
        if(model.DiscountValue != null) {
            views!!.etDiscountValue.setText(model.DiscountValue.toString())
        }
        if(model.DiscountAmount != null) {
            views!!.etDiscountAmount.setText(model.DiscountAmount.toString())
        }
        if(model.AdminDiscountAmount != null) {
            views!!.etAdminDiscountAmount.setText(model.AdminDiscountAmount.toString())
        }
        if(model.TotalDiscountAmount != null) {
            views!!.etTotalDiscountAmount.setText(model.TotalDiscountAmount.toString())
        }
        if(model.ServiceChargeAmount != null) {
            views!!.etServiceCharge.setText(model.ServiceChargeAmount.toString())
        }
        if(model.TotalGSTAmount != null) {
            views!!.etTotalGST.setText(model.TotalGSTAmount.toString())
        }
        if(model.TotalTCSAmount != null) {
            views!!.etTotalTCS.setText(model.TotalTCSAmount.toString())
        }
        if(model.Kasar != null) {
            views!!.etKasar.setText(model.Kasar.toString())
        }
        if(model.TotalCost != null) {
            views!!.etTotalCost.setText(model.TotalCost.toString())
        }

        if(!model.PaxTypeList.isNullOrEmpty()) {
            val arrayList = model.PaxTypeList
            val arrl: ArrayList<PaxDataModel> = ArrayList()
            for (j in 0 until arrayList!!.size) {
                var paxid = 0
                var paxname = ""
                for(i in 0 until arrayListPaxType!!.size) {
                    if(arrayListPaxType!![i].ID == arrayList!![j].PaxTypeID!!) {
                        paxid = arrayListPaxType!![i].ID!!
                        paxname = arrayListPaxType!![i].PaxType!!
                    }
                }

                arrl.add(PaxDataModel(paxid = paxid ,
                    pax = paxname,  noofroom = arrayList!![j].NoOfRoom.toString()))

            }
            setAdapterData(arrl,state)
        }

        if(SectorId != 0 && TravelTypeName != "") {
            GetAllTourPackageAPI(1)
        }
        if(LTCName.equals("NO")) {
            if (TravelTypeName.equals("Tour")) {
                CompanyId = 1
            } else {
                CompanyId = 2
            }
        } else {
            CompanyId = 1
        }
        if(TourPackageId != 0) {
            callTourDetailApi(TourPackageId)
        }
        GetAllCompanyAPI(1)
        GetAllVehicleSharingAPI(1)

        Handler().postDelayed({  hideProgress() }, 1500)
    }

    private fun CallCheckHotelBlock(tourdatecode: String) {

        val call = ApiUtils.apiInterface.Checkblockroom(tourdatecode)
        call.enqueue(object : Callback<CheckBlockRoomResponse> {
            override fun onResponse(call: Call<CheckBlockRoomResponse>, response: Response<CheckBlockRoomResponse>) {

                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        BalanceRoom = TotalRoom + arrayList.BalanceRoom!!
                        val room = arrayList.BalanceRoom!!
                        if(room > 0) {
                            views!!.txtroomavailable.text = "" + room + " rooms are available"
                            views!!.txtroomavailable.visible()
                        } else {
                            views!!.txtroomavailable.text = "No rooms are available"
                            views!!.txtroomavailable.visible()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<CheckBlockRoomResponse>, t: Throwable) {
            }
        })
    }

//    private fun CreateNewUserFirebase(email: String, fname: String, lname: String, mobile: String, cid: Int) {
//        mAuth!!.createUserWithEmailAndPassword(email, "123456")
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val currentUserID = mAuth!!.currentUser!!.uid
//                    RootRef!!.child(ChatConstant.F_CUSTOMER).child(currentUserID).child("Uid").setValue(currentUserID)
//                    RootRef!!.child(ChatConstant.F_CUSTOMER).child(currentUserID).child("id").setValue(cid)
//                    RootRef!!.child(ChatConstant.F_CUSTOMER).child(currentUserID).child("usertype").setValue("customer")
//                    RootRef!!.child(ChatConstant.F_CUSTOMER).child(currentUserID).child("name").setValue(fname+ " "+lname)
//                    RootRef!!.child(ChatConstant.F_CUSTOMER).child(currentUserID).child("mobile").setValue(mobile)
//                }
//            }
//    }
}