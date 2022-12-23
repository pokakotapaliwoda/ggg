package com.example.piotrnikadonzaliczeniowy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.piotrnikadonzaliczeniowy.FeedContracts.TableItems;
import com.example.piotrnikadonzaliczeniowy.FeedContracts.TableUsers;
import com.example.piotrnikadonzaliczeniowy.FeedContracts.TableCart;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


public class DatabaseManager extends SQLiteOpenHelper {
    private final String TAG = "1111";
    Date date = new Date();

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "database.db";

    private static final String SQL_CREATE_TABLE_ITEMS =
            "CREATE TABLE "+TableItems.TABLE_NAME+" ("+
            TableItems._ID+" INTEGER PRIMARY KEY, "+
            TableItems.COLUMN_USERID+" INTEGER, "+
            TableItems.COLUMN_TITLE+" VARCHAR(64), "+
            TableItems.COLUMN_PRICE+" DECIMAL(15,2), "+
            TableItems.COLUMN_DESC+" TEXT, "+
            TableItems.COLUMN_CATEGORY+" VARCHAR(64), "+
            TableItems.COLUMN_DATE+" DATETIME, "+
            TableItems.COLUMN_SOLD+" INTEGER(1))";

    private static final String SQL_CREATE_TABLE_USERS =
            "CREATE TABLE "+TableUsers.TABLE_NAME+" ("+
            TableUsers._ID+" INTEGER PRIMARY KEY, "+
            TableUsers.COLUMN_USERNAME+" VARCHAR(32))";

    private static final String SQL_CREATE_TABLE_CART =
            "CREATE TABLE "+TableCart.TABLE_NAME+" ("+
            TableCart._ID+" INTEGER PRIMARY KEY, "+
            TableCart.COLUMN_ITEMID+" INTEGER)";

    private static final String SQL_DELETE_TABLE_ITEMS =
            "DROP TABLE IF EXISTS "+TableItems.TABLE_NAME;

    private static final String SQL_DELETE_TABLE_USERS =
            "DROP TABLE IF EXISTS "+TableUsers.TABLE_NAME;

    private static final String SQL_DELETE_TABLE_CART =
            "DROP TABLE IF EXISTS "+TableCart.TABLE_NAME;

    private static final String SQL_TRUNCATE_ITEMS =
            "DELETE FROM "+TableItems.TABLE_NAME;

    private static final String SQL_TRUNCATE_USERS =
            "DELETE FROM "+TableUsers.TABLE_NAME;

