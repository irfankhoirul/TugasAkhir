package proyekakhir.mapdemo.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import proyekakhir.mapdemo.NonActivity.DatabaseHandler;
import proyekakhir.mapdemo.NonActivity.UserFunctions;
import proyekakhir.mapdemo.R;
import proyekakhir.mapdemo.NonActivity.User;


public class Activity1_Login extends Activity {

    Button _act1_bt_login, _act1_bt_register;
    EditText _act1_tf_username, _act1_tf_password;
    boolean cek = false;
    CheckBox bypass_login;
    String username = "";
    View arg0_view;

    /**
     * Called when the activity is first created.
     */
    private static String KEY_SUCCESS = "success";
//    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "username";

    String dbusername, user_id, nama_awal, nama_belakang, jenis_user, merk_smartphone,
            tipe_smartphone, merk_motor, tipe_motor, email, verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1_login);

    //    bypass_login = (CheckBox)findViewById(R.id.bypass_login);
        _act1_tf_username = (EditText)findViewById(R.id._act1_tf_username);
        _act1_tf_password = (EditText)findViewById(R.id._act1_tf_password);
        _act1_bt_login = (Button)findViewById(R.id._act1_bt_login);

        boolean cek = false;
        cek = cekLogin();

        if(cek)
        {
            Intent i = new Intent (Activity1_Login.this,Activity2_MainMap.class);
            startActivity(i);
            finish();
        }

        _act1_bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //--Proses Login--//
        //        if(bypass_login.isChecked()){
        //            Intent i = new Intent (Activity1_Login.this,Activity2_MainMap.class);
        //            startActivity(i);
        //            finish();
        //        }else {
                    if (_act1_tf_username.getText().toString().equals("") || _act1_tf_password.getText().toString().equals("")) {
                        Toast.makeText(getBaseContext(), "Fill Username and Password", Toast.LENGTH_SHORT).show();
                    //    Snackbar.make(arg0, "Fill Your Username and Password", Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        arg0_view = arg0;
                        login(arg0);
//                        insertData();
                    }
        //        }
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

    public void insertData()
    {
        User user = new User(dbusername, user_id, nama_awal, nama_belakang,
                jenis_user, merk_smartphone, tipe_smartphone, merk_motor,
                tipe_motor, email, verified);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        db.addUser(user);
    }

    public String generateToken(String input){
        StringBuilder token = new StringBuilder(input);
        StringBuilder newToken = token;
        newToken = newToken.reverse();
        if(token.length() < 10){
            int size = token.length();
            int generated = 10 - size;
            for(int i=0; i<generated; i++){
                newToken.append(token.charAt(i));
            }
        }
        else if(token.length() > 10){
            String newToken0 = newToken.substring(0,10);
            newToken = new StringBuilder(newToken0);
        }
        String newToken1 = newToken.toString().toUpperCase();
        newToken = new StringBuilder(newToken1);


        int ch;
        int[] c1= {1,2,3,1,2,3,1,2,3,1};
        int[] c2= {5,4,3,2,1,5,4,3,2,1};
        int[] c3= {0,9,1,8,2,7,3,6,4,5};
        char cha;

        StringBuilder progress1 = new StringBuilder();
        for(int i=0; i<10; i++){
            ch = (int) newToken.charAt(i);

            if(i==0 || i%2==0) ch-=c1[i];
            else ch+=c1[i];

            if(ch<65) ch=65;
            else if(ch>90) ch=90;

            cha = (char) ch;
            progress1.append(cha);
        }
        progress1 = progress1.reverse();

        StringBuilder progress2 = new StringBuilder();
        for(int i=0; i<10; i++){
            ch = (int) progress1.charAt(i);

            if(i==0) ch+=c2[i];
            else if(i==1) ch-=c2[i];
            else if(i==2) ch+=c2[i];
            else if(i==3) ch-=c2[i];
            else if(i==4) ch+=c2[i];
            else if(i==5) ch-=c2[i];
            else if(i==6) ch+=c2[i];
            else if(i==7) ch-=c2[i];
            else if(i==8) ch+=c2[i];
            else if(i==9) ch-=c2[i];

            if(ch<65)
                ch=65;
            else if(ch>90)
                ch=90;

            cha = (char) ch;
            progress2.append(cha);
        }

        StringBuilder progress3 = new StringBuilder();
        for(int i=0; i<10; i++) {
            cha = progress2.charAt(c3[i]);
            progress3.append(cha);
        }

        return progress3.toString();
//        Toast.makeText(getBaseContext(), progress3.toString(), Toast.LENGTH_LONG).show();
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
                    URL url = new URL("http://surveyorider.com/SRS/");
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

            if(th){
                nDialog.dismiss();
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
//                Toast.makeText(getBaseContext(), "Error in Network Connection", Toast.LENGTH_SHORT).show();
                SnackbarManager.show(
                        Snackbar.with(Activity1_Login.this)
                                .text("Koneksi Gagal!")
                                .actionLabel("COBA LAGI") // action button label
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        new NetCheck().execute();
                                    }
                                }) // action button's ActionClickListener
                                .actionColor(Color.parseColor("#CDDC39"))
                        , Activity1_Login.this);
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
                        pDialog.setTitle("Getting Data");
                        JSONObject json_user = json.getJSONObject("user");

                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        username = json_user.getString("username");
                        dbusername = json_user.getString("username");

                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());

                        pDialog.dismiss();
