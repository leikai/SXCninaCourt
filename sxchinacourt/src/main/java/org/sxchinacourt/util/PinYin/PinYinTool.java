package org.sxchinacourt.util.PinYin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


public class PinYinTool {

	static HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
	static {
		outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	}

	public static String getPinyinStr(char c) {
		String result=null;
		try {
			String[] rets = PinyinHelper.toHanyuPinyinStringArray(c, outputFormat);
			if(null!=rets&&rets.length>0){
				result=rets[0];
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return result;
	}
}
