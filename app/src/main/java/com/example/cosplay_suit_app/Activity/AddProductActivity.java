package com.example.cosplay_suit_app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.CategotyAdapter;
import com.example.cosplay_suit_app.Adapter.ImageAdapter;
import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.databinding.ActivityAddProductBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddProductActivity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url + "/product/";
    static final String BASE_URL_CATE = url + "/category/";
    private ActivityAddProductBinding binding;
    String id;
    ArrayList<CategoryDTO> listLoai = new ArrayList<>();
    String nameCategoty;

    CategotyAdapter categotyAdapter;

    CategoryDTO categoryDTO;
    private static final int REQUEST_IMAGE_PICK = 1;

    private ImageAdapter imageAdapter;
    private List<ItemImageDTO> selectedImageList;
    SanPhamInterface sanPhamInterface;
    StorageReference storageReference;
    FirebaseStorage storage;
    Uri uri;

    ItemImageDTO imageDTO = new ItemImageDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = this.getSharedPreferences("User", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance("gs://duantotnghiepcosplaysuit.appspot.com");
        storageReference = storage.getReference();
        categoryDTO = new CategoryDTO();
        categotyAdapter = new CategotyAdapter(this, R.layout.item_category, listLoai);
        categotyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnAddProductLoai.setAdapter(categotyAdapter);
        //
        selectedImageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(selectedImageList, this);
        binding.rclvImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rclvImage.setAdapter(imageAdapter);
        //

        binding.ivAddProductAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        binding.ivProductToolbarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.spnAddProductLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                categoryDTO = listLoai.get(i);
                Log.d("TAG", "onItemSelected: " + categoryDTO.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AddProductActivity.this, "222222222", Toast.LENGTH_SHORT).show();
            }
        });


        callCategory();
        addProduct();

    }

    public void addProduct() {

        binding.btnRudProductAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callapiRes();
                finish();
            }
        });
        binding.ivProductToolbarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callapiRes();
                finish();

            }
        });

    }
    private void callapiRes() {

        DTO_SanPham dtoSanPham = new DTO_SanPham();
        dtoSanPham.setId_shop(id);
        dtoSanPham.setNameproduct(Objects.requireNonNull(binding.edtRudProductName.getText()).toString());
        dtoSanPham.setId_category(categoryDTO.getId());
        dtoSanPham.setListImage(selectedImageList);

        dtoSanPham.setAmount(Objects.requireNonNull(binding.edtRudProductAmount.getText()).toString());
        dtoSanPham.setPrice(Integer.parseInt(Objects.requireNonNull(binding.edtRudProductPrice.getText()).toString()));
        dtoSanPham.setDescription(Objects.requireNonNull(binding.edtRudProductDescription.getText()).toString());
        //
        DTO_SanPham dtoSanPham1 = new DTO_SanPham();
        dtoSanPham1.setId_shop(id);
        dtoSanPham1.setNameproduct(Objects.requireNonNull(binding.edtRudProductName.getText()).toString());
        dtoSanPham1.setId_category(categoryDTO.getId());
        dtoSanPham1.setListImage(selectedImageList);

        dtoSanPham1.setAmount(Objects.requireNonNull(binding.edtRudProductAmount.getText()).toString());
        dtoSanPham1.setPrice(Integer.parseInt(Objects.requireNonNull(binding.edtRudProductPrice.getText()).toString()));
        dtoSanPham1.setDescription(Objects.requireNonNull(binding.edtRudProductDescription.getText()).toString());
        //
        DTO_SanPham dtoSanPham2 = new DTO_SanPham();
        dtoSanPham2.setId_shop(id);
        dtoSanPham2.setNameproduct(Objects.requireNonNull(binding.edtRudProductName.getText()).toString());
        dtoSanPham2.setId_category(categoryDTO.getId());
        dtoSanPham2.setListImage(selectedImageList);

        dtoSanPham2.setAmount(Objects.requireNonNull(binding.edtRudProductAmount.getText()).toString());
        dtoSanPham2.setPrice(Integer.parseInt(Objects.requireNonNull(binding.edtRudProductPrice.getText()).toString()));
        dtoSanPham2.setDescription(Objects.requireNonNull(binding.edtRudProductDescription.getText()).toString());
        //

        if (binding.checkBoxSmall.isChecked()&& !binding.checkBoxBig.isChecked() && !binding.checkBoxFit.isChecked()){
            dtoSanPham.setSize("Nhỏ");

        }else if (!binding.checkBoxSmall.isChecked()&& binding.checkBoxBig.isChecked() && !binding.checkBoxFit.isChecked()){
            dtoSanPham.setSize("Lớn");

        }else if (!binding.checkBoxSmall.isChecked()&& !binding.checkBoxBig.isChecked() && binding.checkBoxFit.isChecked()){
            dtoSanPham.setSize("Nhỡ");
        }else if (binding.checkBoxSmall.isChecked() && binding.checkBoxBig.isChecked() && !binding.checkBoxFit.isChecked()){
            dtoSanPham.setSize("Nhỏ");
            dtoSanPham1.setSize("Lớn");
            callAddProduct(dtoSanPham);
            callAddProduct(dtoSanPham1);
            return;
        }else if (binding.checkBoxSmall.isChecked() && !binding.checkBoxBig.isChecked() && binding.checkBoxFit.isChecked()){
            dtoSanPham.setSize("Nhỏ");
            dtoSanPham2.setSize("Nhỡ");
            callAddProduct(dtoSanPham);
            callAddProduct(dtoSanPham2);
            return;
        }else if (!binding.checkBoxSmall.isChecked() && binding.checkBoxBig.isChecked() && binding.checkBoxFit.isChecked()){
            dtoSanPham1.setSize("Lớn");
            dtoSanPham2.setSize("Nhỡ");
            callAddProduct(dtoSanPham1);
            callAddProduct(dtoSanPham2);
            return;
        }else if (binding.checkBoxSmall.isChecked() && binding.checkBoxBig.isChecked() && binding.checkBoxFit.isChecked()){
            dtoSanPham.setSize(("Nhỏ"));
            dtoSanPham1.setSize("Lớn");
            dtoSanPham2.setSize("Nhỡ");
            callAddProduct(dtoSanPham);
            callAddProduct(dtoSanPham1);
            callAddProduct(dtoSanPham2);
            return;
        }


//        String strRealPath = RealPathUtil.getRealPath(this, uri);
//        File file = new File(strRealPath);

        callAddProduct(dtoSanPham);

    }

    // Phương thức hỗ trợ để chuyển đổi InputStream thành mảng byte

    void callAddProduct(DTO_SanPham dtoSanPham) {

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface
        sanPhamInterface = retrofit.create(SanPhamInterface.class);

        // tạo đối tượng
        Call<DTO_SanPham> objCall = sanPhamInterface.addProduct(dtoSanPham );
        objCall.enqueue(new Callback<DTO_SanPham>() {
            @Override
            public void onResponse(@NonNull Call<DTO_SanPham> call, Response<DTO_SanPham> response) {

//                Log.d("TAG", "onResponse: oooooo"+response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DTO_SanPham> call, Throwable t) {
//                Log.d("TAG", "onFailure: "+t.getMessage());

            }

        });
    }

    void callCategory() {

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CATE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface
        sanPhamInterface = retrofit.create(SanPhamInterface.class);

        // tạo đối tượng
        Call<List<CategoryDTO>> objCall = sanPhamInterface.ListCategory();
        objCall.enqueue(new Callback<List<CategoryDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryDTO>> call, @NonNull Response<List<CategoryDTO>> response) {

                if (response.body() != null) {
                    listLoai.addAll(response.body());
                    categotyAdapter.notifyDataSetChanged();

                    for (CategoryDTO categoryDTO : listLoai) {
//                        Log.d("TAG", "onResponse1111: " + categoryDTO.getId());

                    }
                }

            }

            @Override
            public void onFailure(Call<List<CategoryDTO>> call, Throwable t) {
//                Log.d("TAG", "onFailure: "+t.getMessage());

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
            //                InputStream inputStream = getContentResolver().openInputStream(imageUri);
//                byte[] imageBytes = getBytesFromInputStream(inputStream);
//                String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            uploadImage();
            // Thêm ảnh đã mã hóa vào danh sách ảnh đã chọn
//                ItemImageDTO itemImage = new ItemImageDTO(base64Image);
//                selectedImageList.add(itemImage);

            // Cập nhật adapter để hiển thị danh sách ảnh đã chọn
            imageAdapter.notifyDataSetChanged();
        }
    }

    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, bytesRead);
        }
        return byteBuffer.toByteArray();
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
                                    // Tạo đối tượng ItemImageDTO với URL ảnh và thêm vào danh sách
                                    ItemImageDTO itemImage = new ItemImageDTO(downloadUri.toString());
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