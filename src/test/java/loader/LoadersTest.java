package com.example.loaderstrategy;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.example.model.Category;

public class LoadersTest {
    @Test
    public void testCSV() throws Exception {
        URL r = getClass().getClassLoader().getResource("sample_game_CSV.csv");
        assertNotNull(r);
        String path = Paths.get(r.toURI()).toString();
        LoadCSV l = new LoadCSV();
        Map<String, Category> m = l.loadQuestions(path);
        assertTrue(m.containsKey("Variables & Data Types"));
    }
    @Test
    public void testJSON() throws Exception {
        URL r = getClass().getClassLoader().getResource("sample_game_JSON.json");
        assertNotNull(r);
        String path = Paths.get(r.toURI()).toString();
        LoadJSON l = new LoadJSON();
        Map<String, Category> m = l.loadQuestions(path);
        assertTrue(m.containsKey("Variables & Data Types"));
    }
    @Test
    public void testXML() throws Exception {
        URL r = getClass().getClassLoader().getResource("sample_game_XML.xml");
        assertNotNull(r);
        String path = Paths.get(r.toURI()).toString();
        LoadXML l = new LoadXML();
        Map<String, Category> m = l.loadQuestions(path);
        assertTrue(m.containsKey("Variables & Data Types"));
    }
}