package edu.gatech.cs2340.shelterme;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.User;

import static org.junit.Assert.*;
/**
 * Created by kyraalexandraleroux on 4/15/18.
 */
public class KyraGenerateKeyTest {
    private User user = new User("j", "l", "jl", 0, Account.Sex.MALE, 0,
         false,
         null, null);
    @Test
    public void AllBasicsTest() {
        assertEquals("FM0F", user.generateKey());
    }
    @Test
    public void Female200Test() {
        User user = new User("j", "l", "jl", 0, Account.Sex.FEMALE, 200,
                true, null, null);
        assertEquals("TF200F", user.generateKey());
    }
    @Test
    public void NoBedAdded() {
        User user = new User("j", "l", "jl", 0, Account.Sex.FEMALE, 26,
                false, null, null);
        assertEquals("FF26F", user.generateKey());
    }
    @Test
    public void HighAgeTest() {
        User user = new User("j", "l", "jl", 0, Account.Sex.MALE, 120,
                false, null, null);
        assertEquals("FM120F", user.generateKey());
    }
    @Test
    public void SomethingsMissingTest() {
        User user = new User("j", "l", "jl", 0, null, 120,
                false, null, null);
        assertEquals("F", user.generateKey());
    }
    @Test
    public void DefaultUserTest() {
        User user = new User();
        assertEquals("FM25F", user.generateKey());
    }
}
