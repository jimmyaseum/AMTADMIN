package com.app.amtadminapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.app.amtadminapp.R
import com.app.amtadminapp.model.response.RegistrationResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.edtMobileNo
import kotlinx.android.synthetic.main.activity_registration.txt_error_mobile_no
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        initializeView()
    }

    override fun initializeView() {
        txtLogin.setOnClickListener(this)
        LLcardButtonSignUp.setOnClickListener(this)

        edtEmail.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                val flag = isValidate()
                when (flag) {
                    true -> {
                        if (isConnectivityAvailable(this)) {
                            callRegisterAPI()
                        } else {
                            toast(getString(R.string.str_msg_no_internet), AppConstant.TOAST_SHORT)
                        }
                    }
                }
                true
            } else {
                false
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.LLcardButtonSignUp -> {
                val flag = isValidate()
                when (flag) {
                    true -> {
                        if (isConnectivityAvailable(this)) {
                            callRegisterAPI()
                        } else {
                            toast(getString(R.string.str_msg_no_internet), AppConstant.TOAST_SHORT)
                        }
                    }
                }
            }
            R.id.txtLogin -> {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun callRegisterAPI() {

        showProgress()
        val jsonObject = JSONObject()

        jsonObject.put("FirstName", edtFirstName.text.toString().trim())
        jsonObject.put("LastName", edtLastName.text.toString().trim())
        jsonObject.put("MobileNo", edtMobileNo.text.toString().trim())
        jsonObject.put("EmailID", edtEmail.text.toString().trim())

        val call = ApiUtils.apiInterface.registration(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.code == 200) {
                        hideProgress()
                        val intent = Intent(applicationContext, VerifyOtpActivity::class.java)
                        intent.putExtra("USER_MOBILE",edtMobileNo.text.toString().trim())
                        startActivity(intent)


                    } else {
                        hideProgress()
                        toast(response.body()?.details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====" + t)
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }

    private fun isValidate(): Boolean {
        var check = true

        if (edtFirstName.text.isEmpty()) {
            txt_error_first_name.visible()
            check = false
        }
        if (edtLastName.text.isEmpty()) {
            txt_error_last_name.visible()
            check = false
        }
        if (edtMobileNo.text.isEmpty()) {
            txt_error_mobile_no.visible()
            check = false
        }
        if (edtMobileNo.text.length < 10) {
            txt_error_mobile_no.visible()
            txt_error_mobile_no.text = "Enter valid mobile number"
            check = false
        }
        if (edtEmail.text.isEmpty()) {
            txt_error_email.visible()
            check = false
        }
        if (!edtEmail.text.toString().isValidEmail()) {
            txt_error_email.visible()
            txt_error_email.text = "Enter valid email"
            check = false
        }

        return check
    }

}