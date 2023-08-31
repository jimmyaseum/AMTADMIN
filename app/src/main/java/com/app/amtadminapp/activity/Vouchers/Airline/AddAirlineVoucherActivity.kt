package com.app.amtadminapp.activity.Vouchers.Airline

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.MimeTypeMap
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.model.TourBookingPaxResponse
import com.app.amtadminapp.model.response.CommonResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_add_airline_voucher.*
import kotlinx.android.synthetic.main.activity_add_airline_voucher.imgBack
import kotlinx.android.synthetic.main.activity_add_airline_voucher.tbTvTitle
import kotlinx.android.synthetic.main.activity_add_airline_voucher.txtSave
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_phone_verify.*
import kotlinx.android.synthetic.main.row_airline_voucher.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class AddAirlineVoucherActivity : BaseActivity(),  View.OnClickListener, EasyPermissions.PermissionCallbacks {

    var state: String = ""

    var CompanyId = 4
    var sharedPreference: SharedPreference? = null
    var calDate = Calendar.getInstance()
    var TicketPurchaseDate: String = ""
    private var ImageUri: Uri? = null

    var AVID: Int = 0
    var TPDate: String = ""
    var TBNO: String = ""
    var TotalPrice: String = ""
    var AVDocument: String = ""
    var DeparturePNR: String = ""
    var ReturnPNR: String = ""

    var ImagePaths: ArrayList<Uri> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_airline_voucher)
        getIntentData()
        initializeView()
    }

    private fun getIntentData() {
        state = intent.getStringExtra("State").toString()
        if(state.equals("Update")) {
            AVID = intent.getIntExtra("AVID",0)
            TPDate = intent.getStringExtra("TPDate").toString()
            TBNO = intent.getStringExtra("TBNO").toString()
            TotalPrice = intent.getStringExtra("TotalPrice").toString()
            AVDocument = intent.getStringExtra("AVDocument").toString()
            DeparturePNR = intent.getStringExtra("DeparturePNR").toString()
            ReturnPNR = intent.getStringExtra("ReturnPNR").toString()

            edtTicketPurchasedDate.setText(TPDate)
            TicketPurchaseDate = convertDateStringToString(TPDate, AppConstant.dd_MM_yyyy_Slash, AppConstant.yyyy_MM_dd_Dash)!!

            edtTourBookingNo.setText(TBNO)
            edtTotalPrice.setText(TotalPrice)
            edtDeparturePNRNo.setText(DeparturePNR)
            edtReturnPNRNo.setText(ReturnPNR)

            if(AVDocument.contains(".pdf")) {
                select_image.gone()
                ll_Pdf.visible()
                select_pdf.text = AVDocument
            } else {
                select_image.visible()
                ll_Pdf.gone()
                select_image.loadUrlRoundedCorner2(
                    AVDocument,
                    R.drawable.ic_image,1
                )
            }

            CallTourBookingPaxAPI()

        }
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(this@AddAirlineVoucherActivity)
        setToolBar()
        InitiaListner()
    }

    private fun setToolBar() {
        if(state.equals("Add")) {
            tbTvTitle.text = "Add Airline Voucher"
            upload_image.text = "Add Document"
        } else {
            tbTvTitle.text = "Edit Airline Voucher"
            upload_image.text = "Change Document"
        }
    }

    private fun InitiaListner() {
        imgBack.setOnClickListener(this)
        txtSave.setOnClickListener(this)
        upload_image.setOnClickListener(this)
        edtTicketPurchasedDate.setOnClickListener(this)

        upload_image.setPaintFlags(upload_image.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        if(state.equals("Update")) {
            select_image.setOnClickListener {
                if(isOnline(this)) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(AVDocument))
                    startActivity(browserIntent)
                } else {
                    toast(resources.getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
                }
            }

            ll_Pdf.setOnClickListener {
                if(isOnline(this)) {
                    var format = "https://docs.google.com/gview?embedded=true&url=%s"
                    val fullPath: String = java.lang.String.format(Locale.ENGLISH, format, AVDocument)
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
                    startActivity(browserIntent)
                } else {
                    toast(resources.getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
                }
            }
        }

        edtTourBookingNo.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                 CallTourBookingPaxAPI()
                true
            } else {
                false
            }
        }

        edtTourBookingNo.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                if (edtTourBookingNo.text!!.isNotEmpty()) {
                    CallTourBookingPaxAPI()
                    hideKeyboard(this@AddAirlineVoucherActivity, view)

                }
            }
        })
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

                    hideKeyboard(applicationContext, txtSave)
                    val flag = isValidate()
                    when (flag) {
                        true -> {
                            if (isConnectivityAvailable(this)) {
                                CallAddVoucherAPI()
                            } else {
                                toast(getString(R.string.str_msg_no_internet), AppConstant.TOAST_SHORT)
                            }
                        }
                    }

                } else {

                    hideKeyboard(applicationContext, txtSave)
                    val flag = isValidate()
                    when (flag) {
                        true -> {
                            if (isConnectivityAvailable(this)) {
                                CallEditVoucherAPI()
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
            R.id.edtTicketPurchasedDate -> {
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
            R.id.upload_image -> {
                preventTwoClick(v)
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                        showBottomSheetDialogGST()
                    }
                    else {
                        EasyPermissions.requestPermissions(
                            this,
                            getString(R.string.msg_permission_camera),
                            900,
                            Manifest.permission.CAMERA
                        )
                    }
                } else {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.msg_permission_storage),
                        900,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
            }
        }
    }

    private fun CallAddVoucherAPI() {
        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }

        val partsList: ArrayList<MultipartBody.Part> = ArrayList()

        if (ImageUri != null) {
            if(ImageUri.toString().contains(".pdf")) {
                partsList.add(CommonUtil.prepareFilePart(this, "*/*", "AirlineVoucherTicket", ImageUri!!))
            } else {
                partsList.add(CommonUtil.prepareFilePart(this, "image/*", "AirlineVoucherTicket", ImageUri!!))
            }
        } else {
            val attachmentEmpty: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
            partsList.add(MultipartBody.Part.createFormData("AirlineVoucherTicket", "", attachmentEmpty))
        }

        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        val MCreatedBy = CommonUtil.createPartFromString(CreatedBy.toString())
        val MTourBookingNo = CommonUtil.createPartFromString(edtTourBookingNo.text.toString().trim())
        val MDeparturePNRNo = CommonUtil.createPartFromString(edtDeparturePNRNo.text.toString().trim())
        val MArrivalPNRNo = CommonUtil.createPartFromString(edtReturnPNRNo.text.toString().trim())
        val MCompanyId = CommonUtil.createPartFromString(CompanyId.toString())
        val MTicketPurchaseDate = CommonUtil.createPartFromString(TicketPurchaseDate)
        val MTotalPrice= CommonUtil.createPartFromString(edtTotalPrice.text.toString().trim())

        var call = ApiUtils.apiInterface2.AddAirlineVoucher(
            MTourBookingNo,
            MCompanyId,
            MTicketPurchaseDate,
            MTotalPrice,
            MCreatedBy,
            MDeparturePNRNo,
            MArrivalPNRNo,
            AirlineVoucherTicket = partsList
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

    private fun CallEditVoucherAPI() {
        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }

        val partsList: ArrayList<MultipartBody.Part> = ArrayList()

        if (ImageUri != null) {
            if(ImageUri.toString().contains(".pdf")) {
                partsList.add(CommonUtil.prepareFilePart(this, "*/*", "AirlineVoucherTicket", ImageUri!!))
            } else {
                partsList.add(CommonUtil.prepareFilePart(this, "image/*", "AirlineVoucherTicket", ImageUri!!))
            }
        } else {
            val attachmentEmpty: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
            partsList.add(MultipartBody.Part.createFormData("AirlineVoucherTicket", "", attachmentEmpty))
        }

        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        val mAVID = CommonUtil.createPartFromString(AVID.toString())

        val MCreatedBy = CommonUtil.createPartFromString(CreatedBy.toString())
        val MTourBookingNo = CommonUtil.createPartFromString(edtTourBookingNo.text.toString().trim())
        val MCompanyId = CommonUtil.createPartFromString(CompanyId.toString())
        val MTicketPurchaseDate = CommonUtil.createPartFromString(TicketPurchaseDate)
        val MDeparturePNRNo = CommonUtil.createPartFromString(edtDeparturePNRNo.text.toString().trim())
        val MArrivalPNRNo = CommonUtil.createPartFromString(edtReturnPNRNo.text.toString().trim())
        val MTotalPrice= CommonUtil.createPartFromString(edtTotalPrice.text.toString().trim())

        var call = ApiUtils.apiInterface2.UpdateAirlineVoucher(
            mAVID,
            MTourBookingNo,
            MCompanyId,
            MTicketPurchaseDate,
            MTotalPrice,
            MCreatedBy,
            MDeparturePNRNo,
            MArrivalPNRNo,
            AirlineVoucherTicket = partsList
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

    private fun CallTourBookingPaxAPI() {
        showProgress()

        val jsonObject = JSONObject()
        jsonObject.put("TourBookingNo", edtTourBookingNo.text.toString().trim())

        val call = ApiUtils.apiInterface2.getTourBookingPaxDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<TourBookingPaxResponse> {
            override fun onResponse(call: Call<TourBookingPaxResponse>, response: Response<TourBookingPaxResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        hideProgress()
                        val Info = response.body()?.Data!!

                        llTBNOInfo.visible()
                        if(Info.Name != null && Info.Name != "") {
                            txtName.text = Info.Name
                        }
                        if(Info.MobileNo != null && Info.MobileNo != "") {
                            txtMobile.text = Info.MobileNo
                        }

                    } else {
                        hideProgress()


                        llTBNOInfo.gone()

                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<TourBookingPaxResponse>, t: Throwable) {
                hideProgress()


                llTBNOInfo.gone()

                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })


    }

    private fun showBottomSheetDialogGST() {
        val bottomSheetDialog = BottomSheetDialog(this,R.style.SheetDialog)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)

        val Select_Image = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Image)
        val Select_Doc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Doc)
        val Select_WordDoc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_WordDoc)

        Select_Image!!.setOnClickListener {
            photopickerGST()
            bottomSheetDialog.dismiss()
        }
        Select_Doc!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 104)
            bottomSheetDialog.dismiss()
        }
        Select_WordDoc!!.gone()

        bottomSheetDialog.show()
    }

    private fun photopickerGST() {
        FilePickerBuilder.instance
            .setMaxCount(1) //optional
            .setSelectedFiles(ImagePaths) //optional
            .setActivityTheme(R.style.LibAppTheme) //optional
            .pickPhoto(this, 111);
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
                    select_image.loadURIRoundedCorner(
                        resultUri,
                        R.drawable.ic_image,
                        1
                    )
                    select_image.visible()
                    ll_Pdf.gone()
                } else {
                    toast(getString(R.string.str_msg_no_internet),Toast.LENGTH_LONG)
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                toast("No apps can perform this action.", Toast.LENGTH_LONG)
            }
        }
        if (requestCode == 111) {
            if (resultCode == RESULT_OK && data != null) {
                ImagePaths = ArrayList()
                ImagePaths.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)!!)
                val PassportPath = ImagePaths[0]
                CropImage.activity(PassportPath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)

            }
        }
        if(requestCode == 104) {
            // PDF File
            if (resultCode == RESULT_OK && data != null) {
                val sUri = data!!.data
                val sPath = sUri!!.path
                ImageUri = Uri.fromFile(fileFromContentUri4("passport", applicationContext, sUri))

                select_pdf.text = sPath
                ll_Pdf.visible()
                select_image.gone()
            }
        }
    }

    fun fileFromContentUri4(name  :String, context: Context, contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = "temp_file_" + name + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
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

    /** AI005
     * This method is to date change
     */
    private fun updateSelectDate(cal: Calendar) {
        TicketPurchaseDate = SimpleDateFormat(AppConstant.yyyy_MM_dd_Dash, Locale.US).format(cal.time)
        edtTicketPurchasedDate.setText(SimpleDateFormat(AppConstant.dd_MM_yyyy_Slash, Locale.US).format(cal.time))
    }

    private fun isValidate(): Boolean {
        var check = true

        if (edtTourBookingNo.text.isEmpty()) {
            edtTourBookingNo.error = "Enter Tour Booking No"
            edtTourBookingNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtTicketPurchasedDate.text.isEmpty()) {
            edtTicketPurchasedDate.error = "Select Ticket Purchased Date"
            edtTicketPurchasedDate.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (edtTotalPrice.text.isEmpty()) {
            edtTotalPrice.error = "Enter Total Price"
            edtTotalPrice.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }

        if(state.equals("Add")) {
            if (ImageUri == null) {
                toast("Please upload image",Toast.LENGTH_LONG)
                check = false
            }
        }
        return check
    }


}