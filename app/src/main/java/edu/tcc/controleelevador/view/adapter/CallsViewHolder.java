package edu.tcc.controleelevador.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.br.uellisson.controleelevador.R;

/**
 * Created by usilva on 17/10/2017.
 *
 * Classe que inicializa os componetes do Adapter das chadadas
 */

public class CallsViewHolder extends RecyclerView.ViewHolder {
    /**
     * Atributos da Classe
     */
    TextView tvRoute;
    TextView tvUser;
    TextView tvDate;
    TextView tvHour;

    /**
     * Contrutor da Classe
     * @param view
     */
    public CallsViewHolder(View view) {
        super(view);
        tvRoute = (TextView) view.findViewById(R.id.tv_route);
        tvUser = (TextView)  view.findViewById(R.id.tv_user);
        tvDate = (TextView)  view.findViewById(R.id.tv_date);
        tvHour = (TextView)  view.findViewById(R.id.tv_hour);
    }
}
