package com.br.uellisson.controleelevador.view.adapter;

import com.br.uellisson.controleelevador.model.CallElevator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by uellisson on 31/08/2017.
 */
public class ReportRecyclerView extends FirebaseRecyclerAdapter<CallElevator, ReportViewHolder> {

    public ReportRecyclerView(Class<CallElevator> modelClass,
                              int modelLayout,
                              Class<ReportViewHolder> viewHolderClass,
                              Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(ReportViewHolder viewHolder, CallElevator model, int position) {
        viewHolder.tvRoute.setText(model.getRoute());
        viewHolder.tvUser.setText(model.getUserName());
        viewHolder.tvDate.setText(model.getDate());
        viewHolder.tvHour.setText(model.getHour());
    }
}
