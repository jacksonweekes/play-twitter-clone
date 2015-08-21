package model;

import static model.UserService.instance;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jackson on 20/08/15.
 */
public class UserTest {

    @Before
    public void setUp() throws Exception {
        // Load users to test
        try {
            instance.addUser(new User("Bob", "bob@example.com", "password"));
            instance.addUser(new User("Jack", "jack@example.com", "password"));
            instance.addUser(new User("Jill", "jill@example.com", "password"));
            instance.addUser(new User("Jan", "jan@example.com", "password"));
        } catch(Exception e) {
            // Do nothing
        }

        assertEquals(4, UserService.instance.getUserArray().length);
        assertNotNull(instance.getUserByUsername("Bob"));
        assertNotNull(instance.getUserByUsername("Jack"));
        assertNotNull(instance.getUserByUsername("Jill"));
        assertNotNull(instance.getUserByUsername("Jan"));
    }

    @Test
    public void testIsPassword() {
        assertTrue(instance.getUserByUsername("Bob").isPassword("password"));
    }

    @Test
    public void testGetUserByEmailAndPassword() {
        assertNotNull(instance.getUserByEmailAndPassword("bob@example.com", "password"));
        assertNull(instance.getUserByEmailAndPassword("bob@example.com", "wrong_password"));
        assertNull(instance.getUserByEmailAndPassword("bill@example.com", "password"));
    }

}