package it.dsgroup.provafinale;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RootActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private PreferenceManager pm;
    private String utenteAttivo;
    private String tipoUtenteAttivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        utenteAttivo = prefs.getString("utenteAttivo","");
        tipoUtenteAttivo = prefs.getString("tipoUtenteAttivo","");

        // controllo se ho un utente già loggato
        if (utenteAttivo.equals("")){
            // vado all'altra activity
        }

        else if (tipoUtenteAttivo.toLowerCase().equals("cliente")){
            // vado alla lista di visualizzazione dei corrieri disonibili
        }

        else if (tipoUtenteAttivo.toLowerCase().equals("corriere")){
            // vao alla lista dei pacchi
        }

        finish();
    }


}
