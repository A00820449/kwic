package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import com.example.lib.Line;

public class App 
{
    Vector<Line> lines;
    Vector<String> stopWords;
    boolean descending;
    private boolean isFile;
    Vector<String> rotations;
    File inputFile;
    Scanner stdinScanner;
    Vector<Integer> inputSkip;
    Vector<Integer> outputSkip;
    String outputFilename;

    public App(File file) {
        lines = new Vector<Line>();
        isFile = true;
        inputFile = file;
        stopWords = new Vector<String>();
        descending = false;
        rotations = new Vector<String>();
        inputSkip = new Vector<Integer>();
        outputSkip = new Vector<Integer>();
        stdinScanner = new Scanner(System.in);
        outputFilename = "output.txt";
    }

    public App(File inFile, File stopFile) {
        this(inFile);
        try {
            Scanner stop = new Scanner(stopFile);
            while (stop.hasNextLine()) {
                stopWords.add(stop.nextLine().toLowerCase().trim());
            }
            stop.close();
            System.out.println("loaded stop file");
        }
        catch (FileNotFoundException e) {
            System.err.println("stop file not found...");
        }

    }

    public App(File inFile, File stopFile, String outputFilename) {
        this(inFile, stopFile);
        this.outputFilename = outputFilename;
    }


    public boolean isFile() {
        return isFile;
    }

    void generateRotations() {
        Vector<String> output = new Vector<String>();
        Iterator<Line> linesIt = lines.iterator();
        while (linesIt.hasNext()) {
            Vector<Line> lineRotations = linesIt.next().getRotations();
            Iterator<Line> lineRotationIt = lineRotations.iterator();
            while (lineRotationIt.hasNext()) {
                output.add(lineRotationIt.next().toString());
            }
        }
        rotations = output;
    }

    void printRotations() {
        System.out.println("OUTPUT:");
        Iterator<String> it = rotations.iterator();
        for (Integer i = 0; it.hasNext(); i++) {
            System.out.println(i.toString() + " " + it.next());
        }
    }

    void printLines() {
        System.out.println("INPUT:");
        Iterator<Line> it = lines.iterator();
        for (Integer i = 0; it.hasNext(); i++) {
            System.out.println(i.toString() + " " + it.next().toString());
        }
    }

    void askForOrder() {
        System.out.println("do you want the results in ascending order? [Y/n]:");
        String answer = stdinScanner.nextLine().trim().toLowerCase();
        if (answer.length() > 0 && answer.charAt(0) == 'n') {
            descending = true;
        }
        else {
            descending = false;
        }
    }

    void fillLinesFile(Scanner scanner) {
        String currLine = "";
        while(scanner.hasNextLine()) {
            currLine = scanner.nextLine().trim().toLowerCase();
            Line newLine = new Line(currLine);
            newLine.filter(stopWords);
            lines.add(newLine);        
        }
    }

    void fillLinesStdin() {
        String currLine = "";
        System.out.println("write lines and end with 'stop'");
        currLine = stdinScanner.nextLine().trim().toLowerCase();
        while(!currLine.equalsIgnoreCase("stop")) {
            Line newLine = new Line(currLine);
            newLine.filter(stopWords);
            lines.add(newLine);
            currLine = stdinScanner.nextLine().trim().toLowerCase();
        }
    }

    void fillLines() {
        Scanner inputScanner;

        isFile = true;
        try {
            inputScanner = new Scanner(inputFile);
            fillLinesFile(inputScanner);
            inputScanner.close();

        }
        catch (FileNotFoundException e) {
            isFile = false;
            System.err.println("input file not found, defaulting to stdin...");
            fillLinesStdin();
        }
    }

    void sortRotations() {
        Collections.sort(rotations);
        if (descending) {
            Collections.reverse(rotations);
        }
    }

    void askForSkipRotations() {
        System.out.println("Enter the lines from the output you wish to skip (separated by whitespace):");
        String input = stdinScanner.nextLine().trim();

        if (input.isBlank()) {
            return;
        }

        Vector<Integer> output = new Vector<Integer>();
        String[] inputNums = input.split("\\s+");

        for (String num: inputNums) {
            try {
                Integer i = Integer.parseInt(num);
                output.add(i);
            }
            catch (NumberFormatException e) {
                System.err.println("'" + num + "'' is not a number, skipping...");
            }
        }

        outputSkip = output;
    }

    void askForSkipInput() {
        System.out.println("Enter the lines from the input you wish to skip (separated by whitespace):");
        String input = stdinScanner.nextLine().trim();

        if (input.isBlank()) {
            return;
        }

        Vector<Integer> output = new Vector<Integer>();
        String[] inputNums = input.split("\\s+");


        for (String num: inputNums) {
            try {
                Integer i = Integer.parseInt(num);
                output.add(i);
            }
            catch (NumberFormatException e) {
                System.err.println("'" + num + "'' is not a number, skipping...");
            }
        }

        inputSkip = output;
    }


    void filterInput() {
        if (inputSkip.size() == 0) {
            return;
        }

        Vector<Line> output = new Vector<Line>();
        Iterator<Line> inputIt = lines.iterator();

        for (Integer i = 0; inputIt.hasNext(); i++) {
            Line line = inputIt.next();
            Iterator<Integer> skipIt = inputSkip.iterator();
            boolean skip = false;
            while (skipIt.hasNext()) {
                if (skipIt.next() == i) {
                    skip = true;
                    break;
                }
            }
            if (!skip) {
                output.add(line);
            }
        }

        lines = output;
    }

    void filterRotations() {
        if (outputSkip.size() == 0) {
            return;
        }

        Vector<String> output = new Vector<String>();
        Iterator<String> rotationsIt = rotations.iterator();

        for (Integer i = 0; rotationsIt.hasNext(); i++) {
            String rotation = rotationsIt.next();
            Iterator<Integer> skipIt = outputSkip.iterator();
            boolean skip = false;
            while (skipIt.hasNext()) {
                if (skipIt.next() == i) {
                    skip = true;
                    break;
                }
            }
            if (!skip) {
                output.add(rotation);
            }
        }

        rotations = output;
    }

    void saveOutputFile() {
        try {
            FileWriter outputfile = new FileWriter(outputFilename);
            Iterator<String> it = rotations.iterator();
            while (it.hasNext()) {
                outputfile.write(it.next());
                outputfile.write('\n');
            }
            outputfile.close();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
        fillLines();
        printLines();
        
        askForSkipInput();
        filterInput();

        askForOrder();
        generateRotations();
        sortRotations();
        printRotations();
        
        askForSkipRotations();
        filterRotations();
        printRotations();

        saveOutputFile();
    }
}
