package com.labb2.controller;

import com.labb2.model.Model;
import com.labb2.view.GUI;

/**
 * Creates a controller that connects the GUI to the Model
 * @author Marcus Carlsson
 * @since 2017-09-14
 * @version 1.0
 */
public class Control {
    private GUI gui;
    private Model model;

    /**
     * The constructor sets the Gui and model that the controller connects and sets their control to this.
     * @param gui GUI
     * @param model Model
     */
    public Control(GUI gui, Model model){
        this.gui = gui;
        this.model = model;
        gui.setControl(this);
        model.setControl(this);

    }

    /**
     *
     * @param city Selected city
     * @param h Time. What hour from 1h back to 23hs ahead
     */
    public void getWeather(int city, int h){

        try {
            model.getWeather(city, h);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the GUI what the weather is at selected hour in selected city
     * @param temp String containing the current temperature
     */
    public void setWeatherLabel(String temp){
        gui.setTempLabel(temp);
    }
}
