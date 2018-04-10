package edu.gatech.cs2340.shelterme;

import org.junit.Before;
import org.junit.Test;

import edu.gatech.cs2340.shelterme.model.StayReport;
import edu.gatech.cs2340.shelterme.model.User;

import static org.junit.Assert.*;

/**
 * A  class that tests the getCurrentStayReport() method in User.java
 *
 * @author Russell Conklin
 */

public class UserCurrentStayReportTest {
    User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void testEmptyStayReports() {
        assertEquals(null, user.getCurrentStayReport());
    }

    @Test
    public void testLastIsActive() {
        StayReport stayReport1 = new StayReport();
        StayReport stayReport2 = new StayReport();
        user.addStayReport(stayReport1);
        user.addStayReport(stayReport2);
        assertEquals(stayReport2, user.getCurrentStayReport());
    }

    @Test
    public void testLastIsNotActive() {
        StayReport stayReport1 = new StayReport();
        StayReport stayReport2 = new StayReport();
        user.addStayReport(stayReport1);
        user.addStayReport(stayReport2);
        stayReport2.checkOut();
        assertEquals(stayReport1, user.getCurrentStayReport());
    }

    @Test
    public void testNoActiveStays() {
        StayReport stayReport1 = new StayReport();
        StayReport stayReport2 = new StayReport();
        user.addStayReport(stayReport1);
        stayReport1.checkOut();
        user.addStayReport(stayReport2);
        stayReport2.checkOut();
        assertEquals(stayReport2, user.getCurrentStayReport());
    }

}
