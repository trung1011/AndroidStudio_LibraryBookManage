package com.a51703210_demo.activities_fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.a51703210_demo.models.Book;
import com.a51703210_demo.models.Category;
import com.a51703210_demo.models.User;
import com.example.a51703210_demo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewDetailUser extends AppCompatActivity {

    User user = new User();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-c191f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef=database.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail_user);
        Bundle bundle = getIntent().getExtras();
        TextView tvUserDetailName =  findViewById(R.id.tvUserDetailName);
        TextView tvUserDetailEmail =  findViewById(R.id.tvUserDetailEmail);
        TextView tvUserDetailPhone = findViewById(R.id.tvUserDetailPhone);
        TextView tvUserDetailGender =  findViewById(R.id.tvUserDetailGender);
        TextView tvUserDetailAddress =  findViewById(R.id.tvUserDetailAddress);
        TextView tvUserDetailProf =  findViewById(R.id.tvUserDetailProf);
        ImageView ivDetailUserPic = findViewById(R.id.ivDetailUserPic);
        Button btnBackToUserList =  findViewById(R.id.btnBackToUserList);

        if(bundle==null){

        }
        else{

            String user_name = bundle.getString("user_name");
            String user_email = bundle.getString("user_email");
            String user_phone = bundle.getString("user_phone");
            boolean user_gender = bundle.getBoolean("user_gender");
            String user_address = bundle.getString("user_address");
            String prof_name = bundle.getString("prof_name");


            tvUserDetailName.setText(user_name);
            tvUserDetailEmail.setText(user_email);
            tvUserDetailPhone.setText(user_phone);
            if (user_gender==true) {
                ivDetailUserPic.setImageResource(R.drawable.male);
                tvUserDetailGender.setText("Male");
            }
            else {
                ivDetailUserPic.setImageResource(R.drawable.female);
                tvUserDetailGender.setText("Female");
            }
            tvUserDetailPhone.setText(user_phone);
            tvUserDetailAddress.setText(user_address);
            tvUserDetailProf.setText(prof_name);
        }



        btnBackToUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}