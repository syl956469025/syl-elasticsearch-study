package syl.study.elasticsearch.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Mtime on 2016/10/18.
 */
public enum ESDataType {

    STRING("String","string"),
    INTEGER("Integer","integer"),
    INT("int","integer"),
    SHORT("Short","short"),
    SSHORT("short","short"),
    BOOLEAN("Boolean","boolean"),
    SBOOLEAN("boolean","boolean"),
    DOUBLE("Double","double"),
    SDOUBLE("double","double"),
    FLOAT("Float","float"),
    SFLOAT("float","float"),
    LOCALDATE("LocalDate","date"),
    LOCALDATETIME("LocalDateTime","date"),
    SLONG("long","long"),
    LONG("Long","long"),
    OBJECT("Object","object"),
    NESTED("Nested","nested");

    private String javaType;

    private String esType;

    private final static Map<String, String> caches = new ConcurrentHashMap<>();
    private static Object _lock = new Object();

    private ESDataType(String javaType, String esType) {
        this.javaType = javaType;
        this.esType = esType;
    }

    public String getEsType() {
        return esType;
    }

    public String getJavaType() {
        return javaType;
    }

    public static String getEsType(String javaType){
        if (caches.isEmpty()){
            synchronized (_lock){
                if (caches.isEmpty()){
                    for (ESDataType s : ESDataType.class.getEnumConstants()){
                        caches.put(s.getJavaType(),s.getEsType());
                    }
                }
            }
        }
        return caches.get(javaType);
    }





}
