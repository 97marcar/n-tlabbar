package com.labb2;

import com.labb2.controller.Control;
import com.labb2.model.Model;
import com.labb2.view.GUI;

public class Main {

    public static void main(String[] args) {

        GUI g = new GUI();
        Model m = new Model();
        Control c = new Control(g, m);
    }
}
