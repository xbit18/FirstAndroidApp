package com.example.myfirstapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Match {
    private String homeTeam;
    private String awayTeam;
    private String date;
    private int id;
    private String referee;
    private String timezone;
    private long firstPeriod;
    private long secondPeriod;
    private int venueId;
    private String venueName;
    private String venueCity;
    private String statusLong;
    private String statusShort;
    private int statusElapsed;
    private int leagueId;
    private String leagueName;
    private int homeId;
    private int awayId;
    private boolean homeWinner;
    private boolean awayWinner;
    private int homeGoals;
    private int awayGoals;
    private SimpleDateFormat oldformat;
    private SimpleDateFormat newformat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public long getFirstPeriod() {
        return firstPeriod;
    }

    public void setFirstPeriod(long firstPeriod) {
        this.firstPeriod = firstPeriod;
    }

    public long getSecondPeriod() {
        return secondPeriod;
    }

    public void setSecondPeriod(long secondPeriod) {
        this.secondPeriod = secondPeriod;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueCity() {
        return venueCity;
    }

    public void setVenueCity(String venueCity) {
        this.venueCity = venueCity;
    }

    public String getStatusLong() {
        return statusLong;
    }

    public void setStatusLong(String statusLong) {
        this.statusLong = statusLong;
    }

    public String getStatusShort() {
        return statusShort;
    }

    public void setStatusShort(String statusShort) {
        this.statusShort = statusShort;
    }

    public int getStatusElapsed() {
        return statusElapsed;
    }

    public void setStatusElapsed(int statusElapsed) {
        this.statusElapsed = statusElapsed;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public int getHomeId() {
        return homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

    public int getAwayId() {
        return awayId;
    }

    public void setAwayId(int awayId) {
        this.awayId = awayId;
    }

    public boolean isHomeWinner() {
        return homeWinner;
    }

    public void setHomeWinner(boolean homeWinner) {
        this.homeWinner = homeWinner;
    }

    public boolean isAwayWinner() {
        return awayWinner;
    }

    public void setAwayWinner(boolean awayWinner) {
        this.awayWinner = awayWinner;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }
    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHomeTeam(){
        return this.homeTeam;
    }

    public String getAwayTeam(){
        return this.awayTeam;
    }

    public String getDate(){
        return this.date;
    }

    public Match(JSONObject json) throws JSONException, ParseException {
        /* getting team names */
        homeTeam = json.getJSONObject("teams").getJSONObject("home").getString("name");
        awayTeam = json.getJSONObject("teams").getJSONObject("away").getString("name");

        /* getting match date */
        String toFormat = json.getJSONObject("fixture").getString("date");
        oldformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ITALY);
        newformat = new SimpleDateFormat("d MMM yyyy 'at' HH:mm", Locale.ITALY);
        Date d = oldformat.parse(toFormat);
        date = newformat.format(d);
        //System.out.println(date);

        id = json.getJSONObject("fixture").getInt("id");
        referee = json.getJSONObject("fixture").getString("referee");
        timezone = json.getJSONObject("fixture").getString("timezone");
        firstPeriod = json.getJSONObject("fixture").getJSONObject("periods").getLong("first");
        secondPeriod = json.getJSONObject("fixture").getJSONObject("periods").getLong("second");
        venueId = json.getJSONObject("fixture").getJSONObject("venue").getInt("id");
        venueName = json.getJSONObject("fixture").getJSONObject("venue").getString("name");
        venueCity = json.getJSONObject("fixture").getJSONObject("venue").getString("city");
        statusLong = json.getJSONObject("fixture").getJSONObject("status").getString("long");
        statusShort = json.getJSONObject("fixture").getJSONObject("status").getString("short");
        statusElapsed = json.getJSONObject("fixture").getJSONObject("status").getInt("elapsed");
        leagueId = json.getJSONObject("fixture").getJSONObject("league").getInt("id");
        leagueName = json.getJSONObject("fixture").getJSONObject("league").getString("name");
        homeGoals = json.getJSONObject("fixture").getJSONObject("goals").getInt("home");
        awayGoals = json.getJSONObject("fixture").getJSONObject("goals").getInt("away");
    }

    @Override
    public String toString(){
        String match = homeTeam + " " + homeGoals + " - " + awayGoals + " " + awayTeam +
                ", " + leagueName + ", " + date + ", " + venueName + ", " + venueCity;
        return match;
    }
}
