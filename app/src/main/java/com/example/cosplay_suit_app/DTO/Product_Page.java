package com.example.cosplay_suit_app.DTO;

import java.util.List;

public class Product_Page {
    List<DTO_SanPham> dtoSanPham;

    int page_length;

    public List<DTO_SanPham> getDtoSanPham() {
        return dtoSanPham;
    }

    public void setDtoSanPham(List<DTO_SanPham> dtoSanPham) {
        this.dtoSanPham = dtoSanPham;
    }

    public int getPage_length() {
        return page_length;
    }

    public void setPage_length(int page_length) {
        this.page_length = page_length;
    }
}
