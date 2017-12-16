package com.emcreations.tintvision.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;

/**
 * PermissionManager
 *
 * Created by Edward McKnight (EM-Creations.co.uk)
 * @since 2017
 * @version 1.2
 */
public class PermissionManager {
    private final Activity activity;
    private final Service service;
    private final static int OVERLAY_REQUEST_CODE = 333;
    private final SharedPreferences settings;
    private final SharedPreferences.Editor editor;

    /**
     * Constructor
     *
     * @param activity Activity
     */
    public PermissionManager(Activity activity) {
        this(activity, null);
    }

    /**
     * Constructor
     *
     * @param service Service
     */
    public PermissionManager(Service service) {
        this(null, service);
    }

    /**
     * Constructor
     */
    private PermissionManager(Activity activity, Service service) {
        this.activity = activity;
        this.service = service;

        // Restore settings
        if (this.activity == null) {
            settings = this.service.getApplicationContext().getSharedPreferences(Settings.SETTINGS_NAME, 0);
        } else {
            settings = this.activity.getApplicationContext().getSharedPreferences(Settings.SETTINGS_NAME, 0);
        }
        editor = settings.edit();
    }

    /**
     * Check whether we have permission to draw overlays
     */
    public void checkDrawOverPermission() {
        if (Build.VERSION.SDK_INT >= 23) { // If API 23 or above, we need to ask for permission
            this.checkDrawOverlayPermissionOverAPI23();
        } else { // Otherwise, we'd already have permission due to tbe AndroidManifest
            editor.putBoolean("canDrawOverlays", true);
            editor.apply();
        }
    }

    /**
     * Return whether we can draw overlays or not
     *
     * @return boolean
     */
    public boolean canDrawOverlays() {
        return settings.getBoolean("canDrawOverlays", false);
    }

    /**
     * Handle activity result
     *
     * @param requestCode int
     * @param resultCode int
     * @param data Intent
     */
    @TargetApi(23)
    public void handleActivityResult(int requestCode, int resultCode,  Intent data) {
        if (requestCode == OVERLAY_REQUEST_CODE) { // Request codes match
            this.setCanDrawOverlay(android.provider.Settings.canDrawOverlays(this.activity.getApplicationContext()));
        }
    }

    /**
     * Check whether we have permission to draw overlays on API >= 23
     */
    @TargetApi(23)
    private void checkDrawOverlayPermissionOverAPI23() {
        if (!android.provider.Settings.canDrawOverlays(this.activity.getApplicationContext())) { // If we don't have permission to draw overlays
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.activity.getApplicationContext().getPackageName()));
            this.activity.startActivityForResult(intent, OVERLAY_REQUEST_CODE); // Request permission
        } else {
            this.setCanDrawOverlay(true);
        }
    }

    /**
     * Set whether or not the overlays can be drawn
     *
     * @param b Boolean
     */
    private void setCanDrawOverlay(boolean b) {
        editor.putBoolean("canDrawOverlays", b);
        editor.apply();
    }
}
