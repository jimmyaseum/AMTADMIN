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
import com.app.amtadminapp.activity.Reports.ReportView.HotelBlockReportViewActivity
import com.app.amtadminapp.adapter.dialog.DialogHotelBlockAdapter
import com.app.amtadminapp.adapter.dialog.DialogTourPackageAdapter
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_confirmed_hotel_voucher_report.*
import kotlinx.android.synthetic.main.activity_hotel_block_report.*
import kotlinx.android.synthetic.main.activity_hotel_block_report.CardGetDetail
import kotlinx.android.synthetic.main.activity_hotel_block_report.edtFromDate
import kotlinx.android.synthetic.main.activity_hotel_block_report.edtToDate
import kotlinx.android.synthetic.main.activity_hotel_block_report.imgBack
import kotlinx.android.synthetic.main.activity_hotel_block_report.imgclearFD
import kotlinx.android.synthetic.main.activity_hotel_block_report.imgclearTD
import kotlinx.android.synthetic.main.activity_hotel_block_report.tbTvTitle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HotelBlockReportActivity : BaseActivity(), View.OnClickListener {
    /* Tour */
    var arrayListTourPackage: ArrayList<TourPackageListModel>? = ArrayList()
    var TourId : Int = 0
    var TourName = ""

    /* Hotel */
    var arrayListHotel: ArrayList<HotelListModel>? = ArrayList()
    var HotelId : Int = 0
    var HotelName = ""

    /* From Date and To Date */
    var calFromDate = Calendar.getInstance()
    var calToDate = Calendar.getInstance()
    var From_Date: String = ""
    var To_Date: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_block_report)
        initializeView()
    }

    override fun initializeView() {
        imgBack.setOnClickListener(this)
        edtTourName.setOnClickListener(this)
        edtHotelName.setOnClickListener(this)
        edtFromDate.setOnClickListener(this)
        edtToDate.setOnClickListener(this)
        imgclearTN.setOnClickListener(this)
        imgclearHN.setOnClickListener(this)
        imgclearFD.setOnClickListener(this)
        imgclearTD.setOnClickListener(this)
        CardGetDetail.setOnClickListener(this)

        val profileName = intent.getStringExtra("Title")
        tbTvTitle.setText(profileName)
        Log.d("Title==>", ""+profileName)

        GetAllTourPackageAPI(1)
        GetAllHotelAPI(1)
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgclearTN -> {
                clearTN()
            }
            R.id.imgclearHN -> {
                clearHN()
            }
            R.id.imgclearFD -> {
                clearFT()
            }
            R.id.imgclearTD -> {
                clearTD()
            }
            R.id.edtTourName -> {
                preventTwoClick(v)
                if (!arrayListTourPackage.isNullOrEmpty()) {
                    selectTourPackageDialog()
                } else {
                    GetAllTourPackageAPI(2)
                }
            }
            R.id.edtHotelName -> {
                preventTwoClick(v)
                if (!arrayListHotel.isNullOrEmpty()) {
                    selectHotelDialog()
                } else {
                    GetAllHotelAPI(2)
                }
            }
            R.id.CardGetDetail -> {
                val intent = Intent(this, HotelBlockReportViewActivity::class.java)
                intent.putExtra("Tour", TourId)
                intent.putExtra("Hotel", HotelId)
                intent.putExtra("FromDate", edtFromDate.text.toString())
                intent.putExtra("ToDate", edtToDate.text.toString())
                startActivity(intent)
            }
            R.id.edtFromDate -> {
                preventTwoClick(v)
                val FromDT = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calFromDate.set(Calendar.YEAR, year)
                        calFromDate.set(Calendar.MONTH, monthOfYear)
                        calFromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateFromDate(calFromDate)
                    },
                    calFromDate.get(Calendar.YEAR),
                    calFromDate.get(Calendar.MONTH),
                    calFromDate.get(Calendar.DAY_OF_MONTH)
                )
