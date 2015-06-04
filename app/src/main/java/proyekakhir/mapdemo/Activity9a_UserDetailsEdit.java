package proyekakhir.mapdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import proyekakhir.mapdemo.library.DatabaseHandler;
import proyekakhir.mapdemo.library.UserFunctions;


public class Activity9a_UserDetailsEdit extends AppCompatActivity {
    EditText
            edit_FirstName,
            edit_LastName,
            edit_Email,
            edit_Username,
            edit_Password,
            edit_Password2,
//            edit_user_adminToken,
//            edit_device_otherName,
//            edit_device_type,
            edit_vehicle_otherName,
            edit_vehicle_type;

    EditText edit_OldPassword;

    RadioButton
    //        edit_user_standard,
    //        edit_user_admin,
    //        edit_device_asus,
    //        edit_device_lenovo,
    //        edit_device_samsung,
    //        edit_device_sony,
    //        edit_device_other,
            edit_vehicle_honda,
            edit_vehicle_yamaha,
            edit_vehicle_suzuki,
            edit_vehicle_kawasaki,
            edit_vehicle_other;

//    RadioGroup edit_radioUser;
    Button edit_submit;

    String v_edit_FirstName, v_edit_LastName,
            v_edit_Email, v_edit_Username,
            v_edit_Password, v_edit_Password2,
    //        v_edit_device_otherName, v_edit_device_type, v_edit_device,
            v_edit_vehicle_otherName,
            v_edit_vehicle_type,
            v_edit_user_adminToken, v_edit_vehicle, v_edit_OldPassword,
            userID;

    int v_edit_user;
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity9a_userdetailsedit);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5D4037")));

        edit_submit = (Button) findViewById(R.id.edit_submit);

        edit_FirstName = (EditText) findViewById(R.id.edit_FirstName);
        edit_LastName = (EditText) findViewById(R.id.edit_LastName);
        edit_Email = (EditText) findViewById(R.id.edit_Email);
        edit_Username = (EditText) findViewById(R.id.edit_Username);
        edit_Password = (EditText) findViewById(R.id.edit_Password);
        edit_Password2 = (EditText) findViewById(R.id.edit_Password2);
    //    edit_user_adminToken = (EditText) findViewById(R.id.edit_user_adminToken);
    //    edit_device_otherName = (EditText) findViewById(R.id.edit_device_otherName);
    //    edit_device_type = (EditText) findViewById(R.id.edit_device_type);
        edit_vehicle_otherName = (EditText) findViewById(R.id.edit_vehicle_otherName);
        edit_vehicle_type = (EditText) findViewById(R.id.edit_vehicle_type);

        edit_OldPassword = (EditText) findViewById(R.id.edit_OldPassword);

    //    edit_radioUser = (RadioGroup)findViewById(R.id.edit_radioUser);
    //    edit_user_standard = (RadioButton) findViewById(R.id.edit_user_standard);
    //    edit_user_admin = (RadioButton) findViewById(R.id.edit_user_admin);;
    //    edit_device_asus = (RadioButton) findViewById(R.id.edit_device_asus);;
    //    edit_device_lenovo = (RadioButton) findViewById(R.id.edit_device_lenovo);;
    //    edit_device_samsung = (RadioButton) findViewById(R.id.edit_device_samsung);;
    //    edit_device_sony = (RadioButton) findViewById(R.id.edit_device_sony);;
    //    edit_device_other = (RadioButton) findViewById(R.id.edit_device_other);;
        edit_vehicle_honda = (RadioButton) findViewById(R.id.edit_vehicle_honda);;
        edit_vehicle_yamaha = (RadioButton) findViewById(R.id.edit_vehicle_yamaha);;
        edit_vehicle_suzuki = (RadioButton) findViewById(R.id.edit_vehicle_suzuki);;
        edit_vehicle_kawasaki = (RadioButton) findViewById(R.id.edit_vehicle_kawasaki);;
        edit_vehicle_other = (RadioButton) findViewById(R.id.edit_vehicle_other);;

