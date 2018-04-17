package it.dsgroup.provafinale;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;

import cz.msebera.android.httpclient.Header;
import it.dsgroup.provafinale.adapters.PaccoAdapter;
import it.dsgroup.provafinale.models.Pacco;
import it.dsgroup.provafinale.models.Session;
import it.dsgroup.provafinale.utilities.FireBaseConnection;
import it.dsgroup.provafinale.utilities.InternalStorage;
import it.dsgroup.provafinale.utilities.JasonParser;
import it.dsgroup.provafinale.utilities.TaskCompletion;

public class ListaPacchiActivity extends AppCompatActivity implements TaskCompletion{

    private RecyclerView recyclerPacco;
    private LinearLayoutManager lm;
    private ProgressDialog pd;
    private TaskCompletion delegation;
    private ArrayList<Pacco> listaPacchi;
    private PaccoAdapter paccoAdapter;
    private SharedPreferences prefs;
    private String utenteAttivo;
    private String tipoUtente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pacchi);

        recyclerPacco = findViewById(R.id.recyclerPacchi);
        lm = new LinearLayoutManager(this);
        listaPacchi = (ArrayList<Pacco>) InternalStorage.readObject(getApplicationContext(),"listaPacchi");
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        utenteAttivo = (String) prefs.getString("utenteAttivo","");
        tipoUtente = (String) prefs.getString("tipoUtenteAttivo","");
        delegation = this;


        if (listaPacchi == null){
            // rest calla per i pacchi
            restCallPacchi(delegation,"users/corrieri/"+utenteAttivo+".json");

        }
        else {
            setRecyclerPacco();
        }
    }

    @Override
    public void taskToDo(String string) {
        pd.dismiss();
        pd.cancel();
        if (string.equals("error")){
            Toast.makeText(getApplicationContext(),"Errore nel caricamento",Toast.LENGTH_SHORT).show();
        }
        else if (string.equals("success")){
            setRecyclerPacco();
        }
    }

    public void setRecyclerPacco (){
        if (listaPacchi.size()==0){
            Toast.makeText(getApplicationContext(),"NO PACKS TO DELIVERY",Toast.LENGTH_LONG).show();
        }
        else {
            paccoAdapter = new PaccoAdapter(listaPacchi,this,tipoUtente);
            recyclerPacco.setLayoutManager(lm);
            recyclerPacco.setAdapter(paccoAdapter);
        }


    }

    public void restCallPacchi (final TaskCompletion delegation, String url){
        pd = new ProgressDialog(this);
        pd.show();
        FireBaseConnection.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                if (s.equals("null")){
                    delegation.taskToDo("error");
                }
                else {
                    listaPacchi = (ArrayList<Pacco>) JasonParser.getPacchi(s);
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
    public void onBackPressed() {
        Session.logout(getApplicationContext());
    }
}
