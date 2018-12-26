package com.example.sultan.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class CardAdapter extends ArrayAdapter<NewsCard> implements AdapterView.OnItemClickListener,
                                                                    Serializable {

    private Context mContext;
    private ArrayList<NewsCard> cardList;

    public CardAdapter(@NonNull Context context, ArrayList<NewsCard> list) {
        super(context, 0, list);
        mContext = context;
        cardList = list;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View card = convertView;
        if (card == null) {
            card = LayoutInflater.from(mContext).inflate(R.layout.activity_card, parent, false);
        }

        NewsCard currentCard = cardList.get(position);

        ImageView img = card.findViewById(R.id.newsImage);
        if(!currentCard.getUrl().equals("null")) {
            Picasso.get().load(currentCard.getUrl()).into(img);
        }

        TextView title = card.findViewById(R.id.title);
        title.setText(currentCard.getArticleTitle());

        TextView desc = card.findViewById(R.id.description);
        if(!currentCard.getDescription().equals("null")) {
            desc.setText(currentCard.getDescription());
        }
        else {
            desc.setVisibility(View.GONE);
        }

        TextView sourceView = card.findViewById(R.id.articleSource);
        sourceView.setText(currentCard.getSource());

        return card;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
