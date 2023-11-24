package com.example.cosplay_suit_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosplay_suit_app.Activity.ChatActivity;
import com.example.cosplay_suit_app.DTO.User;
import com.example.cosplay_suit_app.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListChat extends RecyclerView.Adapter<AdapterListChat.viewholder>{

    Context context;
    ArrayList<User> usersArrayList;

    public AdapterListChat(Context context, ArrayList<User> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;

    }
    public void updateList(ArrayList<User> userList) {
        this.usersArrayList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listchat,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        User user = usersArrayList.get(position);

        holder.tv_time.setText(user.getTime());
        holder.username.setText(user.getFullname());

        if (user.getLastMess().isEmpty()){
            holder.tv_lastmess.setText("Đã gửi một ảnh");
        }else {
            holder.tv_lastmess.setText(user.getLastMess());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("idShop",user.getId());
                Log.d("chuongdk", "onClick: "+user.getId());
                intent.putExtra("nameU",user.getFullname());

                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }
    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        TextView username, tv_lastmess,tv_time;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.avatar_chat);
            username = itemView.findViewById(R.id.tv_nameChat);
            tv_lastmess = itemView.findViewById(R.id.tv_lastmess);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }

}
