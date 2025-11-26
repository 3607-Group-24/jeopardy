package com.example.utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;

import com.example.model.GameEvent;
import com.example.model.Player;

public class ReportGenerator {
    public void generateTextReport(String outPath, String caseId, List<Player> players, List<GameEvent> events) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outPath))) {
            bw.write("JEOPARDY PROGRAMMING GAME REPORT"); 
            bw.newLine();
            bw.write("================================");
            bw.newLine();
            bw.newLine();
            bw.write("Case ID: " + caseId);
            bw.newLine();
            bw.newLine();
            bw.write("Players: " + players.stream().map(Player::getId).collect(Collectors.joining(", ")));
            bw.newLine();
            bw.newLine();
            bw.write("Gameplay Summary:");
            bw.newLine();
            bw.write("-----------------");
            bw.newLine();
            int turn = 1;
            
            for (GameEvent e : events) {
                if ("Answer Question".equalsIgnoreCase(e.getActivity())) {
                    bw.write("Turn " + turn + ": " + e.getPlayerId() + " selected " + e.getCategory() + " for " + e.getQuestionValue() + " pts");
                    bw.newLine();
                    bw.write("Question: (see event log)");
                    bw.newLine();
                    bw.write("Answer: " + e.getAnswerGiven() + " — " + e.getResult() + " (Score after: " + e.getScoreAfter() + ")");
                    bw.newLine();
                    bw.newLine();
                    turn++;
                }
            }

            bw.write("Final Scores:"); bw.newLine();
            for (Player p : players){
                bw.write(p.getId() + ": " + p.getScore());
                bw.newLine(); 
                }
        }
    }

}