package com.example.cosplay_suit_app.bill.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialogthongbao {
    public static void showSuccessDialog(Context context,String title ,String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi người dùng nhấn nút OK
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
