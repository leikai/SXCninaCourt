package org.sxchinacourt.util.file;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import Decoder.BASE64Encoder;

/**
 * @author jzb
 * @date 2010-3-15
 */
public class HBase64 {

	private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();

	private static Vector<String> nodes = new Vector<String>();

	/**
	 * Split string into multiple strings
	 * 
	 * @param original
	 *            Original string
	 * @param separator
	 *            Separator string in original string
	 * @return Splitted string array
	 */
	public static String[] split(String original, String separator) {
		nodes.removeAllElements();
		// Parse nodes into vector
		int index = original.indexOf(separator);
		while (index >= 0) {
			nodes.addElement(original.substring(0, index));
			original = original.substring(index + separator.length());
			index = original.indexOf(separator);
		}
		// Get the last node
		nodes.addElement(original);

		// Create splitted string array
		String[] result = new String[nodes.size()];
		if (nodes.size() > 0) {
			for (int loop = 0; loop < nodes.size(); loop++) {
				result[loop] = (String) nodes.elementAt(loop);
			}
		}
		return result;
	}


	public static String replace(String s, String f, String r) {
		if (s == null) {
			return s;
		}
		if (f == null) {
			return s;
		}
		if (r == null) {
			r = "";
		}

		int index01 = s.indexOf(f);
		while (index01 != -1) {
			s = s.substring(0, index01) + r + s.substring(index01 + f.length());
			index01 += r.length();
			index01 = s.indexOf(f, index01);
		}
		return s;
	}

	/**
	 * Method removes HTML tags from given string.
	 * 
	 * @param text
	 *            Input parameter containing HTML tags (eg.
	 *            <strong>cat</strong>)
	 * @return String without HTML tags (eg. cat)
	 */
	public static String removeHtml(String text) {
		try {
			int idx = text.indexOf("<");
			if (idx == -1) {
				return text;
			}

			String plainText = "";
			String htmlText = text;
			int htmlStartIndex = htmlText.indexOf("<", 0);
			if (htmlStartIndex == -1) {
				return text;
			}
			while (htmlStartIndex >= 0) {
				plainText += htmlText.substring(0, htmlStartIndex);
				int htmlEndIndex = htmlText.indexOf(">", htmlStartIndex);
				htmlText = htmlText.substring(htmlEndIndex + 1);
				htmlStartIndex = htmlText.indexOf("<", 0);
			}
			plainText = plainText.trim();
			return plainText;
		} catch (Exception e) {
			System.err.println("Error while removing HTML: " + e.toString());
			return text;
		}
	}

	/** Base64 encode the given data */
	public static String encode(byte[] data) {
		int start = 0;
		int len = data.length;
		StringBuffer buf = new StringBuffer(data.length * 3 / 2);

		int end = len - 3;
		int i = start;
		int n = 0;

		while (i <= end) {
			int d = ((((int) data[i]) & 0x0ff) << 16)
					| ((((int) data[i + 1]) & 0x0ff) << 8)
					| (((int) data[i + 2]) & 0x0ff);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append(legalChars[d & 63]);

			i += 3;

			if (n++ >= 14) {
				n = 0;
				buf.append(" ");
			}
		}

		if (i == start + len - 2) {
			int d = ((((int) data[i]) & 0x0ff) << 16)
					| ((((int) data[i + 1]) & 255) << 8);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append("=");
		} else if (i == start + len - 1) {
			int d = (((int) data[i]) & 0x0ff) << 16;

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append("==");
		}

		return buf.toString();
	}

	private static int decode(char c) {
		if (c >= 'A' && c <= 'Z') {
			return ((int) c) - 65;
		} else if (c >= 'a' && c <= 'z') {
			return ((int) c) - 97 + 26;
		} else if (c >= '0' && c <= '9') {
			return ((int) c) - 48 + 26 + 26;
		} else {
			switch (c) {
			case '+':
				return 62;
			case '/':
				return 63;
			case '=':
				return 0;
			default:
				throw new RuntimeException("unexpected code: " + c);
			}
		}
	}

	/**
	 * Decodes the given Base64 encoded String to a new byte array. The byte
	 * array holding the decoded data is returned.
	 */

	public static byte[] decode(String s) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			decode(s, bos);
		} catch (IOException e) {
			throw new RuntimeException();
		}
		byte[] decodedBytes = bos.toByteArray();
		try {
			bos.close();
			bos = null;
		} catch (IOException ex) {
			System.err.println("Error while decoding BASE64: " + ex.toString());
		}
		return decodedBytes;
	}

	private static void decode(String s, OutputStream os) throws IOException {
		int i = 0;

		int len = s.length();

		while (true) {
			while (i < len && s.charAt(i) <= ' ') {
				i++;
			}

			if (i == len) {
				break;
			}

			int tri = (decode(s.charAt(i)) << 18)
					+ (decode(s.charAt(i + 1)) << 12)
					+ (decode(s.charAt(i + 2)) << 6)
					+ (decode(s.charAt(i + 3)));

			os.write((tri >> 16) & 255);
			if (s.charAt(i + 2) == '=') {
				break;
			}
			os.write((tri >> 8) & 255);
			if (s.charAt(i + 3) == '=') {
				break;
			}
			os.write(tri & 255);

			i += 4;
		}
	}
	/**
	 * bitmap转为base64
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String encodeTheer(String path) {
		//decode to bitmap
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		//Log.d(TAG, "bitmap width: " + bitmap.getWidth() + " height: " + bitmap.getHeight());
		//convert to byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] bytes = baos.toByteArray();

		//base64 encode
		byte[] encode = Base64.encode(bytes,Base64.DEFAULT);
		String encodeString = new String(encode);
		return encodeString;
	}

	public static Bitmap stringtoBitmap(String string){
		//将字符串转换成Bitmap类型
		Bitmap bitmap=null;
		try {
			byte[]bitmapArray;
			bitmapArray=Base64.decode(string, Base64.DEFAULT);
			bitmap=BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	//图片转化成base64字符串
	public static String GetImageStr(String path)
	{//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		String imgFile = path;//待处理的图片
		InputStream in = null;
		byte[] data = null;
		//读取图片字节数组
		try
		{
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);//返回Base64编码过的字节数组字符串
	}

}