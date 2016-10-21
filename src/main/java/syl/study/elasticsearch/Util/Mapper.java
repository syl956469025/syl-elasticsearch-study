package syl.study.elasticsearch.Util;

import org.elasticsearch.action.admin.indices.alias.Alias;
import syl.study.elasticsearch.annotation.ESAliases;
import syl.study.elasticsearch.annotation.ESColumn;
import syl.study.elasticsearch.annotation.ESIndex;
import syl.study.elasticsearch.annotation.ESType;
import syl.study.elasticsearch.elasticmeta.ElasticIndex;
import syl.study.elasticsearch.elasticmeta.FieldProperties;
import syl.study.elasticsearch.elasticmeta.MappingProperties;
import syl.study.elasticsearch.enums.Analyzed;
import syl.study.elasticsearch.enums.ESDataType;
import syl.study.elasticsearch.enums.Store;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mtime on 2016/7/12.
 */
public final class Mapper {


    public static EntityInfo getEntityInfo(Class<?> clazz) {
        Map<String, Object> properties = getProperties(clazz,null,null);
        Alias alias = getAlias(clazz);
        ElasticIndex index = getIndex(clazz);
        EntityInfo info = new EntityInfo(clazz,properties,alias,index);
        return info;
    }

    private static Map<String,Object> getProperties(Class<?> clazz,Class<?> subClazz,Map<String, Object> prop){
        Field[] fields = clazz.getDeclaredFields();
        if (prop == null){
            prop = new HashMap<>();
        }
        for (Field f : fields) {
            String javaType = f.getType().getTypeName();
            //判断是不是泛型类
            if(f.getType().equals(Object.class)){
                Type type = subClazz.getGenericSuperclass();
                if (type instanceof ParameterizedType){
                    Type trueType = ((ParameterizedType)type).getActualTypeArguments()[0];
                    javaType = trueType.getTypeName();
                }
            }
            //判断是不是数组
            Class<?> componentType = f.getType().getComponentType();
            if (componentType != null){
                javaType =componentType.getTypeName();
            }
            //判断是不是集合
            Class<?> tt = f.getType();
            if (tt.isAssignableFrom(List.class) && !tt.equals(Object.class)){
                Type t = f.getGenericType();
                Type actrualType = ((ParameterizedType) t).getActualTypeArguments()[0];
                if (isPrimetive( (Class<?>) actrualType)){
                    javaType = actrualType.getTypeName();
                }else {
                    Map<String,Object> obj = getSub((Class<?>)actrualType);
                    prop.put(f.getName(),obj);
                    continue;
                }
            }
            //判断是不是自定义类
            if (!isPrimetive(f.getType())){
                Map<String,Object> obj = getSub(f.getType());
                prop.put(f.getName(),obj);
                continue;
            }
            FieldProperties fprop = new FieldProperties();
            ESColumn column = f.getAnnotation(ESColumn.class);
            if (column != null) {
                if (column.store().equals(Store.NOT_STORE)) {
                    fprop.setStore(false);
                }
                if (f.getType().equals("java.lang.String") && column.analyzed().equals(Analyzed.ANALYZED)) {
                    fprop.setIndex(Analyzed.ANALYZED.getName());
                }
            } else {
                fprop.setStore(true);
                fprop.setIndex(Analyzed.NOT_ANALYZED.getName());
            }
            fprop.setType(ESDataType.getEsType(javaType));
            prop.put(f.getName(),fprop);
        }
        //判断有没有父类
        if (clazz.getSuperclass() != Object.class) {
            return getProperties(clazz.getSuperclass(),clazz,prop);
        }
        return prop;
    }


    private static Map<String,Object> getSub(Class<?> clazz){
        Map<String,Object> obj = new HashMap<>();
        Map<String, Object> subProp = getProperties(clazz,null,null);
        obj.put("type", ESDataType.NESTED.getEsType());
        obj.put("properties",subProp);
        return obj;
    }

    private static Alias getAlias(Class<?> clazz){
        //获取索引别名
        ESAliases aliases = clazz.getAnnotation(ESAliases.class);
        if (aliases != null && !StrKit.isBlank(aliases.value())){
           return new Alias(aliases.value());
        }
        return null;
    }


    /**
     * 判断当前类是不是java基础类
     * @param clazz
     * @return
     */
    private static boolean isPrimetive(Class<?> clazz){
        System.out.println(clazz.getTypeName());
        System.out.println("classLoader" + clazz.getClassLoader());
        return clazz.getClassLoader() == null;
    }

    /**
     * 获取索引的名称 和 类型
     * @param clazz
     * @return
     */
    private static ElasticIndex getIndex(Class<?> clazz){
        ElasticIndex index = new ElasticIndex();
        //获取索引名
        ESIndex indexName = clazz.getAnnotation(ESIndex.class);
        if (indexName == null || StrKit.isBlank(indexName.value())){
            index.setIndexName(clazz.getSimpleName().toLowerCase());
        }else{
            index.setIndexName(indexName.value().toLowerCase());
        }
        //获取索引type
        ESType type = clazz.getAnnotation(ESType.class);
        if (type == null || StrKit.isBlank(type.value())){
            index.setIndexType(index.getIndexName());
        }else {
            index.setIndexType(type.value().toLowerCase());
        }
        return index;
    }

    public static class EntityInfo {

        MappingProperties mappings;

        Alias alias;

        ElasticIndex index;



        public EntityInfo(Class<?> clazz,Map<String, Object> properties,Alias alias,ElasticIndex index){
            this.mappings = new MappingProperties(properties);
            this.alias = alias;
            this.index = index;
        }

        public MappingProperties getMappings() {
            return mappings;
        }

        public void setMappings(MappingProperties mappings) {
            this.mappings = mappings;
        }


        public Alias getAlias() {
            return alias;
        }

        public void setAlias(Alias alias) {
            this.alias = alias;
        }

        public ElasticIndex getIndex() {
            return index;
        }

        public void setIndex(ElasticIndex index) {
            this.index = index;
        }
    }

}
