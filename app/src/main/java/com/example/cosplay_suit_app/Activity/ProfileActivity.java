package com.example.cosplay_suit_app.Activity;

import static com.example.cosplay_suit_app.Activity.Chitietsanpham.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.Profile1_DTO;
import com.example.cosplay_suit_app.DTO.ProfileDTO;
import com.example.cosplay_suit_app.Interface_retrofit.UserInterface;
import com.example.cosplay_suit_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileActivity extends AppCompatActivity {

    // Khai báo các TextView và ImageView tương ứng với các thuộc tính trong giao diện
    TextView tv_email, tv_sdt, tv_fullname, tv_andress;
    ImageView btn_11, btn_12, btn_13, btn_14, id_back;
    String id_user,_idprofile;


    static String url = API.URL;
    static final String BASE_URL = url +"/user/api/";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ các thành phần trong layout
        tv_sdt = findViewById(R.id.id_Phone);
        tv_email = findViewById(R.id.id_Email);
        tv_fullname = findViewById(R.id.id_Fullname);
        tv_andress = findViewById(R.id.id_andress);
        id_back = findViewById(R.id.id_back);

        id_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        btn_11 = findViewById(R.id.btn_11);
        btn_12 = findViewById(R.id.btn_12);
        btn_13 = findViewById(R.id.btn_13);
        btn_14 = findViewById(R.id.btn_14);

        btn_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogsua("phone");
            }
        });

        btn_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogsua("email");
            }
        });

        btn_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogsua("name");
            }
        });

        btn_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogsua("address");
            }
        });

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
                if (response.isSuccessful()) {
                    ProfileDTO dto = response.body();
                    if (dto != null) {
                        _idprofile = dto.getId();

                        // Kiểm tra xem thông tin mới có khác thông tin hiện tại không trước khi cập nhật giao diện
                        if (!tv_fullname.getText().toString().equals("Tên:" + dto.getFullname())) {
                            tv_fullname.setText( dto.getFullname());
                        }

                        if (!tv_email.getText().toString().equals("Email:" + dto.getEmail())) {
                            tv_email.setText( dto.getEmail());
                        }

                        if (!tv_andress.getText().toString().equals("Địa chỉ:" + dto.getDiachi())) {
                            tv_andress.setText( dto.getDiachi());
                        }

                        if (!tv_sdt.getText().toString().equals("SĐT nhận hàng:" + dto.getPhone())) {
                            tv_sdt.setText( dto.getPhone());
                        }
                    } else {
                        Log.e(TAG, "Response body là null");
                    }
                } else {
                    Log.e(TAG, "Phản hồi không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ProfileDTO> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                // Xử lý lỗi, nếu cần
            }
        });
    }


    private void dialogsua(String field) {
        final Dialog editDialog = new Dialog(ProfileActivity.this);
        editDialog.setContentView(R.layout.edit_diachi);
        // Sử dụng drawable resource để đặt hình dạng và độ cong của góc cho dialog
        Drawable drawable = getResources().getDrawable(R.drawable.rounded_dialog);
        editDialog.getWindow().setBackgroundDrawable(drawable);

        Profile1_DTO dto = new Profile1_DTO();
        EditText tv_sua = editDialog.findViewById(R.id.tv_sua);
        Button saveButton = editDialog.findViewById(R.id.btn_save);
        dto.setId(_idprofile);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get content from EditText in the dialog
                String value = tv_sua.getText().toString();

                // Update the corresponding field based on the button clicked
                switch (field) {
                    case "phone":
                        dto.setPhone(value);
                        break;
                    case "email":
                        dto.setEmail(value);
                        break;
                    case "name":
                        dto.setFullname(value);
                        break;
                    case "address":
                        dto.setDiachi(value);
                        break;
                }

                // Call the appropriate method to update the user profile
                updateUserProfile(dto);
                // Dismiss the dialog
                editDialog.dismiss();
            }
        });

        editDialog.show();

    }

    private void updateUserProfile(Profile1_DTO dto) {
        // Tạo đối tượng Gson
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
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        // Sử dụng interface
        UserInterface userInterface = retrofit.create(UserInterface.class);

        // Lấy giá trị hiện tại của các trường từ server
        String currentEmail = tv_email.getText().toString();
        String currentName = tv_fullname.getText().toString();
        String currentAddress = tv_andress.getText().toString();
        String currentPhone = tv_sdt.getText().toString();



        validateAndUpdateEmail(dto, currentEmail);
        validateAndUpdateFullName(dto, currentName);
        validateAndUpdateAddress(dto, currentAddress);
        validateAndUpdatePhone(dto, currentPhone);
        // Gửi yêu cầu cập nhật thông tin người dùng
        Call<Profile1_DTO> updateProfileCall = userInterface.updateUserField(dto.getId(), dto);
        updateProfileCall.enqueue(new Callback<Profile1_DTO>() {
            @Override
            public void onResponse(Call<Profile1_DTO> call, Response<Profile1_DTO> response) {
                if (response.isSuccessful()) {
                    Profile1_DTO updatedDto = response.body();
                    if (updatedDto != null) {
                        _idprofile = updatedDto.getId();

                        // Cập nhật giao diện người dùng hoặc thực hiện các hành động khác
                        // Chỉ cập nhật trường được thay đổi
                        if (updatedDto.getFullname() != null) {
                            tv_fullname.setText( updatedDto.getFullname());
                        }
                        if (updatedDto.getEmail() != null) {
                            tv_email.setText( updatedDto.getEmail());
                        }
                        if (updatedDto.getDiachi() != null) {
                            tv_andress.setText(updatedDto.getDiachi());
                        }
                        if (updatedDto.getPhone() != null) {
                            tv_sdt.setText( updatedDto.getPhone());
                        }
                    } else {
                        Log.e(TAG, "Response body là null");
                    }
                } else {
                    Log.e(TAG, "Phản hồi không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Profile1_DTO> call, Throwable t) {
                // Xử lý lỗi (nếu cần)
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
            }
        });
    }
    // Hàm kiểm tra và cập nhật email
    private void validateAndUpdateEmail(Profile1_DTO dto, String currentEmail) {
        if (TextUtils.isEmpty(dto.getEmail())) {
            dto.setEmail(currentEmail);
        }
    }

    // Hàm kiểm tra và cập nhật họ và tên
    private void validateAndUpdateFullName(Profile1_DTO dto, String currentName) {
        if (TextUtils.isEmpty(dto.getFullname())) {
            dto.setFullname(currentName);
        }
    }

    // Hàm kiểm tra và cập nhật địa chỉ
    private void validateAndUpdateAddress(Profile1_DTO dto, String currentAddress) {
        if (TextUtils.isEmpty(dto.getDiachi())) {
            dto.setDiachi(currentAddress);
        }
    }

    // Hàm kiểm tra và cập nhật số điện thoại
    private void validateAndUpdatePhone(Profile1_DTO dto, String currentPhone) {
        if (TextUtils.isEmpty(dto.getPhone()) || !isValidPhoneNumber(dto.getPhone())) {
            dto.setPhone(currentPhone);

        }
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Sử dụng regex để kiểm tra số điện thoại
        String phoneRegex = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);

        // Trả về true nếu số điện thoại hợp lệ, ngược lại trả về false
        return matcher.matches();
    }


}