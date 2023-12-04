package com.example.cosplay_suit_app.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.R;

import java.util.List;

public class SpinnerProductCategoryAdapter extends ArrayAdapter<CategoryDTO> {
    private Context context;
    private List<CategoryDTO> items;

    private Dialog dialog;
    private Handler handler;

    private LocCategoryAdapter.Onclick onclick;
    public SpinnerProductCategoryAdapter(Context context, int resource, List<CategoryDTO> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView);
    }

    private View initView(int position, View convertView) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_category, null);
        }


        TextView textView = convertView.findViewById(R.id.tvCategoryName);

        CategoryDTO categoryDTO = items.get(position);

        if (categoryDTO != null) {
            textView.setText(categoryDTO.getName());
        }

        return convertView;
    }
}

