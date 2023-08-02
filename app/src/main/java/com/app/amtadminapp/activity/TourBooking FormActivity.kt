package com.app.amtadminapp.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.adapter.BookingStepsAdapter
import com.app.amtadminapp.fragment.booking.*
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.BookingStepsModel
import com.app.amtadminapp.utils.AppConstant
import com.app.amtadminapp.utils.ScreenUtils
import com.app.amtadminapp.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_tour_booking_form.*

class TourBookingFormActivity : BaseActivity(), View.OnClickListener, RecyclerClickListener {

    var state : String = ""
    var tourbookingid = 0
    var customerid = 0
    var BookingNo = ""

    val arrayList = ArrayList<BookingStepsModel>()
    lateinit var adapter: BookingStepsAdapter

    var CURRENT_STEP_POSITION = 0

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
                tourbookingid = intent.getIntExtra("ID", 0)
                customerid = intent.getIntExtra("CustomerID", 0)
                BookingNo = intent.getStringExtra("TourBookingNo").toString()

                isAPICallFirst = true
                AppConstant.TOURBookingID = tourbookingid
                AppConstant.CustomerID = customerid
                AppConstant.BookingNo = BookingNo
            }
        }
    }

    private fun setStepsAdapter() {

        arrayList.add(BookingStepsModel("Tour Booking", true, false))
        arrayList.add(BookingStepsModel("Place Details", false, false))
        arrayList.add(BookingStepsModel("Tourist Info", false, false))
        arrayList.add(BookingStepsModel("PAX Info", false, false))
        arrayList.add(BookingStepsModel("Others", false, false))
        arrayList.add(BookingStepsModel("Remarks", false, false))

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
                replaceFragment(TourInfoFragment(state), R.id.container, TourInfoFragment::class.java.simpleName)
            }
            1 -> {
                replaceFragment(PlaceFragment(), R.id.container, PlaceFragment::class.java.simpleName)
            }
            2 -> {
                replaceFragment(PersonalInfoFragment(), R.id.container, PersonalInfoFragment::class.java.simpleName)
            }
            3 -> {
                replaceFragment(PaxInfoFragment(), R.id.container, PaxInfoFragment::class.java.simpleName)
            }
            4 -> {
                replaceFragment(OthersFragment(), R.id.container, OthersFragment::class.java.simpleName)
            }
            5 -> {
                replaceFragment(RemarksFragment(), R.id.container, RemarksFragment::class.java.simpleName)
            }
        }
    }

    private fun goToNextStep(position: Int) {
        when (position) {
            1 -> {
                replaceFragment(PlaceFragment(), R.id.container, PlaceFragment::class.java.simpleName)
            }
            2 -> {
                replaceFragment(PersonalInfoFragment(), R.id.container, PersonalInfoFragment::class.java.simpleName)
            }
            3 -> {
                replaceFragment(PaxInfoFragment(), R.id.container, PaxInfoFragment::class.java.simpleName)
            }
            4 -> {
                replaceFragment(OthersFragment(), R.id.container, OthersFragment::class.java.simpleName)
            }
            5 -> {
                replaceFragment(RemarksFragment(), R.id.container, RemarksFragment::class.java.simpleName)
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