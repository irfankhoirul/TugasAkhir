package proyekakhir.mapdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import proyekakhir.mapdemo.library.DatabaseHandler;
import proyekakhir.mapdemo.library.UserFunctions;


public class Activity3a_App1GoResult extends ActionBarActivity {

    String data = null;
    private String SERVER_ADDRESS = "http://surveyorider.zz.mu/SurveyoRiderServices/";
    private String ID_USER = "";

    private static String url_create_product = "http://muhlish.com/ta/webservice/create_product.php";    //internet
//    private static String url_create_product = "http://192.168.0.105:81/mysqlphpandroidcrud/create_product.php"; //local

    private static final String TAG_SUCCESS = "success";

//    List<Double> xX = new ArrayList<>(); //X-axis
    List<Double> yY = new ArrayList<>(); //Y-axis
//    List<Double> zZ = new ArrayList<>(); //z-axis
    List<Double> lati = new ArrayList<>(); //Latitude
    List<Double> longi = new ArrayList<>(); //Longitude
//    List<Double> v = new ArrayList<>(); //Speed
//    List<Integer> tT = new ArrayList<>(); //waktu

    int min;

    double[] y, y_preview;
    int xS,yS,zS, latS, longS, speedS, tS;
    double[] latitude, longitude;

    //send to server
    int[] qual_preview;
    double[] lat_preview, lon_preview;
    String[] street_preview, city_preview, kec_preview;

    String temp_parT,temp_parX,temp_parY,temp_parZ,temp_parLat,temp_parLong,temp_parV;
    String timeEnd, tempTimeEnd;

