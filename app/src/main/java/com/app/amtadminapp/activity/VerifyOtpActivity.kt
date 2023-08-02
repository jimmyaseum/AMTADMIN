package com.app.amtadminapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.amtadminapp.Chatbot.ChatConstant
import com.app.amtadminapp.R
import com.app.amtadminapp.model.response.RegistrationModel
import com.app.amtadminapp.model.response.RegistrationResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_phone_verify.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyOtpActivity : BaseActivity() , View.OnClickListener {

    var clickCount: Int = 0
    var ID: Int = 0

    private var mAuth: FirebaseAuth? = null
    private var RootRef: DatabaseReference? = null

    companion object {
        private const val TAG = "LoginActivity"
        var fcmDeviceToken = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verify)
        getDeviceToken()
//        requestsmspermission()
        initializeView()
    }

    override fun initializeView() {

        mAuth = FirebaseAuth.getInstance()
        RootRef = FirebaseDatabase.getInstance().reference.child(ChatConstant.F_EMPLOYEE)

        val mobile = intent.getStringExtra("USER_MOBILE").toString()
        ID = intent.getIntExtra("USER_ID",0)
        edtMobileNo.setText(mobile)

//        OTP_Receiver().setEditText(edtOTP1,edtOTP2,edtOTP3,edtOTP4)
        startTimer(30000)

        edtOTP1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()

                    V_otp1.gone()
                    V_fill_otp1.visible()
                    edtOTP2.requestFocus()
                } else {
                    V_otp1.visible()
                    V_fill_otp1.gone()
                }
            }

        })
        edtOTP2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()

                    V_otp2.gone()
                    V_fill_otp2.visible()
                    edtOTP3.requestFocus()
                } else {
                    V_otp2.visible()
                    V_fill_otp2.gone()
                    edtOTP1.requestFocus()
                }
            }

        })
        edtOTP3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()

                    V_otp3.gone()
                    V_fill_otp3.visible()
                    edtOTP4.requestFocus()
                } else {
                    V_otp3.visible()
                    V_fill_otp3.gone()
                    edtOTP2.requestFocus()
                }
            }

        })
        edtOTP4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    V_otp4.gone()
                    V_fill_otp4.visible()
                } else {
                    V_otp4.visible()
                    V_fill_otp4.gone()
                    edtOTP3.requestFocus()
                }
            }
        })

        LLcardButtonVerify.setOnClickListener(this)
        txtresend.setOnClickListener(this)

        edtOTP4.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                CallVerifyOTPAPI()
                true
            } else {
                false
            }
        }
    }

   /* private fun requestsmspermission() {
        val smspermission: String = Manifest.permission.RECEIVE_SMS
        val grant = ContextCompat.checkSelfPermission(this, smspermission)
        // to check if read SMS permission is granted or not
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permission_list = arrayOfNulls<String>(1)
            permission_list[0] = smspermission
            ActivityCompat.requestPermissions(this, permission_list, 1)
        }
    }*/

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.LLcardButtonVerify -> {
                CallVerifyOTPAPI()
            }
            R.id.txtresend -> {
                clickCount = clickCount + 1
                if(clickCount < 5) {
                    startTimer(30000)
                } else {
                    startTimer(600000)
                }
            }
        }
    }

    private  fun startTimer(time: Long) {

        object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60) % 60)

                txttimer.setText("Automatically receive OTP in " + minutes + " : " + seconds)
                llTimer.visible()
                llResend.gone()
            }

            override fun onFinish() {
                llResend.visible()
                llTimer.gone()
            }
        }.start()
    }

    private fun CallVerifyOTPAPI() {

        if (fcmDeviceToken.isNullOrEmpty()) {
            fcmDeviceToken = "123456"
        }

        val otp = edtOTP1.text.toString().trim().plus(edtOTP2.text.toString().trim()).plus(edtOTP3.text.toString().trim()).plus(edtOTP4.text.toString().trim())

        showProgress()

        val jsonObject = JSONObject()

        jsonObject.put("ID", ID)
        jsonObject.put("OTP",otp)
        jsonObject.put("DeviceToken",fcmDeviceToken)
        jsonObject.put("DeviceType",AppConstant.DEVICETYPE)

        val call = ApiUtils.apiInterface.VerifyOTP(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.code == 200) {

                        val sharedPreference = SharedPreference(this@VerifyOtpActivity)
                        val userModel: RegistrationModel = response.body()?.data!!
                        val id = userModel.ID.toString()
                        sharedPreference.setPreference(PrefConstants.PREF_IS_LOGIN, "1")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_ID, id)
                        sharedPreference.setPreference(PrefConstants.PREF_USER_TYPE_ID, userModel.UserTypeID)
                        sharedPreference.setPreference(PrefConstants.PREF_USER_NAME, userModel.Name)
                        sharedPreference.setPreference(PrefConstants.PREF_FullUSER_NAME, userModel.UserName)
                        sharedPreference.setPreference(PrefConstants.PREF_USER_FIRST_NAME, userModel.FirstName)
                        sharedPreference.setPreference(PrefConstants.PREF_USER_LAST_NAME, userModel.LastName)
                        sharedPreference.setPreference(PrefConstants.PREF_USER_IMAGE, userModel.CustomerImage!!)
                        sharedPreference.setPreference(PrefConstants.PREF_USER_MOBILE, userModel.MobileNo)
                        sharedPreference.setPreference(PrefConstants.PREF_USER_EMAIL, userModel.EmailID)
                        sharedPreference.setPreference(PrefConstants.PREF_TOKEN, userModel.Token)

                        if(userModel.EmailID != "") {
                            CreateNewUserFirebase(userModel)
                        } else {
                            hideProgress()
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                    } else {
                        hideProgress()
                        toast(response.body()?.details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun CreateNewUserFirebase(userModel: RegistrationModel) {

        val currentUser = mAuth!!.currentUser
        if (currentUser == null) {
            mAuth!!.signInWithEmailAndPassword(userModel.EmailID, "123456").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUserID = mAuth!!.currentUser!!.uid
                    val deviceToken = FirebaseInstanceId.getInstance().token
                    RootRef!!.child(currentUserID).child("Uid").setValue(currentUserID)
                    RootRef!!.child(currentUserID).child("device_token").setValue(deviceToken)
                    RootRef!!.child(currentUserID).child("id").setValue(userModel.ID)
                    RootRef!!.child(currentUserID).child("usertype").setValue("employee")
                    RootRef!!.child(currentUserID).child("name").setValue(userModel.UserName)
                    RootRef!!.child(currentUserID).child("mobile").setValue(userModel.MobileNo)
                    RootRef!!.child(currentUserID).child("image").setValue(userModel.CustomerImage)
                        .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                            if (task.isSuccessful) {
                                hideProgress()
                                val intent = Intent(applicationContext, HomeActivity::class.java)
                                startActivity(intent)
                                finishAffinity()
                                Toast.makeText(this@VerifyOtpActivity, "Logged in Successful...", Toast.LENGTH_SHORT).show()
                            }
                        })
                } else {

                    mAuth!!.createUserWithEmailAndPassword(userModel.EmailID, "123456")
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val deviceToken = FirebaseInstanceId.getInstance().token
                                val currentUserID = mAuth!!.currentUser!!.uid
                                RootRef!!.child(currentUserID).child("Uid").setValue(currentUserID)
                                RootRef!!.child(currentUserID).child("device_token").setValue(deviceToken)
                                RootRef!!.child(currentUserID).child("id").setValue(userModel.ID)
                                RootRef!!.child(currentUserID).child("usertype").setValue("employee")
                                RootRef!!.child(currentUserID).child("name").setValue(userModel.UserName)
                                RootRef!!.child(currentUserID).child("mobile").setValue(userModel.MobileNo)
                                RootRef!!.child(currentUserID).child("image").setValue(userModel.CustomerImage)

                                hideProgress()
                                val intent = Intent(applicationContext, HomeActivity::class.java)
                                startActivity(intent)
                                finishAffinity()

                            } else {
                                hideProgress()
                                val intent = Intent(applicationContext, HomeActivity::class.java)
                                startActivity(intent)
                                finishAffinity()
                            }
                        }
                }
            }
        }
        else {
            VerifyUserExistance(userModel)
        }
    }

    private fun VerifyUserExistance(userModel: RegistrationModel) {
        val currentUserID: String = mAuth!!.getCurrentUser()!!.getUid()
        RootRef!!.child(currentUserID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("email").exists()) {
                        val currentUserID = mAuth!!.currentUser!!.uid
                        val deviceToken = FirebaseInstanceId.getInstance().token
                        RootRef!!.child(currentUserID).child("Uid").setValue(currentUserID)
                        RootRef!!.child(currentUserID).child("device_token").setValue(deviceToken)
                        RootRef!!.child(currentUserID).child("id").setValue(userModel.ID)
                        RootRef!!.child(currentUserID).child("usertype").setValue("employee")
                        RootRef!!.child(currentUserID).child("name").setValue(userModel.UserName)
                        RootRef!!.child(currentUserID).child("mobile").setValue(userModel.MobileNo)
                        RootRef!!.child(currentUserID).child("image").setValue(userModel.CustomerImage)
                            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                                if (task.isSuccessful) {
                                    hideProgress()
                                    val intent = Intent(applicationContext, HomeActivity::class.java)
                                    startActivity(intent)
                                    finishAffinity()
                                }
                            })
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                fcmDeviceToken = task.result!!.token
            })
    }
}