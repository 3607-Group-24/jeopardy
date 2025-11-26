package com.example.scoringstrategy;

public class WrongAnswer implements IScoreStrategy{
    @Override
    public int calculateScore(int questionValue){
        return -questionValue;
    }
}
