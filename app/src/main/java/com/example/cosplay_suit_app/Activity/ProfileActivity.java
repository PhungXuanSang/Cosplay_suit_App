package com.example.cosplay_suit_app.Activity;

import static com.example.cosplay_suit_app.Activity.Chitietsanpham.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.ProfileDTO;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.Interface_retrofit.UserInterface;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileActivity extends AppCompatActivity {

    // Khai báo các TextView và ImageView tương ứng với các thuộc tính trong giao diện
    TextView tv_email, tv_sdt, tv_fullname, tv_andress;
    ImageView btn_11, btn_12, btn_13, btn_14;
    String id_user;

    static String url = API.URL;
    static final String BASE_URL = url +"/user/api/";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ các thành phần trong layout
        tv_email = findViewById(R.id.id_Email);
        tv_sdt = findViewById(R.id.id_Phone);
        tv_fullname = findViewById(R.id.id_Fullname);
        tv_andress = findViewById(R.id.id_andress);

        btn_11 = findViewById(R.id.btn_11);
        btn_12 = findViewById(R.id.btn_12);
        btn_13 = findViewById(R.id.btn_13);
        btn_14 = findViewById(R.id.btn_14);
        sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);

        id_user = sharedPreferences.getString("id", "");
            getList();
    }
    void getList(){
        // Tạo đối tượng Gson
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Sử dụng interface
        UserInterface userInterface = retrofit.create(UserInterface.class);

        // Tạo đối tượng Call
        Call<ProfileDTO> objCall = userInterface.getUsere(id_user);

        objCall.enqueue(new Callback<ProfileDTO>() {
            @Override
            public void onResponse(Call<ProfileDTO> call, Response<ProfileDTO> response) {
                if (response.isSuccessful()){
                    ProfileDTO dto = response.body();
                    if (dto != null) {
                        tv_fullname.setText(dto.getFullname());
                        tv_email.setText(dto.getEmail()); // Sử dụng phương thức đúng để lấy email
                        tv_andress.setText(dto.getDiachi()); // Sử dụng phương thức đúng để lấy địa chỉ
                        tv_sdt.setText(dto.getPhone()); // Sử dụng phương thức đúng để lấy số điện thoại

                    } else {
                        Log.e(TAG, "Response body là null");
                    }
                } else {
                    Log.e(TAG, "Phản hồi không thành công: " + response.code());
                    // Xử lý phản hồi không thành công, nếu cần
                }
            }

            @Override
            public void onFailure(Call<ProfileDTO> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                // Xử lý lỗi, nếu cần
            }
        });
    }
}