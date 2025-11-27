package com.example.model;

public class Player{

    private final String id;
    private int score = 0;

    public Player(String id){
        this.id = id;
    }

    public String getId(){ 
        return id; 
    }

    public int getScore(){
        return score;
    }
    
    public void addScore(int delta){ 
        score += delta; 
    }

}

