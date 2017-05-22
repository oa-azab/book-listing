package me.azab.oa.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by omar on 5/22/2017.
 */

public class BooksAdapter extends ArrayAdapter<Book> {

    public BooksAdapter(@NonNull Context context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View ItemView = convertView;
        if (ItemView == null) {
            ItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_book, parent, false);
        }

        // Get current book object from list of books
        Book currentBook = getItem(position);

        // Find item layout view
        TextView bookTitleText = (TextView) ItemView.findViewById(R.id.item_book_title);
        TextView bookAuthorsText = (TextView) ItemView.findViewById(R.id.item_book_authors);

        // Bind data to views
        bookTitleText.setText(currentBook.getTitle());
        StringBuilder formated = new StringBuilder(currentBook.getAuthors()[0]);
        for (int i = 1; i < currentBook.getAuthors().length; i++) {
            formated.append(", ");
            formated.append(currentBook.getAuthors()[i]);
        }
        bookAuthorsText.setText(formated);

        return ItemView;
    }
}
