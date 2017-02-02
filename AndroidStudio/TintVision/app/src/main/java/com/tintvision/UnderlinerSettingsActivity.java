package com.tintvision;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.tintvision.util.Settings;
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
 * Underliner settings activity for TintVision
 * 
 * @author Edward McKnight (EM-Creations.co.uk) - UP608985
 * @version 1.0
 */
public class UnderlinerSettingsActivity extends Activity {
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_underliner_settings);

		// Restore settings
		settings = getSharedPreferences(Settings.SETTINGS_NAME, 0);
		editor = settings.edit();

		// Controls
		final CompoundButton btnToggle = (CompoundButton) findViewById(R.id.underlinerToggle);
		final SeekBar thicknessBar = (SeekBar) findViewById(R.id.thicknessSeekBar);
		final SeekBar widthBar = (SeekBar) findViewById(R.id.widthSeekBar);
		final Button btnColour = (Button) findViewById(R.id.buttonUnderlinerColour);

		thicknessBar.setProgress(settings.getInt("underlinerThickness", 10)); // Set the selected thickness
		widthBar.setProgress(settings.getInt("underlinerWidth", 400)); // Set the selected width
		btnToggle.setChecked(settings.getBoolean("underlinerOn", false)); // Set the toggle

		// Listeners
		btnColour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerDialogBuilder
						.with(UnderlinerSettingsActivity.this)
						.setTitle("Pick colour")
						.initialColor(Color.parseColor(settings.getString("underlinerColour", "#000000")))
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
								editor.putString("underlinerColour", "#" + Integer.toHexString(selectedColor));
								editor.commit(); // Save edits

								if (btnToggle.isChecked()) { // If the underliner is currently on
									// Restart the underliner
									stopService(new Intent(getApplicationContext(), UnderlinerService.class));
									startService(new Intent(getApplicationContext(), UnderlinerService.class));
								}
							}
						})
						.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						})
						.build()
						.show();
			}
		});

		btnToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { // If the toggle has been changed
				if (btnToggle.isChecked()) { // If the toggle is now checked
					editor.putBoolean("underlinerOn", true);
					startService(new Intent(getApplicationContext(), UnderlinerService.class)); // Start the underliner
				} else { // If the toggle is now unchecked
					editor.putBoolean("underlinerOn", false);
					stopService(new Intent(getApplicationContext(), UnderlinerService.class)); // Stop the underliner
				}
				editor.commit(); // Save edits
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.underliner_settings, menu);
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
