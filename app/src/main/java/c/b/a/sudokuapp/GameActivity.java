package c.b.a.sudokuapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class GameActivity extends AppCompatActivity {

    private int[][] currentBoard; //the current state of the board
    private int[][] solution;   //the solution to the current game

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO, link buttons and views

        setContentView(R.layout.activity_game);
        currentBoard = createEmptyBoard();

        //get the desired difficulty from DifficultyFragment
        Intent i = getIntent();
        String diff = i.getStringExtra("DIFF");

        //if diff is null, then we resume the current game.
        if(diff == null){
            //TODO, resume previous game
        }
        else{
            generateNewGame(diff);
        }
    }

    //creates an empty 9x9 integer array
    private int[][] createEmptyBoard(){
        int[][] board = new int[9][];

        for(int i = 0 ; i < 9 ; i++){
            board[i] = new int[9];
        }
        return board;
    }


    //Should take in a difficulty parameter (easy, medium or hard) and fetch a json sudoku puzzle from an API
    private void generateNewGame(String difficulty){
        String url = "https://sugoku2.herokuapp.com/board?difficulty=" + difficulty;
        Ion.with(this)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null){
                            JsonArray arr = result.getAsJsonArray("board");
                            currentBoard = parseJsonArrayToInt(arr);
                            getSolution(currentBoard);
                            //TODO, show new game on screen
                        }
                        else{
                            //TODO, error message
                        }
                    }
                });
    }

    //Gets the solution to the game and saves it in the 'solution' private variable.
    // Should be called by generateNewGame()
    private void getSolution(int[][] game){
        String stringBoard = convertBoardToString(game);
        String url = "https://sugoku2.herokuapp.com/solve";
        Ion.with(this)
                .load(url)
                .setMultipartParameter("board", stringBoard)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null){
                            JsonArray arr = result.getAsJsonArray("solution");
                            solution = parseJsonArrayToInt(arr);
                        }
                        else{
                            //TODO, error message
                        }
                    }
                });
    }

    //compares the current board to the solution. True = solved, false = unsolved
    private boolean isSolved(){
        return currentBoard == solution;
    }

    private void gameOver(){
        //TODO, popup?
    }

    //should parse the JsonArrays containing the boards to a two dimensional int array
    private int[][] parseJsonArrayToInt(JsonArray arr){
        int[][] board = createEmptyBoard();
        int inner = 0;
        int outer = 0;
        for(JsonElement i : arr){
            JsonArray innerArr = i.getAsJsonArray();
            for(JsonElement j : innerArr){
                board[outer][inner] = j.getAsInt();
                ++inner;
            }
            ++outer;
            inner = 0;
        }
        return board;
    }

    //converts the 2 dimensional array into a string. Used by getSolution()
    private String convertBoardToString(int[][] game){
        StringBuilder result = new StringBuilder("[[");
        for(int i = 0 ; i < 9 ; i++){
            for(int j = 0 ; j < 9 ; j++){
                result.append(Integer.toString(game[i][j]));
                if(j != 8){
                    result.append(",");
                }
            }
            if(i != 8){
                result.append("],[");
            }
        }
        result.append("]]");
        return result.toString();
    }
}