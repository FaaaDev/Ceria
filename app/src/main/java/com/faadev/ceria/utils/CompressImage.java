package com.faadev.ceria.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CompressImage {
    public static File compress(Context context, Uri uri){
        String sourceFileUri = FileUtils.getPath(uri, context);
        Bitmap original = BitmapFactory.decodeFile(sourceFileUri);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 50, out);

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), original, timeStamp, null);

        Uri baru = Uri.parse(path);

        String compressed = FileUtils.getPath(baru, context);

        File sourceFile = new File(compressed);

        return sourceFile;
    }
}
