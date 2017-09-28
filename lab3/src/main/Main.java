package main;

import control.Control;
import model.Model;
import view.View;

public class Main {
    public static void main(String[] args){
        View view = new View();
        Model model = new Model();
        Control control = new Control(view, model);
    }
}
