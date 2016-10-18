package syl.study.elasticsearch.elasticmeta;

/**
 * Created by Mtime on 2016/10/17.
 */
public class FieldProperties {

    String type;

    String index;

    boolean store;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }
}
