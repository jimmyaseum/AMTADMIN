package com.app.amtadminapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.amtadminapp.R
import com.app.amtadminapp.fragment.ExploreFragment
import com.app.amtadminapp.fragment.ProfileFragment
import com.app.amtadminapp.fragment.booking.BookingListFragment
import com.app.amtadminapp.utils.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener  {

    var sharedPreference: SharedPreference? = null

    //Booking - Filter
    var TourBookingNo = ""
    var Name = ""
    var MobileNo = ""
    var SectorID = 0
    var Sector = ""
    var TravelType = ""
    var TourID = 0
    var Tour = ""
    var TourDateCode = ""
    var NoOfNights = ""
    var GroupBooking = ""
    var Status = ""
    var BookByID = 0
    var BookBy = ""
    var BranchID = 0
    var Branch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initializeView()
    }

    override fun initializeView() {

        setToolBar()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar1,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = false
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        initBottomNavigation()
        bottomNavigationListener()

        val headerLayout = navigationView.getHeaderView(0)

        val LLEditProfile: LinearLayout = headerLayout.findViewById(R.id.LLEditProfile)
        LLEditProfile.setOnClickListener(this)

        //Default Home Fragment called first time
        showHomeFragment()

        // Call Find By Id and Set Data
        sharedPreference = SharedPreference(this)

        val UserName = sharedPreference!!.getPreferenceString(PrefConstants.PREF_FullUSER_NAME)!!.toString()
        headerLayout.tvUserName1.text = UserName

        val image = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_IMAGE)!!
        if(image != "" && image != null) {
            headerLayout.profile_image.loadUrlRoundedCorner(
                image,
                R.drawable.ic_profile,
                1
            )
        }

    }

    private fun setToolBar() {
        setSupportActionBar(toolbar1)
        toolbar1.tbTvTitle!!.text = "Dashboard"
        toolbar1?.tbImgLeft!!.setImageResource(R.drawable.ic_menu)
        toolbar1?.tbImgLeft!!.setOnClickListener(this)
        toolbar1?.tbImgRight!!.setOnClickListener(this)
    }

    private fun openCloseDrawerLayout() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.tbImgLeft -> {
//                openCloseDrawerLayout()
            }
            R.id.tbImgRight -> {

                    //Only Filter icon click from BookingListFragment
                    val fragment = supportFragmentManager.findFragmentByTag(BookingListFragment::class.java.simpleName)
                    if (fragment != null && fragment.isVisible) {
                        val intent = Intent(this, TourBookingFilterActivity::class.java)
                        intent.putExtra("TourBookingNo", TourBookingNo)
                        intent.putExtra("Name", Name)
                        intent.putExtra("MobileNo", MobileNo)
                        intent.putExtra("Sector", Sector)
                        intent.putExtra("SectorID", SectorID)
                        intent.putExtra("TravelType", TravelType)
                        intent.putExtra("Tour", Tour)
                        intent.putExtra("TourID", TourID)
                        intent.putExtra("TourDateCode", TourDateCode)
                        intent.putExtra("GroupBooking", GroupBooking)
                        intent.putExtra("NoOfNights", NoOfNights)
                        intent.putExtra("Status", Status)
                        intent.putExtra("BookByID", BookByID)
                        intent.putExtra("BookBy", BookBy)
                        intent.putExtra("BranchID", BranchID)
                        intent.putExtra("Branch", Branch)
                        startActivityForResult(intent, 1003)
                    }
            }
            R.id.LLEditProfile -> {
                val intent = Intent(this, EditProfileActivity::class.java)
                intent.putExtra("State","Update")
                startActivityForResult(intent, 1001)
            }
        }
    }

    private fun showHomeFragment() {

        toolbar1.tbTvTitle!!.text = "Dashboard"
        replaceFragment(ExploreFragment(), R.id.container, ExploreFragment::class.java.simpleName)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.menu.findItem(R.id.nav_Couple_Tours).isChecked = true
        showHideToolBarRightImage(false, 0)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        showHideToolBarRightImage(false, R.drawable.ic_filter_24dp)

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_Couple_Tours -> {
                toolbar1.tbTvTitle!!.text = resources.getString(R.string.str_couple_tour)
            }
            R.id.nav_Diwali_Vacation -> {
                toolbar1.tbTvTitle!!.text = resources.getString(R.string.str_diwali_vacation)
            }
            R.id.nav_Customized_Holidays -> {
                toolbar1.tbTvTitle!!.text = resources.getString(R.string.str_customized_holidays)
            }
            R.id.nav_Weekend_Gateways -> {
                toolbar1.tbTvTitle!!.text = resources.getString(R.string.str_weekend_gateways)
            }
            R.id.nav_Contact -> {
                toolbar1.tbTvTitle!!.text = resources.getString(R.string.str_contact)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showHideToolBarRightImage(isShow: Boolean, resourceId: Int) {

        if (isShow) {
            toolbar1.tbImgRight.visible()
            tbImgRight.setImageResource(resourceId)
        } else {
            toolbar1.tbImgRight.invisible()
        }
    }

    fun initBottomNavigation() {
        bottomNavigation.menu.clear()
        bottomNavigation.inflateMenu(R.menu.bottom_nav_menu)
    }

    private fun bottomNavigationListener() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_Explore -> {
                    showHomeFragment()
                    resetFilter()
                }
                R.id.nav_Bookings -> {
                    toolbar1.tbTvTitle!!.text = "Bookings"
                    replaceFragment(BookingListFragment(), R.id.container, BookingListFragment::class.java.simpleName)
                    showHideToolBarRightImage(true, R.drawable.ic_filter)
                    resetFilter()
                }

                R.id.nav_Profile -> {
                    toolbar1.tbTvTitle!!.text = "Profile"
                    replaceFragment(ProfileFragment(), R.id.container, ProfileFragment::class.java.simpleName)
                    showHideToolBarRightImage(false, 0)
                    resetFilter()
                }
            }
            true
        }
    }

    private fun resetFilter() {
        TourBookingNo = ""
        Name = ""
        MobileNo = ""
        SectorID = 0
        Sector = ""
        TravelType = ""
        TourID = 0
        Tour = ""
        TourDateCode = ""
        GroupBooking = ""
        NoOfNights = ""
        Status = ""
        BookByID = 0
        BookBy = ""
        BranchID = 0
        Branch = ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1001 -> {
                    val isCallAPI = data!!.getBooleanExtra(AppConstant.IS_API_CALL, false)
                    if (isCallAPI) {
                        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
//                        callCustomerDetailApi(userId)
                    }
                }
                1003 -> {
                    TourBookingNo = data!!.getStringExtra("TourBookingNo").toString()
                    Name = data!!.getStringExtra("Name").toString()
                    MobileNo = data!!.getStringExtra("MobileNo").toString()
                    SectorID = data!!.getIntExtra("SectorID",0)
                    Sector = data!!.getStringExtra("Sector").toString()
                    TravelType = data!!.getStringExtra("TravelType").toString()
                    TourID = data!!.getIntExtra("TourID",0)
                    Tour = data!!.getStringExtra("Tour").toString()
                    TourDateCode = data!!.getStringExtra("TourDateCode").toString()
                    GroupBooking = data!!.getStringExtra("GroupBooking").toString()
                    NoOfNights = data!!.getStringExtra("NoOfNights").toString()
                    Status = data!!.getStringExtra("Status").toString()
                    BookByID = data!!.getIntExtra("BookByID",0)
                    BookBy = data!!.getStringExtra("BookBy").toString()
                    BranchID = data!!.getIntExtra("BranchID",0)
                    Branch = data!!.getStringExtra("Branch").toString()

                    //Pass above data to ContactListFragment
                    for (fragment in supportFragmentManager.fragments) {
                        fragment.onActivityResult(requestCode, resultCode, data)
                    }
                }
            }
        }
    }
}


