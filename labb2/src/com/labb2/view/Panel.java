package com.labb2.view;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Panel extends JPanel implements Observer{

    public Panel(){
        Dimension d = new Dimension(400, 200 );
        this.setMinimumSize(d);
        this.setPreferredSize(d);
        this.setBackground(Color.blue);
        this.setLayout(new GridBagLayout());

    }



    public void update(Observable o, Object arg) {
        this.repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

    }
}
