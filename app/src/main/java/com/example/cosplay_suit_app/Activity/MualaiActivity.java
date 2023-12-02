package com.example.cosplay_suit_app.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_buynow;
import com.example.cosplay_suit_app.DTO.DTO_Bill;
import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.DTO.ProfileDTO;
import com.example.cosplay_suit_app.DTO.TotalPriceManager;
import com.example.cosplay_suit_app.MainActivity;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.ThanhtoanVNpay.DTO_thanhtoan;
import com.example.cosplay_suit_app.ThanhtoanVNpay.DTO_vnpay;
import com.example.cosplay_suit_app.ThanhtoanVNpay.Vnpay_Retrofit;
import com.example.cosplay_suit_app.ThanhtoanVNpay.WebViewThanhtoan;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;
import com.example.cosplay_suit_app.bill.controller.Dialogthongbao;
import com.example.cosplay_suit_app.bill.controller.Mualai_controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MualaiActivity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL_VNPAY = url +"/payment/";
    String TAG = "buynowactivity";
    ImageView img_back;
    TextView tv_tongtien;
    TextView idchonphuongthuc;
    TextView tv_hoten;
    TextView tv_sdt;
    TextView tv_diachi;
    TextView tvname_product;
    TextView tvsize_product;
    TextView tv_soluong;
    TextView tvprice_product;
    TextView tv_tonggia;
    Button btnbuynow;
    String checkphuongthuc;
    Bill_controller billController = new Bill_controller(this);
    String idproduct, selectedNameProperties, id_shop, id, nameproduct;
    int priceproduct, amount;

    public interface OnAddBillCompleteListener {
        void onAddBillComplete();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mualai);
        Anhxa();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        Log.d("Debug", "Intent idproduct: " + getIntent().getStringExtra("idproduct"));
        Log.d("Debug", "Intent priceproduct: " + getIntent().getIntExtra("priceproduct", 0));
        Log.d("Debug", "Intent amount: " + getIntent().getStringExtra("amount"));
        Log.d("Debug", "Intent selectedNameProperties: " + getIntent().getStringExtra("selectedNameProperties"));
        Log.d("Debug", "Intent id_shop: " + getIntent().getStringExtra("id_shop"));

        selectedNameProperties = intent.getStringExtra("selectedNameProperties");
        idproduct = intent.getStringExtra("idproduct");
        amount = Integer.parseInt(intent.getStringExtra("amount"));
        priceproduct = getIntent().getIntExtra("priceproduct", 0);
        id_shop = intent.getStringExtra("id_shop");
        nameproduct = intent.getStringExtra("nameproduct");

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        tvname_product.setText(nameproduct);
        tvsize_product.setText("Kích thước: "+selectedNameProperties);
        tv_soluong.setText("Số lượng: "+amount);
        tvprice_product.setText("Giá: "+decimalFormat.format(priceproduct));
        tv_tonggia.setText(decimalFormat.format(amount*priceproduct) + " VND");
        tv_tongtien.setText(decimalFormat.format(amount*priceproduct) + " VND");

        SharedPreferences sharedPreferences = this.getSharedPreferences("User", this.MODE_PRIVATE);
        id = sharedPreferences.getString("id","");
        Log.d(TAG, "iduser: " + id);

        btnbuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkphuongthuc != null) {
                    if (checkphuongthuc.equals("thanhtoansau")) {
                        UUID uuid = UUID.randomUUID();
                        String vnp_TxnRef = uuid.toString().trim();
                        // Lấy đối tượng Date hiện tại
                        Date currentDate = new Date();
                        // Định dạng ngày giờ theo yyyyMMddHHmmss
                        String formattedDateTime = formatDateTime(currentDate, "yyyyMMddHHmmss");

                        DTO_thanhtoan dtovnpay = new DTO_thanhtoan();
                        dtovnpay.setVnp_CardType("Thanh toán khi nhận hàng");
                        dtovnpay.setVnp_Amount(String.valueOf(amount*priceproduct));
                        dtovnpay.setVnp_PayDate(formattedDateTime);
                        dtovnpay.setVnp_TxnRef(vnp_TxnRef);
                        Bill_controller billController = new Bill_controller(MualaiActivity.this);
                        billController.AddThanhtoan(dtovnpay);

                        String currentDateTime = getCurrentDateTime();
                        Mualai_controller mualaiController = new Mualai_controller(MualaiActivity.this);
                        DTO_Bill dtoBill = new DTO_Bill();
                        dtoBill.setId_user(id);
                        dtoBill.setId_shop(id_shop);
                        dtoBill.setVnp_TxnRef(vnp_TxnRef);
                        dtoBill.setStatus("Wait");
                        dtoBill.setTotalPayment(amount*priceproduct);
                        dtoBill.setMa_voucher("");
                        dtoBill.setTimestart(currentDateTime);
                        dtoBill.setTimeend("");
                        mualaiController.Addbill(dtoBill);

                        //sửa số lượng sp
                        DTO_properties dtoProperties = new DTO_properties();
                        dtoProperties.setAmount(amount);
                        dtoProperties.setNameproperties(selectedNameProperties);
                        billController.UpdateProduct(idproduct, dtoProperties);
                        mualaiController.setOnAddBillCompleteListener(new OnAddBillCompleteListener() {
                            @Override
                            public void onAddBillComplete() {
                                // Gọi databilldetail khi Addbill đã hoàn thành
                                mualaiController.databilldetail(amount, selectedNameProperties, (amount*priceproduct),idproduct);

                            }
                        });
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MualaiActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        },4000);
                    }
                    if (checkphuongthuc.equals("thanhtoanvnpay")) {
                        DTO_vnpay dtovnpay = new DTO_vnpay();
                        dtovnpay.setAmount((amount*priceproduct));
                        dtovnpay.setBankCode("NCB");
                        postthamso(dtovnpay);
                    }
                }else {
                    String tile = "Thông báo mua hàng";
                    String msg = "Bạn phải chọn phương thức";
                    Dialogthongbao.showSuccessDialog(MualaiActivity.this,tile, msg);
                }
            }
        });

        billController.Getidaddress(id, new Bill_controller.ApiResponseCallback() {
            @Override
            public void onApiGetidaddress(ProfileDTO profileDTO) {
                tv_hoten.setText(profileDTO.getFullname());
                tv_sdt.setText(profileDTO.getPhone());
                tv_diachi.setText(profileDTO.getDiachi());
            }
        });

        idchonphuongthuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogchonthanhtoan();
            }
        });
    }

    public void Anhxa(){
        img_back = findViewById(R.id.id_back);
        btnbuynow = findViewById(R.id.btn_buynow);
        tv_tongtien = findViewById(R.id.tv_tongtien);
        idchonphuongthuc = findViewById(R.id.idchonphuongthuc);
        tv_hoten = findViewById(R.id.tv_hoten);
        tv_sdt = findViewById(R.id.tv_sdt);
        tv_diachi = findViewById(R.id.tv_diachi);
        tvname_product = findViewById(R.id.tvname_product);
        tvsize_product = findViewById(R.id.tvsize_product);
        tv_soluong = findViewById(R.id.tv_soluong);
        tvprice_product = findViewById(R.id.tvprice_product);
        tv_tonggia = findViewById(R.id.tv_tonggia);

    }

    public void dialogchonthanhtoan(){
        Dialog dialog = new Dialog(MualaiActivity.this);
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
                    Intent intent = new Intent(MualaiActivity.this, WebViewThanhtoan.class);
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
                    dtovnpay.setVnp_Amount(vnp_Amount);
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
                    billController.AddThanhtoan(dtovnpay);

                    //Thêm vào bảng bill
                    String currentDateTime = getCurrentDateTime();
                    Mualai_controller mualaiController = new Mualai_controller(MualaiActivity.this);
                    DTO_Bill dtoBill = new DTO_Bill();
                    dtoBill.setId_user(id);
                    dtoBill.setId_shop(id_shop);
                    dtoBill.setVnp_TxnRef(vnp_TxnRef);
                    dtoBill.setStatus("Wait");
                    dtoBill.setTotalPayment(amount*priceproduct);
                    dtoBill.setMa_voucher("");
                    dtoBill.setTimestart(currentDateTime);
                    dtoBill.setTimeend("");
                    mualaiController.Addbill(dtoBill);
                    mualaiController.setOnAddBillCompleteListener(new OnAddBillCompleteListener() {
                        @Override
                        public void onAddBillComplete() {
                            // Gọi databilldetail khi Addbill đã hoàn thành
                            mualaiController.databilldetail(amount, selectedNameProperties, (amount*priceproduct),idproduct);

                        }
                    });
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();// Kết thúc WebViewThanhtoan
                            // Chuyển đến hoạt động chính (Trang chủ)
                            Intent intent = new Intent(MualaiActivity.this, MainActivity.class);
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
    private String getCurrentDateTime() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(currentDate);
    }
}