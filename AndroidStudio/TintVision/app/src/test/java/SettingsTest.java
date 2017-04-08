import com.emcreations.tintvision.util.Settings;

import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * JUnit tests for the settings static class
 *
 * @author Edward McKnight (EM-Creations.co.uk) - UP608985
 * @since 2017
 * @version 1.0
 */
public class SettingsTest {

    /**
     * Tests that the settings name is correct
     */
    @Test
    public void settings_CorrectSettingsName() {
        assertThat(Settings.SETTINGS_NAME, is("TintVisionSettings"));
    }
}
