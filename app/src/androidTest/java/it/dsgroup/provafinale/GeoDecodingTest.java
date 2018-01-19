package it.dsgroup.provafinale;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

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
}
