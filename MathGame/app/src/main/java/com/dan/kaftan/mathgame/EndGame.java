package com.dan.kaftan.mathgame;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class EndGame extends AppCompatActivity implements RewardedVideoAdListener {


    TextView tvFinalScore;
    int score = 0;
    int bestScore = 0;
    Button btnStartNewGame;
    String bestScoreString;
    Button btnShare;
    Button rateStarBtn;
    StringBuffer stringBuffer;
    Button btnRevive;
    ImageView ivRevive;
    TextView tvBestScore;
    TextView tvRevive;
    private RewardedVideoAd mRewardedVideoAd;
    private NativeExpressAdView nativeExpressAdView;
    private static final String FILE_NAME = "best_score_multiplication_table_quiz.txt";
    boolean revive = false;
    private static final String TAG = "MainActivity";
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        getSupportActionBar().hide();


        btnRevive = (Button) findViewById(R.id.btnrevive);
        MobileAds.initialize(this, "ca-app-pub-7775472521601802~5091426220");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        rateStarBtn = (Button)findViewById(R.id.rate_star_btn);
        tvBestScore = (TextView)findViewById(R.id.tvBestScore);


        mAdView = findViewById(R.id.adView);
       AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        copyReviveFromPrevActivity();
        copyScoreFromPrevActivity();

        if (!revive) {
            loadRewardedVideoAd();
           btnRevive.setOnClickListener(new View.OnClickListener() {
               @Override
                public void onClick(View view) {
                    if (mRewardedVideoAd.isLoaded()) {
                        mRewardedVideoAd.show();
                        revive = true;
                    }
               }
            });
        }
         else {
           btnRevive.setVisibility(View.INVISIBLE);
            tvRevive.setVisibility(View.INVISIBLE);
       }








        displayFinalScore();
        share();
        startNewGame();
        getBestScore();







        if (bestScore< score){
            saveBestScore();
            tvBestScore.setText(Integer.toString(score));
        }
        else{
            tvBestScore.setText(Integer.toString(bestScore));

        }

        rateStarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "https://play.google.com/store/apps/details?id=com.dan.kaftan.mathgame")));

                }
            }
        });






    }

    // set the text of the final score
    public void displayFinalScore() {

        tvFinalScore = (TextView) findViewById(R.id.tvFinalScore);
        tvFinalScore.setText(Integer.toString(score));
    }


// share app

    public void share() {

        btnShare = (Button) findViewById(R.id.btnshare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Amazing math game for first graders: https://play.google.com/store/apps/details?id=com.dan.kaftan.mathgame";
                String shareSub = "You might like this app:";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "share using"));

            }
        });


    }


    //start new game


    public void startNewGame() {
        btnStartNewGame = (Button) findViewById(R.id.btnStartNewGame);
        btnStartNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(EndGame.this, Game.class);
                i.putExtra("revive", false);
                startActivity(i);
            }

        });


    }

    // reward video ad



    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-7775472521601802/7600355285",
                new AdRequest.Builder().build());
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        System.out.println("onRewardedVideoAdLoaded");

    }

    @Override
    public void onRewardedVideoAdOpened() {
        System.out.println("onRewardedVideoAdOpened");

    }

    @Override
    public void onRewardedVideoStarted() {
        System.out.println("onRewardedVideoStarted");

    }

    @Override
    public void onRewardedVideoAdClosed() {
        System.out.println("onRewardedVideoAdClosed");
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        System.out.println("onRewarded");


    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        System.out.println("onRewardedVideoAdLeftApplication");

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        System.out.println("onRewardedVideoAdFailedToLoad");

    }

    @Override
    public void onRewardedVideoCompleted() {
        System.out.println("onRewardedVideoCompleted");

        Intent i = new Intent(EndGame.this, Game.class);
        i.putExtra("revive", revive);
        i.putExtra("score", score);
        startActivity(i);
    }

   @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        revive=false;
        super.onDestroy();


    }


    public int getScore() {
        return score;
    }

    public boolean isRevive() {
        return revive;
    }


    public void copyReviveFromPrevActivity() {
        Intent reviveIntent = getIntent(); // gets the previously created intent
        revive = reviveIntent.getBooleanExtra("revive", false);
    }

    public void copyScoreFromPrevActivity() {
        Intent reviveIntent = getIntent(); // gets the previously created intent
        score = reviveIntent.getIntExtra("score", 0);
    }







    private void saveBestScore(){

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(Integer.toString(score).getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null){

                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    private void getBestScore(){
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null){

                sb.append(text);
                bestScore = Integer.parseInt(sb.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fis != null){

                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }







}