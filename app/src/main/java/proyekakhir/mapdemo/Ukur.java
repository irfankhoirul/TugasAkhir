package proyekakhir.mapdemo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Ukur extends Activity implements SensorEventListener {

    private float lastX, lastY, lastZ;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private int count=0;
    private boolean timerstart = false;

    private static final String FILENAME = "myFile.txt";
    private static final String TAG = Ukur.class.getName();

    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ, s, ms, F_out, A_out;
    private Button start, stop;

    private boolean stopSave = false;

    ArrayList<Float> x = new ArrayList<Float>();
    ArrayList<Float> y = new ArrayList<Float>();
    ArrayList<Float> z = new ArrayList<Float>();
    ArrayList<Integer> mdetik = new ArrayList<Integer>();
//    ArrayList<Pengukuran> hasil = new ArrayList<Pengukuran>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ukur);
    //    initializeViews();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // fai! we dont have an accelerometer!
        }

        /*
        stop= (Button)findViewById(R.id.bt_stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                ArrayList<Float> A = new ArrayList<Float>();
                stopSave = true;
                A = hitungF(y);

                try {
                    File myFile = new File("/sdcard/record.txt");
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);

                    myOutWriter.append("#Data MS All#\n");
                    for(int i=0;i<mdetik.size();i++){
                        myOutWriter.append(Integer.toString((mdetik.get(i)))+";\n");
                    }

                    myOutWriter.append("#Data X All#\n");
                    for(int i=0;i<mdetik.size();i++){
                        myOutWriter.append(Float.toString(x.get(i))+";\n");
                    }

                    myOutWriter.append("#Data Y All#\n");
                    for(int i=0;i<mdetik.size();i++){
                        myOutWriter.append(Float.toString(y.get(i))+";\n");
                    }

                    myOutWriter.append("#Data Z All#\n");
                    for(int i=0;i<mdetik.size();i++){
                        myOutWriter.append(Float.toString(z.get(i))+";\n");
                    }

                //    myOutWriter.append("\n\n#Data A per detik#\n");
                //    for(int i=0;i<A.size();i++){
                //        myOutWriter.append(i+",t="+Float.toString((A.get(i)))+",r="+Float.toString((A.get(i))/10)+";\n");
                //    }

                    myOutWriter.append("\n#end");
                    myOutWriter.close();
                    fOut.close();
                    Toast.makeText(getApplicationContext(),"Done writing SD 'record.txt'", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

        });
        */
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

    public void timer(SensorEvent event)
    {
        timerstart=true;
        Timer T=new Timer();
        final SensorEvent ev=event;
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int milidetik = count;
                        int detik = count/10;
                        milidetik%=10;
                   //     if(stopSave = false) {
                        //    ms.setText(Integer.toString(milidetik));
                        //    s.setText(Integer.toString(detik));
                            x.add(ev.values[0]);
                            y.add(ev.values[1]);
                            z.add(ev.values[2]);
                            mdetik.add(count);
                   //     }
//                        hasil.add(new Pengukuran(count,ev.values[1]));

                    //    displayCurrentValues(ev);
                        count++;
                    }
                });
            }
        },0,100);
    }


    public void initializeViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);
        s = (TextView) findViewById(R.id.s);
//        ms = (TextView) findViewById(com.google.android.gms.internal.ms);

        ms = (TextView) findViewById(R.id.ms);
        F_out = (TextView) findViewById(R.id.F_out);
        A_out = (TextView) findViewById(R.id.A_out);

        maxX = (TextView) findViewById(R.id.maxX);
        maxY = (TextView) findViewById(R.id.maxY);
        maxZ = (TextView) findViewById(R.id.maxZ);
    }

    protected void onResume() {
        super.onResume();
//        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
    //    sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(timerstart==false)
            timer(event);
        // clean current values
//        displayCleanValues();
        // display the current x,y,z accelerometer values
//        displayCurrentValues(event);
        // display the max x,y,z accelerometer values
    //    displayMaxValues();

        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // if the change is below 2, it is just plain noise
        if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;
    }

    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentValues(SensorEvent event) {
        currentX.setText(Float.toString(event.values[0]));
        currentY.setText(Float.toString(event.values[1]));
        currentZ.setText(Float.toString(event.values[2]));
    }

    // display the max x,y,z accelerometer values
    public void displayMaxValues() {
        if (deltaX > deltaXMax) {
            deltaXMax = deltaX;
            maxX.setText(Float.toString(deltaXMax));
        }
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY;
            maxY.setText(Float.toString(deltaYMax));
        }
        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ;
            maxZ.setText(Float.toString(deltaZMax));
        }
    }
}
