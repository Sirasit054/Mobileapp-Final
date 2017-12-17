package com.example.user.ws_cityguide;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.iceteck.silicompressorr.SiliCompressor;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.snatik.storage.Storage;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DevAddNewPlaceActivity extends AppCompatActivity {
      private SQLiteDatabase mDb;
      private SQLiteHelper mSqlite;

      private ImageView[] mImageViews;
      private EditText mEditName;
      private EditText mEditDescription;
      private EditText mEditLocation;
      private EditText mEditLatitude;
      private EditText mEditLongitude;
      private EditText mEditPhone;
      private EditText mEditMoreInfo;
      private Spinner mSpinner;
      private ScrollableNumberPicker mNumberPicker;

      private String mCategory;
      private String mName;
      private String mDescription;
      private String mLocation;
      private String mLatitude;
      private String mLongitude;
      private String mPhone;
      private String mMoreInfo;
      private String mRating;

      private Storage mStorage;
      private Bitmap mBitmaps[];
      private int mBitmapCount = 0;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dev_add_new_place);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("เพิ่มสถานที่ใหม่");
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

            mSqlite = SQLiteHelper.getInstance(this);
            mDb = mSqlite.getWritableDatabase();

            mStorage = new Storage(this);

            mImageViews = new ImageView[3];
            mImageViews[0] = (ImageView) findViewById(R.id.image_view1);
            mImageViews[1] = (ImageView) findViewById(R.id.image_view2);
            mImageViews[2] = (ImageView) findViewById(R.id.image_view3);

            mEditName = (EditText) findViewById(R.id.edit_name);
            mEditDescription = (EditText) findViewById(R.id.edit_descr);

            mEditLocation = (EditText) findViewById(R.id.edit_location);
            mEditLatitude = (EditText) findViewById(R.id.edit_lat);
            mEditLongitude = (EditText) findViewById(R.id.edit_lon);

            mEditPhone = (EditText) findViewById(R.id.edit_phone);
            mEditMoreInfo = (EditText) findViewById(R.id.edit_more_info);
            mNumberPicker = (ScrollableNumberPicker) findViewById(R.id.number_picker);

            //นำหมวดหมู่จาก Enum มาสร้างเป็ยรายการตัวเลือกของ Spinner
            ArrayList items = new ArrayList();
            for(CityGuideCategory value : CityGuideCategory.values()) {
                  items.add(value.getThaiName());
            }
            mSpinner = (Spinner) findViewById(R.id.spin_cat);
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item_layout, items);
            mSpinner.setAdapter(adapter);

            //เมื่อคลิกปุ่มเลือกภาพ
            findViewById(R.id.button_image).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        selectImage();
                  }
            });

            //เมื่อคลิกปุ่มบันทึกข้อมูล
            findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        saveData();
                  }
            });
      }

      //การเลือกภาพจาก Gallery ให้ใช้ไลบรารี Multimager โดยเลือกได้ไม่เกิน 3 ภาพ
      private void selectImage() {
            for(int i = 0; i < mImageViews.length; i++) {
                  mImageViews[i].setImageBitmap(null);
            }
            Intent intent = new Intent(this, GalleryActivity.class);
            Params params = new Params();
            params.setPickerLimit(3);
            params.setCaptureLimit(3);
            intent.putExtra(Constants.KEY_PARAMS, params);
            startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
      }

      @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if(resultCode != RESULT_OK) {
                  return;
            }

            if(requestCode == Constants.TYPE_MULTI_PICKER) {
                  ArrayList<Image> images = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                  try {
                        mBitmapCount = images.size();   //จำนวนภาพที่เลือกมาจาก Gallery
                        Bitmap bmp;
                        //ให้วนลูปตามจำนวน ImageView ที่มี ถ้าแล้วนำภาพที่เลือกจาก Gallery มาแสดงใน ImageView
                        //โดยหากจำนวนภาพที่เลือกมาน้อยกว่าจำนวน ImageView ที่มี ก็ให้ซ่อนอันที่ไม่มีภาพ
                        for(int i = 0; i < mImageViews.length; i++) {
                              if(i < images.size()) {
                                    //ลดขนาดของภาพด้วยไลบรารี SiliCompressor
                                    bmp = SiliCompressor.with(this).getCompressBitmap(images.get(i).imagePath);
                                    mImageViews[i].setImageBitmap(bmp);
                                    mImageViews[i].setVisibility(View.VISIBLE);
                              } else {
                                    mImageViews[i].setVisibility(View.INVISIBLE);
                              }
                        }
                  } catch(Exception ex) {
                        showToast(ex.getMessage());
                  }
            }
      }

      //อ่านข้อมูลจากวิวไปเก็บไว้ในตัวแปรฟิลด์ เพื่อรอการนำไปใช้งาน
      private boolean readDataFromView() {
            String errMsg = "";
            mBitmaps = new Bitmap[mBitmapCount];
            for(int i = 0; i < mBitmapCount; i++) {
                  if(mImageViews[i].getDrawable() != null) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable)mImageViews[i].getDrawable();
                        mBitmaps[i] = bitmapDrawable.getBitmap();
                  } else {
                        mBitmaps[i] = null;
                  }
            }

            //การจัดเรียงหมวดหมู่ที่แสดงใน Spinner จะเหมือนกับเลขลำดับอ้างอิงใน Enum
            //เพียงแต่เลขลำดับรายการของ Spinner จะเริ่มจาก 0 จึงบวกเพิ่มอีก 1 แล้วนำไปใช้เป็นเลขหมวดหมู่ได้เลย
            mCategory = mSpinner.getSelectedItemPosition() + 1 + "";

            mName = mEditName.getText().toString().trim();
            mDescription = mEditDescription.getText().toString().trim();
            mLocation = mEditLocation.getText().toString().trim();
            mLatitude = mEditLatitude.getText().toString().trim();
            mLongitude = mEditLongitude.getText().toString().trim();
            mPhone = mEditPhone.getText().toString().trim();
            mMoreInfo = mEditMoreInfo.getText().toString().trim();
            mRating = mNumberPicker.getValue() + "";

            if(mBitmapCount == 0) {
                  errMsg = "ต้องมีรูปภาพประกอบอย่างน้อย 1 ภาพ";
            } else if(mName.isEmpty()) {
                  errMsg = "กรุณาใส่ชื่อสถานที่";
            } else if(mDescription.isEmpty()) {
                  errMsg = "กรุณาใส่ลักษณะที่น่าสนใจเกี่ยวกับสถานที่";
            } else if(mLocation.isEmpty()) {
                  errMsg = "กรุณาใส่สถานที่ตั้ง";
            } else if(mLatitude.isEmpty() || mLongitude.isEmpty()) {
                  errMsg = "กรุณาใส่พิกัดตำแหน่ง";
            } else if(Double.valueOf(mLatitude) < -90.0 || Double.valueOf(mLatitude) > 90.0) {
                  errMsg = "ค่า Latitude ต้องอยู่ระหว่าง -90 ถึง 90";
            } else if(Double.valueOf(mLongitude) < -180.0 || Double.valueOf(mLongitude) > 180.0) {
                  errMsg = "ค่า Longitude ต้องอยู่ระหว่าง -180 ถึง 180";
            }

            if(!errMsg.isEmpty()) {
                  showToast(errMsg);
                  return false;
            } else if(!isValidNumber(mEditLatitude) || !isValidNumber(mEditLongitude)) {
                  return false;
            }

            return true;
      }

      private void saveData() {
            if(!readDataFromView()) {
                  return;
            }

            ContentValues cv = new ContentValues();
            cv.put("category", mCategory);
            cv.put("name", mName);
            cv.put("description", mDescription);
            cv.put("location", mLocation);
            cv.put("latitude", mLatitude);
            cv.put("longitude", mLongitude);
            cv.put("phone", mPhone);
            cv.put("rating", mRating);
            cv.put("more_info", mMoreInfo);

            //บันทึกภาพที่แสดงใน ImageView แต่ละอันไว้ใน Internal Storage
            //โดยใช้ timestamp ปัจจุบันเป็นชื่อไฟล์ แล้วนำเลข 1, 2, 3 มาต่อท้ายเพื่อแยก เพื่อปไม่ให้ชื่อซ้ำกัน
            long curTime = System.currentTimeMillis();
            String imagePath = "";
            for(int i = 0; i < mBitmaps.length; i++) {
                  if(mBitmaps[i] != null) {
                        imagePath = mStorage.getInternalFilesDirectory() + "/" + curTime + "_" + (i + 1) + ".png";
                        mStorage.createFile(imagePath, mBitmaps[i]);

                        //ต้องจัดเก็บตำแหน่งของภาพลงในฐานข้อมูลด้วย โดยแยกไว้คนละคอลัมน์
                        cv.put("image_path_" + (i + 1), imagePath);
                  }
            }

            long _id = mDb.insert("table_place", null, cv);

            //ถ้าสำเร็จจะคืนค่า _id ของแถวที่เพิ่มใหม่ ถ้าเกิด Error จะคืนค่า  -1
            if(_id != -1) {
                  mImageViews[0].setImageDrawable(null);
                  mImageViews[1].setImageDrawable(null);
                  mImageViews[2].setImageDrawable(null);
                  mEditName.setText("");
                  mEditDescription.setText("");
                  mEditLocation.setText("");
                  mEditLatitude.setText("");
                  mEditLongitude.setText("");
                  mEditPhone.setText("");
                  mEditMoreInfo.setText("");

                  //ลบภาพที่ถูกลดขนาดออกจากโฟลเดอร์ของ SiliCompressor เนื่องจากเราบันทึกไว้ใน Internal Storage แล้ว
                  String path = mStorage.getExternalStorageDirectory() + "/Silicompressor/images";
                  List<File> files = mStorage.getNestedFiles(path);
                  for(File f : files) {
                        mStorage.deleteFile(f.getPath());
                  }
                  showToast("บันทึกข้อมูลแล้ว");
            } else {
                  showToast("เกิดข้อผิดพลาด \nไม่สามารถบันทึกข้อมูลได้");
            }
      }

      private void showToast(String msg) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
      }

      private boolean isValidNumber(EditText editText) {
            try {
                  Double.valueOf(editText.getText().toString().trim());
            } catch(Exception ex) {
                  showToast("กรุณาใส่จำนวนที่ถูกต้อง");
                  return false;
            }
            return true;
      }

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if(id == android.R.id.home) {
                  finish();
                  return true;
            }

            return super.onOptionsItemSelected(item);
      }
}

