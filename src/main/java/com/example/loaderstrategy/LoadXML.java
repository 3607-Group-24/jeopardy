package com.example.loaderstrategy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.example.model.Category;
import com.example.model.Question;

public class LoadXML implements QuestionLoader {
    @Override
    public Map<String, Category> loadQuestions(String filePath) throws Exception {
        Map<String, Category> map = new HashMap<>();
        File f = new File(filePath);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
        doc.getDocumentElement().normalize();
        NodeList list = doc.getElementsByTagName("QuestionItem");
        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);
            String cat = getText(e, "Category");
            int value = Integer.parseInt(getText(e, "Value"));
            String q = getText(e, "QuestionText");
            Element opts = (Element) e.getElementsByTagName("Options").item(0);
            String A = getText(opts, "OptionA");
            String B = getText(opts, "OptionB");
            String C = getText(opts, "OptionC");
            String D = getText(opts, "OptionD");
            String correct = getText(e, "CorrectAnswer");
            Question question = new Question(cat, value, q, A, B, C, D, correct);
            map.putIfAbsent(cat, new Category(cat));
            map.get(cat).addQuestion(question);
        }
        return map;
    }

    private String getText(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        if (nl == null || nl.getLength() == 0) return "";
        return nl.item(0).getTextContent().trim();
    }
}
