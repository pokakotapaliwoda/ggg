package com.example.piotrnikadonzaliczeniowy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class CartActivity extends AppCompatActivity {
    private final String TAG = "1111";

    ItemAdapter itemAdapter;
    RecyclerView recyclerView;
    DatabaseManager databaseManager;
    SQLiteDatabase dbW;
    SQLiteDatabase dbR;

    TextView textTotal;
    Button buttonReset;
    Button buttonBuy;

    Calendar calendar;
    int thisUserId = 1;
    LinkedHashMap<Integer,Object[]> cart;
    double totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        textTotal = findViewById(R.id.cart_total);
        buttonBuy = findViewById(R.id.cart_buy);
        buttonReset = findViewById(R.id.cart_reset);

        databaseManager = new DatabaseManager(this);
        dbW = databaseManager.getWritableDatabase();
        dbR = databaseManager.getWritableDatabase();
        cart = databaseManager.getCartItems(dbR);

        itemAdapter = new ItemAdapter(CartActivity.this, cart);
        recyclerView = findViewById(R.id.cart_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(itemAdapter);
        updateItems();

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cart.isEmpty()) {
                    Toast.makeText(CartActivity.this, "your cart is empty", Toast.LENGTH_SHORT).show();
                } else {
                    buy();
                    dbW = databaseManager.getWritableDatabase();
                    databaseManager.buyCart(dbW, cart);
                    databaseManager.emptyCart(dbW);
                    updateItems();
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbW = databaseManager.getWritableDatabase();
                databaseManager.emptyCart(dbW);
                cart = new LinkedHashMap<>();
                updateItems();
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

    private void buy() {
        Log.i("Send email", "");
        String[] TO = {"xyz@gmail.com"};
        String[] CC = {"nieolx@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        // send email
        calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTimeInMillis(date.getTime());
        emailIntent.setDataAndType(Uri.parse("mailto:"), "text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "NieOLX");
        String emailText = calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR)+"\n";
        emailText += calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+"\n\n";
        emailText += "Twoj zakup:\n";
        Set<Integer> keySet = cart.keySet();
        for (int key : keySet) {
            String itemTitle = cart.get(key)[2]+"";
            String spaces = new String(new char[24-itemTitle.length()]).replace("\0", ".");
            emailText += itemTitle+" "+spaces+" "+cart.get(key)[3]+" zł\n";
        }
        emailText += "total: "+totalPrice+" zł";
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CartActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "bought all", Toast.LENGTH_SHORT).show();
    }

    public void updateItems() {
        dbR = databaseManager.getReadableDatabase();
        cart = databaseManager.getCartItems(dbR);
        itemAdapter.updateData(cart);
        Set<Integer> keySet = cart.keySet();
        totalPrice = 0;
        for (int key : keySet) {
            totalPrice += (double) cart.get(key)[3];
        }
        textTotal.setText("Total: "+totalPrice+" zł");
        // Log.d(TAG, "updated items with "+dataRead);
    }
}