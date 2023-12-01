package com.example.cosplay_suit_app.ThanhtoanVNpay;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_buynow;
import com.example.cosplay_suit_app.DTO.DTO_buynow;
import com.example.cosplay_suit_app.Fragments.Fragment_trangchu;
import com.example.cosplay_suit_app.MainActivity;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Dialogthongbao;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WebViewThanhtoan extends AppCompatActivity {
    private WebView webView;
    ImageView ic_back;
    static String url = API.URL;
    static final String BASE_URL = url +"/payment/vnpay_return";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_vnpay_main);
        webView = findViewById(R.id.webView);
        ic_back = findViewById(R.id.id_back);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebViewInfo", "Page finished loading: " + url);
            }
        });

        String url = getIntent().getStringExtra("paymentUrl");
        String loc_activity = getIntent().getStringExtra("locactivity");
        webView.loadUrl(url);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                // Lấy URL hiện tại của WebView
                String currentUrl = view.getUrl();
                Log.d("Checkurl trả về từ VNpay", "onPageFinished: " + currentUrl);
                // Kiểm tra xem URL có phải là URL cần xử lý không
                if (currentUrl.startsWith(BASE_URL)) {
                    // Phân tích URL để lấy các tham số
                    Uri uri = Uri.parse(currentUrl);
                    Log.d("uri", "onPageFinished: " + uri);
                    // Lấy giá trị của tham số vnp_Amount
                    String vnp_Amount = uri.getQueryParameter("vnp_Amount");
                    String vnp_BankCode = uri.getQueryParameter("vnp_BankCode");
                    String vnp_BankTranNo = uri.getQueryParameter("vnp_BankTranNo");
                    String vnp_CardType = uri.getQueryParameter("vnp_CardType");
                    String vnp_OrderInfo = uri.getQueryParameter("vnp_OrderInfo");
                    String vnp_PayDate = uri.getQueryParameter("vnp_PayDate");
                    String vnp_ResponseCode = uri.getQueryParameter("vnp_ResponseCode");
                    String vnp_TmnCode = uri.getQueryParameter("vnp_TmnCode");
                    String vnp_TransactionNo = uri.getQueryParameter("vnp_TransactionNo");
                    String vnp_TransactionStatus = uri.getQueryParameter("vnp_TransactionStatus");
                    String vnp_TxnRef = uri.getQueryParameter("vnp_TxnRef");
                    String vnp_SecureHash = uri.getQueryParameter("vnp_SecureHash");

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("vnp_Amount", vnp_Amount);
                    resultIntent.putExtra("vnp_BankCode", vnp_BankCode);
                    resultIntent.putExtra("vnp_BankTranNo", vnp_BankTranNo);
                    resultIntent.putExtra("vnp_CardType", vnp_CardType);
                    resultIntent.putExtra("vnp_OrderInfo", vnp_OrderInfo);
                    resultIntent.putExtra("vnp_PayDate", vnp_PayDate);
                    resultIntent.putExtra("vnp_ResponseCode", vnp_ResponseCode);
                    resultIntent.putExtra("vnp_TmnCode", vnp_TmnCode);
                    resultIntent.putExtra("vnp_TransactionNo", vnp_TransactionNo);
                    resultIntent.putExtra("vnp_TransactionStatus", vnp_TransactionStatus);
                    resultIntent.putExtra("vnp_TxnRef", vnp_TxnRef);
                    resultIntent.putExtra("vnp_SecureHash", vnp_SecureHash);

                    setResult(RESULT_OK, resultIntent);
                    finish();// Kết thúc WebViewThanhtoan
                }
            }
        });
    }
}
