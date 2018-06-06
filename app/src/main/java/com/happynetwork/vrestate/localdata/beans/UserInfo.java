package com.happynetwork.vrestate.localdata.beans;

import java.io.Serializable;

/**
 * 用户信息
 * Created by Tom.yuan on 2017/1/3.
 */
public class UserInfo implements Serializable {
    private String token;
    private String userMobile;
    private String smsCode;
    private String userPic;
    private String userNick;
    private String userName;
    private String userSex;
    private String userBrithday;
    private String userStar;
    private String userHeight;
    private String userWeight;
    private String areaId;
    private String userIndustry;
    private String userCompany;
    private String userJob;
    private String userSalary;
    private String userSchool;
    private String userSpecialty;
    private String userEdu;
    private String userHobby;
    private String userIntro;
    private String userSign;
    private String userType;
    private String userMoney;
    private String userCredit;
    private String userExp;
    private String userLong;
    private String userLat;
    private String userId;
    private String userGeohash;
    private String userTime;
    private String userPassword;

    public String getUserPassword() {
        userPassword = userPassword == null?"":userPassword;
        return userPassword.trim();
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserType() {
        userType = userType == null?"":userType;
        return userType.trim();
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserMoney() {
        userMoney = userMoney == null?"0":userMoney;
        return userMoney.trim();
    }

    public void setUserMoney(String userMoney) {
        this.userMoney = userMoney;
    }

    public String getUserCredit() {
        userCredit = userCredit == null?"0":userCredit;
        return userCredit.trim();
    }

    public void setUserCredit(String userCredit) {
        this.userCredit = userCredit;
    }

    public String getUserExp() {
        userExp = userExp == null?"0":userExp;
        return userExp.trim();
    }

    public void setUserExp(String userExp) {
        this.userExp = userExp;
    }

    public String getUserLong() {
        userLong = userLong == null?"":userLong;
        return userLong.trim();
    }

    public void setUserLong(String userLong) {
        this.userLong = userLong;
    }

    public String getUserLat() {
        userLat = userLat == null?"":userLat;
        return userLat;
    }

    public void setUserLat(String userLat) {
        this.userLat = userLat;
    }

    public String getUserId() {
        userId = userId == null?"":userId;
        return userId.trim();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserGeohash() {
        userGeohash = userGeohash == null?"":userGeohash;
        return userGeohash.trim();
    }

    public void setUserGeohash(String userGeohash) {
        this.userGeohash = userGeohash;
    }

    public String getUserTime() {
        userTime = userTime == null?"":userTime;
        return userTime.trim();
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public String getValus() {
        return getToken() + getUserMobile() + getSmsCode() + getUserPic() + getUserNick() + getUserName() + getUserSex() + getUserBrithday() + getUserStar() + getUserHeight()
                + getUserWeight() + getAreaId() + getUserIndustry() + getUserCompany() + getUserJob() + getUserSalary() + getUserSchool() + getUserSpecialty() + getUserEdu()
                + getUserHobby() + getUserIntro() + getUserSign();
    }

    /**
     * @return 签名
     */
    public String getUserSign() {
        userSign = userSign == null ? "" : userSign;
        return userSign.trim();
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

    /**
     * @return 简介
     */
    public String getUserIntro() {
        userIntro = userIntro == null ? "" : userIntro;
        return userIntro.trim();
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    /**
     * @return 昵称
     */
    public String getUserNick() {
        userNick = userNick == null ? "" : userNick;
        return userNick.trim();
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    /**
     * @return 用户名
     */
    public String getUserName() {
        userName = userName == null ? "" : userName;
        return userName.trim();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return 性别 1-男 2-女
     */
    public String getUserSex() {
        userSex = userSex == null ? "" : userSex;
        return userSex.trim();
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    /**
     * @return 生日
     */
    public String getUserBrithday() {
        userBrithday = userBrithday == null ? "" : userBrithday;
        return userBrithday.trim();
    }

    public void setUserBrithday(String userBrithday) {
        this.userBrithday = userBrithday;
    }

    /**
     * @return 星座
     */
    public String getUserStar() {
        userStar = userStar == null ? "" : userStar;
        return userStar.trim();
    }

    public void setUserStar(String userStar) {
        this.userStar = userStar;
    }

    /**
     * @return 身高
     */
    public String getUserHeight() {
        userHeight = userHeight == null ? "" : userHeight;
        return userHeight.trim();
    }

    public void setUserHeight(String userHeight) {
        this.userHeight = userHeight;
    }

    /**
     * @return 体重
     */
    public String getUserWeight() {
        userWeight = userWeight == null ? "" : userWeight;
        return userWeight.trim();
    }

    public void setUserWeight(String userWeight) {
        this.userWeight = userWeight;
    }

    /**
     * @return 地区id
     */
    public String getAreaId() {
        areaId = areaId == null ? "" : areaId;
        return areaId.trim();
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * @return 行业
     */
    public String getUserIndustry() {
        userIndustry = userIndustry == null ? "" : userIndustry;
        return userIndustry.trim();
    }

    public void setUserIndustry(String userIndustry) {
        this.userIndustry = userIndustry;
    }

    /**
     * @return 公司
     */
    public String getUserCompany() {
        userCompany = userCompany == null ? "" : userCompany;
        return userCompany.trim();
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    /**
     * @return 职务
     */
    public String getUserJob() {
        userJob = userJob == null ? "" : userJob;
        return userJob.trim();
    }

    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }

    /**
     * @return 薪资
     */
    public String getUserSalary() {
        userSalary = userSalary == null ? "" : userSalary;
        return userSalary.trim();
    }

    public void setUserSalary(String userSalary) {
        this.userSalary = userSalary;
    }

    /**
     * @return 学校
     */
    public String getUserSchool() {
        userSchool = userSchool == null ? "" : userSchool;
        return userSchool.trim();
    }

    public void setUserSchool(String userSchool) {
        this.userSchool = userSchool;
    }

    /**
     * @return 专业
     */
    public String getUserSpecialty() {
        userSpecialty = userSpecialty == null ? "" : userSpecialty;
        return userSpecialty.trim();
    }

    public void setUserSpecialty(String userSpecialty) {
        this.userSpecialty = userSpecialty;
    }

    /**
     * @return 学历
     */
    public String getUserEdu() {
        userEdu = userEdu == null ? "" : userEdu;
        return userEdu.trim();
    }

    public void setUserEdu(String userEdu) {
        this.userEdu = userEdu;
    }

    /**
     * @return 爱好
     */
    public String getUserHobby() {
        userHobby = userHobby == null ? "" : userHobby;
        return userHobby.trim();
    }

    public void setUserHobby(String userHobby) {
        this.userHobby = userHobby;
    }

    /**
     * @return 图像
     */
    public String getUserPic() {
        userPic = userPic == null ? "" : userPic;
        return userPic.trim();
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    /**
     * @return 短信验证码
     */
    public String getSmsCode() {
        smsCode = smsCode == null ? "" : smsCode;
        return smsCode.trim();
    }

    /**
     * @return token
     */
    public String getToken() {
        token = token == null ? "" : token;
        return token.trim();
    }

    /**
     * @return 手机号
     */
    public String getUserMobile() {
        userMobile = userMobile == null ? "" : userMobile;
        return userMobile.trim();
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }
}
