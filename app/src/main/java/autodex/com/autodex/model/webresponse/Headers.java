package autodex.com.autodex.model.webresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yasar on 23/10/17.
 */

public class Headers implements Serializable {

    @SerializedName("X-Android-Sent-Millis")
    @Expose
    private String xAndroidSentMillis;
    @SerializedName("Content-Type")
    @Expose
    private String contentType;
    @SerializedName("X-Android-Selected-Protocol")
    @Expose
    private String xAndroidSelectedProtocol;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Transfer-Encoding")
    @Expose
    private String transferEncoding;
    @SerializedName("X-Android-Response-Source")
    @Expose
    private String xAndroidResponseSource;
    @SerializedName("Connection")
    @Expose
    private String connection;
    @SerializedName("Authorization")
    @Expose
    private String authorization;
    @SerializedName("X-Android-Received-Millis")
    @Expose
    private String xAndroidReceivedMillis;
    private final static long serialVersionUID = 6242988917577047540L;

    public String getXAndroidSentMillis() {
        return xAndroidSentMillis;
    }

    public void setXAndroidSentMillis(String xAndroidSentMillis) {
        this.xAndroidSentMillis = xAndroidSentMillis;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getXAndroidSelectedProtocol() {
        return xAndroidSelectedProtocol;
    }

    public void setXAndroidSelectedProtocol(String xAndroidSelectedProtocol) {
        this.xAndroidSelectedProtocol = xAndroidSelectedProtocol;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransferEncoding() {
        return transferEncoding;
    }

    public void setTransferEncoding(String transferEncoding) {
        this.transferEncoding = transferEncoding;
    }

    public String getXAndroidResponseSource() {
        return xAndroidResponseSource;
    }

    public void setXAndroidResponseSource(String xAndroidResponseSource) {
        this.xAndroidResponseSource = xAndroidResponseSource;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getXAndroidReceivedMillis() {
        return xAndroidReceivedMillis;
    }

    public void setXAndroidReceivedMillis(String xAndroidReceivedMillis) {
        this.xAndroidReceivedMillis = xAndroidReceivedMillis;
    }

}