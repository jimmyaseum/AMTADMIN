package com.app.amtadminapp.activity.Vouchers.Hotels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.interFase.RecyclerMultipleItemClickListener
import com.app.amtadminapp.model.PlaceDataModel
import com.app.amtadminapp.model.PlaceInfo
import com.app.amtadminapp.model.TourBookingDetailsResponse
import com.app.amtadminapp.model.response.CommonResponse
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.AppConstant
import com.app.amtadminapp.utils.CommonUtil
import com.app.amtadminapp.utils.PrefConstants
import com.app.amtadminapp.utils.SharedPreference
import com.app.amtadminapp.utils.getRequestJSONBody
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.hideKeyboard
import com.app.amtadminapp.utils.isConnectivityAvailable
import com.app.amtadminapp.utils.preventTwoClick
import com.app.amtadminapp.utils.toast
import com.app.amtadminapp.utils.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_add_hotels_vouchers.*
import kotlinx.android.synthetic.main.activity_add_hotels_vouchers.view.*
import kotlinx.android.synthetic.main.adapter_add_more_places.etCity
import okhttp3.MultipartBody
import org.json.JSONObject
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class AddHotelsVouchersActivity : BaseActivity(),  View.OnClickListener,
    RecyclerMultipleItemClickListener, EasyPermissions.PermissionCallbacks {

    var state: String = ""
    var sharedPreference: SharedPreference? = null

    var arrayList: ArrayList<PlaceDataModel>? = null
    lateinit var adapter: AddMorePlaceAdapter

    var arrayListPlaceType: ArrayList<PlaceInfo>? = null
    var positionListItem: Int = 0
    var selectedPlaceposition = 0

    var ImagePaths: ArrayList<Uri> = ArrayList()
    private var ImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hotels_vouchers)
        getIntentData()
        initializeView()
    }

    private fun getIntentData() {
        state = intent.getStringExtra("state").toString()
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(this@AddHotelsVouchersActivity)
        setToolBar()
        InitiaListner()
    }

    private fun setToolBar() {
        if(state.equals("add")) {
            tbTvTitle.text = "Add Hotel Voucher"

        } else {
            tbTvTitle.text = "Edit Hotel Voucher"

        }
    }

    private fun InitiaListner() {
        imgBack.setOnClickListener(this)
        txtSave.setOnClickListener(this)

        edtTourBookingNo.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(this, v)
                GetAllPlaceTypeAPI()
                true
            } else {
                false
            }
        }

        edtTourBookingNo.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                if (edtTourBookingNo.text!!.isNotEmpty()) {
                    hideKeyboard(this, view)
                    GetAllPlaceTypeAPI()
                }
            }
        })

        rvPlace.layoutManager = LinearLayoutManager(
            applicationContext,
            RecyclerView.VERTICAL, false
        )
        rvPlace.isNestedScrollingEnabled = false

    }

    private fun GetAllPlaceTypeAPI() {
        showProgress()

        val jsonObject = JSONObject()
        jsonObject.put("TourBookingNo", edtTourBookingNo.text.toString().trim())

        val call = ApiUtils.apiInterface2.getTourBookingDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<TourBookingDetailsResponse> {
            override fun onResponse(call: Call<TourBookingDetailsResponse>, response: Response<TourBookingDetailsResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        hideProgress()
                        val Info = response.body()?.Data!!

                        LL_PaxInfo.visible()
                        llTBNOInfo.visible()

                        if(Info.Name != null && Info.Name != "") {
                            txtName.text = Info.Name
                        }
                        if(Info.MobileNo != null && Info.MobileNo != "") {
                            txtMobile.text = Info.MobileNo
                        }
                        if(Info.TourName != null && Info.TourName != "") {
                            txtTourName.text = Info.TourName
                        }
                        if(Info.TourDate != null && Info.TourDate != "") {
                            txtTourDate.text = Info.TourDate
                        }
                        if(Info.RoomType != null && Info.RoomType != "") {
                            txtRoomType.text = Info.RoomType
                        }

                        arrayListPlaceType = Info.places!!
                        setDefaultData()

                    } else {
                        hideProgress()
                        LL_PaxInfo.gone()
                        llTBNOInfo.gone()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<TourBookingDetailsResponse>, t: Throwable) {
                hideProgress()
                LL_PaxInfo.gone()
                llTBNOInfo.gone()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
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
                if(state.equals("add")) {

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
//                                CallEditVoucherAPI()
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

    private fun isValidate(): Boolean {
        var check = true

        if (edtTourBookingNo.text.isEmpty()) {
            edtTourBookingNo.error = "Enter Tour Booking No"
            edtTourBookingNo.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.error_aaa))
            check = false
        }
        if (::adapter.isInitialized) {
            val arrayList1 = adapter.getAdapterArrayList()
            for (i in 0 until arrayList1!!.size) {
                if (TextUtils.isEmpty(arrayList1[i].place)) {
                    check = false
                    toast("Please enter Place Information",Toast.LENGTH_SHORT)
                }
            }
        }

        return check
    }

    // region Place Type
    private fun showPlaceDialog() {
        var dialogSelectPlace = Dialog(this)
        dialogSelectPlace.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_select, null)
        dialogSelectPlace.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectPlace.window!!.attributes)

        dialogSelectPlace.window!!.attributes = lp
        dialogSelectPlace.setCancelable(true)
        dialogSelectPlace.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectPlace.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectPlace.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectPlace.findViewById(R.id.rvDialogCustomer) as RecyclerView
        val edtSearchCustomer = dialogSelectPlace.findViewById(R.id.edtSearchCustomer) as EditText

        val itemAdapter = DialogPlaceAdapter(this, arrayListPlaceType!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                val pax = arrayListPlaceType!![pos].City!!
                val paxid = arrayListPlaceType!![pos].CityID!!
                adapter.updateItem(positionListItem, pax, paxid)
                dialogSelectPlace!!.dismiss()
            }
        })

        rvDialogCustomer.adapter = itemAdapter

        edtSearchCustomer.gone()
        dialogSelectPlace!!.show()
    }
    // endregion

    private fun setDefaultData() {
        arrayList = ArrayList()
        arrayList?.add(PlaceDataModel(placeid = 0, place = "", document = null, documentname = ""))
        setAdapterData(arrayList,state)
    }

    private fun setAdapterData(arrayList: ArrayList<PlaceDataModel>?, state: String) {
        adapter = AddMorePlaceAdapter(arrayList, this, state)
        rvPlace.adapter = adapter
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int, name: String) {
        hideKeyboard(applicationContext, view)

        when (view.id)
        {
            R.id.imgDelete -> {
                if (::adapter.isInitialized) {
                    adapter.remove(position)
                }
            }
            R.id.tvAddMore -> {
                preventTwoClick(view)
                if (::adapter.isInitialized) {
                    adapter.addItem(PlaceDataModel(), 1)
                }
            }
            R.id.etCity -> {
                preventTwoClick(view)
                if (::adapter.isInitialized) {

                    if (!etCity.text.toString().isEmpty()) {
                        for (i in arrayListPlaceType!!.indices) {
                            if (name in arrayListPlaceType!![i].City!!) {
                                selectedPlaceposition = i
                                positionListItem = position

                            }
                        }
                        showPlaceDialog()
                    } else {
                        selectedPlaceposition = 0
                        positionListItem = position

                        showPlaceDialog()

                    }
                }
            }
            R.id.etUploadDocument -> {
                preventTwoClick(view)
                positionListItem = position
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

                    adapter.updateItem3(positionListItem, ImageUri)

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
                ImagePaths = java.util.ArrayList()
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

                adapter.updateItem2(positionListItem, ImageUri, sPath)

            }
        }
    }

    private fun CallAddVoucherAPI() {
        showProgress()
        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }
        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!
        var cityids = ""
        var documents = ""
        val partsList: java.util.ArrayList<MultipartBody.Part> = java.util.ArrayList()
        if (::adapter.isInitialized) {
            val arrayList1 = adapter.getAdapterArrayList()

            val arrListcity: ArrayList<Int> = ArrayList()
            val arrListimage: ArrayList<Uri> = ArrayList()

            for(i in 0 until arrayList1!!.size) {
                arrListcity.add(arrayList1!![i].placeid)
                arrListimage.add(arrayList1!![i].document!!)
            }
            cityids = arrListcity.toString().replace("[", "").replace("]", "")
            documents = arrListimage.toString().replace("[", "").replace("]", "")

            if(arrListimage.size > 0) {
                for (i in arrListimage.indices) {
                    if(arrListimage[i].toString().contains(".pdf")) {
                        partsList.add(CommonUtil.prepareFilePart(this, "application/*", "HotelVoucherImage", arrListimage[i]))

                    } else {
                        partsList.add(CommonUtil.prepareFilePart(this, "image/*", "HotelVoucherImage", arrListimage[i]))
                    }
                }
            }
        }
        val mCreatedBy = CommonUtil.createPartFromString(CreatedBy)
        val mBookingNo = CommonUtil.createPartFromString(edtTourBookingNo.text.toString().trim())
        val mCityids = CommonUtil.createPartFromString(cityids)

        var call = ApiUtils.apiInterface2.AddHotelVoucher(
            mCreatedBy,
            mBookingNo,
            mCityids,
            attachment = partsList
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
                        toast(response.body()?.message.toString(), AppConstant.TOAST_SHORT)

                        val intent = Intent()
                        intent.putExtra(AppConstant.IS_API_CALL, true)
                        setResult(Activity.RESULT_OK, intent)
                        finish()

                    } else {
                        hideProgress()
                        toast(response.body()?.message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
        })
    }

}