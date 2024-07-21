package testing;

import org.apache.commons.lang3.StringUtils;

public class StringShortener {

    public static String shorten(String s, int len) {
        return StringUtils.abbreviate(s, len);
    }

}
