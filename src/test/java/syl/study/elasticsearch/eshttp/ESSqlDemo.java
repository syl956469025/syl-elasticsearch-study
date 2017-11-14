package syl.study.elasticsearch.eshttp;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * ES sql 示例
 *
 * @author 史彦磊
 * @create 2017-11-09 17:00.
 */
public class ESSqlDemo {

    @Test
    public void testJDBC() throws Exception {
        Properties properties = new Properties();
        properties.put("url", "jdbc:elasticsearch://192.168.55.139:9300/membercore");
        DruidDataSource dds = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
        Connection connection = dds.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT  * from  membercore where cardCount=3");
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString("id") + "," + resultSet.getInt("cardCount") + "," + resultSet.getString("zhangsan"));
        }
        ps.close();
        connection.close();
        dds.close();
    }
}
