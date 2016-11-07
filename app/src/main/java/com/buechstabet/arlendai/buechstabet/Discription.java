package com.buechstabet.arlendai.buechstabet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Discription extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences speicher;
    SharedPreferences.Editor editor;

    private Button btBack,btEdit;
    private TextView textView;

    int selectedItem, list_length;
    String[] beschreibungen, art;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discription);

        btBack = (Button)findViewById(R.id.discription_Back);
        btEdit = (Button)findViewById(R.id.discription_edit);
        textView = (TextView)findViewById(R.id.discription_discription);

        btBack.setOnClickListener(this);
        btEdit.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();

        selectedItem = extras.getInt("BeschPos");
        list_length = extras.getInt("ListLength");

        beschreibungen = new String[list_length];
        art = new String[list_length];

        speicher = getApplicationContext().getSharedPreferences("Daten", 0);
        editor = speicher.edit();

        for(int i= 0;i<list_length;i++){

            if(speicher.getString("definition"+i,null)!= null) {

                beschreibungen[i] = speicher.getString("definition" + i, null);
            }
            else{ Log.e("myTag", "beschreibung an der stelle "+i+" ist null"); }

            if(speicher.getString("art"+i,null)!= null) {

                art[i]= speicher.getString("art"+i,null);
            }
            else{ Log.e("myTag", "art an der stelle "+i+" ist null"); }
        }

        textView.setText(art[selectedItem]+"\n\n"+beschreibungen[selectedItem]);
    }

    @Override
    public void onClick(View v) {
        if(v == btBack){
            Intent intent = new Intent(Discription.this, MainActivity.class);
            startActivity(intent);
        }
        else if(v==btEdit){
            Intent intent = new Intent(Discription.this,DefinEditor.class);
            intent.putExtra("BeschPos", selectedItem);
            intent.putExtra("ListLength", list_length);
            startActivity(intent);
        }
    }
}
