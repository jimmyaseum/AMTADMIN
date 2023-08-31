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
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.BaseActivity
import com.app.amtadminapp.interFase.RecyclerClickListener
import com.app.amtadminapp.model.PlaceInfo
import com.app.amtadminapp.model.TourBookingDetailsResponse
import com.app.amtadminapp.model.response.CommonResponse
import com.app.amtadminapp.model.response.HotelVoucherFindByIDResponse
import com.app.amtadminapp.model.response.placeList
import com.app.amtadminapp.retrofit.ApiUtils
import com.app.amtadminapp.utils.AppConstant
import com.app.amtadminapp.utils.CommonUtil
import com.app.amtadminapp.utils.PrefConstants
import com.app.amtadminapp.utils.SharedPreference
import com.app.amtadminapp.utils.getRequestJSONBody
import com.app.amtadminapp.utils.gone
import com.app.amtadminapp.utils.hideKeyboard
import com.app.amtadminapp.utils.isConnectivityAvailable
import com.app.amtadminapp.utils.loadURIRoundedCorner
import com.app.amtadminapp.utils.loadUrlRoundedCorner
import com.app.amtadminapp.utils.preventTwoClick
import com.app.amtadminapp.utils.toast
import com.app.amtadminapp.utils.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_add_hotels_vouchers.edtTourBookingNo
import kotlinx.android.synthetic.main.activity_edit_hotels_vouchers.tbTvTitle
import kotlinx.android.synthetic.main.activity_edit_hotels_vouchers.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

