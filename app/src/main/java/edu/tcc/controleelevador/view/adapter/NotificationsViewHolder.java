package edu.tcc.controleelevador.view.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.br.uellisson.controleelevador.R;

/**
 * Created by uellisson on 31/08/2017.
 *
 * Classe que inicializa os componetes do Adapter das Notificações
 */
public class NotificationsViewHolder extends ViewHolder{

    /**
     * Atributos do classe
     */
    public TextView tvNotify;
    public TextView tvDate;

    /**
     * Método que inicializa os componentes
     * @param itemView
     */
    public NotificationsViewHolder(View itemView) {
        super(itemView);
        tvNotify = (TextView) itemView.findViewById(R.id.tv_notify);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
    }
}