//        radioGroupDevice();
        radioGroupVehicle();

        try {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            User user = db.getUser();
//            HashMap<String, String> user = db.getUserDetails();

            userID = user.getUser_id();
            edit_Username.setText(user.getUsername());
            //(user.getUser_id());
            edit_FirstName.setText(user.getNama_awal());
            edit_LastName.setText(user.getNama_belakang());

            /*
            //Show Jenis User
            if(user.getJenis_user().equals("1")) {
                edit_user_admin.setChecked(false);
                edit_user_standard.setChecked(true);
            }
            else if(user.getJenis_user().equals("2")) {
                edit_user_admin.setChecked(true);
                edit_user_standard.setChecked(false);
            }
            */
            /*
            //Show Merk Smartphone
            if(user.getMerk_smartphone().equalsIgnoreCase("Asus")) {
                edit_device_asus.setChecked(true);
                edit_device_lenovo.setChecked(false);
                edit_device_samsung.setChecked(false);
                edit_device_sony.setChecked(false);
                edit_device_other.setChecked(false);
            }
            else if(user.getMerk_smartphone().equalsIgnoreCase("Lenovo")) {
                edit_device_asus.setChecked(false);
                edit_device_lenovo.setChecked(true);
                edit_device_samsung.setChecked(false);
                edit_device_sony.setChecked(false);
                edit_device_other.setChecked(false);
            }
            else if(user.getMerk_smartphone().equalsIgnoreCase("Samsung")) {
                edit_device_asus.setChecked(false);
                edit_device_lenovo.setChecked(false);
                edit_device_samsung.setChecked(true);
                edit_device_sony.setChecked(false);
                edit_device_other.setChecked(false);
            }
            else if(user.getMerk_smartphone().equalsIgnoreCase("Sony")) {
                edit_device_asus.setChecked(false);
                edit_device_lenovo.setChecked(false);
                edit_device_samsung.setChecked(false);
                edit_device_sony.setChecked(true);
                edit_device_other.setChecked(false);
            }
            else {
                edit_device_asus.setChecked(false);
                edit_device_lenovo.setChecked(false);
                edit_device_samsung.setChecked(false);
                edit_device_sony.setChecked(false);
                edit_device_other.setChecked(true);
                edit_device_otherName.setText(user.getMerk_smartphone());
            }
            */

            //Get tipe / seri smartphone
//            edit_device_type.setText(user.getTipe_smartphone());

            //Get merk motor
            if(user.getMerk_motor().equalsIgnoreCase("Honda")) {
                edit_vehicle_honda.setChecked(true);
                edit_vehicle_yamaha.setChecked(false);
                edit_vehicle_suzuki.setChecked(false);
                edit_vehicle_kawasaki.setChecked(false);
                edit_vehicle_other.setChecked(false);
            }
            else if(user.getMerk_motor().equalsIgnoreCase("Yamaha")) {
                edit_vehicle_honda.setChecked(false);
                edit_vehicle_yamaha.setChecked(true);
                edit_vehicle_suzuki.setChecked(false);
                edit_vehicle_kawasaki.setChecked(false);
                edit_vehicle_other.setChecked(false);
            }
            else if(user.getMerk_motor().equalsIgnoreCase("Suzuki")) {
                edit_vehicle_honda.setChecked(false);
                edit_vehicle_yamaha.setChecked(false);
                edit_vehicle_suzuki.setChecked(true);
                edit_vehicle_kawasaki.setChecked(false);
                edit_vehicle_other.setChecked(false);
            }
            else if(user.getMerk_motor().equalsIgnoreCase("Suzuki")) {
                edit_vehicle_honda.setChecked(false);
                edit_vehicle_yamaha.setChecked(false);
                edit_vehicle_suzuki.setChecked(false);
                edit_vehicle_kawasaki.setChecked(true);
                edit_vehicle_other.setChecked(false);
            }
            else if(user.getMerk_motor().equalsIgnoreCase("Kawasaki")) {
                edit_vehicle_honda.setChecked(false);
                edit_vehicle_yamaha.setChecked(false);
                edit_vehicle_suzuki.setChecked(false);
                edit_vehicle_kawasaki.setChecked(true);
                edit_vehicle_other.setChecked(false);
            }
            else {
                edit_vehicle_honda.setChecked(false);
                edit_vehicle_yamaha.setChecked(false);
                edit_vehicle_suzuki.setChecked(false);
                edit_vehicle_kawasaki.setChecked(false);
                edit_vehicle_other.setChecked(true);
                edit_vehicle_otherName.setText(user.getMerk_motor());
            }

            //Get Type / Seri Motor
            edit_vehicle_type.setText(user.getTipe_motor());

            //Get Email
            edit_Email.setText(user.getEmail());
        }
        catch(Exception ex)
        {
            Toast.makeText(getBaseContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

        edit_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean allow = valueCheck();
                if(allow == true) {
                    Toast.makeText(getBaseContext(), "Right Value", Toast.LENGTH_SHORT).show();
                    getValue();
//                    NetAsync(v);
                    new NetCheck().execute();
                }
                else
                    Toast.makeText(getBaseContext(), "Please check again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getValue(){
        v_edit_FirstName = edit_FirstName.getText().toString();
        v_edit_LastName = edit_LastName.getText().toString();
    //    v_edit_Email = edit_Email.getText().toString();
        v_edit_Username = edit_Username.getText().toString();
        v_edit_OldPassword = edit_OldPassword.getText().toString();
        v_edit_Password = edit_Password.getText().toString();
        v_edit_Password2 = edit_Password2.getText().toString();
//        v_edit_device_otherName = edit_device_otherName.getText().toString();
//        v_edit_device_type = edit_device_type.getText().toString();
        v_edit_vehicle_otherName = edit_vehicle_otherName.getText().toString();
        v_edit_vehicle_type = edit_vehicle_type.getText().toString();

        /*
        if(edit_user_standard.isChecked()) {
            v_edit_user = 1;
            v_edit_user_adminToken = "";
        }
        else if(edit_user_admin.isChecked()) {
            v_edit_user = 2;
            v_edit_user_adminToken = edit_user_adminToken.getText().toString();
        }
        */

        /*
        if(edit_device_asus.isChecked())
            v_edit_device = "asus";
        else if(edit_device_lenovo.isChecked())
            v_edit_device = "lenovo";
        else if(edit_device_samsung.isChecked())
            v_edit_device = "samsung";
        else if(edit_device_sony.isChecked())
            v_edit_device = "sony";
        else if(edit_device_other.isChecked())
            v_edit_device = v_edit_device_otherName.toLowerCase();
        */

        if(edit_vehicle_honda.isChecked())
            v_edit_vehicle = "honda";
        else if(edit_vehicle_yamaha.isChecked())
            v_edit_vehicle = "yamaha";
        else if(edit_vehicle_suzuki.isChecked())
            v_edit_vehicle = "suzuki";
        else if(edit_vehicle_kawasaki.isChecked())
            v_edit_vehicle = "suzuki";
        else if(edit_vehicle_other.isChecked())
            v_edit_vehicle = v_edit_vehicle_otherName.toLowerCase();

    }

    public boolean valueCheck(){
        boolean ret = false;
        /*
        if(
                edit_user_standard.isChecked() == false &&
                        edit_user_admin.isChecked() == false
                ) {
            ret = false;
            //    Toast.makeText(getBaseContext(), "Catch 1", Toast.LENGTH_SHORT).show();
        }
        */
        if(
                edit_FirstName.getText().toString().equals("") ||
                edit_LastName.getText().toString().equals("") ||
        //        edit_Email.getText().toString().equals("") ||
        //        edit_Username.getText().toString().equals("") ||
                edit_OldPassword.getText().toString().equals("") ||
                edit_Password.getText().toString().equals("") ||
                edit_Password2.getText().toString().equals("") ||
        //        edit_device_type.getText().toString().equals("") ||
                edit_vehicle_type.getText().toString().equals("")
                ) {
            ret = false;
            //    Toast.makeText(getBaseContext(), "Catch 2", Toast.LENGTH_SHORT).show();
        }
        /*
        else if(
                edit_device_asus.isChecked() == false &&
                edit_device_lenovo.isChecked() == false &&
                edit_device_samsung.isChecked() == false &&
                edit_device_sony.isChecked() == false &&
                edit_device_other.isChecked() == false
                ) {
            ret = false;
            //    Toast.makeText(getBaseContext(), "Catch 3", Toast.LENGTH_SHORT).show();
        }
        */
        else if(
                edit_vehicle_honda.isChecked() == false &&
                        edit_vehicle_yamaha.isChecked() == false &&
                        edit_vehicle_suzuki.isChecked() == false &&
                        edit_vehicle_kawasaki.isChecked() == false &&
                        edit_vehicle_other.isChecked() == false
                ) {
            ret = false;
            //    Toast.makeText(getBaseContext(), "Catch 4", Toast.LENGTH_SHORT).show();
        }
        /*
        else if(
                edit_device_other.isChecked() == true && edit_device_otherName.getText().toString().equals("")
                ) {
            ret = false;
            //    Toast.makeText(getBaseContext(), "Catch 5", Toast.LENGTH_SHORT).show();
        }
        */
        else if(
                edit_vehicle_other.isChecked() == true && edit_vehicle_otherName.getText().toString().equals("")
                ) {
            ret = false;
            //    Toast.makeText(getBaseContext(), "Catch 6", Toast.LENGTH_SHORT).show();
        }
        /*
        else if(
                edit_user_admin.isChecked() == true && edit_user_adminToken.getText().toString().equals("")
                ) {
            ret = false;
            //    Toast.makeText(getBaseContext(), "Catch 7", Toast.LENGTH_SHORT).show();
        }
        */
        else if(
                !(edit_Password.getText().toString().equals(edit_Password2.getText().toString()))
                ) {
            ret = false;
            Toast.makeText(getBaseContext(), "Password didn't match!", Toast.LENGTH_SHORT).show();
        }

        else ret = true;

        return ret;
    }

    /*
    public void radioGroupDevice(){
        edit_device_asus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edit_device_asus.setChecked(true);
                edit_device_lenovo.setChecked(false);
                edit_device_samsung.setChecked(false);
                edit_device_sony.setChecked(false);
                edit_device_other.setChecked(false);
            }
        });

        edit_device_lenovo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edit_device_asus.setChecked(false);
                edit_device_lenovo.setChecked(true);
                edit_device_samsung.setChecked(false);
                edit_device_sony.setChecked(false);
                edit_device_other.setChecked(false);
            }
        });

        edit_device_samsung.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edit_device_asus.setChecked(false);
                edit_device_lenovo.setChecked(false);
                edit_device_samsung.setChecked(true);
                edit_device_sony.setChecked(false);
                edit_device_other.setChecked(false);
            }
        });

        edit_device_sony.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edit_device_asus.setChecked(false);
                edit_device_lenovo.setChecked(false);
                edit_device_samsung.setChecked(false);
                edit_device_sony.setChecked(true);
                edit_device_other.setChecked(false);
            }
        });

        edit_device_other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edit_device_asus.setChecked(false);
                edit_device_lenovo.setChecked(false);
                edit_device_samsung.setChecked(false);
                edit_device_sony.setChecked(false);
                edit_device_other.setChecked(true);
            }
        });
    }
    */

    public void radioGroupVehicle(){
        edit_vehicle_honda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edit_vehicle_honda.setChecked(true);
                edit_vehicle_yamaha.setChecked(false);
                edit_vehicle_suzuki.setChecked(false);
                edit_vehicle_kawasaki.setChecked(false);
                edit_vehicle_other.setChecked(false);
            }
        });

        edit_vehicle_yamaha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edit_vehicle_honda.setChecked(false);
                edit_vehicle_yamaha.setChecked(true);
                edit_vehicle_suzuki.setChecked(false);
                edit_vehicle_kawasaki.setChecked(false);
                edit_vehicle_other.setChecked(false);
            }
        });

        edit_vehicle_suzuki.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edit_vehicle_honda.setChecked(false);
                edit_vehicle_yamaha.setChecked(false);
                edit_vehicle_suzuki.setChecked(true);
                edit_vehicle_kawasaki.setChecked(false);
                edit_vehicle_other.setChecked(false);
            }
        });

        edit_vehicle_other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edit_vehicle_honda.setChecked(false);
                edit_vehicle_yamaha.setChecked(false);
                edit_vehicle_suzuki.setChecked(false);
                edit_vehicle_kawasaki.setChecked(false);
                edit_vehicle_other.setChecked(true);
            }
        });

        edit_vehicle_kawasaki.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edit_vehicle_honda.setChecked(false);
                edit_vehicle_yamaha.setChecked(false);
                edit_vehicle_suzuki.setChecked(false);
                edit_vehicle_kawasaki.setChecked(true);
                edit_vehicle_other.setChecked(false);
            }
        });
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
            nDialog = new ProgressDialog(Activity9a_UserDetailsEdit.this);
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
                p_reg_user_adminToken, p_reg_vehicle, p_reg_OldPassword;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            p_reg_FirstName = v_edit_FirstName;
            p_reg_LastName = v_edit_LastName;
        //    p_reg_Email = v_edit_Email;
            p_reg_Username = v_edit_Username;
            p_reg_OldPassword = v_edit_OldPassword;
            p_reg_Password = v_edit_Password;