class EditHotelsVouchersActivity : BaseActivity(),  View.OnClickListener,
    RecyclerClickListener, EasyPermissions.PermissionCallbacks {

    var sharedPreference: SharedPreference? = null
    var state: String = ""
    var ID : Int = 0
    var TBNO : String = ""
    var arrayListPlaceType: ArrayList<placeList> = ArrayList()

    var arrayListCity: ArrayList<PlaceInfo>? = ArrayList()

    var ImagePaths: ArrayList<Uri> = ArrayList()
    private var ImageUri: Uri? = null

    lateinit var select_image : ImageView
    lateinit var select_pdf : TextView
    lateinit var ll_Pdf : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_hotels_vouchers)
        getIntentData()
        initializeView()
    }

    private fun getIntentData() {
        state = intent.getStringExtra("state").toString()
        if(state.equals("edit")) {
            ID = intent.getIntExtra("ID",0)
            TBNO = intent.getStringExtra("TourBookingNos").toString()
            txtTourBookingNo.text = TBNO
            GetAllPlaceTypeAPI()
            GetHotelVoucherFindByID()
        }
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(this@EditHotelsVouchersActivity)
        tbTvTitle.text = "Edit Hotel Voucher"

        rvPlace.setLayoutManager(LinearLayoutManager(this))
        arrayListPlaceType.clear()

        InitiaListner()
    }

    private fun InitiaListner() {
        imgBack.setOnClickListener(this)
    }

    private fun GetAllPlaceTypeAPI() {
        showProgress()

        val jsonObject = JSONObject()
        jsonObject.put("TourBookingNo", TBNO)

        val call = ApiUtils.apiInterface2.getTourBookingDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<TourBookingDetailsResponse> {
            override fun onResponse(call: Call<TourBookingDetailsResponse>, response: Response<TourBookingDetailsResponse>) {

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
                        if(Info.TourName != null && Info.TourName != "") {
                            txtTourName.text = Info.TourName
                        }
                        if(Info.TourDate != null && Info.TourDate != "") {
                            txtTourDate.text = Info.TourDate
                        }
                        if(Info.RoomType != null && Info.RoomType != "") {
                            txtRoomType.text = Info.RoomType
                        }

                        arrayListCity = Info.places!!

                    } else {
                        hideProgress()
                        llTBNOInfo.gone()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<TourBookingDetailsResponse>, t: Throwable) {
                hideProgress()
                llTBNOInfo.gone()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun GetHotelVoucherFindByID() {
        showProgress()

        val jsonObject = JSONObject()
        jsonObject.put("ID", ID)

        val call = ApiUtils.apiInterface2.getHotelVoucherFindByID(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<HotelVoucherFindByIDResponse> {
            override fun onResponse(call: Call<HotelVoucherFindByIDResponse>, response: Response<HotelVoucherFindByIDResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        hideProgress()
                        val Info = response.body()?.Data!!
                        arrayListPlaceType = Info.placeList!!
                        LL_PaxInfo.visible()

                        val itemAdapter = HotelPlaceAdapter(this@EditHotelsVouchersActivity, arrayListPlaceType!!,this@EditHotelsVouchersActivity)
                        rvPlace.adapter = itemAdapter

                    } else {
                        hideProgress()
                        LL_PaxInfo.gone()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<HotelVoucherFindByIDResponse>, t: Throwable) {
                hideProgress()
                LL_PaxInfo.gone()
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

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        hideKeyboard(applicationContext, view)
        when (view.id)
        {
            R.id.tbEDIT -> {
                preventTwoClick(view)
                selectDialog(this , arrayListPlaceType[position].CityID!! , arrayListPlaceType[position].CityName!! ,
                    arrayListPlaceType[position].Document!!, arrayListPlaceType[position].HotelVoucherImage!!, arrayListPlaceType[position].ID!!)
            }
            R.id.tbDELETE -> {
                preventTwoClick(view)
            }
        }
    }

    private fun selectDialog(mcontext: Context, Cityid : String , Cityname : String , documentname: String, document : String , id : Int) {

        var CityID = Cityid
        var CityName = Cityname

        val dialogMember = Dialog(mcontext)
        dialogMember.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = LayoutInflater.from(mcontext).inflate(R.layout.dialog_edit_hotel_voucher_city, null)
        dialogMember.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogMember.window!!.attributes)

        dialogMember.window!!.attributes = lp
        dialogMember.setCancelable(true)
        dialogMember.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogMember.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogMember.window!!.setGravity(Gravity.CENTER)


        val etCity = dialogMember.findViewById(R.id.etCity) as EditText
        val etUploadDocument = dialogMember.findViewById(R.id.etUploadDocument) as EditText
        select_image = dialogMember.findViewById(R.id.select_image) as ImageView
        ll_Pdf = dialogMember.findViewById(R.id.ll_Pdf) as LinearLayout
        select_pdf = dialogMember.findViewById(R.id.select_pdf) as TextView

        etCity.setText(CityName)

        if(document != "") {
            if(document.contains(".pdf")) {
                select_image.gone()
                ll_Pdf.visible()
                select_pdf.text = documentname
            } else {
                select_image.visible()
                ll_Pdf.gone()
                select_image.loadUrlRoundedCorner(
                    document,
                    R.drawable.ic_image, 1
                )
            }
        }

        val ImgClose = dialogMember.findViewById(R.id.ImgClose) as ImageView
        val CardSave = dialogMember.findViewById(R.id.CardSave) as CardView

        ImgClose.setOnClickListener {
            dialogMember.dismiss()
        }

        etCity.setOnClickListener {
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

            val itemAdapter = DialogPlaceAdapter(this, arrayListCity!!)
            itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                override fun onItemClickEvent(v:View, pos: Int, flag: Int) {

                    CityName = arrayListCity!![pos].City!!
                    CityID = arrayListCity!![pos].CityID.toString()!!

                    Log.e("CITY ID","===>"+CityID)
                    Log.e("CITY NAME ","===>"+CityName)
                    etCity.setText(CityName)

                    dialogSelectPlace!!.dismiss()
                }
            })
            rvDialogCustomer.adapter = itemAdapter

            edtSearchCustomer.gone()
            dialogSelectPlace!!.show()
        }

        etUploadDocument.setOnClickListener {
            preventTwoClick(it)
            if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                    val bottomSheetDialog = BottomSheetDialog(this,R.style.SheetDialog)
                    bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)

                    val Select_Image = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Image)
                    val Select_Doc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Doc)
                    val Select_WordDoc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_WordDoc)

                    Select_Image!!.setOnClickListener {
                        FilePickerBuilder.instance
                            .setMaxCount(1) //optional
                            .setSelectedFiles(ImagePaths) //optional
                            .setActivityTheme(R.style.LibAppTheme) //optional
                            .pickPhoto(this, 111);
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

        CardSave.setOnClickListener {

            showProgress()

            if (sharedPreference == null) {
                sharedPreference = SharedPreference(this)
            }
            val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!

            val partsList: java.util.ArrayList<MultipartBody.Part> = java.util.ArrayList()

            if (ImageUri != null) {
                if(ImageUri.toString().contains(".pdf")) {
                    partsList.add(CommonUtil.prepareFilePart(this, "*/*", "HotelVoucherImage", ImageUri!!))
                } else {
                    partsList.add(CommonUtil.prepareFilePart(this, "image/*", "HotelVoucherImage", ImageUri!!))
                }
            } else {
                val attachmentEmpty: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
                partsList.add(MultipartBody.Part.createFormData("HotelVoucherImage", "", attachmentEmpty))
            }

            val mCreatedBy = CommonUtil.createPartFromString(CreatedBy)
            val mID = CommonUtil.createPartFromString(id.toString())
            val mCityid = CommonUtil.createPartFromString(CityID)

            var call = ApiUtils.apiInterface2.UpdateHotelVoucher(
                mCreatedBy,
                mID,
                mCityid,
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

                            dialogMember.dismiss()
                            GetHotelVoucherFindByID()

                        } else {
                            hideProgress()
                            toast(response.body()?.message.toString(), AppConstant.TOAST_SHORT)
                        }
                    }
                }
            })
        }

        dialogMember!!.show()
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
                    toast(getString(R.string.str_msg_no_internet), Toast.LENGTH_LONG)
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

}