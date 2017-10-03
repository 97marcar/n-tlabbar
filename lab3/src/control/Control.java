package control;



import model.Model;
import view.View;

import java.util.Observable;
import java.util.Observer;

public class Control extends Observable implements  Observer{
    private View view;
    private Model model;

    public Control(View v, Model m){
        this.view = v;
        this.model = m;
        view.setControl(this);
        model.addObserver(this);
        model.connect();
    }
    public void restart(){
        model.restart();
    }

    public void disconnect(){
        model.disconnect();
    }

    public void move(int x, int y){
        model.move(x, y);
    }
    public String locationStatus(int x, int y){
        return(model.grid[x][y]);
    }

    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }
}
