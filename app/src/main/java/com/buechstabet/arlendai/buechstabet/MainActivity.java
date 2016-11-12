package com.buechstabet.arlendai.buechstabet;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Items Initialisierung
    private Button error;
    private View error_include;
    private ListView list;
    private TextView textView;

    //List dinge erstellen
    private String[] wörter;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddWord.class);
                intent.putExtra("List",wörter.length);
                startActivity(intent);
                finish();
            }
        });
        textView = (TextView)findViewById(R.id.textView2);
        error_include = (View) findViewById(R.id.Error_fenster);
        beforCheck();

    }
    public void beforCheck(){
        if(!checkInternet()) {
            Toast.makeText(this, "Kein Internet...", Toast.LENGTH_LONG).show();
            textView.setVisibility(View.GONE);
            error_include.setVisibility(View.VISIBLE);
            error = (Button)findViewById(R.id.error_button);
            error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    beforCheck();
                }
            });
        }
        else {
            //List erstellungsmethode
            error_include.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            LoadList();
            ListCreator();
        }
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

            //Layout
            list = (ListView)findViewById(R.id.main_wörter_list);
            list.setOnItemClickListener(this);
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

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //to other activity
            LoadList();
            ListCreator();
            Toast.makeText(this,"Aktualisiert",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
