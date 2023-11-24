package com.example.cosplay_suit_app.Package_bill.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.API;
//import com.example.cosplay_suit_app.Activity.AddProductActivity;
//import com.example.cosplay_suit_app.Activity.QlspActivity;
import com.example.cosplay_suit_app.Adapter.ImageAdapter;
import com.example.cosplay_suit_app.Adapter.ImageCmtsAdapter;
import com.example.cosplay_suit_app.DTO.CmtsDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.ImageCmtsDTO;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.Interface_retrofit.CmtsInterface;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddDanhGiaActivity extends AppCompatActivity {

    static String url = API.URL;
    static final String BASE_URL = url +"/comments/";
    RatingBar ratingBar;
    TextView tv_star, tv_nameCmts;
    int danhgia = 0;
    ImageView img_back, img_spCmts, img_sendCmts, img_addCmts;
    RecyclerView rcv_imgCmts;
    EditText ed_content;
    String idSp, idBill, nameSp, anhSp;
    StorageReference storageReference;
    FirebaseStorage storage;
    private static final int REQUEST_IMAGE_PICK = 1;

    private ImageCmtsAdapter imageAdapter;
    private List<ImageCmtsDTO> selectedImageList;
    Uri uri;
    CmtsDTO cmtsDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_danh_gia);

        initView();

        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance("gs://duantotnghiepcosplaysuit.appspot.com");
        storageReference = storage.getReference();

        idSp = getIntent().getStringExtra("idproduct");
        idBill = getIntent().getStringExtra("idbill");
        anhSp = getIntent().getStringExtra("anhsp");
        nameSp = getIntent().getStringExtra("namesp");

        Glide.with(this)
                .load(anhSp)
                .error(R.drawable.image)
                .placeholder(R.drawable.image)
                .centerCrop()
                .into(img_spCmts);

        tv_nameCmts.setText(nameSp);
        tv_star.setVisibility(View.GONE);


        selectedImageList = new ArrayList<>();
        imageAdapter = new ImageCmtsAdapter(selectedImageList, this);
        rcv_imgCmts.setAdapter(imageAdapter);


        img_addCmts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Xử lý sự kiện khi đánh giá thay đổi
                danhgia = (int) rating;
                if (danhgia == 1) {
                    tv_star.setText("Rất tệ");
                    tv_star.setVisibility(View.VISIBLE);
                    tv_star.setTextColor(Color.parseColor("#000000"));
                } else if (danhgia == 2) {
                    tv_star.setText("Tệ");
                    tv_star.setVisibility(View.VISIBLE);
                    tv_star.setTextColor(Color.parseColor("#000000"));
                } else if (danhgia == 3) {
                    tv_star.setText("Bình thường");
                    tv_star.setVisibility(View.VISIBLE);
                    tv_star.setTextColor(Color.parseColor("#000000"));
                } else if (danhgia == 4) {
                    tv_star.setText("Tốt");
                    tv_star.setVisibility(View.VISIBLE);
                    tv_star.setTextColor(Color.parseColor("#F86739"));
                } else {
                    tv_star.setText("Tuyệt vời");
                    tv_star.setVisibility(View.VISIBLE);
                    tv_star.setTextColor(Color.parseColor("#F86739"));
                }

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");
        User user = new User();
        user.setId(id);
        user.setEmail(sharedPreferences.getString("email",""));
        user.setRole(sharedPreferences.getString("role",""));
        user.setPasswd(sharedPreferences.getString("passwd",""));
        user.setPhone(sharedPreferences.getString("phone",""));
        user.setFullname(sharedPreferences.getString("fullname",""));
        img_sendCmts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (danhgia == 0) {
                    tv_star.setText("Vui lòng chọn giá trị ");
                    tv_star.setVisibility(View.VISIBLE);
                    tv_star.setTextColor(Color.parseColor("#FF0000"));
                } else if (ed_content.getText().toString().length()==0) {
                    ed_content.setError("Vui lòng nhập đánh giá trước khi gửi!!");
                } else{
                    cmtsDTO = new CmtsDTO();
                    cmtsDTO.setId_bill(idBill);
                    cmtsDTO.setIdPro(idSp);
                    cmtsDTO.setImage(selectedImageList);
                    cmtsDTO.setStar(danhgia);
                    cmtsDTO.setContent(ed_content.getText().toString());
                    cmtsDTO.setUser(user);
                    addCmts(cmtsDTO);
                }
            }
        });

    }


    private void initView() {
        ratingBar = findViewById(R.id.ratingBar);
        tv_star = findViewById(R.id.tv_star);
        img_back = findViewById(R.id.ic_back);
        img_spCmts = findViewById(R.id.img_spCmts);
        tv_nameCmts = findViewById(R.id.tv_nameCmts);
        img_sendCmts = findViewById(R.id.img_sendcmts);
        rcv_imgCmts = findViewById(R.id.rcv_imgCmts);
        img_addCmts = findViewById(R.id.img_addCmts);
        ed_content = findViewById(R.id.ed_content);

    }

    private  void  addCmts(CmtsDTO cmtsDTO){
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface
        CmtsInterface cmtsInterface = retrofit.create(CmtsInterface.class);

        // tạo đối tượng
        Call<CmtsDTO> objCall = cmtsInterface.addCmts(cmtsDTO);
        objCall.enqueue(new Callback<CmtsDTO>() {
            @Override
            public void onResponse(@NonNull Call<CmtsDTO> call, Response<CmtsDTO> response) {

                Toast.makeText(AddDanhGiaActivity.this, "Đã Đánh giá", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            @Override
            public void onFailure(@NonNull Call<CmtsDTO> call, Throwable t) {

            }

        });
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
        Log.d("TAG", "openImagePicker: " + selectedImageList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            uri = imageUri;

            uploadImage();

            imageAdapter.notifyDataSetChanged();
        }
    }

    private void uploadImage() {
        if (uri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Tạo đường dẫn lưu trữ file trong Firebase Storage
            StorageReference storageRef = storageReference.child("images/" + UUID.randomUUID().toString() + ".jpg");

            // Tải ảnh lên Firebase Storage
            storageRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            // Lấy URL ảnh sau khi tải lên thành công
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {

                                    ImageCmtsDTO itemImage = new ImageCmtsDTO(downloadUri.toString());
                                    selectedImageList.add(itemImage);
                                    imageAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            // Xử lý lỗi tải lên ảnh
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // Cập nhật tiến trình tải lên (nếu cần)
                        }
                    });
        }
    }


}