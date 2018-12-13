package c.b.a.sudokuapp.services;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Logic {

    public Logic(){ }

    /**
     * @param board
     * @return the number of empty cells on the board
     */
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

    /**
     * @param string
     * @return the string of numbers in the form of a two dimensional integer array
     */
    public int[][] stringToInt(String string) {
        int[][] board = new int[9][9];
        int temp = 0;

        for(int j = 0 ; j < 9 ; j++) {
            for(int k = 0 ; k < 9 ; k++) {
                board[j][k] = Character.getNumericValue(string.charAt(temp));
                temp++;
            }
        }
        return board;
    }

    /**
     * @param array
     * @return the two dimensional integer array in the form of a string
     */
    public String intToString(int[][] array) {
        StringBuilder string = new StringBuilder();
        char temp;
        for(int i = 0 ; i < 9 ; i++) {
            for(int j = 0 ; j < 9 ; j++) {
                temp = Character.forDigit(array[i][j], 10);
                string.append(temp);
            }
        }
        return string.toString();
    }

    /**
     * @param email from user
     * @return the email without the @ symbol and everything after that
     */
    public static String splitUserEmail(String email) {
        String[] emailArr = email.split("@");
        return emailArr[0];
    }

    /**
     * converts the 2 dimensional array into a string. Used by getSolution()
     * @param game the sudoku board
     * @return the board in the form of a string
     */
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

    /**
     * should parse the JsonArrays containing the boards to a two dimensional int array
     * @param arr from the API
     * @return the JsonArray in the form of two dimensional integer array
     */
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

    /**
     * @return an empty 9x9 integer array
     */
    public static int[][] createEmptyBoard(){
        int[][] board = new int[9][];

        for(int i = 0 ; i < 9 ; i++){
            board[i] = new int[9];
        }
        return board;
    }


    // buids a bitmap from a drawable in order to compare it
    public static Bitmap buildBitmap(Drawable img){
        Bitmap out; //Assuming no incoming drawable is a bitmap

        int width = img.getIntrinsicWidth();
        int height = img.getIntrinsicHeight();

        if(width <= 0){
            width = 1;
        }
        if(height <= 0){
            height = 1;
        }
        out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        img.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        img.draw(canvas);

        return out;
    }
}
