package it.dsgroup.provafinale.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import it.dsgroup.provafinale.DettaglioPaccoActivity;
import it.dsgroup.provafinale.GestioneConsegnaActivity;
import it.dsgroup.provafinale.R;
import it.dsgroup.provafinale.models.Pacco;
import it.dsgroup.provafinale.utilities.InternalStorage;

/**
 * Created by utente9.academy on 14/12/2017.
 */

public class PaccoAdapter extends RecyclerView.Adapter<PaccoAdapter.ClienteHolder> {

    private ArrayList<Pacco> listaPacchi;
    private Context context;
    private String tipoUtente;

    public PaccoAdapter(ArrayList<Pacco> listaPacchi, Context context, String tipoUtente){
        this.listaPacchi = listaPacchi;
        this.context = context;
        this.tipoUtente = tipoUtente;
    }



    @Override
    public ClienteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pacco_card,parent,false);
        ClienteHolder clienteHolder = new ClienteHolder(v);
        return clienteHolder;
    }

    @Override
    public void onBindViewHolder(final ClienteHolder holder, int position) {
        Pacco pacco = listaPacchi.get(position);
        holder.tPacco.setText(pacco.getIdPacco());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipoUtente.toLowerCase().equals("clienti")){
                    // vado a visualizzare i dettagli del pacco
                    Intent i = new Intent(context, DettaglioPaccoActivity.class);
                    i.putExtra("idPacco",holder.tPacco.getText().toString());
                    context.startActivity(i);

                }
                else if (tipoUtente.toLowerCase().equals("corrieri")){
                    Intent i = new Intent(context, GestioneConsegnaActivity.class);
                    i.putExtra("idPacco",holder.tPacco.getText().toString());
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPacchi.size();
    }

    public class ClienteHolder extends RecyclerView.ViewHolder{
        private TextView tPacco;
        private CardView cardView;
        public ClienteHolder(View itemView) {
            super(itemView);
            cardView =itemView.findViewById(R.id.cardPacco);
            tPacco = itemView.findViewById(R.id.tPacco);
        }
    }
}
