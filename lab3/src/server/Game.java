package server;

public class Game {
    public final int EMPTY = -1;
    private int gameID;
    private int player1 = -1;
    private int player2 = -1;
    private int[][] grid = new int[3][3];

    public Game(int gameID, int player1){
        System.out.println("SPEL SKAPAT");
        this.gameID = gameID;
        this.player1 = player1;
        clearGrid();
    }

    public void clearGrid(){
        for(int i = 0; i < grid.length; i++){
            for(int n = 0; n < grid[i].length; n++){
                grid[i][n] = EMPTY;
            }
        }
    }

    public void setPlayer(int playerID){
        if(player1 == -1){
            player1 = playerID;
        }else if(player2 == -1){
            this.player2 = playerID;
        }else{
            System.out.println("ERROR IN SETPLAYER");
        }

    }
    public void removePlayer(int playerID){
        if(playerID == player1){
            player1 = -1;
        }else if(playerID == player2){
            player2 = -1;
        }
    }

    public boolean avaliableSpot(){
        if(player1 == -1 || player2 == -1){
            return false;
        }else{
            return true;
        }
    }
    public int getGameID(){
        return gameID;
    }
}

