package autodex.com.autodex.model.webresponse;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yasar on 6/11/17.
 */
@Entity
public class ProfileDetails implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("password")
    @Expose
    private String password;

    @Override
    public String toString() {
        return "ProfileDetails{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", firstNameTag=" + firstNameTag +
                ", middleName='" + middleName + '\'' +
                ", middleNameTag=" + middleNameTag +
                ", lastName='" + lastName + '\'' +
                ", lastNameTag=" + lastNameTag +
                ", active=" + active +
                ", imieNumber='" + imieNumber + '\'' +
                ", dob='" + dob + '\'' +
                ", dobTag=" + dobTag +
                ", company='" + company + '\'' +
                ", companyTag=" + companyTag +
                ", designation='" + designation + '\'' +
                ", designationTag=" + designationTag +
                ", personalEmail='" + personalEmail + '\'' +
                ", personalEmailTag=" + personalEmailTag +
                ", businessEmail='" + businessEmail + '\'' +
                ", businessEmailTag=" + businessEmailTag +
                ", address1='" + address1 + '\'' +
                ", address1Tag=" + address1Tag +
                ", address2='" + address2 + '\'' +
                ", address2Tag=" + address2Tag +
                ", image='" + image + '\'' +
                ", city='" + city + '\'' +
                ", cityTag=" + cityTag +
                ", state='" + state + '\'' +
                ", stateTag=" + stateTag +
                ", zip='" + zip + '\'' +
                ", zipTag=" + zipTag +
                ", newUser=" + newUser +
                ", homeLatitude='" + homeLatitude + '\'' +
                ", homeLongitude='" + homeLongitude + '\'' +
                ", roamingHomeLatitude='" + roamingHomeLatitude + '\'' +
                ", roamingHomeLongtitude='" + roamingHomeLongtitude + '\'' +
                ", autoDexNum='" + autoDexNum + '\'' +
                ", autoDexNumTag=" + autoDexNumTag +
                '}';
    }

    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("firstNameTag")
    @Expose
    private Integer firstNameTag;
    @SerializedName("middleName")
    @Expose
    private String middleName;
    @SerializedName("middleNameTag")
    @Expose
    private Integer middleNameTag;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("lastNameTag")
    @Expose
    private Integer lastNameTag;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("imieNumber")
    @Expose
    private String imieNumber;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("dobTag")
    @Expose
    private Integer dobTag;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("companyTag")
    @Expose
    private Integer companyTag;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("designationTag")
    @Expose
    private Integer designationTag;
    @SerializedName("personalEmail")
    @Expose
    private String personalEmail;
    @SerializedName("personalEmailTag")
    @Expose
    private Integer personalEmailTag;
    @SerializedName("businessEmail")
    @Expose
    private String businessEmail;
    @SerializedName("businessEmailTag")
    @Expose
    private Integer businessEmailTag;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("address1Tag")
    @Expose
    private Integer address1Tag;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("address2Tag")
    @Expose
    private Integer address2Tag;


    @Expose
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("cityTag")
    @Expose
    private Integer cityTag;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("stateTag")
    @Expose
    private Integer stateTag;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("zipTag")
    @Expose
    private Integer zipTag;
    @SerializedName("newUser")
    @Expose
    private Boolean newUser;
    @SerializedName("homeLatitude")
    @Expose
    private String homeLatitude;
    @SerializedName("homeLongitude")
    @Expose
    private String homeLongitude;
    @SerializedName("roamingHomeLatitude")
    @Expose
    private String roamingHomeLatitude;
    @SerializedName("roamingHomeLongtitude")
    @Expose
    private String roamingHomeLongtitude;
    @SerializedName("autoDexNum")
    @Expose
    private String autoDexNum;
    @SerializedName("autoDexNumTag")
    @Expose
    private Integer autoDexNumTag;
    private final static long serialVersionUID = -8616034474254334885L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getFirstNameTag() {
        return firstNameTag;
    }

    public void setFirstNameTag(Integer firstNameTag) {
        this.firstNameTag = firstNameTag;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Integer getMiddleNameTag() {
        return middleNameTag;
    }

    public void setMiddleNameTag(Integer middleNameTag) {
        this.middleNameTag = middleNameTag;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getLastNameTag() {
        return lastNameTag;
    }

    public void setLastNameTag(Integer lastNameTag) {
        this.lastNameTag = lastNameTag;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getImieNumber() {
        return imieNumber;
    }

    public void setImieNumber(String imieNumber) {
        this.imieNumber = imieNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Integer getDobTag() {
        return dobTag;
    }

    public void setDobTag(Integer dobTag) {
        this.dobTag = dobTag;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getCompanyTag() {
        return companyTag;
    }

    public void setCompanyTag(Integer companyTag) {
        this.companyTag = companyTag;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getDesignationTag() {
        return designationTag;
    }

    public void setDesignationTag(Integer designationTag) {
        this.designationTag = designationTag;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public Integer getPersonalEmailTag() {
        return personalEmailTag;
    }

    public void setPersonalEmailTag(Integer personalEmailTag) {
        this.personalEmailTag = personalEmailTag;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public Integer getBusinessEmailTag() {
        return businessEmailTag;
    }

    public void setBusinessEmailTag(Integer businessEmailTag) {
        this.businessEmailTag = businessEmailTag;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public Integer getAddress1Tag() {
        return address1Tag;
    }

    public void setAddress1Tag(Integer address1Tag) {
        this.address1Tag = address1Tag;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public Integer getAddress2Tag() {
        return address2Tag;
    }

    public void setAddress2Tag(Integer address2Tag) {
        this.address2Tag = address2Tag;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCityTag() {
        return cityTag;
    }

    public void setCityTag(Integer cityTag) {
        this.cityTag = cityTag;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getStateTag() {
        return stateTag;
    }

    public void setStateTag(Integer stateTag) {
        this.stateTag = stateTag;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Integer getZipTag() {
        return zipTag;
    }

    public void setZipTag(Integer zipTag) {
        this.zipTag = zipTag;
    }

    public Boolean getNewUser() {
        return newUser;
    }

    public void setNewUser(Boolean newUser) {
        this.newUser = newUser;
    }

    public String getHomeLatitude() {
        return homeLatitude;
    }

    public void setHomeLatitude(String homeLatitude) {
        this.homeLatitude = homeLatitude;
    }

    public String getHomeLongitude() {
        return homeLongitude;
    }

    public void setHomeLongitude(String homeLongitude) {
        this.homeLongitude = homeLongitude;
    }

    public String getRoamingHomeLatitude() {
        return roamingHomeLatitude;
    }

    public void setRoamingHomeLatitude(String roamingHomeLatitude) {
        this.roamingHomeLatitude = roamingHomeLatitude;
    }

    public String getRoamingHomeLongtitude() {
        return roamingHomeLongtitude;
    }

    public void setRoamingHomeLongtitude(String roamingHomeLongtitude) {
        this.roamingHomeLongtitude = roamingHomeLongtitude;
    }

    public String getAutoDexNum() {
        return autoDexNum;
    }

    public void setAutoDexNum(String autoDexNum) {
        this.autoDexNum = autoDexNum;
    }

    public Integer getAutoDexNumTag() {
        return autoDexNumTag;
    }

    public void setAutoDexNumTag(Integer autoDexNumTag) {
        this.autoDexNumTag = autoDexNumTag;
    }

}