/**
 * 
 */
package org.willmanning.sudoku;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author wmanningie
 *
 */
public class Prefs extends PreferenceActivity {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		/*
		 * read the settings and inflate them
		 * into the current view
		 */
		addPreferencesFromResource(R.xml.settings);
	}

}
