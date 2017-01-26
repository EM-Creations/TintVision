package com.tintvision;

import com.tintvision.util.Settings;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

/**
 * Underliner service for TintVision
 * 
 * @author Edward McKnight (EM-Creations.co.uk) - UP608985
 * @version 1.0
 */
public class UnderlinerService extends Service implements OnTouchListener {
	private WindowManager windowManager;
	private WindowManager.LayoutParams params;
	private View filter;
	private float lastTouchX, lastTouchY;
	private int activePointer;
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

		filter = new View(this); // Create a new view
		filter.setBackgroundColor(Color.parseColor(settings.getString("underlinerColour", "#000000"))); // Set the background colour
		filter.setOnTouchListener(this); // Set up the on touch listener

		params = new WindowManager.LayoutParams(
				settings.getInt("underlinerWidth", 400),
				settings.getInt("underlinerThickness", 10),
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
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

	/**
	 * Handle user dragging the underliner tool around the screen
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//Toast.makeText(getBaseContext(),"onTouchEvent", Toast.LENGTH_SHORT).show();
		//Log.d("mymsg", "TOUCH EVENT: X: " + event.getX() + " Y: " + event.getY());

		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN: {
			final int pointer = event.getActionIndex();
			final float x = event.getX();
			final float y = event.getY();

			// Save start position
			this.lastTouchX = x;
			this.lastTouchY = y;
			// Save the pointer's ID
			activePointer = event.getPointerId(0);
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			// Get the index of the active pointer and its position
			final int pointerIndex = event.findPointerIndex(activePointer);

			final float x = event.getX(pointerIndex);
			final float y = event.getY(pointerIndex);

			// Calculate distance moved
			final float dX = x - this.lastTouchX;
			final float dY = y - this.lastTouchY;

			// Increment positions by the difference (dX and dY)
			params.x += dX;
			params.y += dY;
			windowManager.updateViewLayout(filter, params);

			//invalidate();

			// Save position
			this.lastTouchX = x;
			this.lastTouchY = y;
			break;
		}

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL: {
			this.activePointer = MotionEvent.INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_POINTER_UP: {
			final int pointerIndex = event.getActionIndex();
			final int pointerId = event.getPointerId(pointerIndex);

			if (pointerId == activePointer) {
				// Choose new pointer and adjust
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				lastTouchX = event.getX(newPointerIndex);
				lastTouchY = event.getY(newPointerIndex);
				activePointer = event.getPointerId(newPointerIndex);
			}

			break;
		}
		}

		// THIS IS WORKING!
		/*
		params.x += 10;
		params.y += 10;
		windowManager.updateViewLayout(filter, params);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.d("OverlayButton onTouch", "touched the button");
			//stopSelf();
		}*/
		return true;
	}

}
