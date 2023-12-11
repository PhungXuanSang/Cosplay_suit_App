package com.example.cosplay_suit_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.DTO.DTO_voucher;
import com.example.cosplay_suit_app.Interface_retrofit.VoucherInterface;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddVoucher_Activity extends AppCompatActivity {
Button buttonAddProduct;
    static String url = API.URL;
    static final String BASE_URL = url + "/Voucher/";
    TextView ed_content,ed_amount,ed_Discount;
    String id;
    String idshop;
    VoucherInterface voucherInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voucher);
        buttonAddProduct=findViewById(R.id.buttonAddProduct);
        ed_content=findViewById(R.id.ed_content);
        ed_amount=findViewById(R.id.ed_amount);
        ed_Discount=findViewById(R.id.ed_Discount);

        SharedPreferences sharedPreferences2 = this.getSharedPreferences("shops", MODE_PRIVATE);
        idshop = sharedPreferences2.getString("id", "");
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DTO_voucher voucher = new DTO_voucher();
                int discount;
                int amount;

                try {
                    discount = Integer.parseInt(ed_Discount.getText().toString());
                } catch (NumberFormatException e) {
                    // Handle the case where the input is not a valid integer
                    Toast.makeText(AddVoucher_Activity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (discount < 0) {
                    voucher.setDiscount(String.valueOf(1));
                } else if (discount > 100) {
                    voucher.setDiscount(String.valueOf(100));
                } else {
                    voucher.setDiscount(String.valueOf(discount));
                }




                try {
                    amount = Integer.parseInt(ed_amount.getText().toString());
                } catch (NumberFormatException e) {
                    // Handle the case where the input is not a valid integer
                    Toast.makeText(AddVoucher_Activity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                    return; // Add this line to exit the method in case of invalid input
                }

                if (amount < 1) {
                    voucher.setAmount(String.valueOf(1));
                } else {
                    voucher.setAmount(String.valueOf(amount));
                }
                voucher.setContent(ed_content.getText().toString());
//                 voucher.setAmount(ed_amount.getText().toString());
                 voucher.setId_shop(idshop);
                callAddProduct(voucher);
            }
        });


    }

    void callAddProduct(DTO_voucher dtoVoucher) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // sử dụng interface
        voucherInterface = retrofit.create(VoucherInterface.class);
        // tạo đối tượng
        Call<DTO_voucher> objCall = voucherInterface.postVoucherByShop(dtoVoucher);
        objCall.enqueue(new Callback<DTO_voucher>() {
            @Override
            public void onResponse(@NonNull Call<DTO_voucher> call, Response<DTO_voucher> response) {
                startActivity(new Intent(AddVoucher_Activity.this, Voucher_activity.class));
                finishAffinity();
            }
            @Override
            public void onFailure(@NonNull Call<DTO_voucher> call, Throwable t) {
            }
        });
    }





}