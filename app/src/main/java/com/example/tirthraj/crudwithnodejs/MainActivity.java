package com.example.tirthraj.crudwithnodejs;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResult = (TextView) findViewById(R.id.tv_result);

        //make get request

        new GetDataTask().execute("https://nodem3.herokuapp.com/patients/");

    }


        class GetDataTask extends AsyncTask<String, Void, String>{

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog =new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading data...");
                progressDialog.show();


            }

            @Override
            protected String doInBackground(String... params) {

                StringBuilder result= new StringBuilder();
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
                    urlConnection.setReadTimeout(10000);//ms
                    urlConnection.setConnectTimeout(10000);//ms
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Content-Type","application/jason");//set header
                    urlConnection.connect();

                    //Read data from server
                    InputStream inputStream= urlConnection.getInputStream();
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while((line = bufferedReader.readLine())!=null){
                        result.append(line).append("\n");

                    }

                }
                catch (IOException ex){
                    return "Network error";

                }
                return result.toString();
            }


            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                //set data responce to textview
                mResult.setText(result);

                //cancel progress dialog
                if(progressDialog !=null){
                    progressDialog.dismiss();;
                }

            }


          
        }

    }

