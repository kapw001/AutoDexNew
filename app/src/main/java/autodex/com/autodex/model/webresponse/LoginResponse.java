package autodex.com.autodex.model.webresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yasar on 23/10/17.
 */

public class LoginResponse implements Serializable {

    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("headers")
    @Expose
    private Headers headers;
    private final static long serialVersionUID = 6969718106206194702L;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

}