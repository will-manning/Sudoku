/**
 * 
 */
package org.willmanning.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author wmanningie
 *
 */
public class PuzzleView extends View {

	/**.
	 * A logging Tag
	 */
	private static final String TAG = "Sudoku";

	/**.
	 * store the game
	 */
	private final Game game;


	/**.
	 * the width of a square
	 */
	private float width;
	
	/**.
	 * the height of a square
	 */
	private float height;
	
	/**
	 * hold on to the current selected
	 * tile's x position
	 */
	private int selX;
	
	/**
	 * hold on to the current selected 
	 * tile's y position
	 */
	private int selY;

	/**.
	 * The current selected Rectangle
	 */
	private final Rect selRect = new Rect();

	/**.
	 * the grid size e.g. 9*9
	 */
	private final int gridSize = 9;

	/**.
	 * The amount of mini grids the
	 * full grid is divided into e.g. 3*3
	 */
	private final int subGridSize = 3;

	/**
	 *Create a Puzzle view. The ocntext passed
	 *is cast to a game and used in this
	 *puzzle view.
	 *
	 * @param context game
	 */
	public PuzzleView(final Context context) {
		super(context);
		this.game = (Game) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	/**.
	 * {@inheritDoc}
	 */
	@Override
	protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w / 9f;
		height = h / 9f;
		getRect(selX, selY, selRect);
		Log.d(TAG, "onSizeChanged: width " + width + ", height " 
				+ height);
		super.onSizeChanged(w, h, oldw, oldh);
	}


	/**.
	 * move the rectangle
	 *
	 * @param x
	 * @param y
	 * @param rect
	 */
	private void getRect(int x, int y, Rect rect) {
		rect.set((int) (x * width), (int) (y * height),
				(int) (x * width + width),
				(int) (y * height + height));

	}

	@Override
	protected void onDraw(Canvas canvas) {


		/*draw the back ground
		 *  a paint is used to draw bitmaps, text
		 *  and geometric shapes
		 *  just draw a solid background
		 */
		Paint backGround = new Paint();
		backGround.setColor(getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), backGround);
		
