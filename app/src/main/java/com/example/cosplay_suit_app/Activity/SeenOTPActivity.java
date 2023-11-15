package com.example.cosplay_suit_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SeenOTPActivity extends AppCompatActivity {
    EditText input1, input2, input3, input4, input5, input6;
    TextView tv_mobile, tv_time, tv_resendotp;
    AppCompatButton btn_getotp;

    private ProgressDialog progressDialog;
    ProgressBar progressBar;
    CountDownTimer w,ws,wss;
    String getotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen_otpactivity);
        progressBar = findViewById(R.id.pro_sendotpnumber);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng chờ...");
        tv_mobile = findViewById(R.id.tv_mobile);
        input1 = findViewById(R.id.inputotp1);
        input2 = findViewById(R.id.inputotp2);
        input3 = findViewById(R.id.inputotp3);
        input4 = findViewById(R.id.inputotp4);
        input5 = findViewById(R.id.inputotp5);
        input6 = findViewById(R.id.inputotp6);
        btn_getotp = findViewById(R.id.btn_otp);
        tv_time = findViewById(R.id.tv_time);
        tv_resendotp = findViewById(R.id.tv_resendotp);
        getotp = getIntent().getStringExtra("otp");
        Log.e("zzz", "onCreate: " + getotp );
        Log.e("zzz", "onCreate: " + getIntent().getStringExtra("token") );
        w = new CountDownTimer(90000, 1000) {
            public void onTick(long mil) {
                tv_time.setText(mil / 1000 + "s");
            }

            @Override
            public void onFinish() {
                tv_time.setVisibility(View.INVISIBLE);
                tv_resendotp.setVisibility(View.VISIBLE);

            }
        }.start();
        tv_mobile.setText(String.format("+84-%s", getIntent().getStringExtra("mobile")));
        btn_getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!input1.getText().toString().trim().isEmpty() && !input2.getText().toString().trim().isEmpty() && !input3.getText().toString().trim().isEmpty() &&
                        !input4.getText().toString().trim().isEmpty() && !input5.getText().toString().trim().isEmpty() && !input6.getText().toString().trim().isEmpty()) {
                    String entercodeotp = input1.getText().toString() +
                            input2.getText().toString() +
                            input3.getText().toString() +
                            input4.getText().toString() +
                            input5.getText().toString() +
                            input6.getText().toString();

                    if (getotp != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        btn_getotp.setVisibility(View.INVISIBLE);

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(getotp,entercodeotp);

                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                            btn_getotp.setVisibility(View.VISIBLE);
                                            Toast.makeText(SeenOTPActivity.this, "otp correct!!", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getApplicationContext(), ForgotPassNewActivity.class);
                                            intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                                            startActivity(intent);
                                            finishAffinity();
                                        } else {
                                            // Xác thực thất bại
                                            progressBar.setVisibility(View.GONE);
                                            btn_getotp.setVisibility(View.VISIBLE);
                                            Toast.makeText(SeenOTPActivity.this, "OTP incorrect!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(SeenOTPActivity.this, "please enter all number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        numberotpmove();

        tv_resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                btn_getotp.setVisibility(View.INVISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+84" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        SeenOTPActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                btn_getotp.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                btn_getotp.setVisibility(View.VISIBLE);
                                Log.e("zzz", "onVerificationFailed: " + e.getMessage());
                                Toast.makeText(SeenOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String news, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                btn_getotp.setVisibility(View.VISIBLE);
                                getotp = news;
                                Log.e("zzz", "onCodeSent: " + news );
                            }
                        });

            }
        });
        findViewById(R.id.btn_cannel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                ws = new CountDownTimer(5000, 1000) {
                    public void onTick(long mil) {
                    }

                    @Override
                    public void onFinish() {
                        progressDialog.dismiss();
                        startActivity(new Intent(SeenOTPActivity.this, ErrorActivity.class));
                        finishAffinity();

                    }
                }.start();
            }
        });
    }

    private void numberotpmove() {
        if (input1.getText().toString().trim().isEmpty() && input2.getText().toString().trim().isEmpty() && input3.getText().toString().trim().isEmpty() &&
                input4.getText().toString().trim().isEmpty() && input5.getText().toString().trim().isEmpty() && input6.getText().toString().trim().isEmpty()) {
            input1.requestFocus();
        }
        input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    input2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    input3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    input4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    input5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    input6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}