package com.nullio.opinieallegro.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nullio.opinieallegro.R;
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

    public ReviewsApadter(Activity context, List<ReviewDisplay> items) {
        super(context, R.layout.list_item, items);
        this.context = context;
        this.itemList = items;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.review_list_item, null, true);
        ImageView image = (ImageView) rowView.findViewById(R.id.image);
        TextView title = (TextView) rowView.findViewById(R.id.content);
        final ImageView fullscreen = (ImageView) rowView.findViewById(R.id.fullscreen);
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreen.setVisibility(View.GONE);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(context).load(itemList.get(position).getPhotoUrls().get(0)).into(fullscreen);
                fullscreen.setVisibility(View.VISIBLE);
            }
        });
        Picasso.with(context).load(itemList.get(position).getPhotoUrls().get(0)).into(image);
        title.setText(itemList.get(position).getContent());
        return rowView;
    }
}
