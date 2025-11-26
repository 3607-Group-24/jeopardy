package com.example.command;

import com.example.core.Game;

public class AnswerQuestionCommand implements ICommand {
    private final Game game;
    private final String answer;

    public AnswerQuestionCommand(Game game, String answer) {
        this.game = game;
        this.answer = answer;
    }

    @Override
    public void execute() {
        game.processAnswer(answer);
    }
}

