package com.example.piotrnikadonzaliczeniowy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import java.util.Date;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "1111";

    DatabaseManager databaseManager;
    SQLiteDatabase dbW;
    SQLiteDatabase dbR;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    SeekBar dateSeekbar;
    SeekBar priceSeekbar;

    int[] priceRange = {0,10000};
    LinkedHashMap<Integer,Object[]> data = new LinkedHashMap<>();
    LinkedHashMap<Integer,Object[]> dataRead = new LinkedHashMap<>();
    public int thisUserId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateSeekbar = findViewById(R.id.seekbar_date);
        dateSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int ii, boolean b) {
                Log.d(TAG, "dateSeekbar "+ii);
                updateItems();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        priceSeekbar = findViewById(R.id.seekbar_price);
        priceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int ii, boolean b) {
                Log.d(TAG, "priceSeekbar "+ii);
                updatePriceRange();
                Log.d(TAG, "priceRange "+priceRange[0]+" - "+priceRange[1]);
                updateItems();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        itemAdapter = new ItemAdapter(MainActivity.this, data);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(itemAdapter);

        databaseManager = new DatabaseManager(this);
        /* Przykładowe dane
        Date date = new Date();
        dbW = databaseManager.getWritableDatabase();
        dbR = databaseManager.getWritableDatabase();
        databaseManager.truncateDatabase(dbW);
        databaseManager.insertEntryUsers(dbW, "ja");
        databaseManager.insertEntryUsers(dbW, "zbyszek");
        databaseManager.insertEntryUsers(dbW, "pokakota paliwoda");
        databaseManager.insertEntryUsers(dbW, "geslerowa magdi");
        databaseManager.insertEntryItems(dbW, 2, "tapczan", 1200, "opis", "meble", date.getTime(), 0);
        databaseManager.insertEntryItems(dbW, 4, "doniczka", 15.99, "opis", "ogrod", date.getTime(), 0);
        databaseManager.insertEntryItems(dbW, 4, "kubek", 12.50, "opis", "dom", date.getTime(), 0);
        databaseManager.insertEntryItems(dbW, 3, "wersalka", 200, "opis", "meble", date.getTime()-10000000, 0);
        databaseManager.insertEntryItems(dbW, 3, "spodnie", 8, "opis", "ubrania", date.getTime()-100000000, 0);
        databaseManager.emptyCart(dbW);
         */
        updateItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if(id == R.id.menu_main) {
            intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            return true;
        } else if(id == R.id.menu_cart) {
            intent = new Intent(this, CartActivity.class);
            this.startActivity(intent);
            return true;
        } else if(id == R.id.menu_profile) {
            intent = new Intent(this, UserProfileActivity.class);
            this.startActivity(intent);
            return true;
        } else if(id == R.id.autor) {
            intent = new Intent(this, AuthorActivity.class);
            this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateItems() {
        dbR = databaseManager.getReadableDatabase();
        dataRead = databaseManager.getAllItems(dbR,thisUserId,null,priceRange,0,dateSeekbar.getProgress());
        itemAdapter.updateData(dataRead);
        Log.d(TAG, "updated items with "+dataRead);
    }

    public void updatePriceRange() {
        switch(priceSeekbar.getProgress()) {
            case 0:
                priceRange[1] = 10;
                break;
            case 1:
                priceRange[1] = 100;
                break;
            case 2:
                priceRange[1] = 1000;
                break;
            case 3:
                priceRange[1] = 10000;
                break;
        }
    }
}