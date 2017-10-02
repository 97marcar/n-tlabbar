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
    public String locationStatus(int x, int y){
        return("EMPTY");
    }
    private void setChangedAndNotify(){
        setChanged();
        notifyObservers();
    }
    public void update(Observable o, Object arg) {

    }
}
