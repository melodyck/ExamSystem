package com.zzxx.exam.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Config 读取系统的配置文件
 */
public class Config {
    private Properties pro = new Properties();

    public Config(String file) {
        try {
            pro.load(Config.class.getResourceAsStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int getInt(String key) {
        return Integer.parseInt(pro.getProperty(key));
    }

    public String getString(String key) {
        String str = null;
        //            str = new String(pro.getProperty(key).getBytes("iso8859-1"));
        str = new String(pro.getProperty(key));
        return str;
    }

}