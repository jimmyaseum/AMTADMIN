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
import com.app.amtadminapp.activity.Reports.ReportView.ConfirmedHotelVoucherReportViewActivity
import com.app.amtadminapp.adapter.dialog.*
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.model.response.booking.MonthDataModel
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_confirmed_hotel_voucher_report.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ConfirmedHotelVoucherReportActivity : BaseActivity(), View.OnClickListener {

    var TourDateCode = ""

    /* From Date and To Date */
    var calFromDate = Calendar.getInstance()
    var calToDate = Calendar.getInstance()
    var From_Date: String = ""
    var To_Date: String = ""

    /* City */
    var arrayListCity: ArrayList<CityModel>? = null
    var CityId : Int = 0
    var CityName : String = ""

    /* Tour */
    var arrayListTourPackage: ArrayList<TourPackageListModel>? = ArrayList()
    var TourId : Int = 0
    var TourName = ""

    private var arrHotelList: ArrayList<HotelListModel> = ArrayList()
    var HotelId : Int = 0
    var HotelName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmed_hotel_voucher_report)
        initializeView()
    }

    override fun initializeView() {
        imgBack.setOnClickListener(this)
        edtSelectTour.setOnClickListener(this)
        edtPlace.setOnClickListener(this)
        edtHotel.setOnClickListener(this)
        edtFromDate.setOnClickListener(this)
        edtToDate.setOnClickListener(this)
        imgclearST.setOnClickListener(this)
        imgclearPL.setOnClickListener(this)
        imgclearHO.setOnClickListener(this)
        imgclearFD.setOnClickListener(this)
        imgclearTD.setOnClickListener(this)
        CardGetDetail.setOnClickListener(this)

        val profileName = intent.getStringExtra("Title")
        tbTvTitle.setText(profileName)
        Log.d("Title==>", ""+profileName)

        GetAllTourPackageAPI(1)
//        GetAllHotelAPI(1)
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgclearST -> {
                clearST()
            }
            R.id.imgclearPL -> {
                clearPL()
            }
            R.id.imgclearHO -> {
                clearHO()
            }
            R.id.imgclearFD -> {
                clearFT()
            }
            R.id.imgclearTD -> {
                clearTD()
            }
            R.id.edtSelectTour -> {
                preventTwoClick(v)
                if (!arrayListTourPackage.isNullOrEmpty()) {
                    selectTourPackageDialog()
                } else {
                    GetAllTourPackageAPI(2)
                }
            }
            R.id.edtPlace -> {
                preventTwoClick(v)
                if(!arrayListCity.isNullOrEmpty()) {
                    selectCityDialog()
                } else {
                    GetAllCityAPI(2)
                }
            }
            R.id.edtHotel -> {
                preventTwoClick(v)
                if(!arrHotelList.isNullOrEmpty()) {
                    selectHotelDialog()
                } else {
                    toast("Tour Month Not Available.", AppConstant.TOAST_SHORT)
                }
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
            R.id.CardGetDetail -> {
                val intent = Intent(this, ConfirmedHotelVoucherReportViewActivity::class.java)

                intent.putExtra("SelectTour", TourId)
                intent.putExtra("Place", CityId)
                intent.putExtra("Hotel", HotelId)
                intent.putExtra("FromDate", From_Date)
                intent.putExtra("ToDate", To_Date)
                startActivity(intent)
            }
        }
    }

    private fun clearST() {
        TourId = 0
        edtSelectTour.setText("")
    }

    private fun clearPL() {
        CityId = 0
        edtPlace.setText("")
    }

    private fun clearHO() {
        HotelId = 0
        edtHotel.setText("")
    }

    private fun clearFT() {
        From_Date = ""
        edtFromDate.setText("")
    }

    private fun clearTD() {
        To_Date = ""
        edtToDate.setText("")
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

                edtSelectTour.setText(TourName)

                dialogSelectTourPackage!!.dismiss()

                if (TourId != 0){
                    GetAllCityAPI(1)
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
                val arrItemsFinal1: ArrayList<TourPackageListModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrayListTourPackage!!) {
                        if (model.TourName!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogTourPackageAdapter(this@ConfirmedHotelVoucherReportActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            TourId = arrItemsFinal1!![pos].ID!!
                            TourName = arrItemsFinal1!![pos].TourName!!
                            edtSelectTour.setText(TourName)

                            if (TourId != 0){
                                GetAllCityAPI(1)
                            }

                            dialogSelectTourPackage!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogTourPackageAdapter(this@ConfirmedHotelVoucherReportActivity, arrayListTourPackage!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            TourId = arrayListTourPackage!![pos].ID!!
                            TourName = arrayListTourPackage!![pos].TourName!!
                            edtSelectTour.setText(TourName)

                            if (TourId != 0){
                                GetAllCityAPI(1)
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

    private fun selectCityDialog() {
        var dialogSelectCity = Dialog(this)
        dialogSelectCity.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectCity.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectCity.window!!.attributes)

        dialogSelectCity.window!!.attributes = lp
        dialogSelectCity.setCancelable(true)
        dialogSelectCity.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectCity.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectCity.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectCity.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectCity.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogCityAdapter(this, arrayListCity!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                CityId = arrayListCity!![pos].CityID
                CityName = arrayListCity!![pos].CityName

                edtPlace.setText(CityName)
                callHotelNameApi(CityId)
                dialogSelectCity!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrItemsFinal1: java.util.ArrayList<CityModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrayListCity!!) {
                        if (model.CityName!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogCityAdapter(this@ConfirmedHotelVoucherReportActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            CityId = arrItemsFinal1!![pos].CityID
                            CityName = arrItemsFinal1!![pos].CityName

                            edtPlace.setText(CityName)
                            callHotelNameApi(CityId)
                            dialogSelectCity!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogCityAdapter(this@ConfirmedHotelVoucherReportActivity, arrayListCity!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            CityId = arrayListCity!![pos].CityID
                            CityName = arrayListCity!![pos].CityName

                            edtPlace.setText(CityName)
                            callHotelNameApi(CityId)
                            dialogSelectCity!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectCity!!.show()
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

        val itemAdapter = DialogHotelNameAdapter(this, arrHotelList)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                HotelId = arrHotelList!![pos].ID!!
                HotelName = arrHotelList!![pos].HotelName!!
                edtHotel.setText(HotelName)

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
                val arrItemsFinal1: java.util.ArrayList<HotelListModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrHotelList!!) {
                        if (model.HotelName!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogHotelNameAdapter(this@ConfirmedHotelVoucherReportActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            HotelId = arrItemsFinal1!![pos].ID!!
                            HotelName = arrItemsFinal1!![pos].HotelName!!

                            edtHotel.setText(HotelName)
                            dialogSelectHotel!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogCityAdapter(this@ConfirmedHotelVoucherReportActivity, arrayListCity!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            HotelId = arrayListCity!![pos].CityID
                            HotelName = arrayListCity!![pos].CityName

                            edtPlace.setText(HotelName)
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
                                        edtSelectTour.setText(TourName)
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

    private fun GetAllCityAPI(mode: Int) {

        showProgress()
        val call = ApiUtils.apiInterface.getAllCity()

        call.enqueue(object : Callback<CityResponse> {
            override fun onResponse(call: Call<CityResponse>, response: Response<CityResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.code == 200) {
                        hideProgress()
                        arrayListCity = response.body()?.data!!
                        if(arrayListCity!!.size > 0) {
                            if(CityId != 0) {
                                for(i in 0 until arrayListCity!!.size) {
                                    if(arrayListCity!![i].CityID == CityId) {
                                        CityId = arrayListCity!![i].CityID!!
                                        CityName = arrayListCity!![i].CityName!!
                                        edtPlace.setText(CityName)
                                    }
                                }
                            }
                            if(mode == 2) {
                                selectCityDialog()
                            }
                        } else {
                            toast("No Value Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                        toast(response.body()?.message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun callHotelNameApi(tourid: Int) {
        showProgress()
        val call = ApiUtils.apiInterface.getHotelByPlace(tourid)
        call.enqueue(object : Callback<HotelListResponse> {
            override fun onResponse(call: Call<HotelListResponse>, response: Response<HotelListResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        hideProgress()
                        arrHotelList.clear()
                        arrHotelList = response.body()?.Data!!
                        if(HotelId != 0) {
                            for(i in 0 until arrHotelList.size) {
                                if(arrHotelList[i].ID == HotelId) {
                                    HotelId = arrHotelList[i].ID!!
                                    HotelName = arrHotelList[i].HotelName!!
                                    edtHotel.setText(HotelName)
                                }
                            }
                        }
                    }
                    else {
                        hideProgress()
                        toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<HotelListResponse>, t: Throwable) {
                hideProgress()
            }
        })
    }

    private fun updateFromDate(cal: Calendar) {
        From_Date = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        edtFromDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
    }

    private fun updateToDate(cal: Calendar) {
        To_Date = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        edtToDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
    }

}