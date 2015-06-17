package proyekakhir.mapdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activity3_App1Go extends AppCompatActivity implements SensorEventListener, LocationListener {

    SharedPreferences pref;

    boolean posisiOK = false;
    boolean koneksi = false;
    int  qual;
    boolean ready = false;
    LocationManager locMan;
    LatLng nowLoc, lastLoc;
    boolean isFirstLocation = true;
    boolean belumDimulai = true;
    int awal = 0, akhir = 1;
    private ProgressDialog pDialog, okDialog;
    double lat, lon;

    //----MAP----//
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Location FIRST_LOCATION;
    private double tempLat, tempLong;
    private double tempSpeed;

    //----ACCELEROMETER----//
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int count;
    private boolean timerstart = false;
    private boolean stopSave = false;
    private boolean ACCELEROMETER_START = false;
    private double tempX, tempY, tempZ;

    //--Penampung Data yang diambil--//
//    List<Double> x = new ArrayList<>();
    List<Double> y = new ArrayList<>();
//    List<Double> z = new ArrayList<>();
//    List<Double> speed = new ArrayList<>();

    //--Penampung Temporary--//
    List<Double> arr_latitude = new ArrayList<>();
    List<Double> arr_longitude = new ArrayList<>();
    List<Double> y_temp = new ArrayList<>();

    //List lokasi marker
    List<Double> marker_lat = new ArrayList<>();
    List<Double> marker_lon = new ArrayList<>();
    List<Integer> marker_quality = new ArrayList<>();

    //Hasil klasifikasi
    int kualitas = 0;

    //----ALL----//
    private TextView _act6_txt_detailLat, _act6_txt_detailLong, _act6_txt_detailSpeed,
            _act6_txt_detailDistance, _act6_txt_xaxis, _act6_txt_yaxis, _act6_txt_zaxis,
            _act6_txt_time;
    private ViewFlipper flipper;

    boolean a = false;
    ////////////////////////////////////
    //Timer
    private Handler handler = new Handler();
    boolean lanjut = true;

    boolean start = false;
    ProgressDialog pgDialog;
    int timer = 0;
    int temp_waktu, timer_counter;

    Vibrator v;

    //---------------------------------------------------------------------------------------------------
    //----ALL----//--------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity3_app1go);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF5722")));

        pgDialog = new ProgressDialog(Activity3_App1Go.this);
        okDialog = new ProgressDialog(Activity3_App1Go.this);

        initializeViews();
        v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        temp_waktu = pref.getInt("p_waktu", 0);
        timer = temp_waktu;
        timer_counter = timer;

        if(koneksi == false){
            new NetCheck().execute();
        }

        //----MAP----//
        setUpMapIfNeeded();

        //----ACCELEROMETER----??
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(getApplicationContext(), "Here we go!", Toast.LENGTH_SHORT).show();
            ACCELEROMETER_START = true;
        }
        else {
            // fail! we dont have an accelerometer!
            Toast.makeText(getApplicationContext(), "Oops!", Toast.LENGTH_SHORT).show();
        }

        locMan = (LocationManager)getSystemService(LOCATION_SERVICE);

    //    if(ready)
        //    mulai();


    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(start == true) {
                if(pgDialog.isShowing())
                    pgDialog.dismiss();
            //    if(true) {
                if(getMapData()>0) { //Checking dari spam (jika speed > 0 maka bukan spam)
                    getAxisValue();
                //    getMapData();
                    if (count % 10 == 0) { //1 detik per proses
                        qual = histogram();
                        resetVariable();
                        Integer[] myTaskParams = {qual};
                        new SnapToRoad().execute(myTaskParams);
                    }
                }
                _act6_txt_time.setText(Integer.toString(count));
                count++;
            }
            else if(posisiOK){
                if(timer_counter > 0){
                    if(!pgDialog.isShowing()) {
                        pgDialog.setIndeterminate(false);
                        pgDialog.setCancelable(false);
                        pgDialog.show();
                    }
                    pgDialog.setMessage(Integer.toString(timer_counter));
                }
                else{
                    start = true;
                }
            }
            if(lanjut && start == true)
                handler.postDelayed(this, 100);
            else if(lanjut && start == false) {
                handler.postDelayed(this, 1000);
                timer_counter--;
                if(timer_counter == 0)
                    v.vibrate(1000);
                else
                    v.vibrate(100);
            }
        }
    };

    public void resetVariable(){
        y_temp.clear();
    }



    public void showMarker(int quality, double lat, double lon){
        if(lat!=0.0 || lon!=0.0) {
            if (ready) {
                Log.v("Latitude inside", Double.toString(lat));
                Log.v("Longitude inside", Double.toString(lon));
                if (quality == 1) {
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .title("Jalan Baik")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_blue)));
                    marker_lat.add(lat);
                    marker_lon.add(lon);
                    marker_quality.add(1);
                }
                else if (quality == 2) {
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .title("Jalan Sedang")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange)));
                    marker_lat.add(lat);
                    marker_lon.add(lon);
                    marker_quality.add(2);
                } else if (quality == 3) {
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .title("Jalan Rusak")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_red)));
                    marker_lat.add(lat);
                    marker_lon.add(lon);
                    marker_quality.add(3);
                }
            }
        }
    }

    public int histogram(){
        // 3 Criteria
        int j0_1 = 0, j1_2 = 0, j2_3 = 0, j3_4 = 0, j4_5 = 0, j5_6 = 0, j6_7 = 0, j7_8 = 0,
                j8_9 = 0, j9_10 = 0, j10_11 = 0, j11_12 = 0, j12_13 = 0, j13_14 = 0, j14_15 = 0,
                j15_16 = 0, j16_17 = 0, j17_18 = 0, j18_19 = 0, j19_20 = 0;

        for(int i=0; i<y_temp.size(); i++) {
            double nilai_Y = Math.abs(y_temp.get(i)-9.781); //Kurangi G lalu absolutkan

            if(nilai_Y >=0 && nilai_Y <1){
                j0_1++;
            }
            else if(nilai_Y >=1 && nilai_Y <2){
                j1_2++;
            }
            else if(nilai_Y >=2 && nilai_Y <3){
                j2_3++;
            }
            else if(nilai_Y >=3 && nilai_Y <4){
                j3_4++;
            }
            else if(nilai_Y >=4 && nilai_Y <5){
                j4_5++;
            }
            else if(nilai_Y >=5 && nilai_Y <6){
                j5_6++;
            }
            else if(nilai_Y >=6 && nilai_Y <7){
                j6_7++;
            }
            else if(nilai_Y >=7 && nilai_Y <8){
                j7_8++;
            }
            else if(nilai_Y >=8 && nilai_Y <9){
                j8_9++;
            }
            else if(nilai_Y >=9 && nilai_Y <10){
                j9_10++;
            }
            else if(nilai_Y >=10 && nilai_Y <11){
                j10_11++;
            }
            else if(nilai_Y >=11 && nilai_Y <12){
                j11_12++;
            }
            else if(nilai_Y >=12 && nilai_Y <13){
                j12_13++;
            }
            else if(nilai_Y >=13 && nilai_Y <14){
                j13_14++;
            }
            else if(nilai_Y >=14 && nilai_Y <15){
                j14_15++;
            }
            else if(nilai_Y >=15 && nilai_Y <16){
                j15_16++;
            }
            else if(nilai_Y >=16 && nilai_Y <17){
                j16_17++;
            }
            else if(nilai_Y >=17 && nilai_Y <18){
                j17_18++;
            }
            else if(nilai_Y >=18 && nilai_Y <19){
                j18_19++;
            }
            else if(nilai_Y >=19 && nilai_Y <20){
                j19_20++;
            }
        }

        //Klasifikasi
        double A = (j0_1+j1_2);
        double B = (j1_2+j2_3+j3_4);
        double C = (j4_5+j5_6+j6_7+j7_8+j8_9+j9_10+j10_11+j11_12+j12_13+j13_14+j14_15+j15_16+j16_17+j17_18+j18_19+j19_20);


        if(A >= 0.9*10){
            kualitas = 1;
        }
        else {
            if(Math.max(B, C) == B){
                kualitas = 2;
            }
            else if(Math.max(B, C) == C){
                kualitas = 3;
            }
        }

        return kualitas;
    }

    public void initializeViews() {
        _act6_txt_detailLat = (TextView) findViewById(R.id._act6_txt_detailLat); //ok
        _act6_txt_detailLong = (TextView) findViewById(R.id._act6_txt_detailLong); //ok
        _act6_txt_detailSpeed = (TextView) findViewById(R.id._act6_txt_detailSpeed); //test
        _act6_txt_detailDistance = (TextView) findViewById(R.id._act6_txt_detailDistance); //
        _act6_txt_xaxis = (TextView) findViewById(R.id._act6_txt_xaxis); //test
        _act6_txt_yaxis = (TextView) findViewById(R.id._act6_txt_yaxis); //test
        _act6_txt_zaxis = (TextView) findViewById(R.id._act6_txt_zaxis); //test
        _act6_txt_time = (TextView) findViewById(R.id._act6_txt_time); //test
        flipper = (ViewFlipper) findViewById(R.id.viewFlipper2);
    }

    //---------------------------------------------------------------------------------------------------
    //----MAP----//--------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
            else
                Toast.makeText(getApplicationContext(), "Loaded failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);

        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(koneksi == true) {
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    if (mMap != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                    }
                    tempLat = location.getLatitude();
                    tempLong = location.getLongitude();
                    tempSpeed = location.getSpeed() * 36 / 10;

                    if (location.getAccuracy() <= 20) {
                        ready = true; //Menentukan apakah aplikasi siap dimulai
                        if (belumDimulai) {
                            pDialog.setMessage("GPS Connection Ready");
                            pDialog.dismiss();

                            belumDimulai = false;
                            okDialog.setTitle("Mengecek Posisi Awal Smartphone");
                            okDialog.setMessage("Posisikan Smartphone Secara Vertikal (Sejajar posisi ban)");
                            okDialog.setIndeterminate(false);
                            okDialog.setCancelable(false);
                            okDialog.show();
                        }
                    }
                    //    if(!isFirstLocation)
//                    _act6_txt_detailDistance.setText(Double.toString(location.getAccuracy()));
                }
            }
        };

       mMap.setOnMyLocationChangeListener(myLocationChangeListener);

    }

    public Double getMapData()
    {
        _act6_txt_detailLat.setText(Double.toString(tempLat));
        _act6_txt_detailLong.setText(Double.toString(tempLong));
        _act6_txt_detailSpeed.setText(Double.toString(tempSpeed));
        return tempSpeed;
    }


    //---------------------------------------------------------------------------------------------------
    //----ACCELEROMETER----//----------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------
    @Override
    public void onSensorChanged(SensorEvent event) {
        tempX = event.values[0];
        tempY = event.values[1];
        tempZ = event.values[2];
        if(tempY >= 9.3 && !belumDimulai){
            posisiOK = true;
            if(okDialog.isShowing()){
                okDialog.dismiss();
                handler.postDelayed(runnable, 0);
            }
        }

    }

    public void getAxisValue()
    {
        _act6_txt_xaxis.setText(Double.toString(Math.abs(tempX)));
        _act6_txt_yaxis.setText(Double.toString(Math.abs(tempY-9.781)));
        _act6_txt_zaxis.setText(Double.toString(Math.abs(tempZ)));
        y_temp.add(tempY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //Option Menu//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu___test_ride_app1, menu);

        return true;
    }

    public void stop(){
        lanjut=false;
        handler.removeCallbacks(runnable);
        Toast.makeText(getApplicationContext(), "Jumlah Data : "+marker_quality.size(), Toast.LENGTH_SHORT).show();

        //Latitude
        double[] lat = new double[marker_lat.size()];
        for(int i = 0; i<marker_lat.size(); i++) {
            lat[i] = marker_lat.get(i);
        }

        //Longitude
        double[] lon = new double[marker_lon.size()];
        for(int i = 0; i<marker_lon.size(); i++) {
            lon[i] = marker_lon.get(i);
        }

        //Quality
        int[] qual = new int[marker_quality.size()];
        for(int i = 0; i<marker_quality.size(); i++) {
            qual[i] = marker_quality.get(i);
        }

//        saveData();
        if(koneksi) {
            Intent i = new Intent(Activity3_App1Go.this, Activity3a_App1GoResult.class);
            i.putExtra("qual-prev", qual);
            i.putExtra("lat-prev", lat);
            i.putExtra("lon-prev", lon);

            startActivity(i);
            finish();
        }
        else{
            Intent intent = new Intent(Activity3_App1Go.this, Activity2_MainMap.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if (id == R.id.menu___test_ride_app1_stop) {
            stopQuestion();
            return(true);
        }
        else if(id == R.id.menu___test_ride_app1_detail){
            flipper.showNext();
            return (true);
        }
        else if (id == R.id.menu_mainMap_satellite) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//            mMap.invalidate();
            return(true);
        }
        else if (id == R.id.menu_maimMap_terrain) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            return(true);
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        stopQuestion();
    }

    public void stopQuestion(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Toast.makeText(getBaseContext(), "You have stop the measurement proccess!", Toast.LENGTH_SHORT).show();
                        stop();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to stop?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public class SnapToRoad extends AsyncTask<Integer, Integer, HashMap<Integer,String>> {

        private final String TAG = SnapToRoad.class.getSimpleName();

        @Override
        protected HashMap<Integer, String> doInBackground(Integer... params) {

            HashMap<Integer, String> ret = new HashMap<>();
            Reader rd = null;
            try {
                URL url = new URL("http://maps.google.com/maps/api/directions/json?origin="
                        + tempLat + "," + tempLong +"&destination="+tempLat+","+tempLong+"&sensor=true");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(5000 /* milliseconds */);
                con.setConnectTimeout(5000 /* milliseconds */);
                con.connect();
                if (con.getResponseCode() == 200) {
                    rd = new InputStreamReader(con.getInputStream());
                    StringBuffer sb = new StringBuffer();
                    final char[] buf = new char[1024];
                    int read;
                    while ((read = rd.read(buf)) > 0) {
                        sb.append(buf, 0, read);
                    }
                    JSONObject jsonObj = new JSONObject(sb.toString());
                    JSONArray predsJsonArray = jsonObj.getJSONArray("routes");
                    lat = predsJsonArray.getJSONObject(0).getJSONObject("bounds").getJSONObject("northeast").getDouble("lat");
                    lon = predsJsonArray.getJSONObject(0).getJSONObject("bounds").getJSONObject("northeast").getDouble("lng");
                    ret.put(1, Double.toString(lat));
                    ret.put(2, Double.toString(lon));
                    ret.put(3, Integer.toString(params[0]));
                    Log.v("Hash 1", ret.get(1));
                    Log.v("Hash 2", ret.get(2));
                    Log.v("Hash 3", ret.get(3));
                }
                con.disconnect();

            } catch (Exception e) {
                Log.e("foo", "bar", e);
                ret.put(1, Double.toString(0.0));
                ret.put(2, Double.toString(0.0));
                ret.put(3, Integer.toString(params[0]));
                Log.v("Hash 1", ret.get(1));
                Log.v("Hash 2", ret.get(2));
                Log.v("Hash 3", ret.get(3));
            } finally {
                if (rd != null) {
                    try {
                        rd.close();
                    } catch (IOException e) {
                        Log.e(TAG, "", e);
                    }
                }
            }
            return ret;
        }

        @Override
        protected void onPostExecute(HashMap<Integer, String> ret) {

            try {
                showMarker(Integer.parseInt(ret.get(3)), Double.parseDouble(ret.get(1)), Double.parseDouble(ret.get(2)));
            }catch (Exception ex){
                Log.e("ShowMarker", ex.getMessage());
            }
            Log.v("Hash 1", ret.get(1));
            Log.v("Hash 2", ret.get(2));
            Log.v("Hash 3", ret.get(3));
        }

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
            nDialog = new ProgressDialog(Activity3_App1Go.this);
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
            nDialog.dismiss();
            if(th){
                koneksi = true;
                pDialog = new ProgressDialog(Activity3_App1Go.this);
                pDialog.setTitle("Waiting for GPS connection");
                pDialog.setMessage("Please Wait ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            //    mulai();
            }
            else{
                Toast.makeText(getBaseContext(), "Error in Network Connection", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity3_App1Go.this, Activity2_MainMap.class);
                startActivity(intent);
                finish();
            }
        }
    }

}