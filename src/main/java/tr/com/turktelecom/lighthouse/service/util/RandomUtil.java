package tr.com.turktelecom.lighthouse.service.util;

import org.apache.commons.lang.RandomStringUtils;

import java.security.SecureRandom;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtil {

    private static final int DEF_COUNT = 20;
    private static final String ALPHABET_NUMBER_CHARACTERS = "abcdefghijklmnprstuvyzABCDEFGHIJKLMNPRSTUVYZ123456789-@!+%&()[]";
    private static final String ALPHABET_NUMBER = "abcdefghijklmnprstuvyzABCDEFGHIJKLMNPRSTUVYZ123456789";
    private RandomUtil() {
    }

    /**
     * Generates a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        //return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
        SecureRandom secureRandom = new SecureRandom();
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i<DEF_COUNT; i++) {
            int index = Math.abs(secureRandom.nextInt(ALPHABET_NUMBER_CHARACTERS.length()-1));
            buffer.append(ALPHABET_NUMBER_CHARACTERS.charAt(index));
        }
        return buffer.toString();
    }

    /**
     * Generates an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        //return RandomStringUtils.randomNumeric(DEF_COUNT);
        SecureRandom secureRandom = new SecureRandom();
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i<DEF_COUNT; i++) {
            int index = Math.abs(secureRandom.nextInt(ALPHABET_NUMBER.length()-1));
            buffer.append(ALPHABET_NUMBER.charAt(index));
        }
        return buffer.toString();
    }

    /**
    * Generates a reset key.
    *
    * @return the generated reset key
    */
    public static String generateResetKey() {
        //return RandomStringUtils.randomNumeric(DEF_COUNT);
        SecureRandom secureRandom = new SecureRandom();
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i<DEF_COUNT; i++) {
            int index = Math.abs(secureRandom.nextInt(ALPHABET_NUMBER.length()-1));
            buffer.append(ALPHABET_NUMBER.charAt(index));
        }
        return buffer.toString();
    }
}
