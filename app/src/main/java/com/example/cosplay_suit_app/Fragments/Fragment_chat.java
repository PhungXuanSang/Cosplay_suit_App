package com.example.cosplay_suit_app.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.AdapterListChat;
import com.example.cosplay_suit_app.DTO.ChatDTO;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.Interface_retrofit.UserInterface;
import com.example.cosplay_suit_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment_chat extends Fragment {
    public Fragment_chat() {
    }

    View view;
    AdapterListChat adapter;
    ArrayList<User> list;
    ArrayList<String> listIdUser;
    FirebaseDatabase database;
    String idUser;
    User user;
    static String url = API.URL;
    static final String BASE_URL = url + "/user/api/";
    RecyclerView rcv_listChat;
    TextView tv_null;

    public static Fragment_chat newInstance() {
        Fragment_chat fragmentChat = new Fragment_chat();
        return fragmentChat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        database = FirebaseDatabase.getInstance();
        rcv_listChat = view.findViewById(R.id.rcv_listChat);
        tv_null = view.findViewById(R.id.tv_null);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);

        idUser = sharedPreferences.getString("id", "");
        Log.d("DEBUG", "idUserCur: "+idUser);
        user = new User();
        list = new ArrayList<>();
        listIdUser = new ArrayList<>();
        adapter = new AdapterListChat(getContext(),list);
        rcv_listChat.setAdapter(adapter);

//        getList();

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getList() {
        list.clear();
        DatabaseReference userRoomsReference = database.getReference().child("chats");

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserInterface userInterface = retrofit.create(UserInterface.class);

        userRoomsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.d("DEBUG", "No rooms found for user.");
                    checkAndUpdateUI();
                    return;
                }else {
                    checkAndUpdateUI();
                }
                list.clear();
                for (DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                    String combinedUserID = roomSnapshot.getKey();
                    String[] ids = combinedUserID.split("_");

                    if (ids.length != 2) {
                        continue;
                    }
                    if (!idUser.equals(ids[0])) {
                        continue;
                    }
                    String receiverId = ids[1];

                    DatabaseReference roomLastMessageRef = database.getReference().child("chats").child(combinedUserID).child("messages");
                    roomLastMessageRef.orderByChild("timeStamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot messageSnapshot) {
                            if (!messageSnapshot.exists()) {
                                return;
                            }
                            for (DataSnapshot lastMessageSnapshot : messageSnapshot.getChildren()) {
                                ChatDTO lastMessage = lastMessageSnapshot.getValue(ChatDTO.class);
                                if (lastMessage != null) {


                                    Call<User> objCall = userInterface.findUser(receiverId);
                                    objCall.enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> call, Response<User> response) {
                                            if (response.isSuccessful()) {
                                                User fetchedUser = response.body();
                                                if (fetchedUser != null) {
                                                    fetchedUser.setLastMess(lastMessage.getMessage());
                                                    fetchedUser.setTime(lastMessage.getTime());
                                                    list.add(0, fetchedUser);
                                                    Log.d("DEBUG", "onDataChange: " + fetchedUser.getFullname());
                                                    Log.d("DEBUG", "onResponse: "+receiverId);
                                                    adapter.notifyDataSetChanged();
                                                    rcv_listChat.scrollToPosition(0);
                                                    checkAndUpdateUI();
                                                }
                                            } else {
                                                Toast.makeText(getContext(), "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<User> call, Throwable t) {
                                            Log.d("chuongdk", "onFailure: " + t.getLocalizedMessage());
                                        }
                                    });
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("DEBUG", "Error fetching messages for room " + combinedUserID + ": " + databaseError.getMessage());
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DEBUG", "Error fetching rooms: " + databaseError.getMessage());
            }
        });

    }
    private void checkAndUpdateUI() {
        if(list.isEmpty()) {
            tv_null.setVisibility(View.VISIBLE);
            rcv_listChat.setVisibility(View.GONE);
        } else {
            tv_null.setVisibility(View.GONE);
            rcv_listChat.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getList();
    }

}
