package com.example.loaderstrategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.example.model.Category;
import com.example.model.Question;

public class LoadCSV implements QuestionLoader {
    @Override
    public Map<String, Category> loadQuestions(String filePath) throws Exception {
        Map<String, Category> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }
                String[] parts = line.split("\t");
                if (parts.length < 8) parts = line.split(",(?=(?:[^\"]\"[^\"]\")[^\"]$)", -1);
                if (parts.length < 8) continue;
                String cat = parts[0].trim();
                int val = Integer.parseInt(parts[1].trim());
                String q = parts[2].trim();
                String A = parts[3].trim(), B = parts[4].trim(), C = parts[5].trim(), D = parts[6].trim();
                String correct = parts[7].trim();
                Question question = new Question(cat, val, q, A, B, C, D, correct);
                map.putIfAbsent(cat, new Category(cat));
                map.get(cat).addQuestion(question);
            }
        }
        return map;
    }
}