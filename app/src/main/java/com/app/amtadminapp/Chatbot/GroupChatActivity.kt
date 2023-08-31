package com.app.amtadminapp.Chatbot

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtadminapp.Chatbot.Nofification.NotificationData
import com.app.amtadminapp.Chatbot.Nofification.PushGroupNotification
import com.app.amtadminapp.Chatbot.Nofification.PushNotification
import com.app.amtadminapp.R
import com.app.amtadminapp.activity.StartActivity
import com.app.amtadminapp.retrofit.ApiUtils.getInstance
import com.app.amtadminapp.utils.isConnectivityAvailable
import com.app.amtadminapp.utils.preventTwoClick
import com.app.amtadminapp.utils.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class GroupChatActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private var imgBack: ImageView? = null
    private var pro_pics: CircleImageView? = null
    private var tbTvTitle: TextView? = null

    private lateinit var GroupMemberList: ArrayList<String>
    private lateinit var GroupMessageKeyRef: DatabaseReference
    private lateinit var GroupMemberInfoList: ArrayList<GroupMemberList>

    private var SendMessageInput: ImageView? = null
    private  var SendFileBtn: ImageView? = null
    private var userMessageInput: EditText? = null

    private var mAuth: FirebaseAuth? = null
    private var RootRef: DatabaseReference? = null
    private var GroupNameRef: DatabaseReference? = null

    private val messagesList: ArrayList<GroupMessages?> = ArrayList()
    private var messageAdapter: GroupMessageAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var usersMessagesList: RecyclerView? = null

    private var checker = ""
    private  var myUrl:String? = ""
    private var fileUri: Uri? = null
    private var uploadTask: StorageTask<*>? = null

    var ImagePaths = ArrayList<Uri>()

    private var currentGroupName: String = ""
    private var currentUsetID:String = ""
    private var currentUserName:String = ""
    private var currentDate:String = ""
    private var currentTime:String = ""

    private var loadingBar: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Comment By AI005 app does't allow capture screen shot
        //window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_cb_conversation)

        loadingBar = ProgressDialog(this)
        currentGroupName = intent.getStringExtra("GroupName").toString()

        mAuth = FirebaseAuth.getInstance()
        currentUsetID = mAuth!!.currentUser!!.uid

        RootRef = FirebaseDatabase.getInstance().reference
        GroupNameRef = FirebaseDatabase.getInstance().reference.child(ChatConstant.F_GROUP).child(currentGroupName).child(ChatConstant.F_GROUP_MESSAGE)

        GetUsetInfo()
        setToolBar(currentGroupName)
        fetchMemberDetails(currentGroupName)

        userMessageInput!!.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                SendMessageInput!!.performClick()
                true
            } else {
                false
            }
        }

        SendMessageInput!!.setOnClickListener {
            SaveMessageInfoToDatabase()
            userMessageInput!!.setText("")
        }

        SendFileBtn!!.setOnClickListener {
            preventTwoClick(it)
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

        GetMessages()
    }

    private fun GetMessages() {
        messagesList.clear()
        GroupNameRef?.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                    val messages = snapshot.getValue(GroupMessages::class.java)!!

                    if (!messagesList.contains(messages)) {
                        messagesList.add(messages)
                        messageAdapter!!.notifyDataSetChanged()
                    }
                    usersMessagesList!!.smoothScrollToPosition(usersMessagesList!!.adapter!!.itemCount)

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun setToolBar(name: String) {

        imgBack = findViewById(R.id.imgBack)
        imgBack!!.setOnClickListener {
            onBackPressed()
        }

        tbTvTitle = findViewById(R.id.tbTvTitle)
        tbTvTitle!!.setText(name)
        tbTvTitle!!.setOnClickListener {
            if(GroupMemberInfoList.size > 0) {
                selectGroupDialog()
            }
        }

        pro_pics = findViewById(R.id.pro_pics)
        pro_pics!!.setVisibility(View.GONE)

        SendMessageInput = findViewById(R.id.send_message_btn)
        SendFileBtn = findViewById(R.id.send_file_btn)

        userMessageInput = findViewById(R.id.input_message)
        usersMessagesList = findViewById(R.id.message_list_of_users)

        messageAdapter = GroupMessageAdapter(messagesList)
        linearLayoutManager = LinearLayoutManager(this)
        usersMessagesList!!.setLayoutManager(linearLayoutManager)
        usersMessagesList!!.setAdapter(messageAdapter)
    }

    private fun fetchMemberDetails(groupname: String) {

        GroupMemberList = ArrayList()
        GroupMemberInfoList = ArrayList()

        val rootRef = FirebaseDatabase.getInstance().reference
        val usersRef = rootRef.child("Groups")

        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                for (snap in dataSnapshot.children) {
                    if (snap.key!! == groupname) {
                         usersRef.child (snap.key!!).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                                for (snap1 in dataSnapshot.children) {
                                    GroupMemberList.add(snap1.key!!)
                                    GetGroupInfo(snap1.key!!)
                                }
                            }
                            override fun onCancelled(@NonNull databaseError: DatabaseError) {
                                //enter code here
                            }
                        })
                    }
                }
            }
            override fun onCancelled(@NonNull databaseError: DatabaseError) {
            }
        })
    }

    private fun SaveMessageInfoToDatabase() {
        val message = userMessageInput!!.text.toString()
        val messageKey = GroupNameRef!!.push().key

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Please write message first...", Toast.LENGTH_SHORT).show()
        } else {
            val calForDate = Calendar.getInstance()
            val currentDateFormat = SimpleDateFormat("dd/MM/yyyy")
            currentDate = currentDateFormat.format(calForDate.time)

            val calForTime = Calendar.getInstance()
            val currentTimeFormat = SimpleDateFormat("hh:mm a")
            currentTime = currentTimeFormat.format(calForTime.time)

            val groupMessageKey = HashMap<String, Any>()
            GroupNameRef!!.updateChildren(groupMessageKey)

            GroupMessageKeyRef = GroupNameRef!!.child(messageKey!!)

            val messageInfoMap = HashMap<String, Any>()
            messageInfoMap["from"] = currentUsetID
            messageInfoMap["name"] = currentUserName
            messageInfoMap["message"] = message
            messageInfoMap["type"] = "text"
            messageInfoMap["date"] = currentDate
            messageInfoMap["time"] = currentTime

            val temparraylist = ArrayList<String>()
            for (i in 0 until GroupMemberInfoList.size) {
                if(currentUsetID != GroupMemberInfoList[i].Uid!!) {
                    temparraylist.add(GroupMemberInfoList[i].Uid!!)
                }
            }
            val unreaduid = temparraylist.toString().replace("[", "").replace("]", "")

            messageInfoMap["unreaduid"] = unreaduid

            GroupMessageKeyRef.updateChildren(messageInfoMap).addOnCompleteListener(
                OnCompleteListener {
                    val arraylist = ArrayList<String>()
                    for (i in 0 until GroupMemberInfoList.size) {
                        if(currentUsetID != GroupMemberInfoList[i].Uid!!) {
                            arraylist.add(GroupMemberInfoList[i].Device_Token!!)
                        }
                    }

                    val calForDate = Calendar.getInstance()
                    val currentDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                    val currentDateTime = currentDateFormat.format(calForDate.time)

                    RootRef!!.child(ChatConstant.F_GROUP).child(currentGroupName).child("ISLatest").setValue(currentDateTime)

                    sendFCMPush(arraylist,message)
                })
        }
    }

    private fun sendFCMPush(devicetoken: ArrayList<String>, msg: String) {

        val msg1 = currentUserName + " : " + msg
        val notificationdata = NotificationData(currentGroupName, msg1)
        val pushNotification = PushGroupNotification(notificationdata,"create","appUser-testUser",
            devicetoken)
        getInstance().sendGroupNotification(pushNotification)
            .enqueue(object : Callback<PushNotification?> {
                override fun onResponse(
                    call: Call<PushNotification?>,
                    response: Response<PushNotification?>
                ) {
                }

                override fun onFailure(call: Call<PushNotification?>, t: Throwable) {
                    Toast.makeText(
                        this@GroupChatActivity,
                        "Something went wrong.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun GetUsetInfo() {
        RootRef?.child(ChatConstant.F_EMPLOYEE)?.child(currentUsetID)?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    currentUserName = snapshot.child("name").value.toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun GetGroupInfo(Uid : String) {
        RootRef?.child(ChatConstant.F_CUSTOMER)?.child(Uid)?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val uid = snapshot.child("Uid").value.toString()
                    val name = snapshot.child("name").value.toString()
                    val device_token = snapshot.child("device_token").value.toString()
                    val mobile = snapshot.child("mobile").value.toString()
                    GroupMemberInfoList.add(GroupMemberList(name,uid,device_token,mobile))
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
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

    private fun showBottomSheetDialogGST() {
        val bottomSheetDialog = BottomSheetDialog(this,R.style.SheetDialog)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)

        val Select_Image = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Image)
        val Select_Doc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_Doc)
        val Select_WordDoc = bottomSheetDialog.findViewById<LinearLayout>(R.id.Select_WordDoc)

        Select_Image!!.setOnClickListener {
            checker = "image"
            photopickerGST()
            bottomSheetDialog.dismiss()
        }
        Select_Doc!!.setOnClickListener {
            checker = "pdf"
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, 104)
            bottomSheetDialog.dismiss()
        }
        Select_WordDoc!!.setOnClickListener {
            checker = "docx"
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/msword"
            startActivityForResult(Intent.createChooser(intent, "Select Ms Word File"), 438)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun photopickerGST() {
        FilePickerBuilder.instance
            .setMaxCount(1) //optional
            .setSelectedFiles(ImagePaths) //optional
            .setActivityTheme(R.style.LibAppTheme) //optional
            .pickPhoto(this, 111);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Image
            111 -> if (resultCode == RESULT_OK && data != null) {
                ImagePaths = ArrayList()
                ImagePaths.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)!!)
                val PassportPath = ImagePaths[0]
                CropImage.activity(PassportPath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri!!
                    if (isConnectivityAvailable(this)) {
                        fileUri = Uri.fromFile(File(resultUri.path))
                        UploadFileOnFCM()
                    } else {
                        toast(getString(R.string.str_msg_no_internet),Toast.LENGTH_LONG)
                    }
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                    toast("No apps can perform this action.", Toast.LENGTH_LONG)
                }
            }
            // PDF File
            104 -> if (resultCode == RESULT_OK && data != null) {
                val sUri = data!!.data
                val sPath = sUri!!.path
                fileUri = Uri.fromFile(fileFromContentUri4("passport", applicationContext, sUri))
                UploadFileOnFCM()
            }
            //Ms Word File
            438 -> {
                val sUri = data!!.data
                val sPath = sUri!!.path
                fileUri = Uri.fromFile(fileFromContentUri4("passport", applicationContext, sUri))
                UploadFileOnFCM()
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

    private fun UploadFileOnFCM() {

        loadingBar!!.setTitle("Sending File")
        loadingBar!!.setMessage("Please wait, we are sending file...")
        loadingBar!!.setCanceledOnTouchOutside(false)
        loadingBar!!.show()

        val messageKey = GroupNameRef!!.push().key

        val storageReference = FirebaseStorage.getInstance().reference.child("Group Documents Files")
        val calForDate = Calendar.getInstance()
        val currentDateFormat = SimpleDateFormat("dd/MM/yyyy")
        currentDate = currentDateFormat.format(calForDate.time)

        val calForTime = Calendar.getInstance()
        val currentTimeFormat = SimpleDateFormat("hh:mm a")
        currentTime = currentTimeFormat.format(calForTime.time)

        val groupMessageKey = HashMap<String, Any>()
        GroupNameRef!!.updateChildren(groupMessageKey)

        GroupMessageKeyRef = GroupNameRef!!.child(messageKey!!)

        val filePath = storageReference.child(messageKey)

        uploadTask = filePath.putFile(fileUri!!)
        uploadTask!!.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            filePath.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result
                myUrl = downloadUrl.toString()

                val messageInfoMap = HashMap<String, Any>()
                messageInfoMap["from"] = currentUsetID
                messageInfoMap["name"] = currentUserName
                messageInfoMap["message"] = myUrl!!
                messageInfoMap["type"] = checker
                messageInfoMap["date"] = currentDate
                messageInfoMap["time"] = currentTime

                val temparraylist = ArrayList<String>()
                for (i in 0 until GroupMemberInfoList.size) {
                    if(currentUsetID != GroupMemberInfoList[i].Uid!!) {
                        temparraylist.add(GroupMemberInfoList[i].Uid!!)
                    }
                }
                val unreaduid = temparraylist.toString().replace("[", "").replace("]", "")
                messageInfoMap["unreaduid"] = unreaduid

                GroupMessageKeyRef.updateChildren(messageInfoMap)
                loadingBar!!.dismiss()

                val arraylist = ArrayList<String>()
                for (i in 0 until GroupMemberInfoList.size) {
                    if(currentUsetID != GroupMemberInfoList[i].Uid!!) {
                        arraylist.add(GroupMemberInfoList[i].Device_Token!!)
                    }
                }

                val calForDate = Calendar.getInstance()
                val currentDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val currentDateTime = currentDateFormat.format(calForDate.time)

                RootRef!!.child(ChatConstant.F_GROUP).child(currentGroupName).child("ISLatest").setValue(currentDateTime)


                sendFCMPush(arraylist,"New " + checker + " Received")
            } else {
                loadingBar!!.dismiss()
            }
        }


    }

    private fun selectGroupDialog() {
        var dialogSelectCity = Dialog(this)
        dialogSelectCity.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_group, null)
        dialogSelectCity.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectCity.window!!.attributes)

        dialogSelectCity.window!!.attributes = lp
        dialogSelectCity.setCancelable(true)
        dialogSelectCity.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectCity.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectCity.window!!.setGravity(Gravity.CENTER)

        val rvDialogCustomer = dialogSelectCity.findViewById(R.id.rvGroupMember) as RecyclerView

        val itemAdapter = DialogGroupListAdapter(this, GroupMemberInfoList!!)
        rvDialogCustomer.adapter = itemAdapter

        dialogSelectCity!!.show()
    }

    override fun onBackPressed() {
        val chatIntent = Intent(this, ChatBoatActivity::class.java)
        startActivity(chatIntent)
        finish()
    }
}
