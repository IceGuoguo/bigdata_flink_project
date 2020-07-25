package com.bing.evaluate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author guobing
 * @version 1.0
 * @date 2019/10/8 下午9:59
 * @description
 */

public class TestTry {
    public static void main(String[] args) {
        System.out.println(test1());
//        Map<String,String> map1 = test3();
//        System.out.println(map1.get("KEY"));

    }

    private static int test1() {
        int i = 1;
        try {
            System.out.println("try...");
            i=10;
            return i;
        } catch (Exception e) {
            System.out.println("catch...");
        } finally {
            System.out.println("i===="+i);
            i=2;
            System.out.println("fi、】nally...");
            System.out.println("i=" + i);
        }

        return i;

    }


    private static Map<String, String> test3() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("KEY", "INIT");
        try {
            System.out.println("try...");
            map.put("KEY", "TRY");
            return map;
        } catch (Exception e) {
            System.out.println("catch...");
            map.put("KEY", "CATCH");
        } finally {
            System.out.println("finally...");
            map.put("KEY", "FINALLY");
            map = null;
        }
        return map;
    }
}
