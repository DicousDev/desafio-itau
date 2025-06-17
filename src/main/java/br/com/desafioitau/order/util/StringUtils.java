package br.com.desafioitau.order.util;

import java.util.Objects;

public class StringUtils {

    public static String removeAllWhiteSpaces(String s) {

        if(Objects.isNull(s)) {
            return null;
        }

        return s.replaceAll("\\s+", "");
    }

    public static Boolean isNumericOnly(String s) {
        if(Objects.isNull(s)) {
            return false;
        }

        return s.matches("\\d+");
    }

    public static Boolean isBlank(String s) {
        return s != null && s.isBlank();
    }
}
