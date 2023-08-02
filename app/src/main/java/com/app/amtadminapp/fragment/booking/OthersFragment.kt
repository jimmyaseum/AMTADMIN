package com.app.amtadminapp.fragment.booking

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.TourBookingFormActivity
import com.app.amtadminapp.adapter.dialog.DialogInitialAdapter
import com.app.amtadminapp.adapter.dialog.DialogVenderAdapter
import com.app.amtadminapp.fragment.BaseFragment
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.fragment_others.*
import kotlinx.android.synthetic.main.fragment_others.view.*
import kotlinx.android.synthetic.main.fragment_others.view.LLcardButtonNext
import kotlinx.android.synthetic.main.fragment_others.view.txtTourBookingNo
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class OthersFragment  : BaseFragment(), View.OnClickListener {
    private var views: View? = null
    var sharedPreference: SharedPreference? = null

    var calDatePickUp = Calendar.getInstance()
    var PickUpDate: String = ""

    var calDateDrop = Calendar.getInstance()
    var DropDate: String = ""

    var calendarPickUpTime: Calendar? = null
    var PickUphour = 0
    var PickUpminute = 0
    var PickuppTime = ""

    var calendarDropTime: Calendar? = null
    var Drophour = 0
    var Dropminute = 0
    var DropppTime = ""

    private val arrTicketBookByList: ArrayList<String> = ArrayList()
    var TicketBookByName : String = ""
    var TicketBookByNameId : Int = 0

    var arrayListVender: ArrayList<VenderListModel>? = null
    var VenderId : Int = 0
    var VenderName : String = ""

    var CreatedBy : Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_others, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {

        sharedPreference = SharedPreference(activity!!)

        views!!.txtTourBookingNo.text = "Tour Booking No : "+ AppConstant.BookingNo

        callDetailApi(AppConstant.TOURBookingID)
        GetAllVenderAPI(1)

        views!!.LLcardButtonNext.setOnClickListener {

            preventTwoClick(it)
            val isvalidate = isValidationPage()
            if(isvalidate) {
                CallSendOTPAPI()
            }
        }

        views!!.edtPickUpDate.setOnClickListener(this)
        views!!.edtDropDate.setOnClickListener(this)
        views!!.edtPickupTime.setOnClickListener(this)
        views!!.edtDropTime.setOnClickListener(this)
        views!!.edtVenderName.setOnClickListener(this)
        views!!.edtTicketBookBy.setOnClickListener(this)

        // AI005 IsLTC Type YES Or No
        arrTicketBookByList.add("CUSTOMER")
        arrTicketBookByList.add("AGENT")

    }

    override fun onClick(v: View?) {
        hideKeyboard(activity!!, v)
        when (v?.id) {
            R.id.edtPickUpDate -> {
                preventTwoClick(v)
                val dpd = DatePickerDialog(
                    activity!!,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calDatePickUp.set(Calendar.YEAR, year)
                        calDatePickUp.set(Calendar.MONTH, monthOfYear)
                        calDatePickUp.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateSelectPickUpDate(calDatePickUp)
                    },
                    calDatePickUp.get(Calendar.YEAR),
                    calDatePickUp.get(Calendar.MONTH),
                    calDatePickUp.get(Calendar.DAY_OF_MONTH)
                )
                dpd.show()
            }
            R.id.edtDropDate -> {
                preventTwoClick(v)
                val dpd = DatePickerDialog(
                    activity!!,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calDateDrop.set(Calendar.YEAR, year)
                        calDateDrop.set(Calendar.MONTH, monthOfYear)
                        calDateDrop.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateSelectDropDate(calDateDrop)
                    },
                    calDateDrop.get(Calendar.YEAR),
                    calDateDrop.get(Calendar.MONTH),
                    calDateDrop.get(Calendar.DAY_OF_MONTH)
                )
                dpd.show()
            }
            R.id.edtPickupTime -> {
                showPickUpTimePickerDialog()
            }
            R.id.edtDropTime -> {
                showDropTimePickerDialog()
            }
            R.id.edtTicketBookBy -> {
                preventTwoClick(v)
                selectTicketBookByDialog()
            }
            R.id.edtVenderName -> {
                preventTwoClick(v)
                if(!arrayListVender.isNullOrEmpty()) {
                    selectVenderDialog()
                } else {
                    GetAllVenderAPI(2)
                }
            }
        }
    }

    /** AI005
     * This method is to date change
     */
    private fun updateSelectPickUpDate(cal: Calendar) {
        PickUpDate = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        views!!.edtPickUpDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
    }

    /** AI005
     * This method is to date change
     */
    private fun updateSelectDropDate(cal: Calendar) {
        DropDate = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        views!!.edtDropDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
    }

    /*Time Picker Dialog*/
    private fun showPickUpTimePickerDialog() {

        if (PickUphour == 0 || PickUpminute == 0) {
            calendarPickUpTime = Calendar.getInstance()
            PickUphour = calendarPickUpTime!!.get(Calendar.HOUR_OF_DAY)
            PickUpminute = calendarPickUpTime!!.get(Calendar.MINUTE)
        }
        val timePickerDialog =
            TimePickerDialog(activity!!, TimePickerDialog.OnTimeSetListener(function = { view, hour, minute ->
                val PickUpTime = convertDateStringToString("$hour:$minute", AppConstant.HH_MM_FORMAT, AppConstant.HH_MM_AA_FORMAT)!!
                PickuppTime = "$hour:$minute"
                edtPickupTime.text = PickuppTime.toEditable() //PickUpTime.toEditable()
            }), PickUphour, PickUpminute, true)

        timePickerDialog.show()
    }

    /*Time Picker Dialog*/
    private fun showDropTimePickerDialog() {

        if (Drophour == 0 || Dropminute == 0) {
            calendarDropTime = Calendar.getInstance()
            Drophour = calendarDropTime!!.get(Calendar.HOUR_OF_DAY)
            Dropminute = calendarDropTime!!.get(Calendar.MINUTE)
        }
        val timePickerDialog =
            TimePickerDialog(activity!!, TimePickerDialog.OnTimeSetListener(function = { view, hour, minute ->
                val DropTime = convertDateStringToString("$hour:$minute", AppConstant.HH_MM_FORMAT, AppConstant.HH_MM_AA_FORMAT)!!
                DropppTime = "$hour:$minute"
                edtDropTime.text = DropppTime.toEditable() //DropTime.toEditable()
            }), Drophour, Dropminute, true)

        timePickerDialog.show()
    }

    // region TicketBookBy

    /** AI005
     * This method is to open TicketBookBy dialog
     */
    private fun selectTicketBookByDialog() {
        var dialogSelectTicketBookBy = Dialog(activity!!)
        dialogSelectTicketBookBy.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectTicketBookBy.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectTicketBookBy.window!!.attributes)

        dialogSelectTicketBookBy.window!!.attributes = lp
        dialogSelectTicketBookBy.setCancelable(true)
        dialogSelectTicketBookBy.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectTicketBookBy.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectTicketBookBy.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectTicketBookBy.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectTicketBookBy.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogInitialAdapter(activity!!, arrTicketBookByList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                TicketBookByName = arrTicketBookByList!![pos]
                views!!.edtTicketBookBy.setText(TicketBookByName)
                dialogSelectTicketBookBy!!.dismiss()

                if(TicketBookByName.equals("CUSTOMER")) {
                    TicketBookByNameId = 1
                } else {
                    TicketBookByNameId = 2
                }

            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectTicketBookBy!!.show()
    }

    // endregion

    //    region Vender

    /** AI005
     * This method is to retrived Vender List data from api
     */
    private fun GetAllVenderAPI(mode: Int) {
        showProgress()
        val call = ApiUtils.apiInterface.getAllVender()
        call.enqueue(object : Callback<VenderListResponse> {
            override fun onResponse(call: Call<VenderListResponse>, response: Response<VenderListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        hideProgress()
                        arrayListVender = response.body()?.Data!!
                        if(arrayListVender!!.size > 0) {
                            if(mode == 2) {
                                selectVenderDialog()
                            }
                        } else {
                            activity!!.toast("Vender Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<VenderListResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }
    /** AI005
     * This method is to open Vender dialog
     */
    private fun selectVenderDialog() {
        var dialogSelectVender = Dialog(activity!!)
        dialogSelectVender.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectVender.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectVender.window!!.attributes)

        dialogSelectVender.window!!.attributes = lp
        dialogSelectVender.setCancelable(true)
        dialogSelectVender.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectVender.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectVender.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectVender.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectVender.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogVenderAdapter(activity!!, arrayListVender!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                VenderId = arrayListVender!![pos].ID!!
                VenderName = arrayListVender!![pos].VendorName!!
                views!!.edtVenderName.setText(VenderName)

                dialogSelectVender!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter
        edtSearchCustomer.gone()
        dialogSelectVender!!.show()
    }

    //  endregion

    private fun CallStep5AddAPI() {
        if (isConnectivityAvailable(activity!!)) {
            CallUpdateAPI()
        } else {
            activity!!.toast(getString(R.string.str_msg_no_internet), AppConstant.TOAST_SHORT)
        }
    }

    private fun CallUpdateAPI() {
        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(activity!!)
        }
        val UpdatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        val jsonObject = JSONObject()

        jsonObject.put("ID",AppConstant.TOURBookingID)
        jsonObject.put("PickUpDate", PickUpDate)
        jsonObject.put("PickUpPlace", views!!.edtPickupPlace.text.toString().trim())
        jsonObject.put("PickUpTime",PickuppTime)
        jsonObject.put("DropDate", DropDate)
        jsonObject.put("DropPlace", views!!.edtDropPlace.text.toString().trim())
        jsonObject.put("DropTime", DropppTime)
        jsonObject.put("RouteVoucherNo",views!!.edtRouteVoucherNo.text.toString().trim())
        jsonObject.put("VehicleType",views!!.edtVehicleType.text.toString().trim())
        jsonObject.put("VendorID", VenderId)
        jsonObject.put("BusSeat", views!!.edtBusSeat.text.toString().trim().toInt())
        jsonObject.put("TicketBookedBy", TicketBookByNameId)
        jsonObject.put("FlightNo", views!!.edtFlightNo.text.toString().trim())
        jsonObject.put("FlightTicketNo", views!!.edtTicketNo.text.toString().trim())
        jsonObject.put("HotelVoucherNo", views!!.edtHotelVoucherNo.text.toString().trim())
        jsonObject.put("CreatedBy", CreatedBy)
        jsonObject.put("UpdatedBy", UpdatedBy)
        jsonObject.put("IsUpdate",true)

        val call = ApiUtils.apiInterface.AddTourOtherInformation(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.code == 200) {
                        hideProgress()

                        var tourFormActivity: TourBookingFormActivity = activity as TourBookingFormActivity
                        tourFormActivity.updateStepsColor(5)
                        tourFormActivity.CURRENT_STEP_POSITION = 5

                        val fragment = RemarksFragment()
                        replaceFragment(R.id.container, fragment, RemarksFragment::class.java.simpleName)

                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun CallSendOTPAPI() {
        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(activity!!)
        }
        val userid = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        val jsonObject = JSONObject()

        jsonObject.put("UserID", userid)
        jsonObject.put("OTPType","TourBookingOTP")

        val call = ApiUtils.apiInterface.SendPTP(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.code == 200) {
                        hideProgress()

                        selectDialog()

                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun CallResendSendOTPAPI() {
        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(activity!!)
        }
        val userid = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        val jsonObject = JSONObject()

        jsonObject.put("UserID", userid)
        jsonObject.put("OTPType","TourBookingOTP")

        val call = ApiUtils.apiInterface.SendPTP(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.code == 200) {
                        hideProgress()

                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun isValidationPage(): Boolean {
        var check = true

        if (view!!.edtPickUpDate.text.toString().isEmpty()) {
            check = false
            view!!.edtPickUpDate.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
        }
        if (view!!.edtPickupTime.text.toString().isEmpty()) {
            check = false
            view!!.edtPickupTime.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
        }
        if (view!!.edtPickupPlace.text.toString().isEmpty()) {
            check = false
            view!!.edtPickupPlace.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
        }
        if (view!!.edtDropDate.text.toString().isEmpty()) {
            check = false
            view!!.edtDropDate.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
        }
        if (view!!.edtDropTime.text.toString().isEmpty()) {
            check = false
            view!!.edtDropTime.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
        }
        if (view!!.edtDropPlace.text.toString().isEmpty()) {
            check = false
            view!!.edtDropPlace.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
        }

        return check
    }

    private fun callDetailApi(tourbookingid: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.getTourBookingInfo(tourbookingid)

        call.enqueue(object : Callback<TourBookingInfoResponse> {
            override fun onResponse(call: Call<TourBookingInfoResponse>, response: Response<TourBookingInfoResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        setAPIData(arrayList)
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

    private fun setAPIData(arrayList: ArrayList<TourBookingInfoModel>) {

        CreatedBy = arrayList[0].CreatedBy

        if(arrayList[0].PickUpDate != null && arrayList[0].PickUpDate != "") {
            PickUpDate = arrayList[0].PickUpDate!!
            val pickupdate = convertDateStringToString(
                arrayList[0].PickUpDate!!,
                AppConstant.yyyy_MM_dd_Dash,
                AppConstant.dd_MM_yyyy_Slash
            )!!
            views!!.edtPickUpDate.setText(pickupdate)
        }
        if(arrayList[0].PickUpTime != null && arrayList[0].PickUpTime != "") {
            PickuppTime = arrayList[0].PickUpTime!!
            views!!.edtPickupTime.setText(arrayList[0].PickUpTime)
        }
        if(arrayList[0].PickUpPlace != null && arrayList[0].PickUpPlace != "") {
            views!!.edtPickupPlace.setText(arrayList[0].PickUpPlace)
        }
        if(arrayList[0].DropDate != null && arrayList[0].DropDate != "") {
            DropDate = arrayList[0].DropDate!!
            val dropdate = convertDateStringToString(
                arrayList[0].DropDate!!,
                AppConstant.yyyy_MM_dd_Dash,
                AppConstant.dd_MM_yyyy_Slash
            )!!
            views!!.edtDropDate.setText(dropdate)
        }
        if(arrayList[0].DropTime != null && arrayList[0].DropTime != "") {
            DropppTime = arrayList[0].DropTime!!
            views!!.edtDropTime.setText(arrayList[0].DropTime)
        }
        if(arrayList[0].DropPlace != null && arrayList[0].DropPlace != "") {
            views!!.edtDropPlace.setText(arrayList[0].DropPlace)
        }
        if(arrayList[0].VendorID != null && arrayList[0].VendorID != 0) {
            VenderId = arrayList[0].VendorID!!
            VenderName = arrayList[0].Vendor!!
            views!!.edtVenderName.setText(arrayList[0].Vendor)
        }
        if(arrayList[0].TicketBookedBy != null && arrayList[0].TicketBookedBy != 0) {
            TicketBookByNameId = arrayList[0].TicketBookedBy!!
            if(TicketBookByNameId.equals(1)) {
                TicketBookByName = "CUSTOMER"
            } else {
                TicketBookByName = "AGENT"
            }

            views!!.edtTicketBookBy.setText(TicketBookByName)
        }
        if(arrayList[0].RouteVoucherNo != null && arrayList[0].RouteVoucherNo != "") {
            views!!.edtRouteVoucherNo.setText(arrayList[0].RouteVoucherNo)
        }
        if(arrayList[0].FlightNo != null && arrayList[0].FlightNo != "") {
            views!!.edtFlightNo.setText(arrayList[0].FlightNo)
        }
        if(arrayList[0].FlightTicketNo != null && arrayList[0].FlightTicketNo != "") {
            views!!.edtTicketNo.setText(arrayList[0].FlightTicketNo)
        }
        if(arrayList[0].HotelVoucherNo != null && arrayList[0].HotelVoucherNo != "") {
            views!!.edtHotelVoucherNo.setText(arrayList[0].HotelVoucherNo)
        }
        if(arrayList[0].BusSeat != null && arrayList[0].BusSeat != "") {
            views!!.edtBusSeat.setText(arrayList[0].BusSeat)
        }
        if(arrayList[0].VehicleType != null && arrayList[0].VehicleType != "") {
            views!!.edtVehicleType.setText(arrayList[0].VehicleType)
        }

    }

    private fun selectDialog() {

        val dialogMember = Dialog(activity!!)
        dialogMember.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = LayoutInflater.from(activity!!).inflate(R.layout.dialog_otp_verify, null)
        dialogMember.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogMember.window!!.attributes)

        dialogMember.window!!.attributes = lp
        dialogMember.setCancelable(true)
        dialogMember.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogMember.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogMember.window!!.setGravity(Gravity.CENTER)

        val edtOTP = dialogMember.findViewById(R.id.edtOTP) as EditText

        val txtresend = dialogMember.findViewById(R.id.txtresend) as TextView
        val ImgClose = dialogMember.findViewById(R.id.ImgClose) as ImageView
        val CardInquiryNow = dialogMember.findViewById(R.id.CardSubmit) as CardView

        ImgClose.setOnClickListener {
            dialogMember.dismiss()
        }

        txtresend.setOnClickListener {
            CallResendSendOTPAPI()
        }

        CardInquiryNow.setOnClickListener {
            if(!edtOTP.text.toString().equals("")) {
                dialogMember.dismiss()
                CallStep5AddAPI()
            } else {
                activity!!.toast("Please enter OTP", AppConstant.TOAST_SHORT)
            }
        }

        dialogMember!!.show()
    }

}