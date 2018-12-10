package c.b.a.sudokuapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Logic {
    public Logic(){

    }

    public int countEmptyCells(int[][] board){
        int emptyCells = 0;
        for(int i = 0 ; i < 9 ; i++){
            for(int j = 0 ; j < 9 ; j++){
                if(board[i][j] == 0){
                    emptyCells++;
                }
            }
        }
        return emptyCells;
    }


    //converts the 2 dimensional array into a string. Used by getSolution()
    public String convertBoardToString(int[][] game){
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


    //should parse the JsonArrays containing the boards to a two dimensional int array
    public int[][] parseJsonArrayToInt(JsonArray arr){
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


    //creates an empty 9x9 integer array
    public int[][] createEmptyBoard(){
        int[][] board = new int[9][];

        for(int i = 0 ; i < 9 ; i++){
            board[i] = new int[9];
        }
        return board;
    }
}
