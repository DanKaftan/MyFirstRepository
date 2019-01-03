package com.dan.kaftan.mathgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    Spinner timerSpinner;
    Spinner difficultySpinner;
    int timerSeconds = 10;
    int maxAnswer =10;
    int selectedMaxAnswer = 10;
    int selectedTimerSeconds = 10;
    int score=0;
    boolean revive;
    boolean mute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();


        getSelectedMaxAnswer();
        getSelectedTimerSeconds();
        setTimerSpinner();
        setdifficultySpinner();
    }

    public void setTimerSpinner(){

        timerSpinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<Integer> arraySpinner = new ArrayList(5);

        arraySpinner.add(3);
        arraySpinner.add(5);
        arraySpinner.add(10);
        arraySpinner.add(15);
        arraySpinner.add(20);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timerSpinner.setAdapter(adapter);
        switch (selectedTimerSeconds){

            case 3: timerSpinner.setSelection(0);

                break;
            case 5: timerSpinner.setSelection(1);
                break;
            case 10: timerSpinner.setSelection(2);
                break;
            case 15: timerSpinner.setSelection(3);
                break;
            case 20: timerSpinner.setSelection(4);
                break;
        }

    }


    public void setdifficultySpinner(){

        difficultySpinner = (Spinner) findViewById(R.id.difficulty_spinner);

        ArrayList<String> arraySpinner = new ArrayList(5);

        arraySpinner.add("up to 5");
        arraySpinner.add("up to 10");
        arraySpinner.add("up to 20");
        arraySpinner.add("up to 50");
        arraySpinner.add("up to 100");
        arraySpinner.add("up to 1000");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);
        switch (selectedMaxAnswer){
            case 5: timerSpinner.setSelection(0);

                break;
            case 10: difficultySpinner.setSelection(1);
                break;
            case 20: difficultySpinner.setSelection(2);
                break;
            case 50: difficultySpinner.setSelection(3);
                break;
            case 100: difficultySpinner.setSelection(4);
                break;
            case 1000: difficultySpinner.setSelection(5);
                break;
        }

    }

    private void setMaxAnswer(){
        switch (difficultySpinner.getSelectedItem().toString()){

            case"up to 5": maxAnswer = 5;
            break;
            case"up to 10": maxAnswer = 10;
            break;
            case"up to 20": maxAnswer = 20;
            break;
            case"up to 50": maxAnswer = 50;
            break;
            case"up to 100": maxAnswer =100;
            break;
            case"up to 1000": maxAnswer = 1000;
            break;




        }

    }

    public void confimOnClick(View view){
        setMaxAnswer();
        saveDifficulty();
        timerSeconds=Integer.valueOf(timerSpinner.getSelectedItem().toString());
        saveTimerSeconds();

        Intent intent = getIntent();
        boolean isFromEnd =intent.getBooleanExtra("isFromEnd",false);
        if(isFromEnd){
            getVariableFromPrevActivity();
            System.out.println("came from isFromEnd = true");
            Intent i = new Intent(Settings.this, EndGame.class);
            i.putExtra("score",score);
            i.putExtra("revive",revive);
            i.putExtra("mute", mute);
            startActivity(i);

        }
        else{
            System.out.println("came from isFromEnd = false");
            Intent i = new Intent(Settings.this, MainActivity.class);
            startActivity(i);

        }

    }


    private void saveTimerSeconds(){

        FileOutputStream fos = null;


        try {
            fos = openFileOutput("settings_timer_seconds", MODE_PRIVATE);
            fos.write(Integer.toString(timerSeconds).getBytes());
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

    private void saveDifficulty(){

        FileOutputStream fos = null;


        try {
            fos = openFileOutput("settings_difficulty", MODE_PRIVATE);
            fos.write(Integer.toString(maxAnswer).getBytes());
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


    private void getSelectedMaxAnswer(){
        FileInputStream fis = null;
        try {
            fis = openFileInput("settings_difficulty_mult");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null){

                sb.append(text);
                selectedMaxAnswer = Integer.parseInt(sb.toString());
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

    private void getSelectedTimerSeconds(){
        FileInputStream fis = null;
        try {
            fis = openFileInput("settings_timer_seconds_mult");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null){

                sb.append(text);
                selectedTimerSeconds = Integer.parseInt(sb.toString());
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

    private void getVariableFromPrevActivity(){
        Intent intent = getIntent();
        score = intent.getIntExtra("score",0);
        revive = intent.getBooleanExtra("revive",false);
        mute = intent.getBooleanExtra("mute", false);

    }







}