		/*
		 * Draw the board
		 * Put the grid lines on the board
		 */
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));
		
		/*
		 * 
		 */
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
		
		/*
		 * 
		 */
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.puzzle_light));
		
		/*
		 * draw the inner grid lines
		 */
		for(int i = 0; i < 9; i++)
		{
			canvas.drawLine(0, i * height, getWidth(), i * height
					, light);
			//add a little highlight to give depth
			canvas.drawLine(0, i * height + 1, getWidth(),
					i * height + 1, hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight()
					, light);
			//add a little highlight to give depth
			canvas.drawLine(i * width + 1, 0, i * width + 1
					, getHeight(), hilite);
		}
		
		/*
		 * draw the outer grid lines
		 */
		for(int i = 0; i < 9; i++)
		{
			if(i % 3 != 0)
				continue;
			canvas.drawLine(0, i * height, getWidth(), i * height
					, dark);
			//add a little highlight to give depth
			canvas.drawLine(0, i * height + 1, getWidth(),
					i * height + 1, hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight()
					, dark);
			//add a little highlight to give depth
			canvas.drawLine(i * width + 1, 0, i * width + 1
					, getHeight(), hilite);
		}
		
		/*
		 * draw the numbers
		 */
		//color and style
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources()
				.getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height * 0.75f);
		foreground.setTextScaleX(width / height);
		foreground.setTextAlign(Paint.Align.CENTER);
		
		FontMetrics fm = backGround.getFontMetrics();
		float x = width / 2;
		/*
		 * ascent and descent are the recommended distance 
		 * between the letter and the top
		 * and bottom lines so take them into consideration  
		 */
		float y = height / 2 - (fm.ascent + fm.descent) / 2;
		
		/*
		 * nine by nine grid
		 */
		for(int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++)
				canvas.drawText(this.game.getTileString(i, j),
						i * width + x, j * height + y,
						foreground);
		}

		/*
		 * *************************************************
		 * BEGIN: draw the selection of a grid rectangle
		 * *************************************************
		 */
		Log.d(TAG, "selRect" + selRect);

		/*
		 * this color is alpha blended which means it's translucent
		 */
		Paint selected = new Paint();
		selected.setColor(getResources()
				.getColor(R.color.puzzle_selected));

		//draw the selected tile in a different color
		canvas.drawRect(selRect, selected);

		/*
		 * ***************************************************
		 * END: draw the selection of a grid rectangle
		 * ***************************************************
		 */
		/*
		 * *************************************************
		 * BEGIN: Add hints
		 * change the colour of the tile depending on how
		 * many possible values there are
		 * *************************************************
		 */
		//create a paint
		Paint hint = new Paint();

		//create an array of colors from the
		//resources
		int[] hintColours = {getResources()
				.getColor(R.color.puzzle_hint_0),
				getResources().getColor(R.color.puzzle_hint_1),
				getResources().getColor(R.color.puzzle_hint_2)};

		//create a new rect
		Rect rect = new Rect();

		//loop through the tile
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++)	{
				//get the moves left
				int movesLeft = 9 - game.getUsedTiles(i, j)
				.length;

				//if moves left is three or less
				if (movesLeft < hintColours.length &&
						movesLeft > 0) {

					getRect(i, j, rect);

					//paint it the right colour
					hint
					.setColor(hintColours[movesLeft - 1]);

					Log.d(TAG, "movesLeft " + movesLeft);

					canvas.drawRect(rect, hint);
				}
			}
		}


		/*
		 * ***************************************************
		 * END: add hints
		 * ***************************************************
		 */
	}

	/**.
	 *{@inheritDoc}
	 *
	 *Add functionality to handle the direction keys as
	 *well as number keys
	 */
	@Override
	public final boolean onKeyDown(final int keyCode,
			final KeyEvent event) {
		Log.d(TAG, "onKeyDown; keycode=" + keyCode
				+ ", event=" + event);

		/*
		 * up, down, left, right
		 */
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			select(selX, selY - 1);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			select(selX, selY + 1);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			select(selX - 1, selY);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			select(selX + 1, selY);
			break;

		//number selection on the keypad
		case KeyEvent.KEYCODE_0:
			//fall through
		case KeyEvent.KEYCODE_SPACE:
			setSelectedTile(0);
			break;
		case KeyEvent.KEYCODE_1:
			setSelectedTile(1);
			break;
		case KeyEvent.KEYCODE_2:
			setSelectedTile(2);
			break;
		case KeyEvent.KEYCODE_3:
			setSelectedTile(3);
			break;
		case KeyEvent.KEYCODE_4:
			setSelectedTile(4);
			break;
		case KeyEvent.KEYCODE_5:
			setSelectedTile(5);
			break;
		case KeyEvent.KEYCODE_6:
			setSelectedTile(6);
			break;
		case KeyEvent.KEYCODE_7:
			setSelectedTile(7);
			break;
		case KeyEvent.KEYCODE_8:
			setSelectedTile(8);
			break;
		case KeyEvent.KEYCODE_9:
			setSelectedTile(9);
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}
	
	/**.
	 * show a custom keypad if the user touches a tile
	 * {@inheritDoc}
	 */
	@Override
	public final boolean onTouchEvent(MotionEvent event) {
		//if teh event isn't an action down call super
		if (event.getAction() != MotionEvent.ACTION_DOWN) {
			return super.onTouchEvent(event);
		}

		//select the touched tile
		select((int) (event.getX() / width),
				(int) (event.getY() / height));

		//show the keypad
		game.showKeypadOrError(selX, selY);

		return true;
	}

	/**.
	 * set the tile as selected by changing the color
	 *
	 * @param x horizontal grid position
	 * @param y vertical grid postion
	 */
	private void select(final int x, final int y) {
		//tell android to redraw
		invalidate(selRect);
		//between 0 and 8
		selX = Math.min(Math.max(x, 0), 8);
		selY = Math.min(Math.max(y, 0), 8);
		//get the rectangle
		getRect(selX, selY, selRect);
		//tell android to redraw just this rectangle
		invalidate(selRect);
	}

	/**.
	 * set the selected tiles value if it's valid
	 *
	 * @param value the value to set the tile
	 */
	public final void setSelectedTile(final int value) {
		//if the value is valid set the tile
		if (game.setTileIfValid(selX, selY, value)) {
			//redraw
			invalidate();
		} else {
			//lets just log it
			Log.d(TAG, "Value " + value + " is invalid for "
					+ selX + " , " + selY);
		}

	}
}
