package com.mobileinformationsystems.exercise5.augmentedreality.SampleApplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.util.Log;
import android.webkit.WebView;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class OffscreenImage
{
    private static final String LOGTAG = "OffscreenImage";

    WebView mView;// = new WebView(this);

    final float scale = 1.0f;
    final int contentWidth = 247;
 //   Bitmap mBitmap;
    FinishedLoading mFinishedLoading;
    Queue<String> mUrlQueue = new LinkedList<String>();
    Activity mActivity;

    @SuppressWarnings("deprecation")
    public OffscreenImage(Activity activity, FinishedLoading finishedLoading)
    {
        mFinishedLoading = finishedLoading;

        mActivity = activity;
    }

    @SuppressWarnings("deprecation")
    private WebView.PictureListener createListener()
    {
        WebView.PictureListener listener = new WebView.PictureListener()
        {
            @Override
            public void onNewPicture(WebView webView, Picture picture)
            {
                if (mView.getProgress() == 100 && mView.getContentHeight() > 0)
                {
                    mView.setPictureListener(null);
                    Log.v(LOGTAG, "loaded::" + mView.getUrl());
                    final int width = Math.round(contentWidth * scale);
                    final int height = Math.round(mView.getContentHeight() * scale);
                    Bitmap bitmap = getBitmap(mView, width, height);
                    mFinishedLoading.GetBitmap(bitmap);
                    bitmap.recycle();
                    mView = null;
                    LoadPages();
                }
            }
        };
        return listener;
    }


    public void AddUrl(String url)
    {
        mUrlQueue.add(url);
    }

    @SuppressWarnings("deprecation")
    public void LoadPages()
    {
        if(mUrlQueue.size() > 0)
        {
            mView = new WebView(mActivity);
            mView.setPictureListener(createListener());
            String url = mUrlQueue.poll();
            Log.v(LOGTAG, "loading::" + url);
            mView.loadUrl(url);
            mView.setInitialScale(Math.round(scale * 100));

            mView.layout(0, 0, 1, 1);
        }
    }

    private Bitmap getBitmap(final WebView view, final int width, final int height)
    {
        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

//    public Bitmap getmBitmap()
//    {
//        return mBitmap;
//    }

    public interface FinishedLoading
    {
        public void GetBitmap(Bitmap bitmap);
    }
}
