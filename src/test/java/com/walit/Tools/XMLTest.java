package com.walit.Tools;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class XMLTest {
    @Test
    public void checkProdName() throws Exception {
        String str = new Parsed().getNameOfProd();
        MatcherAssert.assertThat(str, is("EasyPass"));
    }
}