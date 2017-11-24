package edu.tcc.controleelevador.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.uellisson.controleelevador.R;

import java.util.List;

import edu.tcc.controleelevador.model.CallElevator;
import edu.tcc.controleelevador.model.Notify;

/**
 * Created by usilva on 17/10/2017.
 *
 * Classe do adapter da lista de chamdas do elevador
 */
public class NotificationsAdapter extends RecyclerView.Adapter {

    /**
     * Atributos da Classe
     */
    private List<Notify> listNotifications;
    Context context;

    /**
     * Construtor do Adapter
     * @param listNotifications
     * @param context
     */
    public NotificationsAdapter(List<Notify> listNotifications, Context context) {
        this.listNotifications = listNotifications;
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
                .inflate(R.layout.item_notifications, parent, false);

        NotificationsViewHolder holder = new NotificationsViewHolder(view);

        return holder;
    }

    /**
     * Método que popula os itens do Adapter
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final NotificationsViewHolder holder = (NotificationsViewHolder) viewHolder;
        holder.tvNotify.setText(listNotifications.get(position).getNotify().replace("0","T"));
        holder.tvDate.setText(listNotifications.get(position).getDateHour());
    }

    /**
     * Método que retorna a quantidade de itens do Adapter
     * @return
     */
    @Override
    public int getItemCount() {
        return listNotifications.size();
    }
}