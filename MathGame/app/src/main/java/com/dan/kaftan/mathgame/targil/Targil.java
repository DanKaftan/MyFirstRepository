package com.dan.kaftan.mathgame.targil;

public interface Targil {

    void setFirstNum (int firstNum);
    int getFirstNum ();

    void setSecondNum (int SecondNum);
    int getSecondNum ();

    void setOperator(String operator);
    String getOperator();

    String getTargilAsString();

    int calc();
}
