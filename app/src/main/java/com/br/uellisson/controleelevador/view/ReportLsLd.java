package com.br.uellisson.controleelevador.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.br.uellisson.controleelevador.R;

public class ReportLsLd extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_ls_ld);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.report_ls_ld));

    }
}
