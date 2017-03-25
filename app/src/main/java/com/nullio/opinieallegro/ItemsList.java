package com.nullio.opinieallegro;

import android.content.Context;
import android.content.Intent;
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

import com.nullio.opinieallegro.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemsList extends AppCompatActivity {
    private ListView itemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        itemsListView = (ListView) findViewById(R.id.list);
        getData();

        BoughtLoader boughtLoader = new BoughtLoader(this);
        String result = boughtLoader.getResult();
        if (result=="login") {
            Toast.makeText(this, "Sesja wygasła. Zaloguj się ponownie", Toast.LENGTH_SHORT).show();
            finish();
        } else if (result == "success"){
            // pobrano dane
        }
    }

    private void getData() {
        List<Item> mockList = new ArrayList<>();
        mockList.add(new Item(35, "Polka nascienna", "https://upload.wikimedia.org/wikipedia/commons/0/09/Mieszaniec_czarny_978.jpg"));
        mockList.add(new Item(11, "Kot", "http://kot.net.pl/res/500x500_czy-koty-rozumieja-co-do-nich-mowimy-atdb.jpg"));
        mockList.add(new Item(97, "Pies", "http://g.wieszjak.polki.pl/p/_wspolne/pliki_infornext/210000/zaba_www_sxc_hu_210578.jpg"));
        SingleItemAdapter adapter = new SingleItemAdapter(this, mockList);
        itemsListView.setAdapter(adapter);
    }

    private class SingleItemAdapter extends ArrayAdapter<Item> {
        private final Context context;
        private final List<Item> values;

        public SingleItemAdapter(Context context, List<Item> values) {
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
            textView.setText(values.get(position).getTitle());
            Picasso.with(context).load(values.get(position).getPhotoUrl()).into(imageView);
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToAddReview(values.get(position).getId());
                }
            });
            return rowView;
        }

        private void goToAddReview(int title) {
            Intent intent = new Intent(context, AddReviewActivity.class);
            intent.putExtra(AddReviewActivity.OFFER_ID, title);
            startActivity(intent);
        }
    }
}
