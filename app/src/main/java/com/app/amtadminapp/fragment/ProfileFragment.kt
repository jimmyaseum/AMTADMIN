package com.app.amtadminapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.amtadminapp.Chatbot.ChatBoatActivity
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.*
import com.app.amtadminapp.activity.Vouchers.VoucherActivity
import com.app.amtadminapp.model.response.CommonResponse
import com.app.amtadminapp.model.response.RegistrationResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : BaseFragment(), View.OnClickListener {

    private var views: View? = null
    var sharedPreference: SharedPreference? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_profile, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {

        sharedPreference = SharedPreference(activity!!)

        mAuth = FirebaseAuth.getInstance()

        val UserName = sharedPreference?.getPreferenceString(PrefConstants.PREF_FullUSER_NAME)!!
        views!!.tvUserName.text = UserName
        val image = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_IMAGE)!!
        if(image != "" && image != null) {
            views!!.profile_image.loadUrlRoundedCorner(
                image,
                R.drawable.ic_profile,
                1
            )
        }

        views!!.LLEditProfile.setOnClickListener(this)
        views!!.LL_Logout.setOnClickListener(this)
        views!!.LL_General_Info.setOnClickListener(this)
        views!!.LL_My_Voucher.setOnClickListener(this)
        views!!.LL_Notification.setOnClickListener(this)
        views!!.LL_Chat.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        hideKeyboard(activity!!, v)
        when (v?.id) {
            R.id.LLEditProfile -> {
                val intent = Intent(activity, EditProfileActivity::class.java)
                intent.putExtra("State","Update")
                startActivityForResult(intent, 1001)
            }

            R.id.LL_My_Voucher -> {
                val intent = Intent(context, VoucherActivity::class.java)
                activity!!.startActivity(intent)
            }
            R.id.LL_Chat -> {
                val intent = Intent(activity!!, ChatBoatActivity::class.java)
                startActivity(intent)
            }
            R.id.LL_Logout -> {
                callLogoutAPI()
            }
            R.id.LL_General_Info -> {
                val intent = Intent(activity!!, GeneralInformationActivity::class.java)
                startActivity(intent)
            }
            R.id.LL_Notification -> {
                val intent = Intent(activity!!, NotificationListActivity::class.java)
                startActivity(intent)
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

    private fun callLogoutAPI() {

        showProgress()

        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        val jsonObject = JSONObject()
        jsonObject.put("ID", userId)

        val call = ApiUtils.apiInterface.logout(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.code == 200) {
                        hideProgress()

                        val sharedPreference = SharedPreference(activity!!)
                        sharedPreference.setPreference(PrefConstants.PREF_IS_LOGIN, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_ID, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_FIRST_NAME, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_LAST_NAME, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_IMAGE, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_MOBILE, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_EMAIL, "")
                        sharedPreference.setPreference(PrefConstants.PREF_TOKEN, "")

                        mAuth!!.signOut()

                        val intent = Intent(activity!!, LoginActivity::class.java)
                        activity!!.startActivity(intent)
                        activity!!.finishAffinity()

                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }
}