package com.mda.school.whereismycar;

import com.mda.school.util.Util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilTest {
    @Test
    public void dateFormatTest() throws Exception {
        Date date = new Date(1513263156134L);
        assertEquals("14-12-2017 03:52:36", Util.dateFormat(date));
    }
}