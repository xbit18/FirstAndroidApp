package com.example.myfirstapp;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    public void getCurrentWeather(View view){
        hideKeyboard(this);
        String endpoint = "http://api.weatherapi.com/v1/current.json";
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String location = editText.getText().toString();


        /** Creating query **/
        RequestParams params = new RequestParams();
        params.add("key", "8883827c647647e3bde121720223005");
        params.add("q", location);
        params.add("lang", "en");

        /** Executing query **/
        RestClient.get(endpoint, params , new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Handle success
                Log.i("INFO", new String(responseBody));

                /** Parsing JSON response **/
                try{
                    JSONObject resp = new JSONObject(new String(responseBody));
                    String temp = resp.getJSONObject("current").getString("temp_c");
                    JSONObject location = resp.getJSONObject("location");
                    String city = location.getString("name") + ", " + location.getString("country");
                    String weatherCondition = resp.getJSONObject("current").getJSONObject("condition").getString("text");
                    String message = "The weather in " + city + " is: \n" + weatherCondition + " with a temperature of " + temp.replace(".0","") + "°C";
                    String code = resp.getJSONObject("current").getJSONObject("condition").getString("code");

                    /** Choosing weather image according to weather code **/
                    String imgName = "";
                    if(resp.getJSONObject("current").getInt("is_day") == 1){
                        imgName += "day";
                    } else {
                        imgName += "night";
                    }

                    /** Creating right image filename according to day time and weather code **/
                    JSONArray json = new JSONArray(readJSONFromAsset());
                    for(int i = 0; i<json.length(); i++){
                        int jsonCode = json.getJSONObject(i).getInt("code");
                        if(jsonCode==Integer.parseInt(code)){
                            imgName += Integer.toString(json.getJSONObject(i).getInt("icon"));
                            break;
                        }
                    }

                    /** Setting weather image **/
                    Uri uri = Uri.parse("android.resource://com.example.myfirstapp/drawable/"+imgName);
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageURI(uri);
                    imageView.setScaleX(2);
                    imageView.setScaleY(2);

                    /** Setting weather text **/
                    TextView textView = findViewById(R.id.textView2);
                    textView.setText(message);


                }catch(JSONException e){
                    e.printStackTrace();
                }
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void getGames(View view){
        Intent intent = new Intent(this, GamesActivity.class);
        startActivity(intent);
    }
}

