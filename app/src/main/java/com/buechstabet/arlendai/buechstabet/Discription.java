package com.buechstabet.arlendai.buechstabet;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Discription extends AppCompatActivity {

    private TextView textView,textView2,textView3;

    int selectedItem;
    String beschreibungen, art;
    final String methode = "BeschreibungLaden";
    private View error_include;
    private Button error_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discription);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_discription);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Discription.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        textView = (TextView) findViewById(R.id.discription_discription);
        textView2 = (TextView) findViewById(R.id.textView9);
        error_include = (View) findViewById(R.id.discription_error);
        inCreate();
    }
    public void inCreate(){
        if(!checkInternet()){
            Toast.makeText(this,"Kein Internet",Toast.LENGTH_LONG).show();
            textView2.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            error_include.setVisibility(View.VISIBLE);
            error_button = (Button)findViewById(R.id.error_button);
            error_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inCreate();
                }
            });
        }else {
            textView.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            error_include.setVisibility(View.GONE);

            Bundle extras = getIntent().getExtras();
            selectedItem = extras.getInt("BeschPos");

            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(methode, String.valueOf(selectedItem));
            while (beschreibungen == null) {
                if (backgroundTask.getBesch_test()) {
                    beschreibungen = backgroundTask.getBesch();
                    art = backgroundTask.getArt();
                }
            }
            textView.setText("\n" + beschreibungen);
            textView2.setText(art);
            textView.setTextColor(Color.BLACK);
            textView2.setTextColor(Color.BLACK);
        }
    }
    public boolean checkInternet(){
        //überprüft Internet verbindung
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnectedOrConnecting();
    }
}
