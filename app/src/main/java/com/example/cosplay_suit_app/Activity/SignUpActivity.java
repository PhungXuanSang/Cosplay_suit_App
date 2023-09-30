package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.DTO.SignUpUser;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.DTO.UserInterface;
import com.example.cosplay_suit_app.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
    static final String BASE_URL = "http://192.168.52.107:3000/user/api/";
    String msg = "";
    TextView tvLogin;

    private EditText edname;
    private EditText edphone;
    private EditText edemail;
    private EditText edpasswd;

    private TextInputLayout tilusername;
    private TextInputLayout tilphone;
    private TextInputLayout tilemail;
    private TextInputLayout tilpass;

    private CheckBox check_box;
    int temp = 0;

    String TAG = "zzz";

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);

        tvLogin = findViewById(R.id.tv_signin);
        edname = findViewById(R.id.signup_name);
        edphone = findViewById(R.id.signup_phone);
        edemail = findViewById(R.id.signup_email);
        edpasswd = findViewById(R.id.signup_pass);

        tilusername = findViewById(R.id.til_name);
        tilphone = findViewById(R.id.til_phone);
        tilemail = findViewById(R.id.til_email);
        tilpass = findViewById(R.id.til_pass);

        check_box = findViewById(R.id.id_check);
        check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    tilpass.getEditText().setTransformationMethod(null);
                }else{
                    tilpass.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });


        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate();
                if (temp==0){
                    User u = new User();
                    u.setFullname(edname.getText().toString());
                    u.setPasswd(edpasswd.getText().toString());
                    u.setEmail(edemail.getText().toString());
                    u.setPhone(edphone.getText().toString());
                    u.setRole("User");
                    AddUser(u);

                }else{
                    temp=0;
                }
            }
        });

    }







    void AddUser(User u){
        progressDialog.show();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create( gson))
                .build();
        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<SignUpUser> objCall = userInterface.sign_up(u);
        objCall.enqueue(new Callback<SignUpUser>() {
            @Override
            public void onResponse(Call<SignUpUser> call, Response<SignUpUser> response) {
                SignUpUser signUpUser = response.body();

                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Log.e(TAG, "onResponse1: " +signUpUser.getMessage());
                    Toast.makeText(SignUpActivity.this, signUpUser.getMessage(), Toast.LENGTH_SHORT).show();
                    if (signUpUser.getUser() != null){
                        Log.e(TAG, "onResponse: " + signUpUser.getUser().getEmail() );
                        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }else{
//                    Log.e("zzz", "onResponse: " +signUpUser.getMessage());
//                    Log.e(TAG, "onResponse: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<SignUpUser> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Sign Up Fail", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.e("zzzz", t.getLocalizedMessage());

            }
        });

    }


    void validate(){

        String reg = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";

        String regName = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]" +
                "+[\\-'\\s]?[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ ]+$";

        if (edname.getText().length() ==0){
            edname.setError("Don't leave your name blank!");
            temp++;
        }else if(!(edname.getText().toString().matches(regName))){
            edname.setError("Incorrect phone name format!");
            temp++;
        }else{
        }

        if (edphone.getText().length() ==0){
            edphone.setError("Don't leave your phone number blank!");
            temp++;
        }else if(!(edphone.getText().toString().matches(reg))){
            edphone.setError("Incorrect phone number format!");
            temp++;
        }else{
        }
        if (edemail.getText().length() == 0){
            edemail.setError("Don't leave your email blank!");
            temp++;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(edemail.getText().toString()).matches()){
            edemail.setError("Incorrect email format!");
            temp++;
        }else{
        }

        if (edpasswd.getText().length() == 0){
            edpasswd.setError("Don't leave your password blank!");
            temp++;
        }else if(edpasswd.getText().length() <= 6){
            edpasswd.setError("Password greater than 6 characters!");
            temp++;
        }
        else{
        }
        Log.e("123", "validate: " + temp );

    }


}