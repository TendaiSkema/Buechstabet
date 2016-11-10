package com.buechstabet.arlendai.buechstabet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Items Initialisierung
    private ImageButton suche;
    private EditText suche_text;
    private Button new_word,bSynchro;
    private ListView list;

    //List dinge erstellen
    private String[] wörter;
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

        //check internet
        if(!checkInternet()){
            Toast.makeText(this,"Kein Internet...Bitte einschalten",Toast.LENGTH_LONG).show();
            finish();
        }

        //Layout
        list = (ListView)findViewById(R.id.main_wörter_list);
        list.setOnItemClickListener(this);

        //überprüft fremdaufruf mit bundel
        try {
            Bundle extras = getIntent().getExtras();                    //versucht die extras die es von der aufrufenden Activity zu finden
            String[] ExeptionChecker = extras.getStringArray("List");   //String[] zum überprüfen ob extras = null
            if(ExeptionChecker != null) {
                wörter = extras.getStringArray("List");
            }

        }catch (Exception e){
            //fals nicht

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

    public boolean checkInternet(){
        //überprüft Internet verbindung
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnectedOrConnecting();
    }
    //Laden des wörter[]'s aus dem server
    private void LoadList() {

        String methode = "LoadList";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(methode);
        while (wörter == null){

            //wen der überprüfungs boolean true ist wird die liste hergeholt
            if(backgroundTask.getTest()){
                wörter = backgroundTask.getWörter();
            }
        }
    }
    //erstellung der Liste
    public void ListCreator(){

        if (wörter!=null) {
            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, wörter);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    //Item Listener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        Intent intent = new Intent(MainActivity.this, Discription.class);
        intent.putExtra("BeschPos", position);
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
