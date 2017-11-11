package com.br.uellisson.controleelevador.view.adapter;

import android.app.Notification;

import com.br.uellisson.controleelevador.model.CallElevator;
import com.br.uellisson.controleelevador.model.Notify;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by uellisson on 31/08/2017.
 *
 *  Classe do adapter da lista de notificação do aplicativo
 */
public class NotificationsRecyclerView extends FirebaseRecyclerAdapter<Notify, NotificationsViewHolder> {

    /**
     * Contrutor da Classe do Adapter de Notificações
     * @param modelClass
     * @param modelLayout
     * @param viewHolderClass
     * @param ref
     */
    public NotificationsRecyclerView(Class<Notify> modelClass,
                                     int modelLayout,
                                     Class<NotificationsViewHolder> viewHolderClass,
                                     Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    /**
     * Método que popula os itens de adapter de notificações
     * @param viewHolder
     * @param model
     * @param position
     */
    @Override
    protected void populateViewHolder(NotificationsViewHolder viewHolder, Notify model, int position) {
        viewHolder.tvNotify.setText(model.getNotify());
        viewHolder.tvDate.setText(model.getDateHour());
    }
}
