package it.dsgroup.provafinale;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.dsgroup.provafinale.models.Pacco;
import it.dsgroup.provafinale.services.PushNotification;
import it.dsgroup.provafinale.utilities.HttpGeoDecoding;

public class InsertPaccoActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText idPacco;
    private EditText deposito;
    private EditText destinazione;
    private EditText partenza;
    private EditText data;
    private EditText dimensione;
    private Date dataConsegna;
    private String corriereCommissionato;
    private SimpleDateFormat dateFormat;
    private Button insert;
    private Button controllaAddress;
    private Pacco pacco;
    private SharedPreferences pref;
    private String utenteAttivo;
    private TextView lattitudine;
    private TextView longitudine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pacco);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        utenteAttivo = pref.getString("utenteAttivo","");
        Intent i = getIntent();
        corriereCommissionato = i.getStringExtra("corriereSelezionato");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://provafinale-733e5.firebaseio.com/");

        controllaAddress = findViewById(R.id.bControllaIndirizzo);
        insert = findViewById(R.id.bInsertPacco);
        partenza = findViewById(R.id.ePartenza);
        lattitudine = findViewById(R.id.tLattitudine);
        longitudine = findViewById(R.id.tLongitudine);
        dimensione = findViewById(R.id.eDimensione);
        idPacco = findViewById(R.id.eIdPacco);
        deposito= findViewById(R.id.eDeposito);
        destinazione = findViewById(R.id.eDestinazione);
        data = findViewById(R.id.eDataConsegna);
        insert.setOnClickListener(temporary);
        controllaAddress.setOnClickListener(temporary9);



    }

    View.OnClickListener temporary = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!controllaDati()){
                Toast.makeText(getApplicationContext(),"Si Prega di Controllare i dati inseriti",Toast.LENGTH_SHORT).show();
            }
            else {
                pacco = new Pacco();
                pacco.setDataConsegna(dataConsegna);
                pacco.setIdPacco(idPacco.getText().toString());
                pacco.setDeposito(deposito.getText().toString());
                pacco.setStato("commissionato");
                pacco.setIndCons(destinazione.getText().toString());
                pacco.setDimensione(dimensione.getText().toString());
                pacco.setIndCons(partenza.getText().toString());
                pacco.setDestinatario(utenteAttivo);
                pacco.setCorriere(corriereCommissionato);




                insertPaccoInDB(pacco);

                Intent i = new Intent(InsertPaccoActivity.this,PacchiCommissionatiActivity.class);
                Intent intentPush= new Intent(InsertPaccoActivity.this, PushNotification.class);
                startService(intentPush);
                startActivity(i);


            }

        }
    };

    View.OnClickListener temporary9 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new GetCoordinates().execute(partenza.getText().toString().replace(" ","+"));
            Intent i = new Intent(InsertPaccoActivity.this,MapsActivity.class);
            startActivity(i);
        }
    };

    public boolean controllaDati(){
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dataConsegna = new Date();
        try {
            dataConsegna = dateFormat.parse(data.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        if (idPacco.getText().toString().equals("")||destinazione.getText().toString().equals("")||
                deposito.getText().toString().equals("")|| dimensione.getText().toString().equals("")||partenza.getText().toString().equals("")){
            return false;
        }

        return true;
    }

    private void insertPaccoInDB(Pacco pacco){
        databaseReference.child("users/corrieri/"+corriereCommissionato+"/pacchi/"+pacco.getIdPacco()+"/id").setValue(pacco.getIdPacco());
        databaseReference.child("users/pacchi/"+pacco.getIdPacco()).setValue(pacco);
        databaseReference.child("users/clienti/"+utenteAttivo+"/pacchi/"+pacco.getIdPacco()+"/id").setValue(pacco.getIdPacco());
    }

    public class GetCoordinates extends AsyncTask<String,Void,String>{
        ProgressDialog pd = new ProgressDialog(InsertPaccoActivity.this);
        @Override
        protected void onPreExecute() {
            pd.show();
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try {
                String address = strings[0];
                HttpGeoDecoding httpGeoDecoding = new HttpGeoDecoding();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s",address);
                response = httpGeoDecoding.getGeodecoding(url);
                return response;
            }

            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                pd.dismiss();
                JSONObject jsonObject = new JSONObject(s);

                String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").
                        get("lat").toString();
                String lon = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").
                        get("lng").toString();
                lattitudine.setText(lat);
                longitudine.setText(lon);

            }

            catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