    Button sendToServer, preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3a_app1goresult);

        //get date for pengukuran table
        Calendar c = Calendar.getInstance();
        String jam = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        String menit = Integer.toString(c.get(Calendar.MINUTE));
    //    Toast.makeText(getBaseContext(), jam+":"+menit, Toast.LENGTH_LONG).show();
        timeEnd = jam+":"+menit;

        getAllIntentData();

        //Get Username
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        User user = db.getUser();
        ID_USER = user.getUser_id();

    //    Toast.makeText(getBaseContext(), "Jumlah data = "+min, Toast.LENGTH_LONG).show();

        sendToServer = (Button)findViewById(R.id.act3a_bt_sendToServer);
        sendToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                completingData();


        //        new saveData().execute();
                Toast.makeText(getBaseContext(), "Completing data...", Toast.LENGTH_SHORT).show();
            }

        });

        preview = (Button)findViewById(R.id.act3a_bt_preview);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(Activity3a_App1GoResult.this, Activity3b_App1Preview.class);
                intent.putExtra("qual-prev", qual_preview);
                intent.putExtra("lat-prev", lat_preview);
                intent.putExtra("lon-prev", lon_preview);
                startActivity(intent);


                /*
                try {
                    JSONParser jp = new JSONParser();
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("tag", "insert"));
                    params.add(new BasicNameValuePair("idUser", ID_USER));
                    params.add(new BasicNameValuePair("json", data.toString()));

                    JSONObject json = jp.getJSONFromUrl(SERVER_ADDRESS, params);
                //    Log.v("Hasil Input", json.toString());
                } catch(Exception ex){
                    Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                */

            }

        });

    }

    public String createJsonArray(int[] qual_preview, double[] lat_preview,
                                      double[] lon_preview, String[] street_preview,
                                      String[] city_preview, String[] kec_preview,
                                      int numberOfData) throws JSONException {
        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < numberOfData; i++) {
            obj = new JSONObject();
            try {
                obj.put("idx", i);
                obj.put("lat", lat_preview[i]);
                obj.put("lon", lon_preview[i]);
                obj.put("nama", street_preview[i]);
                obj.put("kec", kec_preview[i]);
                obj.put("kota", city_preview[i]);
                obj.put("kualitas", qual_preview[i]);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray.put(obj);
        }

        JSONObject finalobject = new JSONObject();
        finalobject.put("data", jsonArray);
    //    String ret = "'"+jsonArray.toString()+"'";
        return jsonArray.toString();
    }

    public void completingData(){
        street_preview = new String[qual_preview.length];
        city_preview = new String[qual_preview.length];
        kec_preview = new String[qual_preview.length];
        new NetCheck().execute();

        for(int i=0; i<street_preview.length; i++){
            Log.v("Point-"+i,street_preview[i]+","+kec_preview[i]+","+city_preview[i]);
        }
    }

    public void getAllIntentData()
    {
        Intent intent = getIntent();
        lat_preview = intent.getDoubleArrayExtra("lat-prev");
        lon_preview = intent.getDoubleArrayExtra("lon-prev");
        qual_preview = intent.getIntArrayExtra("qual-prev");
        Toast.makeText(getApplicationContext(), "Lat:"+lat_preview.length+
                " Lon:"+lon_preview.length+" Qual:"+qual_preview.length, Toast.LENGTH_SHORT).show();

        /*
        tS = intent.getIntExtra("time-length",0);
        t = new int[tS];
        t = intent.getIntArrayExtra("time");
        for(int i=0;i<t.length;i++)
        {
            tT.add(t[i]);
        }
        */

        /*
        xS = intent.getIntExtra("x-axis-length",0);
        x = new double[xS];
        x = intent.getDoubleArrayExtra("x-axis");
        for(int i=0;i<x.length;i++)
        {
            xX.add(x[i]);
        }
        */

        /*
        yS = intent.getIntExtra("y-axis-length", 0);
        y = new double[yS];
        y = intent.getDoubleArrayExtra("y-axis");
        y_preview = intent.getDoubleArrayExtra("y-axis");
        for(int i=0;i<y.length;i++)
        {
            yY.add(y[i]);
        }
        */

        /*
        zS = intent.getIntExtra("z-axis-length",0);
        z = new double[zS];
        z = intent.getDoubleArrayExtra("z-axis");
        for(int i=0;i<z.length;i++)
        {
            zZ.add(z[i]);
        }
        */

        /*
        latS = intent.getIntExtra("latitude-length",0);
        latitude = new double[latS];
        latitude = intent.getDoubleArrayExtra("latitude");

        for(int i=0;i<latitude.length;i++)
        {
            lati.add(latitude[i]);
        }

        longS = intent.getIntExtra("longitude-length",0);
        longitude = new double[longS];
        longitude = intent.getDoubleArrayExtra("longitude");

        for(int i=0;i<longitude.length;i++)
        {
            longi.add(longitude[i]);
        }
        */

        /*
        speedS = intent.getIntExtra("speed-length",0);
        speed = new double[speedS];
        speed = intent.getDoubleArrayExtra("speed");
        for(int i=0;i<speed.length;i++)
        {
            v.add(speed[i]);
        }
        */

    //    min = Math.min(tT.size(), Math.min(xX.size(), Math.min(yY.size(), Math.min(zZ.size(), Math.min(lati.size(), Math.min(longi.size(), v.size()))))));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu___test_ride_app1_result, menu);
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

/*
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent = new Intent(Activity3a_App1GoResult.this, Activity2_MainMap.class);
        startActivity(intent);
        finish();
    }
*/

    /**
     * Async Task to check whether internet connection is working.
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Activity3a_App1GoResult.this);
            nDialog.setTitle("Completing Data");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(false);
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
                new CompletingData().execute();
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
    private class CompletingData extends AsyncTask<String, String, Boolean> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Activity3a_App1GoResult.this);
            pDialog.setTitle("Please Wait");
            pDialog.setMessage("Completing your data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            try {
                //Completing Data
                for(int i=0; i<qual_preview.length; i++) {
                    Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = gcd.getFromLocation(lat_preview[i], lon_preview[i], 1);
                    if (addresses.size() > 0) {
                        Log.v("Alamat ke-"+i,
                                addresses.get(0).getThoroughfare()+
                                ","+addresses.get(0).getLocality()+
                                ","+addresses.get(0).getSubAdminArea());
                        street_preview[i] = addresses.get(0).getThoroughfare();
                        city_preview[i] = addresses.get(0).getSubAdminArea();
                        kec_preview[i] = addresses.get(0).getLocality();
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            if(th == true){
                pDialog.dismiss();
                new CreateJSON().execute();
            }
            else{
                pDialog.dismiss();
                Toast.makeText(getBaseContext(), "Error in Completing Proccess!", Toast.LENGTH_SHORT).show();
            }
            
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class CreateJSON extends AsyncTask<String, String, Boolean> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Activity3a_App1GoResult.this);
            pDialog.setTitle("Please Wait");
            pDialog.setMessage("Creating JSON data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            try{
                //Creating JSON Array of data
                data = createJsonArray(qual_preview, lat_preview, lon_preview, street_preview,
                        city_preview, kec_preview, qual_preview.length);
                Log.v("JSON Data", data.toString());
                return true;
            }catch (Exception e)
            {
                Log.e("Error", e.getMessage());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            if(th == true){
                pDialog.dismiss();
                new SendData().execute();
            }
            else{
                pDialog.dismiss();
                Toast.makeText(getBaseContext(), "Error in Creating JSON!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class Send2Server extends AsyncTask<String, String, Boolean> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Activity3a_App1GoResult.this);
            pDialog.setTitle("Please Wait");
            pDialog.setMessage("Completing your data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {
            try{

                //Sending to server
                pDialog.setMessage("Sending data ...");

                HttpClient client = new DefaultHttpClient();
                HttpResponse response;
                HttpPost post = new HttpPost(SERVER_ADDRESS);

                List<NameValuePair> nVP = new ArrayList<NameValuePair>();
                nVP.add(new BasicNameValuePair("tag", "insert"));
                nVP.add(new BasicNameValuePair("idUser", ID_USER));
                nVP.add(new BasicNameValuePair("json", data.toString()));
                post.setEntity(new UrlEncodedFormEntity(nVP));
                response = client.execute(post);
                if(response!=null){
                    Log.v("JSON back Input", response.toString());

                }


                /*
                JSONParser jp = new JSONParser();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "insert"));
                params.add(new BasicNameValuePair("idUser", ID_USER));
                params.add(new BasicNameValuePair("json", data.toString()));

                JSONObject json = jp.getJSONFromUrl(SERVER_ADDRESS, params);
                Log.v("Hasil Input",json.toString());
                */
                return true;

            }catch(Exception e)
            {
                Log.e("Error",e.getMessage());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            if(th == true){
                pDialog.dismiss();
            }
            else{
                pDialog.dismiss();
                Toast.makeText(getBaseContext(), "Error in Sending Data!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class SendData extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String p_username, p_password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Activity3a_App1GoResult.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.sendData(ID_USER, data.toString());
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString("success") != null) {
                    Log.v("Js", json.getString("message"));
                }
                pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                pDialog.dismiss();
            }
        }
    }


}
