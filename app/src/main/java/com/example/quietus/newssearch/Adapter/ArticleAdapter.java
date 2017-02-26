package com.example.quietus.newssearch.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quietus.newssearch.Model.Article;
import com.example.quietus.newssearch.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import static android.R.attr.resource;

/**
 * Created by Quietus on 2017/2/24.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {
    public ArticleAdapter(Context context, ArrayList<Article> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.article_gridviewitem, parent, false);
            holder.imageView = (ImageView)convertView.findViewById(R.id.ivThumbnail);
            holder.textView = (TextView)convertView.findViewById(R.id.tvHeadline);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(article.getHeadline());
        Picasso.with(getContext()).load(article.getThumbnail()).into(holder.imageView);

        return convertView;
    }

    public static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
