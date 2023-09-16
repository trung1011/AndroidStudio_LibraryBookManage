package com.a51703210_demo.activities_fragments;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.a51703210_demo.adapters.BooksAdapter;
import com.a51703210_demo.adapters.UsersAdapter;
import com.a51703210_demo.models.Book;
import com.a51703210_demo.models.User;
import com.example.a51703210_demo.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListBookFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-c191f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef;
    ArrayList<String> keys = new ArrayList<>();
    private RecyclerView recyclerView;
    BooksAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Book> listBook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_book, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvBook);
        getData();
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BooksAdapter(view.getContext(), listBook);
        recyclerView.setAdapter(adapter);


        registerForContextMenu(view);
        setHasOptionsMenu(true);

        return view;
    }


    private void getData() {
        listBook = new ArrayList<>();
        myRef = database.getReference("Book");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Book book = snapshot.getValue(Book.class);
                if (book != null) {
                    listBook.add(book);
                    String key = snapshot.getKey();
                    keys.add(key);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Book book = snapshot.getValue(Book.class);
                if (book == null || listBook == null || listBook.isEmpty())
                    return;

                String key = snapshot.getKey();
                int index = keys.indexOf(key);
                listBook.set(index, book);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Book book = snapshot.getValue(Book.class);
                if (book == null || listBook == null || listBook.isEmpty())
                    return;
                String key = snapshot.getKey();
                int index = keys.indexOf(key);
                if (index != -1) {
                    listBook.remove(index);
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
