package com.emcreations.tintvision;

import com.emcreations.tintvision.util.Settings;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Home activity (startup) for TintVision
 * 
 * @author Edward McKnight (EM-Creations.co.uk) - UP608985
 * @version 1.0
 */
public class HomeActivity extends Activity {
	private SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// Restore settings
		settings = getSharedPreferences(Settings.SETTINGS_NAME, 0);

		final Button btnOverlaySettings = (Button) findViewById(R.id.osButton);
		final Button btnUnderlinerSettings = (Button) findViewById(R.id.usButton);

		// Get settings and turn on overlay and underliner if necessary
		if (settings.getBoolean("overlayOn", false))
			startService(new Intent(getApplicationContext(), OverlayService.class));
		if (settings.getBoolean("underlinerOn", false))
			startService(new Intent(getApplicationContext(), UnderlinerService.class));

		// Listeners
		btnOverlaySettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { // If the overlay settings button has been clicked
				Intent i = new Intent(getApplicationContext(), OverlaySettingsActivity.class);
				startActivity(i); // Start the overlay settings activity
			}
		});

		btnUnderlinerSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { // If the underliner settings button has been clicked
				Intent i = new Intent(getApplicationContext(), UnderlinerSettingsActivity.class);
				startActivity(i); // Start the underliner settings activity
			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

}
