package proyekakhir.mapdemo.library;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UserFunctions {

    private JSONParser jsonParser;

    //Local
//    private static String loginURL = "http://192.168.0.106:81/SurveyoRiderServices/";
//    private static String registerURL = "http://192.168.0.106:81/SurveyoRiderServices/";

    //Online muhlish
//    private static String loginURL = "http://muhlish.com/ta/SurveyoRiderServices/";
//    private static String registerURL = "http://muhlish.com/ta/SurveyoRiderServices/";

    //Onlune free
//    private static String loginURL    = "http://surveyorider.zz.mu/SurveyoRiderServices/";
//    private static String registerURL = "http://surveyorider.zz.mu/SurveyoRiderServices/";

    private static String loginURL    = "http://surveyorider.com/SRS/";
    private static String registerURL = "http://surveyorider.com/SRS/";

    private static String emailURL = "http://muhlish.com/ta/mail.php";

    private static String login_tag = "login";
    private static String get_user_details_tag = "getUserDetails";
    private static String register_tag = "register";
    private static String updateUser_tag = "updateUser";

    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();

    }

    /**
     * Function to Login
     **/
    public JSONObject loginUser(String username, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }

    /**
     * Sending data
     **/
    public JSONObject sendData(String idUser, String string_json){
        // Building Parameters

        JSONParser jp = new JSONParser();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "insert"));
        params.add(new BasicNameValuePair("idUser", idUser));
        params.add(new BasicNameValuePair("json", string_json));
        JSONObject json = jp.getJSONFromUrl(loginURL, params);

        return json;
    }

    /**
     * Function to Get User Details
     **/
    public JSONObject getUserDetails(String username){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_user_details_tag));
        params.add(new BasicNameValuePair("username", username));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

        return json;
    }

    /**
     * Function to Verify User
     **/
    public JSONObject verifyUser(String email){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "verifyUser"));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

        return json;
    }

    /**
     * Function to Change Email
     **/
    public JSONObject updateEmail(String email, String newEmailAddress){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "updateEmail"));
        params.add(new BasicNameValuePair("old_email", email));
        params.add(new BasicNameValuePair("new_email", newEmailAddress));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

        return json;
    }

    /**
     * Function to Get All Roads
     **/
    public JSONObject getAllRoadData(String where, String start, String end, String userId){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "getRoadDataByAll"));
        params.add(new BasicNameValuePair("userId", userId));
        params.add(new BasicNameValuePair("where", where));
        params.add(new BasicNameValuePair("start", start));
        params.add(new BasicNameValuePair("end", end));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

        return json;
    }

    /**
     * Function to Get All Roads
     **/
    public JSONObject getAllRoadDataKhusus(String where, String start, String end, String userId, String range){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "getRoadDataKhusus"));
        params.add(new BasicNameValuePair("userId", userId));
        params.add(new BasicNameValuePair("where", where));
        params.add(new BasicNameValuePair("start", start));
        params.add(new BasicNameValuePair("range", range));
        params.add(new BasicNameValuePair("end", end));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

        return json;
    }

    /**
     * Function to Get Road Details
     **/
    public JSONObject getRoadDataDetails(String fullAddress){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "getRoadDataDetails"));
        params.add(new BasicNameValuePair("full_address", fullAddress));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

        return json;
    }

    /**
     * Function to Get Road Details
     **/
    public JSONObject getFilter(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "getFilter"));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

        return json;
    }

    /**
     * Function to change password
     **/
/*    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));

        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, params);
        return json;
    }
*/
    /**
     * Function to reset the password
     **/
/*    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL, params);
        return json;
    }
*/
     /**
      * Function to  Register
      **/
    public JSONObject registerUser(String pfirst_name, String plast_name, String pemail,
                                   String pusername, String ppassword,
                            //       String puser_type, String padmin_token,
                                   String pdevice_merk, String pdevice_type,
                                   String pvehicle_merk, String pvehicle_type){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));

        params.add(new BasicNameValuePair("first_name", pfirst_name));
        params.add(new BasicNameValuePair("last_name", plast_name));
        params.add(new BasicNameValuePair("email", pemail));
        params.add(new BasicNameValuePair("username", pusername));
        params.add(new BasicNameValuePair("password", ppassword));
//        params.add(new BasicNameValuePair("user_type", puser_type));
//        params.add(new BasicNameValuePair("admin_token", padmin_token));
        params.add(new BasicNameValuePair("device_merk", pdevice_merk));
        params.add(new BasicNameValuePair("device_type", pdevice_type));
        params.add(new BasicNameValuePair("vehicle_merk", pvehicle_merk));
        params.add(new BasicNameValuePair("vehicle_type", pvehicle_type));

        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
        return json;
    }

    public JSONObject updateUser(String userID, String poldpassword, String pfirst_name,
                                 String plast_name, String pusername, String ppassword,
    //                             String puser_type, String padmin_token,
                                 String pdevice_merk, String pdevice_type,
                                 String pvehicle_merk, String pvehicle_type){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", updateUser_tag));

        params.add(new BasicNameValuePair("uid", userID));
        params.add(new BasicNameValuePair("old_password", poldpassword));
        params.add(new BasicNameValuePair("first_name", pfirst_name));
        params.add(new BasicNameValuePair("last_name", plast_name));
    //    params.add(new BasicNameValuePair("email", pemail));
        params.add(new BasicNameValuePair("username", pusername));
        params.add(new BasicNameValuePair("password", ppassword));
    //    params.add(new BasicNameValuePair("user_type", puser_type));
    //    params.add(new BasicNameValuePair("admin_token", padmin_token));
        params.add(new BasicNameValuePair("device_merk", pdevice_merk));
        params.add(new BasicNameValuePair("device_type", pdevice_type));
        params.add(new BasicNameValuePair("vehicle_merk", pvehicle_merk));
        params.add(new BasicNameValuePair("vehicle_type", pvehicle_type));

        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
        return json;
    }

    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}