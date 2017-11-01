package net.danielpark.library.util;

import net.danielpark.library.util.StringUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by namgyu.park on 2017. 10. 23..
 */

public class StringUtilUnitTest {

    @Test
    public void isNullorEmpty() throws Exception {

        assertEquals(true, StringUtil.isNullorEmpty(null));
        assertEquals(true, StringUtil.isNullorEmpty(""));
        assertEquals(true, StringUtil.isNullorEmpty("     "));

        assertEquals(false, StringUtil.isNullorEmpty("11"));
        assertEquals(false, StringUtil.isNullorEmpty(" sl slslkw "));
        assertEquals(true, StringUtil.isNullorEmpty("\n"));
    }

}
