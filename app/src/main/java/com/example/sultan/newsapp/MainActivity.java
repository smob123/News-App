package com.example.sultan.newsapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Fragment {

    private ListView list;
    private CardAdapter adapter;
    private String url;
    String title, imgURL, description, articleUrl;

    public MainActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        list = view.findViewById(R.id.newsList);

        return view;
    }

    private void fetch() {
        final ArrayList<NewsCard> cardList = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getUrl(), null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray articles = response.getJSONArray("articles");

                    for (int i = 0; i < articles.length(); i++) {
                        JSONObject obj = articles.getJSONObject(i);

                        title = obj.getString("title");
                        imgURL = obj.getString("urlToImage");
                        description = obj.getString("description");
                        articleUrl = obj.getString("url");

                        if (!title.equals("null") && !imgURL.equals("null") && !description.equals("null")) {
                            cardList.add(new NewsCard(imgURL, title, description, articleUrl));
                        }
                    }

                    adapter = new CardAdapter(getActivity(), cardList);
                    list.setAdapter(adapter);

                    handleClick(list);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseArray) {
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }

    public void setUrl(String u) {
        url = u;
        fetch();
    }

    public String getUrl() {
        return url;
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
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        //replace view
        transaction.replace(R.id.webFrag, webFrag);

        //add transaction to back stack
        transaction.addToBackStack(null);

        //commit transaction
        transaction.commit();
    }

    public void onBackPressed() {
        getActivity().getFragmentManager().popBackStack();
    }
}
