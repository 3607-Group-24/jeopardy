package com.example.observer;

import java.util.List;

import com.example.model.GameEvent;
import com.example.model.Player;

public class ScoreBoard implements IGameObserver {

    private final List<Player> players;

    public ScoreBoard(List<Player> players) { this.players = players; }


    @Override
    public void update(GameEvent event) {
        System.out.println("[EVENT] " + event.getActivity() + " | " + event.getPlayerId());
    }

    public void show() {
        System.out.println("----- SCOREBOARD -----");
        for (Player p : players) System.out.println(p.getId() + ": $" + p.getScore());
        System.out.println("----------------------");
    }
    
}