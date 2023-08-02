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
import com.app.amtadminapp.activity.Reports.ReportView.UserWiseTourBookingReportView
import com.app.amtadminapp.adapter.dialog.*
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_user_wise_tour_booking_report.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class UserWiseTourBookingReportActivity : BaseActivity(), View.OnClickListener {

    /*Select Branch*/
    var arrayListBranch: ArrayList<BranchListModel>? = ArrayList()
    var BranchId : Int = 0
    var Branch = ""

    /*Select User*/
    var arrayListUser: ArrayList<SelectUserModel>? = ArrayList()
    var UserId : Int = 0
    var User = ""

    /* From Date and To Date */
    var calFromDate = Calendar.getInstance()
    var calToDate = Calendar.getInstance()
    var From_Date: String = ""
    var To_Date: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wise_tour_booking_report)
        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener(this)
        edtSelectBranch.setOnClickListener(this)
        edtSelectUser.setOnClickListener(this)
        edtFromDate.setOnClickListener(this)
        edtToDate.setOnClickListener(this)
        CardGetDetail.setOnClickListener(this)
        imgclearSB.setOnClickListener(this)
        imgclearSU.setOnClickListener(this)
        imgclearFD.setOnClickListener(this)
        imgclearTD.setOnClickListener(this)

        val profileName = intent.getStringExtra("Title")
        tbTvTitle.setText(profileName)
        GetAllBranchAPI(1)
        Log.d("Title==>", ""+profileName)
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgclearSB -> {
                clearSB()
            }
            R.id.imgclearSU -> {
                clearSU()
            }
            R.id.imgclearFD -> {
                clearFT()
            }
            R.id.imgclearTD -> {
                clearTD()
            }
            R.id.edtSelectBranch -> {
                preventTwoClick(v)
                if (!arrayListBranch.isNullOrEmpty()) {
                    selectBranchDialog()
                } else {
                    GetAllBranchAPI(2)
                }
            }
            R.id.edtSelectUser -> {
                preventTwoClick(v)
                if (!arrayListUser.isNullOrEmpty()) {
                    selectUserDialog()
                } else {
                    GetAllUserAPI(2)
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
                val intent = Intent(this, UserWiseTourBookingReportView::class.java)
                intent.putExtra("BranchID", BranchId)
                intent.putExtra("EmployeeID", UserId)
                intent.putExtra("FromDate", From_Date)
                intent.putExtra("ToDate", To_Date)
                startActivity(intent)
            }
        }
    }

    private fun clearSB() {
        BranchId = 0
        edtSelectBranch.setText("")
    }

    private fun clearSU() {
        UserId = 0
        edtSelectUser.setText("")
    }

    private fun clearFT() {
        From_Date = ""
        edtFromDate.setText("")
    }

    private fun clearTD() {
        To_Date = ""
        edtToDate.setText("")
    }

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
                                        edtSelectBranch.setText(Branch)
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

    private fun GetAllUserAPI(mode: Int) {

        showProgress()

        val jsonObject = JSONObject()
        jsonObject.put("BranchID", BranchId)

        val call = ApiUtils.apiInterface.getSelectUser(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<SelectUserResponse> {
            override fun onResponse(call: Call<SelectUserResponse>, response: Response<SelectUserResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.Status == 200) {
                        hideProgress()

                        if(!arrayListUser.isNullOrEmpty()) {
                            arrayListUser!!.clear()
                        }

                        arrayListUser = response.body()?.Data!!
                        if(arrayListUser!!.size > 0) {

                            if(UserId != 0) {
                                for(i in 0 until arrayListUser!!.size) {
                                    if(arrayListUser!![i].EmployeeID == UserId) {
                                        UserId = arrayListUser!![i].EmployeeID!!
                                        User = arrayListUser!![i].EmployeeName!!
                                        edtSelectUser.setText(User)
                                    }
                                }
                            }

                            if(mode == 2) {
                                selectUserDialog()
                            }
                        } else {
                            toast("Tour/Package Not Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                    }
                }
            }

            override fun onFailure(call: Call<SelectUserResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }

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
                edtSelectBranch.setText(Branch)

                if(BranchId != 0) {
                    GetAllUserAPI(1)
                }

                dialogSelectBranch!!.dismiss()

            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectBranch!!.show()
    }

    private fun selectUserDialog() {
        var dialogSelectUser = Dialog(this)
        dialogSelectUser.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectUser.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectUser.window!!.attributes)

        dialogSelectUser.window!!.attributes = lp
        dialogSelectUser.setCancelable(true)
        dialogSelectUser.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectUser.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectUser.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectUser.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectUser.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogSelectUserAdapter(this, arrayListUser!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                UserId = arrayListUser!![pos].EmployeeID!!
                User = arrayListUser!![pos].EmployeeName!!
                edtSelectUser.setText(User)
                dialogSelectUser!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrItemsFinal1: ArrayList<SelectUserModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrayListUser!!) {
                        if (model.EmployeeName!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogSelectUserAdapter(this@UserWiseTourBookingReportActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            UserId = arrItemsFinal1!![pos].EmployeeID!!
                            User = arrItemsFinal1!![pos].EmployeeName!!
                            edtSelectUser.setText(User)
                            dialogSelectUser!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogSelectUserAdapter(this@UserWiseTourBookingReportActivity, arrayListUser!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            UserId = arrayListUser!![pos].EmployeeID!!
                            User = arrayListUser!![pos].EmployeeName!!
                            edtSelectUser.setText(User)
                            dialogSelectUser!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectUser!!.show()
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