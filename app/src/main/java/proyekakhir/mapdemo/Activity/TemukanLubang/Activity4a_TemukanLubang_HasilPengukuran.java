package proyekakhir.mapdemo.Activity.TemukanLubang;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import proyekakhir.mapdemo.Activity.Activity2_MainMap;
import proyekakhir.mapdemo.R;
import proyekakhir.mapdemo.NonActivity.User;
import proyekakhir.mapdemo.NonActivity.DatabaseHandler;
import proyekakhir.mapdemo.NonActivity.UserFunctions;


public class Activity4a_TemukanLubang_HasilPengukuran extends AppCompatActivity {

    String data = null;
    private String SERVER_ADDRESS = "http://surveyorider.zz.mu/SurveyoRiderServices/";
    private String ID_USER = "";
    boolean sent = false;

    public Locale INDONESIA = new Locale("id_ID");

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
    String[] street_preview, city_preview, kec_preview, prov_preview;

    String temp_parT,temp_parX,temp_parY,temp_parZ,temp_parLat,temp_parLong,temp_parV;
    String timeEnd, tempTimeEnd;

    Button sendToServer, preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity4a__pothole_result);

        try{
            android.support.v7.app.ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#455A64")));
        } catch (NullPointerException ex){
        //    Log.e("Null", ex.getMessage());
        }

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
        db.close();

        //    Toast.makeText(getBaseContext(), "Jumlah data = "+min, Toast.LENGTH_LONG).show();

        sendToServer = (Button)findViewById(R.id.act3a_bt_sendToServer);
        sendToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(!sent) {
                    completingData();
                }
                else
                    Toast.makeText(getBaseContext(), "Data telah terkirim!", Toast.LENGTH_SHORT).show();
            }

        });

        preview = (Button)findViewById(R.id.act3a_bt_preview);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(Activity4a_TemukanLubang_HasilPengukuran.this, Activity4b_TemukanLubang_HasilPengukuran_Preview.class);
                intent.putExtra("qual-prev", qual_preview);
                intent.putExtra("lat-prev", lat_preview);
                intent.putExtra("lon-prev", lon_preview);
                startActivity(intent);




            }

        });

    }

    public String createJsonArray(int[] qual_preview, double[] lat_preview,
                                  double[] lon_preview, String[] street_preview,
                                  String[] city_preview, String[] kec_preview, String[] prov_preview,
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
                obj.put("prov", prov_preview[i]);
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
        prov_preview = new String[qual_preview.length];
        new NetCheck().execute();

        for(int i=0; i<street_preview.length; i++){
        //    Log.v("Point-" + i, street_preview[i] + "," + kec_preview[i] + "," + city_preview[i] + "," + prov_preview[i]);
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

        //    min = Math.min(tT.size(), Math.min(xX.size(), Math.min(yY.size(), Math.min(zZ.size(), Math.min(lati.size(), Math.min(longi.size(), v.size()))))));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity4a__pothole_result, menu);
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
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent = new Intent(Activity4a_TemukanLubang_HasilPengukuran.this, Activity2_MainMap.class);
        startActivity(intent);
        finish();
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
            nDialog = new ProgressDialog(Activity4a_TemukanLubang_HasilPengukuran.this);
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

            pDialog = new ProgressDialog(Activity4a_TemukanLubang_HasilPengukuran.this);
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
                    Geocoder gcd = new Geocoder(getApplicationContext(), INDONESIA);
                    List<Address> addresses = gcd.getFromLocation(lat_preview[i], lon_preview[i], 1);
                    if (addresses.size() > 0) {
                    //    Log.v("Alamat ke-"+i,
                    //            addresses.get(0).getThoroughfare()+
                    //                    ","+addresses.get(0).getLocality()+
                    //                    ","+addresses.get(0).getSubAdminArea()+
                    //                    ","+addresses.get(0).getAdminArea());
                        street_preview[i] = addresses.get(0).getThoroughfare();
                        city_preview[i] = addresses.get(0).getSubAdminArea();
                        kec_preview[i] = addresses.get(0).getLocality();
                        prov_preview[i] = addresses.get(0).getAdminArea();
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
            if(th){
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

            pDialog = new ProgressDialog(Activity4a_TemukanLubang_HasilPengukuran.this);
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
                        city_preview, kec_preview, prov_preview, qual_preview.length);
            //    Log.v("JSON Data", data.toString());
                return true;
            }catch (Exception e)
            {
            //    Log.e("Error", e.getMessage());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            if(th){
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
    private class SendData extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String p_username, p_password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Activity4a_TemukanLubang_HasilPengukuran.this);
            pDialog.setTitle("Mengirim Data");
            pDialog.setMessage("Tunggu Sebentar ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.sendDataLubang(ID_USER, data.toString());
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString("success") != null) {
            //        Log.v("Js", json.getString("message"));
                }
                pDialog.dismiss();
                sent = true;

                // Jika terkirim, maka buat notifikasi
            } catch (JSONException e) {
                e.printStackTrace();
                pDialog.dismiss();
            }
        }
    }
}
