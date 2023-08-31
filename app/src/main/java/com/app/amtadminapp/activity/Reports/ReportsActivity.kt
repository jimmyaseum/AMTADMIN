package com.app.amtadminapp.activity.Reports

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.activity.Reports.ReportActivity.*
import com.app.amtadminapp.activity.Reports.ReportView.SelfPendingPaymentReportViewActivity
import com.app.amtadminapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_general_info.imgBack
import kotlinx.android.synthetic.main.activity_reports.*

class ReportsActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        initializeView()
    }

    override fun initializeView() {
        imgBack.setOnClickListener(this)
        RL_Hotel_Block_Report.setOnClickListener(this)
        RL_Pending_Payment_Report.setOnClickListener(this)
        RL_Sector_With_Payment_Report.setOnClickListener(this)
        RL_Sector_Without_Payment_Report.setOnClickListener(this)
        RL_Company_Wise_Booking_Report.setOnClickListener(this)
        RL_Tour_Wise_Pending_Payment_Report.setOnClickListener(this)
        RL_User_Wise_Tour_Booking_Report.setOnClickListener(this)
        RL_Hotel_Report.setOnClickListener(this)
        RL_Hotel_Report_2022.setOnClickListener(this)
        RL_Confirmed_Booking_Report_2023.setOnClickListener(this)
        RL_Confirmed_Hotel_Voucher_Report_2023.setOnClickListener(this)
        RL_Hotel_Report_Place_Wise.setOnClickListener(this)
        RL_Hotel_Summary_Report_Place_Wise.setOnClickListener(this)
        RL_Route_Report.setOnClickListener(this)
        RL_Route_Report_By_TransPorter.setOnClickListener(this)
        RL_Pending_Hotel_Voucher_Report.setOnClickListener(this)
        RL_Pending_Route_Voucher_Report.setOnClickListener(this)
        RL_Branch_Wise_Tour_Booking_Summary.setOnClickListener(this)
        RL_User_Wise_Tour_Booking_Summary.setOnClickListener(this)
        RL_Vehicle_Allotment.setOnClickListener(this)
        RL_Self_Tour_Booking_Report.setOnClickListener(this)
        RL_Self_Pending_Payment_Report.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.RL_Hotel_Block_Report -> {
                val intent = Intent(this, HotelBlockReportActivity::class.java)
                intent.putExtra("Title", TV_Hotel_Block_Report.text)
                startActivity(intent)
            }
            R.id.RL_Pending_Payment_Report -> {
                val intent = Intent(this, PendingPaymentReportActivity::class.java)
                intent.putExtra("Title", TV_Pending_Payment_Report.text)
                startActivity(intent)
            }
            R.id.RL_Sector_With_Payment_Report -> {
                val intent = Intent(this, HotelBlockReportActivity::class.java)
                intent.putExtra("Title", TV_Sector_With_Payment_Report.text)
                startActivity(intent)
            }
            R.id.RL_Sector_Without_Payment_Report -> {
                val intent = Intent(this, HotelBlockReportActivity::class.java)
                intent.putExtra("Title", TV_Sector_Without_Payment_Report.text)
                startActivity(intent)
            }
            R.id.RL_Company_Wise_Booking_Report -> {
                val intent = Intent(this, HotelBlockReportActivity::class.java)
                intent.putExtra("Title", TV_Company_Wise_Booking_Report.text)
                startActivity(intent)
            }
            R.id.RL_Tour_Wise_Pending_Payment_Report -> {
                val intent = Intent(this, HotelBlockReportActivity::class.java)
                intent.putExtra("Title", TV_Tour_Wise_Pending_Payment_Report.text)
                startActivity(intent)
            }
            R.id.RL_User_Wise_Tour_Booking_Report -> {
                val intent = Intent(this, UserWiseTourBookingReportActivity::class.java)
                intent.putExtra("Title", TV_User_Wise_Tour_Booking_Report.text)
                startActivity(intent)
            }
            R.id.RL_Hotel_Report -> {
                val intent = Intent(this, HotelBlockReportActivity::class.java)
                intent.putExtra("Title", TV_Hotel_Report.text)
                startActivity(intent)
            }
            R.id.RL_Hotel_Report_2022 -> {
                val intent = Intent(this, HotelBlockReportActivity::class.java)
                intent.putExtra("Title", TV_Hotel_Report_2022.text)
                startActivity(intent)
            }
            R.id.RL_Confirmed_Booking_Report_2023 -> {
                val intent = Intent(this, ConfirmedBookingReportActivity::class.java)
                intent.putExtra("Title", TV_Confirmed_Booking_Report_2023.text)
                startActivity(intent)
            }
            R.id.RL_Confirmed_Hotel_Voucher_Report_2023 -> {
                val intent = Intent(this, ConfirmedHotelVoucherReportActivity::class.java)
                intent.putExtra("Title", TV_Confirmed_Hotel_Voucher_Report_2023.text)
                startActivity(intent)
            }
            R.id.RL_Hotel_Report_Place_Wise -> {
                val intent = Intent(this, HotelBlockReportActivity::class.java)
                intent.putExtra("Title", TV_Hotel_Report_Place_Wise.text)
                startActivity(intent)
            }
            R.id.RL_Hotel_Summary_Report_Place_Wise -> {
                val intent = Intent(this, HotelBlockReportActivity::class.java)
                intent.putExtra("Title", TV_Hotel_Summary_Report_Place_Wise.text)
                startActivity(intent)
            }
            R.id.RL_Route_Report -> {
                val intent = Intent(this, HotelBlockReportActivity::class.java)
                intent.putExtra("Title", TV_Route_Report.text)
                startActivity(intent)
            }
            R.id.RL_Route_Report_By_TransPorter -> {
                val intent = Intent(this, HotelBlockReportActivity::class.java)
                intent.putExtra("Title", TV_Route_Report_By_TransPorter.text)
                startActivity(intent)
            }
            R.id.RL_Pending_Hotel_Voucher_Report -> {
                val intent = Intent(this, PendingHotelVoucherReportActivity::class.java)
                intent.putExtra("Title", TV_Pending_Hotel_Voucher_Report.text)
                startActivity(intent)
            }
            R.id.RL_Pending_Route_Voucher_Report -> {
                val intent = Intent(this, PendingRouteVoucherReportActivity::class.java)
                intent.putExtra("Title", TV_Pending_Route_Voucher_Report.text)
                startActivity(intent)
            }
            R.id.RL_Branch_Wise_Tour_Booking_Summary -> {
                val intent = Intent(this, BranchWiseTourBookingSummaryReportActivity::class.java)
                intent.putExtra("Title", TV_Branch_Wise_Tour_Booking_Summary.text)
                startActivity(intent)
            }
            R.id.RL_User_Wise_Tour_Booking_Summary -> {
                val intent = Intent(this, HotelBlockReportActivity::class.java)
                intent.putExtra("Title", TV_User_Wise_Tour_Booking_Summary.text)
                startActivity(intent)
            }
            R.id.RL_Vehicle_Allotment -> {
                val intent = Intent(this, VehicleAllotmentReportActivity::class.java)
                intent.putExtra("Title", TV_Vehicle_Allotment.text)
                startActivity(intent)
            }
            R.id.RL_Self_Tour_Booking_Report -> {
                val intent = Intent(this, SelfTourBookingReportActivity::class.java)
                intent.putExtra("Title", TV_Self_Tour_Booking_Report.text)
                startActivity(intent)
            }
            R.id.RL_Self_Pending_Payment_Report -> {
                val intent = Intent(this, SelfPendingPaymentReportViewActivity::class.java)
                intent.putExtra("Title", TV_Self_Pending_Payment_Report.text)
                startActivity(intent)
            }
        }
    }
}