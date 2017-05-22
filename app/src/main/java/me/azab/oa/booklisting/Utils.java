package me.azab.oa.booklisting;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 5/21/2017.
 */

public class Utils {

    /** Tag for the log messages */
    private static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Query for Books Api and get list of book objects
     */
    public static List<Book> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Book> books = parseJsonResponse(jsonResponse);

        // Return the list of {@link Earthquake}s
        return books;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.i(LOG_TAG, "Success response code: " + jsonResponse);

            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     *  This Method parse the json response you got from books api and
     *  return list of book
     *
     * @param response json string response form api
     * @return List of Book objects
     */
    public static List<Book> parseJsonResponse(String response){

        List<Book> bookList = new ArrayList<>();

        try {
            JSONObject rootObject = new JSONObject(response);
            JSONArray itemsArray = rootObject.getJSONArray("items");
            for(int i = 0 ; i<itemsArray.length() ; i++){
                JSONObject bookObject = itemsArray.getJSONObject(i);
                JSONObject bookInfoObject = bookObject.getJSONObject("volumeInfo");

                // get book title
                String title = bookInfoObject.getString("title");

                String[] authors;
                if (bookInfoObject.has("authors")) {
                    JSONArray bookAuthors = bookInfoObject.getJSONArray("authors");
                    // get book authors
                    authors = new String[bookAuthors.length()];
                    for(int j = 0 ; j<bookAuthors.length(); j++){
                        authors[j]=bookAuthors.getString(j);
                    }
                }else{
                    authors = new String[]{""};
                }
//                commented for now
//                JSONObject bookImageLinksObject = bookInfoObject.getJSONObject("imageLinks");
//
//                //get book image
//                String imageUrl = bookImageLinksObject.getString("thumbnail");
                String imageUrl = "";

                // add book object to bookList
                bookList.add(new Book(title,authors,imageUrl));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing Json ", e);
        }

        return bookList;
    }
}
