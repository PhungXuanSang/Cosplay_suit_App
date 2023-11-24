package com.example.cosplay_suit_app.Package_bill.donhang;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.DhWithCmtsAdapter;
import com.example.cosplay_suit_app.Adapter.DhWithoutCmtsAdapter;
import com.example.cosplay_suit_app.DTO.GetCmtsDTO;
import com.example.cosplay_suit_app.DTO.ItemDoneDTO;
import com.example.cosplay_suit_app.Interface_retrofit.CmtsInterface;
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

public class Fragment_DaDanhGia extends Fragment {

    static String url = API.URL;
    static final String BASE_URL = url +"/comments/";
    DhWithCmtsAdapter adapter;
    RecyclerView rcv_done;
    ArrayList<GetCmtsDTO> list;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_da_danh_gia, container, false);

        rcv_done = view.findViewById(R.id.rcv_DDG);
        list = new ArrayList<>();
        adapter = new DhWithCmtsAdapter(getContext(),list);
        rcv_done.setAdapter(adapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        rcv_done.addItemDecoration(dividerItemDecoration);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");

        getListDh(id);

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getListDh(String id) {
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
        CmtsInterface cmtsInterface = retrofit.create(CmtsInterface.class);

        // tạo đối tượng
        Call<List<GetCmtsDTO>> objCall = cmtsInterface.getListCmtsFU(id);
        objCall.enqueue(new Callback<List<GetCmtsDTO>>() {
            @Override
            public void onResponse(Call<List<GetCmtsDTO>> call, Response<List<GetCmtsDTO>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d("CDG", "onResponse: "+list.size());

                } else {
                    Toast.makeText(getContext(),
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetCmtsDTO>> call, Throwable t) {
                Log.d("CDG", "onFailure: " + t);
            }
        });


    }
}