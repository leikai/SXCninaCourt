package org.sxchinacourt.util.PinYin;

/**
 * Created by baggio on 2017/2/8.
 */
public class Char {
    public static final int TYPE_ASCII = 0;
    public static final int TYPE_CHINESE = 1;
    public static final int TYPE_UNKNOWN = 3;

    private Object value;
    private int type;

    public Char(int type, Object value) {
        this.type = type;
        this.value = value;
    }

    public char toChar() {
        if (type == TYPE_CHINESE) {
            return ((String) value).charAt(0);
        }
        return (Character) value;
    }

    // 比较两个字符,主要比较大小写字符，规则:a<A;A<z;
    private static int compareChar(Character c1, Character c2) {
        int temp = c1.compareTo(c2);
        if (temp == 0) {// 两个字符相等,如a=a
            return 0;
        }
        // 判断字符的类型
        int temp1, temp2;
        if (c1 >= 97 && c1 <= 122) {
            temp1 = 1;
        } else if (c1 >= 65 && c1 <= 90) {
            temp1 = 2;
        } else {
            temp1 = 0;
        }
        if (c2 >= 97 && c2 <= 122) {
            temp2 = 1;
        } else if (c2 >= 65 && c2 <= 90) {
            temp2 = 2;
        } else {
            temp2 = 0;
        }
        if (temp1 != temp2 && temp1 != 0 && temp2 != 0) {// 一个大写，另一个小写
            c1 = Character.toLowerCase(c1);
            c2 = Character.toLowerCase(c2);
            temp = c1.compareTo(c2);
            if (temp == 0) {
                return temp1 == 1 ? -1 : 1;
            }
        }
        return temp;
    }

    public static int compareTo(Char c1, Char c2) {
        if (c1.type == c2.type) {
            if (c1.type == Char.TYPE_ASCII) {// 都是ASCII字符
                Character cc1, cc2;
                cc1 = (Character) c1.value;
                cc2 = (Character) c2.value;
                return compareChar(cc1, cc2);
            } else if (c1.type == Char.TYPE_UNKNOWN) {// 都是未知字符
                Character cc1, cc2;
                cc1 = (Character) c1.value;
                cc2 = (Character) c2.value;
                return cc1 - cc2;
            } else {// 都是汉字,如zhang，zhao
                String pin1, pin2;
                pin1 = (String) c1.value;
                pin2 = (String) c2.value;
                int min = pin1.length();
                if (min > pin2.length()) {
                    min = pin2.length();
                }
                int temp;
                for (int j = 0; j < min; j++) {// 比较zhan和zhao部分
                    temp = compareChar(pin1.charAt(j), pin2.charAt(j));
                    if (temp != 0) {
                        return temp;
                    }
                }
                // 相同长度部分的内容完全一样，这时比较长度,如zhan和zhang
                return pin1.length() - pin2.length();
            }
        } else {
            if (c1.type == TYPE_UNKNOWN) {// 未知字符排最后
                return 1;
            } else if (c2.type == TYPE_UNKNOWN) {
                return -1;
            }
            char cc1, cc2;
            cc1 = Character.toUpperCase(c1.toChar());
            cc2 = Character.toUpperCase(c2.toChar());

            if (cc1 == cc2) {// 首字母一样，Z=zhang
                return c1.type - c2.type;
            }
            return compareChar(c1.toChar(), c2.toChar());
        }
    }
}