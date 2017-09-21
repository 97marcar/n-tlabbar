package com.labb2.view;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Panel extends JPanel{

    public Panel(){
        Dimension d = new Dimension(400, 200 );
        this.setMinimumSize(d);
        this.setPreferredSize(d);
        this.setBackground(Color.cyan);
        this.setLayout(new GridBagLayout());

    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);

    }
}
