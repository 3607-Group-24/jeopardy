package com.example.state;
import com.example.core.Game;

public class PlayingState implements IGameState{
    @Override
    public void start(Game game) {
         /* already started */ 
        }

    @Override
    public void end(Game game) { 
        game.setState(new GameOffState()); 
    }

    @Override
    public void processSelection(Game game, String category, int value) {
        game.processSelectionImpl(category, value);
    }

    @Override
    public void processAnswer(Game game, String answer) {
        game.processAnswerImpl(answer);
    }
}