    private static final String SQL_TRUNCATE_CART =
            "DELETE FROM "+TableCart.TABLE_NAME;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_ITEMS);
        Log.d(TAG, "databaseManager created table "+TableItems.TABLE_NAME);
        db.execSQL(SQL_CREATE_TABLE_USERS);
        Log.d(TAG, "databaseManager created table "+TableUsers.TABLE_NAME);
        db.execSQL(SQL_CREATE_TABLE_CART);
        Log.d(TAG, "databaseManager created table "+TableCart.TABLE_NAME);
        // truncateDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL(SQL_DELETE_TABLE_ITEMS);
        db.execSQL(SQL_DELETE_TABLE_USERS);
        db.execSQL(SQL_DELETE_TABLE_CART);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        onUpgrade(db, oldVer, newVer);
    }

    public void truncateDatabase(SQLiteDatabase db) {
        db.execSQL(SQL_TRUNCATE_ITEMS);
        db.execSQL(SQL_TRUNCATE_USERS);
        db.execSQL(SQL_TRUNCATE_CART);
        Log.d(TAG, "(!) databaseManager truncate");
    }

    public void wipeDatabase(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_TABLE_ITEMS);
        db.execSQL(SQL_DELETE_TABLE_USERS);
        db.execSQL(SQL_DELETE_TABLE_CART);
        Log.d(TAG, "(!) databaseManager wipe");
    }

    public void emptyCart(SQLiteDatabase db) {
        db.execSQL(SQL_TRUNCATE_CART);
        Log.d(TAG, "emptied cart");
    }

    public void buyCart(SQLiteDatabase db, LinkedHashMap<Integer,Object[]> cart) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableItems.COLUMN_SOLD, 1);
        String selection = TableItems._ID+" = 0";
        List<String> selectionList = new ArrayList<>();
        Set<Integer> keySet = cart.keySet();
        for (int idx: keySet) {
            selection += " OR "+TableItems._ID+" = ?";
            selectionList.add(cart.get(idx)[0]+"");
        }
        String[] selectionArgs = selectionList.toArray(new String[0]);
        Log.d(TAG, "buyCart: "+db.update(TableItems.TABLE_NAME, contentValues, selection, selectionArgs));
    }

    public void insertEntryItems(SQLiteDatabase db,
                            int userId,
                            String itemTitle,
                            double itemPrice,
                            String itemDesc,
                            String itemCateg,
                            long dateMS,
                            int isSold) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableItems.COLUMN_USERID, userId);
        contentValues.put(TableItems.COLUMN_TITLE, itemTitle);
        contentValues.put(TableItems.COLUMN_PRICE, itemPrice);
        contentValues.put(TableItems.COLUMN_DESC, itemDesc);
        contentValues.put(TableItems.COLUMN_CATEGORY, itemCateg);
        contentValues.put(TableItems.COLUMN_DATE, dateMS);
        contentValues.put(TableItems.COLUMN_SOLD, isSold);

        Log.d(TAG, "insertEntryItems: "+db.insert(TableItems.TABLE_NAME, null, contentValues));
    }

    public void insertEntryUsers(SQLiteDatabase db, String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableUsers.COLUMN_USERNAME, username);

        Log.d(TAG, "insertEntryUsers: "+db.insert(TableUsers.TABLE_NAME, null, contentValues));
    }

    public void addItemToCart(SQLiteDatabase db, int itemId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCart.COLUMN_ITEMID, itemId);
        Log.d(TAG, "addItemToCart: "+db.insert(TableCart.TABLE_NAME, null, contentValues));
    }

    public LinkedHashMap<Integer, Object[]> getAllItems(SQLiteDatabase db,
                                                        int thisUserId,
                                                        Object category,
                                                        int[] priceRange,
                                                        int sold,
                                                        int timePeriod) {
        String[] projection = {
                TableItems._ID,
                TableItems.COLUMN_USERID,
                TableItems.COLUMN_TITLE,
                TableItems.COLUMN_PRICE,
                TableItems.COLUMN_DESC,
                TableItems.COLUMN_CATEGORY,
                TableItems.COLUMN_DATE,
                TableItems.COLUMN_SOLD
        };

        String selection = TableItems.COLUMN_SOLD+" = ? AND "+TableItems.COLUMN_USERID+" != "+thisUserId;
        List<String> selectionList = new ArrayList<>();
        selectionList.add(sold+"");
        if(category != null) {
            selection += " AND "+TableItems.COLUMN_CATEGORY+" = ?";
            selectionList.add(category+"");
        }
        if(priceRange != null) {
            selection += " AND "+TableItems.COLUMN_PRICE+" > ? AND "+TableItems.COLUMN_PRICE+" < ?";
            selectionList.add(priceRange[0]+"");
            selectionList.add(priceRange[1]+"");
        }
        switch(timePeriod) {
            case 1:
                selection += " AND "+TableItems.COLUMN_DATE+" >= "+(date.getTime()-31556952000L);
                break;
            case 2:
                selection += " AND "+TableItems.COLUMN_DATE+" >= "+(date.getTime()-2629800000L);
                break;
            case 3:
                selection += " AND "+TableItems.COLUMN_DATE+" >= "+(date.getTime()-86400000L);
                break;
        }
        String[] selectionArgs = selectionList.toArray(new String[0]);
        String sortOrder = TableItems.COLUMN_DATE+" DESC";

        // Log.d(TAG, "selection "+selection);
        // Log.d(TAG, "selectionList "+selectionList);

        Cursor cursor = db.query(
                TableItems.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        LinkedHashMap<Integer, Object[]> dataReturn = new LinkedHashMap<>();
        int ii = 0;
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TableItems._ID));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(TableItems.COLUMN_USERID));
            String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(TableItems.COLUMN_TITLE));
            double itemPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(TableItems.COLUMN_PRICE));
            String itemDesc = cursor.getString(cursor.getColumnIndexOrThrow(TableItems.COLUMN_DESC));
            String itemCateg = cursor.getString(cursor.getColumnIndexOrThrow(TableItems.COLUMN_CATEGORY));
            String dateMS = cursor.getString(cursor.getColumnIndexOrThrow(TableItems.COLUMN_DATE));
            String isSold = cursor.getString(cursor.getColumnIndexOrThrow(TableItems.COLUMN_SOLD));
            dataReturn.put(ii, new Object[] {id,userId,itemTitle,itemPrice,itemDesc,itemCateg,dateMS,isSold});
            ii++;
        }
        cursor.close();

        return dataReturn;
    }

    public Object[] getItemById(SQLiteDatabase db, int itemId) {
        String[] projection = {
                TableItems._ID,
                TableItems.COLUMN_USERID,
                TableItems.COLUMN_TITLE,
                TableItems.COLUMN_PRICE,
                TableItems.COLUMN_DESC,
                TableItems.COLUMN_CATEGORY,
                TableItems.COLUMN_DATE,
                TableItems.COLUMN_SOLD
        };

        String selection = TableItems._ID+" = ?";
        String[] selectionArgs = {itemId+""};

        Cursor cursor = db.query(
                TableItems.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(TableItems._ID));
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow(TableItems.COLUMN_USERID));
        String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(TableItems.COLUMN_TITLE));
        double itemPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(TableItems.COLUMN_PRICE));
        String itemDesc = cursor.getString(cursor.getColumnIndexOrThrow(TableItems.COLUMN_DESC));
        String itemCateg = cursor.getString(cursor.getColumnIndexOrThrow(TableItems.COLUMN_CATEGORY));
        String dateMS = cursor.getString(cursor.getColumnIndexOrThrow(TableItems.COLUMN_DATE));
        String isSold = cursor.getString(cursor.getColumnIndexOrThrow(TableItems.COLUMN_SOLD));
        Object[] itemData = new Object[] {id,userId,itemTitle,itemPrice,itemDesc,itemCateg,dateMS,isSold};
        cursor.close();

        return itemData;
    }

    public String getUsernameById(SQLiteDatabase db, int userId) {
        String[] projection = {
                TableItems._ID,
                TableUsers.COLUMN_USERNAME
        };

        String selection = TableItems._ID+" = ?";
        String[] selectionArgs = {userId+""};

        Cursor cursor = db.query(
                TableUsers.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToNext();
        String username = cursor.getString(cursor.getColumnIndexOrThrow(TableUsers.COLUMN_USERNAME));
        cursor.close();

        return username;
    }

    public LinkedHashMap<Integer,Object[]> getCartItems(SQLiteDatabase db) {
        String[] projection = {
                TableCart.COLUMN_ITEMID
        };

        Cursor cursor = db.query(
                TableCart.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Integer> ids = new ArrayList<>();
        LinkedHashMap<Integer, Object[]> dataReturn = new LinkedHashMap<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TableCart.COLUMN_ITEMID));
            ids.add(id);
        }
        cursor.close();

        int ii = 0;
        for (int id: ids) {
            dataReturn.put(ii, getItemById(db, id));
            ii++;
        }

        return dataReturn;
    }
}
