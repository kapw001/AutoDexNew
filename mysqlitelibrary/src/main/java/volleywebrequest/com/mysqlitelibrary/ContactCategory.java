
package volleywebrequest.com.mysqlitelibrary;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactCategory implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("createdByUser")
    @Expose
    private String createdByUser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactCategory)) return false;

        ContactCategory that = (ContactCategory) o;

        return getCategory() != null ? getCategory().equals(that.getCategory()) : that.getCategory() == null;

    }

    @Override
    public int hashCode() {
        return getCategory() != null ? getCategory().hashCode() : 0;
    }

    @SerializedName("modifiedByUser")
    @Expose
    private Object modifiedByUser;
    @SerializedName("createDate")
    @Expose
    private Object createDate;
    @SerializedName("modifiedDate")
    @Expose
    private Object modifiedDate;
    @SerializedName("category")
    @Expose
    private String category;
    private final static long serialVersionUID = -5609156958062765008L;

    public String getForignID() {
        return forignID;
    }

    public void setForignID(String forignID) {
        this.forignID = forignID;
    }

    private String forignID;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
