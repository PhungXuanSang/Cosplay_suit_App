package com.example.cosplay_suit_app.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.CategotyAdapter;
import com.example.cosplay_suit_app.Adapter.ImageAdapter;
import com.example.cosplay_suit_app.Adapter.PropAdapter;
import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.Interface_retrofit.ShopInterface;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddProductActivity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url + "/product/";
    private ActivityAddProductBinding binding;
    String id;
    ArrayList<CategoryDTO> listLoai = new ArrayList<>();
    String nameCategoty;

    CategotyAdapter categotyAdapter;

    CategoryDTO categoryDTO;
    private static final int REQUEST_IMAGE_PICK = 1;
    static final String BASE_URL_SHOP = url + "/shop/";
    private ImageAdapter imageAdapter;
    private List<ItemImageDTO> selectedImageList;
    SanPhamInterface sanPhamInterface;
    StorageReference storageReference;
    FirebaseStorage storage;
    Uri uri;

    String idshop,idProduct;

     List<DTO_properties> selectedProp;

    PropAdapter propAdapter;
    int total = 0;

    ItemImageDTO imageDTO = new ItemImageDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = this.getSharedPreferences("User", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        SharedPreferences sharedPreferences2 = this.getSharedPreferences("shops", MODE_PRIVATE);
        idshop = sharedPreferences2.getString("id", "");


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
        selectedProp = new ArrayList<>();
        propAdapter = new PropAdapter(selectedProp,this);
        binding.rclvSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rclvSize.setAdapter(propAdapter);
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

        onClick();
        callCategory();
        addProduct();


    }

    private void onClick() {

        binding.ivAddProductnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
                builder.setTitle("Thuộc tính");
                builder.setMessage("Thêm thuộc tính để tạo ra sản phẩm có nhiều kích thước");
                builder.setPositiveButton("Tôi đã hiểu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý sự kiện khi người dùng nhấn nút OK
                        dialog.dismiss(); // Đóng dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        binding.ivAddProductAddSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                // Gắn layout tùy chỉnh vào dialog
                View dialogView = inflater.inflate(R.layout.dialog_addsize, null);
                builder.setView(dialogView);
                EditText ed_nameProp = dialogView.findViewById(R.id.ed_name);
                EditText ed_MountProp = dialogView.findViewById(R.id.ed_mount);
                Button btn_thoat = dialogView.findViewById(R.id.btn_thoat);
                Button btn_them = dialogView.findViewById(R.id.btn_them);


                // Thiết lập các thành phần trong dialog
                // Ví dụ: Lấy tham chiếu tới các nút hoặc thành phần trong layout và xử lý sự kiện tương ứng

                // Tạo dialog
                AlertDialog dialog = builder.create();


                btn_thoat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btn_them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DTO_properties propertiesDTO = new DTO_properties();
                        propertiesDTO.setNameproperties(ed_nameProp.getText().toString());
                        propertiesDTO.setAmount(Integer.parseInt(ed_MountProp.getText().toString()));

                        selectedProp.add(propertiesDTO);
//                        propertiesDTO.setId_product(idProduct);
//                        startActivity(new Intent(AddProductActivity.this,AddProductActivity.class));
//                        callAddProp(propertiesDTO);
                        int i =0;

                        for (DTO_properties item : selectedProp) {

                            i += item.getAmount();
                        }
                        if (selectedProp.size() != 0) {
                            binding.edtRudProductAmount.setText(String.valueOf(i));
                            binding.edtRudProductAmount.setEnabled(false);
                        }





                        binding.rclvSize.setAdapter(propAdapter);
                        propAdapter.notifyDataSetChanged();


                        dialog.dismiss();
                    }

                });


                dialog.show();
            }
        });


    }

    public void addProduct() {
        trangThai();
        binding.btnRudProductAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callapiRes();

            }
        });
        binding.ivProductToolbarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callapiRes();

            }
        });

    }

    private void callapiRes() {

        DTO_SanPham dtoSanPham = new DTO_SanPham();
        dtoSanPham.setId_shop(idshop);
        dtoSanPham.setNameproduct(Objects.requireNonNull(binding.edtRudProductName.getText()).toString());
        dtoSanPham.setId_category(categoryDTO.getId());
        dtoSanPham.setListImage(selectedImageList);
        dtoSanPham.setListProp(selectedProp);
        int i =0;
        int inputText;
        for (DTO_properties item : selectedProp) {

            i += item.getAmount();
        }
        if (selectedProp.size() != 0) {
            dtoSanPham.setAmount(i);
//            binding.edtRudProductAmount.setText(String.valueOf(i));
//            total = i;
//            binding.edtRudProductAmount.setEnabled(false);
        } else {
            binding.edtRudProductAmount.setEnabled(true);
            dtoSanPham.setAmount(Integer.parseInt(Objects.requireNonNull(binding.edtRudProductAmount.getText()).toString()));
        }
//        dtoSanPham.setAmount(total);

        dtoSanPham.setPrice(Integer.parseInt(Objects.requireNonNull(binding.edtRudProductPrice.getText()).toString()));
        dtoSanPham.setDescription(Objects.requireNonNull(binding.edtRudProductDescription.getText()).toString());
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        dtoSanPham.setTime_product(String.valueOf(currentTime));

        callAddProduct(dtoSanPham);

    }

    private void trangThai() {
        binding.edtRudProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                check();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.edtRudProductDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                check();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.edtRudProductAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                check();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.edtRudProductPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                check();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        check();
    }


    private void check() {

        if (binding.edtRudProductName.getText().toString().trim().isEmpty() || binding.edtRudProductAmount.getText().toString().trim().isEmpty()
                || binding.edtRudProductPrice.getText().toString().trim().isEmpty() || binding.edtRudProductDescription.getText().toString().trim().isEmpty()) {
            binding.tvNote.setVisibility(View.VISIBLE);
            binding.btnRudProductAdd.setEnabled(false);
            binding.btnRudProductAdd.setAlpha(0.3f);
            binding.ivProductToolbarAdd.setEnabled(false);
            binding.ivProductToolbarAdd.setAlpha(0.3f);

        } else {
            binding.tvNote.setVisibility(View.GONE);
            binding.btnRudProductAdd.setEnabled(true);
            binding.btnRudProductAdd.setAlpha(1.0f);
            binding.ivProductToolbarAdd.setEnabled(true);
            binding.ivProductToolbarAdd.setAlpha(1.0f);
        }
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
        Call<DTO_SanPham> objCall = sanPhamInterface.addProduct(dtoSanPham);
        objCall.enqueue(new Callback<DTO_SanPham>() {
            @Override
            public void onResponse(@NonNull Call<DTO_SanPham> call, Response<DTO_SanPham> response) {

//                 idProduct =   response.body().getId();
                showTimedDialog();
//                Toast.makeText(AddProductActivity.this, "Sản phẩm đã được thêm vào cửa hàng.", Toast.LENGTH_SHORT).show();
                final int displayTime = 3000;

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(AddProductActivity.this, QlspActivity.class));
                    finishAffinity();
                }
            }, displayTime);


