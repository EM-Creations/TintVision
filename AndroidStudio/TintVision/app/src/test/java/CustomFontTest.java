import android.test.AndroidTestCase;

import com.emcreations.tintvision.util.CustomFont;

import org.junit.Ignore;
import org.junit.Test;

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
public class CustomFontTest extends AndroidTestCase {
    private CustomFont cF = new CustomFont(getContext());

    /**
     * Tests that the regular font return isn't null
     */
    @Ignore
    @Test
    public void customfont_RegularNotNull() {
        assertThat(cF.getRegularFont(), not(equalTo(null)));
    }

    /**
     * Tests that the bold font return isn't null
     */
    @Ignore
    @Test
    public void customfont_BoldNotNull() {
        assertThat(cF.getBoldFont(), not(equalTo(null)));
    }
}
