package com.example.tintvision;

import com.example.tintvision.util.FilterView;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class OverlayService extends Service {
	private WindowManager windowManager;
	private View filter;

	@Override
	public IBinder onBind(Intent intent) {
		// Not used
		return null;
	}
	
	@Override public void onCreate() {
    super.onCreate();

    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

    filter = new FilterView(this); // Create a new view
    float alpha = (float) 0.8; // Create alpha variable
    filter.setAlpha(alpha); // Set alpha (this doesn't seem to do anything)
    filter.setBackgroundColor(Color.YELLOW); // Set the background colour to yellow
    filter.getBackground().setAlpha(80); // Set the background's alpha (this is the call that works!)
    
    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_PHONE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        PixelFormat.TRANSLUCENT);

    params.gravity = Gravity.TOP | Gravity.LEFT;
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
