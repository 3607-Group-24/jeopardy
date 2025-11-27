package com.example.loaderstrategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.example.model.Category;
import com.example.model.Question;

public class LoadCSV implements QuestionLoader {

    @Override
    public Map<String, Category> loadQuestions(String filePath) throws Exception {
        Map<String, Category> map = new HashMap<>();

        Reader reader = null;
        InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
        if (is != null) {
            reader = new InputStreamReader(is);
        } else {
            // fallback to file path (absolute path)
            if (Files.exists(Paths.get(filePath))) {
                reader = new FileReader(filePath);
            } else {
                throw new IllegalArgumentException("CSV file not found: " + filePath);
            }
        }

        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }
                if (line.isBlank()) continue;

                String[] parts = null;

                // try tab-separated first
                if (line.contains("\t")) {
                    parts = line.split("\t", 8);
                } else if (line.contains(",")) {
                    // naive CSV: split on commas, limit to 8 parts (no full CSV parser)
                    parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 8);
                } else {
                    // fallback, split on whitespace into max 8 columns
                    parts = line.split("\\s+", 8);
                }

                if (parts == null || parts.length < 8) {
                    // skip malformed line
                    continue;
                }

                String cat = parts[0].trim();
                // If category itself contains spaces (like "Variables & Data Types") your CSV should quote it,
                // but if not quoted, this fallback may mis-parse; prefer putting resources in proper CSV format.
                int val;
                try {
                    val = Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException nfe) {
                    // skip malformed numeric value
                    continue;
                }
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