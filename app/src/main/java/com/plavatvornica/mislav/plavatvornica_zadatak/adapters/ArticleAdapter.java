package com.plavatvornica.mislav.plavatvornica_zadatak.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.plavatvornica.mislav.plavatvornica_zadatak.R;
import com.plavatvornica.mislav.plavatvornica_zadatak.database.DatabaseHandler;
import com.plavatvornica.mislav.plavatvornica_zadatak.models.Article;

import java.util.List;


public class ArticleAdapter extends ArrayAdapter<Article> {

    private List<Article> articleList;
    private Context context;
    private LayoutInflater mInflater;
    private boolean downloadImages;


    public ArticleAdapter(Context context, List<Article> articles, boolean downloadImages) {
        super(context, 0, articles);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.articleList = articles;
        this.downloadImages = downloadImages;
    }

    @Override
    public Article getItem(int position) {
        return articleList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Article article = getItem(position);
        if (article != null) {
            vh.textTitle.setText(article.getTitle());
            if (downloadImages) {
                Glide.with(context).load(article.getUrlToImage()).placeholder(R.mipmap.ic_launcher).fitCenter().error(R.mipmap.ic_launcher).into(vh.imageView);
            } else {
                DatabaseHandler db = new DatabaseHandler(context);
                Glide.with(context).load(db.getArticle(position + 1).getImageByte()).asBitmap().placeholder(R.mipmap.ic_launcher).fitCenter().error(R.mipmap.ic_launcher).into(vh.imageView);
            }
        }


        return vh.rootView;
    }


    private static class ViewHolder {
        private final RelativeLayout rootView;
        private final ImageView imageView;
        private final TextView textTitle;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewTitle) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textTitle = textViewTitle;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.image_row);
            TextView textViewName = (TextView) rootView.findViewById(R.id.text_row);
            return new ViewHolder(rootView, imageView, textViewName);
        }
    }
}
