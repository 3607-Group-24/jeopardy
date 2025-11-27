package com.example.model;

public class Question{
    
    private final String category;
    private final int value;
    private final String text;
    private final String optionA, optionB, optionC, optionD;
    private final String correct; 
    private boolean used = false;

    public Question(String category, int value, String text, String optionA, String optionB, String optionC, String optionD, String correct){
        this.category = category;
        this.value = value;
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correct = correct;
    }

    public String getCategory(){
        return category;
    }

    public int getValue(){
        return value;
    }

    public String getText(){
        return text; 
    }

    public String getOptionA(){
        return optionA; 
    }

    public String getOptionB(){
        return optionB; 
    }

    public String getOptionC(){
        return optionC; 
    }

    public String getOptionD(){
        return optionD; 
    }

    public String getCorrect(){
        return correct; 
    }

    public boolean isUsed(){
        return used; 
    }

    public void markUsed(){
        used = true; 
    }

    public boolean checkAnswer(String given){
        
        if(given == null) return false;
        
        String g = given.trim();
        
        if(g.equalsIgnoreCase("A")) return "A".equalsIgnoreCase(correct);
        if(g.equalsIgnoreCase("B")) return "B".equalsIgnoreCase(correct);
        if(g.equalsIgnoreCase("C")) return "C".equalsIgnoreCase(correct);
        if(g.equalsIgnoreCase("D")) return "D".equalsIgnoreCase(correct);
       
        return g.equalsIgnoreCase(optionA) || g.equalsIgnoreCase(optionB) || g.equalsIgnoreCase(optionC) || g.equalsIgnoreCase(optionD) || g.equalsIgnoreCase(correct);
    }

    @Override
    public String toString(){
        return "[" + category + "] " + text + " ($" + value + ")";
    }

}
