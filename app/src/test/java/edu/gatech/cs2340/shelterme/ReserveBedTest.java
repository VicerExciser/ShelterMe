package edu.gatech.cs2340.shelterme;

import edu.gatech.cs2340.shelterme.util.BedManager;
import edu.gatech.cs2340.shelterme.util.DBUtil;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.Bed;
import edu.gatech.cs2340.shelterme.model.User;
import edu.gatech.cs2340.shelterme.model.StayReport;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.util.ReservationManager;
import edu.gatech.cs2340.shelterme.util.ShelterBuilder;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.HashMap;
// .keySet() returns a Set<String>
// .values() returns a Collection<Bed>
import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * A class that tests the reserveBed() method that is handled by ReservationManager.java
 *
 * @author Austin Condict
 */
@SuppressWarnings("ALL")
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class, Model.class, DBUtil.class, ReservationManager.class,
        BedManager.class, ShelterBuilder.class, FirebaseApp.class})
public class ReserveBedTest {

    /* Necessary fields for testing outcomes of reserveBed */
    private static Map<String, Collection<Bed>> reservedResults;
    private static Map<String, Shelter> mockShelterList;
    private static Shelter currShelter;
    private static User currUser;
    private static String userKey;
    private static final String bedKeyExpected = "FTF026_200_F";
    private static final String bedType = "Single";
    private static final int bedsToReserve = 5;
    @Mock
    private static Model mockModel;
    @Mock
    private static DBUtil mockDBUtil;
    @Mock
    private static DatabaseReference mockDatabaseReference;
    @Mock
    private static FirebaseDatabase mockFirebaseDatabase;
    @Mock
    private static FirebaseApp mockFirebaseApp;
    @Mock
    private static ReservationManager mockReservationManager;
    @Mock
    private static BedManager mockBedManager;
    @Mock
    private static ShelterBuilder mockShelterBuilder;

    /* Necessary attributes for instantiating a Shelter */
    private static final String shelterKey = "s_20_Sample Shelter";
    private static final String shelterName = "Sample Shelter";
    private static final String capacityStr = "5";
    private static final String restrictions = "Men";
    private static final double longitude = 1.1;
    private static final double latitude = 1.1;
    private static final String address = "420 Loud St Atlanta, GA 30315";
    private static final String notes = "This is only a test";
    private static final String phone = "(666) 666-4321";

    /* Necessary attributes for instantiating a User */
    private static final String userFullName = "Austin Condict";
    private static final String username = "acondict";
    private static final String userEmail = "acondict11@gmail.com";
    private static final int userPassword = "password".hashCode();
    private static final Account.Sex userSex = Account.Sex.MALE;
    private static final int userAge = 27;
    private static final boolean userIsFamily = false;
    private static final Account.Question secQuest = Account.Question.CITY;
    private static final String secAns = "Atlanta";


    @BeforeClass
    public static void setup() {
        mockModel = Mockito.mock(Model.class);
        mockDBUtil = Mockito.mock(DBUtil.class);
        mockReservationManager = Mockito.mock(ReservationManager.class);
        currUser = new User(userFullName, username, userEmail, userPassword, userSex, userAge,
                userIsFamily, secQuest, secAns);
//        when(Model.findShelterByName(shelterName)).thenReturn(currShelter);
        PowerMockito.mock(Shelter.class);
        currShelter = new Shelter(shelterKey, shelterName, capacityStr, restrictions, longitude,
                latitude, address, notes, phone);
        userKey = currUser.generateKey();
        mockShelterList = new HashMap<>();
        mockShelterList.put(currShelter.getShelterName(), currShelter);
//        mockReservationManager = new ReservationManager(currShelter);
//        bedManager = new BedManager(currShelter.getShelterName());
    }

