package it.dsgroup.provafinale.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import it.dsgroup.provafinale.R;
import it.dsgroup.provafinale.models.Pacco;

/**
 * Created by utente9.academy on 14/12/2017.
 */

public class PaccoAdapter extends RecyclerView.Adapter<PaccoAdapter.ClienteHolder> {

    private ArrayList<Pacco> listaPacchi;
    private Context context;

    public PaccoAdapter(ArrayList<Pacco> listaPacchi, Context context){
        this.listaPacchi = listaPacchi;
        this.context = context;
    }



    @Override
    public ClienteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pacco_card,parent,false);
        ClienteHolder clienteHolder = new ClienteHolder(v);
        return clienteHolder;
    }

    @Override
    public void onBindViewHolder(ClienteHolder holder, int position) {
        Pacco pacco = listaPacchi.get(position);
        holder.tPacco.setText(pacco.getIdPacco());
    }

    @Override
    public int getItemCount() {
        return listaPacchi.size();
    }

    public class ClienteHolder extends RecyclerView.ViewHolder{
        private TextView tPacco;
        public ClienteHolder(View itemView) {
            super(itemView);

            tPacco = itemView.findViewById(R.id.tPacco);
        }
    }
}
