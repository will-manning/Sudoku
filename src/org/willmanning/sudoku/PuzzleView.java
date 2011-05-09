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
	
	/**
	 * The current selected Rectangle
	 */
	private final Rect selRect = new Rect();

	public PuzzleView(Context context)
	{
		super(context);
		this.game = (Game) context;
		setFocusable(true);
		setFocusableInTouchMode(true);

	}

	/**.
	 * {@inheritDoc}
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w / 9f;
		height = h / 9f;
		getRect(selX, selY, selRect);
		Log.d(TAG, "onSizeChanged: width " + width + ", height " 
				+ height);
		super.onSizeChanged(w, h, oldw, oldh);
	}


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
		
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
		
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
				.getColor(R.color.puzzle_background));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height * 0.75f);
		foreground.setTextScaleX(width / height);
		foreground.setTextAlign(Paint.Align.CENTER);
		
		FontMetrics fm = backGround.getFontMetrics();
		float x = width / 2;
		float y = height / 2 - (fm.ascent + fm.descent) / 2;
		for(int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
				canvas.drawText(this.game.getTileString(i, j), 
						i * width + x, j * height + y, foreground);
		}
		
		/*
		 * draw the selection
		 */
		Log.d(TAG, "selRect" + selRect);
		
		Paint selected = new Paint();
		selected.setColor(getResources().getColor(R.color.puzzle_selected));
		
		canvas.drawRect(selRect, selected);		
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		Log.d(TAG, "onKeyDown; keycode=" + keyCode + ", event=" + event);
//
//		switch (keyCode) {
//		case KeyEvent.KEYCODE_DPAD_UP:
//			select(selX, selY - 1);
//			break;
//		case KeyEvent.KEYCODE_DPAD_DOWN:
//			select(selX, selY + 1);
//			break;
//		case KeyEvent.KEYCODE_DPAD_LEFT:
//			select(selX - 1, selY);
//			break;
//		case KeyEvent.KEYCODE_DPAD_RIGHT:
//			select(selX = 1, selY);
//			break;
//			
//		case KeyEvent.KEYCODE_0:
//		case KeyEvent.KEYCODE_SPACE:
//			setSelectedTile(0);
//			break;
//		case KeyEvent.KEYCODE_1:
//			setSelectedTile(1);
//			break;
//		case KeyEvent.KEYCODE_2:
//			setSelectedTile(2);
//			break;
//		case KeyEvent.KEYCODE_3:
//			setSelectedTile(3);
//			break;
//		case KeyEvent.KEYCODE_4:
//			setSelectedTile(4);
//			break;
//		case KeyEvent.KEYCODE_5:
//			setSelectedTile(5);
//			break;
//		case KeyEvent.KEYCODE_6:
//			setSelectedTile(6);
//			break;
//		case KeyEvent.KEYCODE_7:
//			setSelectedTile(7);
//			break;
//		case KeyEvent.KEYCODE_8:
//			setSelectedTile(8);
//			break;
//		case KeyEvent.KEYCODE_9:
//			setSelectedTile(9);
//			break;
//		case KeyEvent.KEYCODE_ENTER:
//		case KeyEvent.KEYCODE_DPAD_CENTER:
//			game.showKeypadOrError(selX, selY);
//			break;
//
//		default:
//			return super.onKeyDown(keyCode, event);			
//		}
//		return true;
//	}
	
	private void select(int x, int y)
	{
		invalidate(selRect);
		selX = Math.min(Math.max(x, 0), 8);
		selY = Math.min(Math.max(y, 0), 8);
		getRect(selX, selY, selRect);
		invalidate(selRect);
	}
	
	public void setSelectedTile()
	{
		
	}
	
//	/**
//	 * Check if the passed value is valid for
//	 * the selected tile
//	 * 
//	 * @param x
//	 * @param y
//	 * @param value
//	 * @return
//	 */
//	protected boolean setTileIfValid(int x, int y, int value){
//		
//	}


}
