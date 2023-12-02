package com.example.cosplay_suit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.ProByCatAdapter;
import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.ProByCatDTO;
import com.example.cosplay_suit_app.Interface_retrofit.CategoryInterface;
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

public class ProductsByCatActivity extends AppCompatActivity {
    static String url = API.URL;
    static final String BASE_URL = url +"/product/";
    ArrayList<ProByCatDTO> list;

    RecyclerView rcv_proByCat;
    ProByCatAdapter adapter;
    String idCat;
    ImageView img_backProByCat;
    TextView tv_nameCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_by_cat);

        rcv_proByCat = findViewById(R.id.rcv_proByCat);
        img_backProByCat = findViewById(R.id.img_backProByCat);
        tv_nameCat = findViewById(R.id.tv_titleProByCat);

        idCat = getIntent().getStringExtra("idCat");
        tv_nameCat.setText(getIntent().getStringExtra("nameCat"));
        list = new ArrayList<>();
        adapter = new ProByCatAdapter(this,list);
        rcv_proByCat.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.devider1));
        rcv_proByCat.addItemDecoration(dividerItemDecoration);
        getListProByCat(idCat);

        img_backProByCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getListProByCat(String id) {
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


        CategoryInterface categoryInterface = retrofit.create(CategoryInterface.class);


        Call<List<ProByCatDTO>> objCall = categoryInterface.getListProByIdCat(id);
        objCall.enqueue(new Callback<List<ProByCatDTO>>() {
            @Override
            public void onResponse(Call<List<ProByCatDTO>> call, Response<List<ProByCatDTO>> response) {
                if (response.isSuccessful()) {

                    list.clear();
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProductsByCatActivity.this,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<ProByCatDTO>> call, Throwable t) {

            }
        });

    }
}