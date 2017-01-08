package com.tintvision;

import com.tintvision.util.FilterView;
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
    int alpha = 80; // Create alpha variable
    filter.setBackgroundColor(Color.YELLOW); // Set the background colour to yellow
    filter.getBackground().setAlpha(alpha); // Set the background's alpha
    
    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT, // Caused right hand side not to be in overlay when set to WRAP_CONTENT
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_PHONE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
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
