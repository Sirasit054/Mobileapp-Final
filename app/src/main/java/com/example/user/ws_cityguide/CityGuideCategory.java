package com.example.user.ws_cityguide;

public enum CityGuideCategory {
      Travel("สถานที่ท่องเที่ยว", 1),
      Food("ร้านอาหารและเครื่องดื่ม", 2),
      Hotel("โรงแรมและที่พัก", 3),
      Shopping("แหล่งช้อปปิ้ง", 4),
      Beauty("สถานเสริมความงาม", 5),
      Clinic("คลินิก", 6),
      Government("สถานที่ราชการ", 7),
      Interest("สถานที่น่าสนใจอื่นๆ", 8);

      private CityGuideCategory(String place, int refNumber) {
            mThaiName = place;
            mRefNumber = refNumber;
      }

      private String mThaiName;
      private int mRefNumber;

      public String getThaiName() {
            return  mThaiName;
      }

      public int getRefNumber() {
            return mRefNumber;
      }
}

