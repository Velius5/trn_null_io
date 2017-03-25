package com.nullio.opinieallegro.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nullio.opinieallegro.AddReviewActivity;
import com.nullio.opinieallegro.R;
import com.nullio.opinieallegro.activity.ItemReviewsActivity;
import com.nullio.opinieallegro.model.Item;
import com.nullio.opinieallegro.transfer.OfferResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WatchedItemsListAdapter extends ArrayAdapter {
    private Activity context;
    private List<OfferResponse> itemList = new ArrayList<>();

    public WatchedItemsListAdapter(Activity context, List<OfferResponse> items) {
        super(context, R.layout.list_item, items);
        this.context = context;
        this.itemList = items;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item, null, true);

        ImageView image = (ImageView) rowView.findViewById(R.id.image);
        TextView title = (TextView) rowView.findViewById(R.id.title);

        Picasso.with(context).load(itemList.get(position).getMain().getMedium()).into(image);
        title.setText(itemList.get(position).getName());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToItemReviews(itemList.get(position).getId());
            }
        });
        return rowView;
    }

    private void goToItemReviews(String id) {
//        Intent intent = new Intent(context, ItemReviewsActivity.class);
//        intent.putExtra(ItemReviewsActivity.OFFER_ID, id);
//        context.startActivity(intent);
    }



}


