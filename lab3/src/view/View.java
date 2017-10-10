package view;

import control.Control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
/**
 *
 * @author Marcus Carlsson
 * @since 2017-09-24
 * @version 1.0
 */
public class View extends JFrame{
    private Control control;
    private Panel panel;
    private JButton restartButton;
    private JButton disconnectButton;
    private JButton findServerButton;


    /**
     * Creates a GUI for the program containing everything that it needs:
     * buttons and game field.
     */
    public View(){
        this.setTitle("Three in a row");

        panel = new Panel();

        findServerButton = new JButton("Find Servers");
        findServerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                control.findServer();
            }
        });
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
                control.disconnect();
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int mouseXpos = e.getX();
                int mouseYpos = e.getY();
                int[]pos = panel.getGridPosition(mouseXpos,mouseYpos);
                control.move(pos[0], pos[1]);
            }
        });


        setLayout();
        this.setResizable(false);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                control.disconnect();
            }
        });
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);


    }
    private void setLayout(){
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        Box panelBox = new Box(BoxLayout.X_AXIS);
        Box containerBox = new Box(BoxLayout.Y_AXIS);

        buttonBox.add(findServerButton);
        buttonBox.add(restartButton);
        buttonBox.add(disconnectButton);


        panelBox.add(panel);


        containerBox.add(panelBox);
        containerBox.add(buttonBox);
        this.add(containerBox);
    }

    /**
     * sets the control
     * @param c control
     */
    public void setControl(Control c){
        control = c;
        panel.setControl(c);
    }

}
