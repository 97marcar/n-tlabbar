package com.labb2.controller;

import com.labb2.model.Model;
import com.labb2.view.GUI;

public class Control {
    private GUI gui;
    private Model model;

    public Control(GUI gui, Model model){
        this.gui = gui;
        this.model = model;
        gui.setControl(this);
        model.setControl(this);

    }

    public void getWeather(int city, int h){

        try {
            model.getWeather(city, h);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWeatherLabel(String temp){
        gui.setTempLable(temp);
    }
}
