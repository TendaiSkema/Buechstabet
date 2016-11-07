package com.buechstabet.arlendai.buechstabet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Items Initialisierung
    private ImageButton suche;
    private EditText suche_text;
    private Button new_word,bSynchro;
    private ListView list;

    //Speicher
    private SharedPreferences speicher;
    private SharedPreferences.Editor editor;

    //List dinge erstellen
    private String[] wörter = {};
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddWord.class);
                intent.putExtra("List",wörter);
                startActivity(intent);
                finish();
            }
        });

        //Layout
        list = (ListView)findViewById(R.id.main_wörter_list);
        list.setOnItemClickListener(this);

        speicher = getApplicationContext().getSharedPreferences("Daten", 0);
        editor = speicher.edit();

        Log.i("myTag", "start Main");
        boolean first_load = true;
        try {
            Bundle extras = getIntent().getExtras();                    //versucht die extras die es von der aufrufenden Activity zu finden
            String[] ExeptionChecker = extras.getStringArray("List");   //
            if(ExeptionChecker != null) {
                wörter = extras.getStringArray("List");
                first_load = false;
            }
            else{}

        }catch (Exception e){Log.e("myTag","wörterliste kreieren übersprungen");}
        if(first_load){
            LoadList();
        }


        //List erstellungsmethode
        ListCreator();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //Laden des wörter[]'s
    private void LoadList() {
        boolean fehler = false;
        editor.clear().apply();
        int length = speicher.getInt("length",404);
        String[] wörter_dummy = new String[length];

        for (int i=0;i<wörter_dummy.length;i++){

            wörter_dummy[i] = speicher.getString("wort"+i,null);

            if(wörter_dummy[i]==null){
                fehler = true;
            }
        }
        if(fehler == false){
            wörter = wörter_dummy;
        }
    }
    //erstellung der Liste
    public void ListCreator(){
        Log.i("myTag", "Begin ListCreator");

        if (wörter!=null) {
            arrayList = new ArrayList<>(Arrays.asList(wörter));
            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, wörter);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else {
            Log.e("myTag", "wörter[] == null");
        }

        Log.i("myTag", "end ListCreator");
    }

    //Item Listener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        Intent intent = new Intent(MainActivity.this, Discription.class);
        intent.putExtra("BeschPos", position);
        intent.putExtra("ListLength", wörter.length);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //to other activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
