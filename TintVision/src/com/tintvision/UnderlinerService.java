package com.tintvision;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

public class UnderlinerService extends Service implements OnTouchListener {
	private WindowManager windowManager;
	private WindowManager.LayoutParams params;
	private View filter;
	float dX;
	float dY;
	int lastAction;

	@Override
	public IBinder onBind(Intent intent) {
		// Not used
		return null;
	}

	@Override public void onCreate() {
		super.onCreate();

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		filter = new View(this); // Create a new view
		filter.setBackgroundColor(Color.BLACK); // Set the background colour to black
		filter.setOnTouchListener(this); // Set up the on touch listener

		params = new WindowManager.LayoutParams(
				400,
				10,
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

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Toast.makeText(getBaseContext(),"onTouchEvent", Toast.LENGTH_SHORT).show();
		// THIS IS WORKING!
		params.x += 10;
		params.y += 10;
		windowManager.updateViewLayout(filter, params);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.d("OverlayButton onTouch", "touched the button");
			//stopSelf();
		}
		return true;
	}

}
