package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import org.apache.commons.io.IOUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE="com.example.myfirstapp.MESSAGE";
    Drawable img = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void getCurrentWeather(View view){

        String endpoint = "current.json";
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String location = editText.getText().toString();

        RequestParams params = new RequestParams();
        params.add("key", "8883827c647647e3bde121720223005");
        params.add("q", location);
        params.add("lang", "en");

        RestClient.get(endpoint, params , new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Handle success
                Log.i("INFO", new String(responseBody));

                try{
                    JSONObject resp = new JSONObject(new String(responseBody));
                    String temp = resp.getJSONObject("current").getString("temp_c");
                    String location = resp.getJSONObject("location").getString("name");
                    String weatherCondition = resp.getJSONObject("current").getJSONObject("condition").getString("text");
                    String weatherConditionImg = resp.getJSONObject("current").getJSONObject("condition").getString("icon").replace("//","");

                    String message = "The weather in " + location + " is: \n" + weatherCondition + " with a temperature of " + temp.replace(".0","") + "°C";
                    //System.out.println("Il meteo a " + location + " é: " + weatherCondition + " con una temperatura di " + temp.replace(".0","") + "°C");
                    String code = resp.getJSONObject("current").getJSONObject("condition").getString("code");

                    String imgName = "";
                    if(resp.getJSONObject("current").getInt("is_day") == 1){
                        imgName += "day";
                    } else {
                        imgName += "night";
                    }

                    try{
                        JSONArray json = new JSONArray(readJSONFromAsset());
                        for(int i = 0; i<json.length(); i++){
                            int jsonCode = json.getJSONObject(i).getInt("code");
                            Log.i("JSON_CODE", Integer.toString(jsonCode));
                            Log.i("RESP_CODE", code);
                            boolean comp = jsonCode == Integer.parseInt(code);
                            Log.i("COMPARE",Boolean.toString(comp));
                            if(jsonCode==Integer.parseInt(code)){
                                imgName += Integer.toString(json.getJSONObject(i).getInt("icon"));
                                Log.i("IMGNAME",imgName);
                                break;
                            }
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    Drawable drawable = null;
                    try {
                        Uri uri = Uri.parse("android.resource://com.example.myfirstapp/drawable/"+imgName);
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        drawable = Drawable.createFromStream(inputStream, uri.toString() );
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    TextView textView = findViewById(R.id.textView2);
                    textView.setText(message);
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageDrawable(drawable);

                }catch(JSONException e){
                    e.printStackTrace();
                }
                /*List<String> list = new ArrayList<>();

                try {
                    JSONArray resp = new JSONArray(new String(responseBody));
                    list = new ArrayList<String>();
                    for (int i=0; i<resp.length(); i++) {
                        JSONObject respObj = (JSONObject) resp.get(i);
                        list.add( respObj.optString("id") + " " + respObj.optString("title"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                //adapter = new RecyclerViewAdapter(getApplicationContext(), list);
                //recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Handle error
                Log.e("ERROR", "An error has occurred");
            }
        });
    }
    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("weather_conditions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

