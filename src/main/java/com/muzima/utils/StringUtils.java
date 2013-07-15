package com.muzima.utils;

import java.util.Arrays;
import java.util.List;

import static android.text.TextUtils.split;

public class StringUtils {

    public static String getCommaSeparatedStringFromList(List<String> values){
        if(values == null){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i<values.size(); i++){
            stringBuilder.append(values.get(i));
            if(i != values.size() - 1){
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

    public static List<String> getListFromCommaSeparatedString(String value){
        String[] values = split(value, ",");
        return Arrays.asList(values);
    }

    public static boolean isEmpty(String description) {
        return (description == null || description.isEmpty());
    }
}