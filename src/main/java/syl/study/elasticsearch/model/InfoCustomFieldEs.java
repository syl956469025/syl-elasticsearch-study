package syl.study.elasticsearch.model;

import java.time.LocalDateTime;

/**
 * 信息自定义字段实体
 * 
 * @author huachengwu
 *
 */

public class InfoCustomFieldEs {
    /**
     * 字段ID
     */
    private Long id;
    /**
     * 信息ID
     */
    private Long infoId;
    /**
     * 英文名称
     */
    private String englishName;
    /**
     * 中文名称
     */
    private String chineseName;
    /**
     * 数据类型
     */
    private int dataType;
    /**
     * 长度限制
     */
    private int dataLength;
    /**
     * 小数位数
     */
    private int scaleLength;
    /**
     * 是否必填
     */
    private int required;
    /**
     * 枚举值
     */
    private String enumText;
    /**
     * 描述
     */
    private String columnDesc;
    /**
     * 创建人
     */
    private int createId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新人
     */
    private int updateId;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 是否删除
     */
    private int delFlag;
    /**
     * 序号
     */
    private int orderNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public int getScaleLength() {
        return scaleLength;
    }

    public void setScaleLength(int scaleLength) {
        this.scaleLength = scaleLength;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public String getEnumText() {
        return enumText;
    }

    public void setEnumText(String enumText) {
        this.enumText = enumText;
    }

    public String getColumnDesc() {
        return columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc;
    }

    public int getCreateId() {
        return createId;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public int getUpdateId() {
        return updateId;
    }

    public void setUpdateId(int updateId) {
        this.updateId = updateId;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
