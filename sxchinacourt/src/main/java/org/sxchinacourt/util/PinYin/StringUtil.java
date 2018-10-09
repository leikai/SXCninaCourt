package org.sxchinacourt.util.PinYin;

/**
 * Created by baggio on 2017/2/18.
 */

public class StringUtil {
    public static String getUnicode(char c) {
        String hexB = Integer.toHexString(c);
        if (hexB.length() <= 2) {
            hexB = "00" + hexB;
        }
        String unicode = "\\u" + hexB;
        return unicode;
    }
}
