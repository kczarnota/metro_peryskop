package com.example.konrad.metroperyskop.utils;

public class Utils
{
    public static String getTag(Class<?> classTag)
    {
        return Constants.APP_TAG + classTag.getSimpleName();
    }
}