package it.dsgroup.provafinale;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import it.dsgroup.provafinale.adapters.PaccoAdapter;
import it.dsgroup.provafinale.models.Pacco;
import it.dsgroup.provafinale.utilities.FireBaseConnection;
import it.dsgroup.provafinale.utilities.InternalStorage;
import it.dsgroup.provafinale.utilities.JasonParser;
import it.dsgroup.provafinale.utilities.TaskCompletion;

public class PacchiCommissionatiActivity extends AppCompatActivity implements TaskCompletion{

    private RecyclerView rrecycleCommissionati;
    private LinearLayoutManager lm;
    private ProgressDialog pd;
    private ArrayList<Pacco> listaPacchi;
    private TaskCompletion delegation;
    private PaccoAdapter paccoAdapter;
    private SharedPreferences prefs;
    private String utenteAttivo;
    private SwipeRefreshLayout refrechPacchi;
    private String tipoUtente;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacchi_commissionati);

        refrechPacchi = findViewById(R.id.refrechPacchi);
        rrecycleCommissionati = findViewById(R.id.recycleCommissionati);
        lm = new LinearLayoutManager(this);
        delegation=this;
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        utenteAttivo = (String) prefs.getString("utenteAttivo","");
        tipoUtente = (String) prefs.getString("tipoUtenteAttivo","");

        listaPacchi = (ArrayList<Pacco>) InternalStorage.readObject(getApplicationContext(),"listaCommissionati");
        if (listaPacchi==null){
            String url = "users/clienti/"+utenteAttivo+".json";
            restCallCommissionati(delegation,url);
        }
        else {
            setRecyclerPacco();
        }

        refrechPacchi.setOnRefreshListener(temporary3);

    }

    @Override
    public void taskToDo(String string) {
        pd.dismiss();
        pd.cancel();
        if (string.equals("error")){
            Toast.makeText(getApplicationContext(),"Errore nel caricamento",Toast.LENGTH_SHORT).show();
        }
        else if (string.equals("success")){
            InternalStorage.writeObject(getApplicationContext(),"listaCommissionati", listaPacchi);
            setRecyclerPacco();
        }
    }

    private void setRecyclerPacco() {
        paccoAdapter = new PaccoAdapter(listaPacchi,this,tipoUtente);
        rrecycleCommissionati.setLayoutManager(lm);
        rrecycleCommissionati.setAdapter(paccoAdapter);
    }

    public void restCallCommissionati (final TaskCompletion delegation, String url){
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
                    listaPacchi = JasonParser.getPacchi(s);
                    delegation.taskToDo("success");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                delegation.taskToDo("error");
            }
        });
    }

    SwipeRefreshLayout.OnRefreshListener temporary3 = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refrechPacchi.setRefreshing(false);
            restCallCommissionati(delegation,"users/clienti/"+utenteAttivo+".json");
        }
    };
}
