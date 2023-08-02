package com.app.amtadminapp.activity.Dashboard

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.dialog.DialogInitialAdapter
import com.app.amtadminapp.adapter.dialog.DialogSectorAdapter
import com.app.amtadminapp.adapter.dialog.DialogTourPackageAdapter
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.SectorListModel
import com.app.amtadminapp.model.response.SectorListResponse
import com.app.amtadminapp.model.response.TourPackageListModel
import com.app.amtadminapp.model.response.TourPackageListResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_dashboard_pending_payment_filter.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PendingPaymentFilterActivity  : BaseActivity(), View.OnClickListener {

    var calDate = Calendar.getInstance()

    //Booking - Filter
    var TourBookingNo = ""
    var Name = ""
    var MobileNo = ""
    var Sector = ""
    var TravelType = ""
    var Tour = ""
    var TourDateCode = ""
    var TourStartDate = ""
    var NoOfNights = ""
    var TotalAmount = ""

    var arrayListSector: ArrayList<SectorListModel>? = ArrayList()
    var SectorId : Int = 0

    var arrayListTourPackage: ArrayList<TourPackageListModel>? = ArrayList()
    var TourId : Int = 0

    private val arrTravelTypeList: ArrayList<String> = ArrayList()
    var TravelTypeName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_pending_payment_filter)
        getIntentData()
        initializeView()
    }

    //Set Intent data
    private fun getIntentData() {

        if (intent.hasExtra("TourBookingNo")) {
            TourBookingNo = intent.getStringExtra("TourBookingNo").toString()
            edtTourBookingNo.setText(TourBookingNo)
        }
        if (intent.hasExtra("Name")) {
            Name = intent.getStringExtra("Name").toString()
            edtName.setText(Name)
        }
        if (intent.hasExtra("MobileNo")) {
            MobileNo = intent.getStringExtra("MobileNo").toString()
            edtMobileNo.setText(MobileNo)
        }
        if (intent.hasExtra("SectorID")) {
            SectorId = intent.getIntExtra("SectorID",0)
        }
        if (intent.hasExtra("Sector")) {
            Sector = intent.getStringExtra("Sector").toString()
            edtSector.setText(Sector)
        }
        if (intent.hasExtra("TravelType")) {
            TravelType = intent.getStringExtra("TravelType").toString()
            edtTravelType.setText(TravelType)
        }
        if (intent.hasExtra("TourID")) {
            TourId = intent.getIntExtra("TourID",0)
        }
        if (intent.hasExtra("Tour")) {
            Tour = intent.getStringExtra("Tour").toString()
            edtTour.setText(Tour)
        }
        if (intent.hasExtra("TourDateCode")) {
            TourDateCode = intent.getStringExtra("TourDateCode").toString()
            edtTourDateCode.setText(TourDateCode)
        }
        if (intent.hasExtra("TourStartDate")) {
            TourStartDate = intent.getStringExtra("TourStartDate").toString()
            val mTourStartDate = convertDateStringToString(TourStartDate, AppConstant.yyyy_MM_dd_Dash, AppConstant.dd_MM_yyyy_Slash)!!
            edtTourStartDate.setText(mTourStartDate)
        }
        if (intent.hasExtra("NoOfNights")) {
            NoOfNights = intent.getStringExtra("NoOfNights").toString()
            edtNoOfNights.setText(NoOfNights)
        }
        if (intent.hasExtra("TotalAmount")) {
            TotalAmount = intent.getStringExtra("TotalAmount").toString()
            edtTotalAmount.setText(TotalAmount)
        }


    }

    override fun initializeView() {

        arrTravelTypeList.add("SHOW ALL")
        arrTravelTypeList.add("TOUR")
        arrTravelTypeList.add("PACKAGE")

        edtTravelType.setOnClickListener(this)
        edtSector.setOnClickListener(this)
        edtTour.setOnClickListener(this)

        tvClearAll.setOnClickListener(this)
        tvFilters.setOnClickListener(this)
        tvClose.setOnClickListener(this)
        tvApply.setOnClickListener(this)
        edtTourStartDate.setOnClickListener(this)

        GetAllSectorAPI(1)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.tvClearAll -> {
                clearAdapterData()
            }
            R.id.tvClose -> {
                finish()
            }
            R.id.tvFilters -> {
                finish()
            }
            R.id.tvApply -> {
                applyFiltersData()
            }
            R.id.edtTravelType -> {
                selectTravelTypeDialog()
            }
            R.id.edtTourStartDate -> {
                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calDate.set(Calendar.YEAR, year)
                        calDate.set(Calendar.MONTH, monthOfYear)
                        calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateSelectDate(calDate)
                    },
                    calDate.get(Calendar.YEAR),
                    calDate.get(Calendar.MONTH),
                    calDate.get(Calendar.DAY_OF_MONTH)
                )
