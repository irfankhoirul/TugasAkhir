package proyekakhir.mapdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import proyekakhir.mapdemo.library.UserFunctions;

public class Activity5_Register extends ActionBarActivity implements View.OnClickListener {

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

    /**
     *  JSON Response node names.
     **/
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity5_register);
        initialize();
        radioGroupVehicle();
        radioGroupDevice();

        reg_clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearAll();
            }
        });

        reg_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean allow = valueCheck();
                if(allow == true) {
//                    Toast.makeText(getBaseContext(), "Right Value", Toast.LENGTH_SHORT).show();
                    getValue();
//                    register();
                    NetAsync(v);
                }
                else
                    Toast.makeText(getBaseContext(), "Please check again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void register(){
        // Register Button Click event

    }

    public void getValue(){
        v_reg_FirstName = reg_FirstName.getText().toString();
        v_reg_LastName = reg_LastName.getText().toString();
        v_reg_Email = reg_Email.getText().toString();
        v_reg_Username = reg_Username.getText().toString();
        v_reg_Password = reg_Password.getText().toString();
        v_reg_Password2 = reg_Password2.getText().toString();
        v_reg_device_otherName = reg_device_otherName.getText().toString();
        v_reg_device_type = reg_device_type.getText().toString();
        v_reg_vehicle_otherName = reg_vehicle_otherName.getText().toString();
        v_reg_vehicle_type = reg_vehicle_type.getText().toString();

        if(reg_user_standard.isChecked()) {
            v_reg_user = 1;
            v_reg_user_adminToken = "";
        }
        else if(reg_user_admin.isChecked()) {
            v_reg_user = 2;
            v_reg_user_adminToken = reg_user_adminToken.getText().toString();
        }

        if(reg_device_asus.isChecked())
            v_reg_device = "asus";
        else if(reg_device_lenovo.isChecked())
            v_reg_device = "lenovo";
        else if(reg_device_samsung.isChecked())
            v_reg_device = "samsung";
        else if(reg_device_sony.isChecked())
            v_reg_device = "sony";
        else if(reg_device_other.isChecked())
            v_reg_device = v_reg_device_otherName.toLowerCase();

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
                reg_user_standard.isChecked() == false &&
                reg_user_admin.isChecked() == false
        ) {
            ret = false;
        //    Toast.makeText(getBaseContext(), "Catch 1", Toast.LENGTH_SHORT).show();
        }
        else if(
                reg_FirstName.getText().toString().equals("") ||
                reg_LastName.getText().toString().equals("") ||
                reg_Email.getText().toString().equals("") ||
                reg_Username.getText().toString().equals("") ||
                reg_Password.getText().toString().equals("") ||
                reg_Password2.getText().toString().equals("") ||
                reg_device_type.getText().toString().equals("") ||
                reg_vehicle_type.getText().toString().equals("")
        ) {
            ret = false;
        //    Toast.makeText(getBaseContext(), "Catch 2", Toast.LENGTH_SHORT).show();
        }
        else if(
                reg_device_asus.isChecked() == false &&
                reg_device_lenovo.isChecked() == false &&
                reg_device_samsung.isChecked() == false &&
                reg_device_sony.isChecked() == false &&
                reg_device_other.isChecked() == false
        ) {
            ret = false;
        //    Toast.makeText(getBaseContext(), "Catch 3", Toast.LENGTH_SHORT).show();
        }
        else if(
                reg_vehicle_honda.isChecked() == false &&
                reg_vehicle_yamaha.isChecked() == false &&
                reg_vehicle_suzuki.isChecked() == false &&
                reg_vehicle_kawasaki.isChecked() == false &&
                reg_vehicle_other.isChecked() == false
        ) {
            ret = false;
        //    Toast.makeText(getBaseContext(), "Catch 4", Toast.LENGTH_SHORT).show();
        }
        else if(
                reg_device_other.isChecked() == true && reg_device_otherName.getText().toString().equals("")
        ) {
            ret = false;
        //    Toast.makeText(getBaseContext(), "Catch 5", Toast.LENGTH_SHORT).show();
        }
        else if(
                reg_vehicle_other.isChecked() == true && reg_vehicle_otherName.getText().toString().equals("")
        ) {
            ret = false;
        //    Toast.makeText(getBaseContext(), "Catch 6", Toast.LENGTH_SHORT).show();
        }
        else if(
                reg_user_admin.isChecked() == true && reg_user_adminToken.getText().toString().equals("")
        ) {
            ret = false;
        //    Toast.makeText(getBaseContext(), "Catch 7", Toast.LENGTH_SHORT).show();
        }
        else if(
                !(reg_Password.getText().toString().equals(reg_Password2.getText().toString()))
        ) {
            ret = false;
            Toast.makeText(getBaseContext(), "Password didn't match!", Toast.LENGTH_SHORT).show();
        }
        else ret = true;

        return ret;
    }

    public void radioGroupDevice(){
        reg_device_asus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reg_device_asus.setChecked(true);
                reg_device_lenovo.setChecked(false);
                reg_device_samsung.setChecked(false);
                reg_device_sony.setChecked(false);
                reg_device_other.setChecked(false);
            }
        });

        reg_device_lenovo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reg_device_asus.setChecked(false);
                reg_device_lenovo.setChecked(true);
                reg_device_samsung.setChecked(false);
                reg_device_sony.setChecked(false);
                reg_device_other.setChecked(false);
            }
        });

        reg_device_samsung.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reg_device_asus.setChecked(false);
                reg_device_lenovo.setChecked(false);
                reg_device_samsung.setChecked(true);
                reg_device_sony.setChecked(false);
                reg_device_other.setChecked(false);
            }
        });

        reg_device_sony.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reg_device_asus.setChecked(false);
                reg_device_lenovo.setChecked(false);
                reg_device_samsung.setChecked(false);
                reg_device_sony.setChecked(true);
                reg_device_other.setChecked(false);
            }
        });

        reg_device_other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reg_device_asus.setChecked(false);
                reg_device_lenovo.setChecked(false);
                reg_device_samsung.setChecked(false);
                reg_device_sony.setChecked(false);
                reg_device_other.setChecked(true);
            }
        });
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
        reg_user_adminToken = (EditText)findViewById(R.id.reg_user_adminToken);
        reg_device_otherName = (EditText)findViewById(R.id.reg_device_otherName);
        reg_device_type = (EditText)findViewById(R.id.reg_device_type);
        reg_vehicle_otherName = (EditText)findViewById(R.id.reg_vehicle_otherName);
        reg_vehicle_type = (EditText)findViewById(R.id.reg_vehicle_type);

        reg_user_standard = (RadioButton)findViewById(R.id.reg_user_standard);
        reg_user_admin = (RadioButton)findViewById(R.id.reg_user_admin);
        reg_device_asus = (RadioButton)findViewById(R.id.reg_device_asus);
        reg_device_lenovo = (RadioButton)findViewById(R.id.reg_device_lenovo);
        reg_device_samsung = (RadioButton)findViewById(R.id.reg_device_samsung);
        reg_device_sony = (RadioButton)findViewById(R.id.reg_device_sony);
        reg_device_other = (RadioButton)findViewById(R.id.reg_device_other);
        reg_vehicle_honda = (RadioButton)findViewById(R.id.reg_vehicle_honda);
        reg_vehicle_yamaha = (RadioButton)findViewById(R.id.reg_vehicle_yamaha);
        reg_vehicle_suzuki = (RadioButton)findViewById(R.id.reg_vehicle_suzuki);
        reg_vehicle_kawasaki = (RadioButton)findViewById(R.id.reg_vehicle_kawasaki);
        reg_vehicle_other = (RadioButton)findViewById(R.id.reg_vehicle_other);

        reg_radioUser = (RadioGroup)findViewById(R.id.reg_radioUser);

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
        reg_user_adminToken.setText(null);
        reg_device_otherName.setText(null);
        reg_device_type.setText(null);
        reg_vehicle_otherName.setText(null);
        reg_vehicle_type.setText(null);

        reg_user_standard.setChecked(false);
        reg_user_admin.setChecked(false);
        reg_device_asus.setChecked(false);
        reg_device_lenovo.setChecked(false);
        reg_device_samsung.setChecked(false);
        reg_device_sony.setChecked(false);
        reg_device_other.setChecked(false);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

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
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(getBaseContext(), "Error in Network Connection", Toast.LENGTH_SHORT).show();
            //    registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {
        /**
         * Defining Process dialog
         **/
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
            p_reg_user = Integer.toString(v_reg_user);
            p_reg_user_adminToken = v_reg_user_adminToken;
            p_reg_device = v_reg_device;
            p_reg_device_type = v_reg_device_type;
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
                    p_reg_Username, p_reg_Password, p_reg_user, p_reg_user_adminToken,
                    p_reg_device, p_reg_device_type, p_reg_vehicle, p_reg_vehicle_type);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                //    registerErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);

                    if(Integer.parseInt(res) == 1){
                        pDialog.setTitle("Getting Data");
                        pDialog.setMessage("Loading Info");

//                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        Toast.makeText(getBaseContext(), json_user.getString("username")+", Successfully Registered", Toast.LENGTH_LONG).show();

                        /**
                         * Removes all the previous data in the SQlite database
                         **/
//                        UserFunctions logout = new UserFunctions();
//                        logout.logoutUser(getApplicationContext());
//                        db.addUser(
//                                json_user.getString("username")
//                        );

                        pDialog.dismiss();

                        Intent login = new Intent(getApplicationContext(), Activity1_Login.class);
                        startActivity(login);
                        finish();
                    }

                    else if (Integer.parseInt(red) ==2 || Integer.parseInt(red) ==4){
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Username already exists", Toast.LENGTH_SHORT).show();

                    }
                    else if (Integer.parseInt(red) ==3){
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Invalid Email id", Toast.LENGTH_SHORT).show();
                    }

                }   else{
                    pDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Error occured in registration here", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Exception = "+e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void NetAsync(View view){
        new NetCheck().execute();
    }
}




