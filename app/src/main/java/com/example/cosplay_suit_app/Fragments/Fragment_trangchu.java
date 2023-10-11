package com.example.cosplay_suit_app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.Adapter.Adapter_SanPham;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.SanPhamInterface;
import com.example.cosplay_suit_app.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment_trangchu extends Fragment {
    static final String BASE_URL = "http://192.168.68.106:3000/product/";

    RecyclerView rcv1,rcv_2,rcv_3;
    List<DTO_SanPham> mlist;
    Adapter_SanPham arrayAdapter;
    View viewok;
    SearchView sv_pro;
    public Fragment_trangchu() {

    }

    public static Fragment_trangchu newInstance(){
        Fragment_trangchu fragmentTrangchu = new Fragment_trangchu();
        return fragmentTrangchu;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_trangchu, container, false);
        rcv_3 = viewok.findViewById(R.id.rcv3);

        mlist = new ArrayList<DTO_SanPham>();
        arrayAdapter = new Adapter_SanPham(mlist,getContext());
        rcv_3.setAdapter(arrayAdapter);
        GetListSanPham();
        return viewok;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    void GetListSanPham() {
        // tạo gson
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface
        SanPhamInterface sanPhamInterface = retrofit.create(SanPhamInterface.class);

        // tạo đối tượng
        Call<List<DTO_SanPham>> objCall = sanPhamInterface.lay_danh_sach();
        objCall.enqueue(new Callback<List<DTO_SanPham>>() {
            @Override
            public void onResponse(Call<List<DTO_SanPham>> call, Response<List<DTO_SanPham>> response) {
                if (response.isSuccessful()) {

                    mlist.clear();
                    mlist.addAll(response.body());
                    arrayAdapter.notifyDataSetChanged();
                    Log.d("list", "onResponse: "+mlist.size());

                } else {
                    Toast.makeText(getContext(),
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }

//                GetListSanPham();
            }

            @Override
            public void onFailure(Call<List<DTO_SanPham>> call, Throwable t) {

            }
        });

    }
}
