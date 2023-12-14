package com.example.cosplay_suit_app;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PhanTrang extends RecyclerView.OnScrollListener {

    private LinearLayoutManager linearLayoutManager ;

    public PhanTrang(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = linearLayoutManager.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int first = linearLayoutManager.findFirstVisibleItemPosition();

        if (isLoading() || isLastPage()){
            return;
        }
        if (first>=0 && (visibleItemCount + first)>=totalItemCount){
            loadMoreItem();
        }



    }
    public abstract void loadMoreItem();
    public abstract boolean isLoading();
    public abstract boolean isLastPage();

}
