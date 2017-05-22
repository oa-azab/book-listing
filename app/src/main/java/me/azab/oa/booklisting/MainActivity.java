package me.azab.oa.booklisting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String QUERY_URL = "https://www.googleapis.com/books/v1/volumes?q=android+kotlin&maxResults=5";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new BooksAsyncTask().execute(QUERY_URL);
    }

    private class BooksAsyncTask extends AsyncTask<String, Void, List<Book>>{

        @Override
        protected List<Book> doInBackground(String... urls) {
            List<Book> mBooks = Utils.fetchBookData(urls[0]);
            return mBooks;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            for(Book b : books){
                Log.d("TEST ",b.getTitle());
            }
        }
    }
}
