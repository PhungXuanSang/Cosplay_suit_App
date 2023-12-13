package com.example.cosplay_suit_app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_ImageList;
import com.example.cosplay_suit_app.Adapter.DanhgiaAdapter;
import com.example.cosplay_suit_app.Adapter.ImageAdapter;
import com.example.cosplay_suit_app.Adapter.PropAdapter;
import com.example.cosplay_suit_app.Adapter.QlspAdapter;
import com.example.cosplay_suit_app.Adapter.SpinnerCategotyAdapter;
import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.DTO.ItemImageDTO;
import com.example.cosplay_suit_app.Interface_retrofit.CategoryInterface;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.databinding.ActivityAddProductBinding;
import com.example.cosplay_suit_app.databinding.ActivityDetailProductBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
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

public class DetailProductActivity extends AppCompatActivity implements PropAdapter.Onclick {
    static String url = API.URL;
    static final String BASE_URL_CAT = url + "/category/api/";
    static final String BASE_URL = url + "/product/";
    private ActivityDetailProductBinding binding;
    String idProduct, nameCategory;
    String id_shop, id_category, nameproduct, image, description, time_product, listImageJson, stringsize;
    int price, amount, sold;
    private static final int REQUEST_IMAGE_PICK = 1;
    private List<ItemImageDTO> selectedImageList;
    boolean status;
    List<ItemImageDTO> listImage;
    List<DTO_properties> listProp;
    private ImageAdapter adapterImageList;
    StorageReference storageReference;
    FirebaseStorage storage;
    PropAdapter propAdapter;

    SpinnerCategotyAdapter categotyAdapter;
    ArrayList<CategoryDTO> listLoai = new ArrayList<>();
    private CategoryDTO selectedCategory;
    int suaamount;
    Uri uri;
    private ArrayList<Uri> uriList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        idProduct = intent.getStringExtra("id_product");
        nameproduct = intent.getStringExtra("name");
        price = intent.getIntExtra("price", 0);
        amount = intent.getIntExtra("slkho", 0);
        image = intent.getStringExtra("image");
        description = intent.getStringExtra("about");
        id_shop = intent.getStringExtra("id_shop");
        time_product = intent.getStringExtra("time_product");
        id_category = intent.getStringExtra("id_category");
        status = intent.getBooleanExtra("status", true);
//        status = Boolean.parseBoolean(intent.getStringExtra("status"));
        sold = intent.getIntExtra("sold", 0);
        // Lấy chuỗi JSON từ Intent
        listImageJson = intent.getStringExtra("listImage");
        // Chuyển chuỗi JSON thành danh sách đối tượng
        listImage = new Gson().fromJson(listImageJson,
                new TypeToken<List<ItemImageDTO>>() {
                }.getType());
        // Lấy chuỗi JSON từ Intent
        stringsize = intent.getStringExtra("listsize");
// Khởi tạo và thiết lập RecyclerView
        listProp = new Gson().fromJson(stringsize,
                new TypeToken<List<DTO_properties>>() {
                }.getType());

        selectedImageList = new ArrayList<>();
        adapterImageList = new ImageAdapter(listImage, this);
        binding.rclvImage.setAdapter(adapterImageList);
        binding.rclvImage.setLayoutManager(new LinearLayoutManager(DetailProductActivity.this, LinearLayoutManager.HORIZONTAL, false));

        propAdapter = new PropAdapter(listProp, this, this::onclikSize);
        binding.rclvSize.setAdapter(propAdapter);
        binding.rclvSize.setLayoutManager(new LinearLayoutManager(DetailProductActivity.this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.rclvSize.addItemDecoration(dividerItemDecoration);
        categotyAdapter = new SpinnerCategotyAdapter(this, R.layout.item_spinner_category, listLoai);
        categotyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnAddProductLoai.setAdapter(categotyAdapter);


        callCategory();


        if (status == false) {
            binding.tvDetailProductStatus.setText("Không thể bán");
            binding.lldetailProductStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.material_orange_400));

        } else {
            binding.tvDetailProductStatus.setText("Có thể bán");
            binding.lldetailProductStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.material_green_400));

        }

        binding.ivAddProductAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        binding.edtRudProductPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String priceStr = editable.toString();

                if (!priceStr.isEmpty()) {
                    // Xóa dấu phẩy từ chuỗi
                    String priceWithoutComma = priceStr.replaceAll(",", "");

                    try {
                        // Chuyển đổi chuỗi thành số
                        price = Integer.parseInt(priceWithoutComma);

                        // Kiểm tra giá trị giới hạn
                        if (price < 1) {
                            price = 1;
                        } else if (price > 99999999) {
                            price = 99999999;
                        }

                        // Nếu giá trị đã thay đổi sau khi xử lý, cập nhật EditText
                        if (price != Integer.parseInt(priceStr)) {
                            binding.edtRudProductPrice.setText(String.valueOf(price));
                            binding.edtRudProductPrice.setSelection(binding.edtRudProductPrice.getText().length());
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        binding.ivProductToolbarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
//                startActivity(new Intent(DetailProductActivity.this,QlspActivity.class));
            }
        });
        selectedCategory = new CategoryDTO();
        binding.spnAddProductLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedCategory = listLoai.get(i);
                Log.d("TAG", "onItemSelected: " + selectedCategory.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(DetailProductActivity.this, "222222222", Toast.LENGTH_SHORT).show();
            }
        });
        onclick();
