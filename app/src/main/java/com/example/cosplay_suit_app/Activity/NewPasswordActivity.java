package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.Interface_retrofit.UserInterface;
import com.example.cosplay_suit_app.MainActivity;
import com.example.cosplay_suit_app.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPasswordActivity extends AppCompatActivity {

    static String url = API.URL;
    static final String BASE_URL = url + "/user/api/";
    private EditText OlPasss;
    private EditText NewPass;
    private EditText CheckNewPass;

    private TextInputLayout tilOlPass;
    private TextInputLayout tilNewPass;
    private TextInputLayout tilCheckNewPass;

    String id_u;
    String username_u;
    String pass_u;

    private int temp = 0;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        OlPasss = findViewById(R.id.pass_oldpass);
        NewPass = findViewById(R.id.pass_newpass);
        CheckNewPass = findViewById(R.id.pass_newpasscheck);

        tilOlPass = findViewById(R.id.pass_tilOldpass);
        tilNewPass = findViewById(R.id.pass_tilnewpass);
        tilCheckNewPass = findViewById(R.id.pass_tilnewpasscheck);

        progressDialog = new ProgressDialog(this);

        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        id_u = sharedPreferences.getString("id", "");
        username_u = sharedPreferences.getString("username","");
        pass_u = sharedPreferences.getString("passwd","");

        findViewById(R.id.pass_btncancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewPasswordActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.pass_btnsave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
                if(temp ==0){
                    progressDialog.show();
                    User u = new User();
                    u.setPasswd(NewPass.getText().toString());
                    Gson gson = new GsonBuilder().setLenient().create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create( gson))
                            .build();
                    UserInterface userInterface = retrofit.create(UserInterface.class);

                    Call<User> objCall = userInterface.new_pass(id_u,u);
                    objCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()){
                                progressDialog.dismiss();
                                sharedPreferences.edit().clear().commit();
                                Toast.makeText(NewPasswordActivity.this, "Change password successfully.You need to log back into the app!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(NewPasswordActivity.this, LoginActivity.class));
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
        String olpass = OlPasss.getText().toString();
        String pass = NewPass.getText().toString();
        String passnew = CheckNewPass.getText().toString();
        if (OlPasss.getText().length()==0){
            tilOlPass.setError("Không để trống Old Password!");
            temp++;
        }else if(!(olpass.equalsIgnoreCase(pass_u))){
            tilOlPass.setError("Password cũ không chính xác!");
        }
        else{
            tilOlPass.setError("");
        }
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