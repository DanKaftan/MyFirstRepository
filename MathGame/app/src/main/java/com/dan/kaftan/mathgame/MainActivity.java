package com.dan.kaftan.mathgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity{

    Button settingsBtn;
    Button muteBtn;
    Button levelBtn;
    int i = 0;
    private static final String TAG = "MainActivity";
    private AdView mAdView;
    MediaPlayer mainActivityBackgroud;
    boolean isVisible =true;
    boolean mute = false;
    boolean isLevel = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        settingsBtn = (Button)findViewById(R.id.settings_btn);
        muteBtn = (Button)findViewById(R.id.mute_btn);
        levelBtn = (Button)findViewById(R.id.lv_btn);




        mainActivityBackgroud= MediaPlayer.create(MainActivity.this,R.raw.a);
        mainActivityBackgroud.setLooping(true);
        mainActivityBackgroud.start();


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        makeFile();

    }

    public void startGameClick(View view){
        mainActivityBackgroud.pause();
        Intent i = new Intent(MainActivity.this, Game.class);
        i.putExtra("mute",mute);
        startActivity(i);

    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isVisible = hasFocus;
        if (!isVisible){
            mainActivityBackgroud.pause();
        }
        else {
            mainActivityBackgroud.start();
        }


    }
    public void settingsClick(View view){
        mainActivityBackgroud.pause();
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);

    }


    public void muteOnClick(View view) {

        if (!mute) {
            mute = true;
            muteBtn.setBackgroundResource(R.drawable.mute_on_btn);
            mainActivityBackgroud.pause();
        } else {
            mute = false;
            muteBtn.setBackgroundResource(R.drawable.mute_off_btn);
            mainActivityBackgroud.start();


        }

    }

    public void levelOnClick(View view) {

        isLevel = true;

        mainActivityBackgroud.pause();
        Intent i = new Intent(MainActivity.this, Game.class);
        i.putExtra("mute",mute);
        i.putExtra("isLevel",isLevel);
        startActivity(i);
    }


    private void makeFile() {

        File file = new File(getApplicationContext().getFilesDir(),"level");
        if (!file.exists()) {


            FileOutputStream fos = null;


            try {
                fos = openFileOutput("level", MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {

                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }



}


