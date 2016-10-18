package syl.study.elasticsearch.model;

/**
 * Created by Mtime on 2016/10/17.
 */
public class FieldInfo {

    String fieldName;

    boolean key;

    boolean analyze;

    boolean store;

    public FieldInfo(){

    }

    public FieldInfo(String fieldName,  boolean key, boolean analyze, boolean store) {
        this.fieldName = fieldName;
        this.key = key;
        this.analyze = analyze;
        this.store = store;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public boolean isAnalyze() {
        return analyze;
    }

    public void setAnalyze(boolean analyze) {
        this.analyze = analyze;
    }

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }

}