//                dpd.datePicker.minDate = System.currentTimeMillis() - 1000
//                dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
                dpd.show()
            }
            R.id.edtSector -> {
                preventTwoClick(v)
                if (!arrayListSector.isNullOrEmpty()) {
                    selectSectorDialog()
                } else {
                    GetAllSectorAPI(2)
                }
            }
            R.id.edtTour -> {
                preventTwoClick(v)
                if(SectorId != 0) {
                    if (!arrayListTourPackage.isNullOrEmpty()) {
                        selectTourPackageDialog()
                    } else {
                        GetAllTourPackageAPI(2)
                    }
                } else {
                    toast("Please Select Sector", Toast.LENGTH_SHORT)
                }
            }
        }
    }

    private fun updateSelectDate(cal: Calendar) {
        TourStartDate = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        edtTourStartDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
    }

    private fun clearAdapterData() {
        TourBookingNo = ""
        Name = ""
        MobileNo = ""
        SectorId = 0
        Sector = ""
        TravelType = ""
        TourId = 0
        Tour = ""
        TourDateCode = ""
        TourStartDate = ""
        NoOfNights = ""
        TotalAmount = ""

        edtTourBookingNo.setText("")
        edtName.setText("")
        edtMobileNo.setText("")
        edtSector.setText("")
        edtTravelType.setText("")
        edtTour.setText("")
        edtTourDateCode.setText("")
        edtTourStartDate.setText("")
        edtNoOfNights.setText("")
        edtTotalAmount.setText("")
    }

    private fun applyFiltersData() {

        val intent = Intent()
        intent.putExtra("TourBookingNo", edtTourBookingNo.text.toString())
        intent.putExtra("Name", edtName.text.toString())
        intent.putExtra("MobileNo", edtMobileNo.text.toString())
        intent.putExtra("SectorID", SectorId)
        intent.putExtra("Sector", edtSector.text.toString())
        intent.putExtra("TravelType", edtTravelType.text.toString())
        intent.putExtra("TourID", TourId)
        intent.putExtra("Tour", edtTour.text.toString())
        intent.putExtra("TourDateCode", edtTourDateCode.text.toString())
        intent.putExtra("TourStartDate", TourStartDate)
        intent.putExtra("NoOfNights", edtNoOfNights.text.toString())
        intent.putExtra("TotalAmount",edtTotalAmount.text.toString())

        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    // region Travel Type

    /** AI005
     * This method is to open Travel Type dialog
     */
    private fun selectTravelTypeDialog() {
        var dialogSelectTravelType = Dialog(this)
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

        val itemAdapter = DialogInitialAdapter(this, arrTravelTypeList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                TravelTypeName = arrTravelTypeList!![pos]
                edtTravelType.setText(TravelTypeName)
                dialogSelectTravelType!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectTravelType!!.show()
    }

    // endregion

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

                            if(SectorId != 0) {
                                for(i in 0 until arrayListSector!!.size) {
                                    if(arrayListSector!![i].ID == SectorId) {
                                        SectorId = arrayListSector!![i].ID!!
                                        Sector = arrayListSector!![i].SectorName!!
                                        edtSector.setText(Sector)
                                    }
                                }
                            }
                            if(mode == 2) {
                                selectSectorDialog()
                            }
                        } else {
                            toast("Sector Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                        toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<SectorListResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }
    /** AI005
     * This method is to open Sector dialog
     */
    private fun selectSectorDialog() {
        var dialogSelectSector = Dialog(this)
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

        val itemAdapter = DialogSectorAdapter(this, arrayListSector!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                SectorId = arrayListSector!![pos].ID!!
                Sector = arrayListSector!![pos].SectorName!!
                edtSector.setText(Sector)

                TourId = 0
                Tour = ""
                edtTour.setText("")

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

                    val itemAdapter = DialogSectorAdapter(this@PendingPaymentFilterActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            SectorId = arrItemsFinal1!![pos].ID!!
                            Sector = arrItemsFinal1!![pos].SectorName!!
                            edtSector.setText(Sector)

                            TourId = 0
                            Tour = ""
                            edtTour.setText("")

                            if(SectorId != 0) {
                                GetAllTourPackageAPI(1)
                            }
                            dialogSelectSector!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogSectorAdapter(this@PendingPaymentFilterActivity, arrayListSector!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            SectorId = arrayListSector!![pos].ID!!
                            Sector = arrayListSector!![pos].SectorName!!
                            edtSector.setText(Sector)

                            TourId = 0
                            Tour = ""
                            edtTour.setText("")

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

    // region Tour

    /** AI005
     * This method is to retrived Tour List data from sectorid and travel type
     */
    private fun GetAllTourPackageAPI(mode: Int) {

        showProgress()

        var traveltype = ""

        val jsonObject = JSONObject()
        jsonObject.put("SectorID", SectorId)
        jsonObject.put("TravelType", traveltype)

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

                            if(TourId != 0) {
                                for(i in 0 until arrayListTourPackage!!.size) {
                                    if(arrayListTourPackage!![i].TourID == TourId) {
                                        TourId = arrayListTourPackage!![i].TourID!!
                                        Tour = arrayListTourPackage!![i].TourName!!
                                        edtTour.setText(Tour)
                                    }
                                }
                            }

                            if(mode == 2) {
                                selectTourPackageDialog()
                            }
                        } else {
                            toast("Tour/Package Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                    }
                }
            }

            override fun onFailure(call: Call<TourPackageListResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }

    /** AI005
     * This method is to open Tour dialog
     */
    private fun selectTourPackageDialog() {
        var dialogSelectTourPackage = Dialog(this)
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

        val itemAdapter = DialogTourPackageAdapter(this, arrayListTourPackage!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                TourId = arrayListTourPackage!![pos].TourID!!
                Tour = arrayListTourPackage!![pos].TourName!!

                edtTour.setText(Tour)

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

                    val itemAdapter = DialogTourPackageAdapter(this@PendingPaymentFilterActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            TourId = arrItemsFinal1!![pos].TourID!!
                            Tour = arrItemsFinal1!![pos].TourName!!
                            edtTour.setText(Tour)
                            dialogSelectTourPackage!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogTourPackageAdapter(this@PendingPaymentFilterActivity, arrayListTourPackage!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            TourId = arrayListTourPackage!![pos].TourID!!
                            Tour = arrayListTourPackage!![pos].TourName!!
                            edtTour.setText(Tour)
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

}