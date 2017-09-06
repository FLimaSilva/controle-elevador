package com.br.uellisson.controleelevador.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.br.uellisson.controleelevador.R;

/**
 * Created by uellisson on 31/08/2017.
 */
public class ReportViewHolder extends ViewHolder{

    public TextView tvRoute;
    public TextView tvUser;
    public TextView tvDate;
    public TextView tvHour;

    public ReportViewHolder(View itemView) {
        super(itemView);
        tvRoute = (TextView) itemView.findViewById(R.id.tv_route);
        tvUser = (TextView) itemView.findViewById(R.id.tv_user);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        tvHour = (TextView) itemView.findViewById(R.id.tv_hour);
    }
}
