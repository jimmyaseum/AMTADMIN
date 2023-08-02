package com.app.amtadminapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.adapter.dialog.DialogCityAdapter
import com.app.amtadminapp.adapter.dialog.DialogInitialAdapter
import com.app.amtadminapp.adapter.dialog.DialogRelationAdapter
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.retrofit.ApiUtils.apiInterface2
import com.app.amtadminapp.utils.*

import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.profile_image
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : BaseActivity(),  View.OnClickListener, EasyPermissions.PermissionCallbacks {
    var state: String = ""
    var custID: Int = 0
    var sharedPreference: SharedPreference? = null
    var calDate = Calendar.getInstance()
    var DOB: String = ""
    var gender: String = ""
    private var ImageUri: Uri? = null

    private val arrInitialList: ArrayList<String> = ArrayList()
    var InitialName : String = ""

    var arrayListCity: ArrayList<CityModel>? = null
    var CityId : Int = 0
    var CityName : String = ""
    var StateId : Int = 0
    var StateName : String = ""
    var CountryId : Int = 0
    var CountryName : String = ""

    var arrayListRelation: ArrayList<RelationModel>? = null
    var RelationId : Int = 0
    var RelationName : String = ""

    var ParentID : Int = 0

    var OperationType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        getIntentData()
        initializeView()
    }

    private fun getIntentData() {
        state = intent.getStringExtra("State").toString()
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(this@EditProfileActivity)

        setToolBar()

        // AI005 Mr, Mrs, Ms, Dr
        arrInitialList.add("Mr.")
        arrInitialList.add("Mrs.")
        arrInitialList.add("Ms.")
        arrInitialList.add("Dr.")

        InitiaListner()
    }

    private fun InitiaListner() {

        if(state.equals("Add")) {
            LLRelation.visible()
        }
        else if(state.equals("FamilyUpdate")) {
            OperationType = 13
            custID = intent.getIntExtra("CustometID",0)
            callCustomerDetailApi(custID)
            LLRelation.visible()
        }
        else {
            OperationType = 2
            // Call Find By Id and Set Data
            sharedPreference = SharedPreference(this)
            val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
            custID = userId
            callCustomerDetailApi(userId)
        }

        imgBack.setOnClickListener(this)
        txtSave.setOnClickListener(this)
        card_change_profile.setOnClickListener(this)
        edtInitial.setOnClickListener(this)
        edtDOB.setOnClickListener(this)
        edtCity.setOnClickListener(this)
        edtRelation.setOnClickListener(this)

        radioMale.setOnClickListener {
            radioMale.isChecked = true
            radioFemale.isChecked = false
        }

        radioFemale.setOnClickListener {
            radioMale.isChecked = false
            radioFemale.isChecked = true
        }
    }

    private fun setToolBar() {
        if(state.equals("Add")) {
            tbTvTitle.text = "Add Member"
        } else {
            tbTvTitle.text = "Edit Profile"
        }
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                preventTwoClick(v)
                finish()
            }
            R.id.txtSave -> {
                preventTwoClick(v)
                if(state.equals("Add")) {
                    if (radioMale.isChecked) {
                        gender = "MALE"
                    } else if (radioFemale.isChecked) {
                        gender = "FEMALE"
                    }
                    hideKeyboard(applicationContext, edtMobileNo)
                    val flag = isValidate()
                    when (flag) {
                        true -> {
                            if (isConnectivityAvailable(this)) {
                                CallAddProfileAPI()
                            } else {
                                toast(
                                    getString(R.string.str_msg_no_internet),
                                    AppConstant.TOAST_SHORT
                                )
                            }
                        }
                    }

                } else {

                    if (radioMale.isChecked) {
                        gender = "MALE"
                    } else if (radioFemale.isChecked) {
                        gender = "FEMALE"
                    }
                    hideKeyboard(applicationContext, edtMobileNo)
                    val flag = isValidate()
                    when (flag) {
                        true -> {
                            if (isConnectivityAvailable(this)) {
                                CallEditProfileAPI()
                            } else {
                                toast(
                                    getString(R.string.str_msg_no_internet),
                                    AppConstant.TOAST_SHORT
                                )
                            }
                        }
                    }
                }
            }
            R.id.edtDOB -> {
                preventTwoClick(v)
                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        calDate.set(Calendar.YEAR, year)
                        calDate.set(Calendar.MONTH, monthOfYear)
                        calDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateSelectDate(calDate)
                    },
                    calDate.get(Calendar.YEAR),
                    calDate.get(Calendar.MONTH),
                    calDate.get(Calendar.DAY_OF_MONTH)
                )
