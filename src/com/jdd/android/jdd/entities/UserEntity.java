package com.jdd.android.jdd.entities;

import java.io.Serializable;

/**
 * 描述：用户
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-09-07
 * @since 1.0
 */
public class UserEntity implements Serializable {
    private long userId;        //id
    private long phoneNo;        //手机号
    private String name;        //名称
    private String gender;      //性别
    private String avatarUrl;       //头像地址
    private int role;   //角色：普通用户、分析师
    private boolean isVerified; //是否加V，认证
    private String nickName;      //昵称
    private short rank;     //军衔
    private long fansCount;      //粉丝数量
    private long intelligenceCount;      //情报数量
    private long experienceCount;      //智文数量
    private long commentCountByOthers;      //被点评数量
    private long commentOthersCount;      //点评他人数量
    private long rewardedCountByOthers;      //被打赏次数
    private String address;      //地址
    private String profession;      //职业
    private String birthday;      //生日
    private String city;
    private String province;
    private String briefIntroduction;      //简介
    private IntelligenceEntity latestIntel;
    private boolean isFollowed = false;
    private double moralCoins;      //道德币
    private double coins;   //今币
    private String email;
    private String signature;   //个性签名
    private long trialExpireTime;   //试用期到期时间
    private long vipExpireTime;     //VIP到期时间
    private long serverTime;        //服务器当前时间
    private long lastLoginTime;     //最后登录时间
    private String realNameVerifyStatus;    //实名认证状态 C>审核成功;Z>未开始认证;B>被退回;N>正在审核

    public String getRealNameVerifyStatus() {
        return realNameVerifyStatus;
    }

    public void setRealNameVerifyStatus(String realNameVerifyStatus) {
        this.realNameVerifyStatus = realNameVerifyStatus;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public long getTrialExpireTime() {
        return trialExpireTime;
    }

    public void setTrialExpireTime(long trialExpireTime) {
        this.trialExpireTime = trialExpireTime;
    }

    public long getVipExpireTime() {
        return vipExpireTime;
    }

    public void setVipExpireTime(long vipExpireTime) {
        this.vipExpireTime = vipExpireTime;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public double getMoralCoins() {
        return moralCoins;
    }

    public void setMoralCoins(double moralCoins) {
        this.moralCoins = moralCoins;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public IntelligenceEntity getLatestIntel() {
        return latestIntel;
    }

    public void setLatestIntel(IntelligenceEntity latestIntel) {
        this.latestIntel = latestIntel;
    }

    public long getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(long phoneNo) {
        this.phoneNo = phoneNo;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public short getRank() {
        return rank;
    }

    public void setRank(short rank) {
        this.rank = rank;
    }

    public long getFansCount() {
        return fansCount;
    }

    public void setFansCount(long fansCount) {
        this.fansCount = fansCount;
    }

    public long getIntelligenceCount() {
        return intelligenceCount;
    }

    public void setIntelligenceCount(long intelligenceCount) {
        this.intelligenceCount = intelligenceCount;
    }

    public long getExperienceCount() {
        return experienceCount;
    }

    public void setExperienceCount(long experienceCount) {
        this.experienceCount = experienceCount;
    }

    public long getCommentCountByOthers() {
        return commentCountByOthers;
    }

    public void setCommentCountByOthers(long commentCountByOthers) {
        this.commentCountByOthers = commentCountByOthers;
    }

    public long getCommentOthersCount() {
        return commentOthersCount;
    }

    public void setCommentOthersCount(long commentOthersCount) {
        this.commentOthersCount = commentOthersCount;
    }

    public long getRewardedCountByOthers() {
        return rewardedCountByOthers;
    }

    public void setRewardedCountByOthers(long rewardedCountByOthers) {
        this.rewardedCountByOthers = rewardedCountByOthers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBriefIntroduction() {
        return briefIntroduction;
    }

    public void setBriefIntroduction(String briefIntroduction) {
        this.briefIntroduction = briefIntroduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
