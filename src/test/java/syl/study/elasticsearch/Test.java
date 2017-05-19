package syl.study.elasticsearch;

import org.junit.Test;
import syl.study.elasticsearch.aggs.TypeRef;
import syl.study.elasticsearch.aggs.User;
import syl.study.elasticsearch.aggs.Utils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jinweile on 2015/12/31.
 */
public class test {

    public static void main(String[] args) throws IOException {
        //ClassPathXmlApplicationContext instance = new ClassPathXmlApplicationContext("etc/spring/spring-root.xml");
        //instance.getBean(Testddd.class).test();
        //System.out.println("ddddd");
//        HashMap<String,String> dd = new HashMap<String, String>();
//        dd.put("ddd","adsfsdfdfa");
//        dd.put("ddd","drhtjyytj");
//        System.out.println(dd.get("ddd"));

//        String log = "\"2016-08-03 17:09\" \"114.255.169.235\" \"影院列表\" \"-\" \"-\" \"iPhone OS,9.300000000000001,290\" \"1013\" \"-\" \"57a1b4d52ac3da5fc6000011\" \"-\" \"-\" \"-\" \"-\" \"1\" \"影院列表\" \"-\" \"-\" \"-\" \"2016-08-03 17:09\" \"-\" \"-\" \"1013\" \"-\" \"-\" \"-\" \"-\" \"2\" \"92828EDC-D483-49D7-890D-64EFE7E2C2E7\" \"-\" \"290\" \"2\" \"2\"";
//        log = log.replace("(^\"|\"$)", "");
//        String[] arr = log.split("\" \"");
//        System.in.read();

        String meta = "{\"modulename\": self.modulename, \"tailed_file\": self.tailed_file, \"readfiletime\": str(timestemp), \"position\": curr_position, \"subsidylog\": 0,\"hostname\":hostname}";
        String logaaa = "2016-09-27 11:14:20 INFO [sdfsfsdfsdfdsfds] (mx.ticket.adpt.modules.utils.ExternalNotifyUtils:230) - 发送外部订单通知到Topic:ticket_adpt_modules_create,消息内容:{\"data\":{\"amount\":2700,\"cinemaId\":668,\"createOrderSeat\":[{\"orderId\":17493,\"salePrice\":3100,\"seatCode\":\"10468\",\"seatId\":\"67909986\",\"seatName\":\"4排16座\",\"servicePrice\":0,\"unitPrice\":2700}],\"enterTime\":\"2016-09-27T11:14:20.733\",\"externalCinemaId\":\"11\",\"externalHallId\":\"115\",\"externalOrderId\":\"2016092711142040321\",\"externalShowTimeCode\":\"8896\",\"hallId\":12,\"mobile\":\"13243210059\",\"orderId\":0,\"platformId\":2,\"quantity\":1,\"refundOrderStatus\":0,\"showtimeId\":385579,\"status\":101,\"suborderId\":17493},\"dateTime\":\"2016-09-27 11:14:20\",\"effective\":true,\"msg\":\"沙盒创建订单成功\",\"type\":\"createOrder\"}";
        String logbbb = meta + "$!@#$%^&$" + logaaa;
        System.out.println(logbbb);
        /*for(int i = 100; i < 200; i++) {
            String log = "dddd"+i+",ssss"+i+",efef"+i+",sdfsd"+i;
            KafkaProducer.getInstance().produce(log, "logstash");
        }*/
        //System.in.read();

        String regS = "^\\{\"modulename\": (?<appname>.+?)\\,.*?\"hostname\":(?<hostname>.+?)\\}\\$\\!@#\\$%\\^&\\$(?<logtime>\\d{4}\\-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\s(?<level>.+?)\\s\\[(?<contextid>.+?)\\]\\s\\((?<class>.+?)\\)\\s\\-\\s(?<logall>.+?)$";
        Pattern p = Pattern.compile(regS, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(logbbb);
        while (m.find()) {
            String appname = m.group("appname");
            String hostname = m.group("hostname");
            String logtime = m.group("logtime");
            String level = m.group("level");
            String contextid = m.group("contextid");
            String classa = m.group("class");
            String logall = m.group("logall");
            System.out.println(logtime);
        }

        //System.in.read();
    }



    @Test
    public void testJson(){
        User u = new User();
        u.setZhangsan("zhangsan");
        u.setBirthday(LocalDate.now().atStartOfDay());
        String s = Utils.toJson(u);
        System.out.println(s);
        User user = Utils.parseObject(s, new TypeRef<User>() {
        });
        System.out.println(user.getZhangsan());
    }

}
