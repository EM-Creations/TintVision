package com.tintvision;

import com.tintvision.util.FilterView;
import com.tintvision.util.Settings;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Overlay service for TintVision
 * 
 * @author Edward McKnight (EM-Creations.co.uk) - UP608985
 * @version 1.0
 */
public class OverlayService extends Service {
	private WindowManager windowManager;
	private View filter;
	private SharedPreferences settings;

	@Override
	public IBinder onBind(Intent intent) {
		// Not used
		return null;
	}

	@Override public void onCreate() {
		super.onCreate();

		// Restore settings
		settings = getSharedPreferences(Settings.SETTINGS_NAME, 0);

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		int alpha = settings.getInt("overlayOpacity", 80); // Create alpha variable
		filter = new FilterView(this); // Create a new view
		filter.setBackgroundColor(Color.parseColor(settings.getString("overlayColour", "#ffff00"))); // Set the background colour
		filter.getBackground().setAlpha(alpha); // Set the background's alpha

		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT, // Caused right hand side not to be in overlay when set to WRAP_CONTENT
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.START;
		params.x = 0;
		params.y = 100;

		windowManager.addView(filter, params);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (filter != null) windowManager.removeView(filter); // If the filter exists, remove it
	}

}