    @Before
    public void mockSetup() {
        mockDatabaseReference = Mockito.mock(DatabaseReference.class);
        mockFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockFirebaseDatabase.getReference()).thenReturn(mockDatabaseReference);

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockFirebaseDatabase);

        PowerMockito.mockStatic(FirebaseApp.class);
        when(FirebaseApp.getInstance()).thenReturn(mockFirebaseApp);

        PowerMockito.mockStatic(Model.class);
        when(Model.getInstance()).thenReturn(mockModel);

        PowerMockito.mockStatic(DBUtil.class);
        when(DBUtil.getInstance()).thenReturn(mockDBUtil);

        PowerMockito.mock(ShelterBuilder.class);
        try {
            whenNew(ShelterBuilder.class).withArguments(currShelter).thenReturn(mockShelterBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PowerMockito.mock(BedManager.class);

        mockBedManager = currShelter.getShelterBedManager();  //////////

        try {
            whenNew(BedManager.class).withArguments(currShelter).thenReturn(mockBedManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mockReservationManager = new ReservationManager(currShelter);   //////

        try {
            whenNew(ReservationManager.class).withArguments(currShelter)
                    .thenReturn(mockReservationManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        when(mockModel.getCurrUser()).thenReturn((User)currUser);


//        when(currShelter.getShelterBedManager()).thenReturn(mockBedManager);
        when(Model.getInstance().getCurrUser()).thenReturn((User)currUser);
        when(Model.getShelterListPointer()).thenReturn((Map<String, Shelter>) mockShelterList);
        when(Model.getInstance().verifyShelterParcel(currShelter)).thenReturn(currShelter);
        when(Model.findShelterByName(shelterName)).thenReturn(currShelter);
    }

    @Test
    public void verifySetup() {
        assertEquals(mockModel.getCurrUser(), currUser);
        assertNull(currUser.getCurrentStayReport());
        assertFalse(currUser.isOccupyingBed());

        assertEquals(currShelter.getVacancies(), 5);
        assertEquals(currShelter.getSingleCapacity(), 5);
        assertEquals(currShelter.getBeds().values().size(), 2);
        assertEquals(currShelter.getShelterBedManager().findValidBedType(userKey),
                bedKeyExpected);
    }

    @Test
    public void testInvalidArgs() {
        int failCount = 0;

        // Bed type cannot be null or gibberish (must be "Single" or "Family")
        try {
            mockReservationManager.reserveBed(null, 1);
        } catch (IllegalArgumentException iae) {
            failCount++;
        }

        try {
            mockReservationManager.reserveBed("lksjafhkjsbkjsf", 1);
        } catch (IllegalArgumentException iae) {
            failCount++;
        }

        // Number of beds must be a positive integer less than shelter capacity/current vacancies
        try {
            mockReservationManager.reserveBed(bedType, -1);
        } catch (IllegalArgumentException iae) {
            failCount++;
        }

        reservedResults = mockReservationManager.reserveBed(bedType, 0);
        if (reservedResults == null) {
            failCount++;
        }

        try {
            mockReservationManager.reserveBed("Single", 1000);
        } catch (IllegalArgumentException iae) {
            failCount++;
        }

        try {
            mockReservationManager.reserveBed("Family", 1000);
        } catch (IllegalArgumentException iae) {
            failCount++;
        }

        assertEquals("An invalid argument seems to be slipping through the cracks",
                failCount, 6);
    }

    @Test
    public void testInvalidUserData() {
        int failCount = 0;
        User nullUser = null;
        mockModel.setCurrUser(null, nullUser);
        when(Model.getInstance().getCurrUser()).thenReturn(nullUser);
        assertNull(mockModel.getCurrUser());


        // User cannot be null
        try {
            mockReservationManager.reserveBed("Single", 5);
        } catch (IllegalArgumentException iae) {
            failCount++;
            mockModel.setCurrUser(userEmail, currUser);
            when(Model.getInstance().getCurrUser()).thenReturn((User)currUser);
        }

        // User must not have already reserved a bed
        currUser.setIsOccupyingBed(true);
        try {
            mockReservationManager.reserveBed(bedType, bedsToReserve);
        } catch (IllegalArgumentException iae) {
            failCount++;
            currUser.clearOccupiedBed();
        }

        // User cannot have any active stay report
        currUser.addStayReport(new StayReport());
        try {
            mockReservationManager.reserveBed(bedType, bedsToReserve);
        } catch (IllegalArgumentException iae) {
            failCount++;
            currUser.clearStayReportHistory();
        }

        assertEquals("Invalid user data slipped through the checks", failCount, 3);
    }


    @Test
    public void testReserveBedCall() {
        if (mockModel.getCurrUser() == null) {
            mockModel.setCurrUser(userEmail, currUser);
        }
        reservedResults = mockReservationManager.reserveBed(bedType, bedsToReserve);

        // Test that reservation returned valid beds
        assertFalse(reservedResults.isEmpty());
        assertTrue(reservedResults.containsKey(bedKeyExpected));

        // Test that the shelter's vacancies have decremented according to # of beds reserved
        assertEquals(currShelter.getVacancies(), 0);
        assertEquals(currShelter.getSingleCapacity(), 0);

        // Test that user is now indicated to be occupying a bed
        assertTrue(currUser.isOccupyingBed());

        // Test that shelter's list of occupied beds was correctly populated
        Map<String, Bed> occupied = currShelter.getBeds().get("O");
        assertNotNull(occupied);
        assertFalse(occupied.isEmpty());

        // Test that reserved beds are marked as occupied & thus unavailable for another reservation
        Collection<Bed> resultValues = reservedResults.get(bedKeyExpected);
        for (Bed b : occupied.values()) {
            assertEquals(b.getOccupantEmail(), userEmail);
            assertTrue(b.getIsOccupied());
        }
        for (Bed b : resultValues) {
            assertEquals(b.getOccupantEmail(), userEmail);
            assertTrue(b.getIsOccupied());
        }

        // Test that a new stay report is generated for the user & is marked as an active stay
        List<StayReport> userStayReports = currUser.getStayReports();
        assertFalse(userStayReports.isEmpty());
        assertEquals(userStayReports.size(), 1);
        StayReport currStay = currUser.getCurrentStayReport();
        assertTrue(currStay.isActive());
        assertEquals(currStay.getNumReserved(), 5);
        assertEquals(currStay.getShelterName(), shelterName);
        assertEquals(currStay.getAccountEmail(), userEmail);
    }
}
