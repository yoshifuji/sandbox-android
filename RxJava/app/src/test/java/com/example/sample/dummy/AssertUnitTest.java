package com.example.sample.dummy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AssertUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    /*
    HamcrestのMatchersに定義されているメソッドの使い方メモ
    http://qiita.com/opengl-8080/items/e57dab6e1fa5940850a3
     */
    @Test
    public void insertElem() throws Exception {
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, "AA", "BB", "CC");
        list.add("111");
        list.add("222");

        assertThat(list, hasItems("AA", "BB", "CC", "111", "222"));
        //assertThat(list, hasItems("AA", "BB", "CC", "111", "2223"));
    }

    @Test
    public void checkEqual() {
        assertThat("Foo", is("Foo"));
        assertThat("Foo", not("Bar"));
    }

    @Test
    public void checkNull() {
        assertThat(null, is(nullValue()));
        assertThat("not null", is(notNullValue()));
    }

    @Test
    public void checkType() {
        Hoge hoge = new Hoge();
        Fuga fuga = new Fuga();
        assertThat(hoge, is(sameInstance(hoge)));
        assertThat(hoge, is(instanceOf(Hoge.class)));
        assertThat(fuga, is(instanceOf(Hoge.class)));
        assertThat(hoge, not(instanceOf(String.class)));
    }
    private static class Hoge {}
    private static class Fuga extends Hoge {}

}