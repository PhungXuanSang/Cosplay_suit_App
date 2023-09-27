package com.example.cosplay_suit_app.DTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserInterface {

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json",
    })

    @POST("login/")
    Call<User> login(@Body User user);

    @FormUrlEncoded
    @POST("login/{email}/{passwd}")
    Call<User> getUser(@Field("email") String email,
                            @Field("passwd") String passwd
    );


//    @GET("login/{email}")
//    Call<User> loginU(@Path("email") String email);

}
