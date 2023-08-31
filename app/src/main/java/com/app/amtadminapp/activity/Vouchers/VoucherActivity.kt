package com.app.amtadminapp.activity.Vouchers

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_general_info.imgBack
import kotlinx.android.synthetic.main.activity_voucher.*

class VoucherActivity : BaseActivity(),  View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voucher)
        initializeView()
    }

    override fun initializeView() {
        imgBack.setOnClickListener(this)
        RL_Hotel_Voucher.setOnClickListener(this)
        RL_Route_Voucher.setOnClickListener(this)
        RL_Airline_Voucher.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.RL_Hotel_Voucher -> {
                val intent = Intent(this@VoucherActivity, HotelVoucherActivity::class.java)
                startActivity(intent)
            }
            R.id.RL_Route_Voucher -> {
                val intent = Intent(this@VoucherActivity, RouteVoucherActivity::class.java)
                startActivity(intent)
            }
            R.id.RL_Airline_Voucher -> {
                val intent = Intent(this@VoucherActivity, AirlineVoucherActivity::class.java)
                startActivity(intent)

            }
        }
    }
}