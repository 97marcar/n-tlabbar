package model;



import java.util.Observable;


public class Model extends Observable {
    public Model(){

    }

    public void move(int x, int y){
        //Send request to make a move at x,y
    }



    private void setChangedAndNotify(){
        setChanged();
        notifyObservers();
    }

}

/**SKit som ska till server
 * public final int EMPTY = 0;
 public final int ME = 1;
 public final int OTHER = 2;
 private int inrow = 0;
 int[][] grid;
 private final int NOT_STARTED = 0;
 private final int MY_TURN = 1;
 private final int OTHER_TURN = 2;
 private final int FINISHED = 3;

 public Model(){
 grid = new int[3][3];
 clearGrid();
 }

 //Move to server
 public void clearGrid(){
 for(int i = 0; i < grid.length; i++){
 for(int n = 0; n < grid[i].length; n++){
 grid[i][n] = EMPTY;
 }
 }
 }
 */
