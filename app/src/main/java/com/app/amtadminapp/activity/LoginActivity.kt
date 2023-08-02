package com.app.amtadminapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.app.amtadminapp.R
import com.app.amtadminapp.model.response.LoginResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_get_invite.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_invite)
        initializeView()
    }

    override fun initializeView() {
        edtMobileNo.setOnEditorActionListener { v, actionId, event ->
            txt_error_mobile_no.gone()
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                SendOtp()
                true
            } else {
                false
            }
        }
    }

    private fun SendOtp() {
        hideKeyboard(applicationContext,edtMobileNo)
        val flag = isValidate()
        when (flag) {
            true -> {
                if (isConnectivityAvailable(this)) {

                    CallLoginAPI()
                } else {
                    toast(getString(R.string.str_msg_no_internet), AppConstant.TOAST_SHORT)
                }
            }
        }
    }

    private fun isValidate(): Boolean {
        var check = true
        if (edtMobileNo.text.isEmpty()) {
            txt_error_mobile_no.visible()
            check = false
        } else {
            if (edtMobileNo.text.length < 10) {
                txt_error_mobile_no.visible()
                txt_error_mobile_no.text = "Enter valid mobile number"
                check = false
            }
        }
        return check
    }

    private fun CallLoginAPI() {
        showProgress()
        val jsonObject = JSONObject()

        jsonObject.put("MobileNo", edtMobileNo.text.toString().trim())

        val call = ApiUtils.apiInterface?.login(getRequestJSONBody(jsonObject.toString()))

        call?.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.code == 200) {
                        hideProgress()
                        val arrayList = response.body()?.data!!
                        val intent = Intent(applicationContext, VerifyOtpActivity::class.java)
                        intent.putExtra("USER_MOBILE",edtMobileNo.text.toString().trim())
                        intent.putExtra("USER_ID",arrayList.ID)
                        startActivity(intent)
                    } else {
                        hideProgress()
                        toast(response.body()?.details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====" + t)
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }

}