package com.dan.kaftan.mathgame;

import android.content.Intent;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.dan.kaftan.mathgame.targil.BankOfTargils;
import com.dan.kaftan.mathgame.targil.Targil;
import com.dan.kaftan.mathgame.targil.TargilAdd;
import com.dan.kaftan.mathgame.targil.TargilMultiply;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.reward.RewardedVideoAd;

public class Game extends AppCompatActivity {

    TextView tv;
    private AdView mAdView;
    List<Integer> answers = new ArrayList<>();
    int fakeAnswer1 = 0;
    int fakeAnswer2 = 0;
    int fakeAnswer3 = 0;
    int trueAnswer = 0;
    TextView tva1;
    TextView tva2;
    TextView tva3;
    TextView tva4;
    TextView timer;
    ImageView iv;
    ImageView hiv1;
    ImageView hiv2;
    ImageView hiv3;
    int invalidationCounter = 0;
    Random rand = new Random();
    int score = 0;
    TextView tvScore;
    boolean answerCheck = false;
    private CountDownTimer mcountDownTimer;
    private CountDownTimer viewResultTimer;
    boolean revive= false;

    int maxResult = Integer.MIN_VALUE;
    int minResult = Integer.MAX_VALUE;


    // for disabling sound
    boolean  isVisible = true;

    // sounds
    MediaPlayer correctSound;
    MediaPlayer falseSound;
    MediaPlayer threeSecondsSound;
    MediaPlayer gameSound;

    boolean gameOver = false;
    private static final String TAG = "MainActivity";



    // this holds the targilim we want to run
    BankOfTargils bankOfTargils = new BankOfTargils();
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();



        tv = (TextView) findViewById(R.id.tv);

        tva1 = (TextView) findViewById(R.id.tva1);
        tva2 = (TextView) findViewById(R.id.tva2);
        tva3 = (TextView) findViewById(R.id.tva3);
        tva4 = (TextView) findViewById(R.id.tva4);
        iv = (ImageView) findViewById(R.id.iv);
        hiv1 = (ImageView) findViewById(R.id.hiv1);
        hiv2 = (ImageView) findViewById(R.id.hiv2);
        hiv3 = (ImageView) findViewById(R.id.hiv3);
        tvScore = (TextView) findViewById(R.id.score);
        mAdView = findViewById(R.id.adView);
        copyReviveFromPrevActivity();
        copyScoreFromPrevActivity(revive);
        tvScore.setText("score: " + Integer.toString(score));
        timer = (TextView) findViewById(R.id.timer);

        initTargilim(10,10,100,false,"x");
     //   initTargilim(9,9,10, true,"+");

        correctSound= MediaPlayer.create(Game.this,R.raw.correct);
        falseSound= MediaPlayer.create(Game.this,R.raw.eror);
        threeSecondsSound = MediaPlayer.create(Game.this,R.raw.three_seconds);
        gameSound= MediaPlayer.create(Game.this,R.raw.game_sound);



        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setGame();

    }


    //  Game setter

    public void setGame() {


        initGameView();

        chooseTargil();

        chooseFakeAnswers();

        chooseLocationForAnswers();

        setTimerForAnswer();

    }




    // choose the location of the true answer
    private void chooseLocationForAnswers() {

        List<TextView> tvaList = Arrays.asList(tva1, tva2, tva3, tva4);
        Collections.shuffle(tvaList);
        for (TextView tva : tvaList) {
            tva.setText(Integer.toString(answers.remove(0)));
        }
    }



// Get fake answers
    private void chooseFakeAnswers() {

        int minFakeResult;
        int maxFakeResult;

        if (trueAnswer-10 < minResult){
            minFakeResult = minResult;
        } else {
            minFakeResult = trueAnswer - 10;
        }

        if (trueAnswer+10 > maxResult){
            maxFakeResult = maxResult;
        } else {
            maxFakeResult = trueAnswer + 10;
        }

        int fakeRange = maxFakeResult-minFakeResult;

        fakeAnswer1 = rand.nextInt(fakeRange) + minFakeResult;
        while (fakeAnswer1 == trueAnswer) {
            fakeAnswer1 = rand.nextInt(fakeRange) + minFakeResult;
        }
        answers.add(fakeAnswer1);


        fakeAnswer2 = rand.nextInt(fakeRange) + minFakeResult;
        while (fakeAnswer2 == trueAnswer || fakeAnswer2 == fakeAnswer1) {
            fakeAnswer2 = rand.nextInt(fakeRange) + minFakeResult;
        }
        answers.add(fakeAnswer2);


        fakeAnswer3 = rand.nextInt(fakeRange) + minFakeResult;
        while (fakeAnswer3 == trueAnswer || fakeAnswer3 == fakeAnswer1 || fakeAnswer3 == fakeAnswer2) {
            fakeAnswer3 = rand.nextInt(fakeRange) + minFakeResult;
        }
        answers.add(fakeAnswer3);
    }






// Get the question and calc the true answer
    @NonNull
    private void chooseTargil() {

        // take random targil
        Targil targil = bankOfTargils.removeRandomTargil();

        // to display it
        tv.setText(targil.getTargilAsString());

        // calc the targil result
        trueAnswer = targil.calc();

        answers.add(trueAnswer);
    }





    // Change the design to the true/false answer view
    private void initGameView() {

        iv.setImageResource(R.drawable.a);
        tva1.setVisibility(View.VISIBLE);
        tva2.setVisibility(View.VISIBLE);
        tva3.setVisibility(View.VISIBLE);
        tva4.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
        tvScore.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
    }




