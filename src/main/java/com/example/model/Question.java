package com.example.model;

public class Question {
    private final String category;
    private final int value;
    private final String text;
    private final String optionA, optionB, optionC, optionD;
    private final String correct; // "A", "B", "C", "D"
    private boolean used = false;

    public Question(String category, int value, String text,
                    String optionA, String optionB, String optionC, String optionD,
                    String correct) {
        this.category = category;
        this.value = value;
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correct = correct;
    }

    public String getCategory() { return category; }
    public int getValue() { return value; }
    public String getText() { return text; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrect() { return correct; }

    public boolean isUsed() { return used; }
    public void markUsed() { used = true; }

    public boolean checkAnswer(String given) {
        if (given == null) return false;
        // Accept "A" or "a" or the full option text (simple)
        String g = given.trim().toUpperCase();
        if (g.equals("A") || g.equals("B") || g.equals("C") || g.equals("D")) {
            return g.equals(correct.toUpperCase());
        }
        // Also allow full option text match
        return given.trim().equalsIgnoreCase(optionA)
            || given.trim().equalsIgnoreCase(optionB)
            || given.trim().equalsIgnoreCase(optionC)
            || given.trim().equalsIgnoreCase(optionD)
            && (correct.equalsIgnoreCase("A") && given.trim().equalsIgnoreCase(optionA)
             || correct.equalsIgnoreCase("B") && given.trim().equalsIgnoreCase(optionB)
             || correct.equalsIgnoreCase("C") && given.trim().equalsIgnoreCase(optionC)
             || correct.equalsIgnoreCase("D") && given.trim().equalsIgnoreCase(optionD));
    }

    @Override
    public String toString() {
        return "[" + category + "] " + text + " ($" + value + ")";
    }
}
