package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cosplay_suit_app.DTO.ChatDTO;
import com.example.cosplay_suit_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_SEND = 1;
    private static final int ITEM_RECEIVE = 2;

    private Context context;
    private ArrayList<ChatDTO> messagesAdapterArrayList;
    private String senderRoom, receiverRoom;

    public AdapterChat(Context context, ArrayList<ChatDTO> messagesAdapterArrayList, String senderRoom, String receiverRoom) {
        this.context = context;
        this.messagesAdapterArrayList = messagesAdapterArrayList;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sendmess, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receivemess, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatDTO message = messagesAdapterArrayList.get(position);

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String idU = sharedPreferences.getString("id", "");

        if (holder instanceof SenderViewHolder) {
            configureSenderViewHolder((SenderViewHolder) holder, message, idU);
        } else if (holder instanceof ReceiverViewHolder) {
            configureReceiverViewHolder((ReceiverViewHolder) holder, message);
        }

    }

    @Override
    public int getItemCount() {
        return messagesAdapterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatDTO message = messagesAdapterArrayList.get(position);
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String idU = sharedPreferences.getString("id", "");

        return idU.equals(message.getSenderid()) ? ITEM_SEND : ITEM_RECEIVE;
    }

    private void configureSenderViewHolder(SenderViewHolder holder, ChatDTO message, String idU) {
        holder.tvTextMessageSender.setText(message.getMessage());
        holder.tvTimestampSender.setText(message.getTime());
        holder.tvTimestampSender.setVisibility(View.VISIBLE);

        if (message.getImage() != null && !message.getImage().isEmpty()) {

            holder.tvTextMessageSender.setVisibility(View.GONE);
            holder.imageViewSender.setVisibility(View.VISIBLE);
            loadImageIntoImageView(message.getImage(), holder.imageViewSender);
        } else {

            holder.tvTextMessageSender.setVisibility(View.VISIBLE);
            holder.imageViewSender.setVisibility(View.GONE);
        }

        holder.itemView.setOnLongClickListener(v -> {
            showDeleteConfirmationDialog(message.getId());
            return true;
        });
        TextView tv_time = holder.tvTimestampSender;

        holder.tvTimestampSender.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_time.getVisibility() == View.VISIBLE) {
                    tv_time.setVisibility(View.GONE);
                } else {
                    tv_time.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void configureReceiverViewHolder(ReceiverViewHolder holder, ChatDTO message) {
        holder.tvTextMessageReceiver.setText(message.getMessage());
        holder.tvTimestampReceiver.setText(message.getTime());
        holder.tvTimestampReceiver.setVisibility(View.VISIBLE);

        if (message.getImage() != null && !message.getImage().isEmpty()) {

            holder.tvTextMessageReceiver.setVisibility(View.GONE);
            holder.imageViewReceiver.setVisibility(View.VISIBLE);
            loadImageIntoImageView(message.getImage(), holder.imageViewReceiver);
        } else {

            holder.tvTextMessageReceiver.setVisibility(View.VISIBLE);
            holder.imageViewReceiver.setVisibility(View.GONE);
        }
        holder.tvTimestampReceiver.setVisibility(View.GONE);
        TextView tv_time = holder.tvTimestampReceiver;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_time.getVisibility() == View.VISIBLE) {
                    tv_time.setVisibility(View.GONE);
                } else {
                    tv_time.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void loadImageIntoImageView(String imageUrl, ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.baseline_image_24)
                .into(imageView);
    }

    private void showDeleteConfirmationDialog(String messageId) {
        new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Yes", (dialog, which) -> deleteMessage(messageId))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteMessage(String messageId) {
        DatabaseReference mDatabaseSender = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages");
        mDatabaseSender.child(messageId).removeValue();

        DatabaseReference mDatabaseReceiver = FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoom).child("messages");
        mDatabaseReceiver.child(messageId).removeValue();

        Toast.makeText(context, "Delete Successfully!", Toast.LENGTH_SHORT).show();
    }

    static class SenderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView tvTextMessageSender, tvTimestampSender;
        ImageView imageViewSender;

        SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.avatar_sender);
            tvTextMessageSender = itemView.findViewById(R.id.tv_message_sender);
            tvTimestampSender = itemView.findViewById(R.id.timestamp_sender);
            imageViewSender = itemView.findViewById(R.id.imageView_sender);
        }
    }

    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView tvTextMessageReceiver, tvTimestampReceiver;
        ImageView imageViewReceiver;

        ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.avatar_receiver);
            tvTextMessageReceiver = itemView.findViewById(R.id.tv_message_receiver);
            tvTimestampReceiver = itemView.findViewById(R.id.timestamp_receiver);
            imageViewReceiver = itemView.findViewById(R.id.imageView_receiver);
        }
    }
}
