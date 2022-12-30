package dev.mbo.springbootbom;

import org.apache.commons.lang3.StringUtils;

public class SimpleTest {

    public static void main(String[] args) {
        System.out.println(StringUtils.abbreviate("foo!bar123", 7));
    }

}
