package c.b.a.sudokuapp.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Objects;

import c.b.a.sudokuapp.services.BoardLinker;
import c.b.a.sudokuapp.services.Logic;
import c.b.a.sudokuapp.MenuActivity;
import c.b.a.sudokuapp.R;
import c.b.a.sudokuapp.services.ScoreHandler;
import c.b.a.sudokuapp.services.Timer;

import static c.b.a.sudokuapp.fragments.MenuFragment.fireBaseHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment implements View.OnClickListener {

    private String input = "", diff, initialBoard, userSolution, solution;
    private int[][] currentBoard;// The current state of the board
    private int emptyCells; //the number of cells currently empty

    private Activity a;

    private TextView timeTaken; //the TextView where the time is displayed
    private TextView[][] cellViews; // every cell in the board
    private Button goBack; //the back button
    private int currentTime; //the current time
    private Button getHint;

    private Logic logic; // Instance of the Logic class
    private Timer timer; // Instance of the Timer class
    private Drawable.ConstantState white_Draw;
    private Bitmap white_BMP;
    private SharedPreferences sharedPref;
    private ScoreHandler scorehandler;

    //Variables for database
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

        //get the activity
        a = getActivity();
        assert a != null;

        //get the desired difficulty from DifficultyFragment
        Intent i = a.getIntent();
        diff = i.getStringExtra("DIFF");

        //link the buttons on the UI
        linkButtons();
        timer = new Timer(timeTaken);
        logic = new Logic();

        //get the current users previous input
        userSolution = fireBaseHandler.currUser.getUserSolution();
        userS = new StringBuilder(userSolution);

        //reset the INPUT shared preference to an empty string
        sharedPref.edit().putString("INPUT", "").apply();

        // If current game is not null, then we resume that game
        if(!fireBaseHandler.currUser.getCurrentGame().equals("")){
            initialBoard = fireBaseHandler.currUser.getCurrentGame();
            solution = fireBaseHandler.currUser.getSolution();
            currentTime = fireBaseHandler.currUser.getCurrentTime();
            resumeGame();
        }

        // If current game is null, create a new game
        else {
            initializeNewGame();
        }
    }

    //save the status of the game and stop the timer thread
    @Override
    public void onDestroy() {
        super.onDestroy();
        fireBaseHandler.setUserCurrentTime(timer.getTime());
        saveTimesToDatabase();
        timer.stopThread();
    }

    //save the status of the game and pause the timer thread
    @Override
    public void onPause() {
        super.onPause();
        fireBaseHandler.setUserCurrentTime(timer.getTime());
        saveTimesToDatabase();
        timer.pauseTimer();
    }

    //resume the timer thread
    @Override
    public void onResume() {
        super.onResume();
        timer.resumeTimer();
    }

    //this is called when a cell is clicked
    private void cellClicked(int row, int cell, View view) {

        //this is the textView of the cell that was clicked
        TextView boxClicked = (TextView) view;

        //if input is "m" the player is only trying to mark or unmark the cell
        if(input.equals("m")){
            changeBackground(boxClicked);
        }

        //as long as anything is selected
        else if(!input.equals("")){

            // if player isn't trying to erase
            if(!input.equals("x")){

                // put the number into the cell
                boxClicked.setText(input);

                // if this field in currentBoard is zero, this cell was empty
                if(currentBoard[row][cell] == 0){
                    emptyCells--;
                }

                //save this input into currentBoard
                currentBoard[row][cell] = Integer.parseInt(input);

                //if there are no more empty cells, check if the game is won
                if(emptyCells == 0){
                    checkBoard();
                }
            }


            //if the player is trying to erase
            else{

                //if there is a number in this cell
                if(currentBoard[row][cell] != 0){

                    //remove the value
                    boxClicked.setText("");
                    currentBoard[row][cell] = 0;

                    //increment the emptycells counter
                    emptyCells++;
                }

                //set the background as white
                boxClicked.setBackgroundResource(R.drawable.grid_b);
            }
            updateUserSolution(row, cell);
        }

    }


    //update the user solution
    private void updateUserSolution(int row, int cell) {

        char temp = Character.forDigit(currentBoard[row][cell], 10);
        int index = (9 * row + cell);
        userS.setCharAt(index, temp);

        //saving to database
        fireBaseHandler.setUserUserSolution(userS.toString());
    }



    // links stuff in the view
    private void linkButtons(){

        //create an instance of the BoardLinker with for this activity
        BoardLinker boardlinker = new BoardLinker(a);
        cellViews = boardlinker.cellViews;

        //initialize the shared preference context
        sharedPref = a.getPreferences(Context.MODE_PRIVATE);

        // for every cell on the board, make it clickable and when it's clicked, call cellClicked
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

        //link some more stuff
        timeTaken = a.findViewById(R.id.timeField);
        white_Draw = Objects.requireNonNull(a.getDrawable(R.drawable.grid_b)).getConstantState();
        white_BMP = Logic.buildBitmap(Objects.requireNonNull(a.getDrawable(R.drawable.grid_b)));
        goBack = a.findViewById(R.id.returnBtn);
        goBack.setOnClickListener(this);
        getHint = a.findViewById(R.id.btnHint);
        getHint.setOnClickListener(this);
        scorehandler = new ScoreHandler(diff);
    }



    private void checkBoard(){

        //if the player has solved the puzzle
        if(isSolved()){

            // display the win popup
            winPopup();

            //reset the users currentGame and solution
            fireBaseHandler.setUserCurrentGame("");
            fireBaseHandler.setUserSolution("");
        }

        //if the player hasn't
        else{

            //show the incorrect popup
            incorrectPopup();
        }
    }

    // show a popup when the player has solved the puzzle
    private void winPopup() {

        int time = timer.getTime();
        String timeRead = timer.getTimeReadable();

        //stop the timer
        timer.stopThread();

        //have the ScoreHandler handle the score
        scorehandler.compareToPrivate(time);

        //build the actual popup
        AlertDialog.Builder msg = new AlertDialog.Builder(a);
        msg.setTitle("Congratulations!");
        msg.setMessage("Your time was " + timeRead);
        msg.setCancelable(true);
        msg.setNeutralButton("New puzzle",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        //if the player wants, play a new game on the same difficulty
                        initializeNewGame();
                    }
                });
        msg.setNegativeButton("Main menu",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //if the player wants, go back to the main menu
                        goToMainMenu();
                    }
                });
        AlertDialog alertMsg = msg.create();
        alertMsg.show();

    }

    //shows a popup when the player has an incorrect solution
    private void incorrectPopup() {

        //pause the timer
        timer.pauseTimer();

        //build the actual popup
        AlertDialog.Builder msg = new AlertDialog.Builder(a);
        msg.setTitle("Incorrect!");
        msg.setMessage("Your solution is incorrect, do you want to continue trying without help?");
        msg.setCancelable(true);
        msg.setNeutralButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //if the player wants to resume this game, resume the timer
                timer.resumeTimer();
            }
        });

        msg.setNegativeButton("Get help", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //if player wants to resume this game but get some help
                getHelp();
                //resume timer
                timer.resumeTimer();
            }
        });

        AlertDialog alertMsg = msg.create();
        alertMsg.show();
    }

    /**
     * When user fills up a board and it is incorrect he can choose to get help
     * This function sets the backround of the incorrect digits to red.
     */
    private void getHelp() {
        String userSolution = fireBaseHandler.currUser.getUserSolution();
        String currBoard = logic.intToString(currentBoard);

        for(int i = 0; i < 81 ; i++) {
            //getting row and cell
            int row = i / 9;
            int cell = i % 9;

            //The initial numbers are set to grey
            if(fireBaseHandler.currUser.getCurrentGame().charAt(i) != '0') {
                cellViews[row][cell].setBackground(a.getDrawable(R.drawable.grid_x));
            }
            //The incorrect digits are set to red
            else if(solution.charAt(i) != userSolution.charAt(i) && currBoard.charAt(i) == userSolution.charAt(i)) {

                cellViews[row][cell].setBackground(a.getDrawable(R.drawable.grid_w));
            }
            //others set to white
            else {
                cellViews[row][cell].setBackground(a.getDrawable(R.drawable.grid_b));
            }
        }
    }


    //reset the board
    private void resetBoard(){

        //reset the board values in the database

        fireBaseHandler.resetUserGame(getString(R.string.initalizeUserSolution), diff);

        //for every cell in the board
        for(int i = 0 ; i < 9 ; i++){
            for(int j = 0 ; j < 9 ; j++){

                //reset the text and make them clickable again.
                cellViews[i][j].setText("");
                cellViews[i][j].setClickable(true);
                cellViews[i][j].setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    //go to the main menu...
    private void goToMainMenu(){
        Intent intent = new Intent(a, MenuActivity.class);
        startActivity(intent);
        a.finish();
    }

    //prints the board it receives as an unclickable board
    @SuppressLint("SetTextI18n")
    private void showCurrentGame(int[][] board){
        for(int i = 0 ; i < 9 ; i++){
            for(int j = 0 ; j < 9 ; j++){

                //if the cell is not supposed to be empty 0
                if(board[i][j] != 0){

                    //input the number, make it bold and gray, and make it unclickable
                    cellViews[i][j].setText(Integer.toString(board[i][j]));
                    cellViews[i][j].setClickable(false);
                    cellViews[i][j].setTypeface(null, Typeface.BOLD);
                    cellViews[i][j].setBackground(a.getDrawable(R.drawable.grid_x));
                }
                else{
                    //if the cell is supposed to be empty, make it white
                    cellViews[i][j].setBackground(a.getDrawable(R.drawable.grid_b));
                }
            }
        }

        //se the board as initialBoard
        initialBoard = logic.intToString(board);
    }



    //sets up a new game
    private void initializeNewGame(){

        //reset the board, get a new game from the API and start the timer.
        currentBoard = Logic.createEmptyBoard();
        solution = logic.intToString(Logic.createEmptyBoard());
        currentTime = 0;
        resetBoard();
        generateNewGame(diff);
        timer.startTimeThread(currentTime);
    }

    //compares the current board to the solution. True = solved, false = unsolved
    private boolean isSolved(){

        String userSol = logic.intToString(currentBoard);
        return userSol.equals(solution);
    }



    //change the background
    private void changeBackground(TextView field){

        Drawable img = field.getBackground();
        if(img == null){
            return;
        }

        //if the background is grey, make it white
        if(!Objects.equals(img.getConstantState(), white_Draw) || !Logic.buildBitmap(img).sameAs(white_BMP)){
            field.setBackgroundResource(R.drawable.grid_b);
        }

        //and vice versa
        else{
            field.setBackgroundResource(R.drawable.grid_m);
        }
    }

    // Take in a difficulty parameter (easy, medium or hard) and fetch a json sudoku puzzle from an API
    public void generateNewGame(String difficulty){

        String gameUrl = "https://sugoku2.herokuapp.com/board?difficulty=";
        Ion.with(this)
                .load(gameUrl + difficulty)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null){

                            //get the JsonArray for the board
                            JsonArray arr = result.getAsJsonArray("board");
                            saveNewGame(arr);
                        }
                        else{
                            Toast.makeText(a, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    //save a game from the API
    public void saveNewGame(JsonArray arr){

        //parse the array into the currentBoard, count the empty cells and show print
        // the board onto the screen
        currentBoard = logic.parseJsonArrayToInt(arr);
        emptyCells = logic.countEmptyCells(currentBoard);
        showCurrentGame(currentBoard);

        //get the solution for this board
        getSolution(currentBoard);

        //save the currentBoard to the database
        fireBaseHandler.setUserCurrentGame(logic.intToString(currentBoard));
    }



    //Gets the solution to the game and saves it in the 'solution' private variable.
    // Should be called by generateNewGame()
    public void getSolution(int[][] game){
        String stringBoard = logic.convertBoardToString(game);
        String solutionUrl = "https://sugoku2.herokuapp.com/solve";
        Ion.with(this)
                .load(solutionUrl)
                .setMultipartParameter("board", stringBoard)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null){

                            //get the JsonArray for the solution and save it.
                            JsonArray arr = result.getAsJsonArray("solution");
                            saveSolution(arr);

                        }
                        else{
                            Toast.makeText(a, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //Save a solution from the API to the database
    public void saveSolution(JsonArray arr){
        solution = logic.intToString(logic.parseJsonArrayToInt(arr));
        fireBaseHandler.setUserSolution(solution);
    }

    //save the times to the database
    private void saveTimesToDatabase() {
        fireBaseHandler.saveAllTimes();
    }

    //prints the user solution to the board
    @SuppressLint("SetTextI18n")
    private void showCurrentSolution() {
        for(int i = 0 ; i < 9 ; i++) {
            for(int j = 0 ; j < 9 ; j++){
                if(logic.stringToInt(userSolution)[i][j] != 0){
                    cellViews[i][j].setText(Integer.toString(logic.stringToInt(userSolution)[i][j]));
                }
            }
        }
    }

    //combine the initialboard and the usersolution into currentBoard
    private void combineBoards() {
        StringBuilder temp = new StringBuilder(initialBoard);
        for(int i = 0 ; i < 81 ; i++){
            if(!(userSolution.charAt(i) == '0')){
                temp.setCharAt(i, userSolution.charAt(i));
            }
        }
        currentBoard = logic.stringToInt(temp.toString());
    }

    //resume a game from the database
    private void resumeGame() {
        combineBoards();
        emptyCells = logic.countEmptyCells(currentBoard);
        showCurrentGame(logic.stringToInt(initialBoard));
        showCurrentSolution();
        timer.startTimeThread(currentTime);
    }


    //Choose a random number, if that cell is empty, fill it in for the player
    @SuppressLint("SetTextI18n")
    private void getHint() {
        int random;

        while(emptyCells > 0) {

            //a random number from 0-80
            random = (int) (Math.random() * 81);

            //if that field is empty, fill it in
            if(logic.intToString(currentBoard).charAt(random) == '0') {

                int number = Character.getNumericValue(solution.charAt(random));
                int row = random / 9;
                int cell = random % 9;

                emptyCells--;
                currentBoard[row][cell] = number;
                updateUserSolution(row, cell);
                cellViews[row][cell].setText(Integer.toString(number));

                //punish the player for his arrogance
                timer.addMinute();

                break; // his will to live!
            }
        }

        //if all fields are filled, check if the solution is correct
        if(emptyCells == 0) {
            checkBoard();
        }
    }


    //a click listener to go back to the menu
    @Override
    public void onClick(View v) {
        if(v == goBack) {
            saveTimesToDatabase();
            goToMainMenu();
        }
        else if(v == getHint) {
            getHint();
        }
    }
}