package org.sxchinacourt.util.PinYin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtil {
    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param inputString
     * @return
     */
    public static String getPinYin(String inputString) {

        char[] input = inputString.trim().toCharArray();
        String output = "";
        try {
            for (int i = 0; i < input.length; i++) {
                if (Character.toString(input[i]).
                        matches("[\\u4E00-\\u9FA5]+")) {
                    output += getPinYin(input[i], i == 0);
                } else {
                    output += Character.toString(
                            input[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;

//        HanyuPinyinOutputFormat format = new
//                HanyuPinyinOutputFormat();
//        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        format.setVCharType(HanyuPinyinVCharType.WITH_V);
//
//        char[] input = inputString.trim().toCharArray();
//        String output = "";
//
//        try {
//            for (int i = 0; i < input.length; i++) {
//                if (Character.toString(input[i]).
//                        matches("[\\u4E00-\\u9FA5]+")) {
//                    String[] temp = PinyinHelper.
//                            toHanyuPinyinStringArray(input[i],
//                                    format);
//                    if (temp.length > 1) {
//                        String unicode = StringUtil.getUnicode(input[i]);
//                        if (unicode.equals("\\u4ec7")) {
//                            output += "qiu";
//                        }else{
//                            output += temp[0];
//                        }
//                    } else {
//                        output += temp[0];
//                    }
//                } else
//                    output += Character.toString(
//                            input[i]);
//            }
//        } catch (BadHanyuPinyinOutputFormatCombination e) {
//            e.printStackTrace();
//        }
//        return output;
    }

    public static String getPinYin(char c, boolean first) throws
            BadHanyuPinyinOutputFormatCombination {
        String output = null;
        HanyuPinyinOutputFormat format = new
                HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        String[] temp = PinyinHelper.
                toHanyuPinyinStringArray(c,
                        format);
        if (first) {
            if (temp.length > 1) {
                String unicode = StringUtil.getUnicode(c);
                if (unicode.equals("\\u4ec7")) {
                    output = "qiu";
                } else if (unicode.equals("\\u66fe")) {
                    output = "zeng";
                } else {
                    output = temp[0];
                }
            } else {
                output = temp[0];
            }
        } else {
            output = temp[0];
        }
        return output;
    }
}
