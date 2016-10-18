package syl.study.elasticsearch.elasticmeta;

import java.util.Map;

/**
 * Created by Mtime on 2016/10/17.
 */
public class MappingProperties {

    /**
     * mapping properties
     * 结构为：
     * {
     *   field: {
     *     type:string,
     *     index:analyzed,
     *     store:true
     *   }
     * }
     */
    Map<String,Object> properties;

    public MappingProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
