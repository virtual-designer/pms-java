package org.nsu.cse215.labgroup3.pms;

public class Main {
    public static void main(String[] args)  {
        try {
            Application.launch(org.nsu.cse215.labgroup3.pms.Application.class, args);
        } catch (Exception e) {
            System.err.println("An exception has been thrown");
            System.err.println("The stack trace is printed below.");
            e.printStackTrace(System.err);
        }
    }
}
