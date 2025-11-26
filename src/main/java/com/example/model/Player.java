package com.example.model;

import com.example.scoringstrategy.IScoreStrategy;

public class Player {
    private final String id; // unique id / name
    private int score = 0;
    private IScoreStrategy strategy;

    public Player(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public int getScore() { return score; }

    public void setStrategy(IScoreStrategy s) { this.strategy = s; }

    public void applyScore(int questionValue) {
        if (strategy == null) {
            return;
        }
        int delta = strategy.calculateScore(questionValue);
        this.score += delta;
    }

    public void adjustScoreRaw(int delta) { this.score += delta; }
}