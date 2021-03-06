package com.emcreations.tintvision.activity;

// QuadFlask. (2017) color picker for android (Version 0.0.13) [Computer software]. Retrieved from https://github.com/QuadFlask/colorpicker
import com.emcreations.tintvision.R;
import com.emcreations.tintvision.service.UnderlinerService;
import com.emcreations.tintvision.util.CustomFont;
import com.emcreations.tintvision.util.PermissionManager;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
// -- End of reference --
import com.emcreations.tintvision.util.Settings;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * Underliner settings activity for TintVision, handles the settings for the underliner tool
 * 
 * @author Edward McKnight (EM-Creations.co.uk)
 * @see UnderlinerService
 * @see OverlaySettingsActivity
 * @since 2017
 * @version 1.0
 */
public class UnderlinerSettingsActivity extends Activity {
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private PermissionManager permissionManager;

	/**
	 * On create method, which is run when the activity is first started
	 *
	 * @param savedInstanceState The saved instance of the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_underliner_settings);
		permissionManager = new PermissionManager(this);

		// Restore settings
		settings = getSharedPreferences(Settings.SETTINGS_NAME, 0);
		editor = settings.edit();

		// Controls
		final CompoundButton btnToggle = (CompoundButton) findViewById(R.id.underlinerToggle);
		final SeekBar thicknessBar = (SeekBar) findViewById(R.id.thicknessSeekBar);
		final SeekBar widthBar = (SeekBar) findViewById(R.id.widthSeekBar);
		final Button btnColour = (Button) findViewById(R.id.buttonUnderlinerColour);
		final TextView titleText = (TextView) findViewById(R.id.titleTextView);
		final TextView colourText = (TextView) findViewById(R.id.colourTextView);
		final TextView thicknessText = (TextView) findViewById(R.id.thicknessTextView);
		final TextView widthText = (TextView) findViewById(R.id.widthTextView);

		thicknessBar.setProgress(settings.getInt("underlinerThickness", 10)); // Set the selected thickness
		widthBar.setProgress(settings.getInt("underlinerWidth", 400)); // Set the selected width
		btnToggle.setChecked(settings.getBoolean("underlinerOn", false)); // Set the toggle

		// Set font
		CustomFont font = new CustomFont(getApplicationContext());
		titleText.setTypeface(font.getBoldFont());
		btnToggle.setTypeface(font.getRegularFont());
		btnColour.setTypeface(font.getRegularFont());
        colourText.setTypeface(font.getRegularFont());
        thicknessText.setTypeface(font.getRegularFont());
        widthText.setTypeface(font.getRegularFont());

		// Listeners
		btnColour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerDialogBuilder
						.with(UnderlinerSettingsActivity.this)
						.setTitle(getResources().getString(R.string.pick_colour))
						.initialColor(Color.parseColor(settings.getString("underlinerColour", "#000000")))
						.wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
						.density(9)
						.setOnColorSelectedListener(new OnColorSelectedListener() {
							@Override
							public void onColorSelected(int selectedColor) {
								//Toast.makeText(getApplicationContext(), "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
							}
						})
						.setPositiveButton(getResources().getString(R.string.answer_ok), new ColorPickerClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
								SharedPreferences.Editor editor = settings.edit();
								editor.putString("underlinerColour", "#" + Integer.toHexString(selectedColor));
								editor.apply(); // Save edits

								if (btnToggle.isChecked()) { // If the underliner is currently on
									// Restart the underliner
									stopService(new Intent(getApplicationContext(), UnderlinerService.class));
									startService(new Intent(getApplicationContext(), UnderlinerService.class));
								}
							}
						})
						.setNegativeButton(getResources().getString(R.string.answer_cancel), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						})
						.lightnessSliderOnly() // Only show the lightness slider
						.build()
						.show();
			}
		});

		btnToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { // If the toggle has been changed
				if (btnToggle.isChecked()) { // If the toggle is now checked
					permissionManager.checkDrawOverPermission();
					if (permissionManager.canDrawOverlays()) { // If we can draw overlays
						editor.putBoolean("underlinerOn", true);
						startService(new Intent(getApplicationContext(), UnderlinerService.class)); // Start the underliner
					} else { // If we can't draw overlays
						btnToggle.setChecked(false);
					}
				} else { // If the toggle is now unchecked
					editor.putBoolean("underlinerOn", false);
					stopService(new Intent(getApplicationContext(), UnderlinerService.class)); // Stop the underliner
				}
				editor.apply(); // Save edits
			}
		});

		thicknessBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				editor.putInt("underlinerThickness", progress);
				editor.commit(); // Save edits

				if (btnToggle.isChecked()) { // If the underliner is currently on
					// Restart the underliner
					stopService(new Intent(getApplicationContext(), UnderlinerService.class));
					startService(new Intent(getApplicationContext(), UnderlinerService.class));
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

		});

		widthBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				editor.putInt("underlinerWidth", progress);
				editor.commit(); // Save edits

				if (btnToggle.isChecked()) { // If the underliner is currently on
					// Restart the underliner
					stopService(new Intent(getApplicationContext(), UnderlinerService.class));
					startService(new Intent(getApplicationContext(), UnderlinerService.class));
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

		});
	}

	/**
	 * onCreateOptionsMenu
	 *
	 * @param menu the options menu
	 * @return boolean
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.underliner_settings, menu);
		return true;
	}

	/**
	 * onOptionsItemSelected
	 *
	 * @param item the menu item
	 * @return boolean
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
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
