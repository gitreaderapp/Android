package com.gitreader.interactorImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import com.gitreader.R;
import com.gitreader.database.DBHelper;
import com.gitreader.interactor.LandingScreenInteractor;
import com.gitreader.model.Book;
import com.gitreader.presentor.LandingScreenPresentor;
import com.gitreader.utils.SaveService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by admin on 1/5/2018.
 */

public class LandingScreenImpl implements LandingScreenInteractor {
    String url;
    Activity activity;
    LandingScreenPresentor.OnComplete onComplete;
    private String html = "";


    @Override
    public void downloadPage(Activity activity, String url, LandingScreenPresentor.OnComplete onComplete) {
        this.url = url;
        this.activity = activity;
        this.onComplete = onComplete;

        SaveService saveService = new SaveService(activity, onComplete);
        saveService.onStartCommand(url);
        // new LoadURL().execute();

    }

   /* public class LoadURL extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {



                try {
                    HttpParams httpParameters = new BasicHttpParams();
// Set the timeout in milliseconds until a connection is established.
// The default value is zero, that means the timeout is not used.
                    int timeoutConnection = 10000;
                    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
// Set the default socket timeout (SO_TIMEOUT)
// in milliseconds which is the timeout for waiting for data.
                    int timeoutSocket = 10000;
                    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
                    HttpClient client = new DefaultHttpClient(httpParameters);

                    HttpGet request = new HttpGet(url);

                    HttpResponse response = client.execute(request);
                    InputStream in = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder str = new StringBuilder();
                    String line = null;

                    String filename = System.currentTimeMillis()+".html";
                    File myDir = new File(Environment
                            .getExternalStorageDirectory()+"/GitReader");

                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    File myFile = new File(myDir+"/"+filename);
                    if(!myFile.exists()){
                        myFile.createNewFile();
                    }

                    FileOutputStream fos;

                    while ((line = reader.readLine()) != null) {
                        str.append(line);
                    }

                    byte[] data = str.toString().getBytes();
                    try {
                        fos = new FileOutputStream(myFile);
                        fos.write(data);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    in.close();
                    html = myFile.getAbsolutePath();

                    DBHelper dbHelper = new DBHelper(activity);

                    Book book = new Book();
                    book.setBookURL(html);
                    book.setBookName(filename);

                    dbHelper.addBook(book);
                } catch (IOException e) {

                }
            catch (Exception e){

            }

                return null;
            }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(html.length()>0){
                onComplete.onSuccess(html);
            }else {
                onComplete.onFail(activity.getString(R.string.error));

            }

            super.onPostExecute(aVoid);
        }
    }*/
}
