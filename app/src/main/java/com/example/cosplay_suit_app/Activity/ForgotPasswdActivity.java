package com.example.cosplay_suit_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cosplay_suit_app.MainActivity;
import com.example.cosplay_suit_app.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ForgotPasswdActivity extends AppCompatActivity {
    EditText edNumber;
    AppCompatButton btnSend;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwd);
        edNumber = findViewById(R.id.input_mobile_number);
        btnSend = findViewById(R.id.btn_otp);
        progressBar = findViewById(R.id.pro_sendotpphone);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswdActivity.this, LoginActivity.class));
                finish();
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edNumber.getText().toString().trim().isEmpty()){
                    if (edNumber.getText().toString().trim().length() == 9){

                        progressBar.setVisibility(View.VISIBLE);
                        btnSend.setVisibility(View.INVISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+84" + edNumber.getText().toString(),
                                60,
                                TimeUnit.SECONDS,
                                ForgotPasswdActivity.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(View.GONE);
                                        btnSend.setVisibility(View.INVISIBLE);

                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        btnSend.setVisibility(View.INVISIBLE);
                                        Log.e("zzz", "onVerificationFailed: " + e.getMessage() );
                                        Toast.makeText(ForgotPasswdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String backotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBar.setVisibility(View.GONE);
                                        btnSend.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(getApplicationContext(), SeenOTPActivity.class);
                                        intent.putExtra("mobile",edNumber.getText().toString());
                                        intent.putExtra("otp",backotp);
                                        intent.putExtra("token",forceResendingToken);
                                        startActivity(intent);
                                    }
                                });

                    }else{
                        Toast.makeText(ForgotPasswdActivity.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ForgotPasswdActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}