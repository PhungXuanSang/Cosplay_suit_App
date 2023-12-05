package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.CategoryDTO;
import com.example.cosplay_suit_app.DTO.DTO_SanPham;
import com.example.cosplay_suit_app.DTO.DTO_properties;
import com.example.cosplay_suit_app.DTO.Favorite;
import com.example.cosplay_suit_app.DTO.NameProDTO;
import com.example.cosplay_suit_app.DTO.Product_Page;
import com.example.cosplay_suit_app.DTO.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SanPhamInterface {

    @GET("getlistsp")
    Call<List<DTO_SanPham>> lay_danh_sach ();

    @GET("favorite/{tb_user}/{tb_product}")
    Call<Favorite> list_favorite(@Path("tb_user") String tb_user , @Path("tb_product") String tb_product);

    @DELETE("favorite/{tb_user}/{tb_product}")
    Call<Void> delete_favorite(@Path("tb_user") String tb_user ,@Path("tb_product") String tb_product);

    @POST("favorite")
    Call<Favorite> add_favorite(@Body Favorite favorite);

    @GET("getproperties/{idproduct}")
    Call<List<DTO_properties>> getproperties(@Path("idproduct") String idproduct);

    @GET("products/{id}")
    Call<DTO_SanPham> getbyid(@Path("id") String id);

    @GET("getlistsp/{id_shop}")
    Call<List<DTO_SanPham>> GetProduct (@Path("id_shop") String tb_user );
    @GET("getlistsplimit/{id_shop}")
    Call<List<DTO_SanPham>> GetProductLimit (@Path("id_shop") String tb_user );
    @POST("addSP")
    Call<DTO_SanPham> addProduct (@Body DTO_SanPham dtoSanPham );
    @GET("getCategoryApp")
    Call<List<CategoryDTO>> ListCategory();

    @PUT("updateSP/{id}")
    Call<DTO_SanPham> updateProductNamePriceDec(@Path("id") String id, @Body DTO_SanPham dtoSanPham);

    @PUT("updateStatus/{id}")
    Call<DTO_SanPham> updateStatus(@Path("id") String id, @Body DTO_SanPham dtoSanPham);
    @GET("getlistsp/{id_shop}/{page}")
    Call<Product_Page> GetProductPage (@Path("id_shop") String tb_user , @Path("page") Integer page);

    @GET("getlistsptrending/{id_shop}")
    Call<List<DTO_SanPham>> GetProductTop (@Path("id_shop") String tb_user );

    @GET("getlistsptrending")
    Call<List<DTO_SanPham>> GetProductLimit();

    @GET("searchProduct/{id_shop}")
    Call<List<DTO_SanPham>> searchProduct (@Path("id_shop") String tb_user, @Query("nameproduct") String search);

    @GET("getproductCategory/{id_shop}/{id_category}")
    Call<List<DTO_SanPham>> GetProductCtegory (@Path("id_shop") String tb_user ,@Path("id_category") String id_category );

    @PUT("updateProductPop/{nameproperties}/{id}")
    Call<List<DTO_properties>> updateSize(@Path("id") String id,@Path("nameproperties") String name, @Body DTO_properties dtoSanPham);


    @GET("getlistNameSP")
    Call<NameProDTO> getListNamePro();
    @GET("getlistsp?")
    Call<List<DTO_SanPham>> getProdcutByName(@Query("nameproduct")String name);
}
