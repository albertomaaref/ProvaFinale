package it.dsgroup.provafinale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import it.dsgroup.provafinale.models.Pacco;
import it.dsgroup.provafinale.services.PushNotification;
import it.dsgroup.provafinale.utilities.FireBaseConnection;
import it.dsgroup.provafinale.utilities.InternalStorage;
import it.dsgroup.provafinale.utilities.JasonParser;
import it.dsgroup.provafinale.utilities.TaskCompletion;

public class GestioneConsegnaActivity extends AppCompatActivity implements TaskCompletion {

    private Button bApplica;
    private TaskCompletion delegation;
    private String idPacco;
    private DatabaseReference ref;
    private FirebaseDatabase database;
    private ProgressDialog pd;
    private Pacco paccoDaconsegnare;
    private Button dettaglioPacco;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_consegna);

        Intent i = getIntent();
        idPacco = i.getStringExtra("idPacco");
        dettaglioPacco = findViewById(R.id.bDettaglio);
        bApplica = findViewById(R.id.bSetInConsegna);
        delegation = this;
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl("https://provafinale-733e5.firebaseio.com/");
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        restCallforPacco(delegation, "users/pacchi/" + idPacco + ".json");

        bApplica.setOnClickListener(temporary4);
        dettaglioPacco.setOnClickListener(temporary5);


    }

    public void restCallforPacco(final TaskCompletion delegation, String url) {
        pd = new ProgressDialog(this);
        pd.show();
        FireBaseConnection.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                if (s.equals("null")) {
                    delegation.taskToDo("error");
                } else {
                    paccoDaconsegnare = JasonParser.getPaccoById(s);
                    delegation.taskToDo("success");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                delegation.taskToDo("error");
            }
        });
    }

    @Override
    public void taskToDo(String string) {
        pd.dismiss();
        pd.cancel();
        if (string.equals("success")) {
            if (paccoDaconsegnare.getStato().equals("consegnato")) {
                bApplica.setEnabled(false);
            }
        }
    }


    View.OnClickListener temporary4 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            paccoDaconsegnare.setStato("in consegna");
            //Intent intentPush = new Intent(GestioneConsegnaActivity.this, PushNotification.class);
            //startService(intentPush);
            setStatoinDB();

        }

    };


    public void setStatoinDB() {
        ref.child("users/pacchi/" + paccoDaconsegnare.getIdPacco()+"/stato").setValue(paccoDaconsegnare.getStato());
        ref.child("users/clienti/" + paccoDaconsegnare.getDestinatario() + "/pacchi/" + paccoDaconsegnare.getIdPacco() + "/id").setValue(paccoDaconsegnare.getIdPacco());
        ref.child("users/clienti/" + paccoDaconsegnare.getDestinatario() + "/pacchi/" + paccoDaconsegnare.getIdPacco() + "/stato").setValue(paccoDaconsegnare.getStato());

        // metto il pacco in local storage per poterlo riprendere finche non ho finito la consegna
        InternalStorage.writeObject(getApplicationContext(), "paccoInConsegna", paccoDaconsegnare);
    }

    View.OnClickListener temporary5 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            anteprimaItinerario();

        }
    };

    private void anteprimaItinerario() {
        Intent i = new Intent(GestioneConsegnaActivity.this, MapsActivity.class);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("adressA", paccoDaconsegnare.getIndDep().toString());
        editor.putString("adressB", paccoDaconsegnare.getIndCons().toString());
        editor.commit();
        startActivity(i);
    }

}
