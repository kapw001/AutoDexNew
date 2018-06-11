
package volleywebrequest.com.mysqlitelibrary;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactAttribute implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("createdByUser")
    @Expose
    private String createdByUser;
    @SerializedName("modifiedByUser")
    @Expose
    private Object modifiedByUser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactAttribute)) return false;

        ContactAttribute that = (ContactAttribute) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        return getValue() != null ? getValue().equals(that.getValue()) : that.getValue() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }

    @SerializedName("createDate")
    @Expose

    private Object createDate;
    @SerializedName("modifiedDate")
    @Expose
    private Object modifiedDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("value")
    @Expose
    private String value;
    private final static long serialVersionUID = 7324011954236559688L;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
