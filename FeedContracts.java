package com.example.piotrnikadonzaliczeniowy;

import android.provider.BaseColumns;

public final class FeedContracts {
    private FeedContracts() {}

    public static class TableItems implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_USERID = "userId";
        public static final String COLUMN_TITLE = "itemTitle";
        public static final String COLUMN_PRICE = "itemPrice";
        public static final String COLUMN_DESC = "itemDesc";
        public static final String COLUMN_CATEGORY = "itemCateg";
        public static final String COLUMN_DATE = "dateMS";
        public static final String COLUMN_SOLD = "isSold";
    }

    public static class TableUsers implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
    }

    public static class TableCart implements BaseColumns {
        public static final String TABLE_NAME = "cart";
        public static final String COLUMN_ITEMID = "itemId";
    }
}