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

    int selectedItem;
    String beschreibungen, art;
    final String methode = "BeschreibungLaden";

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

        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(methode,String.valueOf(selectedItem));
        while (beschreibungen==null){
            if(backgroundTask.getBesch_test()){
                beschreibungen = backgroundTask.getBesch();
                art = backgroundTask.getArt();
            }
        }

        textView.setText(art+"\n\n"+beschreibungen);
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
            startActivity(intent);
        }
    }
}
