package com.nullio.opinieallegro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nullio.opinieallegro.model.BoughtItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemsList extends AppCompatActivity {
    private ListView itemsListView;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        itemsListView = (ListView) findViewById(R.id.list);
        setTitle("Lista zakupionych przedmiotów");
        getUserId();
        BoughtLoader boughtLoader = new BoughtLoader(this);
        String result = boughtLoader.getResult();
        if (Objects.equals(result, "login")) {
            Toast.makeText(this, "Sesja wygasła. Zaloguj się ponownie", Toast.LENGTH_SHORT).show();
            finish();
        } else if (Objects.equals(result, "success")) {
            filterData(boughtLoader.getBoughtItems());
        }
    }

    private void getUserId() {
        SharedPreferences prefs = getSharedPreferences("settings", 0);
        userId = prefs.getString("userId", "0");
    }

    private void filterData(final List<BoughtItem> boughtItems) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("reviews");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> userReviewsMap = (Map<String, String>) dataSnapshot.getValue();
                List<BoughtItem> newItemsList = new ArrayList<>();
                if (userReviewsMap != null) {
                    for (BoughtItem item : boughtItems) {
                        if (!userReviewsMap.containsKey(item.getId())) {
                            newItemsList.add(item);
                        }
                    }
                }
                getData(newItemsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getData(List<BoughtItem> items) {
        SingleItemAdapter adapter = new SingleItemAdapter(this, items);
        itemsListView.setAdapter(adapter);
    }

    private class SingleItemAdapter extends ArrayAdapter<BoughtItem> {
        private final Context context;
        private final List<BoughtItem> values;

        public SingleItemAdapter(Context context, List<BoughtItem> values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.title);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
            textView.setText(values.get(position).getName());
            if (!values.get(position).getImageUrl().equals("")) {
                Picasso.with(context).load(values.get(position).getImageUrl()).into(imageView);
            }
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToAddReview(values.get(position));
                }
            });
            return rowView;
        }

        private void goToAddReview(BoughtItem item) {
            Intent intent = new Intent(context, AddReviewActivity.class);
            intent.putExtra(AddReviewActivity.OFFER_ID, item.getId());
            intent.putExtra(AddReviewActivity.OFFER_NAME, item.getName());
            startActivity(intent);
        }
    }
}
