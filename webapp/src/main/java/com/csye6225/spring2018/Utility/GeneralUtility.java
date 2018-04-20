package com.csye6225.spring2018.Utility;

public class GeneralUtility {

    public static String getPrefix(String username) {
        int index = username.indexOf("@");
        return username.substring(0, index);
    }
}
