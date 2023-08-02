package com.app.amtadminapp.activity.Vouchers.hotel

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.adapter.BookingStepsAdapter
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.BookingStepsModel
import com.app.amtadminapp.model.response.MultiTourBookingViewModel
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_tour_booking_form.*

class AddHotelVoucherActivity : BaseActivity(), View.OnClickListener, RecyclerClickListener {

    var state : String = ""

    val arrayList = ArrayList<BookingStepsModel>()
    lateinit var adapter: BookingStepsAdapter

    var CURRENT_STEP_POSITION = 0
    var sharedPreference: SharedPreference? = null

    var arrayListInquiry: MutableList<MultiTourBookingViewModel>? = null

    var TourBookingNos = ""
    var HotelVoucherIDs = 0

    var isAPICallFirst: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_booking_form)
        getIntentData()
        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener {
            finish()
        }

        rvOrderSteps.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        setStepsAdapter()
        selectedSteps(CURRENT_STEP_POSITION)
    }

    private fun getIntentData() {
        if (intent.hasExtra("state")) {
            state = intent.getStringExtra("state").toString()
            if(state.equals("edit")) {
                TourBookingNos = intent.getStringExtra("TourBookingNos").toString()
                HotelVoucherIDs = intent.getIntExtra("HotelVoucherIDs", 0)
                isAPICallFirst = true
                AppConstant.TOURBookingNO = TourBookingNos
                AppConstant.HotelVoucherIDs = HotelVoucherIDs
            }
        }
    }

    private fun setStepsAdapter() {

        arrayList.add(BookingStepsModel("Tour Details", true, false))
        arrayList.add(BookingStepsModel("Hotel Details", false, false))
        arrayList.add(BookingStepsModel("Pax Details", false, false))

        adapter = BookingStepsAdapter( this, arrayList)
        rvOrderSteps.adapter = adapter
        adapter.setRecyclerRowClick(this)
    }

    private fun goToPreviousStep(position: Int) {
        selectedSteps(position)
    }

    //Set selection steps
    private fun selectedSteps(position: Int) {
        when (position) {
            0 -> {
                replaceFragment(TourDetailsFragment(state), R.id.container, TourDetailsFragment::class.java.simpleName)
            }
            1 -> {
                replaceFragment(HotelDetailsFragment(state), R.id.container, HotelDetailsFragment::class.java.simpleName)
            }
            2 -> {
//                replaceFragment(PaxDetailsFragment(), R.id.container, PaxDetailsFragment::class.java.simpleName)
            }
        }
    }

    private fun goToNextStep(position: Int) {
        when (position) {
            1 -> {
                replaceFragment(HotelDetailsFragment(state), R.id.container, HotelDetailsFragment::class.java.simpleName)
            }
            2 -> {
//                replaceFragment(PaxDetailsFragment(), R.id.container, PaxDetailsFragment::class.java.simpleName)
            }
        }
    }

    //Update Steps color from Fragment
    fun updateStepsColor(position: Int) {
        adapter.updateStepsColor(position)
        setCenterRecyclerViewItem(position)
    }

    override fun onClick(p0: View?) {
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        when (view.id) {
            R.id.llStepNumber -> {
                if (position > CURRENT_STEP_POSITION) {
                    if(CURRENT_STEP_POSITION == 0) {
                        if(isAPICallFirst) {
                            updateStepsColor(position)
                            goToNextStep(position)
                            CURRENT_STEP_POSITION = position
                        }
                    } else {
                        updateStepsColor(position)
                        goToNextStep(position)
                        CURRENT_STEP_POSITION = position
                    }
                } else if (position < CURRENT_STEP_POSITION) {
                    updateStepsColor(position)
                    goToPreviousStep(position)
                    CURRENT_STEP_POSITION = position
                }

            }
        }

    }

    //Set RecyclerView as center item
    private fun setCenterRecyclerViewItem(position: Int) {
        (rvOrderSteps.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
            position,
            ScreenUtils.getScreenWidth(this) * 33 / 100
        )
    }

    private fun fragmentChange(fragment: Fragment, id: Int) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment, "MY_FRAGMENT").commit()
    }




}