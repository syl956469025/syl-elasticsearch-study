package syl.study.elasticsearch.model;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class MemberCore extends BaseEntity<String>{
    
    private static final long serialVersionUID = 1L;
    
    private String memberNo;
    
    private Long memberId;
    
    private String mobile;
    
    private String phone;
    
    private String userName;
    
    private Integer gender;
    
    private Integer channelId;
    
    private String channelExtId;
    
    private String email;
    
    private LocalDate birthday;
    
    private Integer registType;
    
    private String registCinemaId;
    
    private Integer status;
    
    private String changeStatusReason;
    
    private Integer isDelete;
    
    private String manageCinemaId;
    
    private Integer education;
    
    private Integer occupation;
    
    private Integer income;
    
    private Integer marryStatus;
    
    private Integer childNum;
    
    private Integer contractable;
    
    private Integer arrivalType;
    
    private Integer offenChannel;
    
    private Integer[] likeFilmType;
    
    private Integer[] likeContactMethods;
    
    private Integer fqCinemaDist;
    
    private Integer fqCinemaTime;
    
    private String idCardNo;
    
    private String idCardHashNo;
    
    private Integer identityType;
    
    private String weibo;
    
    private String qq;
    
    private String douban;
    
    private String address1;
    
    private String address2;
    
    private String address3;
    
    private String address4;
    
    private Integer countryId;
    
    private Integer provinceId;
    
    private Integer cityId;
    
    private String zipCode;
    
    private LocalDateTime createTime;
    
    private Integer operatorId;
    
    private String operatorName;
    
    private Integer level;
    
    private Integer points;
    
    private Integer cardCount;
    
    private String[] cards;
    
    private LocalDateTime updateTime;

    private Long oldMemberId;
    
    private String recruitEmployeeNo;
    
    private String oftenCode;//常驻影城
    
    private int isSyncToOld;//是否同步到老总部
    
    private String tcPassword;//电渠密码
    
    private LocalDateTime loginTime;//电渠会员最后一次登录时间
    
    private String recruitEmployeeName;//招募人名称


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelExtId() {
        return channelExtId;
    }

    public void setChannelExtId(String channelExtId) {
        this.channelExtId = channelExtId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Integer getRegistType() {
        return registType;
    }

    public void setRegistType(Integer registType) {
        this.registType = registType;
    }

    public String getRegistCinemaId() {
        return registCinemaId;
    }

    public void setRegistCinemaId(String registCinemaId) {
        this.registCinemaId = registCinemaId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getChangeStatusReason() {
        return changeStatusReason;
    }

    public void setChangeStatusReason(String changeStatusReason) {
        this.changeStatusReason = changeStatusReason;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getManageCinemaId() {
        return manageCinemaId;
    }

    public void setManageCinemaId(String manageCinemaId) {
        this.manageCinemaId = manageCinemaId;
    }

    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    public Integer getOccupation() {
        return occupation;
    }

    public void setOccupation(Integer occupation) {
        this.occupation = occupation;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public Integer getMarryStatus() {
        return marryStatus;
    }

    public void setMarryStatus(Integer marryStatus) {
        this.marryStatus = marryStatus;
    }

    public Integer getChildNum() {
        return childNum;
    }

    public void setChildNum(Integer childNum) {
        this.childNum = childNum;
    }

    public Integer getContractable() {
        return contractable;
    }

    public void setContractable(Integer contractable) {
        this.contractable = contractable;
    }

    public Integer getArrivalType() {
        return arrivalType;
    }

    public void setArrivalType(Integer arrivalType) {
        this.arrivalType = arrivalType;
    }

    public Integer getOffenChannel() {
        return offenChannel;
    }

    public void setOffenChannel(Integer offenChannel) {
        this.offenChannel = offenChannel;
    }

    public Integer[] getLikeFilmType() {
        return likeFilmType;
    }

    public void setLikeFilmType(Integer[] likeFilmType) {
        this.likeFilmType = likeFilmType;
    }

    public Integer[] getLikeContactMethods() {
        return likeContactMethods;
    }

    public void setLikeContactMethods(Integer[] likeContactMethods) {
        this.likeContactMethods = likeContactMethods;
    }

    public Integer getFqCinemaDist() {
        return fqCinemaDist;
    }

    public void setFqCinemaDist(Integer fqCinemaDist) {
        this.fqCinemaDist = fqCinemaDist;
    }

    public Integer getFqCinemaTime() {
        return fqCinemaTime;
    }

    public void setFqCinemaTime(Integer fqCinemaTime) {
        this.fqCinemaTime = fqCinemaTime;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIdCardHashNo() {
        return idCardHashNo;
    }

    public void setIdCardHashNo(String idCardHashNo) {
        this.idCardHashNo = idCardHashNo;
    }

    public Integer getIdentityType() {
        return identityType;
    }

    public void setIdentityType(Integer identityType) {
        this.identityType = identityType;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getDouban() {
        return douban;
    }

    public void setDouban(String douban) {
        this.douban = douban;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getCardCount() {
        return cardCount;
    }

    public void setCardCount(Integer cardCount) {
        this.cardCount = cardCount;
    }

    public String[] getCards() {
        return cards;
    }

    public void setCards(String[] cards) {
        this.cards = cards;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getOldMemberId() {
        return oldMemberId;
    }

    public void setOldMemberId(Long oldMemberId) {
        this.oldMemberId = oldMemberId;
    }

    public String getRecruitEmployeeNo() {
        return recruitEmployeeNo;
    }

    public void setRecruitEmployeeNo(String recruitEmployeeNo) {
        this.recruitEmployeeNo = recruitEmployeeNo;
    }

    public String getOftenCode() {
        return oftenCode;
    }

    public void setOftenCode(String oftenCode) {
        this.oftenCode = oftenCode;
    }

    public int getIsSyncToOld() {
        return isSyncToOld;
    }

    public void setIsSyncToOld(int isSyncToOld) {
        this.isSyncToOld = isSyncToOld;
    }

    public String getTcPassword() {
        return tcPassword;
    }

    public void setTcPassword(String tcPassword) {
        this.tcPassword = tcPassword;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public String getRecruitEmployeeName() {
        return recruitEmployeeName;
    }

    public void setRecruitEmployeeName(String recruitEmployeeName) {
        this.recruitEmployeeName = recruitEmployeeName;
    }
}
