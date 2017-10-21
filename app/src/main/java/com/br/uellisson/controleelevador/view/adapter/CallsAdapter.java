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
 */

public class CallsAdapter extends RecyclerView.Adapter {

    List<CallElevator> CallsList;
    Context context;

    public CallsAdapter(List<CallElevator> callsList, Context context) {
        this.CallsList = callsList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_report, parent, false);

        CallsViewHolder holder = new CallsViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final CallsViewHolder holder = (CallsViewHolder) viewHolder;
        holder.tvRoute.setText(CallsList.get(position).getRoute());
        holder.tvUser.setText(CallsList.get(position).getUserName());
        holder.tvDate.setText(CallsList.get(position).getDate());
        holder.tvHour.setText(CallsList.get(position).getHour());
    }

    @Override
    public int getItemCount() {
        return CallsList.size();
    }
}