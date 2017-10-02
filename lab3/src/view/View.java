package view;

import control.Control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class View extends JFrame implements Observer{
    private Control control;
    private Panel panel;
    private JButton restartButton;
    private JButton disconnectButton;
    private JLabel messageLabel;



    public View(){
        this.setTitle("Three in a row");

        panel = new Panel();

        restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Restart");
                control.restart();
            }
        });
        disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Disconnect");
            }
        });

        messageLabel = new JLabel("Welcome");

        setLayout();
        this.setResizable(false);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);


    }
    private void setLayout(){
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        Box messageBox = new Box(BoxLayout.X_AXIS);
        Box panelBox = new Box(BoxLayout.X_AXIS);
        Box containerBox = new Box(BoxLayout.Y_AXIS);

        buttonBox.add(restartButton);
        buttonBox.add(disconnectButton);

        messageBox.add(messageLabel);

        panelBox.add(panel);


        containerBox.add(panelBox);
        containerBox.add(buttonBox);
        containerBox.add(messageBox);
        this.add(containerBox);
    }

    public void setControl(Control c){
        control = c;
        panel.setControl(c);
        c.addObserver(this);
    }

    public void update(Observable o, Object arg) {

    }
}
