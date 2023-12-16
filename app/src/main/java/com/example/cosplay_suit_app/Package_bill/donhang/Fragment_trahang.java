package com.example.cosplay_suit_app.Package_bill.donhang;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.Package_bill.Adapter.Adapter_Bill;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;

import java.util.ArrayList;
import java.util.List;

public class Fragment_trahang extends Fragment {
    String TAG = "Danhgiaactivity";
    List<BillDetailDTO> list;
    Adapter_Bill arrayAdapter;
    RecyclerView recyclerView;
    Context context;
    String checkactivity, checkstatus= "Returns";
    LinearLayout noProductMessage;
    ProgressBar loadingProgressBar;
    String id;
    SwipeRefreshLayout setOnRefreshListener;
    public Fragment_trahang(String checkactivity) {
        this.checkactivity = checkactivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewok = inflater.inflate(R.layout.fragment_trahang, container, false);
        recyclerView = viewok.findViewById(R.id.rcv_danhgia);
        noProductMessage = viewok.findViewById(R.id.noProductMessage);
        loadingProgressBar = viewok.findViewById(R.id.loadingProgressBar);
        setOnRefreshListener = viewok.findViewById(R.id.restartbill);
        //danh sách sản phẩm
        list = new ArrayList<>();
        arrayAdapter = new Adapter_Bill(list, getContext(), checkactivity, checkstatus);
        recyclerView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        id = sharedPreferences.getString("id","");
        reloadBillList();
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
    public void getList(){
        if (id != null && !id.isEmpty()) {
            Bill_controller billController = new Bill_controller(getContext());
            billController.GetUserBillReturns(id, checkactivity, new Bill_controller.ApiGetUserBillReturns() {
                @Override
                public void onApiGetUserBillReturns(List<BillDetailDTO> profileDTO) {
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
                        loadingProgressBar.setVisibility(View.GONE);
                    } else {
                        noProductMessage.setVisibility(LinearLayout.GONE);
                        recyclerView.setVisibility(ListView.VISIBLE);
                        loadingProgressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
    public void reloadBillList() {
        loadingProgressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar trước khi tải dữ liệu
        getList();
    }
    private void fetchData() {
        getList();
        // Kết thúc quá trình làm mới
        setOnRefreshListener.setRefreshing(false);
    }
}
