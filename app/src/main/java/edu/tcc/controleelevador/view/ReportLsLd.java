package edu.tcc.controleelevador.view;

import android.os.Bundle;

import com.br.uellisson.controleelevador.R;

/**
 * Classe responsável pela criação e operação da tela
 * do relatório dos limites de subida e descida do aplicativo
 */
public class ReportLsLd extends BaseActivity {

    /**
     * Método onde é criada a tela de relatóio dos limites de
     * subida e descida do elevador
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_ls_ld);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.report_ls_ld));
    }
}
