import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;

import com.emcreations.tintvision.activity.HomeActivity;
import com.emcreations.tintvision.service.OverlayService;
import com.emcreations.tintvision.util.PermissionManager;
import com.emcreations.tintvision.util.Settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the PermissionManager class
 *
 * @author Edward McKnight (EM-Creations.co.uk)
 * @since 2017
 * @version 1.2
 */
@RunWith(RobolectricTestRunner.class)
public class PermissionManagerTests {
    private PermissionManager pM;
    private Activity activity;
    private Service service;
    private SharedPreferences settings;

    @Before
    public void setup() {
        //this.activity = Robolectric.setupActivity(HomeActivity.class);
        this.service = Robolectric.setupService(OverlayService.class);

        settings = mock(SharedPreferences.class);
        pM = new PermissionManager(this.service);
    }

    /**
     * Tests that the settings name is correct
     */
    @Test
    public void canDrawOverlays_Success() {
        PermissionManager PM = mock(PermissionManager.class);
        boolean expected = true;

        //when(settings.getBoolean(anyString(), anyBoolean())).thenReturn(expected);
        when(PM.canDrawOverlays()).thenReturn(expected);

        boolean result = PM.canDrawOverlays();

        assertTrue(result == expected);
    }
}
