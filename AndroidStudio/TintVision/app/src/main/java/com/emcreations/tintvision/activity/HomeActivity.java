package com.emcreations.tintvision.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.emcreations.tintvision.R;
import com.emcreations.tintvision.service.OverlayService;
import com.emcreations.tintvision.service.UnderlinerService;
import com.emcreations.tintvision.util.CustomFont;
import com.emcreations.tintvision.util.PermissionManager;
import com.emcreations.tintvision.util.Settings;

/**
 * Home activity (startup) for TintVision
 * 
 * @author Edward McKnight (EM-Creations.co.uk)
 * @see Settings
 * @since 2017
 * @version 1.0
 */
public class HomeActivity extends Activity {
	private SharedPreferences settings;
	private PermissionManager permissionManager;

	// TODO: Make TintVision work on API greater than 26 (problem with creating window 2006 with current system) - Investigate TYPE_ACCESSIBILITY_OVERLAY
	/**
	 * On create method, which is run when the activity is first started
	 *
	 * @param savedInstanceState The saved instance of the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		permissionManager = new PermissionManager(this); // Instantiate a permission manager object

		// Restore settings
		settings = getSharedPreferences(Settings.SETTINGS_NAME, 0);

		// Controls
		final Button btnOverlaySettings = (Button) findViewById(R.id.osButton);
		final Button btnUnderlinerSettings = (Button) findViewById(R.id.usButton);
		final TextView titleText = (TextView) findViewById(R.id.titleTextView);

        // Set font
        CustomFont font = new CustomFont(getApplicationContext());
        titleText.setTypeface(font.getBoldFont());
        btnOverlaySettings.setTypeface(font.getBoldFont());
        btnUnderlinerSettings.setTypeface(font.getBoldFont());

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

		permissionManager.checkDrawOverPermission();
	}

	/**
	 * Call back for activity result
	 *
	 * @param requestCode int
	 * @param resultCode int
	 * @param data Intent
	 */
	@TargetApi(23)
	protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
		permissionManager.handleActivityResult(requestCode, resultCode, data);
	}
}
