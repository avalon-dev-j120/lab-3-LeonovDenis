package ru.avalon.java.dev.j120.labs;

import javax.swing.JFrame;

import ru.avalon.java.dev.j120.labs.windows.Chooser;

public class Application {

    public static void main(String[] args) {

        JFrame window = new Chooser();
        window.setVisible(true);
    }

}