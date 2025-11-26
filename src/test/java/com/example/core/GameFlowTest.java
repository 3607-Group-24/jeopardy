package com.example.core;

import java.net.URL;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.example.loaderstrategy.LoadJSON;
import com.example.utilities.ProcessLogger;

public class GameFlowTest {
    @Test
    public void simpleFlow() throws Exception {
        URL r = getClass().getClassLoader().getResource("sample_game_JSON.json");
        assertNotNull(r);
        String path = Paths.get(r.toURI()).toString();

        ProcessLogger logger = ProcessLogger.getInstance("TEST001");
        Game game = new Game(logger);
        GameController controller = new GameController(game, logger);
        controller.setLoader(new LoadJSON());
        controller.loadBoardFromResource(path);

        controller.setupPlayers(2, new java.util.Scanner("Alice\nBob\n"));
        // simulate selection + answer by directly calling impls
        game.processSelection("Variables & Data Types", 100);
        game.processAnswer("A");
        assertFalse(logger.getEvents().isEmpty());
    }
}

