package com.example.cosplay_suit_app.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.XemAllspdamuaActivity;
import com.example.cosplay_suit_app.Adapter.Adapter_mualai;
import com.example.cosplay_suit_app.Adapter.DhWithoutCmtsAdapter;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.ItemDoneDTO;
import com.example.cosplay_suit_app.Interface_retrofit.CmtsInterface;
import com.example.cosplay_suit_app.Package_bill.Activity.Danhgia_Activity;
import com.example.cosplay_suit_app.Package_bill.Activity.Giaohang_Activity;
import com.example.cosplay_suit_app.Package_bill.Activity.Layhang_Activity;
import com.example.cosplay_suit_app.Package_bill.Activity.xannhandon_Activity;
import com.example.cosplay_suit_app.Package_bill.donhang.Collection_adapter_bill;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;
import com.example.cosplay_suit_app.bill.controller.Dialogthongbao;
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

public class Fragment_donhang extends Fragment {
    static String url = API.URL;
    static final String BASE_URL_SLDG = url +"/comments/";
    DhWithoutCmtsAdapter adapter;
    ArrayList<ItemDoneDTO> list;
    static String id="";
    String username_u;
    RelativeLayout rlhoanthanh, rlxacnhandon, rllayhang, rldanggiao, rl_allmualai;
    TextView tv_sldanhgia, tvdonhangmua,tv_slxacnhan,tv_sllayhang,tv_sldanggiao;
    RecyclerView rcv_mualai;
    List<BillDetailDTO> listmualaisp ;
    Adapter_mualai adapterMualai;
    Bill_controller billController = new Bill_controller(getActivity());
    NestedScrollView nestedScrollView;
    List<BillDetailDTO> listxacnhan;
    List<BillDetailDTO> listlayhang;
    List<BillDetailDTO> listdanggiao;
    public Fragment_donhang() {
    }

