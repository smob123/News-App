package com.example.sultan.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RegionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView regions;
    private final ArrayList<String> nameList = new ArrayList<>();
    final ArrayList<String> areaCodeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        setTitle("Change region");

        regions = findViewById(R.id.regionList);

        ReadJson();
    }

    private void ReadJson() {
        String json;

        try {
            InputStream is = getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                    JSONObject country = array.getJSONObject(i);

                    String name = country.getString("Name");
                    String code = country.getString("Code");

                    nameList.add(name);
                    areaCodeList.add(code);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayAdapter adapter = new ArrayAdapter<>(regions.getContext(),
                android.R.layout.simple_list_item_1, nameList);

        regions.setAdapter(adapter);
        regions.setOnItemClickListener(RegionActivity.this);

        searchHandler(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("code", areaCodeList.get(nameList.indexOf(parent
                .getItemAtPosition(position).toString())));
        Intent main = new Intent(getApplication(), DrawerActivity.class);
        main.putExtras(bundle);
        startActivity(main);
    }

    public void searchHandler(final ArrayAdapter regionList) {
        EditText searchBar = findViewById(R.id.search_bar);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regionList.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}
