package proyekakhir.mapdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import proyekakhir.mapdemo.library.DatabaseHandler;
import proyekakhir.mapdemo.library.UserFunctions;


public class Activity1_Login extends Activity {

    Button _act1_bt_login, _act1_bt_register;
    EditText _act1_tf_username, _act1_tf_password;
    boolean cek = false;
    CheckBox bypass_login;
    String username = "";

    /**
     * Called when the activity is first created.
     */
    private static String KEY_SUCCESS = "success";
//    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "username";
//    private static String KEY_FIRSTNAME = "fname";
//    private static String KEY_LASTNAME = "lname";
//    private static String KEY_EMAIL = "email";
//    private static String KEY_CREATED_AT = "created_at";

    String dbusername, user_id, nama_awal, nama_belakang, jenis_user, merk_smartphone,
            tipe_smartphone, merk_motor, tipe_motor, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity1_login);
        bypass_login = (CheckBox)findViewById(R.id.bypass_login);
        _act1_tf_username = (EditText)findViewById(R.id._act1_tf_username);
        _act1_tf_password = (EditText)findViewById(R.id._act1_tf_password);

        _act1_bt_login = (Button)findViewById(R.id._act1_bt_login);

        boolean cek = false;
        cek = cekLogin();

        if(cek == true)
        {
            Intent i = new Intent (Activity1_Login.this,Activity2_MainMap.class);
            startActivity(i);
            finish();
        }

        _act1_bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //--Proses Login--//
                if(bypass_login.isChecked()){
                    Intent i = new Intent (Activity1_Login.this,Activity2_MainMap.class);
                    startActivity(i);
                    finish();
                }else {
                    if (_act1_tf_username.getText().toString().equals("") || _act1_tf_password.getText().toString().equals(""))
                        Toast.makeText(getBaseContext(), "Fill Username and Password", Toast.LENGTH_SHORT).show();
                    else {
                        login(arg0);
                    }
                }
            }
        });

        _act1_bt_register = (Button)findViewById(R.id._act1_bt_register);
        _act1_bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Activity1_Login.this, Activity5_Register.class);
                startActivity(i);
            }

        });
    }

    public boolean cekLogin(){
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//        HashMap<String, String> user =  db.getUserDetails();

        if(db.getRowCount()!=1)
//        if(user.get("username").equals(null) || user.get("username").equals(""))
            return false;
        else
            return true;
    }

    public void login(View v){
        NetAsync(v);
    }

    /**
     * Async Task to check whether internet connection is working.
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Activity1_Login.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
         **/
        @Override
        protected Boolean doInBackground(String... args){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(getBaseContext(), "Error in Network Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String p_username, p_password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            p_username = _act1_tf_username.getText().toString();
            p_password = _act1_tf_password.getText().toString();
            pDialog = new ProgressDialog(Activity1_Login.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(p_username, p_password);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){
                        pDialog.setMessage("Loading User Space");
                        pDialog.setTitle("Getting Data");
//                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        username = json_user.getString("username");
                        dbusername = json_user.getString("username");
//                        Toast.makeText(getBaseContext(), "Your username = "+username, Toast.LENGTH_SHORT).show();

                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
//                        db.addUser(username);

                        ///////Start main activity
//                        Toast.makeText(getBaseContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent (Activity1_Login.this,Activity2_MainMap.class);
//                        startActivity(i);
                        pDialog.dismiss();
//                        finish();

                        new getUserDetails().execute();

                    }else{
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Incorrect Username or Password2!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class getUserDetails extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Activity1_Login.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.getUserDetails(username);
//            Toast.makeText(getBaseContext(), "Your username is = "+username, Toast.LENGTH_SHORT).show();

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){
                        pDialog.setMessage("Loading User Details");
                        pDialog.setTitle("Getting Data");

                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("details");

                        user_id = json_user.getString("user_id");
                        nama_awal = json_user.getString("nama_awal");
                        nama_belakang = json_user.getString("nama_belakang");
                        jenis_user = json_user.getString("jenis_user");
                        merk_smartphone = json_user.getString("merk_smartphone");
                        tipe_smartphone = json_user.getString("tipe_smartphone");
                        merk_motor = json_user.getString("merk_motor");
                        tipe_motor = json_user.getString("tipe_motor");
                        email = json_user.getString("email");

                        Toast.makeText(getBaseContext(),
                                "username = "+dbusername+
                                "user_id = "+user_id+
                                "nama_awal = "+nama_awal+
                                "nama_belakang = "+nama_belakang+
                                "jenis_user = "+jenis_user+
                                "merk_smartphone = "+merk_smartphone+
                                "tipe_smartphone = "+tipe_smartphone+
                                "merk_motor = "+merk_motor+
                                "tipe_motor = "+tipe_motor+
                                "email = "+email
                                ,Toast.LENGTH_LONG).show();

                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(
                                dbusername, user_id, nama_awal, nama_belakang,
                                jenis_user, merk_smartphone, tipe_smartphone, merk_motor,
                                tipe_motor, email
                                );

                        ///////Start main activity
//                        Toast.makeText(getBaseContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent (Activity1_Login.this,Activity2_MainMap.class);
//                        startActivity(i);
                        pDialog.dismiss();
//                        finish();

                    }else{
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Failed get user details!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void NetAsync(View view){
        new NetCheck().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu___home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}