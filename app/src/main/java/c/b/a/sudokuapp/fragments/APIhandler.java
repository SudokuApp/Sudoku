package c.b.a.sudokuapp.fragments;


import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class APIhandler {

    private String getGameUrl;
    private String getSolutionUrl;
    public JsonArray game;
    public JsonArray solution;

    public APIhandler(String diff){
        this.getGameUrl = "https://sugoku2.herokuapp.com/board?difficulty=" + diff;
        this.getSolutionUrl = "https://sugoku2.herokuapp.com/solve";
        game = new JsonArray();
        solution = new JsonArray();
    }

    public void setGetGameUrl(String getGameUrl) {
        this.getGameUrl = getGameUrl;
    }

    public void setGetSolutionUrl(String getSolutionUrl) {
        this.getSolutionUrl = getSolutionUrl;
    }

    //Should take in a difficulty parameter (easy, medium or hard) and fetch a json sudoku puzzle from an API
    public JsonArray generateNewGame(final Context context){
        Ion.with(context)
                .load(getGameUrl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null){
                            game = result.getAsJsonArray("board");
                        }
                        else{
                            //kannski breyta þessu
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return game;
    }

    //Gets the solution to the game and saves it in the 'solution' private variable.
    // Should be called by generateNewGame()
    public JsonArray getSolution(final int[][] game, final Context context){
        String stringBoard = convertBoardToString(game);
        Ion.with(context)
                .load(getSolutionUrl)
                .setMultipartParameter("board", stringBoard)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null){
                            solution = result.getAsJsonArray("solution");
                        }
                        else{
                            //kannski breyta þessu
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return solution;
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

