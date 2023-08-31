package com.app.amtadminapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.amtadminapp.R
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_general_info.*
import kotlinx.android.synthetic.main.activity_general_info.imgBack
import kotlinx.android.synthetic.main.activity_general_info.profile_image
import kotlinx.android.synthetic.main.activity_general_info.tvUserName

class GeneralInformationActivity : BaseActivity(),  View.OnClickListener {

    var sharedPreference: SharedPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_info)
        initializeView()
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(this)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        val firstName = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_NAME)!!
        if(firstName != "") {
            txtFirstName.text = firstName
        }

        val UserName = sharedPreference?.getPreferenceString(PrefConstants.PREF_FullUSER_NAME)!!
        tvUserName.text = UserName

        val email = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_EMAIL)!!
        if(email != "") {
            txtEmial.text = email
        }

        val mobile = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_MOBILE)!!
        if(mobile != "") {
            txtMobileNo.text = mobile
        }

        val image = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_IMAGE)!!
        if(image != "" && image != null) {
            profile_image.loadUrlRoundedCorner(
                image,
                R.drawable.ic_profile,
                1
            )
        }

        imgBack.setOnClickListener(this)
        imgEdit.setOnClickListener(this)
        LLcardButtonLogout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgEdit -> {
                val intent = Intent(this@GeneralInformationActivity, EditProfileActivity::class.java)
                intent.putExtra("State","Update")
                startActivityForResult(intent, 1001)
            }
            R.id.LLcardButtonLogout -> {
                val sharedPreference = SharedPreference(this)
                sharedPreference.setPreference(PrefConstants.PREF_IS_LOGIN, "")
                sharedPreference.setPreference(PrefConstants.PREF_USER_ID, "")
                sharedPreference.setPreference(PrefConstants.PREF_USER_FIRST_NAME, "")
                sharedPreference.setPreference(PrefConstants.PREF_USER_LAST_NAME, "")
                sharedPreference.setPreference(PrefConstants.PREF_USER_IMAGE, "")
                sharedPreference.setPreference(PrefConstants.PREF_USER_MOBILE, "")
                sharedPreference.setPreference(PrefConstants.PREF_USER_EMAIL, "")
                sharedPreference.setPreference(PrefConstants.PREF_TOKEN, "")

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }
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

            }
        }
    }
}