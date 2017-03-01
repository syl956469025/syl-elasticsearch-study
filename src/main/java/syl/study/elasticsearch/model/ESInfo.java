package syl.study.elasticsearch.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ES中信息类
 *
 * @author 史彦磊
 * @create 2017-01-18 18:10.
 */

public class ESInfo extends BaseEntity<Long> {

    /**
     * 标题名称
     */
    private String title;
    /**
     * 有效开始日期
     */
    private LocalDate startDate;
    /**
     * 有效截止日期
     */
    private LocalDate endDate;
    /**
     * 内容
     */
    private String content;
    /**
     * 状态 1：编辑中，2：已发布，3：已停止
     */
    private int infoStatus;
    /**
     * 信息栏目类型 1：图文类，2：调查类
     */
    private int moduleType;
    /**
     * 信息子分类 1：线上调查 2：结果公布
     */
    private int subType;
    /**
     * 所属栏目id
     */
    private int moduleId;
    /**
     * 是否所有影院可看 0：否，1：是
     */
    private int allScope;
    /**
     * 创建人
     */
    private int createId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 操作人
     */
    private String createName;
    /**
     * 更新人
     */
    private int updateId;
    /**
     * 操作人
     */
    private String updateName;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 是否删除 0：否，1：是
     */
    private int delFlag;
    /**
     * 信息发布范围
     */
    private List<ESInfoScope> scope;
    /**
     * 信息自定义字段
     */
    private List<InfoCustomFieldEs> fields;
    /**
     * 发布影院
     */
    private Set<String> cinemaCodes = new HashSet<>();

    /**
     * 添加发布影院
     * 
     * @param cinemaCode
     */
    public void addCinemaCode(String cinemaCode) {
        if (cinemaCodes == null) {
            cinemaCodes = new HashSet<>();
        }
        cinemaCodes.add(cinemaCode);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getInfoStatus() {
        return infoStatus;
    }

    public void setInfoStatus(int infoStatus) {
        this.infoStatus = infoStatus;
    }

    public int getModuleType() {
        return moduleType;
    }

    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getAllScope() {
        return allScope;
    }

    public void setAllScope(int allScope) {
        this.allScope = allScope;
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

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public int getUpdateId() {
        return updateId;
    }

    public void setUpdateId(int updateId) {
        this.updateId = updateId;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
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

    public List<ESInfoScope> getScope() {
        return scope;
    }

    public void setScope(List<ESInfoScope> scope) {
        this.scope = scope;
    }

    public List<InfoCustomFieldEs> getFields() {
        return fields;
    }

    public void setFields(List<InfoCustomFieldEs> fields) {
        this.fields = fields;
    }

    public Set<String> getCinemaCodes() {
        return cinemaCodes;
    }

    public void setCinemaCodes(Set<String> cinemaCodes) {
        this.cinemaCodes = cinemaCodes;
    }
}
