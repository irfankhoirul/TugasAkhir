package proyekakhir.mapdemo.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

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

import proyekakhir.mapdemo.NonActivity.Devices;
import proyekakhir.mapdemo.NonActivity.UserFunctions;
import proyekakhir.mapdemo.R;

public class Activity5_Register extends AppCompatActivity implements View.OnClickListener {

    EditText reg_FirstName, reg_LastName, reg_Email, reg_Username, reg_Password, reg_Password2,
            reg_user_adminToken, reg_device_otherName, reg_device_type, reg_vehicle_otherName, reg_vehicle_type;
    RadioButton reg_user_standard, reg_user_admin, reg_device_asus, reg_device_lenovo,
            reg_device_samsung, reg_device_sony, reg_device_other, reg_vehicle_honda,
            reg_vehicle_yamaha, reg_vehicle_suzuki, reg_vehicle_kawasaki, reg_vehicle_other;
    RadioGroup reg_radioUser;
    Button reg_clear, reg_submit;

    String v_reg_FirstName, v_reg_LastName, v_reg_Email, v_reg_Username, v_reg_Password, v_reg_Password2,
            v_reg_user_adminToken, v_reg_device_otherName, v_reg_device_type, v_reg_vehicle_otherName, v_reg_vehicle_type,
            v_reg_device, v_reg_vehicle;
    int v_reg_user;

    String device, deviceName, deviceMan;

