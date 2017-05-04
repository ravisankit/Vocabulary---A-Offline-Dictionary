package com.ravi.ameliorate.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.ravi.ameliorate.R;

public class SplashActivity extends AppCompatActivity {

    ImageView dictSplash;
    Bitmap mIcon_val=null;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        /*PublisherAdView mPublisherAdView = (PublisherAdView) findViewById(R.id.publisherAdView);
        PublisherAdView mPublisherAdView2 = (PublisherAdView) findViewById(R.id.publisherAdView2);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().
                setContentUrl("http://googleadsdeveloper.blogspot.com/2014/03/monetizing-unity-mobile-apps-just-got.html").build();
        mPublisherAdView.loadAd(adRequest);
        mPublisherAdView2.loadAd(adRequest);*/

        final SharedPreferences sp = getSharedPreferences("dictionary",getApplicationContext().MODE_PRIVATE);
        final Animation animAcce = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        dictSplash=(ImageView) findViewById(R.id.dictSplash);
        dictSplash.setAnimation(animAcce);
        animAcce.start();

        name = sp.getString("name","");

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
				/* Create an Intent that will start the Menu-Activity. */

                if(name.length()==0){

                    Intent intent = new Intent(SplashActivity.this, NameAcitivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }else {

                    Intent intent = new Intent(SplashActivity.this, Dictionary.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            }
        }, 2500);

    }
}
