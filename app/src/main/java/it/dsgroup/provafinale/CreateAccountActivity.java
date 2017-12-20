package it.dsgroup.provafinale;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.dsgroup.provafinale.models.Cliente;
import it.dsgroup.provafinale.models.Corriere;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private Spinner spinner;
    private Button vai;
    private DatabaseReference ref;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        user = findViewById(R.id.eCreateUsername);
        password = findViewById(R.id.eCreatePassword);
        spinner = findViewById(R.id.spinnerRegistrati);
        vai = findViewById(R.id.bVai);
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl("https://provafinale-5bc57.firebaseio.com/");

        vai.setOnClickListener(temporary7);



    }

    public boolean controllaDatiRegistrazione (){
        if (user.getText().toString().equals("")||password.getText().toString().equals("")){
            return false;
        }
        else return true;
    }

    View.OnClickListener temporary7 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!controllaDatiRegistrazione()){
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }
            else {
                insertUser();

            }
        }
    };

    public void insertUser(){
        if (spinner.getSelectedItem().toString().toLowerCase().equals("clienti")){
            Cliente cliente = new Cliente();
            cliente.setUserCliente(user.getText().toString());
            cliente.setPassCliente(password.getText().toString());
            ref.child("users/clienti/"+cliente.getUserCliente()+"/password").setValue(cliente.getPassCliente());
            Intent i = new Intent(CreateAccountActivity.this,ListaCorrieriActivity.class);
            startActivity(i);

        }
        else {
            Corriere corriere = new Corriere();
            corriere.setUserCorriere(user.getText().toString());
            corriere.setPassCorriere(password.getText().toString());
            ref.child("users/corrieri/"+corriere.getUserCorriere()+"/password").setValue(corriere.getPassCorriere());
            Intent i = new Intent(CreateAccountActivity.this,LoginActivity.class);
            startActivity(i);
        }
    }
}
