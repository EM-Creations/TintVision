package com.emcreations.tintvision;

import com.emcreations.tintvision.util.Settings;
import com.emcreations.tintvision.util.CustomFont;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Home activity (startup) for TintVision
 * 
 * @author Edward McKnight (EM-Creations.co.uk) - UP608985
 * @see Settings
 * @since 2017
 * @version 1.0
 */
public class HomeActivity extends Activity {
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private final static int REQUEST_CODE = 333;

	// TODO: Make TintVision work on API greater than 26 (problem with creating window 2006 with current system)
	/**
	 * On create method, which is run when the activity is first started
	 *
	 * @param savedInstanceState The saved instance of the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// Restore settings
		settings = getSharedPreferences(Settings.SETTINGS_NAME, 0);
		editor = settings.edit();

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

		if (Build.VERSION.SDK_INT >= 23) { // If API 23 or above, we need to ask for permission
			checkDrawOverlayPermission();
		} else { // Otherwise, we'd already have permission due to tbe AndroidManifest
			editor.putBoolean("canDrawOverlays", true);
			editor.apply();
		}
	}

	/**
	 * Check whether we have permission to draw overlays
	 */
	@TargetApi(23)
	private void checkDrawOverlayPermission() {
		if (!android.provider.Settings.canDrawOverlays(getApplicationContext())) { // If we don't have permission to draw overlays
			Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
					Uri.parse("package:" + getPackageName()));
			startActivityForResult(intent, REQUEST_CODE); // Request permission
		} else {
			setCanDrawOverlay(true);
		}
	}

	/**
	 * Call back after asking for permission to draw overlays
	 *
	 * @param requestCode int
	 * @param resultCode int
	 * @param data Intent
	 */
	@TargetApi(23)
	@Override
	protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
		if (requestCode == REQUEST_CODE) { // Request codes match
			setCanDrawOverlay(android.provider.Settings.canDrawOverlays(getApplicationContext()));
		}
	}

	/**
	 * Set whether or not the overlays can be drawn
	 *
	 * @param b Boolean
	 */
	private void setCanDrawOverlay(boolean b) {
		editor.putBoolean("canDrawOverlays", b);
		editor.apply();
	}

}
