package autodex.com.autodex.model;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 11/13/15.
 */
public class Contact implements Serializable {
    private String mName;
    private String phoneNumber;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Contact() {
    }

    private String uri;
    private int imgId;

    public boolean is_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(boolean is_favourite) {
        this.is_favourite = is_favourite;
    }

    private boolean is_favourite = true;

    public Contact(String mName, int imgId, String number) {
        this.mName = mName;
        this.imgId = imgId;
        this.number = number;
    }

    private String number;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Contact(String pName, String uri) {
        mName = pName;
        this.uri = uri;
    }

    public String getName() {
        return mName;
    }

    public void setName(String pName) {
        mName = pName;
    }
}
