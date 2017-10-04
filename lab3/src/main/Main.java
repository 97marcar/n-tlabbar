package main;

import control.Control;
import model.Model;
import view.View;
/**
 *
 * @author Marcus Carlsson
 * @since 2017-09-24
 * @version 1.0
 */
public class Main {
    public static void main(String[] args){
        View view = new View();
        Model model = new Model();
        Control control = new Control(view, model);
    }
}
