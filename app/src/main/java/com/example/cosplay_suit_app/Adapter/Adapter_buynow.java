package com.example.cosplay_suit_app.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.API;
import com.example.cosplay_suit_app.Activity.BuynowActivity;
import com.example.cosplay_suit_app.Activity.MualaiActivity;
import com.example.cosplay_suit_app.DTO.CartOrderDTO;
import com.example.cosplay_suit_app.DTO.DTO_Bill;
import com.example.cosplay_suit_app.DTO.DTO_buynow;
import com.example.cosplay_suit_app.DTO.DTO_inbuynow;
import com.example.cosplay_suit_app.DTO.GetVoucher_DTO;
import com.example.cosplay_suit_app.DTO.TotalPriceManager;
import com.example.cosplay_suit_app.Interface_retrofit.CartOrderInterface;
import com.example.cosplay_suit_app.R;
import com.example.cosplay_suit_app.bill.controller.Bill_controller;
import com.example.cosplay_suit_app.bill.controller.Voucher_controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Adapter_buynow extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    static String url = API.URL;
    static final String BASE_URL = url +"/bill/";
    List<DTO_buynow> list;
    Context context;
    private Map<String, List<DTO_inbuynow>> orderMap; // Map lưu trữ danh sách đơn hàng theo idshop
    private List<DTO_inbuynow> allOrders;
    Adapter_inbuynow arrayAdapter;
    String TAG = "Adapter_buynow";
    private TotalPriceManager totalPriceManager;
    int totalForShop;
    String id, magiamgia = "";
    Dialog dialog;
    List<GetVoucher_DTO> getVoucherDtoList;
    Adapterchonvoucherbuynow adapterchonvoucher;
    UpdatetotalPriceManager updatetotalPriceManager;

    public Adapter_buynow(List<DTO_buynow> list, Context context, UpdatetotalPriceManager updatetotalPriceManager) {
        this.list = list;
        this.context = context;
        this.updatetotalPriceManager = updatetotalPriceManager;
        orderMap = new HashMap<>();
        allOrders = new ArrayList<>();
        totalPriceManager = TotalPriceManager.getInstance();
        ArrayList<String> listStringIDcart = totalPriceManager.getListcart();
        // Lấy danh sách đơn hàng của tất cả người dùng
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", context.MODE_PRIVATE);
        id = sharedPreferences.getString("id","");
        getOrdersByUserId(id, new Callback<List<DTO_inbuynow>>() {
            @Override
            public void onResponse(Call<List<DTO_inbuynow>> call, Response<List<DTO_inbuynow>> response) {
                if (response.isSuccessful()) {
                    allOrders = response.body();
                    for (DTO_inbuynow order : allOrders) {
                        String checkorder = order.get_id();
                        if (listStringIDcart.contains(checkorder)) {
                            handleOrders(order.getDtoSanPham().getId_shop(), order);
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    // Xử lý lỗi (nếu có)
                    Log.e("Adapter_ShopCartOrder", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<DTO_inbuynow>> call, Throwable t) {
                // Xử lý lỗi khi gọi API
                Log.e("Adapter_ShopCartOrder", "Error: " + t.getMessage());
            }
        });
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buynow, parent, false);
        return new ItemViewHoldel(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DTO_buynow shopCartorderDTO = list.get(position);

        Adapter_buynow.ItemViewHoldel viewHolder = (Adapter_buynow.ItemViewHoldel) holder;


        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        List<DTO_inbuynow> ordersForShop = orderMap.get(shopCartorderDTO.get_id());
        arrayAdapter = new Adapter_inbuynow(ordersForShop, context);
        viewHolder.recyclerView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        // Tính tổng và hiển thị
        totalForShop = calculateTotalForShop(ordersForShop);
        viewHolder.tv_tonggia.setText( decimalFormat.format(totalForShop) + " VND");
        shopCartorderDTO.setTongbill(totalForShop);

        // Hiển thị thông tin về voucher
        GetVoucher_DTO selectedVoucher = shopCartorderDTO.getSelectedVoucher();
        if (selectedVoucher != null) {
            viewHolder.chonvoucher.setText("Giảm " + selectedVoucher.getDtoVoucher().getDiscount() + "%");
            double magiamgiaValue = Double.parseDouble(selectedVoucher.getDtoVoucher().getDiscount());
            double result = magiamgiaValue / 100;
            double dagiamgia = shopCartorderDTO.getTongbill() * result;
            totalPriceManager.updateTotalPriceFalse(dagiamgia);
            updatetotalPriceManager.updatetext(totalPriceManager.getTotalOrderPrice(), selectedVoucher.get_id());
        } else {
            viewHolder.chonvoucher.setText("Chọn >>");
        }

        //Chọn phương thức thanh toán
        Bill_controller billController = new Bill_controller(context);
        dialog = new Dialog(context);
        getVoucherDtoList = new ArrayList<>();


        viewHolder.chonvoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo dialog
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogchonvoucher);

                ImageView imageViewback = dialog.findViewById(R.id.id_back);
                imageViewback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                adapterchonvoucher = new Adapterchonvoucherbuynow(getVoucherDtoList, context,
                        dialog, shopCartorderDTO.get_id());
                RecyclerView recyclerView = dialog.findViewById(R.id.rcv_voucher);
                recyclerView.setAdapter(adapterchonvoucher);
                adapterchonvoucher.setOnVoucherSelectedListener(new Adapterchonvoucherbuynow.OnVoucherSelectedListener() {
                    @Override
                    public void onVoucherSelected(GetVoucher_DTO selectedVoucher) {
                        // Cập nhật selectedVoucherCode cho item tương ứng trong DTO_buynow
                        shopCartorderDTO.setSelectedVoucher(selectedVoucher);
                        // Cập nhật giao diện cho item này
                        notifyItemChanged(list.indexOf(shopCartorderDTO));
                    }
                });
                billController.getVoucher(id, new Bill_controller.ApiVouche() {
                    @Override
                    public void onApiVouche(List<GetVoucher_DTO> getVoucherDto) {
                        getVoucherDtoList.clear();
                        if (getVoucherDto != null && !getVoucherDto.isEmpty()) {
                            for (GetVoucher_DTO voucherDto : getVoucherDto) {
                                getVoucherDtoList.add(voucherDto);
                            }
                            adapterchonvoucher.notifyDataSetChanged();
                        }
                    }
                });

                Window window = dialog.getWindow();
                WindowManager.LayoutParams layoutParams = window.getAttributes();

                // Chiều rộng full màn hình
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                // Chiều cao full màn hình
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

                window.setAttributes(layoutParams);
                window.setBackgroundDrawableResource(android.R.color.transparent);

                dialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHoldel extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        TextView tv_tonggia;
        TextView chonvoucher;
        public ItemViewHoldel(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rcv_buynow);
            tv_tonggia = itemView.findViewById(R.id.tv_tonggia);
            chonvoucher = itemView.findViewById(R.id.chonvoucher);
        }
    }
    public void getOrdersByUserId(String userId, Callback<List<DTO_inbuynow>> callback) {
        // Tạo một OkHttpClient với interceptor để ghi log (nếu cần)
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        // Tạo Gson ConverterFactory để chuyển đổi JSON thành Java objects
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        // Tạo một API service sử dụng Retrofit
        CartOrderInterface service = retrofit.create(CartOrderInterface.class);

        // Gọi API để lấy danh sách đơn hàng dựa trên userId
        Call<List<DTO_inbuynow>> call = service.getShopidcart(userId);
        call.enqueue(callback);
    }
    private void handleOrders(String idShop, DTO_inbuynow order) {
        if (orderMap.containsKey(idShop)) {
            orderMap.get(idShop).add(order);
        } else {
            List<DTO_inbuynow> orders = new ArrayList<>();
            orders.add(order);
            orderMap.put(idShop, orders);
        }
    }
    private int calculateTotalForShop(List<DTO_inbuynow> orders) {
        int total = 0;
        for (DTO_inbuynow order : orders) {
            total += order.getAmount() * order.getDtoSanPham().getPrice();
        }
        return total;
    }
    private ArrayList<String> arrproduct(List<DTO_inbuynow> orders) {
        ArrayList<String> arrayidpro= new ArrayList<>();
        for (DTO_inbuynow order : orders) {
            arrayidpro.add(order.getDtoSanPham().getId()) ;
        }
        return arrayidpro;
    }
    private ArrayList<Integer> arramount(List<DTO_inbuynow> orders) {
        ArrayList<Integer> arrayamount = new ArrayList<>();
        for (DTO_inbuynow order : orders) {
            arrayamount.add(order.getAmount()) ;
        }
        return arrayamount;
    }
    private ArrayList<Integer> arraTotalPayment(List<DTO_inbuynow> orders) {
        ArrayList<Integer> arrayTotalPayment = new ArrayList<>();
        for (DTO_inbuynow order : orders) {
            arrayTotalPayment.add(order.getTotalPayment()) ;
        }
        return arrayTotalPayment;
    }
    private ArrayList<String> arrsize(List<DTO_inbuynow> orders) {
        ArrayList<String> arraysize= new ArrayList<>();
        for (DTO_inbuynow order : orders) {
            arraysize.add(order.getId_properties());
        }
        return arraysize;
    }
    private ArrayList<String> arridcart(List<DTO_inbuynow> orders) {
        ArrayList<String> arrayidcart= new ArrayList<>();
        for (DTO_inbuynow order : orders) {
            arrayidcart.add(order.get_id());
        }
        return arrayidcart;
    }
    public interface OnAddBillCompleteListener {
        void onAddBillComplete();
    }
    public void performActionOnAllItems(String idthanhtoan, String addres) {
        // Thực hiện công việc cần thiết trên tất cả các item
        for (DTO_buynow item : list) {
            String currentDateTime = getCurrentDateTime();
            DTO_Bill dtoBill = new DTO_Bill();
            dtoBill.setId_user(id);
            dtoBill.setId_shop(item.get_id());
            dtoBill.setId_address(addres);
            dtoBill.setTimestart(currentDateTime);
            dtoBill.setTimeend("");
            dtoBill.setStatus("Wait");
            // Kiểm tra xem item có chọn voucher hay không
            if (item.getSelectedVoucher() != null) {
                dtoBill.setMa_voucher(item.getSelectedVoucher().getDtoVoucher().getId());
                dtoBill.setDiscount(item.getSelectedVoucher().getDtoVoucher().getDiscount());

                Voucher_controller voucherController = new Voucher_controller(context);
                voucherController.Deleteseenvoucher(item.getSelectedVoucher().getDtoVoucher().getId());
            }
            dtoBill.setId_thanhtoan(idthanhtoan);
            dtoBill.setTotalPayment(item.getTongbill());
            Bill_controller billController = new Bill_controller(context);
            billController.Addbill(dtoBill);

            // Lấy danh sách các sản phẩm, số lượng và thuộc tính cho từng item
            ArrayList<String> listidproductForItem = arrproduct(orderMap.get(item.get_id()));
            ArrayList<Integer> listamoutForItem = arramount(orderMap.get(item.get_id()));
            ArrayList<String> listsizeForItem = arrsize(orderMap.get(item.get_id()));
            ArrayList<Integer> listtotalpaymentForItem = arraTotalPayment(orderMap.get(item.get_id()));
            ArrayList<String> listidcartForItem = arridcart(orderMap.get(item.get_id()));
            billController.setOnAddBillCompleteListener(new OnAddBillCompleteListener() {
                @Override
                public void onAddBillComplete() {
                    // Gọi databilldetail khi Addbill đã hoàn thành
                    billController.databilldetail(listidproductForItem,listidcartForItem ,listamoutForItem,
                            listsizeForItem, listtotalpaymentForItem,  item.get_id());

                }
            });
        }
        notifyDataSetChanged(); // Cập nhật RecyclerView sau khi thay đổi dữ liệu
    }

    //Lấy ngày giờ hiện tại
    private String getCurrentDateTime() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(currentDate);
    }
    public interface UpdatetotalPriceManager{
        void updatetext(double giamoi, String idseenvoucherlist);
    }
}
