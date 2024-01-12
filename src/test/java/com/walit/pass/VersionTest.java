package com.walit.pass;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class VersionTest {
    @Test
    public void checkVersion() throws Exception {
        MatcherAssert.assertThat(new CLI().getVersionInfo(), is(new Parsed().getVersion()));
    }
}