//                Log.d("TAG", "onResponse: oooooo"+response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DTO_SanPham> call, Throwable t) {
//                showTimedDialog2();
                Log.d("TAG", "onFailure: "+t.getMessage());

            }

        });
    }


    void callCategory() {

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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

    private void showTimedDialog() {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
            builder.setTitle("Thông báo");
            builder.setMessage("Thêm thành công");
            final AlertDialog dialog = builder.create();

            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(R.color.bgrthemthanhcong);
            }

            final int displayTime = 2000;

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }, displayTime);

            // Hiển thị dialog sau khi đã sẵn sàng tự động đóng
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    }, displayTime);
                }
            });

            dialog.show();
        }
    }
    private void showTimedDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
        builder.setTitle("Thông báo");
        builder.setMessage("Thêm sản phẩm không thành công ");
        final AlertDialog dialog = builder.create();

        final int displayTime = 2000; // Thời gian hiển thị dialog (2 giây)
        dialog.show(); // Hiển thị dialog
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!AddProductActivity.this.isFinishing() && dialog.isShowing()) {
                    dialog.dismiss(); // Đóng dialog khi hoạt động vẫn đang hoạt động và dialog đang hiển thị
                }
            }
        }, displayTime);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
        Log.d("TAG1", "openImagePicker: " + selectedImageList);
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