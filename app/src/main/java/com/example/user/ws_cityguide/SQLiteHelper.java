package com.example.user.ws_cityguide;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

      private static final String DATABASE_NAME = "db_city_guide";
      private static final int VERSION = 1;
      private static SQLiteHelper sqliteHelper;

      private SQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
      }

      public static synchronized SQLiteHelper getInstance(Context c) {
            if(sqliteHelper == null) {
                  sqliteHelper = new SQLiteHelper(c.getApplicationContext());
            }
            return sqliteHelper;
      }

      @Override
      public void onCreate(SQLiteDatabase db) {
            String sql =
                    "CREATE TABLE table_place(" +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "category INTEGER, " +
                            "name TEXT, " +
                            "description TEXT, " +
                            "location TEXT," +
                            "latitude TEXT, " +
                            "longitude TEXT, " +
                            "phone TEXT, " +
                            "rating INTEGER, " +
                            "more_info TEXT, " +
                            "image_path_1 TEXT, " +
                            "image_path_2 TEXT, " +
                            "image_path_3 TEXT)";

            db.execSQL(sql);
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
      }
}
