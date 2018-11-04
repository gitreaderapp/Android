package com.gitreader.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gitreader.R;
import com.gitreader.base.BaseActivity;
import com.gitreader.presentor.LandingScreenPresentor;
import com.gitreader.presentorImpl.LandingScreenPresentorImpl;
import com.gitreader.utils.PreferenceHandler;
import com.gitreader.view.LandingScreenView;

public class LandingActivity extends BaseActivity implements LandingScreenView, View.OnClickListener {

    LandingScreenPresentor landingScreenPresentor;
    EditText etURL;
    Button btnAddURL;
    ImageView imgHelp;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    private int i = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.notificatrion));
        }
        setContentView(R.layout.activity_landing);
        initView();
    }


    private void initView() {
        etURL = (EditText) findViewById(R.id.etURL);
        btnAddURL = (Button) findViewById(R.id.btnAdd);
        btnAddURL.setOnClickListener(this);

        imgHelp = (ImageView) findViewById(R.id.imgHelper);
        imgHelp.setOnClickListener(this);

        landingScreenPresentor = new LandingScreenPresentorImpl(this, this);
        total(i);
        Log.e("total", String.valueOf(i));
    }

    private void total(int i) {
        i = i + 50;
    }

    /**
     * Used to check permission in case of Android Marshmallow version.
     *
     * @param context
     * @return
     */
    private boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        /**
         * Check if Android version of the device is equal to or greater than Marshmallow then  prompt user to allow access to read and write external storage.
         */
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showPermissionAlert(context);

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                }
                return false;
            } else {
                landingScreenPresentor.addToGitReader(etURL);
                return true;
            }
        } else {
            landingScreenPresentor.addToGitReader(etURL);
            return true;
        }
    }

    /**
     * Alert user with the importance of allowing permission.
     *
     * @param context
     */
    private void showPermissionAlert(final Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage("External storage permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    /**
     * We be able to check if the permission is granted by the user. Need to override the onRequestPermissionsResult in the fragment and send the result of the callback to this method.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                /**
                 * check if gratn results length is greater than 0 and is equal to PERMISSION_GRANTED
                 */
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    landingScreenPresentor.addToGitReader(etURL);
                } else {
                    /**
                     * Show alert in case user has denied the request.
                     */
                    showPermissionAlert(this);
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void showMessage(String message) {
        showDialog(getString(R.string.alert), message);

    }

    @Override
    public void showProgress() {
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        hideProgressBar();
    }

    @Override
    public void goNext(String etURL) {
        Bundle bundle = new Bundle();
        bundle.putString(PreferenceHandler.KEY_ADDED_URL, etURL);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void noInternet() {
        showDialog(getString(R.string.app_name), getString(R.string.no_internet));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAdd) {
            checkPermission(this);
        } else if (v.getId() == R.id.imgHelper) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gitreaderapp.com"));
            startActivity(browserIntent);
        }
    }

}
