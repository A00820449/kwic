package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

import com.example.lib.Line;

/**
 * Hello world!
 *
 */
public class App 
{
    Vector<Line> lines;
    Scanner scanner;
    String currLine;
    Vector<String> stopWords;
    private boolean isFile;
    public App(File file) {
        lines = new Vector<Line>();
        isFile = true;
        try {
            scanner = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            System.err.println("file not found, defaulting to stdin...");
            isFile = false;
            scanner = new Scanner(System.in);
        }
        currLine = "";
        stopWords = new Vector<String>();
    }

    public App(File inFile, File stopFile) {
        this(inFile);
        try {
            Scanner stop = new Scanner(stopFile);
            while (stop.hasNextLine()) {
                stopWords.add(stop.nextLine().toLowerCase().trim());
            }
            stop.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("Stop file not found...");
        }

    }

    Vector<String> generateRotations() {
        Vector<String> output = new Vector<String>();
        for (Integer i = 0; i < lines.size(); i++) {
            Vector<Line> v = lines.get(i).getRotations();
            for (Integer j = 0; j < v.size(); j++) {
                output.add(v.get(j).toString());
            }
        }
        return output;
    }

    void printLines(Vector<String> v) {
        for (Integer i = 0; i < v.size(); i++) {
            System.out.println(i.toString() + " " + v.get(i));
        }
    }

    public void run() {
        if (isFile) {
            while(scanner.hasNextLine()) {
                currLine = scanner.nextLine().trim().toLowerCase();
                Line newLine = new Line(currLine);
                newLine.filter(stopWords);
                lines.add(newLine);        
            }
        }
        else {
            System.out.println("write lines and end with 'stop'");
            currLine = scanner.nextLine();
            while(!currLine.trim().equalsIgnoreCase("stop")) {
                lines.add(new Line(currLine.trim().toLowerCase()));   
                currLine = scanner.nextLine();
            }
        }
        scanner.close();

        Vector<String> v = generateRotations();
        Collections.sort(v);

        printLines(v);
    }
}
