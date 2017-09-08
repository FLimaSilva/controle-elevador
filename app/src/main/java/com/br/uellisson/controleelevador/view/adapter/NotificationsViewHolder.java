package com.br.uellisson.controleelevador.view.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.br.uellisson.controleelevador.R;

/**
 * Created by uellisson on 31/08/2017.
 */
public class NotificationsViewHolder extends ViewHolder{

    public TextView tvNotify;
    public TextView tvDate;

    public NotificationsViewHolder(View itemView) {
        super(itemView);
        tvNotify = (TextView) itemView.findViewById(R.id.tv_notify);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
    }
}
