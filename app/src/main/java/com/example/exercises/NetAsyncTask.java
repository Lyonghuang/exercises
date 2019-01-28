package com.example.exercises;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetAsyncTask {
    private DoFinally doFinally;

    public NetAsyncTask(String urlString, DoFinally doFinally) {
        this.doFinally = doFinally;
        new MyAsyncTask().execute(urlString);
    }

    class MyAsyncTask extends AsyncTask<String, Integer, String> {
        private StringBuilder response = null;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];
            try{
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                response = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            doFinally.doFinally(s);
        }
    }
}
