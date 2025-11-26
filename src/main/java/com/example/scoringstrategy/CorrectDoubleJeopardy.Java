package com.example.scoringstrategy;

public class CorrectDoubleJeopardy implements IScoreStrategy{
    @Override
    public int calculateScore(int questionValue){
        return questionValue * 2;
    }
    
}
