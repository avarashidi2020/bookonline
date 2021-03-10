package ir.app.rashidi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.app.rashidi.R;
import ir.app.rashidi.entity.Book;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {
    private Context context;
    private List<Book> books;


    public BookListAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.book_list_item_row,parent,false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText(book.getName());
        Picasso.get().load(book.getImage()).into(holder.img);
    }


    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (books != null)
            return books.size();
        return 0;
    }

    static class BookViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView img;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.bookTitle);
            img = itemView.findViewById(R.id.bookImg);
        }
    }
}
