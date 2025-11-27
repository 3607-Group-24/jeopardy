package com.example.loaderstrategy;

public class LoaderFactory {
    public static QuestionLoader getLoaderForResource(String resourceName) {
        if (resourceName.endsWith(".json")) return new LoadJSON();
        if (resourceName.endsWith(".xml")) return new LoadXML();
        return new LoadCSV();
    }
}

