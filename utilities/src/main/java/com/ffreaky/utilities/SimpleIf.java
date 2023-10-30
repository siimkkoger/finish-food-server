package com.ffreaky.utilities;

import java.util.Collection;

public class SimpleIf {

    public static boolean isEmptyOrNull(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmptyOrNull(Collection<?> collection) {
        return !isEmptyOrNull(collection);
    }

    public static int castToIntIfPossible(long longValue) {
        if (longValue <= Integer.MAX_VALUE && longValue >= Integer.MIN_VALUE) {
            return (int) longValue;
        } else {
            throw new ArithmeticException("long value cannot be cast to int without changing its value.");
        }
    }
}
