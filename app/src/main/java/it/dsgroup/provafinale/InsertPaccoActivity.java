package it.dsgroup.provafinale;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.dsgroup.provafinale.models.Pacco;
import it.dsgroup.provafinale.services.PushNotification;

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
    private Pacco pacco;
    private SharedPreferences pref;
    private String utenteAttivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pacco);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        utenteAttivo = pref.getString("utenteAttivo","");
        Intent i = getIntent();
        corriereCommissionato = i.getStringExtra("corriereSelezionato");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://provafinale-5bc57.firebaseio.com/");

        insert = findViewById(R.id.bInsertPacco);
        partenza = findViewById(R.id.ePartenza);
        dimensione = findViewById(R.id.eDimensione);
        idPacco = findViewById(R.id.eIdPacco);
        deposito= findViewById(R.id.eDeposito);
        destinazione = findViewById(R.id.eDestinazione);
        data = findViewById(R.id.eDataConsegna);
        insert.setOnClickListener(temporary);



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


                insertPaccoInDB(pacco);

                Intent i = new Intent(InsertPaccoActivity.this,PacchiCommissionatiActivity.class);
                Intent intentPush= new Intent(InsertPaccoActivity.this, PushNotification.class);
                startService(intentPush);
                startActivity(i);


            }

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
}
