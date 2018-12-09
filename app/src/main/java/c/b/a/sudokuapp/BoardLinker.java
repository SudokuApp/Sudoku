package c.b.a.sudokuapp;

import android.app.Activity;
import android.widget.TextView;

public class BoardLinker {

    public int[][] cellIDs;
    public TextView[][] cellViews;

    public BoardLinker(Activity a) {
        cellIDs = new int[][]{{R.id.cell00, R.id.cell01, R.id.cell02, R.id.cell03, R.id.cell04, R.id.cell05, R.id.cell06, R.id.cell07, R.id.cell08},
                {R.id.cell10, R.id.cell11, R.id.cell12, R.id.cell13, R.id.cell14, R.id.cell15, R.id.cell16, R.id.cell17, R.id.cell18},
                {R.id.cell20, R.id.cell21, R.id.cell22, R.id.cell23, R.id.cell24, R.id.cell25, R.id.cell26, R.id.cell27, R.id.cell28},
                {R.id.cell30, R.id.cell31, R.id.cell32, R.id.cell33, R.id.cell34, R.id.cell35, R.id.cell36, R.id.cell37, R.id.cell38},
                {R.id.cell40, R.id.cell41, R.id.cell42, R.id.cell43, R.id.cell44, R.id.cell45, R.id.cell46, R.id.cell47, R.id.cell48},
                {R.id.cell50, R.id.cell51, R.id.cell52, R.id.cell53, R.id.cell54, R.id.cell55, R.id.cell56, R.id.cell57, R.id.cell58},
                {R.id.cell60, R.id.cell61, R.id.cell62, R.id.cell63, R.id.cell64, R.id.cell65, R.id.cell66, R.id.cell67, R.id.cell68},
                {R.id.cell70, R.id.cell71, R.id.cell72, R.id.cell73, R.id.cell74, R.id.cell75, R.id.cell76, R.id.cell77, R.id.cell78},
                {R.id.cell80, R.id.cell81, R.id.cell82, R.id.cell83, R.id.cell84, R.id.cell85, R.id.cell86, R.id.cell87, R.id.cell88}};

        cellViews = new TextView[9][];
        for(int i = 0 ; i < 9 ; i++){
            cellViews[i] = new TextView[9];
        }

        for(int i = 0 ; i < 9 ; i++){
            for(int j = 0 ; j < 9 ; j ++){
                cellViews[i][j] = a.findViewById(cellIDs[i][j]);
            }
        }
    }

}
