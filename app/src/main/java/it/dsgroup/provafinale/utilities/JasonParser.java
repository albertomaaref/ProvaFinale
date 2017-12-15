package it.dsgroup.provafinale.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;

import it.dsgroup.provafinale.models.Corriere;
import it.dsgroup.provafinale.models.Pacco;

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

    public static ArrayList<Corriere> getCorrieri (String string){

        ArrayList<Corriere> lista = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(string);
            Iterator keys = object.keys();

            while (keys.hasNext()){
                Corriere corriere = new Corriere();
                String key = (String) keys.next();
                corriere.setUserCorriere(key);
                lista.add(corriere);
            }


        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static ArrayList<Pacco> getPacchi(String url){
        ArrayList<Pacco> listPacchi = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(url);
            Iterator keys = object.keys();
            while (keys.hasNext()){
                String key = (String) keys.next();
                JSONObject oggetto =  object.getJSONObject(key);
                Iterator chiavi = oggetto.keys();

                while (chiavi.hasNext()){
                    Pacco pacco = new Pacco();
                    String chiave = (String) chiavi.next();
                    JSONObject objet = oggetto.getJSONObject(chiave);
                    Iterator clefs = objet.keys();
                    while (clefs.hasNext()){
                        String clef = (String) clefs.next();
                        if (clef.toLowerCase().equals("id")){
                            pacco.setIdPacco(objet.getString(clef).toString());
                            listPacchi.add(pacco);
                        }
                    }
                }

            }
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        return listPacchi;
    }
}
