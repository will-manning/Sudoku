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

	private int puzzle[];

	private PuzzleView puzzleView;
	
	/**.
	 * {@inheritDoc}
	 */
	@Override
	protected final void onCreate(final Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "onCreate");
		
		int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		puzzle = getPuzzle(diff);
		//calculateUsedTiles();
		
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
	
	
	protected String getTileString(int x , int y){
		int v = getTile(x, y);
		if(v == 0)
			return "";
		else {
			return String.valueOf(v);
		}
	}
	
	private int getTile(int x, int y) {
		return puzzle[y * 9 + x];
	}
	
	private void setTile(int x, int y, int value)
	{
		puzzle[y * 9 + x] = value;
	}
	
	 /** Cache of used tiles */
	   private final int used[][][] = new int[9][9][];

	   /** Return cached used tiles visible from the given coords */
	   protected int[] getUsedTiles(int x, int y) {
	      return used[x][y];
	   }

}
