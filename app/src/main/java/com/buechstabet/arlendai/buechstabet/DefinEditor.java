package com.buechstabet.arlendai.buechstabet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DefinEditor extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences speicher;
    SharedPreferences.Editor editor;

    int selectedItem, list_length;
    String[] beschreibungen;
    private EditText edit_defin;
    Button bt_Edit_Fin,bt_Edit_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defin_editor);

        //init items
        bt_Edit_Back = (Button)findViewById(R.id.definEditor_back);
        bt_Edit_Fin = (Button)findViewById(R.id.definEditor_Finish);
        edit_defin = (EditText)findViewById(R.id.definEditor_editDefinition);

        bt_Edit_Back.setOnClickListener(this);
        bt_Edit_Fin.setOnClickListener(this);

        //informationen zuordnen
        Bundle extras = getIntent().getExtras();

        selectedItem = extras.getInt("BeschPos");
        list_length = extras.getInt("ListLength");

        beschreibungen = new String[list_length];

        speicher = getApplicationContext().getSharedPreferences("Daten", 0);
        editor = speicher.edit();

        //laden der definitions liste
        for(int i= 0;i<list_length;i++){

            if(speicher.getString("definition"+i,null)!= null) {

                beschreibungen[i] = speicher.getString("definition" + i, null);
            }
            else{ Log.e("myTag", "beschreibung an der stelle "+i+" ist null"); }
        }
    }

    @Override
    public void onClick(View view) {
        if(view == bt_Edit_Back){

            Intent intent = new Intent(DefinEditor.this, Discription.class);
            intent.putExtra("BeschPos", selectedItem);
            intent.putExtra("ListLength", list_length);
            startActivity(intent);
        }
        else if(view == bt_Edit_Fin){

            String new_defin = edit_defin.getText().toString();
            beschreibungen[selectedItem] = new_defin;

            Save();

            Intent intent = new Intent(DefinEditor.this, Discription.class);
            intent.putExtra("BeschPos", selectedItem);
            intent.putExtra("ListLength", list_length);
            startActivity(intent);
        }
    }

    private void Save() {
        //einzelnes abspeicher der wörter
        for(int i = 0;i<list_length;i++){
            editor.putString("definition"+i,beschreibungen[i]);
        }
        editor.putInt("length",list_length);
        editor.commit();
    }
}
