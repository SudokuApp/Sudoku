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
import android.os.SystemClock;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Objects;

import c.b.a.sudokuapp.fragments.ButtonGroup;

import static c.b.a.sudokuapp.MenuFragment.currUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment implements View.OnClickListener {


    private String input = "", diff, initialBoard, userSolution, solution;
    private int[][] currentBoard;// The current state of the board
    private int emptyCells;

    private Activity a;
    private TextView timeTaken;
    private TextView[][] cellViews;
    private Button goBack;
    private int currentTime;

    private Logic logic; // Instance of the Logic class
    private Timer timer; // Instance of the Timer class
    private Drawable.ConstantState white_Draw;
    private Bitmap white_BMP;
    //private ProgressDialog progress;
    private SharedPreferences sharedPref;
    private ScoreHandler scoreHandler;

    private FirebaseDatabase mDatabase;
    private DatabaseReference userRef;

    private StringBuilder userS;


    public BoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        a = getActivity();
        //get the desired difficulty from DifficultyFragment
        assert a != null;
        Intent i = a.getIntent();
        diff = i.getStringExtra("DIFF");

        linkButtons();
        timer = new Timer(timeTaken);
        logic = new Logic();

        mDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = mDatabase.getReference("users");
        userRef = ref.child(Objects.requireNonNull(firebaseAuth.getUid()));
        goBack = a.findViewById(R.id.returnBtn);
        goBack.setOnClickListener(this);

        userSolution = currUser.getUserSolution();
        userS = new StringBuilder(userSolution);

        //progress = new ProgressDialog(a);
        //progress.setMessage(getString(R.string.loading));
        //progress.show();


        //if diff is null, then we resume the current game.
        sharedPref.edit().putString("INPUT", "").apply();


        // If diff is null, then we resume the current game.
        if(!currUser.getCurrentGame().equals("")){
            initialBoard = currUser.getCurrentGame();
            solution = currUser.getSolution();
            currentTime = currUser.getCurrentTime();
            resumeGame();
        }
        else {
            currentBoard = logic.createEmptyBoard();
            solution = intToString(logic.createEmptyBoard());
            currentTime = 0;
            initializeNewGame();
        }
    }

    @Override
    public void onDestroy() {
        currUser.setCurrentTime(timer.getTime());
        saveToDatabase();
        super.onDestroy();
        timer.stopThread();
    }

    @Override
    public void onPause() {
        currUser.setCurrentTime(timer.getTime());
        super.onPause();
        saveToDatabase();
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
            if(!input.equals("x")){
                boxClicked.setText(input);
                if(currentBoard[row][cell] == 0){
                    emptyCells--;
                }
                currentBoard[row][cell] = Integer.parseInt(input);
                if(emptyCells == 0){
                    checkBoard();
                }
            }
            else if(currentBoard[row][cell] != 0){
                boxClicked.setText("");
                boxClicked.setBackgroundResource(R.drawable.grid_b);
                currentBoard[row][cell] = 0;
                emptyCells++;
            }
            updateUserSolution(row, cell);
        }

    }

    private void updateUserSolution(int row, int cell) {

        char temp = Character.forDigit(currentBoard[row][cell], 10);
        int index = (9 * row + cell);
        userS.setCharAt(index, temp);
        userRef.child("userSolution").setValue(userS.toString());
    }


    // links stuff in the view
    private void linkButtons(){
        BoardLinker boardlinker = new BoardLinker(a);
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
        white_BMP = Logic.buildBitmap(Objects.requireNonNull(a.getDrawable(R.drawable.grid_b)));
    }



    private void checkBoard(){
        if(isSolved()){
            winPopup();
            userRef.child("currentGame").setValue("");
            userRef.child("solution").setValue("");
        }
        else{
            winPopup();
           // incorrectPopup();
        }
    }

    private void winPopup() {
        timer.stopThread();

        ScoreHandler scorehandler = new ScoreHandler(diff, mDatabase);
        scorehandler.compareToPrivate(timer.getTime(), userRef);


        AlertDialog.Builder msg = new AlertDialog.Builder(a);
        msg.setTitle("Congratulations!");
        msg.setMessage("Your time was " + timer.getTimeReadable());
        msg.setCancelable(true);
        msg.setNeutralButton("New puzzle",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
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

    private void incorrectPopup() {

        timer.stopThread();
        currUser.setCurrentTime(timer.getTime());
        AlertDialog.Builder msg = new AlertDialog.Builder(a);
        msg.setTitle("Incorrect!");
        msg.setMessage("Your solution is incorrect, do you want to continue trying without help?");
        msg.setCancelable(true);
        msg.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer.startTimeThread(currUser.getCurrentTime());
            }
        });
        msg.setNeutralButton("New puzzle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initializeNewGame();
            }
        });
        msg.setNegativeButton("Get help", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO
            }
        });

        AlertDialog alertMsg = msg.create();
        alertMsg.show();
    }



    private void resetBoard(){
        userRef.child("currentGame").setValue("");
        userRef.child("solution").setValue("");
        userRef.child("diff").setValue(diff);
        userRef.child("userSolution").setValue(getString(R.string.initalizeUserSolution));
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
        initialBoard = intToString(board);
    }



    //sets up a new game from the received JsonObjcet from the API
    private void initializeNewGame(){

        resetBoard();
        generateNewGame(diff);
        //if(progress.isShowing()){
        //    progress.hide();
        //}
        // TODO passa að tíminn byrji ekki fyrr en borðið er birt
        timer.startTimeThread(currentTime);
    }

    //compares the current board to the solution. True = solved, false = unsolved
    private boolean isSolved(){
        String userSol = intToString(currentBoard);

        return userSol.equals(solution);
    }



    // TODO á þetta að vera public?
    public void changeBackground(TextView field){

        Drawable img = field.getBackground();
        if(img == null){
            return;
        }
        if(!Objects.equals(img.getConstantState(), white_Draw) || !Logic.buildBitmap(img).sameAs(white_BMP)){
            field.setBackgroundResource(R.drawable.grid_b);
        }
        else{
            field.setBackgroundResource(R.drawable.grid_m);
        }
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
                            userRef.child("currentGame").setValue(intToString(currentBoard));

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
                            solution = intToString(logic.parseJsonArrayToInt(arr));
                            userRef.child("solution").setValue(solution);
                        }
                        else{
                            //kannski breyta þessu
                            Toast.makeText(a, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private int[][] stringToInt(String string) {
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

    private String intToString(int[][] array) {
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

    private void saveToDatabase() {


        StringBuilder current = new StringBuilder(intToString(currentBoard));

        for(int i = 0 ; i < 81 ; i++ ){
            if(!(initialBoard.charAt(i) == '0')){
                current.setCharAt(i, '0');
            }
        }
        userRef.child("easyHighScore").setValue(currUser.getEasyHighScores());
        userRef.child("mediumHighScore").setValue(currUser.getMediumHighScores());
        userRef.child("hardHighScore").setValue(currUser.getHardHighScores());
        userRef.child("userSolution").setValue(current.toString());
        userRef.child("currentTime").setValue(timer.getTime());
    }

    @SuppressLint("SetTextI18n")
    private void showCurrentSolution() {
        for(int i = 0 ; i < 9 ; i++) {
            for(int j = 0 ; j < 9 ; j++){
                if(stringToInt(userSolution)[i][j] != 0){
                    cellViews[i][j].setText(Integer.toString(stringToInt(userSolution)[i][j]));
                }
            }
        }
    }

    private void combineBoards() {

        StringBuilder temp = new StringBuilder(initialBoard);
        for(int i = 0 ; i < 81 ; i++){
            if(!(userSolution.charAt(i) == '0')){
                temp.setCharAt(i, userSolution.charAt(i));
            }
        }
        currentBoard = stringToInt(temp.toString());
    }

    private void resumeGame() {
        combineBoards();
        emptyCells = logic.countEmptyCells(currentBoard);
        showCurrentGame(stringToInt(initialBoard));
        showCurrentSolution();


        timer.startTimeThread(currentTime);
        //progress.cancel();
    }

    @Override
    public void onClick(View v) {
        if(v == goBack) {
            saveToDatabase();
            goToMainMenu();
        }
    }
}