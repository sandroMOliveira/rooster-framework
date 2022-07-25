package com.rooster.framework.core.util;

import java.util.Collection;

public class CheckUtils {

    public static boolean isNullOrEmpty(Collection<?> check) {
        return check == null || check.size() == 0;
    }

}
