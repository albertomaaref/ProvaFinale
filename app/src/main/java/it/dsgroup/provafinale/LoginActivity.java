package it.dsgroup.provafinale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import it.dsgroup.provafinale.utilities.FireBaseConnection;
import it.dsgroup.provafinale.utilities.JasonParser;
import it.dsgroup.provafinale.utilities.TaskCompletion;

public class LoginActivity extends AppCompatActivity implements TaskCompletion{

    private Spinner spinnerLogin;
    private EditText userLogin;
    private EditText passLogin;
    private Button bRegistration;
    private ImageButton bLogin;
    private TaskCompletion delegation;
    private ProgressDialog pd;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spinnerLogin = findViewById(R.id.sLogin);
        userLogin = findViewById(R.id.eLoginUsername);
        passLogin = findViewById(R.id.eLoginPassword);
        bRegistration = findViewById(R.id.bRegistration);
        bLogin = findViewById(R.id.bLogin);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        delegation = this;

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!controlData(userLogin) || !controlData(passLogin)){
                    Toast.makeText(getApplicationContext(),"Contorllare i dati inseriti",Toast.LENGTH_SHORT).show();
                }

                else {
                    String url = "users/"+spinnerLogin.getSelectedItem().toString().toLowerCase()+"/"+userLogin.getText().toString().toLowerCase()+".json";
                    restCallLogin(delegation,url);

                }
            }
        });


    }


    public void restCallLogin (final TaskCompletion delegato, String url){
        pd = new ProgressDialog(LoginActivity.this);
        pd.show();
        FireBaseConnection.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String s = new String(responseBody);
                if (s.equals("null")){
                    taskToDo("error");
                }
                else {
                    String password = JasonParser.getPassword(s);
                    taskToDo(password);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                taskToDo("error");
            }
        });
    }

    @Override
    public void taskToDo(String string) {
        pd.dismiss();
        pd.cancel();


        if (string.equals("error")){  // nel caso l'utente non esiste
            Toast.makeText(getApplicationContext(),"utente inesistente",Toast.LENGTH_SHORT).show();
        }

        else {
            if (string.equals(passLogin.getText().toString())){// ne caso corrisponda la password
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("utenteAttivo",userLogin.getText().toString());
                editor.putString("tipoUtenteAttivo",spinnerLogin.getSelectedItem().toString());
                editor.commit();
                Toast.makeText(getApplicationContext(),"LOGIN EFFETTUATO",Toast.LENGTH_SHORT).show();


            }
            else {
                Toast.makeText(getApplicationContext(),"PASSWORD ERRATA",Toast.LENGTH_SHORT).show();
            }
        }

    }

    // controllo se gli editText sono pieni
    private boolean controlData(EditText editText){
        if (editText.getText().toString().equals("")){
            return false;
        }
        return true;
    }


}
