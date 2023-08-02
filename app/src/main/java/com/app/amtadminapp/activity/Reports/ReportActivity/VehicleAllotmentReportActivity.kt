package com.app.amtadminapp.activity.Reports.ReportActivity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.activity.Reports.ReportView.VehicleAllotmentReportViewActivity
import com.app.amtadminapp.adapter.dialog.*
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.model.response.booking.DateDataModel
import com.app.amtadminapp.model.response.booking.MonthDataModel
import com.app.amtadminapp.model.response.booking.TourInfoResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_vehicle_allotment_report.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class VehicleAllotmentReportActivity : BaseActivity(), View.OnClickListener {

    var TourDateCode = ""

    /* Sector */
    var arrayListSector: ArrayList<SectorListModel>? = ArrayList()
    var SectorId : Int = 0
    var Sector = ""

    /* Tour */
    var arrayListTourPackage: ArrayList<TourPackageListModel>? = ArrayList()
    var TourId : Int = 0
    var Tour = ""

    /*Month Name*/
    private var arrMonthList: ArrayList<MonthDataModel> = ArrayList()
    var MonthId : Int = 0
    var MonthName : String = ""

    /*Tour Date*/
    private var arrDateList: ArrayList<DateDataModel> = ArrayList()
    var DateCode : String = ""

    var calDate = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_allotment_report)
        initializeView()
    }

    override fun initializeView() {
        imgBack.setOnClickListener(this)
        edtSectorName.setOnClickListener(this)
        edtPackageName.setOnClickListener(this)
        edtMonth.setOnClickListener(this)
        edtTourDate.setOnClickListener(this)
        CardGetDetail.setOnClickListener(this)
        imgclearSN.setOnClickListener(this)
        imgclearTPN.setOnClickListener(this)
        imgclearMO.setOnClickListener(this)
        imgclearTD.setOnClickListener(this)

        val profileName = intent.getStringExtra("Title")
        tbTvTitle.setText(profileName)
        Log.d("Title==>", ""+profileName)

        GetAllSectorAPI(1)
//        GetAllHotelAPI(1)
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgclearSN -> {
                clearSN()
            }
            R.id.imgclearTPN -> {
                clearTPN()
            }
            R.id.imgclearMO -> {
                clearMO()
            }
            R.id.imgclearTD -> {
                clearTD()
            }
            R.id.edtSectorName -> {
                preventTwoClick(v)
                if (!arrayListSector.isNullOrEmpty()) {
                    selectSectorDialog()
                } else {
                    GetAllSectorAPI(2)
                }
            }
            R.id.edtPackageName -> {
                preventTwoClick(v)
                if (!arrayListTourPackage.isNullOrEmpty()) {
                    selectTourPackageDialog()
                } else {
                    GetAllTourPackageAPI(2)
                }
            }
            R.id.CardGetDetail -> {
                val intent = Intent(this, VehicleAllotmentReportViewActivity::class.java)
                intent.putExtra("TourDateCode", TourDateCode)
                startActivity(intent)
            }
            R.id.edtMonth -> {
                preventTwoClick(v)
                if(!arrMonthList.isNullOrEmpty()) {
                    selectMonthDialog()
                } else {
                    toast("Tour Month Not Available.", AppConstant.TOAST_SHORT)
                }
            }
            R.id.edtTourDate -> {
                preventTwoClick(v)
                if(!arrDateList.isNullOrEmpty()) {
                    selectDateDialog()
                } else {
                    toast("Tour Data Not Available.", AppConstant.TOAST_SHORT)
                }
            }
        }
    }

    private fun clearSN() {
        SectorId = 0
        Sector = ""
        edtSectorName.setText("")

    }

    private fun clearTPN() {
        TourId = 0
        Tour = ""
        edtPackageName.setText("")
    }

    private fun clearMO() {
        MonthId = 0
        MonthName = ""
        edtMonth.setText("")
    }

    private fun clearTD() {
        TourDateCode = ""
        edtTourDate.setText("")
    }

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
                edtSectorName.setText(Sector)


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

                    val itemAdapter = DialogSectorAdapter(this@VehicleAllotmentReportActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            SectorId = arrItemsFinal1!![pos].ID!!
                            Sector = arrItemsFinal1!![pos].SectorName!!
                            edtSectorName.setText(Sector)


                            if(SectorId != 0) {
                                GetAllTourPackageAPI(1)
                            }
                            dialogSelectSector!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogSectorAdapter(this@VehicleAllotmentReportActivity, arrayListSector!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            SectorId = arrayListSector!![pos].ID!!
                            Sector = arrayListSector!![pos].SectorName!!
                            edtSectorName.setText(Sector)

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

                edtPackageName.setText(Tour)
                callTourDetailApi(TourId)
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

                    val itemAdapter = DialogTourPackageAdapter(this@VehicleAllotmentReportActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            TourId = arrItemsFinal1!![pos].TourID!!
                            Tour = arrItemsFinal1!![pos].TourName!!
                            edtPackageName.setText(Tour)
                            callTourDetailApi(TourId)
                            dialogSelectTourPackage!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogTourPackageAdapter(this@VehicleAllotmentReportActivity, arrayListTourPackage!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            TourId = arrayListTourPackage!![pos].TourID!!
                            Tour = arrayListTourPackage!![pos].TourName!!
                            edtPackageName.setText(Tour)
                            callTourDetailApi(TourId)
                            dialogSelectTourPackage!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectTourPackage!!.show()
    }

    private fun selectMonthDialog() {
        var dialogSelectMonth = Dialog(this)
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

        val itemAdapter = DialogMonthAdapter(this, arrMonthList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                MonthId = arrMonthList!![pos].Month!!
                MonthName = arrMonthList!![pos].MonthName!!
                edtMonth.setText(MonthName)
                arrDateList = arrMonthList!![pos].DateData!!

                dialogSelectMonth!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectMonth!!.show()
    }

    private fun selectDateDialog() {
        var dialogSelectDate = Dialog(this)
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

        val itemAdapter = DialogTourDateAdapter(this, arrDateList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                DateCode = arrDateList!![pos].TourDate!!
                edtTourDate.setText(arrDateList!![pos].TourDate)
                TourDateCode = arrDateList!![pos].TourDateCode.toString()

                dialogSelectDate!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectDate!!.show()
    }

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
                                        edtSectorName.setText(Sector)
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
                                        edtPackageName.setText(Tour)
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

    private fun callTourDetailApi(tourid: Int) {

        val call = ApiUtils.apiInterface.getTourDates(tourid)
        call.enqueue(object : Callback<TourInfoResponse> {
            override fun onResponse(call: Call<TourInfoResponse>, response: Response<TourInfoResponse>) {
                if (response.code() == 200) {
                    val arrayList =  response.body()?.Data!![0]
                    if (response.body()?.Status == 200) {
                        arrMonthList.clear()
                        arrMonthList = response.body()?.Data!![0].monthData!!
                        for(i in 0 until arrMonthList.size) {
                            if(arrMonthList[i].Month == MonthId) {
                                MonthId = arrMonthList[i].Month!!
                                MonthName = arrMonthList[i].MonthName!!
                                edtMonth.setText(MonthName)
                                arrDateList = arrMonthList[i].DateData!!
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<TourInfoResponse>, t: Throwable) {
            }
        })
    }

}