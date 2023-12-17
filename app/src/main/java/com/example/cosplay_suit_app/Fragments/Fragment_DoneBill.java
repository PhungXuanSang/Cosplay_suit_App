package com.example.cosplay_suit_app.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cosplay_suit_app.Activity.VouchercuatoiActivity;
import com.example.cosplay_suit_app.Adapter.Adapter_FragDoneBill;
import com.example.cosplay_suit_app.Adapter.Adapter_vouchercuatoi;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.GetVoucher_DTO;
import com.example.cosplay_suit_app.Package_bill.DTO.BillDTO;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;
import com.example.cosplay_suit_app.bill.controller.Voucher_controller;

import java.util.ArrayList;
import java.util.List;

public class Fragment_DoneBill extends Fragment {
    List<BillDetailDTO> list;
    Adapter_FragDoneBill adapterFragDoneBill;
    RecyclerView recyclerView;
    LinearLayout noProductMessage;
    String id;
    ProgressBar loadingProgressBar;
    SwipeRefreshLayout setOnRefreshListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donebill, container, false);
        recyclerView = view.findViewById(R.id.rcv_donebill);
        noProductMessage = view.findViewById(R.id.noProductMessage);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        setOnRefreshListener = view.findViewById(R.id.restartbill);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        id = sharedPreferences.getString("id","");

        list = new ArrayList<>();
        adapterFragDoneBill = new Adapter_FragDoneBill(list, getContext());
        recyclerView.setAdapter(adapterFragDoneBill);
        setOnRefreshListener.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        reloadBillList();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void getuserbilldone(){
        if (id != null && !id.isEmpty()) {
            Bill_controller billController = new Bill_controller(getContext());
            billController.GetUserBillDone(id, "shopstatus", new Bill_controller.ApiGetUserBillDone() {
                @Override
                public void onApiGetUserBillDone(List<BillDetailDTO> profileDTO) {
                    list.clear();
                    if (profileDTO != null && !profileDTO.isEmpty()) {
                        for (BillDetailDTO billDetail : profileDTO) {
                            list.add(billDetail);
                        }
                        adapterFragDoneBill.notifyDataSetChanged();
                    }
                    if (list.isEmpty()) {
                        noProductMessage.setVisibility(LinearLayout.VISIBLE);
                        recyclerView.setVisibility(ListView.GONE);
                    } else {
                        noProductMessage.setVisibility(LinearLayout.GONE);
                        recyclerView.setVisibility(ListView.VISIBLE);
                    }
                    loadingProgressBar.setVisibility(View.GONE); // Ẩn ProgressBar sau khi tải xong
                }
            });
        }
    }
    public void reloadBillList() {
        loadingProgressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar trước khi tải dữ liệu
        getuserbilldone();
    }
    private void fetchData() {
        getuserbilldone();
        // Kết thúc quá trình làm mới
        setOnRefreshListener.setRefreshing(false);
    }
}
