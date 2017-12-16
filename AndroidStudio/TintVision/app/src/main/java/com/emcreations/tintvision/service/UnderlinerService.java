package com.emcreations.tintvision.service;

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

import com.emcreations.tintvision.activity.UnderlinerSettingsActivity;
import com.emcreations.tintvision.util.PermissionManager;
import com.emcreations.tintvision.util.Settings;

/**
 * Underliner service for TintVision, handles the creation, destruction and movement of the underliner tool
 * 
 * @author Edward McKnight (EM-Creations.co.uk)
 * @see OverlayService
 * @see UnderlinerSettingsActivity
 * @since 2017
 * @version 1.0
 */
public class UnderlinerService extends Service implements OnTouchListener {
	private WindowManager windowManager;
	private WindowManager.LayoutParams underlinerParams, underlinerShadowParams;
	private View underliner, underlinerShadow;
	private float lastTouchX, lastTouchY, dX, dY;
	private int activePointer;
	private SharedPreferences settings;
	private PermissionManager permissionManager;

    /**
     * onBind
     *
     * @param intent Intent
     * @return null as the method isn't used
     */
	@Override
	public IBinder onBind(Intent intent) {
		return null; // Not used
	}

    /**
     * On create method, which is run when the service is first started
     */
	@Override public void onCreate() {
		super.onCreate();
        permissionManager = new PermissionManager(this);

		// Restore settings
		settings = getSharedPreferences(Settings.SETTINGS_NAME, 0);

        if (permissionManager.canDrawOverlays()) { // If we have permission
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            underliner = new View(this); // Create a new view
            underlinerShadow = new View(this); // Create the view's shadow
            underliner.setBackgroundColor(Color.parseColor(settings.getString("underlinerColour", "#000000"))); // Set the background colour
            underlinerShadow.setAlpha(0); // Invisible shadow view
            underlinerShadow.setOnTouchListener(this); // Set up the on touch listener

            underlinerParams = new WindowManager.LayoutParams(
                    settings.getInt("underlinerWidth", 400),
                    settings.getInt("underlinerThickness", 10),
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            // Draw underliner shadow 20px padded around the underliner
            underlinerShadowParams = new WindowManager.LayoutParams(
                    settings.getInt("underlinerWidth", 400) + 40,
                    settings.getInt("underlinerThickness", 10) + 40,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            underlinerParams.gravity = Gravity.TOP | Gravity.START;
            underlinerParams.x = 20;
            underlinerParams.y = 100;
            // Copy the underliner params settings to the underliner shadow
            underlinerShadowParams.gravity = underlinerParams.gravity;
            underlinerShadowParams.x = underlinerParams.x - 20;
            underlinerShadowParams.y = underlinerParams.y - 20;

            windowManager.addView(underliner, underlinerParams); // Add the underliner
            windowManager.addView(underlinerShadow, underlinerShadowParams); // Add the underliner shadow
        }
	}

    /**
     * onDestroy method, which is run when the service is stopped
     */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (underliner != null) windowManager.removeView(underliner); // If the underliner exists, remove it
        if (underlinerShadow != null) windowManager.removeView(underlinerShadow); // If the underliner shadow exists, remove it
	}

    /**
     * Handle user dragging the underliner tool around the screen
     *
     * @param v the view to handle the touch event for
     * @param event the event object for this touch
     * @return boolean
     */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
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
                underlinerParams.x += dX;
                underlinerParams.y += dY;
                underlinerShadowParams.x += dX;
                underlinerShadowParams.y += dY;
                windowManager.updateViewLayout(underliner, underlinerParams);
                windowManager.updateViewLayout(underlinerShadow, underlinerShadowParams);

                // Save position
                this.lastTouchX = x;
                this.lastTouchY = y;
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                v.performClick();
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

		return true;
	}
}
