package syl.study.elasticsearch;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mtime on 2016/10/19.
 */
public class Test {

    public static void main(String[] args) throws NoSuchFieldException {
        Map<String,Object> m = new HashMap<>();
        if (m instanceof Map){
            System.out.println("map");
        }
    }
}
