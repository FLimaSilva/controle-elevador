package com.br.uellisson.controleelevador.view.adapter;

import com.br.uellisson.controleelevador.model.FrequencyUse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by uellisson on 31/08/2017.
 */
public class ReportRecyclerView extends FirebaseRecyclerAdapter<FrequencyUse, ReportViewHolder> {

    public ReportRecyclerView(Class<FrequencyUse> modelClass,
                              int modelLayout,
                              Class<ReportViewHolder> viewHolderClass,
                              Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(ReportViewHolder viewHolder, FrequencyUse model, int position) {
        viewHolder.text1.setText(model.getUserName());
        viewHolder.text2.setText(model.getDate());
    }
}
