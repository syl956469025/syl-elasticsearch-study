package syl.study.elasticsearch.model;

/**
 * ES中信息范围类
 *
 * @author 史彦磊
 * @create 2017-01-18 18:14.
 */
public class ESInfoScope {

    /**
     * ID
     */
    private long id;
    /**
     * 信息ID
     */
    private long infoId;
    /**
     * 影院编码
     */
    private String cinemaCode;
    /**
     * 影院名称
     */
    private String cinemaName;
    /**
     * 是否删除  0：否，1：是
     */
    private int delFlag;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInfoId() {
        return infoId;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }

    public String getCinemaCode() {
        return cinemaCode;
    }

    public void setCinemaCode(String cinemaCode) {
        this.cinemaCode = cinemaCode;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }
}
