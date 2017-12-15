package it.dsgroup.provafinale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import it.dsgroup.provafinale.adapters.CorriereAdapter;
import it.dsgroup.provafinale.models.Corriere;
import it.dsgroup.provafinale.models.Pacco;
import it.dsgroup.provafinale.utilities.FireBaseConnection;
import it.dsgroup.provafinale.utilities.InternalStorage;
import it.dsgroup.provafinale.utilities.JasonParser;
import it.dsgroup.provafinale.utilities.TaskCompletion;

public class ListaCorrieriActivity extends AppCompatActivity implements TaskCompletion{

    private RecyclerView recyclerCorriere;
    private LinearLayoutManager lm;
    private CorriereAdapter corriereAdapter;
    private ArrayList<Corriere> listaCorrieri;
    private TaskCompletion delegation;
    private ProgressDialog pd;
    private Button bCommissionati;
    private SwipeRefreshLayout sw;
    private ArrayList<Pacco> listaPacchi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_corrieri);

        bCommissionati = findViewById(R.id.bCommissionati);
        recyclerCorriere = findViewById(R.id.recyclerCorriere);
        sw = findViewById(R.id.refrechCorrieri);
        lm = new LinearLayoutManager(this);
        listaCorrieri= (ArrayList<Corriere>) InternalStorage.readObject(getApplicationContext(),"listaCorrieri");
        delegation = this;

        if (listaCorrieri== null){
            // fare la chiamata rest per prendere la lista dei corrieri
            restCallCorrieri("users/corrieri.json", delegation);
            InternalStorage.writeObject(this,"listaCorrieri",listaCorrieri);
        }
        else {
            setRecyclerCorriere();
        }

        bCommissionati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaCorrieriActivity.this,PacchiCommissionatiActivity.class);
                startActivity(i);
            }
        });

        sw.setOnRefreshListener(temporary);


    }

    public void setRecyclerCorriere (){
        corriereAdapter = new CorriereAdapter(listaCorrieri,this);
        recyclerCorriere.setLayoutManager(lm);
        recyclerCorriere.setAdapter(corriereAdapter);
    }


    @Override
    public void taskToDo(String string) {
        pd.dismiss();
        pd.cancel();
        if (string.equals("erroe")){
            Toast.makeText(getApplicationContext(),"Errore nel caricamento",Toast.LENGTH_SHORT).show();
        }
        else if (string.equals("success")){
            setRecyclerCorriere();
        }
    }

    public void restCallCorrieri (final String url, final TaskCompletion delegation){
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
                    listaCorrieri = JasonParser.getCorrieri(s);
                    delegation.taskToDo("success");

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                delegation.taskToDo("errore");
            }
        });
    }

    SwipeRefreshLayout.OnRefreshListener temporary = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            restCallCorrieri("users/corrieri.json", delegation);
            sw.setRefreshing(false);
        }
    };
}
