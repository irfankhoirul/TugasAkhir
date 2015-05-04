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

    //URL of the PHP API
//    private static String loginURL = "http://10.0.2.2/learn2crack_login_api/";
//    private static String registerURL = "http://10.0.2.2/learn2crack_login_api/";
//    private static String forpassURL = "http://10.0.2.2/learn2crack_login_api/";
//    private static String chgpassURL = "http://10.0.2.2/learn2crack_login_api/";

    private static String loginURL = "http://192.168.0.106:81/SurveyoRiderServices/";
    private static String registerURL = "http://192.168.0.106:81/SurveyoRiderServices/";
//    private static String forpassURL = "http://192.168.0.101:81/SurveyoRiderServices/";
//    private static String chgpassURL = "http://192.168.0.101:81/SurveyoRiderServices/";


    private static String login_tag = "login";
    private static String get_user_details_tag = "getUserDetails";
    private static String register_tag = "register";
    private static String updateUser_tag = "updateUser";
//    private static String forpass_tag = "forpass";
//    private static String chgpass_tag = "chgpass";


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
                                   String pusername, String ppassword, String puser_type,
                                   String padmin_token, String pdevice_merk, String pdevice_type,
                                   String pvehicle_merk, String pvehicle_type){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));

        params.add(new BasicNameValuePair("first_name", pfirst_name));
        params.add(new BasicNameValuePair("last_name", plast_name));
        params.add(new BasicNameValuePair("email", pemail));
        params.add(new BasicNameValuePair("username", pusername));
        params.add(new BasicNameValuePair("password", ppassword));
        params.add(new BasicNameValuePair("user_type", puser_type));
        params.add(new BasicNameValuePair("admin_token", padmin_token));
        params.add(new BasicNameValuePair("device_merk", pdevice_merk));
        params.add(new BasicNameValuePair("device_type", pdevice_type));
        params.add(new BasicNameValuePair("vehicle_merk", pvehicle_merk));
        params.add(new BasicNameValuePair("vehicle_type", pvehicle_type));

        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
        return json;
    }

    public JSONObject updateUser(String userID, String poldpassword, String pfirst_name,  String plast_name, String pusername,
                                  String ppassword, String puser_type,
                                   String padmin_token, String pdevice_merk, String pdevice_type,
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
        params.add(new BasicNameValuePair("user_type", puser_type));
        params.add(new BasicNameValuePair("admin_token", padmin_token));
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