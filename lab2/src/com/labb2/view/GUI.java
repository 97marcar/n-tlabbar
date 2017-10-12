package com.labb2.view;
import com.labb2.controller.Control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * This class creates a Graphical user interface for a Weather application
 * @author Marcus Carlsson
 * @since 2017-09-14
 * @version 1.0
 */

public class GUI extends javax.swing.JFrame{
    private Control control;
    private Panel panel;
    private GridBagConstraints gbc;
    private JLabel areaLabel;
    private JComboBox areaDropDown;
    private JLabel timeLabel;
    private JLabel tempLabel;
    private JLabel currentTempLabel;
    private JSpinner timeSpinner;
    private JButton getWeatherButton;

    /**
     * Constructor that initialize the components when an object is created.
     */
    public GUI(){
        initCompontents();
}


private void initCompontents(){
    this.setTitle("Weather");

    //Creates a Panel object where the components sticks on to
    panel = new Panel();


    this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);

    //Creates the components
    areaLabel = new JLabel("Ort: ");
    String[] cities = {"Skellefteå", "Kåge", "Stockholm", "Luleå"};
    areaDropDown = new JComboBox(cities);
    timeLabel = new JLabel("Klockan: ");
    tempLabel = new JLabel("Temperatur: ");
    currentTempLabel = new JLabel("");

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
        //a method that creates a layout and places the components where they are supposed to
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15,0,0,0);
        setGridPos(0,0);
        panel.add(areaLabel, gbc);

        setGridPos(1,0);
        panel.add(areaDropDown, gbc);

        setGridPos(0,1);
        panel.add(timeLabel, gbc);

        setGridPos(1,1);
        panel.add(timeSpinner, gbc);

        setGridPos(0,2);
        panel.add(tempLabel, gbc);

        setGridPos(1,2);
        panel.add(currentTempLabel, gbc);

        setGridPos(0,4);
        gbc.gridwidth = 2;
        panel.add(getWeatherButton, gbc);

    }

    private void setGridPos(int x, int y){
        //Method to tidy up code
        gbc.gridx = x;
        gbc.gridy = y;
    }

    /**
     * Set the controller that you want to control the GUI
     * @param c Controller
     */
    public void setControl(Control c){
        control = c;
    }


    /**
     * Updates the Temperature label.
     * @param temp A string containing the temperature which you want to display to the user
     */
    public void setTempLabel(String temp){
        currentTempLabel.setText(temp+"°C");
        panel.repaint();
    }

    private class WeatherButton extends AbstractAction{
        //Creates a button

        /**
         * Constructor that creates an AbstractAction button
         * @param label A string containing the text you want the button to display
         */
        public WeatherButton(String label){
            super(label);
        }

        /**
         * Tells the controller to run the getWeather function
         * @param e ActionEvent (when the button is clicked)
         */
        public void actionPerformed(ActionEvent e) {
            control.getWeather(areaDropDown.getSelectedIndex(), (int)timeSpinner.getValue());
        }
    }
}

