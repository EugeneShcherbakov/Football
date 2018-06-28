package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        disableSSL();
        try {
            String response = getMatchesToday();
            if(response == null){return;}
            JSONArray jsonArray = new JSONArray(response);
            List<JSONArray> players = new ArrayList<>();
            for(int i = 0; i< jsonArray.length(); i++){
                JSONObject sp = jsonArray.getJSONObject(i);
                Object stat = sp.getJSONObject("home_team_statistics");
                if(stat == "null"){
                    continue;
                }else{
                JSONObject st = new JSONObject(stat);
                String country = ((JSONObject) stat).getString("country");
                players.add(((JSONObject) stat).getJSONArray("starting_eleven"));
                for(JSONArray e: players){
                    for (int j = 0; j < e.length(); j++) {
                        JSONObject singlePlayer = e.getJSONObject(j);
                        System.out.println(singlePlayer.getString("name"));
                    }
                }
                    System.out.println(country);
                    //System.out.println(name);
            }}
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    private static void disableSSL() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getMatchesToday() throws Exception {
        String url = "https://worldcup.sfg.io/matches/today";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