//                        finish();

                        new getUserDetails().execute();

                    }else{
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Incorrect Username or Password!", Toast.LENGTH_SHORT).show();
//                        Snackbar.make(arg0_view, "Incorrect Username or Password!", Snackbar.LENGTH_SHORT).show();
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

                        JSONObject json_user = json.getJSONObject("details");

                        user_id = json_user.getString("user_id");
                        nama_awal = json_user.getString("nama_awal");
                        nama_belakang = json_user.getString("nama_belakang");
                        jenis_user = "null";
                        merk_smartphone = json_user.getString("merk_smartphone");
                        tipe_smartphone = json_user.getString("tipe_smartphone");
                        merk_motor = json_user.getString("merk_motor");
                        tipe_motor = json_user.getString("tipe_motor");
                        email = json_user.getString("email");
                        verified = json_user.getString("verified");

                        /*
                        Toast.makeText(getBaseContext(),
                                "username = "+dbusername+
                                "\nuser_id = "+user_id+
                                "\nnama_awal = "+nama_awal+
                                "\nnama_belakang = "+nama_belakang+
                                "\nmerk_smartphone = "+merk_smartphone+
                                "\ntipe_smartphone = "+tipe_smartphone+
                                "\nmerk_motor = "+merk_motor+
                                "\ntipe_motor = "+tipe_motor+
                                "\nemail = "+email+
                                "\nverified = "+verified
                                ,Toast.LENGTH_LONG).show();
                        */

                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());

                        try {
                            User user = new User(dbusername, user_id, nama_awal, nama_belakang,
                                    jenis_user, merk_smartphone, tipe_smartphone, merk_motor,
                                    tipe_motor, email, verified);

                            ///////Start main activity
                            if(verified.equals("1")) {
                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                db.addUser(user);
                                Toast.makeText(getBaseContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
//                                Snackbar.make(arg0_view, "Login Successful!", Snackbar.LENGTH_SHORT).show();
                                Intent i = new Intent(Activity1_Login.this, Activity2_MainMap.class);
                                startActivity(i);
                                pDialog.dismiss();
                                finish();
                            }
                            else {
                                Toast.makeText(getBaseContext(), "You haven't verified yet. Please verify your email!", Toast.LENGTH_SHORT).show();

                                pDialog.dismiss();
                                new EmailVer().execute();
                            //    finish();
                            }

                        }
                        catch(Exception e)
                        {
//                            Toast.makeText(getBaseContext(), "Error saving user info!", Toast.LENGTH_LONG).show();
//                            Snackbar.make(arg0_view, "Error inserting data!", Snackbar.LENGTH_SHORT).show();
                            SnackbarManager.show(
                                    Snackbar.with(Activity1_Login.this)
                                            .text("Pengambilan Data User Gagal!!")
                                            .actionLabel("COBA LAGI") // action button label
                                            .actionListener(new ActionClickListener() {
                                                @Override
                                                public void onActionClicked(Snackbar snackbar) {
                                                    new getUserDetails().execute();
                                                }
                                            }) // action button's ActionClickListener
                                            .actionColor(Color.parseColor("#CDDC39"))
                                    , Activity1_Login.this);
                        }

                    }else{
                        pDialog.dismiss();
//                        Toast.makeText(getBaseContext(), "Failed get user details!", Toast.LENGTH_SHORT).show();
//                        Snackbar.make(arg0_view, "Failed get user details!", Snackbar.LENGTH_SHORT).show();
                        SnackbarManager.show(
                                Snackbar.with(Activity1_Login.this)
                                        .text("Pengambilan Data User Gagal!!")
                                        .actionLabel("COBA LAGI") // action button label
                                        .actionListener(new ActionClickListener() {
                                            @Override
                                            public void onActionClicked(Snackbar snackbar) {
                                                new getUserDetails().execute();
                                            }
                                        }) // action button's ActionClickListener
                                        .actionColor(Color.parseColor("#CDDC39"))
                                , Activity1_Login.this);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class EmailVer extends AsyncTask<String, String, String> {
        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Activity1_Login.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://muhlish.com/ta/mail.php");
            HttpResponse response = null;
            String str = "";

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("tag", "emailVerification"));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("token", generateToken(dbusername)));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                response = httpclient.execute(httppost);
                str =  EntityUtils.toString(response.getEntity());

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return str;
        }

        @Override
        protected void onPostExecute(String json) {
            if(Integer.parseInt(json)==1){
                Intent i = new Intent(Activity1_Login.this, Activity5a_RegisterEmailVerification.class);
                i.putExtra("token", generateToken(dbusername));
                i.putExtra("email", email);
                i.putExtra("activity", "login");
                i.putExtra("username", dbusername);
                startActivity(i);
                finish();
            }

            pDialog.dismiss();
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
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }
}