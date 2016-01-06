package com.ph.view;

import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Anand on 1/5/2016.
 */
public class ImageHandler {

    public  static byte[] getImageByteArray(URL url) throws IOException {
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayBuffer buf = new ByteArrayBuffer(500);
        int current = 0;
        while ((current = bis.read()) != -1) {
            buf.append((byte) current);
        }

        Log.i("ImageHandler", buf.toString());
        return buf.toByteArray();
    }




}