//                dpd.datePicker.minDate = System.currentTimeMillis() - 1000
//                dpd.datePicker.maxDate = System.currentTimeMillis() - 1000
                dpd.show()
            }
            R.id.card_change_profile -> {
                preventTwoClick(v)
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this)
                } else {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.msg_permission_storage),
                        900,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
            }
            R.id.edtInitial -> {
                preventTwoClick(v)
                selectInitialDialog()
            }
            R.id.edtCity -> {
                preventTwoClick(v)
                if(!arrayListCity.isNullOrEmpty()) {
                    selectCityDialog()
                } else {
                    GetAllCityAPI()
                }
            }
            R.id.edtRelation-> {
                preventTwoClick(v)
                if(!arrayListRelation.isNullOrEmpty()) {
                    selectRelationDialog()
                } else {
                    GetAllRelationAPI()
                }
            }
        }
    }

    private fun CallEditProfileAPI() {
        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }

        val partsList: ArrayList<MultipartBody.Part> = ArrayList()

        if (ImageUri != null) {
            partsList.add(CommonUtil.prepareFilePart(this, "image/*", "CustomerImage", ImageUri!!))
        } else {
            val attachmentEmpty: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
            partsList.add(MultipartBody.Part.createFormData("CustomerImage", "", attachmentEmpty))
        }

        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        var call = apiInterface2.CustomerUpdate(
            custID, InitialName, edtFirstName.text.toString().trim(), edtLastName.text.toString().trim(),
            edtMobileNo.text.toString().trim(), ParentID, RelationId,
            edtAddress.text.toString().trim(), edtEmail.text.toString().trim(), edtResidentPhoneNo.text.toString().trim(),
            edtTravellingMobileNo.text.toString().trim(), edtEmergencyNo.text.toString().trim(), DOB, gender, edtPincode.text.toString().trim(), CityId, StateId, CountryId,
            true, CreatedBy,OperationType,
            image = partsList
        )

        call.enqueue(object : Callback<CommonResponse> {
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
            }
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {

                if (response.code() == 200) {

                    if (response.body()?.code == 200) {
                        hideProgress()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)

                        val intent = Intent()
                        intent.putExtra(AppConstant.IS_API_CALL, true)
                        setResult(Activity.RESULT_OK, intent)
                        finish()

                    } else {
                        hideProgress()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
        })

    }

    private fun CallAddProfileAPI() {
        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }

        val partsList: ArrayList<MultipartBody.Part> = ArrayList()

        if (ImageUri != null) {
            partsList.add(CommonUtil.prepareFilePart(this, "image/*", "CustomerImage", ImageUri!!))
        } else {
            val attachmentEmpty: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
            partsList.add(MultipartBody.Part.createFormData("CustomerImage", "", attachmentEmpty))
        }

        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        var call = apiInterface2.CustomerAdd(
            InitialName, edtFirstName.text.toString().trim(), edtLastName.text.toString().trim(),
            edtMobileNo.text.toString().trim(), CreatedBy, RelationId,
            edtAddress.text.toString().trim(), edtEmail.text.toString().trim(), edtResidentPhoneNo.text.toString().trim(),
            edtTravellingMobileNo.text.toString().trim(), edtEmergencyNo.text.toString().trim(), DOB, gender, edtPincode.text.toString().trim(), CityId, StateId, CountryId,
            true, CreatedBy,
            image = partsList
        )

        call.enqueue(object : Callback<CommonResponse> {
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
            }
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {

                if (response.code() == 200) {

                    if (response.body()?.code == 200) {
                        hideProgress()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)

                        val intent = Intent()
                        intent.putExtra(AppConstant.IS_API_CALL, true)
                        setResult(Activity.RESULT_OK, intent)
                        finish()

                    } else {
                        hideProgress()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
        })

    }

    private fun GetAllCityAPI() {

        showProgress()
        val call = apiInterface2.getAllCity()

        call.enqueue(object : Callback<CityResponse> {
            override fun onResponse(call: Call<CityResponse>, response: Response<CityResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.code == 200) {
                        hideProgress()
                        arrayListCity = response.body()?.data!!
                        if(arrayListCity!!.size > 0) {
                            selectCityDialog()
                        } else {
                            toast("No Value Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                        toast(response.body()?.message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    /** AI005
     * This method is to open City dialog
     */
    private fun selectCityDialog() {
        var dialogSelectCity = Dialog(this)
        dialogSelectCity.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectCity.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectCity.window!!.attributes)

        dialogSelectCity.window!!.attributes = lp
        dialogSelectCity.setCancelable(true)
        dialogSelectCity.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectCity.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectCity.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectCity.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectCity.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogCityAdapter(this, arrayListCity!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                CityId = arrayListCity!![pos].CityID
                CityName = arrayListCity!![pos].CityName
                StateId = arrayListCity!![pos].StateID
                StateName = arrayListCity!![pos].StateName
                CountryId = arrayListCity!![pos].CountryID
                CountryName = arrayListCity!![pos].CountryName

                edtCity.setText(CityName)
                edtState.setText(StateName)
                edtCountry.setText(CountryName)
                dialogSelectCity!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrItemsFinal1: java.util.ArrayList<CityModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrayListCity!!) {
                        if (model.CityName!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogCityAdapter(this@EditProfileActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            CityId = arrItemsFinal1!![pos].CityID
                            CityName = arrItemsFinal1!![pos].CityName
                            StateId = arrItemsFinal1!![pos].StateID
                            StateName = arrItemsFinal1!![pos].StateName
                            CountryId = arrItemsFinal1!![pos].CountryID
                            CountryName = arrItemsFinal1!![pos].CountryName

                            edtCity.setText(CityName)
                            edtState.setText(StateName)
                            edtCountry.setText(CountryName)

                            dialogSelectCity!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogCityAdapter(this@EditProfileActivity, arrayListCity!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            CityId = arrayListCity!![pos].CityID
                            CityName = arrayListCity!![pos].CityName
                            StateId = arrayListCity!![pos].StateID
                            StateName = arrayListCity!![pos].StateName
                            CountryId = arrayListCity!![pos].CountryID
                            CountryName = arrayListCity!![pos].CountryName

                            edtCity.setText(CityName)
                            edtState.setText(StateName)
                            edtCountry.setText(CountryName)

                            dialogSelectCity!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectCity!!.show()
    }

    private fun GetAllRelationAPI() {

        showProgress()
        val call = apiInterface2.getAllRelation()

        call.enqueue(object : Callback<RelationResponse> {
            override fun onResponse(call: Call<RelationResponse>, response: Response<RelationResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.code == 200) {
                        hideProgress()
                        arrayListRelation = response.body()?.data!!
                        if(arrayListRelation!!.size > 0) {
                            selectRelationDialog()
                        } else {
                            toast("No Value Available.", AppConstant.TOAST_SHORT)
                        }
                    } else {
                        hideProgress()
                        toast(response.body()?.message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<RelationResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    /** AI005
     * This method is to open relation dialog
     */
    private fun selectRelationDialog() {
        var dialogSelectRelation = Dialog(this)
        dialogSelectRelation.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectRelation.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectRelation.window!!.attributes)

        dialogSelectRelation.window!!.attributes = lp
        dialogSelectRelation.setCancelable(true)
        dialogSelectRelation.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectRelation.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectRelation.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectRelation.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectRelation.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogRelationAdapter(this, arrayListRelation!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                RelationId = arrayListRelation!![pos].ID
                RelationName = arrayListRelation!![pos].Name
                edtRelation.setText(RelationName)

                dialogSelectRelation!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrItemsFinal1: java.util.ArrayList<RelationModel> = java.util.ArrayList()
                if (char.toString().trim().isNotEmpty()) {
                    val strSearch = char.toString()
                    for (model in arrayListRelation!!) {
                        if (model.Name!!.toLowerCase().contains(strSearch.toLowerCase())) {
                            arrItemsFinal1.add(model)
                        }
                    }

                    val itemAdapter = DialogRelationAdapter(this@EditProfileActivity, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            RelationId = arrItemsFinal1!![pos].ID
                            RelationName = arrItemsFinal1!![pos].Name
                            edtRelation.setText(RelationName)

                            dialogSelectRelation!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogRelationAdapter(this@EditProfileActivity, arrayListRelation!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            RelationId = arrayListRelation!![pos].ID
                            RelationName = arrayListRelation!![pos].Name
                            edtRelation.setText(RelationName)

                            dialogSelectRelation!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectRelation!!.show()
    }

    /** AI005
     * This method is to open Initial dialog
     */
    private fun selectInitialDialog() {
        var dialogSelectInitial = Dialog(this)
        dialogSelectInitial.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectInitial.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectInitial.window!!.attributes)

        dialogSelectInitial.window!!.attributes = lp
        dialogSelectInitial.setCancelable(true)
        dialogSelectInitial.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectInitial.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectInitial.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectInitial.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectInitial.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogInitialAdapter(this, arrInitialList!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                InitialName = arrInitialList!![pos]
                edtInitial.setText(InitialName)
                dialogSelectInitial!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectInitial!!.show()
    }

    /** AI005
     * This method is to date change
     */
    private fun updateSelectDate(cal: Calendar) {
        DOB = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        edtDOB.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
    }

    /** AI005
     * This method is to get image from external storage */

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri!!

                if (isConnectivityAvailable(this)) {

                    ImageUri = Uri.fromFile(File(resultUri.path))
                    profile_image.loadURIRoundedCorner(
                        resultUri,
                        R.drawable.ic_image,
                        1
                    )
                } else {
                    toast(getString(R.string.str_msg_no_internet),Toast.LENGTH_LONG)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                toast("No apps can perform this action.", Toast.LENGTH_LONG)
            }

        }
    }

    //Permission Result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //EasyPermissions.PermissionCallbacks
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    //EasyPermissions.PermissionCallbacks
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    private fun callCustomerDetailApi(userId: Int) {

        showProgress()

        val call = apiInterface2.getDetailByCustomers(userId)

        call.enqueue(object : Callback<CustomerResponse> {
            override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {
                if (response.code() == 200) {
                    var arrayList: CustomerListModel? = null
                    if (response.body()?.Status == 200) {
                        arrayList = response.body()?.Data!!
                        setAPIData(arrayList)
                    }
                    hideProgress()
                }
            }
            override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun setAPIData(arrayList: CustomerListModel) {

        InitialName = arrayList.Initials
        if(arrayList.DOB != null) {
            DOB = convertDateStringToString(
                arrayList.DOB,
                AppConstant.dd_MM_yyyy_Slash,
                AppConstant.yyyy_MM_dd_Dash
            )!!
        }
        CityId = arrayList.CityID
        CityName = arrayList.CityName
        StateId = arrayList.StateID
        StateName = arrayList.StateName
        CountryId = arrayList.CountryID
        CountryName = arrayList.CountryName
        gender = arrayList.Gender
        ParentID = arrayList.ParentCustomerID
        RelationId = arrayList.RelationshipID
        RelationName = arrayList.Relation

        edtInitial.setText(InitialName)
        edtFirstName.setText(arrayList.FirstName)
        edtLastName.setText(arrayList.LastName)
        edtEmail.setText(arrayList.EmailID)
        edtMobileNo.setText(arrayList.MobileNo)
        edtTravellingMobileNo.setText(arrayList.TravellingMobileNo)
        edtResidentPhoneNo.setText(arrayList.ResidentPhoneNo)
        edtEmergencyNo.setText(arrayList.EmergencyNo)
        edtDOB.setText(arrayList.DOB)
        edtAddress.setText(arrayList.Address)
        edtCity.setText(CityName)
        edtState.setText(StateName)
        edtCountry.setText(CountryName)
        edtPincode.setText(arrayList.Pincode)
        edtRelation.setText(RelationName)

        if(arrayList.CustomerImage != "" && arrayList.CustomerImage != null) {
            profile_image.loadUrlRoundedCorner(
                arrayList.CustomerImage,
                R.drawable.ic_profile,
                1
            )
        }

        if(gender.equals("FEMALE") || gender.equals("Female")) {
            radioFemale.isChecked = true
            radioMale.isChecked = false
        } else if(gender.equals("MALE") || gender.equals("Male")) {
            radioMale.isChecked = true
            radioFemale.isChecked = false
        } else {
            radioFemale.isChecked = false
            radioMale.isChecked = false
        }
    }

    private fun isValidate(): Boolean {
        var check = true

        if (edtFirstName.text.isEmpty()) {
            edtFirstName.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtLastName.text.isEmpty()) {
            edtLastName.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtMobileNo.text.isEmpty()) {
            edtMobileNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtEmail.text.isEmpty()) {
            edtEmail.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtMobileNo.text!!.length < 10) {
            edtMobileNo.error = getString(R.string.error_valid_mobile_number)
            edtMobileNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (!edtEmail.text!!.toString().isValidEmail()) {
            edtEmail.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            edtEmail.error = getString(R.string.error_valid_email)
            check = false
        }
        if (gender.equals("")) {
            toast("Please Select Gender",Toast.LENGTH_LONG)
            check = false
        }
        if (edtCity.text.isEmpty()) {
            edtCity.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        return check
    }

    fun hasText(editText: EditText, error_message: String?): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        editText.error = null

        if (text.length == 0) {
            editText.error = error_message
            return false
        }
        return true
    }
}