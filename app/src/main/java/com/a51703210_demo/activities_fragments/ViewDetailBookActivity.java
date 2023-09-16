package com.a51703210_demo.activities_fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.a51703210_demo.adapters.BooksAdapter;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class ViewDetailBookActivity extends AppCompatActivity {

    Book book = new Book();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-c191f-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef=database.getReference("Book");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        Bundle bundle = getIntent().getExtras();
        TextView tvBookDetailName =  findViewById(R.id.tvBookDetailName);
        TextView tvBookDetailAuthor =  findViewById(R.id.tvBookDetailAuthor);
        TextView tvBookDetailCategory = findViewById(R.id.tvBookDetailCategory);
        TextView tvBookDetailLocation =  findViewById(R.id.tvBookDetailLocation);
        TextView tvBookDetailNumber =  findViewById(R.id.tvBookDetailNumber);
        Button btnBackToBookList =  findViewById(R.id.btnBackToBookList);
//        Button btnBookDelete = findViewById(R.id.btnBookDelete);

        if(bundle==null){

        }
        else{

            String book_name = bundle.getString("book_name");
            String book_author = bundle.getString("book_author");
            int book_number = bundle.getInt("book_number");
            String book_category = bundle.getString("book_category");
            String book_location = bundle.getString("book_location");
            int book_category_id = bundle.getInt("book_category_id");

            Category category = new Category();
            category.setId(book_category_id);
            category.setName(book_category);
            book.setName(book_name);
            book.setCategory(category);
            book.setNumber(book_number);
            book.setAuthor(book_author);
            book.setLocation(book_location);
            List<Book> listBooks= new ArrayList<>();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(listBooks!=null || !listBooks.isEmpty())
                        listBooks.clear();

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Book b = dataSnapshot.getValue(Book.class);
                        if (b.getName()==book.getName()){
                            book.setId(b.getId());
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            tvBookDetailName =  findViewById(R.id.tvBookDetailName);
            tvBookDetailName.setText(book_name);
            tvBookDetailAuthor.setText(book_author);
            tvBookDetailCategory.setText(book_category);
            tvBookDetailLocation.setText(book_location);
            tvBookDetailNumber.setText(String.valueOf(book_number));
        }



        btnBackToBookList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        btnBookDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (book.getName()!=null){
//                    Toast.makeText(v.getContext(), "Not Null", Toast.LENGTH_SHORT).show();
//                }
//                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
//                alert.setTitle("Message");
//                alert.setIcon(R.drawable.ic_baseline_library_books);
//                alert.setMessage("Delele: " + book.getName());
//                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    List<Book> listBooks= new ArrayList<>();
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        myRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if(listBooks!=null || !listBooks.isEmpty())
//                                    listBooks.clear();
//
//                                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                                    Book b = dataSnapshot.getValue(Book.class);
//                                    if (b.getName()==book.getName()){
//                                        myRef.child(b.getId());
//                                        myRef.removeValue();
//                                        break;
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                    }
//                });
//                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//                alert.show();
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuDelete:
                Intent i = new Intent(this, ViewDetailBookActivity.class);
                startActivity(i);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void isDelete(final int position){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Message");
        alert.setIcon(R.drawable.ic_baseline_library_books);
        alert.setMessage("Delele: " + book.getName());
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            List<Book> listBooks= new ArrayList<>();
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this, "Click yes", Toast.LENGTH_SHORT).show();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(listBooks!=null || !listBooks.isEmpty())
                            listBooks.clear();

                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Book b = dataSnapshot.getValue(Book.class);
                            if (b.getId()==book.getId()){
                                dataSnapshot.getRef().removeValue();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }
}