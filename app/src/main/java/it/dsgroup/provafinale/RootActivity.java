package it.dsgroup.provafinale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class RootActivity extends AppCompatActivity {

    private SharedPreferences prefs;

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
            Intent i = new Intent(RootActivity.this, LoginActivity.class);
            startActivity(i);
        }

        else if (tipoUtenteAttivo.toLowerCase().equals("clienti")){
            // vado alla lista di visualizzazione dei corrieri disonibili
            Toast.makeText(getApplicationContext(),"vado dalla lista dei corrieri",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,ListaCorrieriActivity.class);
            startActivity(i);

        }

        else if (tipoUtenteAttivo.toLowerCase().equals("corrieri")){
            // vao alla lista dei pacchi
            Toast.makeText(getApplicationContext(),"vado dalla lista dei pacchi",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,ListaPacchiActivity.class);
            startActivity(i);

        }

        finish();
    }


}
