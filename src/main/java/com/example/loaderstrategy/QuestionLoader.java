package com.example.loaderstrategy;

import java.util.Map;

import com.example.model.Category;

public interface QuestionLoader {
    Map<String, Category> loadQuestions(String filePath) throws Exception;
}
