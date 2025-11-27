package com.example.state;

import com.example.core.Game;

public class GameOnState implements IGameState{
    @Override
    public void start(Game game) { 
        //already on 
    }


    @Override
    public void end(Game game) { 
        game.setState(new GameOffState()); 
    
    }

    @Override
    public void processSelection(Game game, String category, int value) {
        // move to playing
        game.setState(new PlayingState());
        game.processSelectionImpl(category, value);
    }


    @Override
    public void processAnswer(Game game, String answer) { 
        //not expected here
    }
}

