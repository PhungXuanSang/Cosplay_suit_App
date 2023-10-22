package com.example.cosplay_suit_app.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.LoginActivity;
import com.example.cosplay_suit_app.DTO.Shop;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.Interface_retrofit.UserInterface;
import com.example.cosplay_suit_app.MainActivity;
import com.example.cosplay_suit_app.Package_bill.Collection_adapter_bill;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment_profile extends Fragment {

    private TextView tv_fullname;

    private ImageView img_profile;

    private TextView tv_dky_shop, tv_donhangmua;
    private Button btn_login_profile;

    private AppCompatButton appCompatButton;

    private ProgressDialog progressDialog;

    Dialog dialog;

    String username_u;
    String id_user;
    static String id;
    static String url = API.URL;
    static final String BASE_URL = url +"/user/api/";

    public Fragment_profile() {
    }

    public static Fragment_profile newInstance(){
        Fragment_profile fragmentProfile = new Fragment_profile();
        return fragmentProfile;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_profile, container, false);
        return viewok;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_fullname = view.findViewById(R.id.tv_fullname_profile);
        btn_login_profile = view.findViewById(R.id.btn_login_profile);
        tv_dky_shop = view.findViewById(R.id.tv_dky_shop);
        img_profile = view.findViewById(R.id.img_profile);
        tv_donhangmua = view.findViewById(R.id.donhangmua);
        appCompatButton = view.findViewById(R.id.btn_login_profile);


        tv_donhangmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Collection_adapter_bill.class);
                startActivity(intent);
            }
        });

        btn_login_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        tv_dky_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String ed_nameshop = intent.getStringExtra("nameshop");
                String ed_address = intent.getStringExtra("address");
               showDialog(getContext(), ed_nameshop , ed_address );
            }
        });

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        username_u = sharedPreferences.getString("fullname","");
        id = sharedPreferences.getString("id", "");
        tv_fullname.setText(username_u);


        if (!id.equalsIgnoreCase("")) {
            appCompatButton.setText("Sign Out");
            appCompatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedPreferences.edit().clear().commit();
                    startActivity(new Intent(getContext(), MainActivity.class));
                }
            });
        } else {
            appCompatButton.setText("Sign In");
            appCompatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
            });
        }



    }

    public void showDialog(Context context, String name, String address ) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_register_shop);

        progressDialog = new ProgressDialog(getContext());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        id_user = sharedPreferences.getString("id","");


        Button btn_regisshop = dialog.findViewById(R.id.btn_register_shop);
        EditText ed_nameshop = dialog.findViewById(R.id.signup_nameshop);
        EditText ed_address = dialog.findViewById(R.id.signup_address);

        btn_regisshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String fname = ed_nameshop.getText().toString();
                String a = ed_address.getText().toString();


                Log.d("zzzz", "id_user: "+ id_user);
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                Shop shop = new Shop();
                shop.setNameshop(fname);
                shop.setAddress(a);
                shop.setId_user(id_user);


                AddShop(shop);
                User us = new User();
                us.setRole("Salesman");

                UpdateRole(us);




                dialog.dismiss();
            }
        });






        dialog.show();
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
                    Log.e("zzzz", "onResponse1: " +user.getRole());

                    if (u != null){
                        Log.e("zzzzz", "onResponse: " + u );
                        Intent intent = new Intent(getContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                }else{
//                    Log.e("zzz", "onResponse: " +signUpUser.getMessage());
//                    Log.e(TAG, "onResponse: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Sign Up Fail", Toast.LENGTH_SHORT).show();
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

                    Log.e("zzzzz", "onResponse1: " +shop.getId_user());
                    Toast.makeText(getContext(), shop.getId(), Toast.LENGTH_SHORT).show();

                }else{
//                    Log.e("zzz", "onResponse: " +signUpUser.getMessage());
//                    Log.e(TAG, "onResponse: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                Toast.makeText(getContext(), "Sign Up Fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.e("zzzz", t.getLocalizedMessage());

            }
        });

    }

}
