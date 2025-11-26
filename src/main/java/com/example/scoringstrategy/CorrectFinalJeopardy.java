package com.example.scoringstrategy;

public class CorrectFinalJeopardy implements IScoreStrategy{
    @Override
    public int calculateScore(int questionValue){
        return questionValue; 
    }
}
