package com.example.scoringstrategy;

public class CorrectAnswer implements IScoreStrategy{
    @Override
    public int calculateScore(int questionValue){
        return questionValue;
    }
}
