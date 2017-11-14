package syl.study.elasticsearch.model;


import syl.study.utils.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 补充活动积分的记录
 *
 * @author 史彦磊
 * @create 2017-09-11 13:12.
 */


public class ActivityPointRecord extends BaseEntity<String> {


    private ActivityPointRecord(){

    }

    public ActivityPointRecord(String memberNo, String orderId, LocalDate exeDate) {
        this.memberNo = memberNo;
        this.orderId = orderId;
        this.exeDate = exeDate;
        String id = memberNo;
        if (!StringUtils.isEmpty(orderId)){
            id = id + "-" +orderId;
        }else if (exeDate != null){
            id = id + "-" +exeDate.toString();
        }else {
            throw new RuntimeException("ActivityPointRecord 类 orderId,和 exeDate(观影日期)不可以同时为空");
        }
        super.id = id;
    }

    private String memberNo;

    private String orderId;

    private LocalDate exeDate;


    private List<TicketPoint> ticketPoints = new ArrayList<>();


    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDate getExeDate() {
        return exeDate;
    }

    public void setExeDate(LocalDate exeDate) {
        this.exeDate = exeDate;
    }

    public List<TicketPoint> getTicketPoints() {
        return ticketPoints;
    }

    public void setTicketPoints(List<TicketPoint> ticketPoints) {
        this.ticketPoints = ticketPoints;
    }
}
