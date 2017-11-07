package net.danielpark.library;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import net.danielpark.library.util.KeyHash;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by namgyu.park on 2017. 10. 28..
 */
@RunWith(AndroidJUnit4.class)
public class KeyHashInstrumentedTest {

    @Test
    public void getAppKeyHash() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("NltB4Hh7DWL50t8D9rrMhfqhjuA=\n",
                KeyHash.getAppKeyHash(appContext, KeyHash.HashType.SHA));

        assertEquals("luMIUIXHHTJj33c1VVgzmJ2qk5I8eIuIOI9lJokw/58=\n",
                KeyHash.getAppKeyHash(appContext, KeyHash.HashType.SHA256));
    }
}
