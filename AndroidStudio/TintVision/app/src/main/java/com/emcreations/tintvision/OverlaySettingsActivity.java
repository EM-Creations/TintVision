package com.emcreations.tintvision;

// QuadFlask. (2017) color picker for android (Version 0.0.13) [Computer software]. Retrieved from https://github.com/QuadFlask/colorpicker
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
// -- End of reference --
import com.emcreations.tintvision.util.Settings;
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

/**
 * Overlay Settings activity for TintVision
 * 
 * @author Edward McKnight (EM-Creations.co.uk) - UP608985
 * @version 1.0
 */
public class OverlaySettingsActivity extends Activity {
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overlay_settings);

		// Restore settings
		settings = getSharedPreferences(Settings.SETTINGS_NAME, 0);
		editor = settings.edit();

		// Controls
		final CompoundButton btnToggle = (CompoundButton) findViewById(R.id.overlayToggle);
		final SeekBar barOpacity = (SeekBar) findViewById(R.id.opacityBar);
		final Button btnColour = (Button) findViewById(R.id.buttonOverlayColour);

		btnToggle.setChecked(settings.getBoolean("overlayOn", false)); // Set the toggle
		barOpacity.setProgress(settings.getInt("overlayOpacity", 80)); // Set the selected opacity

		// Listeners
		btnColour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerDialogBuilder
						.with(OverlaySettingsActivity.this)
						.setTitle("Pick colour")
						.initialColor(Color.parseColor(settings.getString("overlayColour", "#ffff00")))
						.wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
						.density(9)
						.setOnColorSelectedListener(new OnColorSelectedListener() {
							@Override
							public void onColorSelected(int selectedColor) {
								//Toast.makeText(getApplicationContext(), "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
							}
						})
						.setPositiveButton("ok", new ColorPickerClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
								SharedPreferences.Editor editor = settings.edit();
								editor.putString("overlayColour", "#" + Integer.toHexString(selectedColor));
								editor.commit(); // Save edits

								if (btnToggle.isChecked()) { // If the overlay is currently on
									// Restart the overlay
									stopService(new Intent(getApplicationContext(), OverlayService.class));
									startService(new Intent(getApplicationContext(), OverlayService.class));
								}
							}
						})
						.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
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
					editor.putBoolean("overlayOn", true);
					startService(new Intent(getApplicationContext(), OverlayService.class)); // Start the overlay
				} else { // If the toggle is now unchecked
					editor.putBoolean("overlayOn", false);
					stopService(new Intent(getApplicationContext(), OverlayService.class)); // Stop the overlay
				}
				editor.commit(); // Save edits
			}
		});

		barOpacity.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { // If the setting has changed
				editor.putInt("overlayOpacity", progress);
				editor.commit(); // Save edits

				if (btnToggle.isChecked()) { // If the overlay is currently on
					// Restart the overlay
					stopService(new Intent(getApplicationContext(), OverlayService.class));
					startService(new Intent(getApplicationContext(), OverlayService.class));
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
