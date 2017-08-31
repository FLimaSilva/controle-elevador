package com.br.uellisson.controleelevador.view;

import android.os.Bundle;
import com.br.uellisson.controleelevador.R;

public class NotificationsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.notifications));
    }

}
