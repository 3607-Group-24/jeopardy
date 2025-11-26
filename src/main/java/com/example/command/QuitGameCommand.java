package com.example.command;

import com.example.core.Game;

public class QuitGameCommand implements ICommand {
    private final Game game;

    public QuitGameCommand(Game game) { 
        this.game = game; 

    }

    @Override
    public void execute() { 
        game.endGame(); 
    }
}

