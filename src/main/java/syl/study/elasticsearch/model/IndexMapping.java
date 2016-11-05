package syl.study.elasticsearch.model;

import syl.study.elasticsearch.annotation.ESAliases;
import syl.study.elasticsearch.annotation.ESIndex;

import java.util.List;

/**
 * Created by Mtime on 2016/10/17.
 */
@ESIndex("indexmapping")
@ESAliases("mapping")

public class IndexMapping extends BaseEntity<String> {

    String indexName;

    String indexType;


    List<FieldInfo> fieldInfos;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }


    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(List<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }
}