//        getListCat();
        getInfoProduct();
        status();

    }

    private long parseLongSafely(String number) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void openImagePicker() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null) {
                // Chọn một ảnh
                Uri imageUri = data.getData();
                uriList.add(imageUri);
                Log.d("DetailProductActivity", "Single image selected. New size of uriList: " + uriList.size());
            } else if (data.getClipData() != null) {
                // Chọn nhiều ảnh
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    uriList.add(imageUri);
                }

                // Gọi phương thức uploadImage() hoặc xử lý các URI ở đây
                uploadImages(uriList);

                // Cập nhật RecyclerView hoặc Adapter của bạn nếu cần
                adapterImageList.notifyDataSetChanged();

                // Log để kiểm tra
                Log.d("DetailProductActivity", "Multiple images selected. New size of uriList: " + uriList.size());
            }
        }
    }


    private void uploadImages(List<Uri> uriList) {
        if (uriList != null && !uriList.isEmpty()) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Tạo đối tượng StorageReference để tham chiếu đến Firebase Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            // Duyệt qua từng URI và tải lên Firebase Storage
            for (Uri uri : uriList) {
                // Tạo đường dẫn lưu trữ file trong Firebase Storage
                StorageReference imageRef = storageReference.child("images/" + UUID.randomUUID().toString() + ".jpg");

                // Tải ảnh lên Firebase Storage
                imageRef.putFile(uri)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Lấy URL ảnh sau khi tải lên thành công
                            imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                // Tạo đối tượng ItemImageDTO với URL ảnh và thêm vào danh sách
                                ItemImageDTO itemImage = new ItemImageDTO(downloadUri.toString());
                                listImage.add(itemImage);

                                // Cập nhật RecyclerView
                                adapterImageList.notifyDataSetChanged();

                                // Log để kiểm tra
                                Log.d("DetailProductActivity", "Image uploaded successfully. New size of selectedImageList: " + selectedImageList.size());
                            });
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi tải lên ảnh
                            progressDialog.dismiss();

                            // Log để kiểm tra
                            Log.e("DetailProductActivity", "Image upload failed", e);
                        })
                        .addOnProgressListener(taskSnapshot -> {
                            // Cập nhật tiến trình tải lên (nếu cần)
                        });
            }

            // Ẩn progressDialog sau khi đã tải lên tất cả ảnh
            progressDialog.dismiss();
        }
    }

    private void onclick() {
        DTO_SanPham dtoSanPham = new DTO_SanPham();
        binding.ivProductToolbarUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailProductActivity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn chắc chắn muốn sửa sản phẩm?");

                // Nút tích cực (ví dụ: Đồng ý)
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.edtDeltaProductName.setEnabled(true);
                        binding.edtRudProductDescription.setEnabled(true);
                        binding.tvDeltaProductMount.setEnabled(true);
                        binding.edtRudProductPrice.setEnabled(true);
                        binding.tvDetailProductStatus.setEnabled(true);
                        binding.ivdetailProductAddSize.setEnabled(true);
                        binding.llbtnLuu.setVisibility(View.VISIBLE);
                        // Xử lý sự kiện khi người dùng nhấn Đồng ý
                    }
                });

                // Nút tiêu cực (ví dụ: Hủy)
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý sự kiện khi người dùng nhấn Hủy
                        dialog.dismiss();
                    }
                });

                // Hiển thị hộp thoại
                builder.show();


            }
        });
        binding.btnRudProductAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.edtDeltaProductName.setEnabled(false);
                binding.edtRudProductDescription.setEnabled(false);
                binding.tvDeltaProductMount.setEnabled(false);
                binding.edtRudProductPrice.setEnabled(false);
                binding.tvDetailProductStatus.setEnabled(false);
                binding.ivdetailProductAddSize.setEnabled(false);
                binding.llbtnLuu.setVisibility(View.GONE);
                dtoSanPham.setNameproduct(String.valueOf(binding.edtDeltaProductName.getText()));
                dtoSanPham.setPrice(price);
                dtoSanPham.setDescription(binding.edtRudProductDescription.getText().toString());
