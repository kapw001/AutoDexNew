package autodex.com.autodex.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yasar on 14/9/17.
 */

public class NotificationModel implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("createdByUser")
    @Expose
    private String createdByUser;
    @SerializedName("modifiedByUser")
    @Expose
    private String modifiedByUser;
    @SerializedName("createDate")
    @Expose
    private Object createDate;
    @SerializedName("modifiedDate")
    @Expose
    private Object modifiedDate;
    @SerializedName("contactId")
    @Expose
    private Long contactId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("read")
    @Expose
    private Boolean read;
    public final static Parcelable.Creator<NotificationModel> CREATOR = new Creator<NotificationModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public NotificationModel createFromParcel(Parcel in) {
            return new NotificationModel(in);
        }

        public NotificationModel[] newArray(int size) {
            return (new NotificationModel[size]);
        }

    }
            ;
    private final static long serialVersionUID = -6135863446475358192L;

    protected NotificationModel(Parcel in) {
        this.id = ((Long) in.readValue((Long.class.getClassLoader())));
        this.createdByUser = ((String) in.readValue((String.class.getClassLoader())));
        this.modifiedByUser = ((String) in.readValue((String.class.getClassLoader())));
        this.createDate = ((Object) in.readValue((Object.class.getClassLoader())));
        this.modifiedDate = ((Object) in.readValue((Object.class.getClassLoader())));
        this.contactId = ((Long) in.readValue((Long.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.read = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public NotificationModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(String modifiedByUser) {
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

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(createdByUser);
        dest.writeValue(modifiedByUser);
        dest.writeValue(createDate);
        dest.writeValue(modifiedDate);
        dest.writeValue(contactId);
        dest.writeValue(type);
        dest.writeValue(message);
        dest.writeValue(read);
    }

    public int describeContents() {
        return 0;
    }

}