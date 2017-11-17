package net.danielpark.library.log;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by namgyu.park on 2017. 11. 9..
 */

public class LoggerUnitTest {


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void printLog() throws Exception {
        assertThat(
                Logger.vPrint("TAG", "message"),
                is("TAG[[NativeMethodAccessorImpl.java>invoke0>#-2]] message")
        );
    }
}
