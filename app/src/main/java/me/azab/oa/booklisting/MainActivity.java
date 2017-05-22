package me.azab.oa.booklisting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String QUERY_URL = "https://www.googleapis.com/books/v1/volumes?q=android+kotlin&maxResults=5";
    public static final String BASE_QUERY_URL = "https://www.googleapis.com/books/v1/volumes?q=";


    ListView mListView;
    EditText mEditText;
    Button mButton;
    TextView mEmptyView;
    BooksAdapter mBooksAdapter;
    List<Book> mBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find UI
        mListView = (ListView) findViewById(R.id.listview_books);
        mEditText = (EditText) findViewById(R.id.edit_text_search);
        mButton = (Button) findViewById(R.id.btn_search);
        mEmptyView = (TextView) findViewById(R.id.empty_view);

        // Initialize books adapter
        mBooksAdapter = new BooksAdapter(this, mBooks);

        // Set Adapter to listview
        mListView.setAdapter(mBooksAdapter);

        // Set Empty View
        mListView.setEmptyView(mEmptyView);

        // Handle Button Click
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchParam = mEditText.getText().toString();
                searchParam = searchParam.replace(' ','+');
                new BooksAsyncTask().execute(BASE_QUERY_URL+searchParam);
            }
        });
    }

    private class BooksAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(String... urls) {
            List<Book> mBooks = Utils.fetchBookData(urls[0]);
            return mBooks;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);

            mBooksAdapter.clear();
            mBooksAdapter.addAll(books);
        }
    }
}
