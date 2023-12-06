package com.example.cosplay_suit_app.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_buynow;
import com.example.cosplay_suit_app.DTO.CartShopManager;
import com.example.cosplay_suit_app.DTO.DTO_Address;
import com.example.cosplay_suit_app.DTO.DTO_buynow;
import com.example.cosplay_suit_app.DTO.ProfileDTO;
import com.example.cosplay_suit_app.DTO.TotalPriceManager;
import com.example.cosplay_suit_app.Interface_retrofit.Bill_interface;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.MainActivity;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.ThanhtoanVNpay.DTO_thanhtoan;
import com.example.cosplay_suit_app.ThanhtoanVNpay.DTO_vnpay;
import com.example.cosplay_suit_app.ThanhtoanVNpay.Vnpay_Retrofit;
import com.example.cosplay_suit_app.ThanhtoanVNpay.WebViewThanhtoan;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;
import com.example.cosplay_suit_app.bill.controller.Dialogthongbao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuynowActivity extends AppCompatActivity{
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    static final String BASE_URL_VNPAY = url +"/payment/";
    String TAG = "buynowactivity";
    ImageView img_back;
    TextView tv_tongtien, idchonphuongthuc, tv_hoten,tv_sdt,tv_diachi;
    Button btnbuynow;
    List<DTO_buynow> list;
    Adapter_buynow arrayAdapter;
    RecyclerView recyclerView;
    private TotalPriceManager totalPriceManager;
    Bill_controller billController = new Bill_controller(this);
    Set<String> idShopSet = CartShopManager.getInstance().getListidshop();
    List<String> idShopList;
    String id, hoten, sodienthoai, diachi,checkphuongthuc, idaddress;
    ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buynow);
        Anhxa();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        list = new ArrayList<>();
        arrayAdapter = new Adapter_buynow(list, (Context) BuynowActivity.this);
        recyclerView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        id = sharedPreferences.getString("id","");

        btnbuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hoten.equals("") || sodienthoai.equals("") || diachi.equals("")){
                    String tile = "Thông báo mua hàng";
                    String msg = "Bạn phải chọn điền đầy đủ địa chỉ";
                    Dialogthongbao.showSuccessDialog(BuynowActivity.this,tile, msg);
                }else {
                    if (checkphuongthuc != null) {
                        if (checkphuongthuc.equals("thanhtoansau")) {
                            UUID uuid = UUID.randomUUID();
                            String vnp_TxnRef = uuid.toString().trim();
                            DTO_thanhtoan dtovnpay = new DTO_thanhtoan();
                            dtovnpay.setVnp_CardType("Thanh toán khi nhận hàng");
                            dtovnpay.setVnp_Amount(String.valueOf(totalPriceManager.getTotalOrderPrice()));
                            dtovnpay.setVnp_TxnRef(vnp_TxnRef);
                            Bill_controller billController = new Bill_controller(BuynowActivity.this);
                            billController.AddThanhtoan(dtovnpay, new Bill_controller.ApiAddThanhtoan() {
                                @Override
                                public void onApiAddThanhtoan(DTO_thanhtoan dtoThanhtoan) {
                                    DTO_Address dtoAddress = new DTO_Address();
                                    dtoAddress.setAddress(diachi);
                                    dtoAddress.setFullname(hoten);
                                    dtoAddress.setPhone(sodienthoai);
                                    billController.Add_address(dtoAddress, new Bill_controller.ApiAddress() {
                                        @Override
                                        public void onApiAddress(DTO_Address dto_address) {
                                            idaddress = dto_address.get_id();
                                            arrayAdapter.performActionOnAllItems(dtoThanhtoan.getIdthanhtoan(), idaddress);
                                        }
                                    });
                                }
                            });


                            List<String> list1 = totalPriceManager.getListcart();
                            billController.Upsoluongproduct(list1);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(BuynowActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            },4000);
                        }
                        if (checkphuongthuc.equals("thanhtoanvnpay")) {
                            DTO_vnpay dtovnpay = new DTO_vnpay();
                            dtovnpay.setAmount(totalPriceManager.getTotalOrderPrice());
                            dtovnpay.setBankCode("NCB");
                            postthamso(dtovnpay);
                        }
                    }else {
                        String tile = "Thông báo mua hàng";
                        String msg = "Bạn phải chọn phương thức";
                        Dialogthongbao.showSuccessDialog(BuynowActivity.this,tile, msg);
                    }
                }
            }
        });
        totalPriceManager = TotalPriceManager.getInstance();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tv_tongtien.setText(decimalFormat.format(totalPriceManager.getTotalOrderPrice()) + " VND");

        getShopBuynow(id);
        idchonphuongthuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogchonthanhtoan();
            }
        });
        idShopList = new ArrayList<>(idShopSet);

        for (int i = 0; i < idShopList.size(); i++) {
            Log.d(TAG, "idShop: " + idShopList.get(i));
            Set<String> idCartSet = CartShopManager.getInstance().getCartListForShop(idShopList.get(i));
            List<String> idCartList = new ArrayList<>(idCartSet);

            for (int j = 0; j < idCartList.size(); j++) {
                Log.d(TAG, "idCart: " + idCartList.get(j));
            }
        }

    }
    public void Anhxa(){
        img_back = findViewById(R.id.id_back);
        btnbuynow = findViewById(R.id.btn_buynow);
        recyclerView = findViewById(R.id.rcv_cart);
        tv_tongtien = findViewById(R.id.tv_tongtien);
        idchonphuongthuc = findViewById(R.id.idchonphuongthuc);
        tv_hoten = findViewById(R.id.tv_hoten);
        tv_sdt = findViewById(R.id.tv_sdt);
        tv_diachi = findViewById(R.id.tv_diachi);
    }

    public void getShopBuynow(String id){
        // tạo gson
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
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        CartOrderInterface billInterface = retrofit.create(CartOrderInterface.class);

        // tạo đối tượng
        Call<List<DTO_buynow>> objCall = billInterface.getShopBuynow(id);
        objCall.enqueue(new Callback<List<DTO_buynow>>() {
            @Override
            public void onResponse(Call<List<DTO_buynow>> call, Response<List<DTO_buynow>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    List<DTO_buynow> dtoBuynows = response.body();
                    // Kiểm tra nếu danh sách chưa được thêm
                    for (DTO_buynow dtoBuyNow : dtoBuynows) {
                        String idshop = dtoBuyNow.get_id();
                        if (idShopList.contains(idshop)){
                            list.add(dtoBuyNow);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(BuynowActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DTO_buynow>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

    public void dialogchonthanhtoan(){
        Dialog dialog = new Dialog(BuynowActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chonthanhtoan);

        //thoát khỏi dialog
        ImageView icback = dialog.findViewById(R.id.id_back);
        icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button idchon = dialog.findViewById(R.id.id_chon);

        idchon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //Xử lý công việc trong radiogroup
        RadioGroup radioGroup = dialog.findViewById(R.id.rdo_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                // Lấy RadioButton đã chọn
                RadioButton selectedRadioButton = dialog.findViewById(checkedId);

                // Lấy ID của RadioButton đã chọn
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // Xử lý công việc dựa trên ID của RadioButton
                if (selectedId == R.id.rdo_thanhtoansau) {
                    // Xử lý công việc rdo_thanhtoansau
                    idchonphuongthuc.setText("Thanh toán khi nhận hàng");
                    checkphuongthuc = "thanhtoansau";
                } else if (selectedId == R.id.rdo_thanhtoanVnpay) {
                    // Xử lý công việc rdo_thanhtoanVnpay
                    idchonphuongthuc.setText("Thanh toán VNpay");
                    checkphuongthuc = "thanhtoanvnpay";
                }
            }
        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        // Chiều rộng full màn hình
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        // Chiều cao full màn hình
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
    }

    public void postthamso(DTO_vnpay objUser){
        //tạo dđối towngj chuyển đổi kiểu dữ liệu
        Gson gson = new GsonBuilder().setLenient().create();
        //Tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( BASE_URL_VNPAY )
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //Khởi tạo Interface
        Vnpay_Retrofit userInterface = retrofit.create(Vnpay_Retrofit.class);
        //Tạo Call
        Call<DTO_vnpay> objCall = userInterface.createpaymenturl(objUser);

        //Thực hiệnửi dữ liệu lên server
        objCall.enqueue(new Callback<DTO_vnpay>() {
            @Override
            public void onResponse(Call<DTO_vnpay> call, Response<DTO_vnpay> response) {
                //Kết quẳ server trả về ở đây
                if(response.isSuccessful()){
                    DTO_vnpay dtoVnpay = response.body();
                    Log.d(TAG, "responseData.getData(): "+ dtoVnpay.getDataurl());
                    Intent intent = new Intent(BuynowActivity.this, WebViewThanhtoan.class);
                    intent.putExtra("paymentUrl", dtoVnpay.getDataurl());
                    intent.putExtra("locactivity", "buynowactivity");
                    webViewLauncher.launch(intent);
                }else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<DTO_vnpay> call, Throwable t) {
                //Nếu say ra lỗi sẽ thông báo ở đây
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }
    private final ActivityResultLauncher<Intent> webViewLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Nhận dữ liệu từ Intent và xử lý ở đây
                    String vnp_Amount = result.getData().getStringExtra("vnp_Amount");
                    String vnp_BankCode = result.getData().getStringExtra("vnp_BankCode");
                    String vnp_BankTranNo = result.getData().getStringExtra("vnp_BankTranNo");
                    String vnp_CardType = result.getData().getStringExtra("vnp_CardType");
                    String vnp_OrderInfo = result.getData().getStringExtra("vnp_OrderInfo");
                    String vnp_PayDate = result.getData().getStringExtra("vnp_PayDate");
                    String vnp_ResponseCode = result.getData().getStringExtra("vnp_ResponseCode");
                    String vnp_TmnCode = result.getData().getStringExtra("vnp_TmnCode");
                    String vnp_TransactionNo = result.getData().getStringExtra("vnp_TransactionNo");
                    String vnp_TransactionStatus = result.getData().getStringExtra("vnp_TransactionStatus");
                    String vnp_TxnRef = result.getData().getStringExtra("vnp_TxnRef");
                    String vnp_SecureHash = result.getData().getStringExtra("vnp_SecureHash");

                    Log.d(TAG, "vnp_Amount: " + vnp_Amount);
                    Log.d(TAG, ": vnp_BankCode" +vnp_BankCode);
                    Log.d(TAG, ":vnp_BankTranNo " +vnp_BankTranNo);
                    Log.d(TAG, ": vnp_CardType" +vnp_CardType);
                    Log.d(TAG, ": vnp_OrderInfo" +vnp_OrderInfo);
                    Log.d(TAG, ": vnp_PayDate" +vnp_PayDate);
                    Log.d(TAG, ": vnp_ResponseCode" +vnp_ResponseCode);
                    Log.d(TAG, ": vnp_TmnCode" +vnp_TmnCode);
                    Log.d(TAG, ": vnp_TransactionStatus" +vnp_TransactionStatus);
                    Log.d(TAG, ": vnp_TransactionNo" +vnp_TransactionNo);
                    Log.d(TAG, ": vnp_TxnRef" +vnp_TxnRef);
                    Log.d(TAG, ": vnp_SecureHash" +vnp_SecureHash);

                    DTO_thanhtoan dtovnpay = new DTO_thanhtoan();
                    dtovnpay.setVnp_CardType(vnp_CardType);
                    dtovnpay.setVnp_Amount(String.valueOf(Integer.parseInt(vnp_Amount) /100));
                    dtovnpay.setVnp_BankCode(vnp_BankCode);
                    dtovnpay.setVnp_BankTranNo(vnp_BankTranNo);
                    dtovnpay.setVnp_OrderInfo(vnp_OrderInfo);
                    dtovnpay.setVnp_PayDate(vnp_PayDate);
                    dtovnpay.setVnp_ResponseCode(vnp_ResponseCode);
                    dtovnpay.setVnp_TmnCode(vnp_TmnCode);
                    dtovnpay.setVnp_TransactionNo(vnp_TransactionNo);
                    dtovnpay.setVnp_TransactionStatus(vnp_TransactionStatus);
                    dtovnpay.setVnp_SecureHash(vnp_SecureHash);
                    dtovnpay.setVnp_TxnRef(vnp_TxnRef);
                    Bill_controller billController = new Bill_controller(this);
                    billController.AddThanhtoan(dtovnpay, new Bill_controller.ApiAddThanhtoan() {
                        @Override
                        public void onApiAddThanhtoan(DTO_thanhtoan dtoThanhtoan) {
                            DTO_Address dtoAddress = new DTO_Address();
                            dtoAddress.setAddress(diachi);
                            dtoAddress.setFullname(hoten);
                            dtoAddress.setPhone(sodienthoai);
                            billController.Add_address(dtoAddress, new Bill_controller.ApiAddress() {
                                @Override
                                public void onApiAddress(DTO_Address dtoAddress) {
                                    idaddress = dtoAddress.get_id();
                                    arrayAdapter.performActionOnAllItems(dtoThanhtoan.getIdthanhtoan(), idaddress);
                                }
                            });
                            List<String> list1 = totalPriceManager.getListcart();
                            billController.Upsoluongproduct(list1);
                        }
                    });
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();// Kết thúc WebViewThanhtoan
                            // Chuyển đến hoạt động chính (Trang chủ)
                            Intent intent = new Intent(BuynowActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }, 5000);
                }
            }
    );
    public static String formatDateTime(Date date, String format) {
        // Tạo đối tượng SimpleDateFormat với định dạng
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        // Chuyển đổi Date thành chuỗi định dạng
        return sdf.format(date);
    }
    public void diachi(){
        billController.Getidaddress(id, new Bill_controller.ApiResponseCallback() {
            @Override
            public void onApiGetidaddress(ProfileDTO profileDTO) {
                hoten = profileDTO.getFullname();
                sodienthoai =profileDTO.getPhone();
                diachi = profileDTO.getDiachi();
                tv_hoten.setText(hoten);
                tv_sdt.setText(sodienthoai);
                tv_diachi.setText(diachi);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        diachi();
    }
}