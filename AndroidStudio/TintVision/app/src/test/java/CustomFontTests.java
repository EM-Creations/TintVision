import android.graphics.Typeface;

import com.emcreations.tintvision.util.CustomFont;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * JUnit tests for the CustomFont static class
 *
 * @author Edward McKnight (EM-Creations.co.uk) - UP608985
 * @since 2017
 * @version 1.0
 */
@RunWith(RobolectricTestRunner.class)
public class CustomFontTests {
    private CustomFont cF;

    @Before
    public void setup() {
        cF = new CustomFont(ShadowApplication.getInstance().getApplicationContext());
    }

    /**
     * Tests that the regular font return isn't null
     */
    @Test
    public void regularFont_NotNullSuccess() {
        Typeface result = cF.getRegularFont();

        assertThat(result, not(equalTo(null)));
    }

    /**
     * Tests that the bold font return isn't null
     */
    @Test
    public void boldFont_NotNullSuccess() {
        Typeface result = cF.getBoldFont();

        assertThat(result, not(equalTo(null)));
    }
}
