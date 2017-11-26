package com.emcreations.tintvision.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * CustomFont static class
 *
 * @author Edward McKnight (EM-Creations.co.uk) - UP608985
 * @since 2017
 * @version 1.0
 */
public final class CustomFont {
    /**
     * Regular font
     */
    private final Typeface regularFont;
    /**
     * Bold font
     */
    private final Typeface boldFont;

    /**
     * Constructor
     *
     * @param context the context for the font
     */
    public CustomFont(Context context) {
        // Set the values
        regularFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenDyslexic3-Regular.ttf");
        boldFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenDyslexic3-Bold.ttf");
    }

    /**
     * Get the regular font
     *
     * @return the regular font Typeface object
     */
    public Typeface getRegularFont() {
        return regularFont;
    }

    /**
     * Get the bold font
     *
     * @return the bold font Typeface object
     */
    public Typeface getBoldFont() {
        return boldFont;
    }
}
