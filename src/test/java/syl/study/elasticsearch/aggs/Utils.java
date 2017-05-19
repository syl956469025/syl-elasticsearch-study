package syl.study.elasticsearch.aggs;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by jinweile on 2016/1/21.
 */
public class Utils {



    private Utils(){}

    /**
     * json序列化(包括LocalDateTime转换成utc时间格式)
     * @param object
     * @return
     */
    public static String toJson(Object object){
        SerializeConfig serializeConfig = new SerializeConfig();
        return JSON.toJSONString(object, serializeConfig, SerializerFeature.WriteNullListAsEmpty);
    }

    /**
     * json反序列化(包括utc时间转换成LocalDateTime)
     * @param json
     * @param typeRef
     * @return
     */
    public static <T> T parseObject(String json, TypeRef<T> typeRef){
        ParserConfig parserConfig = new ParserConfig();
        return JSON.parseObject(json, typeRef.getType(), parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    }

    /**
     * 转义检索关键字
     * @param s
     * @return
     */
    public static String escapeQueryChars(String s) {
    	if (s == null) {
			return null;
		}
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\\' || c == '+' || c == '-' || c == '!'  || c == '(' || c == ')' || c == ':'
                    || c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
                    || c == '*' || c == '?' || c == '|' || c == '&'  || c == ';' || c == '/' || c == ' '
                    || Character.isWhitespace(c)) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 字符转义
     * @param s
     * @return
     */
    public static String[] escapeQueryChars(String ...s){
    	if (s == null) {
			return null;
		}
    	String[] values = new String[s.length];
    	for (int i = 0; i < s.length; i++) {
    		String str = escapeQueryChars(s[i]);
    		values[i]=str;
		}
    	return values;
    }

}
