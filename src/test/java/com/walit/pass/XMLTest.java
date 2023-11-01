package com.walit.pass;

import org.hamcrest.MatcherAssert;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

public class XMLTest {
    @Test
    public void checkStrings() throws Exception {
        String pad = new Parsed().getPad();
        MatcherAssert.assertThat(pad, is("AES/CBC/PKCS5PADDING"));
    }
}
