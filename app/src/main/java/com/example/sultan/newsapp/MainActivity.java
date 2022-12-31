package com.example.sultan.newsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
    private String title, imgURL, description, articleUrl, source;
    private StoreCache cache;

    public MainActivity() {
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        list = view.findViewById(R.id.newsList);

        //allow nested scrolling for the CollapsingToolbar to work
        list.setNestedScrollingEnabled(true);
        ViewCompat.setNestedScrollingEnabled(list, true);

        cache = new StoreCache(getContext());
        checkCache();

        refreshLayout(view);
        return view;
    }

    /*checks if there is any cache data from previous sessions*/
    private void checkCache() {
        JSONObject object = cache.getCache();

        if (object != null) {
            addToList(object);
        }

        fetch(); //fetch data from api
    }

    /*handles swipe-to-refresh for the list*/
    private void refreshLayout(View view) {
        final SwipeRefreshLayout layout = view.findViewById(R.id.refresh_layout);

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch();
                layout.setRefreshing(false);
            }
        });
    }

    /*fetched data from api*/
    private void fetch() {
        AsyncHttpClient client = new AsyncHttpClient();

        String userAgentString =new WebView(getContext()).getSettings().getUserAgentString();

        client.addHeader("User-Agent", userAgentString);

        client.get(getUrl(), null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                addToList(response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseArray) {
            }

            @Override
            public void onRetry(int retryNo) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("failure: " + errorResponse.toString());
            }
        });
    }

    /*adds news cards to listview*/
    private void addToList(JSONObject object) {
        final ArrayList<NewsCard> cardList = new ArrayList<>();

        try {
            //convert the passed json object into a json array
            JSONArray articles = object.getJSONArray("articles");

            //store array data in cache
            cache.storeData(object);

            //loop through the array, and only add valid data to the listview
            for (int i = 0; i < articles.length(); i++) {
                JSONObject obj = articles.getJSONObject(i);

                title = obj.getString("title");
                imgURL = obj.getString("urlToImage");
                description = obj.getString("description");
                articleUrl = obj.getString("url");
                source = obj.getJSONObject("source").getString("name");

                if (!title.equals("null")) {
                    cardList.add(new NewsCard(imgURL, title, description, articleUrl, source));
                }
            }

            adapter = new CardAdapter(getActivity(), cardList);
            list.setAdapter(adapter);
            getPermissions(adapter);

            //handle user clicks for each card
            handleClick(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUrl(String u) {
        url = u;
    }

    public String getUrl() {
        return url;
    }

    /*handles user clicks on cards*/
    private void handleClick(ListView view) {
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //open the news article in a webview
                browse(adapter.getItem(i).getWebsite());
            }
        });
    }

    /*open a new webview*/
    private void browse(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        Intent browser = new Intent(getActivity().getApplication(), WebActivity.class);
        browser.putExtras(bundle);
        startActivity(browser);
    }

    /*handles back button presses*/
    public void onBackPressed() {
        //minimize the application instead of unmounting the fragment
        getActivity().getFragmentManager().popBackStack();
    }

    private void getPermissions(CardAdapter adapter) {
        String permissions[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(getContext(), permissions[i]) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), permissions, 1);
            }
        }
    }
}
