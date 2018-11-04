package com.gitreader.view;

import android.widget.EditText;

/**
 * Created by admin on 1/4/2018.
 */

public interface LandingScreenView {
    public void showMessage(String message);

    public void showProgress();

    public void hideProgress();

    public void goNext(String etURL);

    public void noInternet();

}
