package com.mobileinformationsystems.exercise5.augmentedreality.SampleApplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.webkit.WebView;

import java.net.URL;

public class OffscreenImage
{
    private URL mUrl;
    final WebView mView;// = new WebView(this);

    final float scale = 2.0f;
    final int contentWidth = 240;
    Bitmap mBitmap;

    @SuppressWarnings("deprecation")
    public OffscreenImage(URL url, Activity activity)
    {
        mUrl = url;
        mView = new WebView(activity);
        mView.setPictureListener(new WebView.PictureListener()
         {
             @Override
             public void onNewPicture(WebView webView, Picture picture)
             {
                if(mView.getProgress() == 100 && mView.getContentHeight() > 0)
                {
                    mView.setPictureListener(null);
                    final int width = Math.round(contentWidth * scale);
                    final int height = Math.round(mView.getContentHeight() * scale);
                    mBitmap = getBitmap(mView, width, height);
                }
             }
         });
        mView.loadUrl(mUrl.toString());
        mView.setInitialScale(Math.round(scale * 100));

        mView.layout(0, 0, 1, 1);
    }

    private Bitmap getBitmap(final WebView view, final int width, final int height)
    {
        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public Bitmap getmBitmap()
    {
        return mBitmap;
    }
}
