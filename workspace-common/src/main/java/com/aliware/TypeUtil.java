package com.aliware;

/**
 * 1. 常用基础类型对象转换成字符串后占用的最大空间(byte)
 */
public class TypeUtil {

    public static final int BYTE_MAX_LENGTH = Byte.toString(Byte.MAX_VALUE).length();

    public static final int INT_MAX_LENGTH = Integer.toString(Integer.MAX_VALUE).length();

    public static final int LONG_MAX_LENGTH = Long.toString(Long.MAX_VALUE).length();

    public static final int FLOAT_MAX_LENGTH = Float.toString(Float.MAX_VALUE).length();

    public static final int DOUBLE_MAX_LENGTH = Double.toString(Double.MAX_VALUE).length();

}
