package proyekakhir.mapdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import proyekakhir.mapdemo.library.UserFunctions;


public class EmailVerification extends ActionBarActivity {
    EditText verificationCode, newEmail;
    Button bt_verify, bt_reSendVerification, bt_changeEmail;
    String email, activity, username, newEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        initializeComponent();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        activity = intent.getStringExtra("activity");
        username = intent.getStringExtra("username");
    //    Toast.makeText(getBaseContext(), "Email : "+email, Toast.LENGTH_LONG).show();

        bt_changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(newEmail.equals(null)){
                    Toast.makeText(getBaseContext(), "Please Type Your New Email!", Toast.LENGTH_LONG).show();
                }
                else{
                    newEmailAddress = newEmail.getText().toString();
                    new ChangeEmail().execute();
                }

            }
        });

        bt_reSendVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new EmailVer().execute();
            }
        });

        bt_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = getIntent();
                String token = intent.getStringExtra("token");

                String txt = verificationCode.getText().toString();
                if(txt.equals(token)) {
                    Toast.makeText(getBaseContext(), "Token Accepted!", Toast.LENGTH_LONG).show();
                    new NetCheck().execute();
                }
                else
                    Toast.makeText(getBaseContext(), "Token Incorrect!", Toast.LENGTH_LONG).show();
            }
        });
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

    public void initializeComponent(){
        verificationCode = (EditText) findViewById(R.id.verificationCode);
        newEmail = (EditText) findViewById(R.id.newEmail);
        bt_verify = (Button) findViewById(R.id.bt_verify);
        bt_reSendVerification = (Button) findViewById(R.id.bt_reSendVerification);
        bt_changeEmail = (Button) findViewById(R.id.bt_changeEmail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_email_verification, menu);
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
    public void onBackPressed()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent i = new Intent(EmailVerification.this, Activity1_Login.class);
                        startActivity(i);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Registration");
        builder.setMessage("Are you sure want to cancel registratio proccess?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
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
            nDialog = new ProgressDialog(EmailVerification.this);
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
                new verifyUser().execute();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(getBaseContext(), "Error in Network Connection", Toast.LENGTH_SHORT).show();
                //    registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class verifyUser extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(EmailVerification.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Verifying ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.verifyUser(email);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString("success") != null) {
                    Toast.makeText(getBaseContext(), "Verification Success. Please Login.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent (EmailVerification.this, Activity1_Login.class);
                    startActivity(i);
                    finish();
                }else{
                    pDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Failed to Verify User!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

            pDialog = new ProgressDialog(EmailVerification.this);
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
            HttpPost httppost = new HttpPost("http://muhlish.com/ta/mail.php");
            HttpResponse response = null;
            String str = "";

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("tag", "emailVerification"));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("token", generateToken(username)));
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
                Toast.makeText(getBaseContext(), "Verification Sent. Please Check Your Email!", Toast.LENGTH_LONG).show();
            }

            pDialog.dismiss();
        }
    }

    private class ChangeEmail extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(EmailVerification.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Updating Your Email Address ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.updateEmail(email, newEmailAddress);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                if (json.getString("success") != null) {
                    email = newEmailAddress;
                //    Toast.makeText(getBaseContext(), "Your Email successfully changed. Please check your new Email to get your verification code.", Toast.LENGTH_SHORT).show();
                    new EmailVer().execute();
                }else{
                    Toast.makeText(getBaseContext(), "Failed to Verify User!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

}