// set all the questions in a list
    private void initTargilim(int maxFirstNum, int maxSecondNum, int maxExpectedResult, boolean resultLimit, String operator) {

        // take targilim form bank of targilim
        List<Targil> targilim = bankOfTargils.getTarglilim();

        // fill it with new targilim
        for (int firstNum = 1 ; firstNum < maxFirstNum; firstNum++){
            for (int secondNum = 1 ; resultLimit? firstNum + secondNum <= maxExpectedResult : secondNum < maxSecondNum ; secondNum++){
                // generat the targil
                Targil targil = newTargil(firstNum,secondNum,operator);
                // add it to bank
                targilim.add(targil);
                // calc it
                int targilResult  = targil.calc();
                // update max result if needed
                if (targilResult > maxResult){
                    maxResult = targilResult;
                }
                // update min result if needed
                if (targilResult < minResult){
                    minResult= targilResult;
                }
            }
        }
    }

    private Targil newTargil(int firstNum, int secondNum, String operator){
        Targil targil;
        switch (operator){
            case "+": targil =  new TargilAdd(firstNum,secondNum,operator);
            break;
            case "x": targil =  new TargilMultiply(firstNum,secondNum,operator);
            break;

            default:throw new UnsupportedOperationException();
        }
        return targil;
    }


    // check the answer right in case of on click

    public void tva1OnClick(View v) throws InterruptedException {
        handleClick(v, tva1, false);
    }

    public void tva2OnClick(View v) throws InterruptedException {
        handleClick(v, tva2, false);
    }

    public void tva3OnClick(View v) throws InterruptedException {
        handleClick(v, tva3, false);
    }

    public void tva4OnClick(View v) throws InterruptedException {
        handleClick(v, tva4, false);
    }






    public void handleClick(View v, TextView tva, boolean timeOut) throws InterruptedException {
        try {

            // first, cancel timer as the user clicked
            if (mcountDownTimer != null) {
                mcountDownTimer.cancel();
            }

            int tvaNum = 0;
            if (tva != null) {
                tvaNum = Integer.parseInt(tva.getText().toString());
            }
            tva1.setVisibility(View.INVISIBLE);
            tva2.setVisibility(View.INVISIBLE);
            tva3.setVisibility(View.INVISIBLE);
            tva4.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.INVISIBLE);
            tvScore.setVisibility(View.INVISIBLE);
            timer.setVisibility(View.INVISIBLE);




            if (!timeOut && tvaNum == trueAnswer) {
                iv.setImageResource(R.drawable.vi);

                // do not disturb with sounds if not visible
                if(isVisible){
                    correctSound.start();
                }
                //   correctSoundEffect(context);
                score = score + 10;
                answerCheck = true;


                //result();
            } else {
                iv.setImageResource(R.drawable.x);

                // do not disturb with sounds if not visible
                if(isVisible){
                    falseSound.start();
                }
                answerCheck = false;
                invalidationCounter = invalidationCounter + 1;
                //     result();

                if (invalidationCounter == 1) {
                    hiv3.setVisibility(View.INVISIBLE);
                }
                if (invalidationCounter == 2) {
                    hiv2.setVisibility(View.INVISIBLE);
                }
                if (invalidationCounter == 3) {
                    hiv1.setVisibility(View.INVISIBLE);
                }
            }
            tvScore.setText("score: " + Integer.toString(score));

            if (invalidationCounter != 3) {
                setTimerForViewResult();
            } else {
                setTimerForGameOver();
            }

        } catch (Exception e) {

        }

    }

    public void gameOver() {

        if (isVisible){
            Intent a = new Intent(Game.this, EndGame.class);
            a.putExtra("score", score);
            a.putExtra("revive", revive);
            startActivity(a);
        } else {
            gameOver = true;
        }
    }

    public void setTimerForAnswer() {
        mcountDownTimer = new CountDownTimer(11000, 1000) { //40000 milli seconds is total time, 1000 milli seconds is time interval
            int timerNum = 10;




            public void onTick(long millisUntilFinished) {
                timer.setText(Integer.toString(timerNum));


                timerNum = timerNum - 1;

               gameSound.start();
               if(isVisible == false){
                   gameSound.pause();
               }

               if(timerNum == 3){
                   threeSecondsSound.start();
                   System.out.println("threeSecondsSound.start()");
               }


            }

            public void onFinish() {
                try {
                    handleClick(null, null, true);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }.start();
    }

    public void setTimerForViewResult() {
        viewResultTimer = new CountDownTimer(2000, 1000) { //40000 milli seconds is total time, 1000 milli seconds is time interval
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                setGame();
            }
        }.start();
    }

    public void setTimerForGameOver() {
        viewResultTimer = new CountDownTimer(2000, 1000) { //40000 milli seconds is total time, 1000 milli seconds is time interval
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                gameOver();

            }
        }.start();
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isVisible = hasFocus;

        gameSound.pause();
        threeSecondsSound.pause();
        if (isVisible && gameOver) {
            gameOver = false;
            gameOver();
        }
    }

    public void copyReviveFromPrevActivity() {
        Intent reviveIntent = getIntent(); // gets the previously created intent
        revive = reviveIntent.getBooleanExtra("revive", false);
    }

    public void copyScoreFromPrevActivity(boolean revive) {
        if(revive){
            Intent reviveIntent = getIntent(); // gets the previously created intent
            score = reviveIntent.getIntExtra("score", 0);
        }


    }




}










