package c.b.a.sudokuapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import c.b.a.sudokuapp.fragments.ButtonGroup;

import static c.b.a.sudokuapp.MenuFragment.currUser;

public class GameActivity extends AppCompatActivity implements ButtonGroup.OnFragmentInteractionListener{

    private String input = "";
    private String diff;
    private int[][] currentBoard; //the current state of the board
    private int[][] solution;   //the solution to the current game

    private TextView status, timeTaken, testext;
    private int buttonGroupArr[];
    private int cells[];
    private int emptyCells;
    private int timeTotal;
    private Drawable.ConstantState white_Draw;
    private Bitmap white_BMP;
    private ProgressDialog progress;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        progress = new ProgressDialog(GameActivity.this);
        progress.setMessage(getString(R.string.loading));
        progress.show();


        mDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        ref = mDatabase.getReference("users");
        userRef = ref.child(firebaseAuth.getUid());

        linkButtons();

        Intent i = getIntent();
        diff = i.getStringExtra("DIFF");

        //if diff is null, then we resume the current game.
        if(diff == null){
            currentBoard = stringToInt(currUser.getCurrentGame());
            solution = stringToInt(currUser.getSolution());
            resumeGame(currentBoard, solution);
        }
        else{
            currentBoard = createEmptyBoard();
            generateNewGame(diff);
        }

        white_Draw = getDrawable(R.drawable.grid_b).getConstantState();
        white_BMP = buildBitmap(getDrawable(R.drawable.grid_b));

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
            winPopup();
            userRef.child("currentGame").setValue("");
            userRef.child("solution").setValue("");
        }
        else{
            Toast.makeText(GameActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private void winPopup(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setTitle("Congratulations!");
        msg.setMessage("Your time was <time>" );
        msg.setCancelable(true);
        msg.setNeutralButton("New puzzle",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        resetBoard();
                        generateNewGame(diff);
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
            View line = findViewById(buttonGroupArr[i]);
            for(int j = 0 ; j < 9 ; j++){
                TextView cell = line.findViewById(cells[j]);
                cell.setText("");
                cell.setClickable(true);
                cell.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    private void goToMainMenu(){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void setOperator(String in){
        status.setText("Operation: "+in);
    }

    public void operation(View view){

        switch(view.getId()){
            case R.id.btn1:
                input = "1";
                break;
            case R.id.btn2:
                input = "2";
                break;
            case R.id.btn3:
                input = "3";
                break;
            case R.id.btn4:
                input = "4";
                break;
            case R.id.btn5:
                input = "5";
                break;
            case R.id.btn6:
                input = "6";
                break;
            case R.id.btn7:
                input = "7";
                break;
            case R.id.btn8:
                input = "8";
                break;
            case R.id.btn9:
                input = "9";
                break;
            case R.id.btnMark:
                input = "?";
                break;
        }
        setOperator(input);
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
        saveToDatabase();
        goToMainMenu();

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
        String string = "";
        char temp;
        for(int i = 0 ; i < 9 ; i++) {
            for(int j = 0 ; j < 9 ; j++) {
                temp = Character.forDigit(array[i][j], 10);
                string = string + temp;
            }
        }
        return string;
    }

    private void saveToDatabase() {
        userRef.child("currentGame").setValue(intToString(currentBoard));
        userRef.child("solution").setValue(intToString(solution));
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

                            SystemClock.sleep(1000);

                            timeTotal = getTimeTotal();
                            startTimeThread();
                            progress.cancel();
                        }
                        else{
                            //kannski breyta þessu
                            Toast.makeText(GameActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void resumeGame(int[][] currentBoard, int[][] solution) {
        countEmptyCells(currentBoard);
        showCurrentGame(currentBoard);

        SystemClock.sleep(1000);

        timeTotal = getTimeTotal();
        startTimeThread();
        progress.cancel();
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