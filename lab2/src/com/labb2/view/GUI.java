package com.labb2.view;
import com.labb2.controller.Control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

public class GUI extends javax.swing.JFrame{
    private Control control;
    private Panel panel;
    private GridBagConstraints gbc;
    private JLabel areaLable;
    private JComboBox areaDropDown;
    private JLabel timeLable;
    private JLabel tempLable;
    private JLabel currentTempLable;
    private JSpinner timeSpinner;
    private JButton getWeatherButton;


    public GUI(){
        initCompontents();
}

private void initCompontents(){
    this.setTitle("Weather");
    panel = new Panel();
    this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);

    areaLable = new JLabel("Ort: ");
    String[] cities = {"Skellefteå", "Kåge", "Stockholm"};
    areaDropDown = new JComboBox(cities);

    timeLable = new JLabel("Timme: ");
    tempLable = new JLabel("Temperatur: ");
    currentTempLable = new JLabel("");


    SpinnerModel model = new SpinnerNumberModel(1, 1, 24, 1);
    timeSpinner = new JSpinner(model);
    JComponent editor = new JSpinner.NumberEditor(timeSpinner, "00");
    timeSpinner.setEditor(editor);
    getWeatherButton = new JButton(new WeatherButton("Hämta väder"));



    setLayout();
    this.setResizable(false);
    this.setContentPane(panel);
    this.pack();
    this.setVisible(true);
}


    private void setLayout(){
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15,0,0,0);
        setGridPos(0,0);
        panel.add(areaLable, gbc);

        setGridPos(1,0);
        panel.add(areaDropDown, gbc);

        setGridPos(0,1);
        panel.add(timeLable, gbc);

        setGridPos(1,1);
        panel.add(timeSpinner, gbc);

        setGridPos(0,2);
        panel.add(tempLable, gbc);

        setGridPos(1,2);
        panel.add(currentTempLable, gbc);


        setGridPos(0,4);
        gbc.gridwidth = 2;
        panel.add(getWeatherButton, gbc);

    }

    private void setGridPos(int x, int y){
        gbc.gridx = x;
        gbc.gridy = y;
    }

    public void setControl(Control c){
        control = c;
    }

    public void setTempLable(String temp){
        currentTempLable.setText(temp+"°C");
        panel.repaint();
    }

    private class WeatherButton extends AbstractAction{
        public WeatherButton(String label){
            super(label);
        }

        public void actionPerformed(ActionEvent e) {
            control.getWeather(areaDropDown.getSelectedIndex(), (int)timeSpinner.getValue());
        }
    }
}

