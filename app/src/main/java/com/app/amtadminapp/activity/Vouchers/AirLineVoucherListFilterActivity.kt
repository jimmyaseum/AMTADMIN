package com.app.amtadminapp.activity.Vouchers

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
import com.app.amtadminapp.adapter.dialog.DialogInitialAdapter
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.BranchListModel
import com.app.amtadminapp.model.response.BranchListResponse
import com.app.amtadminapp.model.response.EmployeeListModel
import com.app.amtadminapp.model.response.EmployeeListResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.*
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.edtAirlineVoucherNo
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.edtBookBy
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.edtBranch
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.edtJourney
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.edtNoOfPax
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.edtTicketPurchaseDate
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.edtTotalPrice
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.edtTourBookingNo
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.tvApply
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.tvClearAll
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.tvClose
import kotlinx.android.synthetic.main.activity_airline_voucher_list_filter.tvFilters
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AirLineVoucherListFilterActivity  : BaseActivity(), View.OnClickListener {

    var calDate = Calendar.getInstance()

    var AirlineVoucherNo = ""
    var TourBookingNo = ""
    var Name = ""
    var PNR = ""
    var TicketPurchased = ""
    var Journey = ""
    var NoOfPax = ""
    var TotalPrice = ""
    var Status = ""
    var BookBy = ""
    var Branch = ""

    var arrayListEmployee: ArrayList<EmployeeListModel>? = ArrayList()
    var BookById : Int = 0

    var arrayListBranch: ArrayList<BranchListModel>? = ArrayList()
    var BranchId : Int = 0

    private val arrJourneyList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airline_voucher_list_filter)
        getIntentData()
        initializeView()
    }

    //Set Intent data
    private fun getIntentData() {

        if (intent.hasExtra("AirlineVoucherNo")) {
            AirlineVoucherNo = intent.getStringExtra("AirlineVoucherNo").toString()
            edtAirlineVoucherNo.setText(AirlineVoucherNo)
        }
        if (intent.hasExtra("TourBookingNo")) {
            TourBookingNo = intent.getStringExtra("TourBookingNo").toString()
            edtTourBookingNo.setText(TourBookingNo)
        }
        if (intent.hasExtra("Name")) {
            Name = intent.getStringExtra("Name").toString()
            edtName.setText(Name)
        }
        if (intent.hasExtra("PNR")) {
            PNR = intent.getStringExtra("PNR").toString()
            edtPNR.setText(PNR)
        }
        if (intent.hasExtra("TicketPurchased")) {
            TicketPurchased = intent.getStringExtra("TicketPurchased").toString()
            edtTicketPurchaseDate.setText(TicketPurchased)
        }
        if (intent.hasExtra("Journey")) {
            Journey = intent.getStringExtra("Journey").toString()
            edtJourney.setText(Journey)
        }
        if (intent.hasExtra("NoOfPax")) {
            NoOfPax = intent.getStringExtra("NoOfPax").toString()
            edtNoOfPax.setText(NoOfPax)
        }
        if (intent.hasExtra("TotalPrice")) {
            TotalPrice = intent.getStringExtra("TotalPrice").toString()
            edtTotalPrice.setText(TotalPrice)
        }
        if (intent.hasExtra("Status")) {
            Status = intent.getStringExtra("Status").toString()
            edtStatus.setText(Status)
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

        arrJourneyList.add("ONEWAY")
        arrJourneyList.add("TWOWAY")
        arrJourneyList.add("MULTICITY")

        edtJourney.setOnClickListener(this)
        edtBookBy.setOnClickListener(this)
        edtBranch.setOnClickListener(this)

        tvClearAll.setOnClickListener(this)
        tvFilters.setOnClickListener(this)
        tvClose.setOnClickListener(this)
        tvApply.setOnClickListener(this)
        edtTicketPurchaseDate.setOnClickListener(this)

        GetAllEmployeeAPI(1)
        GetAllBranchAPI(1)

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
            R.id.edtTicketPurchaseDate -> {
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
                dpd.show()
            }
            R.id.edtJourney -> {
                selectJourneyDialog()
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
        TicketPurchased = SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time)
        edtTicketPurchaseDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
    }

    private fun clearAdapterData() {
         AirlineVoucherNo = ""
         TourBookingNo = ""
         Name = ""
         PNR = ""
         TicketPurchased = ""
         Journey = ""
         NoOfPax = ""
         TotalPrice = ""
         Status = ""
         BookBy = ""
         Branch = ""

        edtAirlineVoucherNo.setText("")
        edtTourBookingNo.setText("")
        edtName.setText("")
        edtPNR.setText("")
        edtTicketPurchaseDate.setText("")
        edtJourney.setText("")
        edtNoOfPax.setText("")
        edtTotalPrice.setText("")
        edtStatus.setText("")
        edtBookBy.setText("")
        edtBranch.setText("")
    }

    private fun applyFiltersData() {
        val intent = Intent()
        intent.putExtra("AirlineVoucherNo", edtAirlineVoucherNo.text.toString())
        intent.putExtra("TourBookingNo", edtTourBookingNo.text.toString())
        intent.putExtra("TicketPurchased", TicketPurchased)
        intent.putExtra("Name", edtName.text.toString())
        intent.putExtra("PNR", edtPNR.text.toString())
        intent.putExtra("Journey", edtJourney.text.toString())
        intent.putExtra("NoOfPax", edtNoOfPax.text.toString())
        intent.putExtra("TotalPrice", edtTotalPrice.text.toString())
        intent.putExtra("Status", edtStatus.text.toString())
        intent.putExtra("BookByID", BookById)
        intent.putExtra("BookBy", edtBookBy.text.toString())
        intent.putExtra("BranchID",BranchId)
        intent.putExtra("Branch", edtBranch.text.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    // region Journey

    /** AI005
     * This method is to open Travel Type dialog
     */
    private fun selectJourneyDialog() {
        var dialogSelectJourney = Dialog(this)
        dialogSelectJourney.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectJourney.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectJourney.window!!.attributes)

        dialogSelectJourney.window!!.attributes = lp
        dialogSelectJourney.setCancelable(true)
        dialogSelectJourney.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectJourney.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectJourney.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectJourney.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectJourney.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogInitialAdapter(this, arrJourneyList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                Journey = arrJourneyList!![pos]
                edtJourney.setText(Journey)
                dialogSelectJourney!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectJourney!!.show()
    }
    //endregion

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

                    val itemAdapter = DialogEmployeeAdapter(this@AirLineVoucherListFilterActivity, arrItemsFinal1)
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
                    val itemAdapter = DialogEmployeeAdapter(this@AirLineVoucherListFilterActivity, arrayListEmployee!!)
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