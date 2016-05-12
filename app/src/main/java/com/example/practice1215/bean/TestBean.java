package com.example.practice1215.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guanjun on 2016/5/11.
 */
public class TestBean implements Serializable {
    int one;
    int one1;
    int one2;
    List<TestN> testNs;

    public class TestN implements Serializable {
        int one;
        int one1;
        int one2;

        public void setOne(int one) {
            this.one = one;
        }

        public void setOne1(int one1) {
            this.one1 = one1;
        }

        public void setOne2(int one2) {
            this.one2 = one2;
        }

        public int getOne2() {
            return one2;
        }

        public int getOne1() {
            return one1;
        }

        public int getOne() {
            return one;
        }
    }
}
