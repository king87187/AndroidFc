package com.example.dalu.a370project.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by DALU on 2016/4/10.
 */
public class StreamUtil {
    public  static String readFromStream(InputStream in) throws IOException {
       /* ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while ((len =inputStream.read(b))!=-1){
            byteArrayOutputStream.write(b,0,len);
        }
        String result  = byteArrayOutputStream.toString();
        return result;*/
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];

        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }

        String result = out.toString();
        in.close();
        out.close();
        return result;

    }
}
