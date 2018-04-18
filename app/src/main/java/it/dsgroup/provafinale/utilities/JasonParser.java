package it.dsgroup.provafinale.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.WeakHashMap;

import it.dsgroup.provafinale.models.Corriere;
import it.dsgroup.provafinale.models.Pacco;

/**
 * Created by utente9.academy on 14/12/2017.
 */

public final class JasonParser {

    public static String getPassword(String string) {

        try {
            JSONObject object = new JSONObject(string);
            Iterator keys = object.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (key.toLowerCase().equals("password")) {
                    return object.getString(key);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Corriere> getCorrieri(String string) {

        ArrayList<Corriere> lista = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(string);
            Iterator keys = object.keys();

            while (keys.hasNext()) {
                Corriere corriere = new Corriere();
                String key = (String) keys.next();
                corriere.setUserCorriere(key);
                lista.add(corriere);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static ArrayList<Pacco> getPacchi(String url) {
        ArrayList<Pacco> listPacchi = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(url);
            Iterator keys = object.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                JSONObject oggetto = object.getJSONObject(key);
                Iterator chiavi = oggetto.keys();

                while (chiavi.hasNext()) {
                    Pacco pacco = new Pacco();
                    String chiave = (String) chiavi.next();
                    JSONObject objet = oggetto.getJSONObject(chiave);
                    Iterator clefs = objet.keys();
                    while (clefs.hasNext()) {
                        String clef = (String) clefs.next();
                        if (clef.toLowerCase().equals("idpacco")) {
                            pacco.setIdPacco(objet.getString(clef).toString());
                            listPacchi.add(pacco);
                        }
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listPacchi;
    }

    public static Pacco getPaccoById(String url) {
        Pacco pacco = new Pacco();

        try {
            JSONObject object = new JSONObject(url);
            Iterator keys = object.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (key.toLowerCase().equals("deposito")) pacco.setDeposito(object.getString(key));
                if (key.toLowerCase().equals("destinatario"))
                    pacco.setDestinatario(object.getString(key));
                if (key.toLowerCase().equals("dimensione"))
                    pacco.setDimensione(object.getString(key));
                if (key.toLowerCase().equals("idpacco")) pacco.setIdPacco(object.getString(key));
                if (key.toLowerCase().equals("indcons")) pacco.setIndCons(object.getString(key));
                if (key.toLowerCase().equals("inddep")) pacco.setIndDep(object.getString(key));
                if (key.toLowerCase().equals("stato")) pacco.setStato(object.getString(key));
                if (key.toLowerCase().equals("corriere")) pacco.setCorriere(object.getString(key));
                if (key.toLowerCase().equals("dataconsegna")) {


                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String data = object.getString(key);
                    Date dataConsegna = new Date(data);
                    pacco.setDataConsegna(dataConsegna);
                    /*
                    JSONObject oggetto = object.getJSONObject(key);
                    Iterator chiavi = oggetto.keys();
                    while (chiavi.hasNext()) {
                        String chiave = (String) chiavi.next();
                        if (chiave.toLowerCase().equals("date"))
                            data = oggetto.getString(chiave) + "-";
                        if (chiave.toLowerCase().equals("month"))
                            data = data + oggetto.getString(chiave) + "-";
                        if (chiave.toLowerCase().equals("year"))
                            data = data + oggetto.getString(chiave);
                    }
                    try {
                        dataConsegna = dateFormat.parse(data);
                        pacco.setDataConsegna(dataConsegna);
                    } catch (ParseException e) {
                        e.printStackTrace();

                    }
                    */

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return pacco;

    }

    /*public static String getStatoPaccofromcliente(String url) {
        try {
            JSONObject object = new JSONObject(url);
            Iterator keys = object.keys();
            while (keys.hasNext()){
                String key = (String) keys.next();
                if(key.toLowerCase().equals("pacchi")){
                    JSONObject object1 = new JSONObject(key);
                    Iterator chiavi = object1.keys();
                    while (chiavi.hasNext()){
                        String chiave = (String) chiavi.next();
                        if (chiave.toLowerCase().equals("stato")){
                            return object.getString(chiave);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }*/
}
