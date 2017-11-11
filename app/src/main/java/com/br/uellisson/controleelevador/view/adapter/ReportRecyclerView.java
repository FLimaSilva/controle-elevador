package com.br.uellisson.controleelevador.view.adapter;

import com.br.uellisson.controleelevador.model.CallElevator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by uellisson on 31/08/2017.
 *
 *  Classe do adapter da lista de relatório de frequência de uso
 */
public class ReportRecyclerView extends FirebaseRecyclerAdapter<CallElevator, ReportViewHolder> {

    /**
     * Contrutor da Classe do Adapter
     * @param modelClass
     * @param modelLayout
     * @param viewHolderClass
     * @param ref
     */
    public ReportRecyclerView(Class<CallElevator> modelClass,
                              int modelLayout,
                              Class<ReportViewHolder> viewHolderClass,
                              Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    /**
     * Método que popula os itens de adapter da Frequencia de uso
     * @param viewHolder
     * @param model
     * @param position
     */
    @Override
    protected void populateViewHolder(ReportViewHolder viewHolder, CallElevator model, int position) {
        viewHolder.tvRoute.setText(model.getRoute().replace("0", "T"));
        viewHolder.tvUser.setText(model.getUserName());
        viewHolder.tvDate.setText(model.getDate());
        viewHolder.tvHour.setText(model.getHour());
    }
}
