package edu.gatech.cs2340.shelterme;

//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.HashMap;
import java.util.Map;

import edu.gatech.cs2340.shelterme.controllers.DBUtil;
import edu.gatech.cs2340.shelterme.model.Bed;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * A  class that tests the setId() method in Bed.java
 *
 * @author Sandra Alsayar
 */

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({FirebaseDatabase.class, Model.class, DBUtil.class, FirebaseApp.class})
public class BedIDTest {
    private static Bed bed;
    private static Shelter testShelter;
    private static Map<String, Shelter> testShelterList;
    private static Model testModel;
    private static DBUtil testDBUtil;
    private static FirebaseDatabase testFirebase;
    private static DatabaseReference testReference;

    @BeforeClass
    public static void oneTimeSetUp() {
        testModel = Mockito.mock(Model.class);
        testDBUtil = Mockito.mock(DBUtil.class);
        testReference = Mockito.mock(DatabaseReference.class);
        testFirebase = Mockito.mock(FirebaseDatabase.class);
        bed = new Bed();
        testShelter = new Shelter();
        testShelterList = new HashMap<>();
        testShelterList.put(testShelter.getShelterName(), testShelter);
    }

    @Before
    public void setUp() {
        when(testFirebase.getReference()).thenReturn(testReference);
        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(testFirebase);
        PowerMockito.mockStatic(Model.class);
        when(Model.getInstance()).thenReturn(testModel);
        PowerMockito.mockStatic(DBUtil.class);
        when(DBUtil.getInstance()).thenReturn(testDBUtil);
        when(Model.getShelterListPointer()).thenReturn((HashMap<String, Shelter>) testShelterList);
    }

    @Test
    public void testNull() {
        bed.setId(null);
        assertNull(bed.getId());
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