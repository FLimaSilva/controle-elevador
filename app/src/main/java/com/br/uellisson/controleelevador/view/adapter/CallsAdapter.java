package com.br.uellisson.controleelevador.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.br.uellisson.controleelevador.R;
import com.br.uellisson.controleelevador.model.CallElevator;

import java.util.List;

/**
 * Created by usilva on 17/10/2017.
 *
 * Classe do adapter da lista de chamdas do elevador
 */
public class CallsAdapter extends RecyclerView.Adapter {

    /**
     * Atributos da Classe
     */
    List<CallElevator> CallsList;
    Context context;

    /**
     * Construtor do Adapter
     * @param callsList
     * @param context
     */
    public CallsAdapter(List<CallElevator> callsList, Context context) {
        this.CallsList = callsList;
        this.context = context;
    }

    /**
     * Método onde é criado o Adapter
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_report, parent, false);

        CallsViewHolder holder = new CallsViewHolder(view);

        return holder;
    }

    /**
     * Método que popula os itens do Adapter
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final CallsViewHolder holder = (CallsViewHolder) viewHolder;
        holder.tvRoute.setText(CallsList.get(position).getRoute().replace("0","T"));
        holder.tvUser.setText(CallsList.get(position).getUserName());
        holder.tvDate.setText(CallsList.get(position).getDate());
        holder.tvHour.setText(CallsList.get(position).getHour());
    }

    /**
     * Método que retorna a quantidade de itens do Adapter
     * @return
     */
    @Override
    public int getItemCount() {
        return CallsList.size();
    }
}