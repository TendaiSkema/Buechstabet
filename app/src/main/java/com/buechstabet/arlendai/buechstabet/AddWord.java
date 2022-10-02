package com.buechstabet.arlendai.buechstabet;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AddWord extends AppCompatActivity implements View.OnClickListener {

    private Button best, zuruek;
    private EditText text_input, defin;

    private CheckBox cb_nomen, cb_verb, cb_adjektiv;
    int list_length;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        if(!checkInternet()){
            Toast.makeText(this,"Kein Internet",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        //init items
        best = findViewById(R.id.addWord_add);
        defin = findViewById(R.id.addWord_definition);
        text_input = findViewById(R.id.addWord_word);
        zuruek = findViewById(R.id.addWord_back);
        cb_nomen = findViewById(R.id.addWord_cB_Nomen);
        cb_adjektiv = findViewById(R.id.addWord_cB_Adjektiv);
        cb_verb = findViewById(R.id.addWord_cB_Verb);

        best.setOnClickListener(this);
        zuruek.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        //laden der listen

        String[] besch_list = extras.getStringArray("BeschList");
        String[] art_list = extras.getStringArray("ArtList");
        String[] woerter_list = extras.getStringArray("Wörter");
        list_length = extras.getInt("List");
    }


    @Override
    public void onClick(View v) {
        if(v == best){
            if(text_input.getText().length()>0 && defin.getText().length()>0) {

                final String newWord = text_input.getText().toString();
                final String newDis = defin.getText().toString();
                final String newArt = checkArt();
                final int position = list_length;

                String methode = "Save";
                BackgroundTask back_task = new BackgroundTask(this);
                back_task.execute(methode,newWord,newDis,newArt,String.valueOf(position));

                Intent intent = new Intent(AddWord.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "Wortfeld oder Beschreibung ist leer!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v== zuruek){
            Intent intent = new Intent(AddWord.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public boolean checkInternet(){
        //überprüft Internet verbindung
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnectedOrConnecting();
    }
    private String checkArt() {
        //wortart herausfinden
        String art = "Unbeschreiblich";
        boolean cb_adjektiv_isCheckt = cb_adjektiv.isChecked();
        boolean cb_nomen_isCheckt = cb_nomen.isChecked();
        boolean cb_verb_isCheckt = cb_verb.isChecked();

        if(cb_adjektiv_isCheckt&&cb_nomen_isCheckt&&cb_verb_isCheckt){
            art = "Universal";
        }
        else if(!cb_adjektiv_isCheckt&&!cb_nomen_isCheckt&&cb_verb_isCheckt){
            art = "Verb";
        }
        else if(!cb_adjektiv_isCheckt&&cb_nomen_isCheckt&&cb_verb_isCheckt){
            art = "Noverb";
        }
        else if(cb_adjektiv_isCheckt&&!cb_nomen_isCheckt&&cb_verb_isCheckt){
            art = "Adverb";
        }
        else if(cb_adjektiv_isCheckt&&cb_nomen_isCheckt&&!cb_verb_isCheckt){
            art = "Nomdjektiv";
        }
        else if(cb_adjektiv_isCheckt&&!cb_nomen_isCheckt&&!cb_verb_isCheckt){
            art = "Adjektiv";
        }
        else if(!cb_adjektiv_isCheckt&&cb_nomen_isCheckt&&!cb_verb_isCheckt){
            art = "Nomen";
        }

        return art;
    }
}
