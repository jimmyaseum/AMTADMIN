package com.app.amtadminapp.activity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.amtadminapp.R
import com.app.amtadminapp.model.AdminAppVersion
import com.app.amtadminapp.model.response.CommonResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import com.app.amtadminapp.utils.PrefConstants.PREF_IS_LOGIN
import com.app.amtadminapp.utils.PrefConstants.PREF_IS_WELCOME
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    var sharedPreference: SharedPreference? = null
    private var progressDialog: ProgressDialog? = null

    var id = ""
    var Type = ""
    var customerid = ""
    var bookingno = ""

    var currentVersion = ""
    var LatestVersion = ""
    var dialog: Dialog? = null
    private var mAuth: FirebaseAuth? = null

    // Jimmy Latest Commited by 02/08/2023 on 3: 20
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreference = SharedPreference(applicationContext)
        mAuth = FirebaseAuth.getInstance()
//        getDeviceToken()

        getCurrentVersion()

        if(isOnline(this)) {
            getLatestVersion()
        } else {
            toast(getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
        }
    }

    private fun getLatestVersion() {

        showProgress()

        val jsonObject = JSONObject()
        jsonObject.put("SettingKey", "Admin")

        val call = ApiUtils.apiInterface2.getAdminAppVersion(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<AdminAppVersion> {
            override fun onResponse(call: Call<AdminAppVersion>, response: Response<AdminAppVersion>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        hideProgress()
                        val version = response.body()?.Data!!
                        LatestVersion = version.Version

                        if(currentVersion == LatestVersion) {
                            changeActivitys()
                        } else {
                            if (sharedPreference?.getPreferenceString(PREF_IS_LOGIN).equals("1")) {
                                callLogoutAPI()
                            } else {
                                showUpdateDialog()
                            }
                        }

                    } else {
                        hideProgress()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<AdminAppVersion>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun changeActivitys() {
        Handler().postDelayed(Runnable {

            if (sharedPreference?.getPreferenceString(PREF_IS_WELCOME).equals("1")) {

                if (sharedPreference?.getPreferenceString(PREF_IS_LOGIN).equals("1")) {

                    if (intent.extras != null && intent.hasExtra("ID")) {

                        id = intent.extras!!.getString("ID").toString()
                        Type = intent.extras!!.getString("NotificationType").toString()
                        bookingno = intent.extras!!.getString("TourBookingNo").toString()
                        customerid = intent.extras!!.getString("CustomerID").toString()

                        if(Type == "TOURBOOKING") {
                            val intent = Intent(this, TourBookingFormActivity::class.java)
                            intent.putExtra("state","edit")
                            intent.putExtra("TourBookingNo",bookingno)
                            intent.putExtra("ID",id.toInt())
                            intent.putExtra("CustomerID",customerid.toInt())
                            intent.putExtra("NotificationType",Type)
                            finish()
                        }
                        else {
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    else {
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                else {
                    val intent = Intent(applicationContext, StartActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            } else {
                val intent = Intent(applicationContext, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 3000)
    }

    /*private fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                VerifyOtpActivity.fcmDeviceToken = task.result!!.token

                Log.e("FCM TOKEN","===>"+task.result!!.token)
            })
    }*/

    private fun getCurrentVersion() {
        val pm = this.packageManager
        var pInfo: PackageInfo? = null
        try {
            pInfo = pm.getPackageInfo(this.packageName, 0)
        } catch (e1: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
        currentVersion = pInfo!!.versionName

    }

    private fun showUpdateDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Please Update")
        builder.setMessage("We have launched a new and improved version. Please update the app to continue using the app.")
        builder.setPositiveButton("Update Now",
            DialogInterface.OnClickListener { dialog, which ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.app.amtadminapp")
                    )
                )
                dialog.dismiss()
            })
        builder.setCancelable(false)
        dialog = builder.show()
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

                        val sharedPreference = SharedPreference(this@SplashActivity)
                        sharedPreference.setPreference(PrefConstants.PREF_IS_LOGIN, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_ID, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_FIRST_NAME, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_LAST_NAME, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_IMAGE, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_MOBILE, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_EMAIL, "")
                        sharedPreference.setPreference(PrefConstants.PREF_TOKEN, "")

                        mAuth!!.signOut()
                        showUpdateDialog()

                    } else {
                        hideProgress()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }

    fun hideProgress() {
        if (progressDialog != null)
            progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    fun showProgress() {
        hideProgress()
        progressDialog = CommonUtil.showLoadingDialog(this)
    }

}
