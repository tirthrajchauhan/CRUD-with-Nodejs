package com.example.tirthraj.crudwithnodejs;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

       // new GetDataTask().execute("https://nodem3.herokuapp.com/patients/");
       // new PostDataTask().execute("https://nodem3.herokuapp.com/patients/");
        //new PutDataTask().execute("https://nodem3.herokuapp.com/patients/5c01b41d7f4b2c0016f8da2a");

        new DeleteDataTask().execute("https://nodem3.herokuapp.com/patients/5c01b41d7f4b2c0016f8da2a");
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
            protected String doInBackground(String... params){
                try{
                return  getData(params[0]);}
                catch (IOException ex){
                    return "Network error !";
                }
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

            private  String getData(String urlPath) throws IOException{

                {

                    StringBuilder result= new StringBuilder();
                    BufferedReader bufferedReader= null;

                    try {
                        URL url = new URL(urlPath);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setReadTimeout(10000);//ms
                        urlConnection.setConnectTimeout(10000);//ms
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setRequestProperty("Content-Type", "application/json");//set header
                        urlConnection.connect();

                        //Read data from server
                        InputStream inputStream = urlConnection.getInputStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            result.append(line).append("\n");

                        }

                    }finally {{
                        if(bufferedReader != null){
                            bufferedReader.close();
                        }
                    }


                    }
                    return result.toString();
                }


            }

        }

        class PostDataTask extends  AsyncTask<String, Void, String>{

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog =new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Inserting data...");
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    return postData(params[0]);
                } catch (IOException ex) {
                    return "network Error !";
                } catch (JSONException ex) {
                    return "Data Invalid !";
                }

            }


            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                mResult.setText(result);
                if (progressDialog != null){
                progressDialog.dismiss();

                }
                }

             private String postData(String urlPath) throws IOException , JSONException {

                 StringBuilder result = new StringBuilder();
                 BufferedWriter bufferedWriter = null;
                 BufferedReader bufferedReader = null;


                try {
                    //create data to send to sever

                    JSONObject dataToSend = new JSONObject();
                    dataToSend.put("first_name", "Andrew");
                    dataToSend.put("last_name", "james");
                    dataToSend.put("dob", "010289");
                    dataToSend.put("address", "Toronto");
                    dataToSend.put("department", "psychology");
                    dataToSend.put("doctor", "DR.D");

                    //Initialize and config request then connect server
                    URL url = new URL(urlPath);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setReadTimeout(10000);//ms
                    urlConnection.setConnectTimeout(10000);//ms
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true); // enable output (body data)
                    urlConnection.setRequestProperty("Content-Type", "application/json");//set header
                    urlConnection.connect();

                    // Write data into server.
                    OutputStream outputStream = urlConnection.getOutputStream();
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                    bufferedWriter.write(dataToSend.toString());
                    bufferedWriter.flush();

                    //read data response from server

                    InputStream inputStream = urlConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {

                        result.append(line).append("\n");
                    }
                }finally {
                  if (bufferedReader != null){
                      bufferedReader.close();
                  }
                  if(bufferedWriter!=null){
                      bufferedWriter.close();
                  }
                }

                return result.toString();
             }
        }

        class  PutDataTask  extends  AsyncTask<String, Void, String>{

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Updating data...");
                progressDialog.show();

            }

            @Override
            protected String doInBackground(String... params) {
             try{
                 return  putData(params[0]);
             }catch (IOException ex){
                 return  "network error";
             }
             catch (JSONException ex){
                 return "data invalid";
             }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);


                mResult.setText(result);
                if(progressDialog != null){
                    progressDialog.dismiss();
                }

            }

            private String putData(String urlPath) throws  IOException,JSONException{

                BufferedWriter bufferedWriter=null;
                String result= null;

               try {
                   //Create Data to update

                   JSONObject dataToSend = new JSONObject();
                   dataToSend.put("first_name", "Andrew-Updated");
                   dataToSend.put("last_name", "james-Updated");
                   dataToSend.put("dob", "111111");
                   dataToSend.put("address", "Toronto-up");
                   dataToSend.put("department", "psychology-up");
                   dataToSend.put("doctor", "DR.D-up");

                   //initialize and config request, then connect to server

                   URL url = new URL(urlPath);
                   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                   urlConnection.setReadTimeout(10000);//ms
                   urlConnection.setConnectTimeout(10000);//ms
                   urlConnection.setRequestMethod("PUT");
                   urlConnection.setDoOutput(true); // enable output (body data)
                   urlConnection.setRequestProperty("Content-Type", "application/json");//set header
                   urlConnection.connect();


                   // Write data into server.
                   OutputStream outputStream = urlConnection.getOutputStream();
                   bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                   bufferedWriter.write(dataToSend.toString());
                   bufferedWriter.flush();

                   //Check update successful or not

                   if (urlConnection.getResponseCode() == 200) {
                       return "update succesfully";
                   } else {
                       return "update failed";
                   }
               }
               finally {
                   if(bufferedWriter != null){
                       bufferedWriter.close();
                   }
               }


            }

        }

        class DeleteDataTask extends  AsyncTask<String,Void,String>{

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Deleting data...");
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                try{
                return deleteData(params[0]);}
                catch (IOException ex){
                    return "Network error";
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                mResult.setText(result);

                if(progressDialog !=null){
                    progressDialog.dismiss();
                }
            }

            private  String deleteData(String urlPath) throws IOException{

                String result = null;

                //initialize and config request, than connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);//ms
                urlConnection.setConnectTimeout(10000);//ms
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setRequestProperty("Content-Type", "application/json");//set header
                urlConnection.connect();

                //check delete successful or not
                if(urlConnection.getResponseCode() == 204){
                    result = "Delete Successfully !";
                }else {
                    result = "Delete failed !";
                }

                return result;
            }

        }

    }

