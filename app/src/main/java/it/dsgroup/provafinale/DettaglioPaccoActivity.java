package it.dsgroup.provafinale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import it.dsgroup.provafinale.models.Pacco;
import it.dsgroup.provafinale.utilities.FireBaseConnection;
import it.dsgroup.provafinale.utilities.JasonParser;
import it.dsgroup.provafinale.utilities.TaskCompletion;

public class DettaglioPaccoActivity extends AppCompatActivity implements TaskCompletion {

    private TextView id;
    private TextView dimensione;
    private TextView dataConsegna;
    private TextView corriere;
    private TextView stato;
    private TaskCompletion delegation;
    private RadioButton radioConsegnato;
    private ProgressDialog pd;
    private Pacco pacco;
    private String tipoUtente;
    private String idPacco;
    private SharedPreferences pref;
    private RadioGroup radioGroup;
    private DatabaseReference ref;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_pacco);

        Intent i = getIntent();
        idPacco = i.getStringExtra("idPacco");
        id = findViewById(R.id.tdId);
        dimensione = findViewById(R.id.tdDimensione);
        radioGroup = findViewById(R.id.rGroup);
        dataConsegna = findViewById(R.id.tdDataConsegna);
        corriere = findViewById(R.id.tdCorriere);
        stato = findViewById(R.id.tdStato);
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl("https://provafinale-733e5.firebaseio.com/");
        radioConsegnato = findViewById(R.id.radioConsegnato);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        tipoUtente = pref.getString("tipoUtenteAttivo","");
        delegation  = this;

        // attivo e disattivo il bottone di consegna
        if (tipoUtente.toLowerCase().equals("clienti")){
            radioConsegnato.setEnabled(true);
        }
        else if (tipoUtente.toLowerCase().equals("corrieri")||pacco.getStato().toLowerCase().equals("consegnato")){
            radioConsegnato.setEnabled(false);
        }

        restCallDettaglioPacco(delegation,"users/pacchi/"+idPacco+".json");
        radioGroup.setOnCheckedChangeListener(temporary8);







    }



    @Override
    public void taskToDo(String string) {
        pd.dismiss();
        pd.cancel();
        if (string.toLowerCase().equals("error")){
            Toast.makeText(getApplicationContext(),"errore nel caricamento del pacco",Toast.LENGTH_SHORT).show();
        }
        else {
            id.setText(idPacco);
            dimensione.setText(pacco.getDimensione());
            stato.setText(pacco.getStato());
            dataConsegna.setText(pacco.getDataConsegna().toString());
            if (pacco.getStato().toLowerCase().equals("consegnato")){
                radioConsegnato.setEnabled(false);}

        }
    }

    public void restCallDettaglioPacco(TaskCompletion delegation, String url){
        pd = new ProgressDialog(this);
        pd.show();
        FireBaseConnection.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                if (s.equals("null")){
                    taskToDo("error");
                }
                else {
                    pacco = JasonParser.getPaccoById(s);
                    taskToDo("success");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                taskToDo("error");
            }
        });
    }


    RadioGroup.OnCheckedChangeListener temporary8 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            ref.child("users/clienti/"+pacco.getDestinatario()+"/pacchi/"+pacco.getIdPacco()+"/stato").setValue("consegnato");
            ref.child("users/corrieri/"+pacco.getCorriere()+"/pacchi/"+pacco.getIdPacco()+"/stato").setValue("consegnato");
            ref.child("users/pacchi/"+pacco.getIdPacco()+"/"+"stato").setValue("consegnato");
        }
    };


}
