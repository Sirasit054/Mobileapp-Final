package com.example.user.ws_cityguide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class PlaceDetailActivity extends AppCompatActivity {
      private SQLiteDatabase mDb;
      private SQLiteHelper mSqlite;
      private String m_id;

      private ImageView mImgTop;
      private ImageView mImgThumb1;
      private ImageView mImgThumb2;
      private ImageView mImgThumb3;
      private ImageView[] mImgThumbs;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_place_detail);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("รายละเอียดของสถานที่");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                  }
            });
            fab.hide();

            Intent intent = getIntent();
            if(intent.getStringExtra("_id") != null) {
                  m_id = intent.getStringExtra("_id");
            } else {
                  return;
            }

            mSqlite = SQLiteHelper.getInstance(this);
            mDb = mSqlite.getWritableDatabase();

            mImgTop = (ImageView)findViewById(R.id.img_top);
            mImgThumb1 = (ImageView)findViewById(R.id.img_thumb_1);
            mImgThumb2 = (ImageView)findViewById(R.id.img_thumb_2);
            mImgThumb3 = (ImageView)findViewById(R.id.img_thumb_3);
            mImgThumbs = new ImageView[] {mImgThumb1, mImgThumb2, mImgThumb3};

            readData();
            setOnThumbnailClick();
      }

      private void readData() {
            String sql = "SELECT * FROM table_place WHERE _id = " + m_id;
            Cursor cursor = mDb.rawQuery(sql, null);
            if(!cursor.moveToFirst()) {
                  return;
            }

            //แสดงภาพ Thumbnail ทั้งสามอัน
            if(cursor.getString(10) != null) {
                  mImgThumbs[0].setImageURI(Uri.parse(cursor.getString(10)));
            }

            if(cursor.getString(11) != null) {
                  mImgThumbs[1].setImageURI(Uri.parse(cursor.getString(11)));
            }

            if(cursor.getString(12) != null) {
                  mImgThumbs[2].setImageURI(Uri.parse(cursor.getString(12)));
            }

            TextView tv;
            tv = (TextView)findViewById(R.id.text_name);
            tv.setText(cursor.getString(2));

            RatingBar rb = (RatingBar)findViewById(R.id.rating_bar);
            rb.setRating(cursor.getInt(8));

            tv = (TextView)findViewById(R.id.text_description);
            tv.setText(cursor.getString(3));

            tv = (TextView)findViewById(R.id.text_location);
            tv.setText("ที่ตั้ง: \n" + cursor.getString(4));

            final String pos = cursor.getString(5) + "," + cursor.getString(6);
            tv = (TextView)findViewById(R.id.text_position);
            tv.setText("พิกัดตำแหน่ง: \n" + pos);
            //เมื่อคลิกที่ข้อความแสดงแผนที่ ให้ส่งอินเทนต์พร้อมพิกัดไปยังแอป Google Maps
            tv = (TextView)findViewById(R.id.text_show_map);
            tv.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        String geo = "geo:" + pos + "?z=16";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(geo));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                  }
            });

            tv = (TextView)findViewById(R.id.text_phone);
            tv.setText("โทรศัพท์: \n" + cursor.getString(7));

            tv = (TextView)findViewById(R.id.text_more_info);
            tv.setText("ข้อมูลเพิ่มเติม: \n" + cursor.getString(9));

            cursor.close();
      }

      private void setOnThumbnailClick() {
            //เมื่อคลิกที่ภาพ Thumbnail แต่ละอัน
            mImgThumb1.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        onThumbnailClick(v);
                  }
            });

            mImgThumb2.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        onThumbnailClick(v);
                  }
            });

            mImgThumb3.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        onThumbnailClick(v);
                  }
            });

            mImgThumb1.performClick();   //ให้อันภาพแรกถูกคลิกโดยอัตโนมัติ (แสดงทันทีที่เปิดแอคทิวิตี้)
      }

      private void onThumbnailClick(View view) {
            //ภาพ Thumbnail อันใดถูกคลิก ให้นำไปแสดงที่ภาพใหญ่ (img_top)
            ImageView imageView = (ImageView)view;
            BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
            if(bitmapDrawable != null) {
                  Bitmap bmp = bitmapDrawable.getBitmap();
                  mImgTop.setImageBitmap(bmp);
            }
      }

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if(id == android.R.id.home) {
                  onBackPressed();
                  return true;
            }

            return super.onOptionsItemSelected(item);
      }
}