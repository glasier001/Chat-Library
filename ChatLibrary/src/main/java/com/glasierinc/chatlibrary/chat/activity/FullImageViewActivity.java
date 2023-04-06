package com.glasierinc.chatlibrary.chat.activity;

import static com.glasierinc.chatlibrary.chat.util.ChatConstance.TYPE_CALL;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.TYPE_PDF;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.TYPE_VIDEO;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.commonlib.utils.SharedPrefs;
//import com.google.firebase.analytics.FirebaseAnalytics;
import com.glasierinc.chatlibrary.R;
import com.glasierinc.chatlibrary.chat.util.AppPrefs;
import com.glasierinc.chatlibrary.databinding.ActivityFullImageViewBinding;

public class FullImageViewActivity extends AppCompatActivity {
    private ActivityFullImageViewBinding mBinding;
    private int currentDuration = 0;
    private boolean close = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_full_image_view);
        //MediaController mediaController= new MediaController(this);
        mBinding.spinKit.setVisibility(View.VISIBLE);
        MediaController mediaController = new MediaController(this) {
            @Override
            public void hide() {
                super.show(0);//Default no auto hide timeout
            }
        };

        mBinding.civCloseMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                currentDuration = mBinding.videoView.getCurrentPosition();
                Log.e("close", String.valueOf(mBinding.videoView.getCurrentPosition()));
                SharedPrefs.savePrefs(getApplicationContext(), AppPrefs.DISCOVER_CURRENT_VIDEO_TIME, String.valueOf(currentDuration));
                close = false;
                mBinding.videoView.stopPlayback();
                finish();
            }
        });


        if (getIntent().getStringExtra("type").equalsIgnoreCase(TYPE_VIDEO)) {
            mBinding.cl.setBackgroundColor(Color.BLACK);
            mBinding.ivImage.setVisibility(View.GONE);
            mBinding.videoView.setVisibility(View.VISIBLE);

            Log.e("video", getIntent().getStringExtra("image"));

            mediaController.setAnchorView(mBinding.videoView);
            mBinding.videoView.setMediaController(mediaController);
            mBinding.videoView.setVideoPath(getIntent().getStringExtra("image"));
            mBinding.videoView.requestFocus();
            mBinding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer arg0) {
                    mBinding.spinKit.setVisibility(View.GONE);
                    mBinding.videoView.start();
                }
            });


            googleAnalyticEvent(getIntent().getStringExtra("image"));

        }


       else if (getIntent().getStringExtra("type").equalsIgnoreCase(TYPE_CALL)) {
            mBinding.cl.setBackgroundColor(Color.BLACK);
            mBinding.ivImage.setVisibility(View.GONE);
            mBinding.videoView.setVisibility(View.VISIBLE);

            Log.e("video", getIntent().getStringExtra("image"));

            mediaController.setAnchorView(mBinding.videoView);
            mBinding.videoView.setMediaController(mediaController);
            mBinding.videoView.setVideoPath(getIntent().getStringExtra("image"));
            mBinding.videoView.requestFocus();
            mBinding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer arg0) {
                    mBinding.spinKit.setVisibility(View.GONE);
                    mBinding.videoView.start();
                }
            });


            googleAnalyticEvent(getIntent().getStringExtra("image"));


            // for audio to play from url but some custom ui to be made for this like play,pause,seekbar and etc
//            MediaPlayer mediaPlayer;
//            mediaPlayer = MediaPlayer.create(this, R.raw.test_cbr);
//            mediaPlayer.setOnCompletionListener(new OnCompletionListener()
//            {
//                @Override
//                public void onCompletion(MediaPlayer mp)
//                {
//                    // TODO Auto-generated method stub
//                    Toast.makeText(getApplicationContext(), "Completed",
//                            Toast.LENGTH_LONG).show();
//
//                }
//            });
//            mediaPlayer.start();

        }



        else if (getIntent().getStringExtra("type").equalsIgnoreCase(TYPE_PDF)) {
            Log.e("pdf", getIntent().getStringExtra("image"));
            mBinding.ivImage.setVisibility(View.GONE);
            mBinding.videoView.setVisibility(View.GONE);
            mBinding.webview.setVisibility(View.GONE);
            String url = "https://docs.google.com/gview?embedded=true&url=" + getIntent().getStringExtra("image");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);

//            mBinding.webview.loadUrl(getIntent().getStringExtra("image"));
//
//            mBinding.webview.setDownloadListener(new DownloadListener() {
//                @Override
//                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//                    startActivity(i);
//                    finish();
//                }
//            });
//            pdfOpen(getIntent().getStringExtra("image"));
        } else {
            mBinding.ivImage.setVisibility(View.VISIBLE);
            mBinding.spinKit.setVisibility(View.GONE);
            mBinding.videoView.setVisibility(View.GONE);
            mBinding.webview.setVisibility(View.GONE);

            mBinding.cl.setBackgroundColor(Color.WHITE);
            mBinding.civCloseMedia.setImageDrawable(getDrawable(R.drawable.close_black));

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();


            Glide.with(this).load(getIntent().getStringExtra("image")).placeholder(circularProgressDrawable).into(mBinding.ivImage);

        }


    }




    @Override
    protected void onPause() {
        super.onPause();
        if (close) {
            Log.e("cccc", String.valueOf(mBinding.videoView.getCurrentPosition()));
            currentDuration = mBinding.videoView.getCurrentPosition();
            SharedPrefs.savePrefs(getApplicationContext(), AppPrefs.DISCOVER_CURRENT_VIDEO_TIME, String.valueOf(currentDuration));
        }

        mBinding.videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("got the time in resume", SharedPrefs.getSharedPrefString(this, AppPrefs.DISCOVER_CURRENT_VIDEO_TIME, "0"));
        if (SharedPrefs.getSharedPrefString(this, AppPrefs.DISCOVER_CURRENT_VIDEO_TIME, "0") != null) {
            mBinding.videoView.seekTo(Integer.parseInt(SharedPrefs.getSharedPrefString(this, AppPrefs.DISCOVER_CURRENT_VIDEO_TIME, "0")));
            mBinding.videoView.start();
        }

    }

    private void googleAnalyticEvent(String videoLink) {

//        @SuppressLint("MissingPermission") FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        Bundle bundle = new Bundle();
//        bundle.putString("VideoAndroid", "VideoAndroid");
//        bundle.putString("VideoLink", videoLink);
//        mFirebaseAnalytics.logEvent("VideoAndroid", bundle);
    }

    private void pdfOpen(String fileUrl) {

        mBinding.webview.getSettings().setJavaScriptEnabled(true);
        mBinding.webview.getSettings().setPluginState(WebSettings.PluginState.ON);

        //---you need this to prevent the webview from
        // launching another browser when a url
        // redirection occurs---
        mBinding.webview.setWebViewClient(new Callback());

        mBinding.webview.loadUrl(
                "https://docs.google.com/gview?embedded=true&url=" + fileUrl);

    }

    private class Callback extends WebViewClient {
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);

        }

        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }

}