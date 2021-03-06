package com.emcreations.tintvision.activity;

// QuadFlask. (2017) color picker for android (Version 0.0.13) [Computer software]. Retrieved from https://github.com/QuadFlask/colorpicker
import com.emcreations.tintvision.R;
import com.emcreations.tintvision.service.OverlayService;
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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
 * Overlay Settings activity for TintVision, handles the settings for the overlay
 *
 * @author Edward McKnight (EM-Creations.co.uk)
 * @see OverlayService
 * @see UnderlinerSettingsActivity
 * @since 2017
 * @version 1.0
 */
public class OverlaySettingsActivity extends Activity {
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
    private int oldOpacity;
    private String oldColour;
    private CompoundButton btnToggle;
	private boolean alertActive = false;
	private final String readingTestURL = "http://www.em-creations.co.uk/apps/readingtest.html";
	private PermissionManager permissionManager;

	/**
	 * On create method, which is run when the activity is first started
	 *
	 * @param savedInstanceState The saved instance of the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overlay_settings);
		permissionManager = new PermissionManager(this);

		// Restore settings
		settings = getSharedPreferences(Settings.SETTINGS_NAME, 0);
		editor = settings.edit();
        oldOpacity = settings.getInt("overlayOpacity", 80);
        oldColour = settings.getString("overlayColour", "#ffff00");

		// Controls
        btnToggle = (CompoundButton) findViewById(R.id.overlayToggle);
		final SeekBar barOpacity = (SeekBar) findViewById(R.id.opacityBar);
		final Button btnColour = (Button) findViewById(R.id.buttonOverlayColour);
		final TextView titleText = (TextView) findViewById(R.id.titleTextView);
        final TextView opacityText = (TextView) findViewById(R.id.opacityTextView);
        final TextView colourText = (TextView) findViewById(R.id.colourTextView);

		btnToggle.setChecked(settings.getBoolean("overlayOn", false)); // Set the toggle
		barOpacity.setProgress(settings.getInt("overlayOpacity", 80)); // Set the selected opacity

		// Set font
        CustomFont font = new CustomFont(getApplicationContext());
		titleText.setTypeface(font.getBoldFont());
		btnToggle.setTypeface(font.getRegularFont());
        opacityText.setTypeface(font.getRegularFont());
        colourText.setTypeface(font.getRegularFont());
        btnColour.setTypeface(font.getRegularFont());

		// Listeners
		btnColour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerDialogBuilder
						.with(OverlaySettingsActivity.this)
						.setTitle(getResources().getString(R.string.pick_colour))
						.initialColor(Color.parseColor(settings.getString("overlayColour", "#ffff00")))
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
								editor.putString("overlayColour", "#" + Integer.toHexString(selectedColor));
								editor.apply(); // Save edits

								if (btnToggle.isChecked()) { // If the overlay is currently on
									// Restart the overlay
									stopService(new Intent(getApplicationContext(), OverlayService.class));
									startService(new Intent(getApplicationContext(), OverlayService.class));
                                    readingTestCheck();
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
						editor.putBoolean("overlayOn", true);
						startService(new Intent(getApplicationContext(), OverlayService.class)); // Start the overlay
						readingTestCheck();
					} else { // If we can't draw overlays
						btnToggle.setChecked(false);
					}
				} else { // If the toggle is now unchecked
					editor.putBoolean("overlayOn", false);
					stopService(new Intent(getApplicationContext(), OverlayService.class)); // Stop the overlay
				}
				editor.apply(); // Save edits
			}
		});

		barOpacity.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { // If the setting has changed
				editor.putInt("overlayOpacity", progress);
				editor.apply(); // Save edits
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
                if (btnToggle.isChecked()) { // If the overlay is currently on
                    // Restart the overlay
                    stopService(new Intent(getApplicationContext(), OverlayService.class));
                    startService(new Intent(getApplicationContext(), OverlayService.class));
                }
                readingTestCheck();
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
		getMenuInflater().inflate(R.menu.overlay_settings, menu);
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
     * Check if the user could take a reading test or not
     */
    private void readingTestCheck() {
        // Check if the overlay's settings have changed
        if ((this.oldOpacity != settings.getInt("overlayOpacity", 80) || !this.oldColour.equals(settings.getString("overlayColour", "#ffff00"))) && btnToggle.isChecked() && !alertActive) {
			alertActive = true;
            // Update the old values to the new ones
            this.oldOpacity = settings.getInt("overlayOpacity", 80);
            this.oldColour = settings.getString("overlayColour", "#ffff00");
            AlertDialog alertDialog = new AlertDialog.Builder(OverlaySettingsActivity.this).create();
            alertDialog.setTitle(getResources().getText(R.string.reading_test_title));
            alertDialog.setMessage(getResources().getText(R.string.reading_test_question));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getText(R.string.answer_yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
							alertActive = false;
                            dialog.dismiss();
                            // Open the reading test
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(readingTestURL));
                            startActivity(browserIntent);
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getText(R.string.answer_no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
							alertActive = false;
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
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
