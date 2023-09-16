package com.a51703210_demo.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a51703210_demo.activities_fragments.ViewDetailBookActivity;
import com.a51703210_demo.models.Book;
import com.example.a51703210_demo.R;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private List<Book> lstBook;
    private Context mContext;
    public BooksAdapter(Context context, List<Book> lstBook) {
        this.mContext = context;
        this.lstBook = lstBook;
    }
    @NonNull
    @Override
    public BooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_row, parent,false);
        BooksAdapter.ViewHolder holder = new BooksAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BooksAdapter.ViewHolder holder, int position) {
        Book book = lstBook.get(position);
        if(book==null)
            return;

        holder.tvBookName.setText(book.getName());
        holder.tvBookAuthor.setText(book.getAuthor());
        holder.tvBookCategory.setText(book.getCategory().getName());
        holder.tvBookLocation.setText("Location: "+book.getLocation());
        holder.tvBookNumber.setText("Available: "+String.valueOf(book.getNumber()));
        holder.layout_book_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Size", book.getName());
                onClickDetail(book);
            }
        });
    }

    private void onClickDetail(Book book) {
        Intent i =new Intent(mContext, ViewDetailBookActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putSerializable("object_book", book);
        bundle.putString("book_name", book.getName());
        bundle.putString("book_author", book.getAuthor());
        bundle.putInt("book_number", book.getNumber());
        bundle.putString("book_location", book.getLocation());
        bundle.putString("book_category", book.getCategory().getName());
        bundle.putInt("book_category_id", book.getCategory().getId());

        i.putExtras(bundle);
        mContext.startActivity(i);
    }

    @Override
    public int getItemCount() {
        if(lstBook!=null)
            return lstBook.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_book_item;
        TextView tvBookName, tvBookAuthor, tvBookCategory, tvBookLocation, tvBookNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName =  itemView.findViewById(R.id.tvBookName);
            tvBookAuthor =  itemView.findViewById(R.id.tvBookAuthor);
            tvBookCategory =  itemView.findViewById(R.id.tvBookCategory);
            tvBookLocation =  itemView.findViewById(R.id.tvBookLocation);
            tvBookNumber =  itemView.findViewById(R.id.tvBookNumber);
            layout_book_item = itemView.findViewById(R.id.layout_book_item);
        }
    }
    public void release(){
        mContext=null;
    }
}
