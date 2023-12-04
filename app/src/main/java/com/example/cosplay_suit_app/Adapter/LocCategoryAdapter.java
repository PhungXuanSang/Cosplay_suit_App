package com.example.cosplay_suit_app.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.R;

import java.util.ArrayList;

public class LocCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private Context context;
    private ArrayList<CategoryDTO> list;
    private Dialog dialog;
    private Handler handler;

    private Onclick onclick;
    private boolean isLoading = false;


    public LocCategoryAdapter(Context context, ArrayList<CategoryDTO> list, Dialog dialog, Handler handler, Onclick onclick) {
        this.context = context;
        this.list = list;
        this.dialog = dialog;
        this.handler = handler;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loaading_category, parent, false);
            return new LoadingViewHolder(view);
        }
        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            CategoryDTO categoryDTO = list.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            itemViewHolder.tv_nameCat.setText(categoryDTO.getName());
            Glide.with(context)
                    .load(categoryDTO.getImageCategory())
                    .into(itemViewHolder.img_cat);

            itemViewHolder.cvLocCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_click_animation);
                    itemViewHolder.itemView.startAnimation(animation);

                    onclick.onClickItem(categoryDTO);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 1000);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            // Hiển thị giao diện cho mục "loading"
        }
    }

    @Override
    public int getItemCount() {
        return isLoading ? list.size() + 1 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == list.size() ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void showLoading() {
        if (!isLoading) {
            isLoading = true;
            notifyItemInserted(list.size());
        }
    }

    public void hideLoading() {
        if (isLoading) {
            isLoading = false;
            notifyItemRemoved(list.size());
        }
    }

    public void clearlistProductCategory() {
        int itemCount = list.size();
        list.clear();
        notifyItemRangeRemoved(0, itemCount);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameCat;
        ImageView img_cat;
        CardView cvLocCategory;

        public ItemViewHolder(View view) {
            super(view);
            tv_nameCat = view.findViewById(R.id.tv_nameCat);
            img_cat = view.findViewById(R.id.img_Cat);
            cvLocCategory = view.findViewById(R.id.cvLocCategory);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View view) {
            super(view);
        }
    }

    public interface Onclick {
        void onClickItem(CategoryDTO categoryDTO);
    }
}