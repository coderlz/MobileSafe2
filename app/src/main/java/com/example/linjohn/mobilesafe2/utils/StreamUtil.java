package com.example.linjohn.mobilesafe2.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by Administrator on 5/5/2016.
 */
public class StreamUtil {

    public  static String parserStreamUtil(InputStream stream) throws IOException{


        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        StringWriter sw = new StringWriter();

        String str = null;

        while ((str = br.readLine()) != null) {

            sw.write(str);

        }
        return sw.toString();
    }
}
