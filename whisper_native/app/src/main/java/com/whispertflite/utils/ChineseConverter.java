package com.whispertflite.utils;

import com.github.houbb.opencc4j.util.ZhConverterUtil;

public class ChineseConverter {
    public static String toSimplified(String traditional) {
        return ZhConverterUtil.toSimple(traditional);
    }
} 