//                dtoSanPham.setId_category();
                dtoSanPham.setId_category(selectedCategory.getId());
                dtoSanPham.setListImage(listImage);
                dtoSanPham.setListProp(listProp);
                //
                UpdateInfo(dtoSanPham);
//                callapiRes();
                Log.d("TAG", "onClicksssssssssssssss");
            }
        });

        binding.ivdetailProductAddSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailProductActivity.this);
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

                ed_MountProp.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String priceStr = editable.toString();

                        if (!priceStr.isEmpty()) {
                            amount = Integer.parseInt(priceStr);

                            if (amount < 1) {
                                amount = 1;
                            } else if (amount > 99999999) {
                                amount = 99999999;
                            }

                            if (amount != parseLongSafely(priceStr)) {
                                ed_MountProp.setText(String.valueOf(amount));
                                ed_MountProp.setSelection(ed_MountProp.getText().length());
                            }
                        }


                    }
                });
                btn_thoat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btn_them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String enteredName = ed_nameProp.getText().toString().trim();

                        if (!isNameExists(enteredName)) {
                            // Tên không trùng, thêm vào danh sách và kiểm tra tổng số lượng
                            DTO_properties propertiesDTO = new DTO_properties();
                            propertiesDTO.setAmount(Integer.parseInt(String.valueOf(amount)));
                            propertiesDTO.setNameproperties(enteredName);

                            listProp.add(propertiesDTO);

                            int totalAmount = 0;
                            for (DTO_properties item : listProp) {
                                totalAmount += item.getAmount();
                            }

                            if (listProp.size() != 0) {
                                binding.tvDeltaProductMount.setText(String.valueOf(totalAmount));
                                binding.tvDeltaProductMount.setEnabled(false);
                            }

                            binding.rclvSize.setAdapter(propAdapter);
                            propAdapter.notifyDataSetChanged();

                            dialog.dismiss();
                        } else {
                            // Hiển thị thông báo khi tên trùng
                            Toast.makeText(DetailProductActivity.this, "Tên kích thước không được phép trùng lặp", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                dialog.show();
            }
        });


    }

    private boolean isNameExists(String name) {
        for (DTO_properties item : listProp) {
            if (item.getNameproperties().equalsIgnoreCase(name)) {
                return true; // Tên đã tồn tại
            }
        }
        return false; // Tên không tồn tại
    }

    private void getInfoProduct() {
        binding.edtDeltaProductName.setText(nameproduct);
        binding.edtRudProductDescription.setText(description);
        binding.tvDeltaProductMount.setText(String.valueOf(amount));
//        binding.tvDeltaProductPrice.setText(String.valueOf(price));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        binding.edtRudProductPrice.setText(decimalFormat.format(price));
//        binding.tvDeltaProductLoai.setText(nameCategory);
    }


    private void status() {

        DTO_SanPham dtoSanPham = new DTO_SanPham();

        binding.tvDetailProductStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (status == false) {
                    binding.tvDetailProductStatus.setText("Có thể bán");
                    binding.lldetailProductStatus.setBackgroundColor(ContextCompat.getColor(DetailProductActivity.this, R.color.material_green_400));
                    status = true;
                } else {
                    binding.tvDetailProductStatus.setText("Không thể bán");
                    binding.lldetailProductStatus.setBackgroundColor(ContextCompat.getColor(DetailProductActivity.this, R.color.material_orange_400));
                    status = false;
                }
                dtoSanPham.setStatus(status);
                UpdateStatus(dtoSanPham);

            }
        });


    }

    private void UpdateStatus(DTO_SanPham dtoSanPham) {
        Gson gson = new GsonBuilder().setLenient().create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();


        SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);


        Call<DTO_SanPham> objCall = sanPhamInterface.updateStatus(idProduct, dtoSanPham);
        objCall.enqueue(new Callback<DTO_SanPham>() {
            @Override
            public void onResponse(Call<DTO_SanPham> call, Response<DTO_SanPham> response) {
                if (response.isSuccessful()) {


                } else {
                    Toast.makeText(DetailProductActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DTO_SanPham> call, Throwable t) {
                Log.d("TAG", "ôppopopopop: " + t.getMessage());
            }
        });


    }

    private void UpdateInfo(DTO_SanPham dtoSanPham) {
        Gson gson = new GsonBuilder().setLenient().create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();


        SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);


        Call<DTO_SanPham> objCall = sanPhamInterface.updateProductNamePriceDec(idProduct, dtoSanPham);
        objCall.enqueue(new Callback<DTO_SanPham>() {
            @Override
            public void onResponse(Call<DTO_SanPham> call, Response<DTO_SanPham> response) {
                if (response.isSuccessful()) {
                    showTimedDialog();
                } else {
                    Toast.makeText(DetailProductActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DTO_SanPham> call, Throwable t) {
                Log.d("TAG", "ôppopopopop: " + t.getMessage());
            }
        });


    }

    private void showTimedDialog() {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailProductActivity.this);
            builder.setTitle("Thông báo");
            builder.setMessage("Sửa thành công");
            final AlertDialog dialog = builder.create();

            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(R.color.bgrthemthanhcong);
            }

            final int displayTime = 1000;

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


    private void checkAndSetCategory(String categoryId) {
        for (int i = 0; i < listLoai.size(); i++) {
            CategoryDTO category = listLoai.get(i);
            if (category.getId().equals(categoryId)) {
                selectedCategory = category;
                binding.spnAddProductLoai.setSelection(i);
                break;
            }
        }
    }


    void callCategory() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);

        Call<List<CategoryDTO>> objCall = sanPhamInterface.ListCategory();
        objCall.enqueue(new Callback<List<CategoryDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryDTO>> call, @NonNull Response<List<CategoryDTO>> response) {

                if (response.body() != null) {
                    listLoai.addAll(response.body());


                    int specificItemPosition = findPositionById(id_category);


                    if (specificItemPosition != -1) {
                        CategoryDTO specificItem = listLoai.remove(specificItemPosition);
                        listLoai.add(0, specificItem);
                    }

                    categotyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryDTO>> call, Throwable t) {
                // Xử lý khi gặp lỗi kết nối
            }
        });
    }

    private int findPositionById(String categoryId) {
        for (int i = 0; i < listLoai.size(); i++) {
            CategoryDTO category = listLoai.get(i);
            if (category.getId().equals(categoryId)) {
                return i; // Trả về vị trí nếu tìm thấy
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }


    @Override
    public void onclikSize(DTO_properties dtoProperties) {

        UpdateSize(dtoProperties);
    }

    void UpdateSize(DTO_properties dtoProperties) {

        final Dialog dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.dialog_sua_size);
        dialog1.setCancelable(false);

        Window window = dialog1.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (dialog1 != null && dialog1.getWindow() != null) {
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }

        EditText ed_name = dialog1.findViewById(R.id.ed_namesua);
        EditText ed_Amount = dialog1.findViewById(R.id.ed_mountsua);

        Button btn_sua = dialog1.findViewById(R.id.btn_suaSize);
        Button huy = dialog1.findViewById(R.id.btn_thoatsize);

        ed_name.setText(dtoProperties.getNameproperties());
        String soluong = String.valueOf(dtoProperties.getAmount());
        ed_Amount.setText(soluong);
        ed_Amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String priceStr = editable.toString();

                if (!priceStr.isEmpty()) {
                    amount = Integer.parseInt(priceStr);

                    if (amount < 1) {
                        amount = 1;
                    } else if (amount > 99999999) {
                        amount = 99999999;
                    }

                    if (amount != parseLongSafely(priceStr)) {
                        ed_Amount.setText(String.valueOf(amount));
                        ed_Amount.setSelection(ed_Amount.getText().length());
                    }
                }

            }
        });

        btn_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_name.getText().toString().trim().length() == 0) {
                    ed_name.setError("Không được để trống!!");
                } else if (ed_Amount.getText().toString().trim().length() == 0) {
                    ed_Amount.setError("Không được để trống!!");
                } else {
                    dtoProperties.setNameproperties(ed_name.getText().toString());
                    dtoProperties.setAmount(Integer.parseInt(ed_Amount.getText().toString()));

                    Gson gson = new GsonBuilder().setLenient().create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    SanPhamInterface truyenInterface = retrofit.create(SanPhamInterface.class);

                    Call<List<DTO_properties>> objCall = truyenInterface.updateSize(idProduct, dtoProperties.getNameproperties(), dtoProperties);

                    objCall.enqueue(new Callback<List<DTO_properties>>() {
                        @Override
                        public void onResponse(Call<List<DTO_properties>> call, Response<List<DTO_properties>> response) {
                            if (response.isSuccessful()) {
//                                binding.tvDeltaProductMount.setText(response.body().getAmount());
                                List<DTO_properties> dtoProperties1 = response.body();

                                listProp.clear();
                                listProp.add((DTO_properties) dtoProperties1);
                                propAdapter.notifyDataSetChanged();

                            } else {
                                // Xử lý khi gọi không thành công
                            }
                        }

                        @Override
                        public void onFailure(Call<List<DTO_properties>> call, Throwable t) {
                            Log.e("TAG", "zzzzzzzzzzzzzzzzzzzzzzzzzz" + t.getLocalizedMessage());
                        }
                    });

                    dialog1.dismiss();

                }
            }
        });

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }
}