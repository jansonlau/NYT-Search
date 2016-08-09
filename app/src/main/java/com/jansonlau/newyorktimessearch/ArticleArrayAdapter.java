// 3. Output all article in a grid view using an array adapter

package com.jansonlau.newyorktimessearch;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by jansonlau on 8/7/16.
 */

// Extend from ArrayAdapter of type <Article>
public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get data item for position
        Article article = this.getItem(position);

        // Check if existing view is being reused
        // If using recycled view, inflate layout
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }

        // Find image view
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);

        // Clear out recycled image from convertView from last time
        imageView.setImageResource(0);

        // Declare and initialize tvTitle (headline)
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        // Set tvTitle's text
        tvTitle.setText(article.getHeadLine());

        // Populate the thumbnail image
        // Remote download the image in the background
        String thumbnail = article.getThumbNail(); // Get reference to a thumbnail

        if (!TextUtils.isEmpty(thumbnail)) { // If thumbnail is not empty
            // Optional (Instead of using Picasso, I used Glide)
            Glide.with(getContext()).load(thumbnail)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(imageView);
        }

        return convertView;
    }
}
