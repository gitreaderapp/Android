package com.gitreader.presentor;

import android.widget.EditText;

/**
 * Created by admin on 1/4/2018.
 */

public interface LandingScreenPresentor {


    public void addToGitReader(EditText etURL);

    public interface OnComplete {
        public void onSuccess(String title);

        public void onFail(String message);
    }
}
