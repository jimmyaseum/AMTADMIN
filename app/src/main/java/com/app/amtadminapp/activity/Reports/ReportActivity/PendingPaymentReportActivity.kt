package com.app.amtadminapp.activity.Reports.ReportActivity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.app.amtadminapp.activity.Reports.ReportView.PendingPaymentReportViewActivity
import com.app.amtadminapp.adapter.dialog.DialogInitialAdapter
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.utils.AppConstant
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.preventTwoClick
import kotlinx.android.synthetic.main.activity_confirmed_booking_report.*
import kotlinx.android.synthetic.main.activity_pending_payment_report.*
import kotlinx.android.synthetic.main.activity_pending_payment_report.CardGetDetail
import kotlinx.android.synthetic.main.activity_pending_payment_report.edtDateType
import kotlinx.android.synthetic.main.activity_pending_payment_report.edtFromDate
import kotlinx.android.synthetic.main.activity_pending_payment_report.edtToDate
import kotlinx.android.synthetic.main.activity_pending_payment_report.edtTravelType
import kotlinx.android.synthetic.main.activity_pending_payment_report.imgBack
import kotlinx.android.synthetic.main.activity_pending_payment_report.imgclearDT
import kotlinx.android.synthetic.main.activity_pending_payment_report.imgclearFD
import kotlinx.android.synthetic.main.activity_pending_payment_report.imgclearTD
import kotlinx.android.synthetic.main.activity_pending_payment_report.imgclearTT
import kotlinx.android.synthetic.main.activity_pending_payment_report.tbTvTitle
import java.text.SimpleDateFormat
import java.util.*

class PendingPaymentReportActivity : BaseActivity(), View.OnClickListener {

    private val arrTravelTypeList: ArrayList<String> = ArrayList()
    var TravelTypeName : String = ""

    private val arrDateTypeList: ArrayList<String> = ArrayList()
    var DateTypeName : String = ""

    /* From Date and To Date */
    var calFromDate = Calendar.getInstance()
    var calToDate = Calendar.getInstance()
    var From_Date: String = ""
    var To_Date: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_payment_report)
        initializeView()
    }

    override fun initializeView() {
        arrTravelTypeList.add("TOUR")
        arrTravelTypeList.add("PACKAGE")
        arrTravelTypeList.add("ALL")

        arrDateTypeList.add("Booking Date")
        arrDateTypeList.add("Travel Date")

        imgBack.setOnClickListener(this)
        edtTravelType.setOnClickListener(this)
        edtDateType.setOnClickListener(this)
        edtFromDate.setOnClickListener(this)
        edtToDate.setOnClickListener(this)
        CardGetDetail.setOnClickListener(this)
        imgclearDT.setOnClickListener(this)
        imgclearTT.setOnClickListener(this)
        imgclearFD.setOnClickListener(this)
        imgclearTD.setOnClickListener(this)

        val profileName = intent.getStringExtra("Title")
        tbTvTitle.setText(profileName)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgclearDT -> {
                clearDT()
            }
            R.id.imgclearTT -> {
                clearTT()
            }
            R.id.imgclearFD -> {
                clearFT()
            }
            R.id.imgclearTD -> {
                clearTD()
            }
            R.id.edtTravelType -> {
                selectTravelTypeDialog()
            }
            R.id.edtDateType -> {
                selectDateTypeDialog()
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
                val intent = Intent(this, PendingPaymentReportViewActivity::class.java)

                Log.e("PUT DATA 1 ","===>"+edtTravelType.text.toString())
                Log.e("PUT DATA 1 ","===>"+edtDateType.text.toString())
                intent.putExtra("Date", edtDateType.text.toString())
                intent.putExtra("Travel", edtTravelType.text.toString())
                intent.putExtra("FromDate", edtFromDate.text.toString())
                intent.putExtra("ToDate", edtToDate.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun clearDT() {
        DateTypeName = ""
        edtDateType.setText("")
    }

    private fun clearTT() {
        edtTravelType.setText("")
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

    private fun selectDateTypeDialog() {
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

        val itemAdapter = DialogInitialAdapter(this, arrDateTypeList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                DateTypeName = arrDateTypeList!![pos]
                edtDateType.setText(DateTypeName)
                dialogSelectTravelType!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectTravelType!!.show()
    }
}