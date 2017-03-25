package com.nullio.opinieallegro.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nullio.opinieallegro.R;
import com.nullio.opinieallegro.activity.ReviewDetailsActivity;
import com.nullio.opinieallegro.model.ReviewDisplay;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bartoszlach on 25.03.2017.
 */
public class ReviewsApadter extends ArrayAdapter {
    private Activity context;
    private List<ReviewDisplay> itemList = new ArrayList<>();
    private final String title;

    public ReviewsApadter(Activity context, List<ReviewDisplay> items, String title) {
        super(context, R.layout.list_item, items);
        this.context = context;
        this.itemList = items;
        this.title = title;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.review_list_item, null, true);
        ImageView image = (ImageView) rowView.findViewById(R.id.image);
        TextView titleTextView = (TextView) rowView.findViewById(R.id.content);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReviewDetailsActivity.class);
                intent.putExtra(ReviewDetailsActivity.OFFER_TITLE, title);
                intent.putExtra(ReviewDetailsActivity.OFFER_PHOTO, itemList.get(position).getPhotoUrls().get(0));
                intent.putExtra(ReviewDetailsActivity.OFFER_CONTENT, itemList.get(position).getContent());
                context.startActivity(intent);
            }
        });
        Picasso.with(context).load(itemList.get(position).getPhotoUrls().get(0)).into(image);
        titleTextView.setText(itemList.get(position).getContent());
        return rowView;
    }
}
