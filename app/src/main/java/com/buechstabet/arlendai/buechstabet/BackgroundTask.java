package com.buechstabet.arlendai.buechstabet;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Gresch Marco on 06.11.2016.
 */

public class BackgroundTask extends AsyncTask<String,Void,String> {

    Context ctx;
    final String get_word_list= "http://buechstabet.esy.es/buechstabet/get_word_list.php";
    final String url = "http://buechstabet.esy.es/buechstabet/add_word.php";
    String jason_string,methode;

    BackgroundTask(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {

        if(methode.equals("Save")) {
            //return statement von doInBackgroung in eimem toast ausgeben
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected String doInBackground(String... params) {

        methode = params[0];

        if(methode.equals("Save")){
            //einspeichern des neuen wortes

            String wort = params[1];
            String besch = params[2];
            String art = params[3];
            String position = params[4];

            try {
                //Verbindung zum php-script herstellen
                URL scriptURL = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) scriptURL.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();

                //Die zu speichernden daten anpassen und "key" zuordnen
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String daten =  URLEncoder.encode("wort","UTF-8")+"="+URLEncoder.encode(wort,"UTF-8")+"&"+
                        URLEncoder.encode("besch","UTF-8")+"="+URLEncoder.encode(besch,"UTF-8")+"&"+
                        URLEncoder.encode("art","UTF-8")+"="+URLEncoder.encode(art,"UTF-8")+"&"+URLEncoder.encode("pos","UTF-8")+"="+URLEncoder.encode(position,"UTF-8");

                bufferedWriter.write(daten);
                bufferedWriter.flush();
                bufferedWriter.close();

                //Antwort de skripts empfangen starten
                InputStream is = connection.getInputStream();
                String aswer = getTextFromPHP(is);
                is.close();
                connection.disconnect();
                return aswer;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(methode.equals("BeschreibungLaden")){
            //Laden der Beschreibung
        }
        else if(methode.equals("LoadList")){
            //Läd die wörter liste
            try {
                //stellt die verbindung zum php script her
                URL scriptURL = new URL(get_word_list);
                HttpURLConnection connection = (HttpURLConnection) scriptURL.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((jason_string = bufferedReader.readLine()) != null){

                stringBuilder.append(jason_string+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                connection.disconnect();
                Log.i("JSON String",jason_string);
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public String getTextFromPHP(InputStream is){
        //empfangen der php antwort
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String aktuelleZeile;

        //zeile für zeile durchlesen und abspeichern
        try {
            while((aktuelleZeile = reader.readLine())!=null){
                stringBuilder.append(aktuelleZeile);
                stringBuilder.append("\n");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString().trim();
    }
}
