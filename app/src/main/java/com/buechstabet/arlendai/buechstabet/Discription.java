package com.buechstabet.arlendai.buechstabet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Discription extends AppCompatActivity {

    SharedPreferences speicher;
    SharedPreferences.Editor editor;

    private Button btBack,btEdit;
    private TextView textView;

    int selectedItem;
    String beschreibungen, art;
    final String methode = "BeschreibungLaden";

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

        textView = (TextView)findViewById(R.id.discription_discription);

        Bundle extras = getIntent().getExtras();
        selectedItem = extras.getInt("BeschPos");

        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(methode,String.valueOf(selectedItem));
        while (beschreibungen==null){
            if(backgroundTask.getBesch_test()){
                beschreibungen = backgroundTask.getBesch();
                art = backgroundTask.getArt();
            }
        }
        textView.setText("Wortart:\n"+art+"\n\n"+"Bedeutung:\n"+beschreibungen);
        textView.setTextColor(Color.BLACK);
    }
}
