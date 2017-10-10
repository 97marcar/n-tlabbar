package control;

/**
 *
 * @author Marcus Carlsson
 * @since 2017-09-24
 * @version 1.0
 */

import model.Model;
import view.View;

import java.util.Observable;
import java.util.Observer;

public class Control extends Observable implements  Observer{
    private View view;
    private Model model;

    /**
     * Creates a controller ands sets itself to control the view and model
     * @param v view to control
     * @param m model to control
     */
    public Control(View v, Model m){
        this.view = v;
        this.model = m;
        view.setControl(this);
        model.addObserver(this);
        model.connect();

    }

    /**
     * Make the model check for servers
     */
    public void findServer(){
        model.send();
    }

    /**
     * Make the model restart the game
     */
    public void restart(){
        model.restart();
    }

    /**
     * Make the model disconnect from the game
     */
    public void disconnect(){
        model.disconnect();
    }

    /**
     * Make the model to try and make a move on a specific position
     * @param x position on the x-axis to try and make a move
     * @param y position on the y-axis to try and make a move
     */
    public void move(int x, int y){
        model.move(x, y);
    }

    /**
     * Checks a position in the game to see what it contains
     * @param x position on the x-axis to check
     * @param y position on the y-axis to check
     * @return what the position contains (ME OTHER or EMPTY)
     */
    public String locationStatus(int x, int y){
        return(model.grid[x][y]);
    }

    /**
     * Updates the graphical game field
     * @param o Observable
     * @param arg Object
     */
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }
}
