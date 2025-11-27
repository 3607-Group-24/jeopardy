package com.example.loaderstrategy;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.model.Category;
import com.example.model.Question;

public class LoadJSON implements QuestionLoader {
    @Override
    public Map<String, Category> loadQuestions(String filePath) throws Exception {
        String content = Files.readString(Paths.get(filePath)).trim();
        Map<String, Category> map = new HashMap<>();
        if (content.startsWith("[")) content = content.substring(1);
        if (content.endsWith("]")) content = content.substring(0, content.length() - 1);
        String[] objects = content.split("\\},\\s*\\{");
        for (String obj : objects) {
            if (!obj.startsWith("{")) obj = "{" + obj;
            if (!obj.endsWith("}")) obj = obj + "}";
            String cat = extract(obj, "\"Category\"");
            int value = Integer.parseInt(defaultIfEmpty(extract(obj, "\"Value\""), "0"));
            String q = extract(obj, "\"Question\"");
            String options = extractBlock(obj, "\"Options\"\\s*:\\s*\\{", "\\}");
            String A = extract(options, "\"A\"");
            String B = extract(options, "\"B\"");
            String C = extract(options, "\"C\"");
            String D = extract(options, "\"D\"");
            String correct = extract(obj, "\"CorrectAnswer\"");
            Question question = new Question(cat, value, q, A, B, C, D, correct);
            map.putIfAbsent(cat, new Category(cat));
            map.get(cat).addQuestion(question);
        }
        return map;
    }

    private String extract(String src, String key) {
        Pattern p = Pattern.compile(key + "\\s*:\\s*\"([^\"]*)\"");
        Matcher m = p.matcher(src);
        return m.find() ? m.group(1) : "";
    }

    private String extractBlock(String src, String startPattern, String endToken) {
        Pattern p = Pattern.compile(startPattern + "(.+?)\\}");
        Matcher m = p.matcher(src);
        if (m.find()) return m.group(1) + "}";
        return "";
    }

    private String defaultIfEmpty(String s, String def) { return (s == null || s.isBlank()) ? def : s; }
}
