package c.b.a.sudokuapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import c.b.a.sudokuapp.fragments.ButtonGroup;

public class GameActivity extends AppCompatActivity implements ButtonGroup.OnFragmentInteractionListener{

    private String input = "";
    private int[][] currentBoard; //the current state of the board
    private int[][] solution;   //the solution to the current game

    private TextView status, timeTaken;
    private int buttonGroupArr[];
    private int cells[];
    private int emptyCells;
    private int timeTotal;
    private Drawable.ConstantState white_Draw;
    private Bitmap white_BMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        linkButtons();

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
        white_Draw = getDrawable(R.drawable.grid_b).getConstantState();
        white_BMP = buildBitmap(getDrawable(R.drawable.grid_b));
        timeTotal = getTimeTotal();
        startTimeThread();

    }
    public int getTimeTotal(){
        return 0; //Implement how to return total time if returning
    }

    private void startTimeThread(){
        new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try{
                        Thread.sleep(1000);
                        timeTaken.setText(String.valueOf(timeTotal));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeTaken.setText(DateUtils.formatElapsedTime(timeTotal++));
                            }
                        });
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public void onFragmentInteraction(int row, int cell, View view) {
        TextView boxClicked = (TextView) view;
        if(input == "?"){
            changeBackground(boxClicked);
        }
        else if(input != ""){

            boxClicked.setText(input);
            if(currentBoard[row][cell] == 0){
                emptyCells--;
            }
            currentBoard[row][cell] = Integer.parseInt(input);
            if(emptyCells == 0){
                checkBoard();
            }
        }
    }

    // links stuff in the view
    private void linkButtons(){
        buttonGroupArr = new int[]{R.id.group0, R.id.group1, R.id.group2, R.id.group3, R.id.group4, R.id.group5, R.id.group6, R.id.group7, R.id.group8};
        cells = new int[]{R.id.t0, R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7, R.id.t8};
        for(int i = 0; i < 9; i++){
            ButtonGroup thisFrag = (ButtonGroup) getSupportFragmentManager().findFragmentById(buttonGroupArr[i]);
            assert thisFrag != null;
            thisFrag.setGridID(i);
        }
        status = findViewById(R.id.opField);
        timeTaken = findViewById(R.id.timeField);
    }

    private void checkBoard(){
        if(isSolved()){

        }
        else{
            Toast.makeText(GameActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    public void shortMsg(String in){
        Toast.makeText(this, "Input "+in, Toast.LENGTH_SHORT).show();
        status.setText("Operation: "+in);
    }

    public void operation(View view){
        switch(view.getId()){
            case R.id.btn1:
                input = "1";
                //shortMsg(input);
                break;
            case R.id.btn2:
                input = "2";
                //shortMsg(input);
                break;
            case R.id.btn3:
                input = "3";
                //shortMsg(input);
                break;
            case R.id.btn4:
                input = "4";
                //shortMsg(input);
                break;
            case R.id.btn5:
                input = "5";
                //shortMsg(input);
                break;
            case R.id.btn6:
                input = "6";
                //shortMsg(input);
                break;
            case R.id.btn7:
                input = "7";
                //shortMsg(input);
                break;
            case R.id.btn8:
                input = "8";
                //shortMsg(input);
                break;
            case R.id.btn9:
                input = "9";
                //shortMsg(input);
                break;
            case R.id.btnMark:
                input = "?";
                shortMsg(input);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void showCurrentGame(int[][] board){
        for(int i = 0 ; i < 9 ; i++){
            View line = findViewById(buttonGroupArr[i]);
            for(int j = 0 ; j < 9 ; j++){
                TextView cell = line.findViewById(cells[j]);
                if(board[i][j] != 0){
                    cell.setText(Integer.toString(board[i][j]));
                    cell.setClickable(false);
                    cell.setTypeface(null, Typeface.BOLD);
                    cell.setBackground(getDrawable(R.drawable.grid_x));
                }
            }
        }
    }

    public void goBack(View view){
        finish();
    }

    private void countEmptyCells(int[][] board){
        emptyCells = 0;
        for(int i = 0 ; i < 9 ; i++){
            for(int j = 0 ; j < 9 ; j++){
                if(board[i][j] == 0){
                    emptyCells++;
                }
            }
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
                            countEmptyCells(currentBoard);
                            showCurrentGame(currentBoard);
                        }
                        else{
                            //kannski breyta þessu
                            Toast.makeText(GameActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                            //kannski breyta þessu
                            Toast.makeText(GameActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //compares the current board to the solution. True = solved, false = unsolved
    private boolean isSolved(){
        return currentBoard == solution;
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

    public void changeBackground(TextView field){

        Drawable img = field.getBackground();
        if(img == null){
            return;
        }
        if(!img.getConstantState().equals(white_Draw) || !buildBitmap(img).sameAs(white_BMP)){
            field.setBackgroundResource(R.drawable.grid_b);
        }
        else{
            field.setBackgroundResource(R.drawable.grid_m);
        }
        /*
        if(!img.equals(white)){
            field.setBackgroundResource(R.drawable.grid_b);
        }
        else{
            field.setBackgroundResource(R.drawable.grid_m);
        }
        */

        //field.setBackgroundResource(R.drawable.grid_1m);
    }

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