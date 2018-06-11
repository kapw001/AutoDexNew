package autodex.com.autodex.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yasar on 21/11/17.
 */

public class SaveSettings implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("createdByUser")
    @Expose
    private Object createdByUser;
    @SerializedName("modifiedByUser")
    @Expose
    private Object modifiedByUser;
    @SerializedName("createDate")
    @Expose
    private String createDate;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("contactNearMeNotify")
    @Expose
    private Boolean contactNearMeNotify;
    @SerializedName("birthdayNotify")
    @Expose
    private Boolean birthdayNotify;
    @SerializedName("newAutoDexUserNotify")
    @Expose
    private Boolean newAutoDexUserNotify;
    @SerializedName("showNotificationAlert")
    @Expose
    private Boolean showNotificationAlert;
    @SerializedName("vibrateOnNotificationAlert")
    @Expose
    private Boolean vibrateOnNotificationAlert;
    @SerializedName("popUpNotify")
    @Expose
    private Boolean popUpNotify;
    @SerializedName("changeNumWithCountryCode")
    @Expose
    private Boolean changeNumWithCountryCode;
    @SerializedName("enableOtpNotification")
    @Expose
    private Boolean enableOtpNotification;
    @SerializedName("manageBlockUnBlockContacts")
    @Expose
    private Boolean manageBlockUnBlockContacts;
    @SerializedName("manageUserInfo")
    @Expose
    private Boolean manageUserInfo;
    @SerializedName("showFirstLastNameWithImage")
    @Expose
    private Boolean showFirstLastNameWithImage;
    @SerializedName("notifyMiles")
    @Expose
    private Integer notifyMiles;
    private final static long serialVersionUID = -1164089333168606571L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(Object createdByUser) {
        this.createdByUser = createdByUser;
    }

    public Object getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(Object modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getContactNearMeNotify() {
        return contactNearMeNotify;
    }

    public void setContactNearMeNotify(Boolean contactNearMeNotify) {
        this.contactNearMeNotify = contactNearMeNotify;
    }

    public Boolean getBirthdayNotify() {
        return birthdayNotify;
    }

    public void setBirthdayNotify(Boolean birthdayNotify) {
        this.birthdayNotify = birthdayNotify;
    }

    public Boolean getNewAutoDexUserNotify() {
        return newAutoDexUserNotify;
    }

    public void setNewAutoDexUserNotify(Boolean newAutoDexUserNotify) {
        this.newAutoDexUserNotify = newAutoDexUserNotify;
    }

    public Boolean getShowNotificationAlert() {
        return showNotificationAlert;
    }

    public void setShowNotificationAlert(Boolean showNotificationAlert) {
        this.showNotificationAlert = showNotificationAlert;
    }

    public Boolean getVibrateOnNotificationAlert() {
        return vibrateOnNotificationAlert;
    }

    public void setVibrateOnNotificationAlert(Boolean vibrateOnNotificationAlert) {
        this.vibrateOnNotificationAlert = vibrateOnNotificationAlert;
    }

    public Boolean getPopUpNotify() {
        return popUpNotify;
    }

    public void setPopUpNotify(Boolean popUpNotify) {
        this.popUpNotify = popUpNotify;
    }

    public Boolean getChangeNumWithCountryCode() {
        return changeNumWithCountryCode;
    }

    public void setChangeNumWithCountryCode(Boolean changeNumWithCountryCode) {
        this.changeNumWithCountryCode = changeNumWithCountryCode;
    }

    public Boolean getEnableOtpNotification() {
        return enableOtpNotification;
    }

    public void setEnableOtpNotification(Boolean enableOtpNotification) {
        this.enableOtpNotification = enableOtpNotification;
    }

    public Boolean getManageBlockUnBlockContacts() {
        return manageBlockUnBlockContacts;
    }

    public void setManageBlockUnBlockContacts(Boolean manageBlockUnBlockContacts) {
        this.manageBlockUnBlockContacts = manageBlockUnBlockContacts;
    }

    public Boolean getManageUserInfo() {
        return manageUserInfo;
    }

    public void setManageUserInfo(Boolean manageUserInfo) {
        this.manageUserInfo = manageUserInfo;
    }

    public Boolean getShowFirstLastNameWithImage() {
        return showFirstLastNameWithImage;
    }

    public void setShowFirstLastNameWithImage(Boolean showFirstLastNameWithImage) {
        this.showFirstLastNameWithImage = showFirstLastNameWithImage;
    }

    public Integer getNotifyMiles() {
        return notifyMiles;
    }

    public void setNotifyMiles(Integer notifyMiles) {
        this.notifyMiles = notifyMiles;
    }

}