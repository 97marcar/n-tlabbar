package com.labb2;

import com.labb2.controller.Control;
import com.labb2.model.Model;
import com.labb2.view.GUI;

/**
 * Main class that creates a GUI, Model and Controller
 * @author Marcus Carlsson
 * @since 2017-09-14
 * @version 1.0
 */
public class Main {

    /**
     * Creates the objects
     * @param args no function
     */
    public static void main(String[] args) {

        GUI g = new GUI();
        Model m = new Model();
        Control c = new Control(g, m);
    }
}
