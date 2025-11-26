package com.example;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Scanner;

import com.example.core.Game;
import com.example.core.GameController;
import com.example.loaderstrategy.LoaderFactory;
import com.example.loaderstrategy.QuestionLoader;
import com.example.observer.ScoreBoard;
import com.example.utilities.ProcessLogger;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== JEOPARDY (Interactive) ===");

        System.out.println("Choose input format: 1) CSV  2) JSON  3) XML");
        int choice = 1;
        try { choice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception ignored) {}

        String resource = "sample_game_CSV.csv";
        if (choice == 2) resource = "sample_game_JSON.json";
        else if (choice == 3) resource = "sample_game_XML.xml";

        
        ProcessLogger logger = ProcessLogger.getInstance("GAME001");
        Game game = new Game(logger);
        GameController controller = new GameController(game, logger);

        // attach observer
        game.attachObserver(new ScoreBoard(game.getPlayers()));

        // loader factory
        QuestionLoader loader = LoaderFactory.getLoaderForResource(resource);
        controller.setLoader(loader);

        // load resource from main/resources
        try {
            URL res = Main.class.getClassLoader().getResource(resource);
            if (res == null) throw new RuntimeException("Resource not found: " + resource);
            String path = Paths.get(res.toURI()).toString();
            controller.loadBoardFromResource(path);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // players
        System.out.print("Number of players (1-4): ");
        int n = 2;
        try { n = Integer.parseInt(sc.nextLine().trim()); } catch (Exception ignored) {}
        if (n < 1) n = 1; if (n > 4) n = 4;

        controller.setupPlayers(n, sc);

        // start interactive loop
        controller.startGameLoop(sc);

        // finish: generate outputs
        try {
            controller.finishGame("sample_game_report.txt", "game_event_log.csv");
            System.out.println("Report and log written.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}