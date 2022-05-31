package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.protocol.RequestExpectContinue;

public class GamesActivity extends AppCompatActivity {
    private static AsyncHttpClient client = new AsyncHttpClient();
    ArrayList<Match> matches = new ArrayList<>();
    LinearLayout linearLayout;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_games);

        linearLayout = findViewById(R.id.gamesLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        String sportAPIKey = "1a8af91e31023965c499e27ae753c2c1";
        String sportAPIEndpoint = "https://v3.football.api-sports.io/fixtures";
        RequestParams params = new RequestParams();
        params.add("live","all");
        client.addHeader("x-apisports-key", sportAPIKey);
        client.addHeader("x-rapidapi-host", "v3.football.api-sports.io");
        client.get(sportAPIEndpoint, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("INFO", new String(responseBody));
                try{
                    JSONObject json = new JSONObject(new String(responseBody));

                    JSONArray response = json.getJSONArray("response");
                    System.out.print("N games found: " + response.length());
                    for(int i = 0; i<response.length(); i++){
                        try{
                            Match match = new Match(response.getJSONObject(i));
                            TextView textView = new TextView(context);
                            String game = match.toString();
                            System.out.println(game);
                            textView.setText(game);
                            linearLayout.addView(textView);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

//                        String homeTeam = response.getJSONObject(i).getJSONObject("teams").getJSONObject("home").getString("name");
//                        String awayTeam = response.getJSONObject(i).getJSONObject("teams").getJSONObject("away").getString("name");



                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("ERROR", new String(responseBody));
            }
        });

        /*for(int i = 0; i<homeTeams.size(); i++){
            TextView textView = new TextView(this);
            String game = homeTeams.get(i) + " - " + awayTeams.get(i);
            textView.setText("Prova");
            linearLayout.addView(textView);
        }*/
        /*for(int i = 0; i<10; i++){
            TextView textView = new TextView(this);
            textView.setText("Prova");
            linearLayout.addView(textView);

        }*/
    }
    public void callbackGames(ArrayList<String> home, ArrayList<String> away){

    }
}