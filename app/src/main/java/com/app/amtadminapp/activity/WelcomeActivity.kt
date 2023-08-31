package com.app.amtadminapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.app.amtadminapp.R
import com.app.amtadminapp.adapter.WelcomePagerAdapter
import com.app.amtadminapp.utils.PrefConstants
import com.app.amtadminapp.utils.SharedPreference
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.visible
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity(), View.OnClickListener {

    var current_position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        initializeView()
    }

    override fun initializeView() {

        val adapter = WelcomePagerAdapter(this)
        viewPager!!.adapter = adapter

        txtSkip.setOnClickListener(this)
        txtNext.setOnClickListener(this)
        LLcardButtonNext.setOnClickListener(this)
        //Set Page Indicator
        pageIndicator.setViewPager(viewPager)

        val density = resources.displayMetrics.density
        //Set circle indicator radius
        pageIndicator.radius = 6 * density

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                current_position = position
                //Last position - right now 4 pages if add or remove then need to change accordingly
                if (position == 2) {
                    LLIndication.gone()
                    LLcardButtonNext.visible()
                } else {
                    LLIndication.visible()
                    LLcardButtonNext.gone()
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txtSkip -> {
                val sharedPreference = SharedPreference(this)
                sharedPreference.setPreference(PrefConstants.PREF_IS_WELCOME, "1")

                val intent = Intent(applicationContext, StartActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.txtNext -> {
                viewPager.currentItem = current_position + 1

            }
            R.id.LLcardButtonNext -> {
                val sharedPreference = SharedPreference(this)
                sharedPreference.setPreference(PrefConstants.PREF_IS_WELCOME, "1")

                val intent = Intent(applicationContext, StartActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
