package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_SanPham;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.Interface_retrofit.SanPhamInterface;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultActivity extends AppCompatActivity {

    static String url = API.URL;
    static final String BASE_URL = url +"/product/";

    List<DTO_SanPham> mlist;
    Adapter_SanPham arrayAdapter;
    RecyclerView rcv_searchPro;
    String nameSearch;
    ImageView img_back;
    ArrayList<String> listNamePro;

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter autoCompleteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        rcv_searchPro = findViewById(R.id.rcv_ProSearch);
        img_back = findViewById(R.id.img_backSearch);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        listNamePro = getIntent().getStringArrayListExtra("list_name_pro");
        nameSearch = getIntent().getStringExtra("search_query");
        mlist = new ArrayList<>();
        arrayAdapter = new Adapter_SanPham(this);
        arrayAdapter.updateData(mlist);
        rcv_searchPro.setAdapter(arrayAdapter);
        GetListSanPham(nameSearch);

        setupAutoCompleteTextView();


        Drawable rightDrawable = autoCompleteTextView.getCompoundDrawables()[2]; // Lấy drawableRight


        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP &&
                        event.getRawX() >= (autoCompleteTextView.getRight() - autoCompleteTextView.getCompoundDrawables()[2].getBounds().width())) {

                    String searchQuery = autoCompleteTextView.getText().toString().trim();


                    if (!searchQuery.isEmpty()) {

//                        Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
//                        intent.putExtra("search_query", searchQuery);
//                        intent.putStringArrayListExtra("list_name_pro", listNamePro);
//                        startActivity(intent);
                        GetListSanPham(searchQuery);
                    } else {

                        Toast.makeText(SearchResultActivity.this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
                return false;
            }
        });

        autoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);


    }
    private void setupAutoCompleteTextView() {
        autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listNamePro);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {

//                    layoutlistspdx.setVisibility(View.VISIBLE);
                    rcv_searchPro.setVisibility(View.VISIBLE);
                } else {

//                    layoutlistspdx.setVisibility(View.GONE);
                    rcv_searchPro.setVisibility(View.GONE);
                }

                autoCompleteAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing to do here
            }
        });


        autoCompleteTextView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedSuggestion = (String) autoCompleteAdapter.getItem(position);
            autoCompleteTextView.setText(selectedSuggestion);
            // Perform the search and show the results
            performSearch(selectedSuggestion);
        });
    }

    private void performSearch(String query) {

//        Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
//        intent.putExtra("search_query", query);
//        intent.putStringArrayListExtra("list_name_pro", listNamePro);
//        startActivity(intent);
        GetListSanPham(query);
    }
    private  void GetListSanPham(String nameSearch) {
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
        SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);

        // tạo đối tượng
        Call<List<DTO_SanPham>> objCall = sanPhamInterface.getProdcutByName(nameSearch);
        objCall.enqueue(new Callback<List<DTO_SanPham>>() {
            @Override
            public void onResponse(Call<List<DTO_SanPham>> call, Response<List<DTO_SanPham>> response) {
                if (response.isSuccessful()) {

                    mlist.clear();
                    mlist.addAll(response.body());
                    arrayAdapter.notifyDataSetChanged();
                    Log.d("list", "onResponse: "+mlist.size());
                    rcv_searchPro.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(SearchResultActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }

//                GetListSanPham();
            }

            @Override
            public void onFailure(Call<List<DTO_SanPham>> call, Throwable t) {
                Log.e("list", "onFailure: " + t.getLocalizedMessage() );
            }
        });

    }
}