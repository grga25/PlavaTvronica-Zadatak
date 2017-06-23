package com.plavatvornica.mislav.plavatvornica_zadatak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.plavatvornica.mislav.plavatvornica_zadatak.database.DatabaseHandler;
import com.plavatvornica.mislav.plavatvornica_zadatak.models.Article;
import com.plavatvornica.mislav.plavatvornica_zadatak.utils.Utils;


public class SingleNewsFragment extends Fragment {

    TextView text_title, text_description;
    ImageView image;
    DatabaseHandler db;
    View view;
    SingleNewsActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_single_news, parent, false);

        activity = (SingleNewsActivity) getActivity();
        if (activity != null) {
            int position = activity.getPosition();
            db = new DatabaseHandler(getContext());
            setSingleView(position);
            activity.registerReceiver(myReceiver, new IntentFilter(SingleNewsActivity.KEY_BROADCAST_INTENT));
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (activity != null) {
            activity.unregisterReceiver(myReceiver);
        }
    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra(SingleNewsActivity.KEY_POSITION, 0);
            setSingleView(position);
        }
    };

    private void setSingleView(int position) {
        Article article = db.getArticle(position + 1);
        text_title = (TextView) view.findViewById(R.id.text_title);
        text_description = (TextView) view.findViewById(R.id.text_description);
        image = (ImageView) view.findViewById(R.id.image_article);
        text_title.setText(article.getTitle());
        text_description.setText(article.getDescription());
        image.setImageBitmap(Utils.getImage(article.getImageByte()));
    }

}
