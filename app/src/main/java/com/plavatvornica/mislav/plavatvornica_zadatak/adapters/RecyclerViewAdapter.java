package com.plavatvornica.mislav.plavatvornica_zadatak.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.plavatvornica.mislav.plavatvornica_zadatak.R;
import com.plavatvornica.mislav.plavatvornica_zadatak.database.DatabaseHandler;
import com.plavatvornica.mislav.plavatvornica_zadatak.models.Article;

import java.util.List;

/**
 * Created by Mislav on 22.6.2017..
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Article> articleList;
    private Context context;
    private OnItemClickListener listener;
    private boolean downloadImages;

    public RecyclerViewAdapter(Context context, List<Article> articles, OnItemClickListener listener, boolean downloadImages) {
        this.context = context;
        this.articleList = articles;
        this.listener = listener;
        this.downloadImages = downloadImages;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row, parent, false);
        return new ViewHolder(view);
        /*LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.layout_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;*/
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Article article = articleList.get(position);
        if(article != null){
            holder.bind(position, listener);
            TextView textView = holder.textTitle;
            textView.setText(article.getTitle());
            if (downloadImages) {
                Glide.with(context).load(article.getUrlToImage()).placeholder(R.mipmap.ic_launcher).fitCenter().error(R.mipmap.ic_launcher).into(holder.imageView);
            } else {
                DatabaseHandler db = new DatabaseHandler(context);
                Glide.with(context).load(db.getArticle(position + 1).getImageByte()).asBitmap().placeholder(R.mipmap.ic_launcher).fitCenter().error(R.mipmap.ic_launcher).into(holder.imageView);
            }
        }

        //ImageView imageView = holder.imageView:
        //imageView.setim
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textTitle;

        public ViewHolder(final View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.text_row);
            imageView = (ImageView) itemView.findViewById(R.id.image_row);
        }

        public void bind(final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }
}
