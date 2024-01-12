package com.walit.pass;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class XMLTest {
    @Test
    public void checkStrings() throws Exception {
        String pad = new Parsed().getPad();
        MatcherAssert.assertThat(pad, is("AES/CBC/PKCS5PADDING"));
    }
}
