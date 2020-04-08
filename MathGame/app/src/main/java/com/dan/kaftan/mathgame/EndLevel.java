package com.dan.kaftan.mathgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class EndLevel extends AppCompatActivity {

    ImageButton nextLevelBtn;
    ImageButton menuBtn;
    int levelNum;

    boolean isLevel;
    boolean mute;
    boolean levelPassed;

    ImageView star1;
    ImageView star2;
    ImageView star3;
    ImageView tv;
    ImageView levelFailedIv;

    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_level);
        getSupportActionBar().hide();


        try {
            if (Game.interstitialAd != null && Game.interstitialAd.isLoaded()) {
                Game.interstitialAd.show();
            }
        }catch (Exception e){
            // never mind, it is just an Ad...
        }



        menuBtn = (ImageButton) findViewById(R.id.menu_btn);
        nextLevelBtn = (ImageButton) findViewById(R.id.next_level_btn);


        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        tv = (ImageView) findViewById(R.id.tv_iv);
        levelFailedIv = (ImageView)findViewById(R.id.level_failed_iv);


        MobileAds.initialize(this, "ca-app-pub-7775472521601802~5091426220");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        Intent a = getIntent();
        isLevel = a.getBooleanExtra("isLevel", false);
        mute = a.getBooleanExtra("mute", false);
        levelPassed = a.getBooleanExtra("levelPassed", levelPassed);
        levelNum = a.getIntExtra("levelNum", 0 );


        if (levelPassed){
            tv.setImageResource(R.drawable.level_passed_txt);
            levelFailedIv.setVisibility(View.INVISIBLE);
            nextLevelBtn.setImageResource(R.drawable.next_level_btn);
        }
        else {
            star1.setVisibility(View.INVISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);
            tv.setImageResource(R.drawable.try_again_txt);
            nextLevelBtn.setImageResource(R.drawable.try_again_btn);

        }

        if (levelNum == 46){
            levelFailedIv.setImageResource(R.drawable.eeee);
            tv.setImageResource(R.drawable.aaaaa);
            star1.setVisibility(View.INVISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);
        }

    }


    public void menuBtnOnClick(View view) {
        Intent i = new Intent(EndLevel.this, MainActivity.class);
        startActivity(i);


    }


    public void nextLevelBtnOnCLick(View view) {

        Intent i = new Intent(EndLevel.this, Game.class);
        i.putExtra("isLevel", isLevel);
        i.putExtra("mute", mute);
        startActivity(i);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}
