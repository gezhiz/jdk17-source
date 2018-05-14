package com.worthto;

import org.junit.Test;

/**
 * Created by gezz on 2018/5/10.
 */
public class JavaTest {
    private static int LIST_SIZE = 10;
    private static String[] list = new String[LIST_SIZE];
    @Test
    public void test() throws InterruptedException {
        new Thread(new MyRun(0,"gezz")).start();
        new Thread(new MyRun(0,"worthto")).start();
        Thread.sleep(1000);
        System.out.println(list[0]);
    }

    public static synchronized void seti(int i, String value) {
        if (i >= LIST_SIZE) {
            throw new IllegalArgumentException();
        }
        System.out.println("execute:" + value);
        if (list[i] != null) {
            list[i] = value;
        }
    }
    public static class MyRun implements Runnable {
        private String value;
        private int i;

        public MyRun(int i, String value) {
            this.value = value;
            this.i = i;
        }

        @Override
        public void run() {

            seti(i,value);
        }
    }
}
