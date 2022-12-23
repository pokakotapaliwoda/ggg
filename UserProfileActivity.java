package com.example.piotrnikadonzaliczeniowy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    TextView profileId;
    Button cartButton;
    int thisUserId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileId = findViewById(R.id.profile_id);
        profileId.setText("USER ID: "+thisUserId);

        cartButton = findViewById(R.id.profile_to_cart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, CartActivity.class);
                startActivity(intent);
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
}