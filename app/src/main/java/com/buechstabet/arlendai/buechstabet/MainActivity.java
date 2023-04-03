package com.buechstabet.arlendai.buechstabet;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private View error_include;
    private TextView textView;

    //List dinge erstellen
    private String[] woerter,besch,art;

    //dbManager
    private FirebaseFirestore db;
    private DbManager dbManager = new DbManager();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddWord.class);
            intent.putExtra("List", this.woerter.length);
            intent.putExtra("Wörter", this.woerter);
            intent.putExtra("BeschList",this.besch);
            intent.putExtra("ArtList",this.art);
            startActivity(intent);
            finish();
        });

        textView = findViewById(R.id.textView2);
        error_include = findViewById(R.id.Error_fenster);

        try{

            Bundle extras = getIntent().getExtras();
            besch = extras.getStringArray("BeschList");
            art = extras.getStringArray("ArtList");
            woerter = extras.getStringArray("Wörter");
            ListCreator();

        }catch (Exception e) {

            Log.e("Teg","Bundle == null");
            beforCheck();
        }

    }

    public void beforCheck(){
        if(!checkInternet()) {
            Toast.makeText(this, "Kein Internet...", Toast.LENGTH_LONG).show();
            textView.setVisibility(View.GONE);
            error_include.setVisibility(View.VISIBLE);
            //Items Initialisierung
            Button error = findViewById(R.id.error_button);
            error.setOnClickListener(view -> beforCheck());
        }
        else {
            //List erstellungsmethode
            error_include.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            LoadList();
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
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    // set woerter
    private void setWoerter(ArrayList<WordObj> wordObjList){
        this.woerter = new String[wordObjList.size()];
        this.art = new String[wordObjList.size()];
        this.besch = new String[wordObjList.size()];

        for (int i=0;i<wordObjList.size();i++) {
            WordObj word = wordObjList.get(i);
            Log.d(TAG, "LoadList/ adding word: "+word.word);
            this.woerter[i] = word.word;
            this.art[i] = word.type;
            this.besch[i] = word.dicription;
        }
    }

    // wenn die wörter geladen wurden
    private void onLoaded(@NonNull Task<QuerySnapshot> task){
        ArrayList<WordObj> wordList = new ArrayList<>();
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                WordObj doc = WordObj.WordFromQueryDocumentSnapshot(document);
                Log.d(TAG, "Task Successful: "+document.getId() + " => " + document.getData());
                wordList.add(doc);
            }
            setWoerter(wordList);
            Log.d(TAG, "Word Array:"+ Arrays.toString(woerter));
            ListCreator();
        } else {
            Log.w(TAG, "Error getting documents.", task.getException());
        }
    }

    //Laden des wörter[]'s aus dem server
    private void LoadList() {
        dbManager.readData("CH", this::onLoaded);
        //wen der überprüfungs boolean true ist wird die liste hergeholt
        Log.d(TAG, "LoadList: Read DB data");
    }

    //erstellung der Liste
    public void ListCreator(){

        if (this.woerter !=null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, this.woerter);

            //Layout
            ListView list = findViewById(R.id.main_wörter_list);
            list.setOnItemClickListener(this);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    //Item Listener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        Intent intent = new Intent(MainActivity.this, Discription.class);
        intent.putExtra("BeschPos", position);
        intent.putExtra("BeschList",besch);
        intent.putExtra("ArtList",art);
        intent.putExtra("Wörter", woerter);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //to other activity
            LoadList();
            Toast.makeText(this,"Aktualisiert",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
