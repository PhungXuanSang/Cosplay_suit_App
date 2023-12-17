package com.example.cosplay_suit_app.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cosplay_suit_app.Adapter.Adapter_FragDoneBill;
import com.example.cosplay_suit_app.Adapter.Adapter_FragHistorybill;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.History_DTO;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;
import com.example.cosplay_suit_app.bill.controller.Wallet_controller;

import java.util.ArrayList;
import java.util.List;

public class Fragment_HistoryBill extends Fragment {
    List<History_DTO> list;
    Adapter_FragHistorybill adapterFragHistorybill;
    RecyclerView recyclerView;
    LinearLayout noProductMessage;
    String id;
    ProgressBar loadingProgressBar;
    SwipeRefreshLayout setOnRefreshListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historybill, container, false);
        recyclerView = view.findViewById(R.id.rcv_donebill);
        noProductMessage = view.findViewById(R.id.noProductMessage);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        setOnRefreshListener = view.findViewById(R.id.restartbill);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        id = sharedPreferences.getString("id","");

        list = new ArrayList<>();
        adapterFragHistorybill = new Adapter_FragHistorybill(list, getContext());
        recyclerView.setAdapter(adapterFragHistorybill);
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
    public void gethistory(){
        if (id != null && !id.isEmpty()) {
            Wallet_controller walletController = new Wallet_controller(getContext());
            walletController.getlichsuthuchien(id, new Wallet_controller.Apigetlichsuthuchien() {
                @Override
                public void onApigetlichsuthuchien(List<History_DTO> getVoucherDto) {
                    list.clear();
                    if (getVoucherDto != null && !getVoucherDto.isEmpty()) {
                        for (History_DTO billDetail : getVoucherDto) {
                            list.add(billDetail);
                        }
                        adapterFragHistorybill.notifyDataSetChanged();
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
        gethistory();
    }
    private void fetchData() {
        gethistory();
        // Kết thúc quá trình làm mới
        setOnRefreshListener.setRefreshing(false);
    }
}