    public static Fragment_donhang newInstance(){
        Fragment_donhang fragmentDonhang = new Fragment_donhang();
        return fragmentDonhang;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_donhang, container, false);
        rlhoanthanh = viewok.findViewById(R.id.rl_hoanthanh);
        rlxacnhandon = viewok.findViewById(R.id.rl_xacnhandon);
        rllayhang = viewok.findViewById(R.id.rl_layhang);
        rldanggiao = viewok.findViewById(R.id.rl_danggiao);
        tv_sldanhgia = viewok.findViewById(R.id.tv_sldanhgia);
        tv_slxacnhan = viewok.findViewById(R.id.tv_slxacnhan);
        tv_sllayhang = viewok.findViewById(R.id.tv_sllayhang);
        tv_sldanggiao = viewok.findViewById(R.id.tv_sldanggiao);
        tvdonhangmua = viewok.findViewById(R.id.donhangmua);
        rcv_mualai = viewok.findViewById(R.id.rcv_mualai);
        rl_allmualai = viewok.findViewById(R.id.rl_allmualai);
        nestedScrollView = viewok.findViewById(R.id.id_scroll);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        username_u = sharedPreferences.getString("fullname", "");
        id = sharedPreferences.getString("id", "");
        rlxacnhandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.equals("")){
                    String tille = "Thông báo ứng dụng";
                    String msg = "Bạn cần đăng nhập để sử dụng chức năng";
                    Dialogthongbao.showSuccessDialog(getContext(), tille, msg);
                }else {
                    Intent intent = new Intent(getContext(), xannhandon_Activity.class);
                    startActivity(intent);
                }
            }
        });
        rllayhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.equals("")){
                    String tille = "Thông báo ứng dụng";
                    String msg = "Bạn cần đăng nhập để sử dụng chức năng";
                    Dialogthongbao.showSuccessDialog(getContext(), tille, msg);
                }else {
                    Intent intent = new Intent(getContext(), Layhang_Activity.class);
                    startActivity(intent);
                }
            }
        });
        rldanggiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.equals("")){
                    String tille = "Thông báo ứng dụng";
                    String msg = "Bạn cần đăng nhập để sử dụng chức năng";
                    Dialogthongbao.showSuccessDialog(getContext(), tille, msg);
                }else {
                    Intent intent = new Intent(getContext(), Giaohang_Activity.class);
                    startActivity(intent);
                }
            }
        });
        rlhoanthanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.equals("")){
                    String tille = "Thông báo ứng dụng";
                    String msg = "Bạn cần đăng nhập để sử dụng chức năng";
                    Dialogthongbao.showSuccessDialog(getContext(), tille, msg);
                }else {
                    Intent intent = new Intent(getContext(), Danhgia_Activity.class);
                    startActivity(intent);
                }
            }
        });
        tvdonhangmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.equals("")){
                    String tille = "Thông báo ứng dụng";
                    String msg = "Bạn cần đăng nhập để sử dụng chức năng";
                    Dialogthongbao.showSuccessDialog(getContext(), tille, msg);
                }else {
                    Intent intent = new Intent(getContext(), Collection_adapter_bill.class);
                    intent.putExtra("checkactivity","user");
                    startActivity(intent);
                }
            }
        });
        rl_allmualai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.equals("")){
                    String tille = "Thông báo ứng dụng";
                    String msg = "Bạn cần đăng nhập để sử dụng chức năng";
                    Dialogthongbao.showSuccessDialog(getContext(), tille, msg);
                }else {
                    Intent intent = new Intent(getContext(),XemAllspdamuaActivity.class );
                    startActivity(intent);
                }
            }
        });
        list = new ArrayList<>();
        adapter = new DhWithoutCmtsAdapter(getContext(),list);
        listxacnhan = new ArrayList<>();
        listlayhang = new ArrayList<>();
        listdanggiao = new ArrayList<>();

        listmualaisp = new ArrayList<>();
        adapterMualai = new Adapter_mualai(listmualaisp, getActivity());
        rcv_mualai.setAdapter(adapterMualai);
        return viewok;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_sldanhgia.setVisibility(View.GONE);

    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        if (id != null && !id.isEmpty()) {
            billController.Getdsmualaisp(id,listmualaisp,adapterMualai);
            getsoluongdg();
            getsoluongxacnhan();
            getsoluonglayhang();
            getsoluongdanggiao();
        }
    }
    private void getsoluongdg() {
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
                .baseUrl(BASE_URL_SLDG)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();


        CmtsInterface cmtsInterface = retrofit.create(CmtsInterface.class);

        Call<List<ItemDoneDTO>> objCall = cmtsInterface.getListDhWithoutCmts(id);
        objCall.enqueue(new Callback<List<ItemDoneDTO>>() {
            @Override
            public void onResponse(Call<List<ItemDoneDTO>> call, Response<List<ItemDoneDTO>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    int soLuongDanhGia = list.size();
                    if (soLuongDanhGia > 0) {
                        tv_sldanhgia.setVisibility(View.VISIBLE);
                        tv_sldanhgia.setText(String.valueOf(soLuongDanhGia));
                    } else {
                        tv_sldanhgia.setVisibility(View.GONE);
                    }
                    Log.d("CDG", "onResponse: "+list.size());

                } else {
                    Toast.makeText(getContext(),
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<ItemDoneDTO>> call, Throwable t) {
                Log.d("CDG", "onFailure: " + t);
            }
        });
    }
    private void getsoluongxacnhan(){
        billController.GetUserBillWait(id, "user", new Bill_controller.ApiGetUserBillWait() {
            @Override
            public void onApiGetUserBillWait(List<BillDetailDTO> profileDTO) {
                listxacnhan.clear();
                if (profileDTO != null && !profileDTO.isEmpty()) {
                    for (BillDetailDTO billDetail : profileDTO) {
                        listxacnhan.add(billDetail);
                    }
                }
                int soLuong = listxacnhan.size();
                if (soLuong > 0) {
                    tv_slxacnhan.setVisibility(View.VISIBLE);
                    tv_slxacnhan.setText(String.valueOf(soLuong));
                } else {
                    tv_slxacnhan.setVisibility(View.GONE);
                }
            }
        });
    }
    private void getsoluonglayhang(){
        billController.GetUserBillPack(id, "user", new Bill_controller.ApiGetUserBillPack() {
            @Override
            public void onApiGetUserBillPack(List<BillDetailDTO> profileDTO) {
                listlayhang.clear();
                if (profileDTO != null && !profileDTO.isEmpty()) {
                    for (BillDetailDTO billDetail : profileDTO) {
                        listlayhang.add(billDetail);
                    }
                }
                int soLuong = listlayhang.size();
                if (soLuong > 0) {
                    tv_sllayhang.setVisibility(View.VISIBLE);
                    tv_sllayhang.setText(String.valueOf(soLuong));
                } else {
                    tv_sllayhang.setVisibility(View.GONE);
                }
            }
        });
    }
    private void getsoluongdanggiao(){
        billController.GetUserBillDelivery(id, "user", new Bill_controller.ApiGetUserBillDelivery() {
            @Override
            public void onApiGetUserBillDelivery(List<BillDetailDTO> profileDTO) {
                listdanggiao.clear();
                if (profileDTO != null && !profileDTO.isEmpty()) {
                    for (BillDetailDTO billDetail : profileDTO) {
                        listdanggiao.add(billDetail);
                    }
                }
                int soLuong = listdanggiao.size();
                if (soLuong > 0) {
                    tv_sldanggiao.setVisibility(View.VISIBLE);
                    tv_sldanggiao.setText(String.valueOf(soLuong));
                } else {
                    tv_sldanggiao.setVisibility(View.GONE);
                }
            }
        });
    }

}
