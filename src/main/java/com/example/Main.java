package com.example;

import java.io.File;

public class Main {
    static App app;

    public static void main( String[] args )
    {   
        String filename = "input.txt";
        if (args.length > 0) {
            filename = args[0];
        }
        File f = new File(filename);
        app = new App(f);
        app.run();
    }
}
