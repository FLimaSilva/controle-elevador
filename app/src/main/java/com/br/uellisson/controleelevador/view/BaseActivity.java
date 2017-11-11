package com.br.uellisson.controleelevador.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.br.uellisson.controleelevador.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe que representa a Base de todas as Activities
 */
abstract public class BaseActivity extends AppCompatActivity {

    /**
     * Atributos da classe
     */
    protected EditText email;
    protected EditText password;
    protected ProgressBar progressBar;

    /**
     * Método para exibir uma mensagem que fica na parte inferior da tela
     * @param message
     */
    protected void showSnackbar(String message ){
        Snackbar.make(progressBar,
                message,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    /**
     * Método que coloca as setas de voltar nas tela
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    /**
     * Metodos que exibe o loadind
     */
    protected void openProgressBar(){
        progressBar.setVisibility( View.VISIBLE );
    }

    /**
     * Metodos que exconde o loadind
     */
    protected void closeProgressBar(){
        progressBar.setVisibility( View.GONE );
    }


    /**
     * Método que verifica as permissoes do usuario
     * @return
     */
    public boolean checkAndRequestPermissions(){

        int permissaogravar = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissaogravar != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            return false;
        }

        return true;
    }

    /**
     * Método que captura e trata a resposta do popup de permissoes
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1: {

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {

                    for (int i = 0; i < permissions.length; i++) {

                        perms.put(permissions[i], grantResults[i]);
                    }

                    if (perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {

                        SharedPreferences.Editor editor = getSharedPreferences("PERMISSIONS", MODE_PRIVATE).edit();
                        editor.putBoolean("ENABLED", true);
                        editor.commit();

                    } else {


                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            showOkDialog(getString(R.string.permissions_required_permission_internet), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            checkAndRequestPermissions();
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            SharedPreferences.Editor editor = getSharedPreferences("PERMISSIONS", MODE_PRIVATE).edit();
                                            editor.putBoolean("ENABLED", false);
                                            editor.commit();
                                            break;
                                    }
                                }
                            });

                        } else {

                            Toast.makeText(this, getString(R.string.permissions_go_to_settings), Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = getSharedPreferences("PERMISSIONS", MODE_PRIVATE).edit();
                            editor.putBoolean("ENABLED", false);
                            editor.commit();
                        }
                    }
                }
            }
        }
    }

    /**
     * Método que Exibe o popup de explicacao para as permissoes
     * @param message
     * @param okListener
     */
    private void showOkDialog(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancelar", okListener)
                .create()
                .show();
    }
}
