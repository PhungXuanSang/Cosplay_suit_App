package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_TrenddingProduct;
import com.example.cosplay_suit_app.Adapter.ProSearchAdapter;
import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.NameProDTO;
import com.example.cosplay_suit_app.Interface_retrofit.CategoryInterface;
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

public class SearchActivity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url +"/product/";
    ImageView img_back;
    RecyclerView rcv_tkdx;
    ArrayList<DTO_SanPham> list;
    ProSearchAdapter adapter;
    AutoCompleteTextView autoCompleteTextView;
    ArrayList<String> listNamePro;
    ArrayAdapter autoCompleteAdapter;
    LinearLayout layoutlistspdx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        img_back = findViewById(R.id.img_backSearch);
        rcv_tkdx = findViewById(R.id.rcv_tkdx);
        layoutlistspdx = findViewById(R.id.layoutlistspdexuat);
        autoCompleteTextView.requestFocus();


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(autoCompleteTextView, InputMethodManager.SHOW_IMPLICIT);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        list = new ArrayList<>();
        adapter = new ProSearchAdapter(this,list);
        rcv_tkdx.setAdapter(adapter);
        getListProductLimit();
        listNamePro = new ArrayList<>();
        getListNamePro();

        setupAutoCompleteTextView();


        Drawable rightDrawable = autoCompleteTextView.getCompoundDrawables()[2]; // Lấy drawableRight


        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP &&
                        event.getRawX() >= (autoCompleteTextView.getRight() - autoCompleteTextView.getCompoundDrawables()[2].getBounds().width())) {

                    String searchQuery = autoCompleteTextView.getText().toString().trim();


                    if (!searchQuery.isEmpty()) {

                        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                        intent.putExtra("search_query", searchQuery);
                        intent.putStringArrayListExtra("list_name_pro", listNamePro);
                        startActivity(intent);
                    } else {

                        Toast.makeText(SearchActivity.this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
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

                    layoutlistspdx.setVisibility(View.VISIBLE);
//            rlt_search_results.setVisibility(View.GONE);
                } else {

                    layoutlistspdx.setVisibility(View.GONE);
//            rlt_search_results.setVisibility(View.VISIBLE);
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

        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putExtra("search_query", query);
        intent.putStringArrayListExtra("list_name_pro", listNamePro);
        startActivity(intent);
    }

    private void getListNamePro() {
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


        Call<NameProDTO> objCall = sanPhamInterface.getListNamePro();
        objCall.enqueue(new Callback<NameProDTO>() {
            @Override
            public void onResponse(Call<NameProDTO> call, Response<NameProDTO> response) {
                if (response.isSuccessful()) {
                    NameProDTO nameProDTO = response.body();
                    if (nameProDTO != null) {
                        listNamePro.clear();
                        listNamePro.addAll(nameProDTO.getNameProductList());
                        Log.d("Search", "onResponse: " + listNamePro.size());
                    }
                } else {
                    Toast.makeText(SearchActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<NameProDTO> call, Throwable t) {
                Log.d("Search", "onFailure: "+t.getLocalizedMessage());
            }
        });
    }

    private void getListProductLimit() {
        try {
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
            SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);
            Call<List<DTO_SanPham>> objCall = sanPhamInterface.GetProductLimit();
            objCall.enqueue(new Callback<List<DTO_SanPham>>() {
                @Override
                public void onResponse(Call<List<DTO_SanPham>> call, Response<List<DTO_SanPham>> response) {
                    if (response.isSuccessful()){
                        list.clear();
                        list.addAll(response.body());
                        adapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onFailure(Call<List<DTO_SanPham>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}