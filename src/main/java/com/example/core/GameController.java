package com.example.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.example.loaderstrategy.QuestionLoader;
import com.example.model.Category;
import com.example.model.Player;
import com.example.model.Question;
import com.example.state.GameOnState;
import com.example.utilities.ProcessLogger;
import com.example.utilities.ReportGenerator;

public class GameController {

    private final Game game;
    private final ProcessLogger logger;
    private QuestionLoader loader;

    public GameController(Game game, ProcessLogger logger) {
        this.game = game;
        this.logger = logger;
    }

    public void setLoader(QuestionLoader loader) {
        this.loader = loader;
    }

    /* ================= LOAD BOARD ================= */

    public void loadBoardFromResource(String path) throws Exception {
        Map<String, Category> board = loader.loadQuestions(path);
        game.setBoard(board);

        logger.logSystem("Load File");
        logger.logSystem("File Loaded Successfully");
    }

    /* ================= PLAYER SETUP ================= */

    public void setupPlayers(int count, Scanner sc) {
        logger.logSystem("Select Player Count");

        for (int i = 1; i <= count; i++) {
            System.out.print("Enter player " + i + " name: ");
            String name = sc.nextLine().trim();
            if (name.isBlank()) {
                name = "P" + i;
            }
            game.addPlayer(new Player(name));
        }

        game.setState(new GameOnState());
    }

    /* ================= MAIN LOOP ================= */

    public void startGameLoop(Scanner sc) {
        game.startGame();

        while (!game.allQuestionsUsed() && !game.isEnded()) {
            Player current = game.getCurrentPlayer();
            System.out.println("\n--- " + current.getId() + " (Score: " + current.getScore() + ") ---");

            List<String> categories = new ArrayList<>(game.getBoard().keySet());
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ") " + categories.get(i));
            }

            System.out.print("Choose category number (or Q to quit): ");
            String categoryInput = sc.nextLine().trim();

            if (categoryInput.equalsIgnoreCase("Q")) {
                game.endGame();
                return;
            }

            int catIndex;
            try {
                catIndex = Integer.parseInt(categoryInput) - 1;
            } catch (Exception e) {
                System.out.println("Invalid input.");
                continue;
            }

            if (catIndex < 0 || catIndex >= categories.size()) {
                System.out.println("Invalid category number.");
                continue;
            }

            String categoryName = categories.get(catIndex);
            Category category = game.getBoard().get(categoryName);

            int value = chooseValue(category, sc);
            game.processSelection(categoryName, value);

            Question question = game.getCurrentQuestion();
            if (question == null) {
                System.out.println("No question available.");
                continue;
            }

            System.out.println("\nQuestion: " + question.getText());
            System.out.println("A) " + question.getOptionA());
            System.out.println("B) " + question.getOptionB());
            System.out.println("C) " + question.getOptionC());
            System.out.println("D) " + question.getOptionD());

            System.out.print("Answer (A/B/C/D or text): ");
            String answer = sc.nextLine().trim();

            game.processAnswer(answer);

            System.out.println("\nScores:");
            game.getPlayers().forEach(p ->
                    System.out.println(p.getId() + ": " + p.getScore())
            );
        }
    }

    private int chooseValue(Category category, Scanner sc) {
        List<Integer> values = category.getAvailableValues();

        if (values.isEmpty()) {
            return 0;
        }

        System.out.println("Available values: " + values.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ")));

        System.out.print("Choose value: ");
        try {
            int v = Integer.parseInt(sc.nextLine().trim());
            if (values.contains(v)) {
                return v;
            }
        } catch (Exception ignored) {}

        System.out.println("Invalid value, using lowest available.");
        return values.get(0);
    }

    /* ================= FINISH ================= */

    public void finishGame(String reportPath, String eventLogPath) throws Exception {
        logger.logSystem("Generate Report");

        ReportGenerator rg = new ReportGenerator();
        rg.generateTextReport(
                reportPath,
                logger.getCaseId(),
                game.getPlayers(),
                logger.getEvents()
        );

        logger.writeCsv(eventLogPath);
        logger.logSystem("Generate Event Log");
    }
}