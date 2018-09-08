package com.example.sultan.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ListView list;
    private CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetch();
    }

    private void fetch() {
        list = findViewById(R.id.newsList);
        final ArrayList<NewsCard> cardList = new ArrayList<>();

        final String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=1b3db723c84947058381da0ff4b821f7";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null,  new JsonHttpResponseHandler() {
            @Override
            public void onStart() {}

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray articles = response.getJSONArray("articles");

                    for(int i = 0; i < articles.length(); i++) {
                        JSONObject obj = articles.getJSONObject(i);

                        String title = obj.getString("title");
                        String imgURL = obj.getString("urlToImage");
                        String description = obj.getString("description");
                        String url = obj.getString("url");

                        if(!title.equals("null") && !imgURL.equals("null") && !description.equals("null")) {
                            cardList.add(new NewsCard(imgURL, title, description, url));
                        }
                    }

                    adapter = new CardAdapter(MainActivity.this, cardList);
                    list.setAdapter(adapter);
                }
                catch(Exception e){
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseArray) {}

            @Override
            public void onRetry(int retryNo) {}
        });
    }
}
