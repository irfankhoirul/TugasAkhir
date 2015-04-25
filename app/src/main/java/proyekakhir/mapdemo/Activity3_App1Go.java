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
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class Activity3_App1Go extends FragmentActivity implements SensorEventListener, LocationListener {

    //----MAP----//
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Location FIRST_LOCATION;
    private double tempLat, tempLong;
    private double tempSpeed;

    //----ACCELEROMETER----//
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int count=0;
    private boolean timerstart = false;
    private boolean stopSave = false;
    private boolean ACCELEROMETER_START = false;
    private double tempX, tempY, tempZ;

    //Data yang diambil//
    List<Double> x = new ArrayList<>();
    List<Double> y = new ArrayList<>();
    List<Double> z = new ArrayList<>();
    List<Double> speed = new ArrayList<>();
    List<Double> arr_latitude = new ArrayList<>();
    List<Double> arr_longitude = new ArrayList<>();
    List<Integer> waktu = new ArrayList<>();

    //----ALL----//
    private TextView _act6_txt_detailLat, _act6_txt_detailLong, _act6_txt_detailSpeed,
            _act6_txt_detailDistance, _act6_txt_xaxis, _act6_txt_yaxis, _act6_txt_zaxis,
            _act6_txt_time;
    private ViewFlipper flipper;

    private double timer;

    private int countPengukuran = 0;

    //---------------------------------------------------------------------------------------------------
    //----ALL----//--------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3_app1go);

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

    public void mulai(){
        float[] axisValue = new float[3];
        Timer T=new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int detik = count;
                        timeConverter(detik);
                        getAxisValue();
                        getMapData();
                        waktu.add(detik);


                        if(detik%5==0)
                        {
                            // histogram();
                            // clearMemory();
                        }

                        count++;
                    }
                });
            }
        },0,20); //50 data every second (40x25=1000 (1s))
    }

    public void timeConverter(int time){
        int jam, menit, detik, temp;

        jam = time/3600;
        temp = time%3600;
        menit = temp/60;
        detik = temp%60;

        timer = detik;

        _act6_txt_time.setText(Integer.toString(jam)+":"+Integer.toString(menit)+":"+Integer.toString(detik));
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

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    protected void onPause() {
        super.onPause();
    }

//    @Override
//    public void onBackPressed() {
//    }

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

                if(firstChange==true) {
//                    now.setLatitude(location.getLatitude());
//                    now.setLongitude(location.getLongitude());
                    firstChange=false;
                    Toast.makeText(getApplicationContext(), "First Change!", Toast.LENGTH_SHORT).show();
                }else {
//                    prev = now;
//                    now.setLatitude(location.getLatitude());
//                    now.setLongitude(location.getLongitude());

//                    distance+=prev.distanceTo(now);
                }

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

    public ArrayList<Float> hitungF(ArrayList yy){
        ArrayList<Float> F = new ArrayList<Float>();
        float tempM = 0,k=0;
        int batas = yy.size();
        int sisa = batas%10;
        batas-=sisa;
        for(int i = 0; i<batas; i+=10) {
            for (int j = i; j < i+10; j++) {
                tempM += (Float) yy.get(j);
            }
            F.add(tempM);
            tempM=0;
        }
        return F;
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
        Toast.makeText(getApplicationContext(), "Stopped!", Toast.LENGTH_SHORT).show();

        int[] time = new int[waktu.size()];
        for(int i = 0; i<waktu.size(); i++) {
            time[i] = waktu.get(i);
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
        i.putExtra("time-length",waktu.size());
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
            file+="\nWaktu\n";
            for(int i = 0; i<waktu.size(); i++){
                file+=waktu.get(i)+"\n";
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

}