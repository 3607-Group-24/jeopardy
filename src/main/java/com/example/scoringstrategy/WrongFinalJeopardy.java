package com.example.scoringstrategy;

public class WrongFinalJeopardy implements IScoreStrategy{
    @Override
    public int calculateScore(int questionValue){
        return -questionValue;
    }
}