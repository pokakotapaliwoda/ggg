package com.example.piotrnikadonzaliczeniowy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AuthorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
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
}