package com.a51703210_demo.activities_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.a51703210_demo.adapters.UsersAdapter;
import com.a51703210_demo.models.User;
import com.example.a51703210_demo.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListUserFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-c191f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef;
    ArrayList<String> keys = new ArrayList<>();
    private RecyclerView recyclerView;
    UsersAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> listUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_user, container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.rvUser);
        getData();
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new UsersAdapter(view.getContext(), listUser);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void getData() {
        listUser = new ArrayList<>();
        myRef = database.getReference("User");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    listUser.add(user);
                    String key = snapshot.getKey();
                    keys.add(key);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User p = snapshot.getValue(User.class);
                if (p == null || listUser == null || listUser.isEmpty())
                    return;

                String key = snapshot.getKey();
                int index = keys.indexOf(key);
                listUser.set(index, p);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null || listUser == null || listUser.isEmpty())
                    return;
                String key = snapshot.getKey();
                int index = keys.indexOf(key);
                if (index != -1) {
                    listUser.remove(index);
                    keys.remove(index);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
