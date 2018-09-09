package com.example.sultan.newsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
    String title, imgURL, description, articleUrl;

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

                        title = obj.getString("title");
                        imgURL = obj.getString("urlToImage");
                        description = obj.getString("description");
                        articleUrl = obj.getString("url");

                        if(!title.equals("null") && !imgURL.equals("null") && !description.equals("null")) {
                            cardList.add(new NewsCard(imgURL, title, description, articleUrl));
                        }
                    }

                    adapter = new CardAdapter(MainActivity.this, cardList);
                    list.setAdapter(adapter);

                    handleClick(list);
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

    private void handleClick(ListView view) {
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                browse(adapter.getItem(i).getWebsite());
            }
        });
    }

    private void browse(String url) {
        //create new fragment and transaction
        WebFragment frag = new WebFragment();
        frag.setUrl(url);
        Fragment webFrag = frag;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //replace view
        transaction.replace(R.id.webFrag, webFrag);

        //add transaction to back stack
        transaction.addToBackStack(null);

        //commit transaction
        transaction.commit();
    }
}
