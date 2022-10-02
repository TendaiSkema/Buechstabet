package com.buechstabet.arlendai.buechstabet;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Discription extends AppCompatActivity {

    private TextView textView,textView2;

    int selectedItem;
    String beschreibungen, art;
    String[] besch_list, art_list,wörter_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discription);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_discription);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Discription.this, MainActivity.class);
                intent.putExtra("Wörter",wörter_list);
                intent.putExtra("BeschList",besch_list);
                intent.putExtra("ArtList",art_list);
                startActivity(intent);
                finish();
            }
        });
        textView = (TextView) findViewById(R.id.discription_discription);
        textView2 = (TextView) findViewById(R.id.textView9);

        Bundle extras = getIntent().getExtras();
        selectedItem = extras.getInt("BeschPos");
        besch_list = extras.getStringArray("BeschList");
        art_list = extras.getStringArray("ArtList");
        wörter_list = extras.getStringArray("Wörter");

        beschreibungen = besch_list[selectedItem];
        art = art_list[selectedItem];

        textView.setText("\n" + beschreibungen);
        textView2.setText(art);
        textView.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
    }
}
