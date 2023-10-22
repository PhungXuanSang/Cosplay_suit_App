package com.example.cosplay_suit_app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.Shop;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.Interface_retrofit.UserInterface;
import com.example.cosplay_suit_app.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterShopActivity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url +"/user/api/";

    String msg = "";
    TextView tvLogin;

    private EditText ed_nameshop;
    private EditText ed_address;
    private Button btn_register;


    private TextInputLayout tilnameshop;
    private TextInputLayout tiladdress;


    int temp = 0;

    String TAG = "zzz";

    private ProgressDialog progressDialog;
    String id_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_register_shop);

        progressDialog = new ProgressDialog(this);


        ed_nameshop = findViewById(R.id.signup_nameshop);
        ed_address = findViewById(R.id.signup_address);


        tilnameshop = findViewById(R.id.til_name);
        tiladdress = findViewById(R.id.til_address);

        btn_register = findViewById(R.id.btn_register_shop);

        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        id_user = sharedPreferences.getString("id","");




        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String fname = ed_nameshop.getText().toString();
                String a = ed_address.getText().toString();


                Log.d("zzzz", "id_user: "+ id_user);
                Toast.makeText(RegisterShopActivity.this, "", Toast.LENGTH_SHORT).show();
                Shop shop = new Shop();
                shop.setNameshop(fname);
                shop.setAddress(a);
                shop.setId_user(id_user);


                AddShop(shop);
                User us = new User();
                us.setRole("Salesman");

                UpdateRole(us);
            }
        });

    }


    void UpdateRole(User u){
        progressDialog.show();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create( gson))
                .build();
        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<User> objCall = userInterface.udate_role(id_user,u);
        objCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Log.e(TAG, "onResponse1: " +user.getRole());

                    if (u != null){
                        Log.e(TAG, "onResponse: " + u );
                        Intent intent = new Intent(RegisterShopActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }else{
//                    Log.e("zzz", "onResponse: " +signUpUser.getMessage());
//                    Log.e(TAG, "onResponse: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterShopActivity.this, "Sign Up Fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.e("zzzz", t.getLocalizedMessage());

            }
        });

    }
    void AddShop(Shop s){

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create( gson))
                .build();
        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<Shop> objCall = userInterface.new_shop(s);
        objCall.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                Shop shop = response.body();

                if (response.isSuccessful()){

                    Log.e(TAG, "onResponse1: " +shop.getId_user());
                    Toast.makeText(RegisterShopActivity.this, shop.getId(), Toast.LENGTH_SHORT).show();

                }else{
//                    Log.e("zzz", "onResponse: " +signUpUser.getMessage());
//                    Log.e(TAG, "onResponse: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                Toast.makeText(RegisterShopActivity.this, "Sign Up Fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.e("zzzz", t.getLocalizedMessage());

            }
        });

    }





}