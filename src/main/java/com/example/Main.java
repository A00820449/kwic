package com.example;

import java.io.File;

public class Main {
    static App app;

    public static void main( String[] args )
    {   
        String inputfilename = "input.txt";
        String stopfilename = "stop.txt";
        String outputfilename = "output.txt";
        if (args.length > 0) {
            inputfilename = args[0];
        }

        if (args.length > 1) {
            stopfilename = args[1];
        }

        if (args.length > 2) {
            outputfilename = args[2];
        }

        File f = new File(inputfilename);
        File stopFile = new File(stopfilename);
        app = new App(f, stopFile, outputfilename);
        app.run();
    }
}
