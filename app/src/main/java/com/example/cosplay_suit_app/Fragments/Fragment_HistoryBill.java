package com.example.cosplay_suit_app.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
    String id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historybill, container, false);
        recyclerView = view.findViewById(R.id.rcv_donebill);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        id = sharedPreferences.getString("id","");

        list = new ArrayList<>();
        adapterFragHistorybill = new Adapter_FragHistorybill(list, getContext());
        recyclerView.setAdapter(adapterFragHistorybill);
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
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
