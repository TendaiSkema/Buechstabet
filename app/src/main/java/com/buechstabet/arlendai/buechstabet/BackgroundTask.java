package com.buechstabet.arlendai.buechstabet;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

//Created by Tendai Rondof on 06.11.2016.


public class BackgroundTask extends AsyncTask<String,Void,String> {

    private Context ctx;
    private final String get_lists= "http://buechstabet.esy.es/buechstabet/load.php";
    private final String url = "http://buechstabet.esy.es/buechstabet/add_word.php";
    private String jason_string,methode;
    private String[] word_array,besch_array,art_array;
    private boolean wörter_test;

    BackgroundTask(Context ctx){
        this.ctx = ctx;
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

        else if(methode.equals("LoadList")){
            //Läd die listen
            try {
                //stellt die verbindung zum php script her
                URL scriptURL = new URL(get_lists);
                HttpURLConnection connection = (HttpURLConnection) scriptURL.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                //liest aus dem bufferedReader jede zeile aus und bildet einen Java string
                while ((jason_string = bufferedReader.readLine()) != null){

                stringBuilder.append(jason_string+"\n");

                }

                bufferedReader.close();
                inputStream.close();
                connection.disconnect();

                //kürzt den String um den überflüssigen zeilen umbruch und speichert im in dem array
                String lists_string = stringBuilder.toString().trim();
                makeJSONtoStringArray(lists_string);
                wörter_test=true;
                return "Wörter geladen";

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

    private void makeJSONtoStringArray(String word_list_string) {

        try {
            //macht der php-jasonstring zu einem java string array
            JSONObject jasonObject = new JSONObject(word_list_string);
            JSONArray jasonArray = jasonObject.getJSONArray("server_response");
            int count = 0;
            word_array = new String[jasonArray.length()];
            besch_array = new String[jasonArray.length()];
            art_array = new String[jasonArray.length()];

            while (count<jasonArray.length()){
                JSONObject jo = jasonArray.getJSONObject(count);
                word_array[count] = jo.getString("wort");
                besch_array[count] = jo.getString("besch");
                art_array[count] = jo.getString("art");
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public String[] getWörter(){
        return word_array;
    }
    public boolean getTest(){
        return wörter_test;
    }

    public String[] getArt() {
        return art_array;
    }

    public String[] getBesch() {
        return besch_array;
    }
}