    /**
     *  JSON Response node names.
     **/
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcmObj;
    String regId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity5_register);

        try{
            android.support.v7.app.ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5D4037")));
        } catch(NullPointerException ex){
        //    Log.e("Null", ex.getMessage());
        }

        initialize();
        radioGroupVehicle();

        device = Devices.getDeviceName();
        deviceName = android.os.Build.MODEL;
        deviceMan = android.os.Build.MANUFACTURER;

        reg_clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearAll();
            }
        });

        reg_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean allow = valueCheck();
                if(allow) {
                    getValue();
                    NetAsync(v);
                }
                else
                    Toast.makeText(getBaseContext(), "Periksa Kembali!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getValue(){
        v_reg_FirstName = reg_FirstName.getText().toString();
        v_reg_LastName = reg_LastName.getText().toString();
        v_reg_Email = reg_Email.getText().toString();
        v_reg_Username = reg_Username.getText().toString();
        v_reg_Password = reg_Password.getText().toString();
        v_reg_Password2 = reg_Password2.getText().toString();

    //    v_reg_device_otherName = reg_device_otherName.getText().toString();
    //    v_reg_device_type = reg_device_type.getText().toString();
        v_reg_vehicle_otherName = reg_vehicle_otherName.getText().toString();
        v_reg_device_type = android.os.Build.MODEL;
        v_reg_vehicle_type = reg_vehicle_type.getText().toString();

        v_reg_device = Build.MANUFACTURER;

        if(reg_vehicle_honda.isChecked())
            v_reg_vehicle = "honda";
        else if(reg_vehicle_yamaha.isChecked())
            v_reg_vehicle = "yamaha";
        else if(reg_vehicle_suzuki.isChecked())
            v_reg_vehicle = "suzuki";
        else if(reg_vehicle_kawasaki.isChecked())
            v_reg_vehicle = "suzuki";
        else if(reg_vehicle_other.isChecked())
            v_reg_vehicle = v_reg_vehicle_otherName.toLowerCase();

    }

    public boolean valueCheck(){
        boolean ret = false;
        if(
                reg_FirstName.getText().toString().equals("") ||
                reg_LastName.getText().toString().equals("") ||
                reg_Email.getText().toString().equals("") ||
                reg_Username.getText().toString().equals("") ||
                reg_Password.getText().toString().equals("") ||
                reg_Password2.getText().toString().equals("") ||
        //        reg_device_type.getText().toString().equals("") ||
                reg_vehicle_type.getText().toString().equals("")
        ) {
            ret = false;
        //    Toast.makeText(getBaseContext(), "Catch 2", Toast.LENGTH_SHORT).show();
        }

        else if(
                !reg_vehicle_honda.isChecked() &&
                !reg_vehicle_yamaha.isChecked() &&
                !reg_vehicle_suzuki.isChecked() &&
                !reg_vehicle_kawasaki.isChecked() &&
                !reg_vehicle_other.isChecked()
        ) {
            ret = false;
        //    Toast.makeText(getBaseContext(), "Catch 4", Toast.LENGTH_SHORT).show();
        }

        else if(
                reg_vehicle_other.isChecked() && reg_vehicle_otherName.getText().toString().equals("")
        ) {
            ret = false;
        //    Toast.makeText(getBaseContext(), "Catch 6", Toast.LENGTH_SHORT).show();
        }

        else if(
                !(reg_Password.getText().toString().equals(reg_Password2.getText().toString()))
        ) {
            ret = false;
            Toast.makeText(getBaseContext(), "Password tidak cocok!", Toast.LENGTH_SHORT).show();
        }
        else ret = true;

        return ret;
    }

    public void radioGroupVehicle(){
        reg_vehicle_honda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reg_vehicle_honda.setChecked(true);
                reg_vehicle_yamaha.setChecked(false);
                reg_vehicle_suzuki.setChecked(false);
                reg_vehicle_kawasaki.setChecked(false);
                reg_vehicle_other.setChecked(false);
            }
        });

        reg_vehicle_yamaha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reg_vehicle_honda.setChecked(false);
                reg_vehicle_yamaha.setChecked(true);
                reg_vehicle_suzuki.setChecked(false);
                reg_vehicle_kawasaki.setChecked(false);
                reg_vehicle_other.setChecked(false);
            }
        });

        reg_vehicle_suzuki.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reg_vehicle_honda.setChecked(false);
                reg_vehicle_yamaha.setChecked(false);
                reg_vehicle_suzuki.setChecked(true);
                reg_vehicle_kawasaki.setChecked(false);
                reg_vehicle_other.setChecked(false);
            }
        });

        reg_vehicle_other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reg_vehicle_honda.setChecked(false);
                reg_vehicle_yamaha.setChecked(false);
                reg_vehicle_suzuki.setChecked(false);
                reg_vehicle_kawasaki.setChecked(false);
                reg_vehicle_other.setChecked(true);
            }
        });

        reg_vehicle_kawasaki.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reg_vehicle_honda.setChecked(false);
                reg_vehicle_yamaha.setChecked(false);
                reg_vehicle_suzuki.setChecked(false);
                reg_vehicle_kawasaki.setChecked(true);
                reg_vehicle_other.setChecked(false);
            }
        });
    }

    public void initialize(){
        reg_FirstName = (EditText)findViewById(R.id.reg_FirstName);
        reg_LastName = (EditText)findViewById(R.id.reg_LastName);
        reg_Email = (EditText)findViewById(R.id.reg_Email);
        reg_Username = (EditText)findViewById(R.id.reg_Username);
        reg_Password = (EditText)findViewById(R.id.reg_Password);
        reg_Password2 = (EditText)findViewById(R.id.reg_Password2);

        reg_vehicle_otherName = (EditText)findViewById(R.id.reg_vehicle_otherName);
        reg_vehicle_type = (EditText)findViewById(R.id.reg_vehicle_type);

        reg_vehicle_honda = (RadioButton)findViewById(R.id.reg_vehicle_honda);
        reg_vehicle_yamaha = (RadioButton)findViewById(R.id.reg_vehicle_yamaha);
        reg_vehicle_suzuki = (RadioButton)findViewById(R.id.reg_vehicle_suzuki);
        reg_vehicle_kawasaki = (RadioButton)findViewById(R.id.reg_vehicle_kawasaki);
        reg_vehicle_other = (RadioButton)findViewById(R.id.reg_vehicle_other);

        reg_clear = (Button)findViewById(R.id.reg_clear);
        reg_submit = (Button)findViewById(R.id.reg_submit);
    }

    public void clearAll(){
        reg_FirstName.setText(null);
        reg_LastName.setText(null);
        reg_Email.setText(null);
        reg_Username.setText(null);
        reg_Password.setText(null);
        reg_Password2.setText(null);
        reg_vehicle_otherName.setText(null);
        reg_vehicle_type.setText(null);

        reg_vehicle_honda.setChecked(false);
        reg_vehicle_yamaha.setChecked(false);
        reg_vehicle_suzuki.setChecked(false);
        reg_vehicle_kawasaki.setChecked(false);
        reg_vehicle_other.setChecked(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu___register, menu);
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

    @Override
    public void onClick(View v) {

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

    // Check if Google Playservices is installed in Device or not
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
            //    Toast.makeText(
            //            getApplicationContext(),
            //            "Bad. This device doesn't support Play services, App will not work normally",
            //            Toast.LENGTH_SHORT).show();
            //    finish();
            }
            return false;
        } else {
        //    Toast.makeText(
        //            getApplicationContext(),
        //            "Good. This device supports Play services, App will work normally",
        //            Toast.LENGTH_LONG).show();
        }
        return true;
    }


    /**
     * Async Task to check whether internet connection is working
     **/
    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Activity5_Register.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){
            /**
             * Gets current device state and checks for working internet connection by trying Google.
             **/
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
                if (checkPlayServices()) {

                    // Register Device in GCM Server
//                    registerInBackground(emailID);
                    new GmsRegister().execute();

                }

            }
            else{
                nDialog.dismiss();
                Toast.makeText(getBaseContext(), "Koneksi gagal!", Toast.LENGTH_SHORT).show();
            //    registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    private class GmsRegister extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Activity5_Register.this);
            pDialog.setTitle("GCM Registration");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            String msg = "";
            try {
                if (gcmObj == null) {
                    gcmObj = GoogleCloudMessaging
                            .getInstance(getApplicationContext());
                }
                regId = gcmObj
                        .register("693648714129");
                msg = "Registration ID :" + regId;

            } catch (IOException ex) {
                msg = "Error";
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String message) {
            if(!message.equals("Error")){
            //    Log.v("RegID", message);
                new ProcessRegister().execute();
//                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getBaseContext(), "Registrasi GMS gagal!", Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {
            private ProgressDialog pDialog;

            String p_reg_FirstName, p_reg_LastName, p_reg_Email, p_reg_Username, p_reg_Password,
                    p_reg_device_type, p_reg_vehicle_type, p_reg_user, p_reg_device,
                    p_reg_user_adminToken, p_reg_vehicle;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                p_reg_FirstName = v_reg_FirstName;
                p_reg_LastName = v_reg_LastName;
                p_reg_Email = v_reg_Email;
                p_reg_Username = v_reg_Username;
                p_reg_Password = v_reg_Password;
                p_reg_device = Build.MANUFACTURER;
                p_reg_device_type = Build.MODEL;
                p_reg_vehicle = v_reg_vehicle;
                p_reg_vehicle_type = v_reg_vehicle_type;

                pDialog = new ProgressDialog(Activity5_Register.this);
                pDialog.setTitle("Contacting Servers");
                pDialog.setMessage("Registering ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... args) {
                UserFunctions userFunction = new UserFunctions();
                JSONObject json = userFunction.registerUser(p_reg_FirstName, p_reg_LastName, p_reg_Email,
                        p_reg_Username, p_reg_Password, p_reg_device, p_reg_device_type, p_reg_vehicle,
                        p_reg_vehicle_type, regId);
                return json;
            }

            @Override
            protected void onPostExecute(JSONObject json) {

                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        //    Toast.makeText(getBaseContext(), "OK", Toast.LENGTH_LONG).show();
                        String res = json.getString(KEY_SUCCESS);
                        String red = json.getString(KEY_ERROR);

                        if(Integer.parseInt(res) == 1){
                            pDialog.setTitle("Getting Data");
                            pDialog.setMessage("Loading Info");
                            JSONObject json_user = json.getJSONObject("user");

                            pDialog.dismiss();

                            // Send Email Verification
                            new EmailVer().execute();

                        }

                        else if (Integer.parseInt(red) ==2){
                            pDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Username sudah digunakan!", Toast.LENGTH_SHORT).show();
                        }
                        else if (Integer.parseInt(red) ==4){
                            pDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Username sudah digunakan!", Toast.LENGTH_SHORT).show();
                        }
                        else if (Integer.parseInt(red) ==3){
                            pDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Email salah!", Toast.LENGTH_SHORT).show();
                        }

                    }   else{
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Registrasi gagal!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Exception = "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
    }

        public void NetAsync(View view){
            new NetCheck().execute();
        }

        private class EmailVer extends AsyncTask<String, String, String> {
            /**
             * Defining Process dialog
             **/
            private ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = new ProgressDialog(Activity5_Register.this);
                pDialog.setTitle("Contacting Servers");
                pDialog.setMessage("Sending Verification Email ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            protected String doInBackground(String... args) {
                // Create a new HttpClient and Post Header
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://surveyorider.com/SRS/mail.php");
                HttpResponse response = null;
                String str = "";

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                    nameValuePairs.add(new BasicNameValuePair("tag", "emailVerification"));
                    nameValuePairs.add(new BasicNameValuePair("email", v_reg_Email));
                    nameValuePairs.add(new BasicNameValuePair("token", generateToken(v_reg_Username)));
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
                Intent i = new Intent(Activity5_Register.this, Activity5a_RegisterEmailVerification.class);
                i.putExtra("token", generateToken(v_reg_Username));
                i.putExtra("email", v_reg_Email);
                i.putExtra("activity", "register");
                i.putExtra("username", v_reg_Username);
                startActivity(i);
                finish();
            }

            pDialog.dismiss();
        }
    }
}




