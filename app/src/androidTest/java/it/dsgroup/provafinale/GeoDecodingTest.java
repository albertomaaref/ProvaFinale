package it.dsgroup.provafinale;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.dsgroup.provafinale.utilities.HttpGeoDecoding;

/**
 * Created by alim on 19-Jan-18.
 */
@RunWith(AndroidJUnit4.class)
public class GeoDecodingTest {
    @Test
    public void geoDecoding(){
        String response;
        try {
            String address = "milano";
            HttpGeoDecoding httpGeoDecoding = new HttpGeoDecoding();
            String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s",address);
            response = httpGeoDecoding.getGeodecoding(url);
            Log.i("URL", url);
            Log.i("response", response);
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void formatDate (){
        String data = "29/07/2018";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Date dataConsegna = new Date();
        try {
            dataConsegna = dateFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();


        }
        Log.i("day",""+data.substring(0,2));
        Log.i("month",data.substring(3,5));
        Log.i("year",data.substring(6,10));

    }
}
