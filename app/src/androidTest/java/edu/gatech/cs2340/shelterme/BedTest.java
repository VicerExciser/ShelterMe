package edu.gatech.cs2340.shelterme;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.gatech.cs2340.shelterme.controllers.MainActivity;
import edu.gatech.cs2340.shelterme.model.Bed;

import static org.junit.Assert.*;

/**
 * A  class that tests the setId() method in Bed.java
 *
 * @author Sandra Alsayar
 */

@RunWith(AndroidJUnit4.class)
public class BedTest {
Bed bed;
@Rule
public ActivityTestRule<MainActivity> myActivity =
        new ActivityTestRule<>(MainActivity.class);
    public static final long TIMEOUT = 200L;

    @Before
    public void setUp() throws Exception {
        bed = new Bed();

    }

    @Test
    public void testNull() {
        bed.setId(null);
        assertEquals("bed_1001", bed.getId());
    }

    @Test
    public void testNotBed() {
        bed.setId("1003");
        assertEquals("bed_1003", bed.getId());
    }

    @Test
    public void testWithBed() {
        bed.setId("bed_1003");
        assertEquals("bed_1003", bed.getId());
    }

    @Test
    public void testBedInDatabase() {
        bed.setId("bed_1");
        assertEquals("bed_1", bed.getId());
    }

    @Test
    public void testBedNumNotInDatabase() {
        bed.setId("bed_999");
        assertEquals("bed_999", bed.getId());
    }
}