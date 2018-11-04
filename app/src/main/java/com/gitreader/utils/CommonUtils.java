package com.gitreader.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by sameer.madaan on 8/9/2016.
 */
public class CommonUtils {


    public static BitmapDrawable setIconColor(int resourceID, Activity activity, int color) {
        if (color == 0) {
            color = 0xffffffff;
        }

        final Resources res = activity.getResources();
        Drawable maskDrawable = res.getDrawable(resourceID);
        if (!(maskDrawable instanceof BitmapDrawable)) {
            return null;
        }

        Bitmap maskBitmap = ((BitmapDrawable) maskDrawable).getBitmap();
        final int width = maskBitmap.getWidth();
        final int height = maskBitmap.getHeight();

        Bitmap outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawBitmap(maskBitmap, 0, 0, null);

        Paint maskedPaint = new Paint();
        maskedPaint.setColor(color);
        maskedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        canvas.drawRect(0, 0, width, height, maskedPaint);

        BitmapDrawable outDrawable = new BitmapDrawable(res, outBitmap);
        return outDrawable;
    }


    public static void setBackgroundThemeForTiles(Activity activity, View view, int shapeColorID, int shapeColor, int shapedrawableId, int shapeDrawable) {
        //Set Background Color

        LayerDrawable ldTheme = (LayerDrawable) view.getBackground();
        GradientDrawable shape = (GradientDrawable)
                ldTheme.findDrawableByLayerId(shapeColorID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shape.setStroke(5, shapeColor);
        } else {
            shape.setStroke(5, shapeColor);
        }
        //Set Bitmap Drawable
        if (shapeDrawable != 0) {
            BitmapDrawable bmp = (BitmapDrawable) ResourcesCompat.getDrawable(activity.getResources(), shapeDrawable, activity.getTheme());
            //  bmp.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            ldTheme.setDrawableByLayerId(shapedrawableId, bmp);
        }
    }

    public static boolean isOnline(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


    private void copyAssets(Activity activity) {
        AssetManager assetManager = activity.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(activity.getExternalFilesDir(null), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    // convert from bitmap to byte array
    public static String getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

        String imgString = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);

        return imgString;
    }


    // get the base 64 string


}
