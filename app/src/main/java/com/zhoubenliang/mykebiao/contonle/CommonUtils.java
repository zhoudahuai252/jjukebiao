package com.zhoubenliang.mykebiao.contonle;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

public class CommonUtils {
    public final static String BMOB_APPID = "5a17b0df45a2368bc7ad41b64cda5a27";
    public final static String FACE_API_KEY="bbab5072f27a0d85c080b18249c68dfc";
    public final static String FACE_API_SECRET="yK_4d7RJrcfdVoNUjK9bCNGmHXhoAJdH";
    public final  static  String FACE_SET_NAME="";
    public static ProgressDialog getProcessDialog(Context context, String tips){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(tips);
        dialog.setCancelable(false);
        return dialog;
    }
    /**
     * 图片处理类
     */
    public static byte[] ImageProcess(String path) {
        Bitmap img = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        float scale = Math.min(1,
                Math.min(600f / img.getWidth(), 600f / img.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
                img.getHeight(), matrix, false);

        imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] array = stream.toByteArray();
        return array;
    }
}
