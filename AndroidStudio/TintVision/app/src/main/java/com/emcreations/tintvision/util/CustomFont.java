package com.emcreations.tintvision.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * CustomFont static class
 *
 * @author Edward McKnight (EM-Creations.co.uk) - UP608985
 * @version 1.0
 */
public final class CustomFont {
    /**
     * Regular font
     */
    private Typeface regularFont;
    /**
     * Bold font
     */
    private Typeface boldFont;

    /**
     * Constructor
     *
     * @param context Context
     */
    public CustomFont(Context context) {
        // Set the values
        regularFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenDyslexic3-Regular.ttf");
        boldFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenDyslexic3-Bold.ttf");
    }

    /**
     * Get the regular font
     *
     * @return Typeface
     */
    public Typeface getRegularFont() {
        return regularFont;
    }

    /**
     * Get the bold font
     *
     * @return Typeface
     */
    public Typeface getBoldFont() {
        return boldFont;
    }
}
