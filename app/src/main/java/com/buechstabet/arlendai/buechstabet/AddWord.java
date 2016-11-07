package com.buechstabet.arlendai.buechstabet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AddWord extends AppCompatActivity implements View.OnClickListener {

    private Button best,zurück;
    private EditText text_input, defin;

    private CheckBox cb_nomen, cb_verb, cb_adjektiv;

    private SharedPreferences speicher;
    private SharedPreferences.Editor editor;
    private String[] wörter_list, definition_list,art_list;


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

        best = (Button)findViewById(R.id.addWord_add);
        defin = (EditText)findViewById(R.id.addWord_definition);
        text_input = (EditText)findViewById(R.id.addWord_word);
        zurück =(Button)findViewById(R.id.addWord_back);
        cb_nomen = (CheckBox)findViewById(R.id.addWord_cB_Nomen);
        cb_adjektiv = (CheckBox)findViewById(R.id.addWord_cB_Adjektiv);
        cb_verb = (CheckBox)findViewById(R.id.addWord_cB_Verb);

        best.setOnClickListener(this);
        zurück.setOnClickListener(this);

        speicher = getApplicationContext().getSharedPreferences("Daten", 0);
        editor = speicher.edit();

        Bundle extras = getIntent().getExtras();

        wörter_list = new String[extras.getStringArray("List").length+1];
        String[] wörter_dummy = extras.getStringArray("List");

        for (int i =0;i<wörter_dummy.length;i++){
            wörter_list[i]= wörter_dummy[i];
        }

        definition_list = new String[wörter_list.length];

        for (int i=0;i<definition_list.length;i++){
            definition_list[i] = speicher.getString("definition"+i,null);
            if(definition_list[i]==null){
                Log.e("myTag","wörter_dummy["+i+"] == null");
            }
        }
        art_list = new String[wörter_list.length];

        for (int i=0;i<art_list.length;i++){
            art_list[i] = speicher.getString("art"+i,null);
            if(art_list[i]==null){
                Log.e("myTag","wörter_dummy["+i+"] == null");
            }
        }
    }


    @Override
    public void onClick(View v) {
        if(v == best){
            if(text_input.getText().length()>0 && defin.getText().length()>0) {

                final String newWord = text_input.getText().toString();
                final String newDis = defin.getText().toString();
                final String newArt = checkArt();

                final int position = wörter_list.length-1;
                wörter_list[position] = newWord;
                definition_list[position] = newDis;
                art_list[position]=newArt;

                String methode = "Save";
                BackgroundTask back_task = new BackgroundTask(this);
                back_task.execute(methode,newWord,newDis,newArt,String.valueOf(position));

                Intent intent = new Intent(AddWord.this, MainActivity.class);
                intent.putExtra("List",wörter_list);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "Wort oder Beschreibung ist leer!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v==zurück){
            Intent intent = new Intent(AddWord.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public boolean checkInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnectedOrConnecting();
    }
    public void Save(String wort,String besch,String art,int pos){
        //einzelnes abspeicher der wörter
        for(int i = 0;i<wörter_list.length;i++){
            editor.putString("wort"+i,wörter_list[i]);
            editor.putString("definition"+i,definition_list[i]);
            editor.putString("art"+i,art_list[i]);
        }
        editor.putInt("length",wörter_list.length);
        editor.commit();
    }
    private String checkArt() {
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
