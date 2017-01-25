package com.tintvision;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Overlay Settings activity for TintVision
 * 
 * @author Edward McKnight (EM-Creations.co.uk) - UP608985
 * @version 1.0
 */
public class OverlaySettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overlay_settings);

		final CompoundButton btnToggle = (CompoundButton) findViewById(R.id.overlayToggle);
		btnToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { // If the toggle has been changed
				if (btnToggle.isChecked()) { // If the toggle is now checked
					startService(new Intent(getApplicationContext(), OverlayService.class)); // Start the overlay
				} else { // If the toggle is now unchecked
					stopService(new Intent(getApplicationContext(), OverlayService.class)); // Stop the overlay
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.overlay_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
