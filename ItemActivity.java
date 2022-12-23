package com.example.piotrnikadonzaliczeniowy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.LinkedHashMap;

public class ItemActivity extends AppCompatActivity {
    private final String TAG = "1111";

    Calendar calendar;
    DatabaseManager databaseManager;
    SQLiteDatabase dbW;
    SQLiteDatabase dbR;

    ImageView itemImage;
    TextView textItemTitle;
    TextView textItemPrice;
    TextView textItemUserName;
    TextView textItemCategory;
    TextView textItemPostDate;
    TextView textItemDescription;
    Button buttonAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        databaseManager = new DatabaseManager(this);
        dbW = databaseManager.getWritableDatabase();
        dbR = databaseManager.getWritableDatabase();

        Intent intent = getIntent();
        int itemId = Integer.parseInt(intent.getStringExtra("itemId"));
        int userId = Integer.parseInt(intent.getStringExtra("userId"));
        String itemTitle = intent.getStringExtra("title");
        String itemPrice = intent.getStringExtra("price");
        String itemDesc = intent.getStringExtra("description");
        String itemCateg = intent.getStringExtra("category");
        long dateMS = Long.parseLong(intent.getStringExtra("date"));

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMS);

        textItemTitle = findViewById(R.id.item_title);
        textItemTitle.setText(itemTitle);
        textItemPrice = findViewById(R.id.item_price);
        textItemPrice.setText(itemPrice+" zł");
        textItemCategory = findViewById(R.id.item_category);
        textItemCategory.setText("> "+itemCateg);
        textItemDescription = findViewById(R.id.item_desc);
        textItemDescription.setText(itemDesc);
        textItemDescription = findViewById(R.id.item_desc);
        textItemDescription.setText(itemDesc);
        textItemUserName = findViewById(R.id.item_user_name);
        String username = databaseManager.getUsernameById(dbR, userId);
        textItemUserName.setText("od: "+username);
        textItemPostDate = findViewById(R.id.item_post_date);
        String postDate = calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR);
        textItemPostDate.setText(postDate);

        itemImage = findViewById(R.id.item_full_image);
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ItemActivity.this, itemTitle, Toast.LENGTH_SHORT).show();
            }
        });

        buttonAddToCart = findViewById(R.id.button_add_cart);
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(itemId);
            }
        });
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

    private void addToCart(int itemId) {
        dbW = databaseManager.getWritableDatabase();
        databaseManager.addItemToCart(dbW, itemId);
        Toast.makeText(ItemActivity.this, "item added to cart!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}