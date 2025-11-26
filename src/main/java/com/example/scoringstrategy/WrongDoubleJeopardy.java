package com.example.scoringstrategy;

public class WrongDoubleJeopardy implements IScoreStrategy{
    @Override
    public int calculateScore(int questionValue){
        return -questionValue * 2;
    }
}
