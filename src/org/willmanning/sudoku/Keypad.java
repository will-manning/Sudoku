package org.willmanning.sudoku;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**.
 *
 * A keypad that just shows teh available numbers
 * for the selected tile
 *
 * @author wmanningie
 *
 */
public class Keypad extends Dialog {

	/**.
	 * used for easy identification in logging
	 */
	private static final String TAG = "Sudoku";

	/**.
	 * the numbers not available for the
	 * selected rectangle
	 */
	private int[] usedNums;

	/**.
	 * The puzzle view
	 */
	private PuzzleView puzzleView;

	/**.
	 * Store the keys so that listeners can be
	 * added
	 */
	private final View[] keys = new View[9];

	/**.
	 * Store the keyboard so that a listener can be
	 * added
	 */
	private View keypad;

	/**.
	 * The  constructor just sets local variables with
	 * those passed in
	 *
	 * @param context the calling context
	 * @param usedNumsIn the numbers not available for selection
	 * @param puzzleViewIn the puzzle view
	 */
	public Keypad(final Context context, final int[] usedNumsIn
			, final PuzzleView puzzleViewIn) {
		super(context);

		usedNums = usedNumsIn;
		puzzleView = puzzleViewIn;
	}

	/**.
	 * {@inheritDoc}
	 */
	@Override
	protected final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//set the title
		setTitle(R.string.keypad_title);

		//set the content view using the layout xml file
		setContentView(R.layout.keypad);

		//add all the keys and the keyboard
		//to local variables so that listeners
		//can be added
		getKeys();

		//make used keys invisible
		for (int number : usedNums) {
			if (number != 0) {
				keys[number - 1].setVisibility(View.INVISIBLE);
			}
		}

		//set the listeners
		setListeners();
	}

	/**.
	 * Allow the user to use the keypad to select a number
	 * {@inheritDoc}
	 */
	@Override
	public final boolean onKeyDown(final int keyCode,
			final KeyEvent event) {
		int value = 0;

		//switch to set value based on input
		switch (keyCode) {
		case KeyEvent.KEYCODE_0:
			//fall though
		case KeyEvent.KEYCODE_SPACE:
			value = 0;
			break;
		case KeyEvent.KEYCODE_1:
			value = 1;
			break;
		case KeyEvent.KEYCODE_2:
			value = 2;
			break;
		case KeyEvent.KEYCODE_3:
			value = 3;
			break;
		case KeyEvent.KEYCODE_4:
			value = 4;
			break;
		case KeyEvent.KEYCODE_5:
			value = 5;
			break;
		case KeyEvent.KEYCODE_6:
			value = 6;
			break;
		case KeyEvent.KEYCODE_7:
			value = 7;
			break;
		case KeyEvent.KEYCODE_8:
			value = 8;
			break;
		case KeyEvent.KEYCODE_9:
			value = 9;
			break;
		default:
			//for default just call super
			super.onKeyDown(keyCode, event);
			break;
		}

		//if the value is valid then set the tile
		if (isValid(value)) {
			setTileNumber(value);
		}

		return super.onKeyDown(keyCode, event);
	}

	/**.
	 * Double check that the number is valid
	 * for the tile
	 *
	 * @param value the value to set the tile to
	 * @return
	 */
	private Boolean isValid(int value)
	{
		for (int i : usedNums) {
			if (value == i) {
				return false;
			}
		}

		return true;
	}

	/**.
	 * Get all the keys and the keyboard so that
	 * listeners can be added later
	 */
	private void getKeys() {
		keypad = findViewById(R.id.keypad);
		keys[0] = findViewById(R.id.keypad_1);
		keys[1] = findViewById(R.id.keypad_2);
		keys[2] = findViewById(R.id.keypad_3);
		keys[3] = findViewById(R.id.keypad_4);
		keys[4] = findViewById(R.id.keypad_5);
		keys[5] = findViewById(R.id.keypad_6);
		keys[6] = findViewById(R.id.keypad_7);
		keys[7] = findViewById(R.id.keypad_8);
		keys[8] = findViewById(R.id.keypad_9);
	}

	/**.
	 * Add listeners to the keys and the keyboard
	 *
	 * The listeners just return a number corresponding
	 * to the key touched
	 */
	private void setListeners()	{

		//for each key add a listener that return
		//a number#
		for (int i = 0; i < keys.length; i++) {
			//this need to be final so that the inner class
			//can access it
			final int value = i + 1;
			keys[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(final View v) {
					setTileNumber(value);
				}
			});
		}

		//the keypad returns 0
		keypad.setOnClickListener(new View.OnClickListener() {

			public void onClick(final View v) {
				setTileNumber(0);
			}
		});

	}	

	/**.
	 * Set the selected tile to the passed value
	 *
	 * @param number teh value to set on the tile
	 */
	private void setTileNumber(final int number) {
		puzzleView.setSelectedTile(number);
		//close the keypad dialog
		dismiss();
	}

}
