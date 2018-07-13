package com.dan.kaftan.mathgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class welcome_screen extends AppCompatActivity {

    ImageView iv;
    TextView tv1;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
getSupportActionBar().hide();
        iv = (ImageView)findViewById(R.id.iv);
        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        tv1.startAnimation(myanim);
        tv2.startAnimation(myanim);
        iv.startAnimation(myanim);
        swichActivity();



    }

    public void swichActivity(){
        final Intent i = new Intent(welcome_screen.this, MainActivity.class);

        Thread timer = new Thread(){
            public void  run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }

}
