package it.dsgroup.provafinale;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

public class GestioneConsegnaActivity extends AppCompatActivity {

    private Spinner spinnerStato;
    private Button bApplica;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_consegna);

        spinnerStato = findViewById(R.id.spinnerGestione);
        bApplica = findViewById(R.id.bApplica);




    }
}
