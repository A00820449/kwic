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
            System.out.println(v.get(i));
        }
    }

    public void run() {
        if (isFile) {
            while(scanner.hasNextLine()) {
                currLine = scanner.nextLine();
                lines.add(new Line(currLine.trim().toLowerCase()));            
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
