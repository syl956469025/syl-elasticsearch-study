package syl.study.elasticsearch;

import org.junit.Test;
import syl.study.elasticsearch.model.Points;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mtime on 2016/10/21.
 */
public class TestGeneric {

    private List<Points> list;

    /***
     * 获取List中的泛型
     */
    @Test
    public void testList() throws NoSuchFieldException, SecurityException {
        Type t = TestGeneric.class.getDeclaredField("list").getGenericType();
        if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
            for (Type t1 : ((ParameterizedType) t).getActualTypeArguments()) {
                System.out.println();
                System.out.print(t1 + ",");
            }
            System.out.println();
        }
    }

    /***
     * 获取Map中的泛型
     */
    @Test
    public void testMap() throws NoSuchFieldException, SecurityException {
        Type t = Test.class.getDeclaredField("map").getGenericType();
        if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
            for (Type t1 : ((ParameterizedType) t).getActualTypeArguments()) {
                System.out.print(t1 + ",");
            }
            System.out.println();
        }
    }


    @Test
    public void testType(){
        System.out.println(isjava(ArrayList.class));
        System.out.println(isjava(Points.class));
    }



    public static boolean isjava(Class<?> clazz){
        System.out.println(clazz.getClassLoader());
        return clazz.getClassLoader() == null;
    }
}
