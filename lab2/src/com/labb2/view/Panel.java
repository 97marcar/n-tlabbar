package com.labb2.view;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Creates a JPanel with some specific settings.
 * @author Marcus Carlsson
 * @since 2017-09-14
 * @version 1.0
 */
public class Panel extends JPanel{

    /**
     * Constructor sets dimensions, background color and layout.
     */
    public Panel(){
        Dimension d = new Dimension(400, 200 );
        this.setMinimumSize(d);
        this.setPreferredSize(d);
        this.setBackground(Color.cyan);
        this.setLayout(new GridBagLayout());

    }



}
