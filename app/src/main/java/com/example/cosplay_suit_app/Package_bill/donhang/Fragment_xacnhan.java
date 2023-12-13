package com.example.cosplay_suit_app.Package_bill.donhang;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cosplay_suit_app.Activity.QlspActivity;
import com.example.cosplay_suit_app.Adapter.QlspAdapter;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.Package_bill.Adapter.Adapter_Bill;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;

import java.util.ArrayList;
import java.util.List;

public class Fragment_xacnhan extends Fragment {
    String TAG = "Danhgiaactivity";
    List<BillDetailDTO> list;
    Adapter_Bill arrayAdapter;
    RecyclerView recyclerView;
    String checkactivity, checkstatus= "Wait";
    LinearLayout noProductMessage;
    String id;
    SwipeRefreshLayout setOnRefreshListener;
    public Fragment_xacnhan(String checkactivity) {
        this.checkactivity = checkactivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_xacnhan, container, false);
        recyclerView = viewok.findViewById(R.id.rcv_danhgia);
        noProductMessage = viewok.findViewById(R.id.noProductMessage);
        setOnRefreshListener = viewok.findViewById(R.id.restartbill);
        //danh sách sản phẩm
        list = new ArrayList<>();
        arrayAdapter = new Adapter_Bill(list, getContext(), checkactivity, checkstatus);
        recyclerView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        id = sharedPreferences.getString("id","");
        getlist();
        setOnRefreshListener.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        return viewok;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void getlist(){
        if (id != null && !id.isEmpty()) {
            Bill_controller billController = new Bill_controller(getContext());
            billController.GetUserBillWait(id, checkactivity, new Bill_controller.ApiGetUserBillWait() {
                @Override
                public void onApiGetUserBillWait(List<BillDetailDTO> profileDTO) {
                    list.clear();
                    if (profileDTO != null && !profileDTO.isEmpty()) {
                        for (BillDetailDTO billDetail : profileDTO) {
                            list.add(billDetail);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                    if (list.isEmpty()) {
                        noProductMessage.setVisibility(LinearLayout.VISIBLE);
                        recyclerView.setVisibility(ListView.GONE);
                    } else {
                        noProductMessage.setVisibility(LinearLayout.GONE);
                        recyclerView.setVisibility(ListView.VISIBLE);
                    }
                }
            });
        }
    }

    private void fetchData() {
        getlist();
        // Kết thúc quá trình làm mới
        setOnRefreshListener.setRefreshing(false);
    }
    @Override
    public void onResume() {
        super.onResume();
        getlist();
        // Cập nhật adapter nếu cần
        if (arrayAdapter != null) {
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
