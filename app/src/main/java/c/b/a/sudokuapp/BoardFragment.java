package c.b.a.sudokuapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Objects;

import c.b.a.sudokuapp.fragments.ButtonGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment {


    private String input = "";
    private String diff;
    private int[][] currentBoard; //the current state of the board
    private int[][] solution;   //the solution to the current game
    private Activity a;
    private TextView timeTaken;
    private int emptyCells;
    private Logic logic;
    private Timer timer; //Timer
    private Drawable.ConstantState white_Draw;
    private Bitmap white_BMP;
    private BoardLinker boardlinker;
    private ProgressDialog progress;
    private int[][] cellIDs;
    private TextView[][] cellViews;
    private SharedPreferences sharedPref;


    public BoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        a = getActivity();
        //get the desired difficulty from DifficultyFragment
        Intent i = a.getIntent();
        diff = i.getStringExtra("DIFF");

        linkButtons();
        timer = new Timer();
        logic = new Logic();

        currentBoard = logic.createEmptyBoard();
        solution = logic.createEmptyBoard();

        //if diff is null, then we resume the current game.
        if(diff == null){
            //TODO, resume previous game
        }
        else{
            initializeNewGame();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.stopThread();
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.pauseTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        timer.resumeTimer();
    }


    private void cellClicked(int row, int cell, View view) {
        TextView boxClicked = (TextView) view;

        if(input.equals("m")){
            changeBackground(boxClicked);
        }
        else if(!input.equals("")){

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
        boardlinker = new BoardLinker(a);
        cellIDs = boardlinker.cellIDs;
        cellViews = boardlinker.cellViews;
        sharedPref = a.getPreferences(Context.MODE_PRIVATE);

        for(int i = 0 ; i < 9 ; i++){
            for(int j = 0 ; j < 9 ; j++){
                final int finalI = i;
                final int finalJ = j;
                cellViews[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        input = sharedPref.getString(getString(R.string.input), "");
                        cellClicked(finalI, finalJ, v);
                    }
                });
            }
        }

        timeTaken = a.findViewById(R.id.timeField);
        white_Draw = Objects.requireNonNull(a.getDrawable(R.drawable.grid_b)).getConstantState();
        white_BMP = buildBitmap(Objects.requireNonNull(a.getDrawable(R.drawable.grid_b)));
    }



    private void checkBoard(){
        if(isSolved()){
            winPopup();
        }
        else{
            Toast.makeText(a, "Incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private void winPopup() {
        timer.stopThread();
        AlertDialog.Builder msg = new AlertDialog.Builder(a);
        msg.setTitle("Congratulations!");
        msg.setMessage("Your time was " + timer.getTime());
        msg.setCancelable(true);
        msg.setNeutralButton("New puzzle",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        resetBoard();
                        initializeNewGame();
                    }
                });
        msg.setNegativeButton("Main menu",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToMainMenu();
                    }
                });
        AlertDialog alertMsg = msg.create();
        alertMsg.show();

    }

    private void resetBoard(){
        for(int i = 0 ; i < 9 ; i++){
            for(int j = 0 ; j < 9 ; j++){
                cellViews[i][j].setText("");
                cellViews[i][j].setClickable(true);
                cellViews[i][j].setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    private void goToMainMenu(){
        Intent intent = new Intent(a, MenuActivity.class);
        startActivity(intent);
        a.finish();
    }

    @SuppressLint("SetTextI18n")
    private void showCurrentGame(int[][] board){
        for(int i = 0 ; i < 9 ; i++){
            for(int j = 0 ; j < 9 ; j++){
                if(board[i][j] != 0){
                    cellViews[i][j].setText(Integer.toString(board[i][j]));
                    cellViews[i][j].setClickable(false);
                    cellViews[i][j].setTypeface(null, Typeface.BOLD);
                    cellViews[i][j].setBackground(a.getDrawable(R.drawable.grid_x));
                }
                else{
                    cellViews[i][j].setBackground(a.getDrawable(R.drawable.grid_b));
                }
            }
        }
    }


    //sets up a new game from the received JsonObjcet from the API
    private void initializeNewGame(){

        progress = new ProgressDialog(a);
        progress.setMessage(getString(R.string.loading));
        progress.show();

        resetBoard();
        generateNewGame(diff);

        if(progress.isShowing()){
            progress.hide();
        }

        timer.startTimeThread(0, timeTaken);
    }

    //compares the current board to the solution. True = solved, false = unsolved
    private boolean isSolved(){
        return currentBoard == solution;
    }



    public void changeBackground(TextView field){

        Drawable img = field.getBackground();
        if(img == null){
            return;
        }
        if(!Objects.equals(img.getConstantState(), white_Draw) || !buildBitmap(img).sameAs(white_BMP)){
            field.setBackgroundResource(R.drawable.grid_b);
        }
        else{
            field.setBackgroundResource(R.drawable.grid_m);
        }
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
                            currentBoard = logic.parseJsonArrayToInt(arr);
                            emptyCells = logic.countEmptyCells(currentBoard);
                            showCurrentGame(currentBoard);
                            getSolution(currentBoard);
                        }
                        else{
                            //kannski breyta þessu
                            Toast.makeText(a, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //Gets the solution to the game and saves it in the 'solution' private variable.
    // Should be called by generateNewGame()
    private void getSolution(int[][] game){
        String stringBoard = logic.convertBoardToString(game);
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
                            solution = logic.parseJsonArrayToInt(arr);
                        }
                        else{
                            //kannski breyta þessu
                            Toast.makeText(a, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
