package syl.study.elasticsearch.Util;

import org.omg.CORBA.Object;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Mtime on 2016/11/9.
 */
public class CollectionUtil {


    public static boolean isEmpty(Collection coll){
        if (coll == null || coll.isEmpty()){
            return true;
        }
        return false;
    }


    public static boolean notEmpty(Collection coll){
        return !isEmpty(coll);
    }


    public static boolean isEmptyMap(Map<Object,Object> map){
        if (map == null || map.isEmpty()){
            return true;
        }
        return false;
    }


    public static boolean notEmptyMap(Map<Object,Object> map){
        return !isEmptyMap(map);
    }





}
