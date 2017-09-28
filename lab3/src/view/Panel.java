package view;

import control.Control;


import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Panel extends JPanel implements Observer{
    private final int SQUARE_SIZE = 150;
    private final Color MY_COLOR = Color.BLUE;
    private final Color SQUARE_COLOR = Color.BLACK;
    private final Color OTHER_COLOR = Color.RED;
    private final Color BACKGROUND_COLOR = Color.WHITE;
    private Control control;
    private int width = 3*SQUARE_SIZE+1;
    private int height = 3*SQUARE_SIZE+1;

    public Panel(){
        Dimension d = new Dimension(width, height);
        this.setMinimumSize(d);
        this.setPreferredSize(d);
        this.setBackground(BACKGROUND_COLOR);

    }
    public void setControl(Control c){
        control = c;
        c.addObserver(this);
    }

    public void update(Observable o, Object arg) {
        this.repaint();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 3; y++){
                if(control.locationStatus(x,y).equals("EMPTY")){
                    g.setColor(SQUARE_COLOR);
                    g.drawRect(SQUARE_SIZE*x, SQUARE_SIZE*y, SQUARE_SIZE, SQUARE_SIZE);
                }
                if(control.locationStatus(x,y).equals("ME")){
                    g.setColor(MY_COLOR);
                    g.fillRect(SQUARE_SIZE*x, SQUARE_SIZE*y, SQUARE_SIZE, SQUARE_SIZE);
                }
                if(control.locationStatus(x,y).equals("OTHER")){
                    g.setColor(OTHER_COLOR);
                    g.fillRect(SQUARE_SIZE*x, SQUARE_SIZE*y, SQUARE_SIZE, SQUARE_SIZE);
                }

                }
        }
    }
}
