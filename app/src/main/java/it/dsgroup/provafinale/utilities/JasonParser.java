package it.dsgroup.provafinale.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by utente9.academy on 14/12/2017.
 */

public final class JasonParser {

    public static String getPassword(String string)  {

        try {
            JSONObject object = new JSONObject(string);
            Iterator keys = object.keys();
            while (keys.hasNext()){
                String key = (String) keys.next();
                if (key.toLowerCase().equals("password")){
                    return object.getString(key);
                }
            }
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
