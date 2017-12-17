package com.example.user.ws_cityguide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andremion.floatingnavigationview.FloatingNavigationView;

import java.util.ArrayList;

public class ListPlaceActivity extends AppCompatActivity {
      private SQLiteDatabase mDb;
      private SQLiteHelper mSqlite;
      private FloatingNavigationView mFloatingNav;
      private Toolbar mToolbar;
      private int mCatRefNumber;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list_place);

            //ตรวจสอบอินเทนต์ว่ามีหมายเลขหมวดหมู่ส่งเข้ามาหรือไม่
            Intent intent = getIntent();
            if(intent.getStringExtra("cat_ref_number") != null) {
                  mCatRefNumber = Integer.valueOf(intent.getStringExtra("cat_ref_number"));
            } else {
                  mCatRefNumber = 0;
            }

            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setToolbarTitle(mCatRefNumber);

            setSupportActionBar(mToolbar);
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

            mSqlite = SQLiteHelper.getInstance(this);
            mDb = mSqlite.getWritableDatabase();

            createItems(mCatRefNumber);
            setFloatingNavView();
      }

      private void createItems(int catRefNumber) {
            String sql = "SELECT * FROM table_place ";
            if(catRefNumber != 0) {
                 sql += " WHERE category = " + catRefNumber;
            }

            Cursor cursor = mDb.rawQuery(sql, null);

            ArrayList<RowItem> arrayList = new ArrayList<>();
            RowItem item;
            int _id;
            int imageCount = 0;
            while(cursor.moveToNext()) {
                  _id = cursor.getInt(0);
                  item = new RowItem();
                  item._id = _id + "";
                  item.name = cursor.getString(2);
                  item.latitude = cursor.getString(5);
                  item.longitude = cursor.getString(6);
                  item.description = cursor.getString(3);
                  item.rating = cursor.getInt(8);

                  imageCount = 0;
                  if(cursor.getString(12) != null) {
                        imageCount++;
                        item.imagePath = cursor.getString(12);
                  }
                  if(cursor.getString(11) != null) {
                        imageCount++;
                        item.imagePath = cursor.getString(11);
                  }
                  if(cursor.getString(10) != null) {
                        imageCount++;
                        item.imagePath = cursor.getString(10);
                  }

                  item.imageCount = imageCount;

                  arrayList.add(item);


            }

            CustomAdapter adapter = new CustomAdapter(this, arrayList);
            RecyclerView rcv = (RecyclerView) findViewById(R.id.recyclerView);
            rcv.setAdapter(adapter);
            rcv.setLayoutManager(new LinearLayoutManager(this));
      }

      //เมื่อเลือกข้อมูลของหมวดหมู่ใดมาแสดง ให้เปลี่ยนข้อความบนทูลบาร์เป็นชื่อหมวดหมู่นั้น
      private void setToolbarTitle(int catRefNumber) {
            if(catRefNumber > 0) {
                  //ใช้หมายเลขอ้างอิงของหมวดหมู่ที่เลือก เพื่อเทียบหาชื่อของหมวดหมู่
                  String title = CityGuideCategory.values()[catRefNumber-1].getThaiName();
                  mToolbar.setTitle(title);
            } else if(catRefNumber == 0) {
                  mToolbar.setTitle("รวมทั้งหมด");
            }
      }

      private void setFloatingNavView() {
            //นำตัวเลือกจาก Enum มาสร้างเป็นรายการเมนูของ Floating Nav View
            mFloatingNav = (FloatingNavigationView)findViewById(R.id.floating_nav_view);
            CityGuideCategory[] cat = CityGuideCategory.values();
            int count = CityGuideCategory.values().length;
            for(int i = 0; i < count; i++) {
                  mFloatingNav.getMenu().add(0, cat[i].getRefNumber(), i + 1, cat[i].getThaiName());
                   //0  คือ id กลุ่มเมนู อาจกำหนดเป็นเลขตัวอะไรก็ได้
                  //cat[i].getRefNumber()  กำหนดเป็นค่า id ของเมนู
                  //i + 1   คือลำดับเมนู
                  //cat[i].getThaiName()  กำหนดเป็นข้อความบนรายการเมนู
            }

            //อีกวิธี
            /*
            int i = 1;
            for(CityGuideCategory cat : CityGuideCategory.values()) {
                  mFloatingNav.getMenu().add(0, cat.getRefNumber(), i + 1, cat.getThaiName());
                  i++;
            }
            */

            mFloatingNav.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                        mFloatingNav.open();
                  }
            });

            //เมื่อคลิกรายการเมนูของ Floating Nav View ให้นำค่า id ของเมนู
            //ซึ่งสร้างจากเลขอ้างอิงของแต่ละหมวดหมู่
            //มาใช้เป็นเงื่อนไขเพื่อเอาเฉพาะข้อมูลในหมวดหมู่นั้นมาแสดง
            mFloatingNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                  @Override
                  public boolean onNavigationItemSelected(MenuItem item) {
                        int catRefNumber = item.getItemId();
                        createItems(catRefNumber);
                        mFloatingNav.close();
                        mToolbar.setTitle(item.getTitle());
                        return true;
                  }
            });
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

      @Override
      public void onBackPressed() {
            if(mFloatingNav.isOpened()) {
                  mFloatingNav.close();
            } else {
                  super.onBackPressed();
            }
      }

      private void showToast(String msg) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
      }
}
