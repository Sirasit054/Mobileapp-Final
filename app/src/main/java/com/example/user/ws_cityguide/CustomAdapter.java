package com.example.user.ws_cityguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
//import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomHolder> {
      private Context mContext;
      private ArrayList<RowItem> mItems;

      public CustomAdapter(Context context, ArrayList<RowItem> items) {
            mContext = context;
            mItems = items;
      }

      @Override
      public CustomHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            final View v = inflater.inflate(R.layout.list_pace_item_layout, parent, false);
            final CustomHolder vHolder = new CustomHolder(v);
            return vHolder;
      }

      @Override
      public void onBindViewHolder(final CustomHolder vHolder, int position) {
            final RowItem item = mItems.get(position);
            vHolder.textName.setText(item.name);
            vHolder.imgContent.setImageURI(Uri.parse(item.imagePath));

            //ถ้ารายการนั้นมีจำนวนภาพมากกว่าหนึ่ง ให้แสดงตัวเลขจำนวนที่เหลือบน TextView ที่วางซ้อนอยู่บนภาพ
            if(item.imageCount > 1) {
                  vHolder.textImageCount.setText("+" + (item.imageCount - 1));
            }
            vHolder.textDescription.setText(item.description);
            vHolder.ratingBar.setRating(item.rating);

            //เมื่อคลิกที่ภาพหมุดตรงมุมขวาบน ให้เปิดแผนที่โดยส่งพิกัดตำแหน่งไปกับอินเทนต์
            vHolder.imgPin.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        int pos = vHolder.getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION) {
                              String geo =  "geo:" + item.latitude + "," + item.longitude + "&z=16";
                              Intent intent = new Intent(Intent.ACTION_VIEW);
                              intent.setData(Uri.parse(geo));
                              intent.setPackage("com.google.android.apps.maps");
                              mContext.startActivity(intent);
                        }
                  }
            });

            //เมื่อคลิกข้อความ "รายละเอียด" ให้เปิดไปยังแอคทิวิตี้แสดงรายละเอียด โดยส่ง _id ไปกับอินเทนต์
            vHolder.textReadMore.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        int pos = vHolder.getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION) {
                              Intent intent = new Intent(mContext, PlaceDetailActivity.class);
                              intent.putExtra("_id", item._id);

                              //จับคู่สำหรับการทำ Shared Element Transition
                              Pair<View, String> p1 = new Pair((View) vHolder.imgContent, "img_transition");
                              ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, p1);

                              mContext.startActivity(intent, options.toBundle());
                        }
                  }
            });
      }

      @Override
      public int getItemCount() {
            return mItems.size();
      }

}

