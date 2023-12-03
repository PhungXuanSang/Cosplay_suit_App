package com.example.cosplay_suit_app.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
    SearchView sv_listChat;
    ArrayList<User> filteredList;
    ProgressBar progressBar;

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
        progressBar = view.findViewById(R.id.progressBar);
        tv_null = view.findViewById(R.id.tv_null);
        sv_listChat = view.findViewById(R.id.sv_chat);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);

        idUser = sharedPreferences.getString("id", "");
        filteredList = new ArrayList<>();
        user = new User();
        list = new ArrayList<>();
        listIdUser = new ArrayList<>();
        adapter = new AdapterListChat(getContext(),list);
        rcv_listChat.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
//        getList();

        sv_listChat.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList.clear();
                for (User user : list) {
                    if (user.getFullname().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(user);
                    }
                }

                adapter.updateList(filteredList);
                return true;
            }
        });
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getList() {
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        DatabaseReference userRoomsReference = database.getReference().child("chats");

        Gson gson = new GsonBuilder().setLenient().create();
        // Create a new object from HttpLoggingInterceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).client(client)
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
                                                    fetchedUser.setTimeStamp(lastMessage.getTimeStamp());
                                                    list.add(0, fetchedUser);
                                                    adapter.updateList(list);
                                                    Log.d("DEBUG", "onResponse: id nguoi nhan =  "+receiverId+"  ten nguoi nhan = "+ fetchedUser.getFullname()+" time "+fetchedUser.getTime());
                                                    adapter.notifyDataSetChanged();

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
        if (list.isEmpty()) {
            tv_null.postDelayed(() -> {
                if (list.isEmpty()) {
                    tv_null.setVisibility(View.VISIBLE);
                    rcv_listChat.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }, 1000);
        } else {
            tv_null.setVisibility(View.GONE);
            rcv_listChat.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {

        super.onResume();
        getList();
    }

}
