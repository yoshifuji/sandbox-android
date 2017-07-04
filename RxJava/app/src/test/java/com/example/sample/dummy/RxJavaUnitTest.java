package com.example.sample.dummy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RxJavaUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    /*
    rxjava2 practice
     */
    @Test
    public void fizzbazz() throws Exception {
        Observable.range(1, 100)
            .map(i -> {
                if (i % 15 == 0) {
                    return "FizzBuzz";
                }
                if (i % 3  == 0) {
                    return "Fizz";
                }
                if (i % 5  == 0) {
                    return "Buzz";
                }
                return Integer.toString(i);
            })
            .subscribe(
                    (i) -> {System.out.println(i + ", ");},
                    (e) -> e.printStackTrace(),
                    System.out::println
            );
    }

    /*
    非同期や並列処理にも役立つRxJavaの使い方
    http://qiita.com/disc99/items/1b2e44a1105008ec3ac9
     */
    @Test
    public void rxCollections(){
        List<Integer> res = Observable.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6)) // IterableなオブジェクトからObservableを生成
                .flatMap(Observable::just)        // Stream APIのflatMap相当
                .filter(i -> i % 2 == 0)          // Stream APIのfilter相当
                .map(i -> i * 2)                  // Stream APIのmap相当
                .skip(1)                          // Stream APIのskip相当
                .take(3)                          // Stream APIのlimit相当 ※同様の処理のlimit()も存在する
                .toList()                         // ストリームをListに変換
                .blockingGet();                    // 同期処理で行う
                //.single();                        // 要素を取得

        assertThat(res, hasItems(8,12));
    }

}