
package volleywebrequest.com.mysqlitelibrary;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetContactListResponse implements Serializable {


    private String wholeName;

    public String getWholeName() {
        return wholeName;
    }

    public void setWholeName(String wholeName) {
        this.wholeName = wholeName;
    }

    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String localContactID;

    public String getLocalContactID() {
        return localContactID;
    }

    public void setLocalContactID(String localContactID) {
        this.localContactID = localContactID;
    }

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
    private Object createDate;
    @SerializedName("modifiedDate")
    @Expose
    private Object modifiedDate;
    @SerializedName("profileTag")
    @Expose
    private String profileTag;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("middleName")
    @Expose
    private String middleName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("contactUserId")
    @Expose
    private Integer contactUserId;
    @SerializedName("favorite")
    @Expose
    private Boolean favorite;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;


    @SerializedName("useEmail")

    @Expose
    private Boolean useEmail;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("address2")
    @Expose
    private String address2;


    //    @Override

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetContactListResponse)) return false;

        GetContactListResponse that = (GetContactListResponse) o;

        if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null)
            return false;
        if (getMiddleName() != null ? !getMiddleName().equals(that.getMiddleName()) : that.getMiddleName() != null)
            return false;
        if (getLastName() != null ? !getLastName().equals(that.getLastName()) : that.getLastName() != null)
            return false;
        return getPhoneNumber() != null ? getPhoneNumber().equals(that.getPhoneNumber()) : that.getPhoneNumber() == null;

    }

    @Override
    public int hashCode() {
        int result = getFirstName() != null ? getFirstName().hashCode() : 0;
        result = 31 * result + (getMiddleName() != null ? getMiddleName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getPhoneNumber() != null ? getPhoneNumber().hashCode() : 0);
        return result;
    }

//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof GetContactListResponse)) return false;
//
//        GetContactListResponse that = (GetContactListResponse) o;
//
//        if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null)
//            return false;
//        if (getMiddleName() != null ? !getMiddleName().equals(that.getMiddleName()) : that.getMiddleName() != null)
//            return false;
//        if (getLastName() != null ? !getLastName().equals(that.getLastName()) : that.getLastName() != null)
//            return false;
//        if (getDob() != null ? !getDob().equals(that.getDob()) : that.getDob() != null)
//            return false;
//        if (getPhoneNumber() != null ? !getPhoneNumber().equals(that.getPhoneNumber()) : that.getPhoneNumber() != null)
//            return false;
//        if (getContactAttributes() != null ? !getContactAttributes().equals(that.getContactAttributes()) : that.getContactAttributes() != null)
//            return false;
//        return getContactCategories() != null ? getContactCategories().equals(that.getContactCategories()) : that.getContactCategories() == null;
//
//    }
//
//    @Override
//    public int hashCode() {
//        int result = getFirstName() != null ? getFirstName().hashCode() : 0;
//        result = 31 * result + (getMiddleName() != null ? getMiddleName().hashCode() : 0);
//        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
//        result = 31 * result + (getDob() != null ? getDob().hashCode() : 0);
//        result = 31 * result + (getPhoneNumber() != null ? getPhoneNumber().hashCode() : 0);
//        result = 31 * result + (getContactAttributes() != null ? getContactAttributes().hashCode() : 0);
//        result = 31 * result + (getContactCategories() != null ? getContactCategories().hashCode() : 0);
//        return result;
//    }

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("profileImage")
    @Expose
    private String profileImage;
    @SerializedName("contactAttributes")
    @Expose
    private List<ContactAttribute> contactAttributes = null;
    @SerializedName("contactCategories")
    @Expose
    private List<ContactCategory> contactCategories = null;
    private final static long serialVersionUID = 23426167424208198L;

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

    public Object getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Object createDate) {
        this.createDate = createDate;
    }

    public Object getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Object modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getProfileTag() {
        return profileTag;
    }

    public void setProfileTag(String profileTag) {
        this.profileTag = profileTag;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {

        return firstName + " " + middleName + " " + lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getContactUserId() {
        return contactUserId;
    }

    public void setContactUserId(Integer contactUserId) {
        this.contactUserId = contactUserId;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getUseEmail() {
        return useEmail;
    }

    public void setUseEmail(Boolean useEmail) {
        this.useEmail = useEmail;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public List<ContactAttribute> getContactAttributes() {
        return contactAttributes;
    }

    public void setContactAttributes(List<ContactAttribute> contactAttributes) {
        this.contactAttributes = contactAttributes;
    }

    public List<ContactCategory> getContactCategories() {
        return contactCategories;
    }

    public void setContactCategories(List<ContactCategory> contactCategories) {
        this.contactCategories = contactCategories;
    }

}