//                dpd.datePicker.minDate = System.currentTimeMillis() - 1000
//                dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
                FromDT.show()
            }
            R.id.edtToDate -> {
                preventTwoClick(v)
                val ToDT = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calToDate.set(Calendar.YEAR, year)
                        calToDate.set(Calendar.MONTH, monthOfYear)
                        calToDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateToDate(calToDate)
                    },
                    calToDate.get(Calendar.YEAR),
                    calToDate.get(Calendar.MONTH),
                    calToDate.get(Calendar.DAY_OF_MONTH)
                )
//                dpd.datePicker.minDate = System.currentTimeMillis() - 1000
//                dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
                ToDT.show()
            }
        }
    }

    private fun clearTN() {
        TourId = 0
        edtTourName.setText("")
    }

    private fun clearHN() {
        HotelId = 0
        edtHotelName.setText("")
    }

    private fun clearFT() {
        From_Date = ""
        edtFromDate.setText("")
    }

    private fun clearTD() {
        To_Date = ""
        edtToDate.setText("")
    }

    private fun updateFromDate(cal: Calendar) {
        From_Date = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        edtFromDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
    }

    private fun updateToDate(cal: Calendar) {
        To_Date = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        edtToDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
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

                TourId = arrayListTourPackage!![pos].ID!!
                TourName = arrayListTourPackage!![pos].TourName!!

                edtTourName.setText(TourName)

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

                    val itemAdapter = DialogTourPackageAdapter(this@HotelBlockReportActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            TourId = arrItemsFinal1!![pos].ID!!
                            TourName = arrItemsFinal1!![pos].TourName!!
                            edtTourName.setText(TourName)
                            dialogSelectTourPackage!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogTourPackageAdapter(this@HotelBlockReportActivity, arrayListTourPackage!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            TourId = arrayListTourPackage!![pos].ID!!
                            TourName = arrayListTourPackage!![pos].TourName!!
                            edtTourName.setText(TourName)
                            dialogSelectTourPackage!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectTourPackage!!.show()
    }

    private fun selectHotelDialog() {
        var dialogSelectHotel = Dialog(this)
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

        val itemAdapter = DialogHotelBlockAdapter(this, arrayListHotel!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                HotelId = arrayListHotel!![pos].ID!!
                HotelName = arrayListHotel!![pos].HotelName!!

                edtHotelName.setText(HotelName)

                dialogSelectHotel!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrItemsFinal1: ArrayList<HotelListModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrayListHotel!!) {
                        if (model.HotelName!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogHotelBlockAdapter(this@HotelBlockReportActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            HotelId = arrItemsFinal1!![pos].ID!!
                            HotelName = arrItemsFinal1!![pos].HotelName!!
                            edtHotelName.setText(HotelName)
                            dialogSelectHotel!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogHotelBlockAdapter(this@HotelBlockReportActivity, arrayListHotel!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            HotelId = arrayListHotel!![pos].ID!!
                            HotelName = arrayListHotel!![pos].HotelName!!
                            edtHotelName.setText(HotelName)
                            dialogSelectHotel!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectHotel!!.show()
    }

    private fun GetAllTourPackageAPI(mode: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.getAllTour()
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
                                    if(arrayListTourPackage!![i].ID == TourId) {
                                        TourId = arrayListTourPackage!![i].ID!!
                                        TourName = arrayListTourPackage!![i].TourName!!
                                        edtTourName.setText(TourName)
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

    private fun GetAllHotelAPI(mode: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.getAllHotel()
        call.enqueue(object : Callback<HotelListResponse> {
            override fun onResponse(call: Call<HotelListResponse>, response: Response<HotelListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        hideProgress()

                        if(!arrayListHotel.isNullOrEmpty()) {
                            arrayListHotel!!.clear()
                        }

                        arrayListHotel = response.body()?.Data!!
                        if(arrayListHotel!!.size > 0) {

                            if(HotelId != 0) {
                                for(i in 0 until arrayListHotel!!.size) {
                                    if(arrayListHotel!![i].ID == HotelId) {
                                        HotelId = arrayListHotel!![i].ID!!
                                        HotelName = arrayListHotel!![i].HotelName!!
                                        edtHotelName.setText(HotelName)
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

            override fun onFailure(call: Call<HotelListResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }

}