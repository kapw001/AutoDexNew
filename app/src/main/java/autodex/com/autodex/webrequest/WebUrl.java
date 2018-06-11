package autodex.com.autodex.webrequest;

/**
 * Created by yasar on 4/10/17.
 */
//c4018b31-bf9e-4103-87dc-2840f78ddab1
public class WebUrl {

    public static final String BASEURL = "http://autodex-api-dev.safelogicsolutions.com/autodexweb/";
    public static final String BASEAPI = "rest/";
    public static final String URL = BASEURL + BASEAPI;
    public static final String LOGIN = URL + "login";
    public static final String CREATEPROFILE = URL + "user/createProfile";
    public static final String GETCONTACTLIST = URL + "contacts/list";
    public static final String ADDCONTACT = URL + "contacts/";
    public static final String GETCONTACT = URL + "contacts/";
    public static final String GETPROFILEBYPHONENUMBER = URL + "user/getByAutoDexNum/";
    public static final String GETPROFILEIMAGE = URL + "user/profileimage";
    public static final String UPDATEPROFILE = URL + "user/";
    public static final String PROFILEIMAGEUPLOAD = URL + "user/profileimage";
    public static final String SAVEALLCONTACTS = URL + "contacts/saveAllContacts";
    public static final String CONTACTPROFILEIMAGEUPLOAD = URL + "updateProfileImage/";
    public static final String SAVEPREFERENCE = URL + "user/preference";
    public static final String GEOLOCATION = URL + "user/geolocation";
    public static final String GENERATEOTP = URL + "login/generateotp";
    public static final String VALIDATEOTP = URL + "login/validateotp";
    public static final String CHANGEPASSWORD = URL + "user/password";
    public static final String LOGOUT = URL + "user/logout";
    public static final String ADDUSERDEVICE = URL + "user/addUserDevice";
    public static final String NOTIFICATION_LIST = URL + "user/notification/list";
    public static final String NOTIFICATION_READ = URL + "user/notification";
    public static final String changeAutodexNumber = URL + "user/changeAutodexNumber";


}
