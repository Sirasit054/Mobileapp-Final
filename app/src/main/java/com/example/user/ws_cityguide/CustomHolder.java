package com.example.user.ws_cityguide;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;


public class CustomHolder extends RecyclerView.ViewHolder {
      public TextView textName;
      public ImageView imgPin;
      public RoundedImageView imgContent;
      public TextView textDescription;
      public RatingBar ratingBar;
      public TextView textImageCount;
      public TextView textReadMore;

      public CustomHolder(View view) {
            super(view);
            textName = (TextView)view.findViewById(R.id.text_name);
            imgPin = (ImageView)view.findViewById(R.id.img_pin);
            imgContent =  (RoundedImageView) view.findViewById(R.id.img_content);
            textDescription = (TextView)view.findViewById(R.id.text_description);
            ratingBar = (RatingBar)view.findViewById(R.id.rating_bar);
            textImageCount = (TextView)view.findViewById(R.id.text_image_count);
            textReadMore = (TextView)view.findViewById(R.id.text_read_more);
      }

}
