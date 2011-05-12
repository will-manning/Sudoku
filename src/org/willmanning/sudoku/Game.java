/**
 * 
 */
package org.willmanning.sudoku;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author wmanningie
 *
 */
public class Game extends Activity {

	/**.
	 * initial numbers for easy puzzle
	 */
	private final String easyPuzzle =
	      "360000000004230800000004200" +
	      "070460003820000014500013020" +
	      "001900000007048300000000045";
	/**.
	 * initial numbers for medium puzzle
	 */
	   private final String mediumPuzzle =
	      "650000070000506000014000005" +
	      "007009000002314700000700800" +
	      "500000630000201000030000097";
   /**.
	 * initial numbers for hard puzzle
	 */
   private final String hardPuzzle =
      "009000000080605020501078000" +
      "000000700706040102004000000" +
      "000720903090301080000000600";

	/**.
	 * A logging Tag
	 */
	private static final String TAG = "Sudoku";

	/**.
	 * A key to identify the difficulty extraData param
	 */
	protected static final String KEY_DIFFICULTY =
		"org.willmanning.sudoku.difficulty";
	/**.
	 * int to id easy game
	 */
	private static final int DIFFICULTY_EASY = 0;
	/**.
	 * int to id medium game
	 */
	private static final int DIFFICULTY_MEDIUM = 1;
	/**.
	 * int to id hard game
	 */
	private static final int DIFFICULTY_HARD = 3;

	/**
	 * An array of teh puzzle numbers
	 */
	private int puzzle[];

	/**
	 * The puzzleView created
	 */
	private PuzzleView puzzleView;
	
	private final int gridSize = 9;
	
	/**
	 * store tha already used number for a given tile
	 * for example if a number has been used anywhere
	 * vertically, horizontally or within the same
	 * sub grid then it cannot be used again.
	 */
	private final int usedTiles[][][] = new int[gridSize][gridSize][];
	
	
	
	/**.
	 * {@inheritDoc}
	 */
	@Override
	protected final void onCreate(final Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "onCreate");
		
		int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		puzzle = getPuzzle(diff);
		calculateUsedTiles();
		
		puzzleView = new PuzzleView(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();
	}
	
	private int[] getPuzzle(int diff)
	{
		String puz;
		
		switch (diff){
			case DIFFICULTY_EASY:
				puz = easyPuzzle;
				break;
			case DIFFICULTY_MEDIUM:
				puz = mediumPuzzle;
				break;
			case DIFFICULTY_HARD:
				puz = hardPuzzle;
				break;
			default:
				puz = easyPuzzle;
		}
		return fromPuzzleString(puz);
	}
	
	/**.
	 * and array of integers to a string
	 * @param puz
	 * @return
	 */
	static private String toPuzzleString(int[] puz){
		StringBuilder buf = new StringBuilder();
		for(int element: puz)
		{
			buf.append(element);
		}
		return buf.toString();
	}
	
	static private int[] fromPuzzleString(String string)
	{
		int[] puz = new int[string.length()];
		for(int i =0; i < puz.length; i++)
		{
			puz[i] = string.charAt(i) - '0';
		}
		return puz;
	}

	/**.
	 * return the value of the given tile as a
	 * string
	 *
	 * @param x the x position
	 * @param y the x position
	 * @return the value of the tile as a string
	 */
	protected final String getTileString(final int x ,final int y) {
		int v = getTile(x, y);
		if (v == 0) {
			return "";
		} else {
			return String.valueOf(v);
		}
	}

	/**.
	 * return the tile for the given position
	 *
	 * @param x the x position
	 * @param y the x position
	 * @return the tile
	 */
	private int getTile(int x, int y) {
		return puzzle[y * gridSize + x];
	}

	/**.
	 * set the tile to the given value
	 *
	 * @param x the x position
	 * @param y the x position
	 * @param value the value to set
	 */
	private void setTile(final int x, final int y, final int value)	{
		puzzle[y * gridSize + x] = value;
	}

	/**.
	 * Get the tiles used values
	 * You can't set a tile to a value that's in here
	 *
	 * @param x the x position
	 * @param y the x position
	 * @return teh used values
	 */
	protected final int[] getUsedTiles(final int x, final int y) {
		return usedTiles[x][y];
	}

	/**.
	 * If there is a number in the cell it
	 * can not appear in that column, row
	 * or mini grid again
	 *
	 * e.g. for the top left cell
	 * if there is a 9 in any of the three locations
	 * this cell can not be 9
	 *
	 * This calculates used numbers for a single tile
	 *
	 * @param x teh x coord
	 * @param y the y coord
	 * @return the used values in this tiles range
	 */
	private int[] calculateUsedTiles(final int x, final int y) {

		Log.d(TAG, "calculate used tiles");

		int[] usedTilesIn = new int[gridSize];

		//horizntal
		for (int i = 0; i < gridSize; i++) {
			if (i == x) {
				continue;
			}
			int tile = getTile(i, y);
			//don't care about zeros
			if (tile != 0) {
				/*we can overwrite because we
				 * don't care about duplicates
				 */
				usedTilesIn[tile - 1] = tile;
			}
		}

		//vertical
		for (int i = 0; i < gridSize; i++) {
			if (i == y) {
				continue;
			}
			int tile = getTile(x, i);
			//don't care about zeros
			if (tile != 0) {
				usedTilesIn[tile - 1] = tile;
			}
		}

		//cell block
		/*
		 * (x / 3) is an int which means if x is
		 * 2 start x will be 0
		 * if x is 5 startx will be 1 etc
		 */
		int startx = (x / 3) * 3;
		int starty = (y / 3) * 3;

		for (int i = startx; i < startx + 3; i++) {
			for (int j = starty; j < starty + 3; j++) {
				if (i == x && j == y) {
					continue;
				}
				int tile = getTile(i, j);
				if (tile != 0) {
					usedTilesIn[tile - 1] = tile;
				}
			}
		}

		/*
		 * time to strip out those zeros
		 */

		int used = 0;
		for (int i : usedTilesIn) {
			if (i != 0) {
				used++;
			}
		}

		int[] usedTiles1 = new int[used];
		used = 0;
		for (int i : usedTilesIn) {
			if (i != 0) {
				usedTiles1[used++] = i;
			}
		}

		return usedTiles1;
	}

	/**.
	 * Check if the passed value is valid for
	 * the selected tile
	 *
	 * if it is set it
	 *
	 * @param x the x coord
	 * @param y the y coord
	 * @param value the value to check
	 * @return if tile was set or not
	 */
	protected final boolean setTileIfValid(final int x,
			final int y, final int value) {
		//get the used values in this tiles range
		int[] tiles = getUsedTiles(x, y);

		//loop through to check if this number is there
		for (int t : tiles) {
			if (t == value) {
				return false;
			}
		}
		//else set the tile
		setTile(x, y, value);

		//recalculate the used numbers for this tile
		calculateUsedTiles();

		return true;
	}

	/**.
	 * calculate the used values for each tile
	 * on the game board
	 */
	private void calculateUsedTiles() {
		/*
		 * in a 9*9 loop calculate the used
		 * values for each tile
		 */
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j< 9; j++) {
				usedTiles[i][j] = calculateUsedTiles(i, j);
			}
		}
	}


}
