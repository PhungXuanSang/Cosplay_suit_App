package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.LoginUser;
import com.example.cosplay_suit_app.DTO.Profile1_DTO;
import com.example.cosplay_suit_app.DTO.ProfileDTO;
import com.example.cosplay_suit_app.DTO.Shop;
import com.example.cosplay_suit_app.DTO.SignUpUser;
import com.example.cosplay_suit_app.DTO.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserInterface {

//    @Headers({
//            "Content-Type: application/json",
//            "Accept: application/json",
//    })

    @POST("login")
    Call<LoginUser> login(@Body User user);

    @POST("reg")
    Call<SignUpUser> sign_up(@Body User objT);
    @PUT("doimk/{id}")
    Call<User> new_pass(@Path("id") String id, @Body User objU);
    @PUT("fPasswd/{phone}")
    Call<User> forPass(@Path("phone") String phone, @Body User objU);
    @PUT("regapp/{id_user}")
    Call<User> udate_role(@Path("id_user") String id_user,@Body User objT);

    @POST("shop/add")
    Call<Shop> new_shop(@Body Shop shop);
    @GET("fUser/{id_user}")
    Call<User> findUser(@Path("id_user") String id_user);

    // Định nghĩa các phương thức yêu cầu API tại đây

    @GET("profile/{id_user}")
    Call<ProfileDTO> getUsere(@Path("id_user") String id_user);

    @PUT("uprofile/{id}")
    Call<Profile1_DTO> updateUserField(@Path("id") String id_user, @Body Profile1_DTO updatedProfile);

    @POST("postprofile")
    Call<ProfileDTO> postUserField(@Body ProfileDTO dto);


//    @GET("login/{email}")
//    Call<User> loginU(@Path("email") String email);

}
