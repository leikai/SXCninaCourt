package org.sxchinacourt.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Base64;
import android.util.TypedValue;

import org.sxchinacourt.activity.fragment.ContactsFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class BitmapUtil {
    /**
     * 这里是对头像进行动态绘制的
     * **/
    public static Bitmap GetUserImageByNickName(Context context, String nickname){
        Bitmap bitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 抗锯齿
        Canvas canvas = new Canvas(bitmap);
        int a= (int)(Math.random()*6);
        paint.setColor(Color.parseColor(ContactsFragment.ImageBgColor[a]));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(40, 40, 40, paint);
        paint.setColor(Color.WHITE);
        // sp----->px单位变换
        int sp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,14, // 多少sp
                context.getResources().getDisplayMetrics());// 屏幕的密度
        paint.setTextSize(sp);
        // 获得输入字的宽高
        Rect bounds = new Rect();
        String text=getNewText(nickname);

        paint.getTextBounds(text, 0,text.length(), bounds);
        float x, y;
        x = 40 - bounds.width() / 2;
        y = 40 + bounds.height() / 2;
        canvas.drawText(text, x, y, paint);
        return bitmap;
    }
    /**
     * 返回处理过的昵称，保证显示两个或两个一下，包含中文默认取后两个，非中文取前两个
     * */
    private static String getNewText(String content){
        String text;
        if(isChinese(content)){
            if(content.length()>2){
                text=content.substring(content.length()-2,content.length());
            }
            else{
                text=content;
            }
        }
        else{
            if(content.length()>2){
                text=content.substring(0,2);
            }
            else{
                text=content;
            }
        }
        return text;
    }
    // 判断一个字符是否是中文
    public static boolean isChinese(char c) {
        return c >= 0x4E00 &&  c <= 0x9FA5;// 根据字节码判断
    }
    // 判断一个字符串是否含有中文
    public static boolean isChinese(String str) {
        if (str == null) return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) return true;// 有一个中文字符就返回
        }
        return false;
    }


    // 将bitmap转成string类型通过Base64
    public static String BitmapToString(Bitmap bitmap) {
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
        //可以设计添加头
        //String title=""data:image/jpeg;base64,";
//        return title+result;
        return result;

    }
    /**
     *
     *  * base64转为bitmap
     *  * @param base64Data
     *  * @return
     *
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data.replaceFirst("data:image/jpeg;base64,", "").trim(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
