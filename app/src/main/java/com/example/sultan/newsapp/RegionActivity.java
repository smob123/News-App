package com.example.sultan.newsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RegionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView regions;
    private final String url = "https://pkgstore.datahub.io/core/country-list/data_json/data/8c458f2d15d9f2119654b29ede6e45b8/data_json.json";
    private final ArrayList<String> nameList = new ArrayList<>();
    final ArrayList<String> codeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        setTitle("Change region");

        regions = findViewById(R.id.regionList);

        addRegions();
    }

    public void addRegions() {


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {}

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {}

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseArray) {
                for(int i = 0; i < responseArray.length(); i++) {
                    try {
                        JSONObject country = responseArray.getJSONObject(i);

                        String name = country.getString("Name");
                        String code = country.getString("Code");

                        nameList.add(name);
                        codeList.add(code);
                    }
                    catch (Exception e){
                        System.out.println(e);
                    }
                }

                final ArrayAdapter adapter = new ArrayAdapter<>(regions.getContext(),
                        android.R.layout.simple_list_item_1, nameList);

                regions.setAdapter(adapter);
                regions.setOnItemClickListener(RegionActivity.this);
            }

            @Override
            public void onRetry(int retryNo) {}
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("code", codeList.get(position));
        Intent main = new Intent(getApplication(), DrawerActivity.class);
        main.putExtras(bundle);
        startActivity(main);
    }
}
