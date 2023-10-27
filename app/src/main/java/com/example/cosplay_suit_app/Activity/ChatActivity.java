package com.example.cosplay_suit_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.AdapterChat;
import com.example.cosplay_suit_app.DTO.ChatDTO;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.Interface_retrofit.UserInterface;
import com.example.cosplay_suit_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();
        database = FirebaseDatabase.getInstance();
        sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);

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
                addMessage();
            }
        });
    }



    private void initView() {
        img_back = findViewById(R.id.img_backChat);
        btn_send = findViewById(R.id.sendbtnn);
        ed_chat = findViewById(R.id.ed_msg);
        rcv_chat = findViewById(R.id.rcv_chat);
        tv_nameShop = findViewById(R.id.tv_nameShop);

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
                Log.d("DEBUG", "onChildAdded: "+newMessage.getMessage());
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
        if (message.isEmpty()){
            Toast.makeText(ChatActivity.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
            return;
        }
        ed_chat.setText("");
        Date date = new Date();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a",Locale.ENGLISH);
        String formattedTime = timeFormat.format(calendar.getTime());

        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("chats")
                .child(senderRoom)
                .child("messages");
        String id = databaseReference.push().getKey();
        ChatDTO messagess = new ChatDTO(id, message, SenderUID, date.getTime(), formattedTime);

        databaseReference.child(id).setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                database.getReference().child("chats")
                        .child(reciverRoom)
                        .child("messages")
                        .child(id).setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });



            }
        });

    }
}