package com.example.cosplay_suit_app.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.DTO.ChatDTO;
import com.example.cosplay_suit_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChat extends RecyclerView.Adapter{
    Context context;
    ArrayList<ChatDTO> messagesAdpterArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;
    private String senderRoom, receiverRoom;

    public AdapterChat(Context context, ArrayList<ChatDTO> messagesAdpterArrayList, String senderRoom, String receiverRoom) {
        this.context = context;
        this.messagesAdpterArrayList = messagesAdpterArrayList;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.item_sendmess, parent, false);
            return new senderVierwHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receivemess, parent, false);
            return new reciverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatDTO messages = messagesAdpterArrayList.get(position);
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", context.MODE_PRIVATE);

        String idU = sharedPreferences.getString("id", "");
        if (idU.equals(messages.getSenderid())) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(context).setTitle("Delete")
                            .setMessage("Are you sure you want to delete this message?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseReference mDatabaseSender = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages");
                                    mDatabaseSender.child(messages.getId()).removeValue();

                                    DatabaseReference mDatabaseReceiver = FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoom).child("messages");
                                    mDatabaseReceiver.child(messages.getId()).removeValue();
                                    Toast.makeText(context, "Delete Sucessfuly!", Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();

                    return true;
                }
            });

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_time;
                if (holder.getClass() == senderVierwHolder.class) {
                    tv_time = ((senderVierwHolder) holder).tv_time;
                } else {
                    tv_time = ((reciverViewHolder) holder).tv_time;
                }

                if (tv_time.getVisibility() == View.VISIBLE) {
                    tv_time.setVisibility(View.GONE);
                } else {
                    tv_time.setVisibility(View.VISIBLE);
                }
            }
        });

        if (holder.getClass() == senderVierwHolder.class) {
            senderVierwHolder viewHolder = (senderVierwHolder) holder;

            viewHolder.msgtxt.setText(messages.getMessage());
            viewHolder.tv_time.setText(messages.getTime());
        } else {
            reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            viewHolder.tv_time.setText(messages.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return messagesAdpterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatDTO messages = messagesAdpterArrayList.get(position);
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", context.MODE_PRIVATE);

        String idU = sharedPreferences.getString("id", "");
        if (idU.equals(messages.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
    }

    class  senderVierwHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt,tv_time;
        public senderVierwHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.avatar_sender);
            msgtxt = itemView.findViewById(R.id.tv_message_sender);
            tv_time = itemView.findViewById(R.id.timestamp_sender);
        }
    }
    class reciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt,tv_time;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.avatar_receiver);
            msgtxt = itemView.findViewById(R.id.tv_message_receiver);
            tv_time = itemView.findViewById(R.id.timestamp_receiver);
        }
    }

}
