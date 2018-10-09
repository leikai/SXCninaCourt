package org.sxchinacourt.util.PinYin;

/**
 * Created by baggio on 2017/2/8.
 */

public class Characters {
    private Char[] chars;
    private String character;
    private int charCount;
    private int length;

    public Characters(String characters) {
        this.character = characters;
        length = character == null ? 0 : character.length();
        chars = new Char[length];
        charCount = 0;
    }

    public char getFistChar() {
        Char c = charAt(0);
        if (c == null) {
            return Character.MIN_VALUE;
        }
        return c.toChar();
    }

    public Char charAt(int index) {
        if (index >= length) {
            return null;
        }
        while (index >= charCount) {
            char c = character.charAt(charCount);
            Object value;
            int type;
            if (c >= 0 && c <= 255) {// 字母
                value = c;
                type = Char.TYPE_ASCII;
            } else {
                String pinyin = PinYinTool.getPinyinStr(c);
                if (pinyin == null || pinyin.equals("")) {// 未知
                    value = c;
                    type = Char.TYPE_UNKNOWN;
                } else {// 汉字
                    value = pinyin;
                    type = Char.TYPE_CHINESE;
                }
            }

            Char nameChar = new Char(type, value);
            chars[charCount] = nameChar;
            charCount++;
        }
        return chars[index];
    }

    public static int compareTo(Characters characters1, Characters characters2) {
        if (characters1.length == 0 && characters2.length == 0) {
            return 0;
        }
        int minCount = characters1.length;
        if (minCount > characters2.length) {
            minCount = characters2.length;
        }
        Char c1, c2;
        int temp;
        for (int i = 0; i < minCount; i++) {
            c1 = characters1.charAt(i);
            c2 = characters2.charAt(i);
            temp = Char.compareTo(c1, c2);
            if (temp != 0) {
                return temp;
            }
        }
        // 相同长度部分的内容完全一样，这时比较长度
        return characters1.length - characters2.length;
    }
}
