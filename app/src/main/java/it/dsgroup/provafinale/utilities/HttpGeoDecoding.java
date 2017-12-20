package it.dsgroup.provafinale.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by utente9.academy on 20/12/2017.
 */

public class HttpGeoDecoding {
    public HttpGeoDecoding() {
    }

    public String getGeodecoding(String requestUrl){
        URL url;
        String response="";
        try {
            url = new URL(requestUrl);
            HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type","applicatin/x-www-form-urlencoded");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                String line ="";
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null){
                    response+=line;
                }


            }
            else response="";
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return response;


    }
}
