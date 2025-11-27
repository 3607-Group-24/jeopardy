package com.example.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Category{

    private final String name;
    private final List<Question> questions = new ArrayList<>();

    public Category(String name){ 
        this.name = name; 
    }

    public String getName(){
        return name; 
    }

    public List<Question> getQuestions(){
        return questions; 
    }

    public void addQuestion(Question q){ 
        if (q != null) questions.add(q); 
    }

    public Optional<Question> findByValue(int value){
        return questions.stream().filter(q -> q.getValue() == value && !q.isUsed()).findFirst();
    }

    public boolean allUsed(){
        return questions.stream().allMatch(Question::isUsed); 
    }

    public List<Integer> getAvailableValues(){
        return questions.stream().filter(q -> !q.isUsed()).map(Question::getValue).distinct().sorted().collect(Collectors.toList());
    }
}


