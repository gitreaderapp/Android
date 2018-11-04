package com.gitreader.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.gitreader.R;

/**
 * Created by admin on 1/4/2018.
 */

public class BaseActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    public void showDialog(String title, String string) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setTitle(title);
        builder.setMessage(string);
        builder.setCancelable(true);


// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void showProgressBar() {
        progressDialog = ProgressDialog.show(this, "", "Please wait..");
        progressDialog.show();
    }

    public void hideProgressBar() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            android.support.v7.app.AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new android.support.v7.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new android.support.v7.app.AlertDialog.Builder(this);
            }
            builder.setTitle(getString(R.string.app_name))
                    .setMessage("Are you sure you want to exit the App?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
