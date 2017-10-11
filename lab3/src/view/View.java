package view;

import control.Control;

import javax.swing.*;
import java.awt.event.*;

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
    private JButton joinServerButton;
    private JButton disconnectButton;
    private JButton findServerButton;
    private JTextField ipTextField;
    private JSpinner portSpinner;



    /**
     * Creates a GUI for the program containing everything that it needs:
     * buttons and game field.
     */
    public View(){
        this.setTitle("Three in a row");

        panel = new Panel();

        disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Disconnect");
                control.disconnect();
            }
        });

        ipTextField = new JTextField();
        SpinnerModel spinnerModel = new SpinnerNumberModel(6066, 0, 65535, 1);
        portSpinner = new JSpinner(spinnerModel);
        JComponent editor = new JSpinner.NumberEditor(portSpinner, "00000");
        portSpinner.setEditor(editor);

        joinServerButton = new JButton("Join Server");
        joinServerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                control.joinServer(ipTextField.getText(), (int)portSpinner.getValue());
            }
        });
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
        Box serverBox = new Box(BoxLayout.X_AXIS);
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        Box panelBox = new Box(BoxLayout.X_AXIS);
        Box containerBox = new Box(BoxLayout.Y_AXIS);


        serverBox.add(ipTextField);
        serverBox.add(portSpinner);
        serverBox.add(joinServerButton);
        serverBox.add(findServerButton);

        buttonBox.add(restartButton);
        buttonBox.add(disconnectButton);


        panelBox.add(panel);


        containerBox.add(panelBox);
        containerBox.add(serverBox);
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
