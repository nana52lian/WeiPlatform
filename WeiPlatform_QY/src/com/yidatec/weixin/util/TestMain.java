package com.yidatec.weixin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestMain {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String args[]) {
        List<String> list = new ArrayList<String>();
        list.add("2010-01-29 14:25");
        list.add("2010-01-30 08:15");
        list.add("2010-01-30 08:15");
        list.add("2010-01-29 09:00");
        list.add("2010-01-30 16:45");
        list.add("2010-01-29 09:00");
        list.add("2010-01-30 11:50:15");
        list.add("2010-01-30 11:51");
        list.add("2010-01-30 11:50:14");
        Collections.sort(list, new Comparator() {
            public int compare(Object o, Object o1) {
                String str1 = o.toString();
                String str2 = o1.toString();
                return str1.compareTo(str2);
            }
        });
        for (String str : list) {
            System.out.println(str);
        }
    }
	
}
