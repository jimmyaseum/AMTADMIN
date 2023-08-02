package com.app.amtadminapp.Chatbot;

import static com.app.amtadminapp.utils.ExtensionsKt.isConnectivityAvailable;
import static com.app.amtadminapp.utils.ExtensionsKt.preventTwoClick;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.amtadminapp.Chatbot.Nofification.NotificationData;
import com.app.amtadminapp.Chatbot.Nofification.PushNotification;
import com.app.amtadminapp.R;
import com.app.amtadminapp.retrofit.ApiUtils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private ImageView imgBack;
    private CircleImageView pro_pics;
    private TextView tbTvTitle;

    private String messageReceiverID, messageReceiverName, messageReceiverImage, messageReceiverDeviceToken, currentUsetID, currentUserName;

    private ImageView SendMessageInput, SendFileBtn;
    private EditText userMessageInput;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private final List<Messages> messagesList = new ArrayList();
    private LinearLayoutManager linearLayoutManager;
    private ChatMessageAdapter messageAdapter;
    private RecyclerView usersMessagesList;

    private String saveCurrentTime, saveCurrentDate;
    private String checker = "", myUrl = "";
    private Uri fileUri;
    private StorageTask uploadTask;
    ArrayList<Uri> ImagePaths = new ArrayList();
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cb_conversation);

        mAuth = FirebaseAuth.getInstance();
        currentUsetID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        GetCurrentUserInfo();

        messageReceiverID = getIntent().getExtras().getString("visit_user_id").toString();
        messageReceiverName = getIntent().getExtras().getString("visit_user_name").toString();
        messageReceiverImage = getIntent().getExtras().getString("visit_user_image").toString();
        messageReceiverDeviceToken = getIntent().getExtras().getString("visit_user_device_token").toString();

        IntializeController();

        userMessageInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    SendMessageInput.performClick();
                    return true;
                } else {
                    return false;
                }
            }
        });

        SendMessageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });

        SendFileBtn.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preventTwoClick(view);
                if (EasyPermissions.hasPermissions(ConversationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (EasyPermissions.hasPermissions(ConversationActivity.this, Manifest.permission.CAMERA)) {
                        showBottomSheetDialogGST();
                    } else {
                        EasyPermissions.requestPermissions(
                                ConversationActivity.this,
                                getString(R.string.msg_permission_camera),
                                900,
                                Manifest.permission.CAMERA
                        );
                    }
                } else {
                    EasyPermissions.requestPermissions(
                            ConversationActivity.this,
                            getString(R.string.msg_permission_storage),
                            900,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    );
                }
            }
        });

        GetMessages();
    }

    private void GetMessages() {
        messagesList.clear();
        RootRef.child("Messages").child(currentUsetID).child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        Messages messages = snapshot.getValue(Messages.class);
                        if (!messagesList.contains(messages)) {
                            messagesList.add(messages);
                            messageAdapter.notifyDataSetChanged();
                        }
                        usersMessagesList.smoothScrollToPosition(usersMessagesList.getAdapter().getItemCount());

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void IntializeController() {

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadingBar = new ProgressDialog(this);

        pro_pics = findViewById(R.id.pro_pics);

        tbTvTitle = findViewById(R.id.tbTvTitle);
        tbTvTitle.setText(messageReceiverName);

        if (!messageReceiverImage.equals("")) {
            Picasso.get().load(messageReceiverImage).placeholder(R.drawable.ic_profile).into(pro_pics);
            pro_pics.setVisibility(View.VISIBLE);
        } else {
            pro_pics.setVisibility(View.GONE);
        }

        SendMessageInput = findViewById(R.id.send_message_btn);
        SendFileBtn = findViewById(R.id.send_file_btn);

        userMessageInput = findViewById(R.id.input_message);
        usersMessagesList = findViewById(R.id.message_list_of_users);

        messageAdapter = new ChatMessageAdapter(messagesList);
        linearLayoutManager = new LinearLayoutManager(this);
        usersMessagesList.setLayoutManager(linearLayoutManager);
        usersMessagesList.setAdapter(messageAdapter);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy"); // "MMM dd, yyyy"
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

    }

    private void SendMessage() {
        final String messageText = userMessageInput.getText().toString();

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "Please write message first...", Toast.LENGTH_SHORT).show();
        } else {
            String messageSenderRef = "Messages/" + currentUsetID + "/" + messageReceiverID;
            String messageReceiverRef = "Messages/" + messageReceiverID + "/" + currentUsetID;

            DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                    .child(currentUsetID).child(messageReceiverID).push();

            String messagePushID = userMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", currentUsetID);
            messageTextBody.put("to", messageReceiverID);
            messageTextBody.put("messageID", messagePushID);
            messageTextBody.put("time", saveCurrentTime);
            messageTextBody.put("date", saveCurrentDate);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + messagePushID, messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        GetUsetInfo(messageText);

                    } else {
                        Toast.makeText(ConversationActivity.this, "Message Not Sent ", Toast.LENGTH_SHORT).show();
                    }
                    userMessageInput.setText("");
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser != null) {
            updateUserStatus("open");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser != null) {
            updateUserStatus("close");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser != null) {
            updateUserStatus("close");
        }
    }

    protected void updateUserStatus(String screenstate) {

        Map messageTextBody = new HashMap();
        messageTextBody.put("screenstate", screenstate);

        String currentuserId = mAuth.getCurrentUser().getUid();
        RootRef.child(ChatConstant.F_EMPLOYEE).child(currentuserId).updateChildren(messageTextBody);
    }

    private void sendFCMPush(String msg) {

        NotificationData notificationdata = new NotificationData(currentUserName , msg);
        PushNotification pushNotification = new PushNotification(notificationdata , messageReceiverDeviceToken);

        ApiUtils.INSTANCE.getInstance().sendNotification(pushNotification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(ConversationActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void showBottomSheetDialogGST() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,R.style.SheetDialog);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        LinearLayout Select_Image = bottomSheetDialog.findViewById(R.id.Select_Image);
        LinearLayout Select_Doc = bottomSheetDialog.findViewById(R.id.Select_Doc);
        LinearLayout Select_WordDoc = bottomSheetDialog.findViewById(R.id.Select_WordDoc);

        Select_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "image";
                photopickerGST();
                bottomSheetDialog.dismiss();
            }
        });

        Select_Doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "pdf";
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, 104);
                bottomSheetDialog.dismiss();
            }
        });
        Select_WordDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "docx";
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/msword");
                startActivityForResult(Intent.createChooser(intent, "Select Ms Word File"), 438);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    private void photopickerGST() {
        FilePickerBuilder.getInstance()
                .setMaxCount(1) //optional
                .setSelectedFiles(ImagePaths) //optional
                .setActivityTheme(R.style.LibAppTheme) //optional
                .pickPhoto(this, 111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            UploadImage(data);
        } else */
         if(requestCode == 111) {
            if (resultCode == RESULT_OK && data != null) {
                ImagePaths = new ArrayList();
//                ImagePaths.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                ImagePaths.addAll(data.<Uri>getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                Uri PassportPath = ImagePaths.get(0);
                CropImage.activity(PassportPath)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                if (isConnectivityAvailable(this)) {
                    fileUri = Uri.fromFile(new File(resultUri.getPath()));
                    UploadFileOnFCM();
                } else {
                    Toast.makeText(this, getString(R.string.str_msg_no_internet), Toast.LENGTH_SHORT).show();
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "No apps can perform this action.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == 104) {
            if (resultCode == RESULT_OK && data != null) {
                Uri sUri = data.getData();
                String sPath = sUri.getPath();
                fileUri = Uri.fromFile(fileFromContentUri4("passport", this, sUri));
                UploadFileOnFCM();
            }
        }
        else if(requestCode == 438) {
            if (resultCode == RESULT_OK && data != null) {
                Uri sUri = data.getData();
                String sPath = sUri.getPath();
                fileUri = Uri.fromFile(fileFromContentUri4("passport", getApplicationContext(), sUri));
                UploadFileOnFCM();
            }
        }
    }

    public File fileFromContentUri4(String name, Context context, Uri contentUri) {
        // Preparing Temp file name
         String fileExtension = getFileExtension(context, contentUri);

         String fileName = "";
        if (fileExtension != null){
            fileName = "temp_file_" + name + "." + fileExtension;
        } else {
            fileName = "temp_file_" + name + "";
        }

        // Creating Temp file
        File tempFile = new File(context.getCacheDir(), fileName);
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream oStream = new FileOutputStream(tempFile);
            InputStream inputStream = context.getContentResolver().openInputStream(contentUri);
            copy(inputStream,oStream);
            oStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempFile;
    }

    private String getFileExtension(Context context,Uri uri) {
        String fileType = context.getContentResolver().getType(uri);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType);
    }

    private void copy(InputStream source, OutputStream target) {

        byte[] buf = new byte[8192];
        int length = 0;
//        while (source.read(buf).also { length = it } > 0) {
//            target.write(buf, 0, length)
//        }

        while (true) {
            try {
                if (!((length = source.read(buf)) != -1))
                    target.write(buf, 0, length);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void UploadFileOnFCM() {
        loadingBar.setTitle("Sending File");
        loadingBar.setMessage("Please wait, we are sending file...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Documents Files");

        final String messageSenderRef = "Messages/" + currentUsetID + "/" + messageReceiverID;
        final String messageReceiverRef = "Messages/" + messageReceiverID + "/" + currentUsetID;

        DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                .child(currentUsetID).child(messageReceiverID).push();

        final String messagePushID = userMessageKeyRef.getKey();

        final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");

        uploadTask = filePath.putFile(fileUri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    myUrl = downloadUrl.toString();

                    Map messageTextBody = new HashMap();
                    messageTextBody.put("message", myUrl);
                    messageTextBody.put("name", fileUri.getLastPathSegment());
                    messageTextBody.put("type", checker);
                    messageTextBody.put("from", currentUsetID);
                    messageTextBody.put("to", messageReceiverID);
                    messageTextBody.put("messageID", messagePushID);
                    messageTextBody.put("time", saveCurrentTime);
                    messageTextBody.put("date", saveCurrentDate);

                    Map messageBodyDetails = new HashMap();
                    messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
                    messageBodyDetails.put(messageReceiverRef + "/" + messagePushID, messageTextBody);

                    RootRef.updateChildren(messageBodyDetails);
                    loadingBar.dismiss();

                    GetUsetInfo("New " + checker + " Received");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(ConversationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetUsetInfo(final String msg) {
        RootRef.child(ChatConstant.F_CUSTOMER).child(messageReceiverID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String screenstate = snapshot.child("screenstate").getValue().toString();
                    if(screenstate.equals("close")) {
                        sendFCMPush(msg);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetCurrentUserInfo() {
        RootRef.child(ChatConstant.F_EMPLOYEE).child(currentUsetID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentUserName = snapshot.child("name").getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


