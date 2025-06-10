package com.resume.matcher.resumematchermain.resume.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeywordExtractor {

    private static final List<String> TECHNOLOGIES = List.of("Java", "Python", "Spring", "React", "Docker");

    public List<String> extractKeywords(String text) {
        List<String> foundKeywords = new ArrayList<>();
        String[] words = text.split("\\s+");
        for (String word : words) {
            if (TECHNOLOGIES.contains(word)) {
                foundKeywords.add(word);
            }
        }
        return foundKeywords;
    }
}
