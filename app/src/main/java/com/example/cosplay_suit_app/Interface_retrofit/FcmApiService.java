package com.example.cosplay_suit_app.Interface_retrofit;

import com.example.cosplay_suit_app.DTO.FcmMessage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FcmApiService {

    @Headers({
            "Content-Type: application/json",
            "Authorization: key=AAAAJsVHII4:APA91bFX5usTFnlkKZF9m4ChY4wX7sCan3PEUtKOfsbh4QtHv_EkWnIPG1HdKhiVhuE7rTi5AE38j6P0D8cPB2DcpDrRg-jG-wLFXFZvBsXNymZNy0YDPo_hkvq7P1XgKTra7Zq3DXVG"
    })
    @POST("fcm/send")
    Call<ResponseBody> sendFcmMessage(@Body FcmMessage message);
}
