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
    private String hintString;
    private DoFinally doFinally;
    ProgressDialog progressDialog;

    public NetAsyncTask(String urlString, String hintString, DoFinally doFinally) {
        this.hintString = hintString;
        this.doFinally = doFinally;
        new MyAsyncTask().execute(urlString);
    }

    class MyAsyncTask extends AsyncTask<String, Integer, String> {
        private StringBuilder response = null;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MyApplication.getContext());
            progressDialog.setTitle("@string/progress_title");
            progressDialog.setMessage(hintString);
            progressDialog.setCancelable(false);
            progressDialog.show();
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
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.cancel();
            doFinally.doFinally(s);
        }
    }
}
