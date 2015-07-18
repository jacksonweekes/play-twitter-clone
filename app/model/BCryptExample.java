package model;

import org.mindrot.jbcrypt.BCrypt;

/**
 * A little demo code to show how BCrypt is used to hash passwords
 */
public class BCryptExample {

    // Eek, not threadsafe.
    static String last;

    public static String encrypt(String s) {
        last = BCrypt.hashpw(s, BCrypt.gensalt());
        return last;
    }

    public static boolean matchesLastEncrypted(String pw) {
        return BCrypt.checkpw(pw, last);
    }

}
