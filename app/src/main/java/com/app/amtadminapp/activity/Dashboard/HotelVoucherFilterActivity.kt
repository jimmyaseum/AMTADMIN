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
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.dialog.DialogBranchAdapter
import com.app.amtadminapp.adapter.dialog.DialogCompanyAdapter
import com.app.amtadminapp.adapter.dialog.DialogEmployeeAdapter
import com.app.amtadminapp.adapter.dialog.DialogTourPackageAdapter
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.model.response.booking.CompanyListModel
import com.app.amtadminapp.model.response.booking.CompanyListResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.*
import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.edtBookBy
import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.edtBranch
import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.edtCompanyName
import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.edtName
import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.edtNoOfNights
import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.edtTourBookingNo
import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.edtTourDateCode
import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.tvApply
import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.tvClearAll
import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.tvClose
import kotlinx.android.synthetic.main.activity_dashboard_hotel_voucher_filter.tvFilters
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HotelVoucherFilterActivity  : BaseActivity(), View.OnClickListener {

    var calDate = Calendar.getInstance()

    //HotelVoucher - Filter
    var HotelVoucher = ""
    var TourBookingNo = ""
    var TourName = ""
    var TourDate = ""
    var CompanyName = ""
    var NoOfNights = ""
    var VehicleSharing = ""
    var BookBy = ""
    var Branch = ""
    var Name = ""
    var TourDateCode = ""

    var arrayListTourPackage: ArrayList<TourPackageListModel>? = ArrayList()
    var TourId : Int = 0

    var arrayListCompany: ArrayList<CompanyListModel>? = ArrayList()
    var CompanyId : Int = 0

    var arrayListEmployee: ArrayList<EmployeeListModel>? = ArrayList()
    var BookById : Int = 0

    var arrayListBranch: ArrayList<BranchListModel>? = ArrayList()
    var BranchId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_hotel_voucher_filter)
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
        if (intent.hasExtra("TourDateCode")) {
            TourDateCode = intent.getStringExtra("TourDateCode").toString()
            edtTourDateCode.setText(TourDateCode)
        }

        if (intent.hasExtra("HotelVoucher")) {
            HotelVoucher = intent.getStringExtra("HotelVoucher").toString()
            edtHotelVoucher.setText(HotelVoucher)
        }
        if (intent.hasExtra("TourID")) {
            TourId = intent.getIntExtra("TourID",0)
        }
        if (intent.hasExtra("TourName")) {
            TourName = intent.getStringExtra("TourName").toString()
            edtTourName.setText(TourName)
        }
        if (intent.hasExtra("TourDate")) {
            TourDate = intent.getStringExtra("TourDate").toString()
            val mTourDate = convertDateStringToString(TourDate, AppConstant.yyyy_MM_dd_Dash, AppConstant.dd_MM_yyyy_Slash)!!
            edtTourDate.setText(mTourDate)
        }
        if (intent.hasExtra("CompanyID")) {
            CompanyId = intent.getIntExtra("CompanyID",0)
        }
        if (intent.hasExtra("CompanyName")) {
            CompanyName = intent.getStringExtra("CompanyName").toString()
            edtCompanyName.setText(CompanyName)
        }
        if (intent.hasExtra("NoOfNights")) {
            NoOfNights = intent.getStringExtra("NoOfNights").toString()
            edtNoOfNights.setText(NoOfNights)
        }
        if (intent.hasExtra("VehicleSharing")) {
            VehicleSharing = intent.getStringExtra("VehicleSharing").toString()
            edtVehicleSharing.setText(VehicleSharing)
        }
        if (intent.hasExtra("BookByID")) {
            BookById = intent.getIntExtra("BookByID",0)
        }
        if (intent.hasExtra("BookBy")) {
            BookBy = intent.getStringExtra("BookBy").toString()
            edtBookBy.setText(BookBy)
        }
        if (intent.hasExtra("BranchID")) {
            BranchId = intent.getIntExtra("BranchID",0)
        }
        if (intent.hasExtra("Branch")) {
            Branch = intent.getStringExtra("Branch").toString()
            edtBranch.setText(Branch)
        }
    }

    override fun initializeView() {

        llName.gone()
        llTourDateCode.gone()
        llVehicleSharing.gone()

        edtTourName.setOnClickListener(this)
        edtCompanyName.setOnClickListener(this)
        edtBookBy.setOnClickListener(this)
        edtBranch.setOnClickListener(this)

        tvClearAll.setOnClickListener(this)
        tvFilters.setOnClickListener(this)
        tvClose.setOnClickListener(this)
        tvApply.setOnClickListener(this)
        edtTourDate.setOnClickListener(this)

        GetAllTourPackageAPI(1)
        GetAllEmployeeAPI(1)
        GetAllBranchAPI(1)
        GetAllCompanyAPI(1)


    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.tvClearAll -> {
                clearAdapterData()
            }
            R.id.tvFilters -> {
                finish()
            }
            R.id.tvClose -> {
                finish()
            }
            R.id.tvApply -> {
                applyFiltersData()
            }
            R.id.edtTourDate -> {
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
            R.id.edtTourName -> {
                preventTwoClick(v)
                if (!arrayListTourPackage.isNullOrEmpty()) {
                    selectTourPackageDialog()
                } else {
                    GetAllTourPackageAPI(2)
                }
            }
            R.id.edtCompanyName -> {
                preventTwoClick(v)
                if (!arrayListCompany.isNullOrEmpty()) {
                    selectCompanyDialog()
                } else {
                    GetAllCompanyAPI(2)
                }
            }
            R.id.edtBranch -> {
                preventTwoClick(v)
                if (!arrayListBranch.isNullOrEmpty()) {
                    selectBranchDialog()
                } else {
                    GetAllBranchAPI(2)
                }
            }
            R.id.edtBookBy -> {
                preventTwoClick(v)
                if (!arrayListEmployee.isNullOrEmpty()) {
                    selectEmployeeDialog()
                } else {
                    GetAllEmployeeAPI(2)
                }
            }
        }
    }

    private fun updateSelectDate(cal: Calendar) {
        TourDate = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        edtTourDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
    }

    private fun clearAdapterData() {
        HotelVoucher = ""
        TourId = 0
        TourName = ""
        TourDate = ""
        CompanyId = 0
        CompanyName = ""
        NoOfNights = ""
        BookById = 0
        BookBy = ""
        BranchId = 0
        Branch = ""
        TourBookingNo = ""
        Name = ""
        TourDateCode = ""

        edtHotelVoucher.setText("")
        edtTourName.setText("")
        edtTourDate.setText("")
        edtCompanyName.setText("")
        edtNoOfNights.setText("")
        edtVehicleSharing.setText("")
        edtBookBy.setText("")
        edtBranch.setText("")
        edtTourBookingNo.setText("")
        edtName.setText("")
        edtTourDateCode.setText("")
    }

    private fun applyFiltersData() {
        val intent = Intent()
        intent.putExtra("TourBookingNo", edtTourBookingNo.text.toString())
        intent.putExtra("Name", edtName.text.toString())
        intent.putExtra("TourDateCode", edtTourDateCode.text.toString())
        intent.putExtra("HotelVoucher", edtHotelVoucher.text.toString())
        intent.putExtra("TourID", TourId)
        intent.putExtra("TourName", edtTourName.text.toString())
        intent.putExtra("TourDate", TourDate)
        intent.putExtra("CompanyID", CompanyId)
        intent.putExtra("CompanyName", edtCompanyName.text.toString())
        intent.putExtra("NoOfNights", edtNoOfNights.text.toString())
        intent.putExtra("VehicleSharing", edtVehicleSharing.text.toString())
        intent.putExtra("BookByID", BookById)
        intent.putExtra("BookBy", edtBookBy.text.toString())
        intent.putExtra("BranchID",BranchId)
        intent.putExtra("Branch", edtBranch.text.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

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
                                    edtCompanyName.setText(arrayListCompany!![i].CompanyName!!)
                                }
                            }
                            if(mode == 2) {
                                selectCompanyDialog()
                            }
                        } else {
                            toast("Company Name Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                        toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<CompanyListResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open Company dialog
     */
    private fun selectCompanyDialog() {
        var dialogSelectCompany = Dialog(this)
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

        val itemAdapter = DialogCompanyAdapter(this, arrayListCompany!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                CompanyId = arrayListCompany!![pos].ID!!
                CompanyName = arrayListCompany!![pos].CompanyName!!
                edtCompanyName.setText(CompanyName)

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

                    val itemAdapter = DialogCompanyAdapter(this@HotelVoucherFilterActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {
                            CompanyId = arrItemsFinal1!![pos].ID!!
                            CompanyName = arrItemsFinal1!![pos].CompanyName!!
                            edtCompanyName.setText(CompanyName)
                            dialogSelectCompany!!.dismiss()
                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogCompanyAdapter(this@HotelVoucherFilterActivity, arrayListCompany!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            CompanyId = arrayListCompany!![pos].ID!!
                            CompanyName = arrayListCompany!![pos].CompanyName!!
                            edtCompanyName.setText(CompanyName)
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

    // region Tour

    /** AI005
     * This method is to retrived Tour List data from sectorid and travel type
     */
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

                    val itemAdapter = DialogTourPackageAdapter(this@HotelVoucherFilterActivity, arrItemsFinal1)
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
                    val itemAdapter = DialogTourPackageAdapter(this@HotelVoucherFilterActivity, arrayListTourPackage!!)
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
                            if(BookById != 0) {
                                for (i in 0 until arrayListEmployee!!.size) {
                                    if (arrayListEmployee!![i].ID!! == BookById) {
                                        BookById = arrayListEmployee!![i].ID!!
                                        BookBy = arrayListEmployee!![i].FirstName!! + " " + arrayListEmployee!![i].LastName!!
                                        edtBookBy.setText(BookBy)
                                    }
                                }
                            }
                            if(mode == 2) {
                                selectEmployeeDialog()
                            }
                            hideProgress()
                        } else {
                            toast("Employee Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                        toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<EmployeeListResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }
    /** AI005
     * This method is to open Employee dialog
     */
    private fun selectEmployeeDialog() {
        var dialogSelectEmployee = Dialog(this)
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

        val itemAdapter = DialogEmployeeAdapter(this, arrayListEmployee!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                BookById = arrayListEmployee!![pos].ID!!
                BookBy = arrayListEmployee!![pos].FirstName!! + " " +arrayListEmployee!![pos].LastName!!
                edtBookBy.setText(BookBy)

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

                    val itemAdapter = DialogEmployeeAdapter(this@HotelVoucherFilterActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {
                            BookById = arrItemsFinal1!![pos].ID!!
                            BookBy = arrItemsFinal1!![pos].FirstName!! + " " +arrItemsFinal1!![pos].LastName!!
                            edtBookBy.setText(BookBy)
                            dialogSelectEmployee!!.dismiss()
                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogEmployeeAdapter(this@HotelVoucherFilterActivity, arrayListEmployee!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            BookById = arrayListEmployee!![pos].ID!!
                            BookBy = arrayListEmployee!![pos].FirstName!! + " " +arrayListEmployee!![pos].LastName!!
                            edtBookBy.setText(BookBy)
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

    //    region Branch
    /** AI005
     * This method is to retrived Branch List data from api
     */
    private fun GetAllBranchAPI(mode: Int) {
        showProgress()
        val call = ApiUtils.apiInterface.getAllBranch()
        call.enqueue(object : Callback<BranchListResponse> {
            override fun onResponse(call: Call<BranchListResponse>, response: Response<BranchListResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        hideProgress()
                        arrayListBranch = response.body()?.Data!!
                        if(arrayListBranch!!.size > 0) {

                            if(BranchId != 0) {
                                for(i in 0 until arrayListBranch!!.size) {
                                    if(arrayListBranch!![i].ID == BranchId) {
                                        BranchId = arrayListBranch!![i].ID!!
                                        Branch = arrayListBranch!![i].BranchName!!
                                        edtBranch.setText(Branch)
                                    }
                                }
                            }
                            if(mode == 2) {
                                selectBranchDialog()
                            }
                        } else {
                            toast("Branch Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                        toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<BranchListResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }
    /** AI005
     * This method is to open Branch dialog
     */
    private fun selectBranchDialog() {
        var dialogSelectBranch = Dialog(this)
        dialogSelectBranch.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectBranch.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectBranch.window!!.attributes)

        dialogSelectBranch.window!!.attributes = lp
        dialogSelectBranch.setCancelable(true)
        dialogSelectBranch.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectBranch.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectBranch.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectBranch.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectBranch.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogBranchAdapter(this, arrayListBranch!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                BranchId = arrayListBranch!![pos].ID!!
                Branch = arrayListBranch!![pos].BranchName!!
                edtBranch.setText(Branch)

                dialogSelectBranch!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectBranch!!.show()
    }

    //  endregion
}