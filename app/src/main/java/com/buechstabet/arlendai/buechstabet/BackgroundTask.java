package com.buechstabet.arlendai.buechstabet;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Gresch Marco on 06.11.2016.
 */

public class BackgroundTask extends AsyncTask<String,Void,String> {
    Context ctx;

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
        Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... params) {

        String methode = params[0];
        if(methode.equals("Save")){
            final String url = "http://buechstabet.esy.es/buechstabet/add_word.php";
            String wort = params[1];
            String besch = params[2];
            String art = params[3];

            try {
                URL scriptURL = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) scriptURL.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String daten =  URLEncoder.encode("wort","UTF-8")+" = "+URLEncoder.encode(wort,"UTF-8")+"&"+
                        URLEncoder.encode("besch","UTF-8")+" = "+URLEncoder.encode(besch,"UTF-8")+"&"+
                        URLEncoder.encode("art","UTF-8")+" = "+URLEncoder.encode(art,"UTF-8");

                bufferedWriter.write(daten);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream is = connection.getInputStream();
                is.close();
                return "Speichern Erfolgreich";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
