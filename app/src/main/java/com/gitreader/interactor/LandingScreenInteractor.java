package com.gitreader.interactor;

import android.app.Activity;

import com.gitreader.presentor.LandingScreenPresentor;

/**
 * Created by admin on 1/5/2018.
 */

public interface LandingScreenInteractor {
    public void downloadPage(Activity activity, String url, LandingScreenPresentor.OnComplete onComplete);
}
