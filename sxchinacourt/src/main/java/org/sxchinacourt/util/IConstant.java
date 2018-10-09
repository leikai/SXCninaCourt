
package org.sxchinacourt.util;

/**
 * 常量类
 * com.kinggrid.iapprevisiondemo.IConstant
 * @author wmm
 * create at 2016年2月17日 上午9:55:11
 */
public interface IConstant {
	/**
	 * 金格许可码：过期时间 2017-9-26
	 */
	public final String copyRight = "SxD/phFsuhBWZSmMVtSjKZmm/c/3zSMrkV2Bbj5tznSkEVZmTwJv0wwMmH/+p6wLiUHbjadYueX9v51H9GgnjUhmNW1xPkB++KQqSv/VKLDsR8V6RvNmv0xyTLOrQoGzAT81iKFYb1SZ/Zera1cjGwQSq79AcI/N/6DgBIfpnlwiEiP2am/4w4+38lfUELaNFry8HbpbpTqV4sqXN1WpeJ7CHHwcDBnMVj8djMthFaapMFm/i6swvGEQ2JoygFU3CQHU1ScyOebPLnpsDlQDzCjYgmFZo8sqFMkNKOgywo7x6aD2yiupr6ji7hzsE6/QqGcC+eseQV1yrWJ/1FwxLD4Y1YsZxXwh2w5W4lqa1RyVWEbHWAH22+t7LdPt+jENUp0yRGw03l8UY1ryrlWCQGj7ISWDgc3YLh0bz4sFvgWSgCNRP4FpYjl8hG/IVrYXKlEsCbCtDWPSC8ObydILqOW8fXpxdRHfEuWC1PB9ruQ=";
	public final int REVISION_MODE_SIGN = 1; //手写签批
	public final int REVISION_MODE_WORD = 2; //文字签批
	public final int REVISION_MODE_MIX = 3;  //混合签批
	public final int REVISION_MODE_MIX_SIGN = 4;  //混合签批
	public final int REVISION_MODE_MIX_WORD = 5;  //混合签批
	public final int REVISION_MODE_GRID = 6;
	
	int REVISION_SIGN = 1 << 0;
	int REVISION_WORD_SIGN = 1 << 1;
	int REVISION_MIX_SIGN = 1 << 2;
	int REVISION_GRID_SIGN = 1 << 3;
	int REVISION_FULL_SIGN = 1 << 4;
	int REVISION_IMG_SIGN = 1 << 5;
	
	public static class Helper{
		static int modes;
		
		public static void setSignModeEnabled(int... mode){
			for(int i = 0; i < mode.length; i++){
				modes |= mode[i];
			}
		}
		
		public static boolean enabled(final int mask) {
            return (modes & mask) == mask;
        }
	}
}

