package com.example.lib;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

public class Line {
    Vector<String> words;
    public Line() {
        words = new Vector<String>();
    }
    public Line(String s) {
        String[] arr = s.toLowerCase().replaceAll("[\".,!?]", " ").trim().split("\\s+");
        words = new Vector<String>(Arrays.asList(arr));
    }
    public Vector<Line> getRotations() {
        Vector<Line> output = new Vector<Line>();
        for (Integer i = 0; i < words.size(); i++) {
            Line rotation = new Line();
            for (Integer j = i; j < words.size(); j++) {
                rotation.words.add(this.words.get(j));
            }
            for (Integer j = 0; j < i; j++) {
                rotation.words.add(this.words.get(j));
            }
            output.add(rotation);
        }
        return output;
    }
    public String toString() {
        return String.join(" ", words);
    }
    public void filter(Vector<String> stopWords) {
        Iterator<String> i = words.iterator();
        while (i.hasNext()) {
            String word = i.next();
            Iterator<String> j = stopWords.iterator();
            while (j.hasNext()) {
                String stopWord = j.next();
                if (word.equalsIgnoreCase(stopWord)) {
                    i.remove();
                    break;
                }
            }
        }
    }
}
