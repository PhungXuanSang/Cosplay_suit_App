package com.example.cosplay_suit_app.DTO;

import java.util.List;

public class Product_Page {
    List<ProByCatDTO> dtoSanPham;

    int page_length;

    public List<ProByCatDTO> getDtoSanPham() {
        return dtoSanPham;
    }

    public void setDtoSanPham(List<ProByCatDTO> dtoSanPham) {
        this.dtoSanPham = dtoSanPham;
    }

    public int getPage_length() {
        return page_length;
    }

    public void setPage_length(int page_length) {
        this.page_length = page_length;
    }
}
