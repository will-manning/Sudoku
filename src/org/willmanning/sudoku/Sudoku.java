package org.willmanning.sudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Sudoku extends Activity implements OnClickListener{
	
	private static final String TAG = "Sudoku";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //click listeners for buttons
        View continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        View newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
    }

    /**
     * {@inheritDoc}
     */
	public final void onClick(final View v) {
		switch(v.getId()) {
		case R.id.about_button:
			Intent intent = new Intent(this, About.class);
			startActivity(intent);
			break;
		case R.id.new_button:
			openNewGameDialog();
			break;
		case R.id.exit_button:
			finish();
			break;
		default:
			break;
		}
	}

	/**.
	 * opens the new game Dialog
	 */
	private void openNewGameDialog() {
		/*
		 * the set items takes the array of items and the
		 * listener for an onclick
		 */
		new AlertDialog.Builder(this).setTitle(R.string.new_game_title)
		.setItems(R.array.difficulty,
				new DialogInterface.OnClickListener() {

			/*
			 * The action to perform when an item is clicked
			 * (non-Javadoc)
			 * @see android.content.DialogInterface
			 * .OnClickListener#onClick
			 * (android.content.DialogInterface, int)
			 */
			public void onClick(final DialogInterface dialog,
					final int which) {
			 startGame(which);
			}

		}).show();

	}

	/**.
	 * start the game
	 *
	 * @param difficulty the difficulty selected when starting the game
	 */
	private void startGame(final int difficulty) {
		Log.d(TAG, "clicked on " + difficulty);
		//create an intent to fire up the activity
		Intent intent = new Intent(this, Game.class);
		intent.putExtra(Game.KEY_DIFFICULTY, difficulty);
		startActivity(intent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Prefs.class));
			return true;
		default:
			break;
		}
		return false;
	}

}