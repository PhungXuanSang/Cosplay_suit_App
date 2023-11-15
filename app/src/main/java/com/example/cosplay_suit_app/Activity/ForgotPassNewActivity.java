package com.example.cosplay_suit_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.Interface_retrofit.UserInterface;
import com.example.cosplay_suit_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPassNewActivity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url + "/user/api/";
    private EditText NewPass;
    private EditText CheckNewPass;

    private TextInputLayout tilNewPass;
    private TextInputLayout tilCheckNewPass;

    CountDownTimer w,w1;
    private int temp = 0;

    String phone;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_new);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        NewPass = findViewById(R.id.pass_newpass);
        CheckNewPass = findViewById(R.id.pass_newpasscheck);

        tilNewPass = findViewById(R.id.pass_tilnewpass);
        tilCheckNewPass = findViewById(R.id.pass_tilnewpasscheck);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng chờ...");

        phone = getIntent().getStringExtra("mobile");
        Log.e("zzz", "onCreate: " + phone );
        findViewById(R.id.pass_btncancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                w = new CountDownTimer(3000, 1000) {
                    public void onTick(long mil) {


                    }

                    @Override
                    public void onFinish() {
                        progressDialog.dismiss();
                        startActivity(new Intent(ForgotPassNewActivity.this, ErrorActivity.class));
                        finishAffinity();

                    }
                }.start();
            }
        });

        findViewById(R.id.pass_btnsave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
                if(temp ==0){
                    progressDialog.show();

                    if (user != null) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                    }

                    User u = new User();
                    u.setPasswd(NewPass.getText().toString());
                    Gson gson = new GsonBuilder().setLenient().create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create( gson))
                            .build();
                    UserInterface userInterface = retrofit.create(UserInterface.class);

                    Call<User> objCall = userInterface.forPass("0"+phone,u);
                    objCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Change password successfully.You need to log back into the app!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }else{
                                Log.e("zzz", "onResponse: " + response.message() );
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("zzz", "onFailure: " + t.getLocalizedMessage() );
                        }
                    });
                }else{
                    temp = 0;
                }
            }
        });
    }

    void validate(){

        String pass = NewPass.getText().toString();
        String passnew = CheckNewPass.getText().toString();

        if (NewPass.getText().length()==0){
            tilNewPass.setError("Không để trống New Password!");
            temp++;
        }else if(NewPass.getText().length() <= 6){
            tilNewPass.setError("NewPassword lớn hơn 6 ký tự");
            temp++;
        }
        else{
            tilNewPass.setError("");
        }
        if (CheckNewPass.getText().length()==0){
            tilCheckNewPass.setError("Không để trống New Password!");
            temp++;
        }else if(CheckNewPass.getText().length() <= 6){
            tilCheckNewPass.setError("Check NewPassword lớn hơn 6 ký tự");
            temp++;
        }
        else if (!(passnew.equalsIgnoreCase(pass))){
            tilCheckNewPass.setError("Password không trùng nhau!");
            temp++;
        }
        else{
            tilCheckNewPass.setError("");
        }
    }
}