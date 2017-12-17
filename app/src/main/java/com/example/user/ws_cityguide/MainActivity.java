package com.example.user.ws_cityguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, DevListPlaceActivity.class);
                        intent.putExtra("cat_ref_number", "1");
                        startActivity(intent);
                  }
            });
            /*
            หลังจากแอปเสร็จสมบูรณ์แล้ว ให้ซ่อนปุ่ม FAB โดยเอาคอมเมนต์ออก
             */
            //fab.hide();

            randomParallaxImage();
            createItem();
      }

      private void randomParallaxImage() {
            ImageView imgParallax = (ImageView)findViewById(R.id.img_parallax);
            int[] images = {
                    R.drawable.p_1,
                    R.drawable.p_2,
                    R.drawable.p_3,
                    R.drawable.p_4
            };

            int r = new Random().nextInt(images.length);
            imgParallax.setBackgroundResource(images[r]);
      }

      private void createItem() {
            int[] icons = {
                    R.drawable.ic_beach,
                    R.drawable.ic_dining,
                    R.drawable.ic_hotel,
                    R.drawable.ic_shopping,
                    R.drawable.ic_spa,
                    R.drawable.ic_hospital,
                    R.drawable.ic_gov,
                    R.drawable.ic_my_location
            };

            final LayoutInflater inflater = getLayoutInflater();
            LinearLayout root = (LinearLayout)findViewById(R.id.main_layout);
            //root.removeAllViewsInLayout();

            int count = CityGuideCategory.values().length;
            for(int i = 0; i < count; i++) {
                  //แปลงเลย์เอาต์แบบ XML เป็น Java Object แล้วกำหนดขนาดด้วย LayoutParams
                  View item = inflater.inflate(R.layout.main_item_layout, null);
                  item.setLayoutParams(new ViewGroup.LayoutParams(
                          ViewGroup.LayoutParams.MATCH_PARENT,
                          ViewGroup.LayoutParams.WRAP_CONTENT));

                  //การอ้างถึงวิวบนรายการ ต้องทำผ่านออบเจ็กต์ที่เรา inflate มา
                  ImageView img = (ImageView)item.findViewById(R.id.img_category);
                  img.setImageResource(icons[i]);

                  TextView text = (TextView)item.findViewById(R.id.text_category);
                  String str = CityGuideCategory.values()[i].getThaiName();  //ชื่อหมวดหมู่
                  text.setText(str);

                  final String catRefNum = CityGuideCategory.values()[i].getRefNumber() + "";  //เลขอ้างอิง

                  //เมื่อคลิกที่รายการใด ให้แนบเลขอ้างอิงของหมวดหมู่นั้นไว้กับอินเทนต์
                  //แล้วส่งไปยังแอคทิวิตี่ที่แสดงรายการสถานที่
                  item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              Intent intent = new Intent(MainActivity.this, ListPlaceActivity.class);
                              intent.putExtra("cat_ref_number", catRefNum);
                              startActivity(intent);
                        }
                  });
                  //เพิ่มวิวลงในเลย์เอาต์ของ NestedScrollView (ในที่นี้คือ LinearLayout)
                  root.addView(item, i);
            }
      }

      @Override
      public void onBackPressed() {
            finishAffinity();
            System.exit(0);
      }

     /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

}

