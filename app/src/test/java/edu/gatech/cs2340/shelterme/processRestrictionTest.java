package edu.gatech.cs2340.shelterme;

import org.junit.Before;
import org.junit.Test;

import edu.gatech.cs2340.shelterme.controllers.MainActivity;
import edu.gatech.cs2340.shelterme.model.Bed;
import edu.gatech.cs2340.shelterme.model.User;
import edu.gatech.cs2340.shelterme.model.Shelter;


import static org.junit.Assert.*;

/**
 * A  class that tests the processRestriction() method in Shelter.java
 *
 * @author Evan Woodard
 */


public class processRestrictionTest {
    Shelter rs;
    Bed bed;
    Shelter shelter;

        @Before
        public void setUp() {
            rs = new Shelter();
            bed = new Bed();
            shelter = new Shelter();
        }

        @Test
        public void testAnyone() {
            rs.setRestrictions("anyone");
            assertEquals("anyone", rs.getRestrictions());
        }

        @Test
        public void testVeterans() {
            rs.setRestrictions("veterans");
            assertEquals("veterans", rs.getRestrictions());
    }

        @Test
        public void testUnder5() {
            rs.setRestrictions("under 5");
            assertEquals("under 5", rs.getRestrictions());
    }

        @Test
        public void testChildren() {
            rs.setRestrictions("children");
            assertEquals("children", rs.getRestrictions());
    }




}
