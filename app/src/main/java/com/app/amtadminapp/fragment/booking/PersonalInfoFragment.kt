package com.app.amtadminapp.fragment.booking

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.TourBookingFormActivity
import com.app.amtadminapp.adapter.dialog.DialogCityAdapter
import com.app.amtadminapp.fragment.BaseFragment
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.response.*
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import kotlinx.android.synthetic.main.fragment_personal_info.*
import kotlinx.android.synthetic.main.fragment_personal_info.edtCity
import kotlinx.android.synthetic.main.fragment_personal_info.edtCountry
import kotlinx.android.synthetic.main.fragment_personal_info.edtState
import kotlinx.android.synthetic.main.fragment_personal_info.view.*
import kotlinx.android.synthetic.main.fragment_personal_info.view.LLcardButtonNext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalInfoFragment  : BaseFragment(), View.OnClickListener  {
    private var views: View? = null
    var sharedPreference: SharedPreference? = null

    var arrayListCity: ArrayList<CityModel>? = null
    var CityId : Int? = 0
    var CityName : String? = ""
    var StateId : Int? = 0
    var StateName : String? = ""
    var CountryId : Int? = 0
    var CountryName : String? = ""

    var arrayListCompanyCity: ArrayList<CityModel>? = null
    var CompanyCityId : Int? = 0
    var CompanyCityName : String? = ""
    var CompanyStateId : Int? = 0
    var CompanyStateName : String? = ""
    var CompanyCountryId : Int? = 0
    var CompanyCountryName : String? = ""

    var SectorType: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_personal_info, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(activity!!)

        views!!.txtTourBookingNo.text = "Tour Booking No : "+ AppConstant.BookingNo

        callDetailApi(AppConstant.TOURBookingID)

        views!!.cbYes.setOnClickListener {
            views!!.cbYes.isChecked = true
            views!!.cbNo.isChecked = false
            views!!.LL_Company.visible()
        }
        views!!.cbNo.setOnClickListener {
            views!!.cbNo.isChecked = true
            views!!.cbYes.isChecked = false
            views!!.LL_Company.gone()
        }

        views!!.edtCity.setOnClickListener(this)
        views!!.edtCompanyCity.setOnClickListener(this)
        views!!.LLcardButtonNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        hideKeyboard(activity!!, v)
        when (v?.id) {
            R.id.edtCity -> {
                preventTwoClick(v)
                if(!arrayListCity.isNullOrEmpty()) {
                    selectCityDialog()
                } else {
                    GetAllCityAPI(1)
                }
            }
            R.id.edtCompanyCity -> {
                preventTwoClick(v)
                if(!arrayListCompanyCity.isNullOrEmpty()) {
                    selectCompanyCityDialog()
                } else {
                    GetAllCityAPI(2)
                }
            }
            R.id.LLcardButtonNext -> {
                preventTwoClick(v)
                hideKeyboard(activity!!, v)
                val flag = isValidate()
                when (flag) {
                    true -> {
                        if (isConnectivityAvailable(activity!!)) {

                            CallUpdateAPI()
                        } else {
                            activity!!.toast(
                                getString(R.string.str_msg_no_internet),
                                AppConstant.TOAST_SHORT
                            )
                        }
                    }
                }
            }
        }
    }

    private fun CallUpdateAPI() {
        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(activity!!)
        }

        val jsonObject = JSONObject()

        jsonObject.put("ID",AppConstant.TOURBookingID)
        jsonObject.put("CustomerID",AppConstant.CustomerID)
        jsonObject.put("IsCustomerUpdate", false)
        jsonObject.put("FirstName", views!!.edtFirstName.text.toString().trim())
        jsonObject.put("LastName", views!!.edtLastName.text.toString().trim())
        jsonObject.put("Address",views!!.edtAddress.text.toString().trim())
        jsonObject.put("CityID", CityId)
        jsonObject.put("StateID", StateId)
        jsonObject.put("CountryID", CountryId)
        jsonObject.put("MobileNoDuringTravelling",views!!.edtTravellingMobileNo.text.toString().trim())
        jsonObject.put("EmailID",views!!.edtEmail.text.toString().trim())
        jsonObject.put("ResidentPhoneNo", views!!.edtResidentPhoneNo.text.toString().trim())
        jsonObject.put("EmergencyNo", views!!.edtEmergencyNo.text.toString().trim())
        jsonObject.put("PANCardNo",views!!.edtPanNo.text.toString().trim())
        jsonObject.put("PassportNo", views!!.edtPassPortNo.text.toString().trim())
        jsonObject.put("AadharNo", views!!.edtAadharNo.text.toString().trim())
        jsonObject.put("IsCompanyInvoice", views!!.cbYes.isChecked)
        if(views!!.cbYes.isChecked) {
            jsonObject.put("CompanyName",views!!.edtCompanyName.text.toString().trim())
            jsonObject.put("CompanyAddress", views!!.edtCompanyAddress.text.toString().trim())
            jsonObject.put("CompanyGSTNo",views!!.edtCompanyGSTNo.text.toString().trim() )
            jsonObject.put("CompanyPANNo", views!!.edtCompanyPANNo.text.toString().trim())
            jsonObject.put("CompanyCityID", CompanyCityId)
            jsonObject.put("CompanyStateID", CompanyStateId)
            jsonObject.put("CompanyCountryID", CompanyCountryId)
        } else {
            jsonObject.put("CompanyName","")
            jsonObject.put("CompanyAddress", "")
            jsonObject.put("CompanyGSTNo", "")
            jsonObject.put("CompanyPANNo", "")
            jsonObject.put("CompanyCityID", 0)
            jsonObject.put("CompanyStateID", 0)
            jsonObject.put("CompanyCountryID",0)
        }

        val call = ApiUtils.apiInterface2.AddTourPersonalInformation(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.code == 200) {
                        hideProgress()

                        var tourFormActivity: TourBookingFormActivity = activity as TourBookingFormActivity
                        tourFormActivity.updateStepsColor(3)
                        tourFormActivity.CURRENT_STEP_POSITION = 3

                        val fragment = PaxInfoFragment()
                        replaceFragment(R.id.container, fragment, PaxInfoFragment::class.java.simpleName)
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

    private fun GetAllCityAPI(mode: Int) {

        showProgress()
        val call = ApiUtils.apiInterface2.getAllCity()

        call.enqueue(object : Callback<CityResponse> {
            override fun onResponse(call: Call<CityResponse>, response: Response<CityResponse>) {

                if (response.code() == 200) {
                    LogUtil.e(TAG, "=====onResponse====")

                    if (response.body()?.code == 200) {

                        hideProgress()

                        arrayListCity = response.body()?.data!!
                        arrayListCompanyCity = response.body()?.data!!

                        if(mode == 1) {
                            if(arrayListCity!!.size > 0) {
                                selectCityDialog()
                            } else {
                                activity!!.toast("No Value Available.", AppConstant.TOAST_SHORT)
                            }
                        } else {
                            if(arrayListCompanyCity!!.size > 0) {
                                selectCompanyCityDialog()
                            } else {
                                activity!!.toast("No Value Available.", AppConstant.TOAST_SHORT)
                            }
                        }


                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====${t.printStackTrace()}")
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    /** AI005
     * This method is to open City dialog
     */
    private fun selectCityDialog() {
        var dialogSelectCity = Dialog(activity!!)
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

        val itemAdapter = DialogCityAdapter(activity!!, arrayListCity!!)
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

                    val itemAdapter = DialogCityAdapter(activity!!, arrItemsFinal1)
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
                    val itemAdapter = DialogCityAdapter(activity!!, arrayListCity!!)
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

    /** AI005
     * This method is to open City dialog
     */
    private fun selectCompanyCityDialog() {
        var dialogSelectCompanyCity = Dialog(activity!!)
        dialogSelectCompanyCity.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectCompanyCity.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectCompanyCity.window!!.attributes)

        dialogSelectCompanyCity.window!!.attributes = lp
        dialogSelectCompanyCity.setCancelable(true)
        dialogSelectCompanyCity.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectCompanyCity.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectCompanyCity.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectCompanyCity.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectCompanyCity.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogCityAdapter(activity!!, arrayListCompanyCity!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                CompanyCityId = arrayListCompanyCity!![pos].CityID
                CompanyCityName = arrayListCompanyCity!![pos].CityName
                CompanyStateId = arrayListCompanyCity!![pos].StateID
                CompanyStateName = arrayListCompanyCity!![pos].StateName
                CompanyCountryId = arrayListCompanyCity!![pos].CountryID
                CompanyCountryName = arrayListCompanyCity!![pos].CountryName

                edtCompanyCity.setText(CompanyCityName)
                edtCompanyState.setText(CompanyStateName)
                edtCompanyCountry.setText(CompanyCountryName)
                dialogSelectCompanyCity!!.dismiss()
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

                    val itemAdapter = DialogCityAdapter(activity!!, arrItemsFinal1)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            CompanyCityId = arrItemsFinal1!![pos].CityID
                            CompanyCityName = arrItemsFinal1!![pos].CityName
                            CompanyStateId = arrItemsFinal1!![pos].StateID
                            CompanyStateName = arrItemsFinal1!![pos].StateName
                            CompanyCountryId = arrItemsFinal1!![pos].CountryID
                            CompanyCountryName = arrItemsFinal1!![pos].CountryName

                            edtCompanyCity.setText(CompanyCityName)
                            edtCompanyState.setText(CompanyStateName)
                            edtCompanyCountry.setText(CompanyCountryName)
                            dialogSelectCompanyCity!!.dismiss()

                        }
                    })

                    rvDialogCustomer.adapter = itemAdapter
                } else {
                    val itemAdapter = DialogCityAdapter(activity!!, arrayListCity!!)
                    itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                        override fun onItemClickEvent(v: View, pos: Int, flag: Int) {

                            CompanyCityId = arrayListCompanyCity!![pos].CityID
                            CompanyCityName = arrayListCompanyCity!![pos].CityName
                            CompanyStateId = arrayListCompanyCity!![pos].StateID
                            CompanyStateName = arrayListCompanyCity!![pos].StateName
                            CompanyCountryId = arrayListCompanyCity!![pos].CountryID
                            CompanyCountryName = arrayListCompanyCity!![pos].CountryName

                            edtCompanyCity.setText(CompanyCityName)
                            edtCompanyState.setText(CompanyStateName)
                            edtCompanyCountry.setText(CompanyCountryName)
                            dialogSelectCompanyCity!!.dismiss()

                        }
                    })
                    rvDialogCustomer.adapter = itemAdapter
                }
            }
        })
        dialogSelectCompanyCity!!.show()
    }

    private fun isValidate(): Boolean {
        var check = true

        if (view!!.edtFirstName.text.isEmpty()) {
            view!!.edtFirstName.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
            check = false
        }
        if (view!!.edtLastName.text.isEmpty()) {
            view!!.edtLastName.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
            check = false
        }
        if (view!!.edtMobileNo.text.isEmpty()) {
            view!!.edtMobileNo.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
            check = false
        }
        if (view!!.edtEmail.text.isEmpty()) {
            view!!.edtEmail.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
            check = false
        }
        if (view!!.edtMobileNo.text!!.length < 10) {
            view!!.edtMobileNo.error = getString(R.string.error_valid_mobile_number)
            view!!.edtMobileNo.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
            check = false
        }
        if (!view!!.edtEmail.text!!.toString().isValidEmail()) {
            view!!.edtEmail.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
            view!!.edtEmail.error = getString(R.string.error_valid_email)
            check = false
        }
        if (view!!.edtCity.text.isEmpty()) {
            view!!.edtCity.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
            check = false
        }
        if(SectorType.equals("INTERNATIONAL")) {
            if (view!!.edtPassPortNo.text.isEmpty()) {
                view!!.edtPassPortNo.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
                check = false
            }
        }
        if(view!!.cbYes.isChecked) {
            if (view!!.edtCompanyName.text.isEmpty()) {
                view!!.edtCompanyName.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
                check = false
            }
            if (view!!.edtCompanyCity.text.isEmpty()) {
                view!!.edtCompanyCity.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
                check = false
            }
            if (view!!.edtCompanyPANNo.text.isEmpty()) {
                view!!.edtCompanyPANNo.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
                check = false
            } else {
                if (!isValidPanCardNo(view!!.edtCompanyPANNo.text!!.toString())) {
                    view!!.edtCompanyPANNo.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
                    view!!.edtCompanyPANNo.error = getString(R.string.error_valid_panno)
                    check = false
                } else {
                    if (!views!!.edtCompanyGSTNo.text.isEmpty()) {
                        if (!isValidGSTNo(view!!.edtCompanyGSTNo.text!!.toString())) {
                            view!!.edtCompanyGSTNo.error = getString(R.string.error_valid_gstno)
                            view!!.edtCompanyGSTNo.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.error_aaa))
                            check = false
                        }
                    }
                }
            }
        }

        return check
    }

    private fun callDetailApi(tourbookingid: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.getTourBookingInfo(tourbookingid)

        call.enqueue(object : Callback<TourBookingInfoResponse> {
            override fun onResponse(call: Call<TourBookingInfoResponse>, response: Response<TourBookingInfoResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        setAPIData(arrayList)
                    }
                    hideProgress()
                }
            }
            override fun onFailure(call: Call<TourBookingInfoResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun setAPIData(arrayList: ArrayList<TourBookingInfoModel>) {

        SectorType = arrayList[0].SectorType

        if(arrayList[0].FirstName != null && arrayList[0].FirstName != "") {
            views!!.edtFirstName.setText(arrayList[0].FirstName)
        }
        if(arrayList[0].LastName != null && arrayList[0].LastName != "") {
            views!!.edtLastName.setText(arrayList[0].LastName)
        }
        if(arrayList[0].EmailID != null && arrayList[0].EmailID != "") {
            views!!.edtEmail.setText(arrayList[0].EmailID)
        }
        if(arrayList[0].MobileNo != null && arrayList[0].MobileNo != "") {
            views!!.edtMobileNo.setText(arrayList[0].MobileNo)
        }
        if(arrayList[0].Address != null && arrayList[0].Address != "") {
            views!!.edtAddress.setText(arrayList[0].Address)
        }
        if(arrayList[0].CityName != null && arrayList[0].CityName != "") {
            CityId = arrayList[0].CityID!!
            CityName = arrayList[0].CityName!!
            views!!.edtCity.setText(arrayList[0].CityName)
        }
        if(arrayList[0].StateName != null && arrayList[0].StateName != "") {
            StateId = arrayList[0].StateID!!
            StateName = arrayList[0].StateName!!
            views!!.edtState.setText(arrayList[0].StateName)
        }
        if(arrayList[0].CountryName != null && arrayList[0].CountryName != "") {
            CountryId = arrayList[0].CountryID!!
            CountryName = arrayList[0].CountryName!!
            views!!.edtCountry.setText(arrayList[0].CountryName)
        }
        if(arrayList[0].MobileNoDuringTravelling != null && arrayList[0].MobileNoDuringTravelling != "") {
            views!!.edtTravellingMobileNo.setText(arrayList[0].MobileNoDuringTravelling)
        }
        if(arrayList[0].ResidentPhoneNo != null && arrayList[0].ResidentPhoneNo != "") {
            views!!.edtResidentPhoneNo.setText(arrayList[0].ResidentPhoneNo)
        }
        if(arrayList[0].EmergencyNo != null && arrayList[0].EmergencyNo != "") {
            views!!.edtEmergencyNo.setText(arrayList[0].EmergencyNo)
        }
        if(arrayList[0].PANCardNo != null && arrayList[0].PANCardNo != "") {
            views!!.edtPanNo.setText(arrayList[0].PANCardNo)
        }
        if(arrayList[0].PassportNo != null && arrayList[0].PassportNo != "") {
            views!!.edtPassPortNo.setText(arrayList[0].PassportNo)
        }
        if(arrayList[0].AadharNo != null && arrayList[0].AadharNo != "") {
            views!!.edtAadharNo.setText(arrayList[0].AadharNo)
        }

        if(arrayList[0].IsCompanyInvoice == true) {
            views!!.cbYes.isChecked = true
            views!!.cbNo.isChecked = false
            views!!.LL_Company.visible()
        } else {
            views!!.cbNo.isChecked = true
            views!!.cbYes.isChecked = false
            views!!.LL_Company.gone()
        }

        if(arrayList[0].CompanyName != null && arrayList[0].CompanyName != "") {
            views!!.edtCompanyName.setText(arrayList[0].CompanyName)
        }
        if(arrayList[0].CompanyAddress != null && arrayList[0].CompanyAddress != "") {
            views!!.edtCompanyAddress.setText(arrayList[0].CompanyAddress)
        }
        if(arrayList[0].CompanyGSTNo != null && arrayList[0].CompanyGSTNo != "") {
            views!!.edtCompanyGSTNo.setText(arrayList[0].CompanyGSTNo)
        }
        if(arrayList[0].CompanyPANNo != null && arrayList[0].CompanyPANNo != "") {
            views!!.edtCompanyPANNo.setText(arrayList[0].CompanyPANNo)
        }
        if(arrayList[0].CompanyCityName != null && arrayList[0].CompanyCityName != "") {
            CompanyCityId = arrayList[0].CompanyCityID!!
            CompanyCityName = arrayList[0].CompanyCityName!!
            views!!.edtCompanyCity.setText(arrayList[0].CompanyCityName)
        }
        if(arrayList[0].CompanyStateName != null && arrayList[0].CompanyStateName != "") {
            CompanyStateId = arrayList[0].CompanyStateID!!
            CompanyStateName = arrayList[0].CompanyStateName!!
            views!!.edtCompanyState.setText(arrayList[0].CompanyStateName)
        }
        if(arrayList[0].CompanyCountryName != null && arrayList[0].CompanyCountryName != "") {
            CompanyCountryId = arrayList[0].CompanyCountryID!!
            CompanyCountryName = arrayList[0].CompanyCountryName!!
            views!!.edtCompanyCountry.setText(arrayList[0].CompanyCountryName)
        }
    }


}