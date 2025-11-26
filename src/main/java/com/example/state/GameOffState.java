package com.example.state;
import com.example.core.Game;

public class GameOffState implements IGameState{

    @Override
    public void start(Game game) { 
        game.setState(new GameOnState()); 
        game.getLogger().logSystem("Start Game"); 
    }

    
    @Override
    public void end(Game game) { 
        //already off
    
    }


    @Override
    public void processSelection(Game game, String category, int value) { 
         //not expected here
    
    }


    @Override
    public void processAnswer(Game game, String answer) { 
        //not expected here
    
    }
}

