package com.example.cosplay_suit_app.bill.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Adapter.Adapter_buynow;
import com.example.cosplay_suit_app.DTO.BillDetailDTO;
import com.example.cosplay_suit_app.DTO.DTO_Bill;
import com.example.cosplay_suit_app.DTO.DTO_billdetail;
import com.example.cosplay_suit_app.DTO.DTO_idbill;
import com.example.cosplay_suit_app.Interface_retrofit.Bill_interface;
import com.example.cosplay_suit_app.Interface_retrofit.Billdentail_Interfece;
import com.example.cosplay_suit_app.Interface_retrofit.Thanhtoan_interface;
import com.example.cosplay_suit_app.Package_bill.Activity.xannhandon_Activity;
import com.example.cosplay_suit_app.Package_bill.Adapter.Adapter_Bill;
import com.example.cosplay_suit_app.ThanhtoanVNpay.DTO_thanhtoan;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;

public class Bill_controller {
    private static final String TAG = "addbill";
    static String url = API.URL;
    static final String BASE_URL_CARTORDER = url + "/bill/";
    static final String BASE_URL_Thanhtoan = url + "/thanhtoan/";
    Context mContext;
    DTO_idbill dtoIdbill;
    private Adapter_buynow.OnAddBillCompleteListener onAddBillCompleteListener;
    public void setOnAddBillCompleteListener(Adapter_buynow.OnAddBillCompleteListener listener) {
        this.onAddBillCompleteListener = listener;
    }
    // Constructor để khởi tạo context và base URL
    public Bill_controller(Context context) {
        this.mContext = context;
    }
    public void Addbill(DTO_Bill dtoBill) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Bill_interface billInterface = retrofit.create(Bill_interface.class);
        Call<DTO_Bill> objCall = billInterface.addbill(dtoBill);

