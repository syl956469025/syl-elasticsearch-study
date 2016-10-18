package syl.study.elasticsearch.Util;

import org.elasticsearch.action.admin.indices.alias.Alias;
import syl.study.elasticsearch.annotation.ESAliases;
import syl.study.elasticsearch.annotation.ESColumn;
import syl.study.elasticsearch.annotation.ESIndex;
import syl.study.elasticsearch.annotation.ESType;
import syl.study.elasticsearch.elasticmeta.ElasticIndex;
import syl.study.elasticsearch.enums.Analyzed;
import syl.study.elasticsearch.enums.ESDataType;
import syl.study.elasticsearch.enums.Store;
import syl.study.elasticsearch.elasticmeta.FieldProperties;
import syl.study.elasticsearch.elasticmeta.MappingProperties;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mtime on 2016/7/12.
 */
public final class Mapper {


    public static EntityInfo getEntityInfo(Class<?> clazz) {
        Map<String, Object> properties = getProperties(clazz);
        Alias alias = getAlias(clazz);
        ElasticIndex index = getIndex(clazz);
        EntityInfo info = new EntityInfo(clazz,properties,alias,index);
        return info;
    }

    private static Map<String,Object> getProperties(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        Map<String,Object> prop = new HashMap<>();
        for (Field f : fields) {
            FieldProperties fprop = new FieldProperties();
            String javaType = f.getType().getSimpleName();
            fprop.setType(ESDataType.getEsType(javaType));
            ESColumn column = f.getAnnotation(ESColumn.class);
            if (column != null) {
                if (column.subClass()){
                    Map<String,Object> obj = new HashMap<>();
                    Map<String, Object> subProp = getProperties(column.clazz());
                    obj.put("type", ESDataType.NESTED.getEsType());
                    obj.put("properties",subProp);
                    prop.put(f.getName(),obj);
                    continue;
                }
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
            prop.put(f.getName(),fprop);
        }
        return prop;
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
     * 获取索引的名称 和 类型
     * @param clazz
     * @return
     */
    private static ElasticIndex getIndex(Class<?> clazz){
        ElasticIndex index = new ElasticIndex();
        //获取索引名
        ESIndex indexName = clazz.getAnnotation(ESIndex.class);
        if (indexName == null || StrKit.isBlank(indexName.value())){
            index.setIndexName(clazz.getSimpleName());
        }else{
            index.setIndexName(indexName.value());
        }
        //获取索引type
        ESType type = clazz.getAnnotation(ESType.class);
        if (type == null || StrKit.isBlank(type.value())){
            index.setIndexType(index.getIndexName());
        }else {
            index.setIndexType(type.value());
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
