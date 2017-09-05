package com.br.uellisson.controleelevador.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

/**
 * Created by uellisson on 31/08/2017.
 */
public class ReportViewHolder extends ViewHolder{

    public TextView text1;
    public TextView text2;

    public ReportViewHolder(View itemView) {
        super(itemView);

        text1 = (TextView) itemView.findViewById(android.R.id.text1);
        text2 = (TextView) itemView.findViewById(android.R.id.text2);
    }
}