//            p_reg_user = Integer.toString(v_edit_user);
//            p_reg_user_adminToken = v_edit_user_adminToken;
            p_reg_device = android.os.Build.MANUFACTURER;
            p_reg_device_type = android.os.Build.MODEL;
            p_reg_vehicle = v_edit_vehicle;
            p_reg_vehicle_type = v_edit_vehicle_type;

            pDialog = new ProgressDialog(Activity9a_UserDetailsEdit.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Updating data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.updateUser(userID, p_reg_OldPassword, p_reg_FirstName,
                    p_reg_LastName, p_reg_Username, p_reg_Password,
            //        p_reg_user, p_reg_user_adminToken,
                    p_reg_device, p_reg_device_type,
                    p_reg_vehicle, p_reg_vehicle_type);
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

                        Toast.makeText(getBaseContext(), json.getString("success_msg")+" jPlease re-login!", Toast.LENGTH_LONG).show();

                        /**
                         * Removes all the previous data in the SQlite database
                         **/
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        db.resetTables();

                        pDialog.dismiss();

                        Intent login = new Intent(getApplicationContext(), Activity1_Login.class);
                        startActivity(login);
                        finish();
                    }

                    else if (Integer.parseInt(red) == 1){
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Incorrect Password!", Toast.LENGTH_SHORT).show();
                    }

                }   else{
                    pDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Error occured."+json.getString("error_msg"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Exception = "+e.toString(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity9a__user_details_edit, menu);
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
