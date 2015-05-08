package proyekakhir.mapdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Activity3_App1Go extends ActionBarActivity implements SensorEventListener, LocationListener {

    int c = 0;

    //Timer
    Timer timer;

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

    //Data yang diambil//
    List<Double> x = new ArrayList<>();
    List<Double> y = new ArrayList<>();
//    List<Double> y_temp;
    List<Double> z = new ArrayList<>();
    List<Double> speed = new ArrayList<>();
    List<Double> arr_latitude = new ArrayList<>();
    List<Double> arr_longitude = new ArrayList<>();
    List<Integer> counter = new ArrayList<>();

    //Hasil klasifikasi
    int kualitas = 0;

    //----ALL----//
    private TextView _act6_txt_detailLat, _act6_txt_detailLong, _act6_txt_detailSpeed,
            _act6_txt_detailDistance, _act6_txt_xaxis, _act6_txt_yaxis, _act6_txt_zaxis,
            _act6_txt_time;
    private ViewFlipper flipper;

    //---------------------------------------------------------------------------------------------------
    //----ALL----//--------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity3_app1go);

        /*
        /// Drawer activity
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity2_mainmap, null,false);
        frameLayout.addView(activityView);
        ///
        */

        initializeViews();

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
            // fai! we dont have an accelerometer!
            Toast.makeText(getApplicationContext(), "Oops!", Toast.LENGTH_SHORT).show();
        }

        mulai();
    }

    public void resetVariable(){
        x.clear();
        y.clear();
        z.clear();
        speed.clear();
        arr_latitude.clear();
        arr_longitude.clear();
        counter.clear();
        Toast.makeText(getApplicationContext(), "Variable Reseted!", Toast.LENGTH_SHORT).show();
    }

    public void mulai(){
        float[] axisValue = new float[3];
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        counter.add(count);
                        getAxisValue();
                        getMapData();

                        if (count % 50 == 0) { //5 detik per proses
                            histogram();
                            resetVariable();
                            c++;
                        }

                        _act6_txt_time.setText(Integer.toString(count));
                        count++;
                    }
                });
            }
        }, 0, 100); //10 data every second
    }

    /*
    public void timeConverter(int time){
        int jam, menit, detik, temp;

        jam = time/3600;
        temp = time%3600;
        menit = temp/60;
        detik = temp%60;

        timer = detik;

        _act6_txt_time.setText(Integer.toString(jam) + ":" + Integer.toString(menit) + ":" + Integer.toString(detik));
    }
    */

    public void histogram(){
        int j0_1 = 0, j1_2 = 0, j2_3 = 0, j3_4 = 0, j4_5 = 0, j5_6 = 0, j6_7 = 0, j7_8 = 0,
                j8_9 = 0, j9_10 = 0, j10_11 = 0, j11_12 = 0, j12_13 = 0, j13_14 = 0, j14_15 = 0,
                j15_16 = 0, j16_17 = 0, j17_18 = 0, j18_19 = 0, j19_20 = 0;

        for(int i=0; i<y.size(); i++) {
            double nilai_Y = Math.abs(y.get(i)-9.781); //Kurangi G lalu absolutkan

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
        //Baik ==> 1+2+3 >= 90%
        double A = (j0_1+j1_2+j2_3+j3_4);
        //Rusak ==> 1+2+3 <90%
        //Rusak Tipe A ==> Max di 1+2+3
        double B1 = (j1_2+j2_3+j3_4);
        //Rusak Tipe B ==> Max di 4+5+6
        double B2 = (j4_5+j5_6+j6_7);
        //Rusak Tipe C ==> Max di 7+8+9
        double B3 = (j7_8+j8_9+j9_10);
        //Rusak Tipe D ==> Max di 10+11+12
        double B4 = (j10_11+j11_12+j12_13);
        //Rusak Tipe E ==> Max di 13+14+15
        double B5 = (j13_14+j14_15+j15_16);
        //Rusak Tipe F ==> Max di 16+17+18+19+20
        double B6 = (j16_17+j17_18+j18_19+j19_20);

        if(A >= 0.9*50){
            kualitas = 1;
        }
        else {
            if(Math.max(B1,Math.max(B2, Math.max(B3, Math.max(B4, Math.max(B5, B6))))) == B1){
                kualitas = 2;
            }
            else if(Math.max(B1,Math.max(B2, Math.max(B3, Math.max(B4, Math.max(B5, B6))))) == B2){
                kualitas = 3;
            }
            else if(Math.max(B1,Math.max(B2, Math.max(B3, Math.max(B4, Math.max(B5, B6))))) == B3){
                kualitas = 4;
            }
            else if(Math.max(B1,Math.max(B2, Math.max(B3, Math.max(B4, Math.max(B5, B6))))) == B4){
                kualitas = 5;
            }
            else if(Math.max(B1,Math.max(B2, Math.max(B3, Math.max(B4, Math.max(B5, B6))))) == B5){
                kualitas = 6;
            }
            else if(Math.max(B1,Math.max(B2, Math.max(B3, Math.max(B4, Math.max(B5, B6))))) == B6){
                kualitas = 7;
            }
        }

        Toast.makeText(getApplicationContext(), "Histogram created. Output : "+kualitas, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();
        Log.d("Data Histogram ke-" + c, " Max : "+kualitas
                        + "; 0-1: " + Double.toString(j0_1)
                        + "; 1-2: " + Double.toString(j1_2)
                        + "; 2-3: " + Double.toString(j2_3)
                        + "; 3-4: " + Double.toString(j3_4)
                        + "; 4-5: " + Double.toString(j4_5)
                        + "; 5-6: " + Double.toString(j5_6)
                        + "; 6-7: " + Double.toString(j6_7)
                        + "; 7-8: " + Double.toString(j7_8)
                        + "; 8-9: " + Double.toString(j8_9)
                        + "; 9-10: " + Double.toString(j9_10)
                        + "; 10-11: " + Double.toString(j10_11)
                        + "; 11-12: " + Double.toString(j11_12)
                        + "; 12-13: " + Double.toString(j12_13)
                        + "; 13-14: " + Double.toString(j13_14)
                        + "; 14-15: " + Double.toString(j14_15)
                        + "; 15-16: " + Double.toString(j15_16)
                        + "; 16-17: " + Double.toString(j16_17)
                        + "; 17-18: " + Double.toString(j17_18)
                        + "; 18-19: " + Double.toString(j18_19)
                        + "; 19-20: " + Double.toString(j19_20));
        Log.d("Persentase",
                " A:"+ Double.toString(A) +
                "; B1:"+ Double.toString(B1) +
                "; B2:"+ Double.toString(B2) +
                "; B3:"+ Double.toString(B3) +
                "; B4:"+ Double.toString(B4) +
                "; B5:"+ Double.toString(B5) +
                "; B6:"+ Double.toString(B6)
        );
    //    _act6_txt_time.append("#");
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
            else if(mMap == null)
                Toast.makeText(getApplicationContext(), "Loaded failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            Location prev, now;
            boolean firstChange = true;
            double distance = 0.0;

            @Override
            public void onMyLocationChange(Location location) {
                //    Toast.makeText(getApplicationContext(), "Location Changed!", Toast.LENGTH_SHORT).show();
                //    mMap.clear();
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                if(mMap != null){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }
                tempLat = location.getLatitude();
                tempLong = location.getLongitude();
                tempSpeed = location.getSpeed()*36/10;

                _act6_txt_detailDistance.setText(Double.toString(distance));
            }
        };

        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

    }

    public void getMapData()
    {
        _act6_txt_detailLat.setText(Double.toString(tempLat));
        _act6_txt_detailLong.setText(Double.toString(tempLong));
        _act6_txt_detailSpeed.setText(Double.toString(tempSpeed));
        arr_latitude.add(tempLat);
        arr_longitude.add(tempLong);
        speed.add(tempSpeed);
    }


    //---------------------------------------------------------------------------------------------------
    //----ACCELEROMETER----//----------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------
    @Override
    public void onSensorChanged(SensorEvent event) {
        tempX = event.values[0];
        tempY = event.values[1];
        tempZ = event.values[2];
    }

    public void getAxisValue()
    {
        _act6_txt_xaxis.setText(Double.toString(tempX));
        _act6_txt_yaxis.setText(Double.toString(tempY));
        _act6_txt_zaxis.setText(Double.toString(tempZ));
        x.add(tempX);
        y.add(tempY);
        z.add(tempZ);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //---------------------------------------------------------------------------------------------------
    //----SPEED----//------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //Option Menu//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu___test_ride_app1, menu);

        return true;
    }

    public void stop(){
        timer.cancel();
        Toast.makeText(getApplicationContext(), "Stopped!", Toast.LENGTH_SHORT).show();

        int[] time = new int[counter.size()];
        for(int i = 0; i<counter.size(); i++) {
            time[i] = counter.get(i);
        }

        double[] xX = new double[x.size()];
        for(int i = 0; i<x.size(); i++) {
            xX[i] = x.get(i);
        }

        double[] yY = new double[y.size()];
        for(int i = 0; i<y.size(); i++) {
            yY[i] = y.get(i);
        }

        double[] zZ = new double[z.size()];
        for(int i = 0; i<z.size(); i++) {
            zZ[i] = z.get(i);
        }

        double[] lati = new double[arr_latitude.size()];
        for(int i = 0; i<arr_latitude.size(); i++) {
            lati[i] = arr_latitude.get(i);
        }

        double[] longi = new double[arr_longitude.size()];
        for(int i = 0; i<arr_longitude.size(); i++) {
            longi[i] = arr_longitude.get(i);
        }

        double[] v = new double[speed.size()];
        for(int i = 0; i<speed.size(); i++) {
            v[i] = speed.get(i);
        }

//        saveData();
        Intent i = new Intent (Activity3_App1Go.this,Activity3a_App1GoResult.class);
        i.putExtra("time-length",counter.size());
        i.putExtra("time",time);

        i.putExtra("x-axis-length",x.size());
        i.putExtra("x-axis",xX);

        i.putExtra("y-axis-length",y.size());
        i.putExtra("y-axis",yY);

        i.putExtra("z-axis-length",z.size());
        i.putExtra("z-axis",zZ);

        i.putExtra("latitude-length",arr_latitude.size());
        i.putExtra("latitude",lati);

        i.putExtra("longitude-length",arr_longitude.size());
        i.putExtra("longitude",longi);

        i.putExtra("speed-length",speed.size());
        i.putExtra("speed",v);

        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu___test_ride_app1_stop:
                stopQuestion();

                return true;
            case R.id.menu___test_ride_app1_detail:
                flipper.showNext();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveData()
    {
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int a = c.get(Calendar.DATE);
        String file = "This is record file\n";
        try {
//            File myFile = new File("/sdcard/pengukuran/p"+countPengukuran+".txt");
            String location = "/sdcard/"+a+"_"+seconds+".txt";
            File myFile = new File(location);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            //isikan data
            file+="\nCounter\n";
            for(int i = 0; i<counter.size(); i++){
                file+=counter.get(i)+"\n";
            }
            file+="\nX-Axis\n";
            for(int i = 0; i<x.size(); i++){
                file+=x.get(i)+"\n";
            }
            file+="\nY-Axis\n";
            for(int i = 0; i<y.size(); i++){
                file+=y.get(i)+"\n";
            }
            file+="\nZ-Axis\n";
            for(int i = 0; i<z.size(); i++){
                file+=z.get(i)+"\n";
            }
            file+="\nLatitude\n";
            for(int i = 0; i<arr_latitude.size(); i++){
                file+=arr_latitude.get(i)+"\n";
            }
            file+="\nLongitude\n";
            for(int i = 0; i<arr_longitude.size(); i++){
                file+=arr_longitude.get(i)+"\n";
            }
            file+="\nSpeed\n";
            for(int i = 0; i<speed.size(); i++){
                file+=speed.get(i)+"\n";
            }
            file+="\n\nEnd of file";

            myOutWriter.append(file);
            myOutWriter.close();
            fOut.close();
            Toast.makeText(getBaseContext(),"Done writing SD 'mysdfile.txt'",Toast.LENGTH_SHORT).show();
        //    countPengukuran++;
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
        }
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

    /**
     * Async Task to count histogram
     **/
    private class HistogramBackgroundTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... args) {
            histogram();
            resetVariable();
            c++;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean out) {

        }
    }

}