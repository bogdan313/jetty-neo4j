package helpers;

import java.util.Random;

/**
 * Helper class for generate random string with defined length or some number (integer or double)
 */
public class GenerateHelper {
    public static String generateString(int length) {
        if (length < 1) return "";
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        int index;
        for (int i = 0; i < length; i++) {
            index = random.nextInt(chars.length() - 1);
            result.append(chars.charAt(index));
        }
        return result.toString();
    }

    public static String generateString() {
        return GenerateHelper.generateString(10);
    }

    public double generateDouble() {
        return new Random().nextDouble();
    }

    public int generateInt(int max) {
        return new Random().nextInt(max);
    }

    public int generateInt() {
        return new Random().nextInt();
    }
}
