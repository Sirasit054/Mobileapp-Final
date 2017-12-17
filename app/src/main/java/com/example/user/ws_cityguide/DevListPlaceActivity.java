package com.example.user.ws_cityguide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.snatik.storage.Storage;

import java.util.ArrayList;

public class DevListPlaceActivity extends AppCompatActivity {
      private SQLiteDatabase mDb;
      private SQLiteHelper mSqlite;
      private FloatingNavigationView mFloatingNav;
      private Toolbar mToolbar;
      private int mCatRefNumber = 1;
      private ListView mListView;
      private ArrayList<String> m_ids;
      private ArrayList<String> mItems;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dev_list_place);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);

            Intent intent = getIntent();
            if(intent.getStringExtra("cat_ref_number") != null) {
                  mCatRefNumber = Integer.valueOf(intent.getStringExtra("cat_ref_number"));
            } else {
                  mCatRefNumber = 0;
            }

            setToolbarTitle(mCatRefNumber);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        startActivity(new Intent(DevListPlaceActivity.this, DevAddNewPlaceActivity.class));
                  }
            });

            mSqlite = SQLiteHelper.getInstance(this);
            mDb = mSqlite.getWritableDatabase();
            mListView = (ListView) findViewById(R.id.listview);

            createItem(mCatRefNumber);

            setFloatingNavView();
      }

      private void createItem(int catRefNumber) {
            String sql = "SELECT _id, name FROM table_place ";
            if(catRefNumber != 0) {
                  sql += " WHERE category = " + catRefNumber;
            }

            Cursor cursor = mDb.rawQuery(sql, null);

            m_ids = new ArrayList<>();   //เก็บค่า _id ของแต่ละแถว
            mItems = new ArrayList<>();  //เก็บชื่อสถานที่

            while(cursor.moveToNext()) {
                  m_ids.add(cursor.getString(0));
                  mItems.add(cursor.getString(1));
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, mItems);
            mListView.setAdapter(adapter);
            mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //ให้นำลำดับของรายการที่ถูกคลิกไปเทียบหาค่า _id จาก ArrayList อีกอัน
                        String _id = m_ids.get(position);
                        deleteData(_id);
                  }
            });
      }

      private void deleteData(final String _id) {
            FragmentManager fm = getSupportFragmentManager();
            ConfirmDialog dlg = ConfirmDialog.newInstance("ต้องการลบข้อมูลทั้งหมดของสถานที่แห่งนี้จริงหรือไม่?", "ไม่ใช่", "ใช่");
            dlg.show(fm, null);
            dlg.setOnFinishDialogListener(new ConfirmDialog.OnFinishDialogListener() {
                  @Override
                  public void onFinishDialog(ConfirmDialog.Button button) {
                        if(button == ConfirmDialog.Button.Positive) {
                              //ลบภาพออกจาก Internal Storage
                              String sql = "SELECT * FROM table_place WHERE _id = " + _id;
                              Cursor cursor = mDb.rawQuery(sql, null);

                              Storage storage = new Storage(DevListPlaceActivity.this);
                              if(cursor.moveToNext()) {
                                    String imagePath1 = cursor.getString(10);
                                    if(imagePath1 != null) {
                                          storage.deleteFile(imagePath1);
                                    }

                                    String imagePath2 = cursor.getString(11);
                                    if(imagePath2 != null) {
                                          storage.deleteFile(imagePath2);
                                    }

                                    String imagePath3 = cursor.getString(12);
                                    if(imagePath3 != null) {
                                          storage.deleteFile(imagePath3);
                                    }
                              }

                              sql = "DELETE FROM table_place WHERE _id = " + _id;
                              mDb.execSQL(sql);

                              createItem(mCatRefNumber);     //โหลดใหม่
                        }
                  }
            });
      }

      private void setToolbarTitle(int catRefNumber) {
            if(catRefNumber > 0) {
                  String title = CityGuideCategory.values()[catRefNumber - 1].getThaiName();
                  mToolbar.setTitle(title);
            } else if(catRefNumber == 0) {
                  mToolbar.setTitle("รวมทั้งหมด");
            }
      }

      private void setFloatingNavView() {
            mFloatingNav = (FloatingNavigationView) findViewById(R.id.floating_nav_view);
            CityGuideCategory[] cat = CityGuideCategory.values();
            int count = CityGuideCategory.values().length;
            for(int i = 0; i < count; i++) {
                  mFloatingNav.getMenu().add(0, cat[i].getRefNumber(), i + 1, cat[i].getThaiName());
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

            mFloatingNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                  @Override
                  public boolean onNavigationItemSelected(MenuItem item) {
                        int catRefNumber = item.getItemId();
                        createItem(catRefNumber);
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
