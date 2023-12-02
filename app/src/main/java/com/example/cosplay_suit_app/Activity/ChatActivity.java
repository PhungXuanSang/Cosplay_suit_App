package com.example.cosplay_suit_app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.AdapterChat;
import com.example.cosplay_suit_app.DTO.ChatDTO;
import com.example.cosplay_suit_app.DTO.FcmMessage;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.Interface_retrofit.FcmApiService;
import com.example.cosplay_suit_app.Interface_retrofit.UserInterface;
import com.example.cosplay_suit_app.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    ImageView img_back;
    CardView btn_send;
    EditText ed_chat;
    FirebaseDatabase database;

    RecyclerView rcv_chat;
    private ChildEventListener chatEventListener;
    DatabaseReference chatreference;
    AdapterChat adapter;
    ArrayList<ChatDTO> list;

    String senderRoom,reciverRoom,SenderUID;
    String reciverUid,idU;
    SharedPreferences sharedPreferences;
    TextView tv_nameShop;
    static String url = API.URL;
    static final String BASE_URL = url + "/user/api/";
    private Uri filePath; // đường dẫn file
    // khai báo request code để chọn ảnh
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseStorage storage;

    private StorageReference storageReference;
    private String link_anh;
    ImageView img_addImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();
        database = FirebaseDatabase.getInstance();
        sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);
        FirebaseApp.initializeApp(this);
        storageReference = FirebaseStorage.getInstance("gs://duantotnghiepcosplaysuit.appspot.com").getReference();


        idU = sharedPreferences.getString("id", "");
        SenderUID =  idU;
        reciverUid = getIntent().getStringExtra("idShop");
        setNameReciver();
        Log.d("DEBUG", "onCreate: "+reciverUid);
        senderRoom = SenderUID+"_"+reciverUid;
        reciverRoom = reciverUid+"_"+SenderUID;
        list = new ArrayList<>();
        adapter = new AdapterChat(this,list,senderRoom,reciverRoom);
        getListChat();
        rcv_chat.setAdapter(adapter);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = ed_chat.getText().toString().trim();

                if (message.isEmpty()) {

                    Toast.makeText(ChatActivity.this, "Enter The Message or Select Image First", Toast.LENGTH_SHORT).show();
                    return;
                }


                addMessage();
                sendNotification(reciverUid, message);
            }
        });
        img_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
    }



    private void initView() {
        img_back = findViewById(R.id.img_backChat);
        btn_send = findViewById(R.id.sendbtnn);
        ed_chat = findViewById(R.id.ed_msg);
        rcv_chat = findViewById(R.id.rcv_chat);
        tv_nameShop = findViewById(R.id.tv_nameShop);
        img_addImage = findViewById(R.id.img_addImage);
    }
    public void sendFcmData(String fcmToken, String content) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FcmApiService apiService = retrofit.create(FcmApiService.class);

        FcmMessage message = new FcmMessage();
        message.setTo(fcmToken);

        Map<String, String> data = new HashMap<>();
        data.put("title", "Bạn có tin nhắn mới");
        data.put("message", content);
        Log.d("Chat", "sendFcmData: "+content);
        message.setData(data);

        Call<ResponseBody> call = apiService.sendFcmMessage(message);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Log.d("ChatActivity", "Gửi thông báo FCM thành công");
                } else {

                    Log.e("ChatActivity", "Gửi thông báo FCM thất bại");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ChatActivity", "Lỗi khi gửi thông báo FCM: " + t.getMessage());
            }
        });
    }

    private void sendNotification(String recipientUID, String message) {

        DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference("userTokens").child(recipientUID);
        tokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String recipientToken = dataSnapshot.getValue(String.class);
                if (recipientToken != null) {

                    sendFcmData(recipientToken, message);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ChatActivity", "Lỗi khi lấy mã FCM của người nhận: " + databaseError.getMessage());
            }
        });
    }
    private void setNameReciver() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<User> objCall = userInterface.findUser(reciverUid);
        objCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User fetchedUser = response.body();
                    if (fetchedUser != null) {
                        tv_nameShop.setText(fetchedUser.getFullname());
                    }
                } else {
                    Toast.makeText(ChatActivity.this, "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("chuongdk", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getListChat() {
        chatreference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                ChatDTO newMessage = dataSnapshot.getValue(ChatDTO.class);
                list.add(newMessage);
                adapter.notifyDataSetChanged();



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                ChatDTO deletedMessage = dataSnapshot.getValue(ChatDTO.class);
                if (deletedMessage != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId().equals(deletedMessage.getId())) {
                            list.remove(i);
                            adapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ChatActivity", "Failed to read chats.", databaseError.toException());
            }
        };


        chatreference.addChildEventListener(chatEventListener);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatreference != null && chatEventListener != null) {
            chatreference.removeEventListener(chatEventListener);
        }
    }
    private void addMessage() {
        String message = ed_chat.getText().toString();
        ed_chat.setText("");

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        String formattedTime = timeFormat.format(calendar.getTime());

        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("chats")
                .child(senderRoom)
                .child("messages");
        String id = databaseReference.push().getKey();

        Log.d("Chat", "addMessage: Image URL = " + link_anh);

        ChatDTO messagess = new ChatDTO(id, message, SenderUID, date.getTime(), formattedTime, link_anh);

        databaseReference.child(id).setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                database.getReference().child("chats")
                        .child(reciverRoom)
                        .child("messages")
                        .child(id).setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Cập nhật UI hoặc thực hiện các công việc khác sau khi gửi tin nhắn
                            }
                        });
            }
        });
    }

    private void SelectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Lấy dữ liệu từ màn hình chọn ảnh truyền về
            filePath = data.getData();
            Log.d("zzzzz", "onActivityResult: " + filePath.toString());
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                if (filePath != null) {

                    // Hiển thị dialog
                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    // Tạo đường dẫn lưu trữ file, images/ là 1 thư mục trên firebase, chuỗi uuid... là tên file, tạm thời có thể phải lên web firebase tạo sẵn thư mục images
                    StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                    Log.d("chat", "uploadImage: " + ref.getPath());

                    // Tiến hành upload file
                    ref.putFile(filePath)
                            .addOnSuccessListener(
                                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // upload thành công, tắt dialog


                                            progressDialog.dismiss();



                                        }
                                    })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace(); // có lỗi upload
                                    progressDialog.dismiss();
                                    Toast.makeText(ChatActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(
                                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            // cập nhật tiến trình upload
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                        }
                                    })
                            .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    // gọi task để lấy URL sau khi upload thành công
                                    return ref.getDownloadUrl();
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        // upload thành công, lấy được url ảnh, ghi ra log. Bạn có thể ghi vào CSdl....
                                        link_anh = downloadUri.toString();
                                        Log.d("Chat", "onComplete: url download = " + downloadUri.toString());
                                        addMessage();
                                    } else {
                                        // lỗi lấy url download
                                    }
                                }
                            });
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }
}