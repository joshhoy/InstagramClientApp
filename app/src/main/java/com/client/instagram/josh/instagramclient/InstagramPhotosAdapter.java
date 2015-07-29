package com.client.instagram.josh.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by joshho on 7/28/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    // What data do we need from activity
    // Context, Data source
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
    // what our items looks like
    // user the template to display each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data time for this position
        InstagramPhoto photo = getItem(position);
        // Check if we are using a recycled view, if not we need to inflate
        if (convertView == null) {
            // create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // Lookup the view for populating the data (photo, caption)
        TextView  tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        // Insert the item model data into each of the view items
        tvCaption.setText(photo.caption);
        // Clear out the imageview
        ivPhoto.setImageResource(0);
        // Insert the image using picasso
        // Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.ic_launcher).into(ivPhoto);
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);

        // Return the created item as a view
        return convertView;


    }

}
