package com.gitreader.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gitreader.R;
import com.gitreader.adapter.AllBooksAdapter;
import com.gitreader.base.BaseActivity;
import com.gitreader.presentor.MainActivityPresentor;
import com.gitreader.presentorImpl.MainActivityPresentorImpl;
import com.gitreader.utils.PreferenceHandler;
import com.gitreader.view.MainActivityView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.regex.Pattern;

public class MainActivity extends BaseActivity implements MainActivityView {

    WebView wbURL;
    RecyclerView rvBooks;
    MainActivityPresentor mainActivityPresentor;
    private AdView mAdView;
    private final Pattern fileNameReplacementPattern = Pattern.compile("[^a-zA-Z0-9-_\\.]");

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

        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        rvBooks = (RecyclerView) findViewById(R.id.rvBooks);
        mainActivityPresentor = new MainActivityPresentorImpl(this, this);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));

        wbURL = (WebView) findViewById(R.id.wbURL);
        wbURL.setWebViewClient(new WebViewClient());
        wbURL.getSettings().setJavaScriptEnabled(true);
        wbURL.getSettings().setLoadWithOverviewMode(true);
        wbURL.getSettings().setUseWideViewPort(true);

        mainActivityPresentor.loadBooks();
//
//        MobileAds.initialize(this, "ca-app-pub-3324953024383318~5683865005");
//
//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, LandingActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSelectBook(final String bookURL) {
        wbURL.loadUrl("file://" + bookURL);
        setUrl(bookURL);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public static boolean isURL(String url) {
        if (url == null) {
            return false;
        }
        // Assigning the url format regular expression
        String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
        return url.matches(urlPattern);
    }

    private String getFileName(String url) {

        String filename = url.substring(url.lastIndexOf('/') + 1);

        if (filename.trim().length() == 0) {
            filename = String.valueOf(url.hashCode());
        }

        if (filename.contains("?")) {
            filename = filename.substring(0, filename.indexOf("?")) + filename.substring(filename.indexOf("?") + 1).hashCode();
        }

        filename = fileNameReplacementPattern.matcher(filename).replaceAll("_");
        filename = filename.substring(0, Math.min(200, filename.length()));
        return filename;
    }


    @Override
    public void setAdapter(AllBooksAdapter allBooksAdapter) {
        rvBooks.setAdapter(allBooksAdapter);
        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            wbURL.loadUrl("file://" + bundle.getString(PreferenceHandler.KEY_ADDED_URL).toString());
//        } else {
//            wbURL.loadUrl("file://" + allBooksAdapter.getItem(allBooksAdapter.getItemCount() - 1));
//        }
        String bookURL =/* "file://" + */(bundle != null && bundle.getString(PreferenceHandler.KEY_ADDED_URL) != null ? bundle.getString(PreferenceHandler.KEY_ADDED_URL) :
                allBooksAdapter.getItem(allBooksAdapter.getItemCount() - 1));
        Log.e("Url", "file://" + bookURL);
        wbURL.loadUrl("file://" + bookURL);
        setUrl(bookURL);
    }

    private void setUrl(final String bookURL) {
        wbURL.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("Url", url);
                try {
                    if (isURL(url)) {
//                        if (bookURL.contains("index.html") && (url.contains("html") || url.contains("part"))) {
                        url = "file://" + bookURL.replace("index.html", getFileName(url));
//                        } else {
//                            url = "file://" + bookURL;
//                        }
                    } else if (!url.contains(".html") && !url.contains("part")) {
                        url += "index.html";
                    } else if (url.contains("part")) {
                        String url1 = url;
//                        url1 = url1.replace("/storage/sdcard/Android/data/jonas.tool.saveForOffline/files/",
//                                "https://worldaftercapital.gitbooks.io/worldaftercapital/content/");
                        url1 = "https://worldaftercapital.gitbooks.io/worldaftercapital/content/" + getUrl(url1) + "/";
                        url = "file://" + bookURL.replace("index.html", getFileName(url1));
                    } else if (!url.contains("Introduction___World_After_Capital")) {
                        String add = url.replace("file:///storage/sdcard/Android/data/jonas.tool.saveForOffline/files/", "");
                        url = "file://" + bookURL.replace("index.html", add.isEmpty() ? "index.html" : add);
                    }
                    view.loadUrl(url);
                    Log.e("fileUrl", url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private String getUrl(String abc) {
//        String test = "file:///storage/sdcard/Android/data/jonas.tool.saveForOffline/files/Introduction___World_After_Capital2018-05-21-11-51-34/";
        String part = "";

        if (abc.contains("part-one")) {
            part = "part-one";
        } else if (abc.contains("part-two")) {
            part = "part-two";
        } else if (abc.contains("part-three")) {
            part = "part-three";
        } else if (abc.contains("part-four")) {
            part = "part-four";
        }

//        for (int i = test.length(); i < abc.length(); i++)
//            part = part + abc.charAt(i);
        return part;
    }

    @Override
    public void onDelete(String bookURL) {
        mainActivityPresentor.deleteBook(bookURL);
    }

    @Override
    public void addNew() {
        Intent intent = new Intent(this, LandingActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wbURL.canGoBack()) {
                wbURL.goBack();
            } else {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
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
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
