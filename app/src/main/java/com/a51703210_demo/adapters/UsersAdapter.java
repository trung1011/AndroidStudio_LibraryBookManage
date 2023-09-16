package com.a51703210_demo.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a51703210_demo.activities_fragments.ViewDetailUser;
import com.a51703210_demo.models.User;
import com.example.a51703210_demo.R;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<User> lstUser;
    private Context mContext;
    public UsersAdapter(Context context, List<User> lstUser) {
        this.mContext = context;
        this.lstUser = lstUser;
    }
    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        User p = lstUser.get(position);
        if(p==null)
            return;
        if (p.getPic()==1){
            holder.ivPic.setImageResource(R.drawable.male);
        }
        else {holder.ivPic.setImageResource(R.drawable.female);}

        holder.tvName.setText(p.getName());
        holder.tvAddress.setText(p.getAddress());
        holder.tvEmail.setText(p.getEmail());
        if (p.isGender()){
            holder.tvGender.setText("Male");
        }
        else {holder.tvGender.setText("Female");}
        holder.tvPhone.setText(p.getPhone());
        holder.tvProf.setText(p.getProf().getName());
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDetail(p);
            }
        });
    }

    private void onClickDetail(User p) {
        Intent i =new Intent(mContext, ViewDetailUser.class);
        Bundle bundle = new Bundle();
//        bundle.putSerializable("object_person", p);
        bundle.putString("user_name", p.getName());
        bundle.putString("user_email", p.getEmail());
        bundle.putString("user_phone", p.getPhone());
        bundle.putBoolean("user_gender", p.isGender());
        bundle.putString("user_address", p.getAddress());
        bundle.putString("prof_name", p.getProf().getName());
        i.putExtras(bundle);

        mContext.startActivity(i);
    }

    @Override
    public int getItemCount() {
        if(lstUser!=null)
            return lstUser.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_item;
        ImageView ivPic;
        TextView tvName, tvEmail, tvGender, tvAddress, tvPhone, tvProf;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvName =  itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail =  itemView.findViewById(R.id.tvEmail);
            tvGender =  itemView.findViewById(R.id.tvGender);
            tvAddress =  itemView.findViewById(R.id.tvAddress);
            tvProf =  itemView.findViewById(R.id.tvProf);
            layout_item = itemView.findViewById(R.id.layout_item);
        }
    }
    public void release(){
        mContext=null;
    }
}
