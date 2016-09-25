package org.opportunity_hack.finfit.helper;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONException;
import org.opportunity_hack.finfit.activity.SurveyActivity;

public class RestApi {

    static final String USERNAME = "aparna@missionassetfund.org.finfitapp";
    static final String PASSWORD = "Y*HIX%!yhIQAQfkwMI2NsG&lc3Y6NdEDyRtHYsE8TqgL7GvlvGN0rP";
    static final String LOGINURL = "https://login.salesforce.com";
    static final String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
    static final String CLIENTID = "3MVG9szVa2RxsqBY4lI1fjyn.mlhe8Nn1tUfIy6LZ1LCHygk.2Jy8omCUbL60AYjVScGHbkSzNl2Hlq46VmLP";
    static final String CLIENTSECRET = "1758597290486051987";
    private static String REST_ENDPOINT = "/services/data";
    private static String API_VERSION = "/v37.0";
    private static String baseUri;
    private String loginInstanceUrl, loginAccessToken;
    String query;
    public static ArrayList<String> questionName = new ArrayList<>();
    public static ArrayList<String> answerName = new ArrayList<>();

    public void getConnection(String query) {
        new GetConnection().execute(query);
    }

    private class GetConnection extends AsyncTask<String, Void, Object> {

        String JSON_STRING;
        JSONObject jsonObject = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            new GetQuery().execute();
        }

        @Override
        protected String doInBackground(String... params) {

            query = params[0];

            RequestHandler rh = new RequestHandler();
            HashMap<String, String> data = new HashMap<>();

            data.put("client_id", CLIENTID);
            data.put("client_secret", CLIENTSECRET);
            data.put("username", USERNAME);
            data.put("password", PASSWORD);

            JSON_STRING = rh.sendPostRequest(LOGINURL + GRANTSERVICE, data);

            try {
                jsonObject = new JSONObject(JSON_STRING);

                loginAccessToken = jsonObject.getString("access_token");
                loginInstanceUrl = jsonObject.getString("instance_url");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            baseUri = loginInstanceUrl + REST_ENDPOINT + API_VERSION;
            query = baseUri + "/query/?q=" + query;
            //Select+Name,Question__c,Question__r.Min_Answers_to_Select__c,Language__c,Question__r.Max_Answers_to_Select__c,Id+FROM+Language_Question__c+WHERE+Question__r.Include_Question_on_Main_Flow__c=true+ORDER+BY+Question__r.Order__c";

            return null;
        }
    }

    private class GetQuery extends AsyncTask<String, Void, Object> {

        String JSON_STRING, JSON_STRING_ANSWERS;
        JSONObject jsonObject = null, jsonAnswer = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            SurveyActivity.setData();
        }

        @Override
        protected String doInBackground(String... params) {

            RequestHandler rh = new RequestHandler();
            JSON_STRING = rh.sendGetRequest(query, loginAccessToken);

            //Log.i("`*`*`*`*`", JSON_STRING);

            try {
                jsonObject = new JSONObject(JSON_STRING);
                JSONArray result = jsonObject.getJSONArray("records");

                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);
                    String name = jo.getString("Name");

                    String q = baseUri + "/query/?q=Select+Id,Name,Answer__c+FROM+Language_Answer__c+WHERE+Answer__r.Question__c=" + jo.getString("Question__c") + ".Question__c+ORDER+BY+Answer__r.Order__c";
                    JSON_STRING_ANSWERS = rh.sendGetRequest(q, loginAccessToken);

                    Log.i("`*`*`", JSON_STRING_ANSWERS);

                    try {
                        jsonAnswer = new JSONObject(JSON_STRING_ANSWERS);
                        JSONArray r = jsonObject.getJSONArray("records");

                        for (int j = 0; j < r.length(); j++) {
                            JSONObject joo = r.getJSONObject(j);
                            String answername = joo.getString("Name");

                            answerName.add(answername);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    questionName.add(name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
