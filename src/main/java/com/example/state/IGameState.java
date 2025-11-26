package com.example.state;
import com.example.core.Game;

public interface IGameState {
    void start(Game game);
    void end(Game game);
    void processSelection(Game game, String category, int value);
    void processAnswer(Game game, String answer);
}
