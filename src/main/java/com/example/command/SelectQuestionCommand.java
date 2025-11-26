package com.example.command;

import com.example.core.Game;

public class SelectQuestionCommand implements ICommand {
    private final Game game;
    private final String category;
    private final int value;

    public SelectQuestionCommand(Game game, String category, int value) {
        this.game = game;
        this.category = category;
        this.value = value;
    }

    @Override
    public void execute() {
        game.processSelection(category, value);
    }
}

