package it.dsgroup.provafinale.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.dsgroup.provafinale.InsertPaccoActivity;
import it.dsgroup.provafinale.R;
import it.dsgroup.provafinale.models.Corriere;

/**
 * Created by utente9.academy on 14/12/2017.
 */

public class CorriereAdapter extends RecyclerView.Adapter<CorriereAdapter.CorriereHolder>{
    private ArrayList<Corriere> listaCorrieri;
    private Context context;

    public CorriereAdapter(ArrayList<Corriere> listaCorrieri, Context context) {
        this.listaCorrieri = listaCorrieri;
        this.context = context;
    }

    @Override
    public CorriereHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.corriere_card,parent,false);
        CorriereHolder corriereHolder = new CorriereHolder(v);
        return corriereHolder;
    }

    @Override
    public void onBindViewHolder(final CorriereHolder holder, int position) {
        final String nomeCorriere = listaCorrieri.get(position).getUserCorriere();
        holder.tCorriere.setText(nomeCorriere.toUpperCase());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, InsertPaccoActivity.class);
                i.putExtra("corriereSelezionato",holder.tCorriere.getText().toString().toLowerCase());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaCorrieri.size();
    }

    public class CorriereHolder extends RecyclerView.ViewHolder {
        private TextView tCorriere = itemView.findViewById(R.id.tCorriere);
        private CardView cardView = itemView.findViewById(R.id.cardCorriere);
        public CorriereHolder(View itemView) {
            super(itemView);

        }
    }
}
