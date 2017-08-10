package tw.zhuran.balsam.util;

import java.util.Random;

public class Randoms {

    private static Random random = new Random(System.currentTimeMillis());

    public static String string(int length) {
        String[] origin = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

        StringBuilder s = new StringBuilder(10);

        for (int i = 0; i < length; i++) {
            s.append(select(origin));
        }
        return s.toString();
    }

    public static int number(int max) {
        return random.nextInt(max);
    }

    public static String select(String[] origin) {
        return origin[number(origin.length)];
    }
}
