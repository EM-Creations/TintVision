package com.tintvision;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Home activity (startup) for TintVision
 * 
 * @author Edward McKnight (EM-Creations.co.uk) - UP608985
 * @version 1.0
 */
public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		final Button btnOverlaySettings = (Button) findViewById(R.id.osButton);
		btnOverlaySettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { // If the overlay settings button has been clicked

				Toast.makeText(getBaseContext(),"Button touched", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getApplicationContext(), OverlaySettingsActivity.class);
				startActivity(i); // Start the overlay settings activity
			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Start OverlayService which shows the overlay and UnderlinerService which shows the underliner tool
		//startService(new Intent(getApplicationContext(), OverlayService.class)); // Now turned on / off on setings page
		startService(new Intent(getApplicationContext(), UnderlinerService.class));
	}

}
