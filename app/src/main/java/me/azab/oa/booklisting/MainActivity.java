package me.azab.oa.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    ProgressBar mLoadingIndicator;
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
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        // Set loading indicator visiblity gone
        mLoadingIndicator.setVisibility(View.GONE);

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
                // Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is a network connection, fetch data
                if (networkInfo != null && networkInfo.isConnected()) {
                    String searchParam = mEditText.getText().toString();
                    searchParam = searchParam.replace(' ', '+');
                    new BooksAsyncTask().execute(BASE_QUERY_URL + searchParam);
                } else {
                    mEmptyView.setText(getString(R.string.no_connection));
                }
            }
        });
    }

    private class BooksAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }

        @Override
        protected List<Book> doInBackground(String... urls) {
            List<Book> mBooks = Utils.fetchBookData(urls[0]);
            return mBooks;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);

            if(books.size() == 0 ){
                mEmptyView.setText(getString(R.string.no_matching_results));
            }
            mBooksAdapter.clear();
            mBooksAdapter.addAll(books);
            mLoadingIndicator.setVisibility(View.GONE);
        }
    }
}
