package server;
/**
 *
 * @author Marcus Carlsson
 * @since 2017-09-24
 * @version 1.0
 */

public class Game {
    private final int EMPTY = -1;
    private int gameID;
    private int player1 = -1;
    private int player2 = -1;
    private int[][] grid = new int[3][3];
    private int inrow = 0;
    private int gameOver = 0;
    private int playersTurn;

    /**
     * Creates a game and sets the first player that connects to player 1.
     * @param gameID gameID to make sure the server and clients know which game is which.
     * @param player1 which specific client that is connected to this specific game
     */
    public Game(int gameID, int player1){
        System.out.println("GAME CREATED");
        this.gameID = gameID;
        this.player1 = player1;
        clearGrid();
    }

     private void clearGrid(){
        for(int i = 0; i < grid.length; i++){
            for(int n = 0; n < grid[i].length; n++){
                grid[i][n] = EMPTY;
            }
        }
    }

    /**
     * Restarts the game
     */
    public void restart(){
         clearGrid();
         gameOver = 0;
    }

    /**
     * Makes a move in the game if its the correct players turn
     * @param x where on x-axis
     * @param y where on y-axis
     * @param playerID which player that makes the move
     * @return true if move went though else false
     */
    public boolean move(int x, int y, int playerID){
        System.out.println("CURRENT PLAYERS TURN: "+playersTurn);
        System.out.println("CURRENT PLAYER: "+playerID);
        if(playersTurn == playerID){
            if(grid[x][y] == -1){
                grid[x][y] = playerID;
                checkWin(playerID);
                playersTurn = (playersTurn==player1) ?  player2 : player1;
                System.out.println("NEW PLAYERS TURN:"+playersTurn);
                return (true);
            }else{
                return (false);
            }
        }else {
            return (false);
        }
    }




    /**
     * Sets a connected player to the available spot
     * @param playerID new player
     */
    public void setPlayer(int playerID){
        if(player1 == -1){
            player1 = playerID;
        }else if(player2 == -1){
            this.player2 = playerID;
        }else{
            System.out.println("ERROR IN SETPLAYER");
        }
        this.playersTurn = playerID;
        System.out.println("PLAYER: "+ playerID +" CONNECTED TO: "+getGameID());

    }

    /**
     * Removes a player
     * @param playerID which player to remove
     */
     void removePlayer(int playerID){
        System.out.println("REMOVE BEGIN PLAYERID: "+playerID);
        if(playerID == player1){
            player1 = -1;
            gameOver = 0;
            System.out.println("1 REMOVED");
        }else if(playerID == player2){
            System.out.println("2 REMOVED");
            gameOver = 0;
            player2 = -1;
        }
    }

    /**
     * checks if there is an available spot
     * @return the alternate state of the availableSpot
     */
    public boolean availableSpot(){
        boolean availableSpot  = (player1 == -1 ^ player2 == -1);
        if(availableSpot) clearGrid();
        return(!availableSpot);
    }


     private boolean vertical(int player){
        for(int y = 0; y < grid.length; y++){
            inrow = 0;
            for(int x = 0; x < grid[0].length; x++){
                if(grid[x][y] == player){
                    inrow++;
                    if(inrow == 3){
                        return(true);
                    }
                }else{
                    inrow = 0;
                }
            }
        }
        return false;
    }

    private boolean horisontal(int player){
        for(int x = 0; x < grid.length; x++){
            inrow = 0;
            for(int y = 0; y < grid[0].length; y++){
                if(grid[x][y] == player){
                    inrow++;
                    if(inrow == 3){
                        return(true);
                    }
                }else{
                    inrow = 0;
                }
            }
        }
        return false;
    }

    private boolean diagonal(int player){
        //Diagonal top left to top right (x-value)
        for(int i = 0; i < grid.length; i++){
            inrow = 0;
            for(int x = 0, y = i; x <= i; x++, y--){
                if(grid[x][y] == player){
                    inrow++;
                    if(inrow == 3){
                        return(true);
                    }
                }else{
                    inrow = 0;
                }
            }
        }

        inrow = 0;

        //Diagonal bottom right to bottom left (x-value)
        for(int i = grid.length-1; i >= 0; i--){
            inrow = 0;
            for(int x = grid.length-1, y = i; x >= i; x--, y++){
                if(grid[x][y] == player){
                    inrow++;
                    if(inrow == 3){
                        return(true);
                    }
                }else{
                    inrow = 0;
                }
            }
        }

        //Diagonal top right to top left (x-value)
        for(int i = 0; i < grid.length; i++){
            inrow = 0;
            for(int x = grid.length-1-i, y = 0; y <= i; x++, y++){
                if(grid[x][y] == player){
                    inrow++;
                    if(inrow == 3){
                        return(true);
                    }
                }else{
                    inrow = 0;
                }
            }
        }

        //Diagonal bottom left to bottom right (x-value)
        for(int i = 0; i < grid.length-1; i++){
            inrow = 0;
            for(int x = 0, y = grid.length-1-i; x <= i; x++, y++){
                if(grid[x][y] == player){
                    inrow++;
                    if(inrow == 3){
                        return(true);
                    }
                }else{
                    inrow = 0;
                }
            }
        }

        return(false);
    }

    /**
     * Check if a player has 3 in row, if he does set the gameOver to 1
     *
     * @param player the player to check for
     *
     */
    public void checkWin(int player){
        if(vertical(player) || horisontal(player) || diagonal(player)){
            gameOver = 1;
        }
    }


    /**
     * @return gameID
     */
    public int getGameID(){
        return gameID;
    }

    /**
     * @return gameOver
     */
    public int getGameOver(){
        return gameOver;
    }
}



