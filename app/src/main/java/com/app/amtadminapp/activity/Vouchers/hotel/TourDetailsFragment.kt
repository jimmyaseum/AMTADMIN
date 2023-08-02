package com.app.amtadminapp.activity.Vouchers.hotel

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.adapter.dialog.*
import com.app.amtadminapp.fragment.BaseFragment
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.model.response.booking.*
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.fragment_tour_details.*
import kotlinx.android.synthetic.main.fragment_tour_details.view.*
import kotlinx.android.synthetic.main.fragment_tour_details.view.LLcardButtonNext
import kotlinx.android.synthetic.main.fragment_tour_details.view.etCompanyName
import kotlinx.android.synthetic.main.fragment_tour_details.view.etNights
import kotlinx.android.synthetic.main.fragment_tour_details.view.etRoomType
import kotlinx.android.synthetic.main.fragment_tour_details.view.etSector
import kotlinx.android.synthetic.main.fragment_tour_details.view.etTourCode
import kotlinx.android.synthetic.main.fragment_tour_details.view.etTourDate
import kotlinx.android.synthetic.main.fragment_tour_details.view.etTourPackgeName
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TourDetailsFragment(var state: String) : BaseFragment(), View.OnClickListener {

    private var views: View? = null
    var sharedPreference: SharedPreference? = null

    var TourBooingNo = ""

    var arrayListSector: ArrayList<SectorListModel>? = ArrayList()
    var SectorId : Int = 0
    var SectorName : String = ""

    var arrayListTourPackage: ArrayList<TourPackageListModel>? = ArrayList()
    var TourPackageId : Int = 0
    var TourPackageName : String = ""

    var arrayListCompany: ArrayList<CompanyListModel>? = ArrayList()
    var CompanyId : Int = 0
    var CompanyName : String = ""

    var arrayListEmployee: ArrayList<EmployeeListModel>? = ArrayList()
    var EmployeeId : Int = 0
    var EmployeeName : String = ""

    var arrayListRoomType: ArrayList<RoomTypeListModel>? = ArrayList()
    var RoomTypeId : Int = 0
    var RoomTypeName : String = ""

    private var arrDateList: ArrayList<DateDataModel> = ArrayList()
    var DateCode : String = ""

    var calDate = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_tour_details, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(activity!!)

        GetAllSectorAPI(1)
        GetAllRoomTypeAPI(1)
        GetAllCompanyAPI(1)
        GetAllEmployeeAPI(1)

        views!!.etSector.setOnClickListener(this)
        views!!.etTourPackgeName.setOnClickListener(this)
        views!!.etTourDate.setOnClickListener(this)
        views!!.etCompanyName.setOnClickListener(this)
        views!!.etBookBy.setOnClickListener(this)
        views!!.etRoomType.setOnClickListener(this)
        views!!.etTourCode.setOnClickListener(this)

        views!!.LLcardButtonNext.setOnClickListener(this)
        views!!.LLcardButtonView.setOnClickListener(this)

        views!!.etNights.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    val days = strSearch.toInt() + 1
                    views!!.etDays.setText(days.toString())

                }
            }
        })

        if(state.equals("edit")) {
            callHotelBookingApi(AppConstant.HotelVoucherIDs)
        }
    }

    override fun onClick(v: View?) {
        hideKeyboard(activity!!, v)
        when (v?.id) {
            R.id.etSector -> {
                preventTwoClick(v)
                if (!arrayListSector.isNullOrEmpty()) {
                    selectSectorDialog()
                } else {
                    GetAllSectorAPI(2)
                }
            }
            R.id.etTourPackgeName -> {
                preventTwoClick(v)
                if(SectorId != 0) {
                    if (!arrayListTourPackage.isNullOrEmpty()) {
                        selectTourPackageDialog()
                    } else {
                        GetAllTourPackageAPI(2)
                    }
                } else {
                    activity!!.toast("Please Select Sector", Toast.LENGTH_SHORT)
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
            R.id.etBookBy -> {
                preventTwoClick(v)
                if(!arrayListEmployee.isNullOrEmpty()) {
                    selectEmployeeDialog()
                } else {
                    GetAllEmployeeAPI(2)
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
            R.id.etTourDate  -> {

                preventTwoClick(v)
                calDate.time = SimpleDateFormat("dd/MM/yyyy").parse(views!!.etTourDate.text.toString())
                val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calDate.set(Calendar.YEAR, year)
                    calDate.set(Calendar.MONTH, monthOfYear)
                    calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val date = SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(calDate.time)
                    views!!.etTourDate.setText(date)

                },
                    calDate.get(Calendar.YEAR),
                    calDate.get(Calendar.MONTH),
                    calDate.get(Calendar.DAY_OF_MONTH)
                )
                dpd.show()

            }
            R.id.etTourCode -> {
                preventTwoClick(v)
                if(!arrDateList.isNullOrEmpty()) {
                    selectDateDialog()
                } else {
                    activity!!.toast("Tour Data Not Available.", AppConstant.TOAST_SHORT)
                }
            }
            R.id.LLcardButtonNext -> {

                preventTwoClick(v)

                if(state.equals("add")) {
                    CallStep1AddAPI()
                } else {
//                    CallStep1UpdateAPI()
                }

            }
            R.id.LLcardButtonView -> {
                if(etTourBookingNo.text.toString().equals("")) {
                    activity!!.toast("Please Enter Tour Booking No",Toast.LENGTH_LONG)
                } else {
                    callDetailApi(etTourBookingNo.text.toString().toInt())
                }
            }
        }
    }

    private fun CallStep1AddAPI() {

        if (isConnectivityAvailable(activity!!)) {
            CallAddAPI()
        } else {
            activity!!.toast(getString(R.string.str_msg_no_internet), AppConstant.TOAST_SHORT)
        }
    }

    private fun CallAddAPI() {

        showProgress()

        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        val mStartDate = convertDateStringToString(
            views!!.etTourDate.text.toString(),
            AppConstant.dd_MM_yyyy_Slash,
            AppConstant.yyyy_MM_dd_Dash
        )!!

        val jsonObject = JSONObject()

        jsonObject.put("CreatedBy", CreatedBy)
        jsonObject.put("TourBookingNo",TourBooingNo)
        jsonObject.put("SectorID",SectorId)
        jsonObject.put("TourID",TourPackageId)
        jsonObject.put("CompanyID",CompanyId)
        jsonObject.put("RoomTypeID",RoomTypeId)
        jsonObject.put("NoOfNights",views!!.etNights.text.toString().toInt())
        jsonObject.put("TourDate",mStartDate)
        jsonObject.put("TourDateCode",views!!.etTourCode.text)
        jsonObject.put("TotalAdults",views!!.etTotalAdults.text.toString().toInt())
        jsonObject.put("TotalExtraAdults", views!!.etTotalExtraAdults.text.toString().toInt())
        jsonObject.put("TotalCWB",views!!.etTotalChildWB.text.toString().toInt())
        jsonObject.put("TotalCNB",views!!.etTotalChildNB.text.toString().toInt())
        jsonObject.put("TotalInfants",views!!.etTotalInfants.text.toString().toInt())
        jsonObject.put("Remarks",views!!.etRemarks.text.toString())
        jsonObject.put("BookBy",EmployeeId )
        jsonObject.put("PickUpPlace",views!!.etPickUpPlace.text)
        jsonObject.put("DropPlace", views!!.etDropPlace.text)

        val call = ApiUtils.apiInterface2.AddHotelVoucherStep1(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CommonResponse2> {
            override fun onResponse(call: Call<CommonResponse2>, response: Response<CommonResponse2>) {
                if (response.code() == 200) {
                    if (response.body()?.code == 200) {

                        hideProgress()

                        val data = response.body()?.Data!!
                        AppConstant.HotelVoucherIDs = data.ID

                        var inquiryFormActivity: AddHotelVoucherActivity = activity as AddHotelVoucherActivity
                        inquiryFormActivity.updateStepsColor(1)
                        inquiryFormActivity.CURRENT_STEP_POSITION = 1
                        inquiryFormActivity.isAPICallFirst = true

                        val tourFormActivity: AddHotelVoucherActivity = activity!! as AddHotelVoucherActivity
                        tourFormActivity.updateStepsColor(1)
                        tourFormActivity.CURRENT_STEP_POSITION = 1
                        tourFormActivity.isAPICallFirst = true
                        tourFormActivity.state = "edit"

                        val fragment = HotelDetailsFragment(state)
                        replaceFragment(R.id.container, fragment, HotelDetailsFragment::class.java.simpleName)

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

    //    region Sector
    /** AI005
     * This method is to retrived Sector List data from api
     */
    private fun GetAllSectorAPI(mode: Int) {
        showProgress()
        val call = ApiUtils.apiInterface.getAllSector()
        call.enqueue(object : Callback<SectorListResponse> {
            override fun onResponse(call: Call<SectorListResponse>, response: Response<SectorListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        hideProgress()
                        arrayListSector = response.body()?.Data!!
                        if(arrayListSector!!.size > 0) {
                            if(mode == 2) {
                                selectSectorDialog()
                            }
                        } else {
                            activity!!.toast("Sector Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<SectorListResponse>, t: Throwable) {
                hideProgress()
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
                views!!.etSector.setText(SectorName)

                dialogSelectSector!!.dismiss()

                if(SectorId != 0) {
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
                            views!!.etSector.setText(SectorName)
                            if(SectorId != 0) {
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

                            views!!.etSector.setText(SectorName)

                            if(SectorId != 0) {
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

    // region Tour/Package

    /** AI005
     * This method is to retrived TourPackage List data from sectorid and travel type
     */
    private fun GetAllTourPackageAPI(mode: Int) {

        showProgress()

        val jsonObject = JSONObject()
        jsonObject.put("SectorID", SectorId)
        jsonObject.put("TravelType", "")

        val call = ApiUtils.apiInterface.getAllTourPackage(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<TourPackageListResponse> {
            override fun onResponse(call: Call<TourPackageListResponse>, response: Response<TourPackageListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        hideProgress()

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
                    } else {
                        hideProgress()
                    }
                }
            }

            override fun onFailure(call: Call<TourPackageListResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
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

                views!!.etTourPackgeName.setText(TourPackageName)
                callTourDetailApi(TourPackageId)
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
                            views!!.etTourPackgeName.setText(TourPackageName)
                            callTourDetailApi(TourPackageId)
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
                            views!!.etTourPackgeName.setText(TourPackageName)
                            callTourDetailApi(TourPackageId)
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
        showProgress()
        val call = ApiUtils.apiInterface.getAllCompany()
        call.enqueue(object : Callback<CompanyListResponse> {
            override fun onResponse(call: Call<CompanyListResponse>, response: Response<CompanyListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        hideProgress()
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
                        hideProgress()
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<CompanyListResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
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

    // region roomtype
    /** AI005
     * This method is to retrived VehicleSharing type data from api
     */
    private fun GetAllRoomTypeAPI(mode: Int) {
        showProgress()
        val call = ApiUtils.apiInterface.getAllRoomTypes()
        call.enqueue(object : Callback<RoomTypeListResponse> {
            override fun onResponse(call: Call<RoomTypeListResponse>, response: Response<RoomTypeListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        hideProgress()
                        arrayListRoomType = response.body()?.Data!!
                        if(arrayListRoomType!!.size > 0) {

                            if(mode == 2) {
                                selectRoomTypeDialog()
                            }

                        } else {
                            activity!!.toast("Room Type Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<RoomTypeListResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open room type dialog
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

                dialogSelectRoomType!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectRoomType!!.show()
    }
    // endregion

    // region Employee
    /** AI005
     * This method is to retrived Sector List data from api
     */
    private fun GetAllEmployeeAPI(mode: Int) {
        showProgress()
        val call = ApiUtils.apiInterface.getAllEmployee()
        call.enqueue(object : Callback<EmployeeListResponse> {
            override fun onResponse(call: Call<EmployeeListResponse>, response: Response<EmployeeListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {


                        arrayListEmployee = response.body()?.Data!!

                        if(arrayListEmployee!!.size > 0) {
//                            for(i in 0 until  arrayListEmployee!!.size) {
//                                if(EmployeeId == arrayListEmployee!![i].ID!!) {
//                                    EmployeeId = arrayListEmployee!![i].ID!!
//                                    EmployeeName = arrayListEmployee!![i].FirstName!! + " " +arrayListEmployee!![i].LastName!!
//                                    views!!.etBookBy.setText(EmployeeName)
//                                }
//                            }
                            if(mode == 2) {
                                selectEmployeeDialog()
                            }
                            hideProgress()
                        } else {
                            activity!!.toast("Employee Name Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<EmployeeListResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open Employee dialog
     */
    private fun selectEmployeeDialog() {
        var dialogSelectEmployee = Dialog(activity!!)
        dialogSelectEmployee.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectEmployee.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectEmployee.window!!.attributes)

        dialogSelectEmployee.window!!.attributes = lp
        dialogSelectEmployee.setCancelable(true)
        dialogSelectEmployee.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectEmployee.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectEmployee.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectEmployee.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectEmployee.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogEmployeeAdapter(activity!!, arrayListEmployee!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                EmployeeId = arrayListEmployee!![pos].ID!!
                EmployeeName = arrayListEmployee!![pos].FirstName!! + " " +arrayListEmployee!![pos].LastName!!
                views!!.etBookBy.setText(EmployeeName)

                dialogSelectEmployee!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrItemsFinal1: java.util.ArrayList<EmployeeListModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrayListEmployee!!) {
                        if (model.FirstName!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogEmployeeAdapter(activity!!, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {
                            EmployeeId = arrItemsFinal1!![pos].ID!!
                            EmployeeName = arrItemsFinal1!![pos].FirstName!! + " " +arrItemsFinal1!![pos].LastName!!
                            views!!.etBookBy.setText(EmployeeName)
                            dialogSelectEmployee!!.dismiss()
                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogEmployeeAdapter(activity!!, arrayListEmployee!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            EmployeeId = arrayListEmployee!![pos].ID!!
                            EmployeeName = arrayListEmployee!![pos].FirstName!! + " " +arrayListEmployee!![pos].LastName!!
                            views!!.etBookBy.setText(EmployeeName)
                            dialogSelectEmployee!!.dismiss()

                        }
                    })
                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectEmployee!!.show()
    }
    // endregion

    // region Get Tour Date Api
    private fun callTourDetailApi(tourid: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.getTourDates(tourid)

        call.enqueue(object : Callback<TourInfoResponse> {
            override fun onResponse(call: Call<TourInfoResponse>, response: Response<TourInfoResponse>) {
                if (response.code() == 200) {
                    val arrayList =  response.body()?.Data!![0]
                    if (response.body()?.Status == 200) {
                        arrDateList.clear()
                        val arrMonthList = response.body()?.Data!![0].monthData!!
                        for(i in 0 until  arrMonthList.size) {
                            arrDateList = arrMonthList!![i].DateData!!
                        }
                    }
                    hideProgress()
                }
            }
            override fun onFailure(call: Call<TourInfoResponse>, t: Throwable) {
                hideProgress()
            }
        })
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

        val itemAdapter = DialogDateCodeAdapter(activity!!, arrDateList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

//                val date = convertDateStringToString(
//                    arrDateList!![pos].TourDate!!,
//                    AppConstant.yyyy_MM_dd_Dash,
//                    AppConstant.dd_MM_yyyy_Slash
//                )!!
                DateCode = arrDateList!![pos].TourDateCode!!
                views!!.etTourCode.setText(arrDateList!![pos].TourDateCode)

//                AppConstant.TOURDATE = arrDateList!![pos].TourDate!!

                dialogSelectDate!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectDate!!.show()
    }

    // endregion

    private fun callDetailApi(tourbookingid: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.GetMultiTourBookingView(tourbookingid)

        call.enqueue(object : Callback<MultiTourBookingViewResponse> {
            override fun onResponse(call: Call<MultiTourBookingViewResponse>, response: Response<MultiTourBookingViewResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        SetAPIData(arrayList)
                    }
                }
            }
            override fun onFailure(call: Call<MultiTourBookingViewResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun SetAPIData(arrayList: ArrayList<MultiTourBookingViewModel>) {

        TourBooingNo = arrayList[0].TourBookingNo!!
        views!!.etTourBookingNo.setText(arrayList[0].TourBookingNo)

        val model = arrayList!![0]

        if(model.SectorID != null && model.SectorID != 0) {
            SectorId = model.SectorID!!
            SectorName = model.Sector!!
            views!!.etSector.setText(SectorName)

            if(SectorId != 0) {
                GetAllTourPackageAPI(1)
            }
        }
        if(model.TourID != null && model.TourID != 0) {
            TourPackageId = model.TourID!!
            TourPackageName = model.Tour!!
            views!!.etTourPackgeName.setText(TourPackageName)
        }
        if(model.TourDateCode != null && model.TourDateCode != "") {
            views!!.etTourCode.setText(model.TourDateCode)
        }
        if(model.CompanyID != null && model.CompanyID != 0) {
            CompanyId = model.CompanyID!!
            CompanyName = model.Company!!
            views!!.etCompanyName.setText(CompanyName)
        }

        if(model.TourDate != null && model.TourDate != "") {
            val mStartDate = convertDateStringToString(
                model.TourDate!!,
                AppConstant.yyyy_MM_dd_Dash,
                AppConstant.dd_MM_yyyy_Slash
            )!!
            views!!.etTourDate.setText(mStartDate)
        }
        if(model.RoomTypeID != null && model.RoomTypeID != 0) {
            RoomTypeId = model.RoomTypeID!!

            if(RoomTypeId != 0 && arrayListRoomType!!.size > 0) {
                var roomtype = ""
                for(i in 0 until arrayListRoomType!!.size) {
                    if(arrayListRoomType!![i].ID == RoomTypeId) {
                        roomtype = arrayListRoomType!![i].Title!!
                    }
                }
                RoomTypeName = roomtype
                views!!.etRoomType.setText(RoomTypeName)
            }
        }

        if(model.NoOfNights != null) {
            views!!.etNights.setText(model.NoOfNights.toString())
            val days = model.NoOfNights!! + 1
            views!!.etDays.setText(days.toString())
        }
        if(model.TotalAdults != null) {
            views!!.etTotalAdults.setText(model.TotalAdults.toString())
        }
        if(model.TotalExtraAdults != null) {
            views!!.etTotalExtraAdults.setText(model.TotalExtraAdults.toString())
        }
        if(model.TotalCWB != null) {
            views!!.etTotalChildWB.setText(model.TotalCWB.toString())
        }
        if(model.TotalCNB != null) {
            views!!.etTotalChildNB.setText(model.TotalCNB.toString())

        }
        if(model.TotalInfants != null) {
            views!!.etTotalInfants.setText(model.TotalInfants.toString())
        }
        if(model.PickupPlace != null && model.PickupPlace != "") {
            views!!.etPickUpPlace.setText(model.PickupPlace)
        }
        if(model.DropPlace != null && model.DropPlace != "") {
            views!!.etDropPlace.setText(model.DropPlace)
        }

        hideProgress()
    }

    private fun callHotelBookingApi(hotelid: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.GetHotelBookingFindBy(hotelid)

        call.enqueue(object : Callback<HotelBookingViewResponse> {
            override fun onResponse(call: Call<HotelBookingViewResponse>, response: Response<HotelBookingViewResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!

//                        GetAllCityBYSectorAPI(arrayList.SectorID!!)
                        SetAPIDataEdit(arrayList)
                    }
                }
            }
            override fun onFailure(call: Call<HotelBookingViewResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun SetAPIDataEdit(arrayList: HotelBookingViewModel) {
        TourBooingNo = arrayList.TourBookingNo!!
        views!!.etTourBookingNo.setText(arrayList.TourBookingNo)

        val model = arrayList

        if(model.SectorID != null && model.SectorID != 0) {
            SectorId = model.SectorID!!
            SectorName = model.SectorName!!
            views!!.etSector.setText(SectorName)

            if(SectorId != 0) {
                GetAllTourPackageAPI(1)
            }
        }
        if(model.TourID != null && model.TourID != 0) {
            TourPackageId = model.TourID!!
            TourPackageName = model.Tour!!
            views!!.etTourPackgeName.setText(TourPackageName)
        }
        if(model.TourDateCode != null && model.TourDateCode != "") {
            views!!.etTourCode.setText(model.TourDateCode)
        }
        if(model.CompanyID != null && model.CompanyID != 0) {
            CompanyId = model.CompanyID!!
            CompanyName = model.Company!!
            views!!.etCompanyName.setText(CompanyName)
        }
        if(model.TourDate != null && model.TourDate != "") {
            val mStartDate = convertDateStringToString(
                model.TourDate!!,
                AppConstant.yyyy_MM_dd_Dash,
                AppConstant.dd_MM_yyyy_Slash
            )!!
            views!!.etTourDate.setText(mStartDate)
        }
        if(model.RoomTypeID != null && model.RoomTypeID != 0) {
            RoomTypeId = model.RoomTypeID!!

            if(RoomTypeId != 0 && arrayListRoomType!!.size > 0) {
                var roomtype = ""
                for(i in 0 until arrayListRoomType!!.size) {
                    if(arrayListRoomType!![i].ID == RoomTypeId) {
                        roomtype = arrayListRoomType!![i].Title!!
                    }
                }
                RoomTypeName = roomtype
                views!!.etRoomType.setText(RoomTypeName)
            }
        }

        if(model.NoOfNights != null) {
            views!!.etNights.setText(model.NoOfNights.toString())
//            val days = model.NoOfNights!! + 1
//            views!!.etDays.setText(days.toString())
        }
        if(model.NoOfDays != null) {
            views!!.etDays.setText(model.NoOfDays.toString())
        }

        if(model.TotalAdults != null) {
            views!!.etTotalAdults.setText(model.TotalAdults.toString())
        }
        if(model.TotalExtraAdults != null) {
            views!!.etTotalExtraAdults.setText(model.TotalExtraAdults.toString())
        }
        if(model.TotalCWB != null) {
            views!!.etTotalChildWB.setText(model.TotalCWB.toString())
        }
        if(model.TotalCNB != null) {
            views!!.etTotalChildNB.setText(model.TotalCNB.toString())

        }
        if(model.TotalInfants != null) {
            views!!.etTotalInfants.setText(model.TotalInfants.toString())
        }

        if(model.Remarks != null && model.Remarks != "") {
            views!!.etRemarks.setText(model.Remarks)
        }

        if(model.PickupPlace != null && model.PickupPlace != "") {
            views!!.etPickUpPlace.setText(model.PickupPlace)
        }
        if(model.DropPlace != null && model.DropPlace != "") {
            views!!.etDropPlace.setText(model.DropPlace)
        }
        if(model.BookBy != null && model.BookBy != 0) {
            EmployeeId = model.BookBy!!
            EmployeeName = model.BookByName!!
            views!!.etBookBy.setText(EmployeeName)
        }
    }
}