        objCall.enqueue(new Callback<DTO_Bill>() {
            @Override
            public void onResponse(Call<DTO_Bill> call, Response<DTO_Bill> response) {
                if (response.isSuccessful()) {
                    // Sử dụng mContext để hiển thị Toast
                    DTO_Bill result = response.body();
                    dtoIdbill = new DTO_idbill();
                    dtoIdbill.set_id(result.get_id());
                    dtoIdbill.setId_shop(result.getId_shop());
                    if (onAddBillCompleteListener != null) {
                        onAddBillCompleteListener.onAddBillComplete();
                    }
                } else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DTO_Bill> call, Throwable t) {
                // Sử dụng mContext để hiển thị thông báo lỗi
                Toast.makeText(mContext, "Lỗi: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }
    public void databilldetail(ArrayList<String> listidproduct, ArrayList<String> listidcartForItem, ArrayList<Integer> listamout, ArrayList<String> listsize, ArrayList<Integer> listtotalpayment, String idshop){
        int size = Math.min(listidproduct.size(),Math.min(listidcartForItem.size(), Math.min(listamout.size(), Math.min(listsize.size(), listtotalpayment.size()))));
        Cart_controller cartController = new Cart_controller(mContext);
        for (int i = 0; i < size; i++) {
            String idProduct = listidproduct.get(i);
            int amount = listamout.get(i);
            String sizeValue = listsize.get(i);
            int totalPayment = listtotalpayment.get(i);
            String idcart = listidcartForItem.get(i);
            cartController.DeleteCartorder(idcart);
            if (idshop.equals(dtoIdbill.getId_shop())){
                DTO_billdetail dtoBilldetail = new DTO_billdetail();
                dtoBilldetail.setAmount(amount);
                dtoBilldetail.setSize(sizeValue);
                dtoBilldetail.setTotalPayment(totalPayment);
                dtoBilldetail.setId_bill(dtoIdbill.get_id());
                dtoBilldetail.setId_product(idProduct);
                dtoBilldetail.setSize(sizeValue);

                Addbilldetail(dtoBilldetail);
            }
        }
    }
    public void Addbilldetail(DTO_billdetail dtoBill) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Billdentail_Interfece billInterface = retrofit.create(Billdentail_Interfece.class);
        Call<DTO_billdetail> objCall = billInterface.addbilldetail(dtoBill);

        objCall.enqueue(new Callback<DTO_billdetail>() {
            @Override
            public void onResponse(Call<DTO_billdetail> call, Response<DTO_billdetail> response) {
                if (response.isSuccessful()) {
                    // Sử dụng mContext để hiển thị Toast

                } else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DTO_billdetail> call, Throwable t) {
                // Sử dụng mContext để hiển thị thông báo lỗi
                Toast.makeText(mContext, "Lỗi: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }
    public void AddThanhtoan(DTO_thanhtoan dtoThanhtoan){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_Thanhtoan)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Thanhtoan_interface thanhtoanInterface = retrofit.create(Thanhtoan_interface.class);
        Call<DTO_thanhtoan> objCall = thanhtoanInterface.Addthanhtoan(dtoThanhtoan);

        objCall.enqueue(new Callback<DTO_thanhtoan>() {
            @Override
            public void onResponse(Call<DTO_thanhtoan> call, Response<DTO_thanhtoan> response) {
                if (response.isSuccessful()) {
                    // Sử dụng mContext để hiển thị Toast
                } else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DTO_thanhtoan> call, Throwable t) {
                // Sử dụng mContext để hiển thị thông báo lỗi
                Toast.makeText(mContext, "Lỗi: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }
    public void GetUserBillWait(String id, List<BillDetailDTO> list, Adapter_Bill arrayAdapter, String status,String type) {
        // tạo gson
        Gson gson = new GsonBuilder().setLenient().create();

        // Create a new object from HttpLoggingInterceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        Billdentail_Interfece billDetailDTO = retrofit.create(Billdentail_Interfece.class);

        // tạo đối tượng
        Call<List<BillDetailDTO>> objCall = billDetailDTO.getstatuswait(type,id);
        objCall.enqueue(new Callback<List<BillDetailDTO>>() {
            @Override
            public void onResponse(Call<List<BillDetailDTO>> call, Response<List<BillDetailDTO>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    List<BillDetailDTO> billDetailList = response.body();
                    if (billDetailList != null && !billDetailList.isEmpty()) {
                        for (BillDetailDTO billDetail : billDetailList) {
                            if (billDetail.getDtoBill().getStatus().equals(status)){
                                list.add(billDetail);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(mContext, "Danh sách đối tượng trống.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BillDetailDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });

    }
    public void GetUserBillPack(String id, List<BillDetailDTO> list, Adapter_Bill arrayAdapter, String status,String type) {
        // tạo gson
        Gson gson = new GsonBuilder().setLenient().create();

        // Create a new object from HttpLoggingInterceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        Billdentail_Interfece billDetailDTO = retrofit.create(Billdentail_Interfece.class);

        // tạo đối tượng
        Call<List<BillDetailDTO>> objCall = billDetailDTO.getstatusPack(type,id);
        objCall.enqueue(new Callback<List<BillDetailDTO>>() {
            @Override
            public void onResponse(Call<List<BillDetailDTO>> call, Response<List<BillDetailDTO>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    List<BillDetailDTO> billDetailList = response.body();
                    if (billDetailList != null && !billDetailList.isEmpty()) {
                        for (BillDetailDTO billDetail : billDetailList) {
                            if (billDetail.getDtoBill().getStatus().equals(status)){
                                list.add(billDetail);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(mContext, "Danh sách đối tượng trống.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BillDetailDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });

    }
    public void GetUserBillDelivery(String id, List<BillDetailDTO> list, Adapter_Bill arrayAdapter, String status,String type) {
        // tạo gson
        Gson gson = new GsonBuilder().setLenient().create();

        // Create a new object from HttpLoggingInterceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        Billdentail_Interfece billDetailDTO = retrofit.create(Billdentail_Interfece.class);

        // tạo đối tượng
        Call<List<BillDetailDTO>> objCall = billDetailDTO.getstatusDelivery(type,id);
        objCall.enqueue(new Callback<List<BillDetailDTO>>() {
            @Override
            public void onResponse(Call<List<BillDetailDTO>> call, Response<List<BillDetailDTO>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    List<BillDetailDTO> billDetailList = response.body();
                    if (billDetailList != null && !billDetailList.isEmpty()) {
                        for (BillDetailDTO billDetail : billDetailList) {
                            if (billDetail.getDtoBill().getStatus().equals(status)){
                                list.add(billDetail);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(mContext, "Danh sách đối tượng trống.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BillDetailDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });

    }
    public void GetUserBillDone(String id, List<BillDetailDTO> list, Adapter_Bill arrayAdapter, String status,String type) {
        // tạo gson
        Gson gson = new GsonBuilder().setLenient().create();

        // Create a new object from HttpLoggingInterceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client) // Set HttpClient to be used by Retrofit
                .build();

        // sử dụng interface
        Billdentail_Interfece billDetailDTO = retrofit.create(Billdentail_Interfece.class);

        // tạo đối tượng
        Call<List<BillDetailDTO>> objCall = billDetailDTO.getstatusDone(type,id);
        objCall.enqueue(new Callback<List<BillDetailDTO>>() {
            @Override
            public void onResponse(Call<List<BillDetailDTO>> call, Response<List<BillDetailDTO>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    List<BillDetailDTO> billDetailList = response.body();
                    if (billDetailList != null && !billDetailList.isEmpty()) {
                        for (BillDetailDTO billDetail : billDetailList) {
                            if (billDetail.getDtoBill().getStatus().equals(status)){
                                list.add(billDetail);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(mContext, "Danh sách đối tượng trống.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext,
                            "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BillDetailDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });

    }
    public void UpdateStatusBill(String idbill,DTO_Bill dtoBill){
        //Tạo đối tượng chuyển đổi kiểu dữ liệu
        Gson gson = new GsonBuilder().setLenient().create();
        //Tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CARTORDER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Khởi  tạo interface
        Bill_interface userInterface = retrofit.create(Bill_interface.class);
        // Tạo Call
        Call<DTO_Bill> objCall = userInterface.upstatusbill(idbill, dtoBill);
        Log.d(TAG, "UpdateStatusBill: "+dtoBill.getStatus());
        // Thực hiện gửi dữ liệu lên server
        objCall.enqueue(new Callback<DTO_Bill>() {
            @Override
            public void onResponse(Call<DTO_Bill> call, Response<DTO_Bill> response) {
                // kết quả server trả về ở đây
                if (response.isSuccessful()) {
                    String title = "Thông báo đơn hàng";
                    String msg = "Xác nhận đơn thành thông";
                    Dialogthongbao.showSuccessDialog(mContext, title, msg);
                } else {
                    Log.e(TAG, response.message());
                }
            }
            @Override
            public void onFailure(Call<DTO_Bill> call, Throwable t) {
                // nếu xảy ra lỗi sẽ thông báo ở đây

                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

}
