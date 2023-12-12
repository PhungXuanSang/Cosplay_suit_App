package com.example.cosplay_suit_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.LoginUser;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.Interface_retrofit.UserInterface;
import com.example.cosplay_suit_app.MainActivity;
import com.example.cosplay_suit_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    static String url = API.URL;
    static final String BASE_URL = url + "/user/api/";

    TextView tvSignup;

    private EditText edlogin;
    private EditText edpasswd;

    private TextInputLayout tillogin;
    private TextInputLayout tilpass;
    FirebaseAuth auth;
    FirebaseDatabase database;


    int temp = 0;

    String TAG = "zzz";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng chờ...");
        tvSignup = findViewById(R.id.tv_signup);

        edlogin = findViewById(R.id.login_login);
        edpasswd = findViewById(R.id.login_pass);

        tillogin = findViewById(R.id.til_login);
        tilpass = findViewById(R.id.til_pass);

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();


        findViewById(R.id.tv_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finishAffinity();
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate();
                if (temp==0){

                    LoginUser();
                }else{
                    temp=0;
                }
            }
        });

        findViewById(R.id.tv_forgot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswdActivity.class));
                finish();
            }
        });
        findViewById(R.id.id_back_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");
        Log.e(TAG, "onStart: " + id );
        if (!id.equalsIgnoreCase("")){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    void LoginUser() {
        progressDialog.show();
        String phone = edlogin.getText().toString().trim();
        String passwd = edpasswd.getText().toString().trim();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<LoginUser> objCall = userInterface.login(new User(phone, passwd));
        objCall.enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {

                LoginUser dto = response.body();

                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Log.e(TAG, "onResponse1: " +dto.getMessage());
                    Toast.makeText(LoginActivity.this, dto.getMessage(), Toast.LENGTH_SHORT).show();
                    if (dto.getUser() != null){
                        Log.e(TAG, "onResponse: " + dto.getUser().getEmail() );
                        remenber(dto.getUser().getId(),dto.getUser().getFullname(),dto.getUser().getPasswd(),dto.getUser().getEmail(),dto.getUser().getPhone(),dto.getUser().getRole());

                        DatabaseReference myRefId = database.getReference("Users/" + dto.getUser().getId() + "/id");
                        myRefId.setValue(dto.getUser().getId());

                        DatabaseReference myRefFullname = database.getReference("Users/" + dto.getUser().getId() + "/fullname");
                        myRefFullname.setValue(dto.getUser().getFullname());
                        Log.e(TAG, "onResponse: " + dto.getUser().getId());

                        saveUserFCMToken(dto.getUser().getId());

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finishAffinity();
                    }
                }else{
//                    Log.e("zzz", "onResponse: " +signUpUser.getMessage());
//                    Log.e(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.e("zzzz", t.getLocalizedMessage());
            }
        });

    }



    public void remenber(String id, String fullname,  String passwd, String email , String phone,String role){
        SharedPreferences preferences = getSharedPreferences("User",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("id",id);
        editor.putString("fullname",fullname);
        editor.putString("passwd",passwd);
        editor.putString("email",email);
        editor.putString("phone",phone);
        editor.putString("role",role);
        editor.apply();
    }


    void validate(){

        String reg = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";
        if (edlogin.getText().length() ==0){
            tillogin.setError("Don't leave your phone number blank!");
            temp++;
        }else if(!(edlogin.getText().toString().matches(reg))){
            tillogin.setError("Incorrect phone number format!");
            temp++;
        }else{
            tillogin.setError("");
        }
        if (edpasswd.getText().length() == 0){
            tilpass.setError("Don't leave your password blank!");
            temp++;
        }else if(edpasswd.getText().length() <= 6){
            tilpass.setError("Password greater than 6 characters!");
            temp++;
        }
        else{
            tilpass.setError("");
        }
    }

    private void saveUserFCMToken(String id) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }


                        String token = task.getResult();

                        String userUID = id;
                        DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference("userTokens").child(userUID);
                        tokenRef.setValue(token);
                    }
                });
    }

}