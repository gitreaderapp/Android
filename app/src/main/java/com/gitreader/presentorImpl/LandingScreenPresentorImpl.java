package com.gitreader.presentorImpl;

import android.app.Activity;
import android.graphics.pdf.PdfRenderer;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.gitreader.R;
import com.gitreader.interactor.LandingScreenInteractor;
import com.gitreader.interactorImpl.LandingScreenImpl;
import com.gitreader.presentor.LandingScreenPresentor;
import com.gitreader.utils.CommonUtils;
import com.gitreader.utils.PreferenceHandler;
import com.gitreader.view.LandingScreenView;

/**
 * Created by admin on 1/4/2018.
 */

public class LandingScreenPresentorImpl implements LandingScreenPresentor, LandingScreenPresentor.OnComplete {

    Activity activity;
    LandingScreenView landingScreenView;
    LandingScreenInteractor landingScreenInteractor;

    public LandingScreenPresentorImpl(Activity activity, LandingScreenView landingScreenView) {
        this.activity = activity;
        this.landingScreenView = landingScreenView;
        landingScreenInteractor = new LandingScreenImpl();
    }

    @Override
    public void addToGitReader(EditText etURL) {
        if (CommonUtils.isOnline(activity)) {
            if (etURL.getText().toString().trim().length() > 0) {
                if (Patterns.WEB_URL.matcher(etURL.getText().toString()).matches()) {
                    landingScreenView.showProgress();
                    PreferenceHandler.writeBoolean(activity, PreferenceHandler.IS_ADDED, true);
                    landingScreenInteractor.downloadPage(activity, etURL.getText().toString(), this);
                    //landingScreenView.goNext(etURL.getText().toString());
                } else {
                    landingScreenView.showMessage(activity.getString(R.string.enter_valid_url));

                }
            } else {
                landingScreenView.showMessage(activity.getString(R.string.enter_url));
            }
        } else {
            landingScreenView.noInternet();
        }
    }

    @Override
    public void onSuccess(String title) {
        landingScreenView.hideProgress();
        landingScreenView.goNext(title);
    }

    @Override
    public void onFail(String message) {
        landingScreenView.hideProgress();
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}
