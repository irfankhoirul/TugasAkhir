package proyekakhir.mapdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Activity3a_App1GoResult extends DrawerActivity {

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_create_product = "http://muhlish.com/ta/webservice/create_product.php";    //internet
//    private static String url_create_product = "http://192.168.0.105:81/mysqlphpandroidcrud/create_product.php"; //local

    private static final String TAG_SUCCESS = "success";

    List<Double> xX = new ArrayList<>(); //X-axis
    List<Double> yY = new ArrayList<>(); //Y-axis
    List<Double> zZ = new ArrayList<>(); //z-axis
    List<Double> lati = new ArrayList<>(); //Latitude
    List<Double> longi = new ArrayList<>(); //Longitude
    List<Double> v = new ArrayList<>(); //Speed
    List<Integer> tT = new ArrayList<>(); //waktu

    int min;
    int[] t;
    double[] x,y,z;
    int xS,yS,zS, latS, longS, speedS, tS;
    double[] latitude, longitude, speed;

    String temp_parT,temp_parX,temp_parY,temp_parZ,temp_parLat,temp_parLong,temp_parV;
    String timeEnd, tempTimeEnd;

    Button sendToServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity3a_app1goresult);

        /// Drawer activity
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity3a_app1goresult, null,false);
        frameLayout.addView(activityView);
        ///

        //get date for pengukuran table
        Calendar c = Calendar.getInstance();
        String jam = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        String menit = Integer.toString(c.get(Calendar.MINUTE));
        Toast.makeText(getBaseContext(), jam+":"+menit, Toast.LENGTH_LONG).show();
        timeEnd = jam+":"+menit;

        getAllIntentData();

        Toast.makeText(getBaseContext(), "Jumlah data = "+min, Toast.LENGTH_LONG).show();

        sendToServer = (Button)findViewById(R.id.bt_sendToServer);
        sendToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
        //        new CreateNewProduct().execute();
                new saveData().execute();
                Toast.makeText(getBaseContext(), "Saving data..", Toast.LENGTH_LONG).show();
            }

        });


        /*
        ListView list = (ListView) findViewById(R.id.dataHasil);

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        for(int a=0;a<xLength;a++)
        {
            map = new HashMap<String, String>();
            map.put("x", Float.toString(x[a]));
            mylist.add(map);
        }

        SimpleAdapter mSchedule = new SimpleAdapter(this, mylist, R.layout.__activity___test_ride_app1_details,
                new String[] {"x"}, new int[] {R.id.t_x});

        list.setAdapter(mSchedule);
        */


    }

    public void getAllIntentData()
    {
        Intent intent = getIntent();
        tS = intent.getIntExtra("time-length",0);
        t = new int[tS];
        t = intent.getIntArrayExtra("time");
        for(int i=0;i<t.length;i++)
        {
            tT.add(t[i]);
        }

        xS = intent.getIntExtra("x-axis-length",0);
        x = new double[xS];
        x = intent.getDoubleArrayExtra("x-axis");
        for(int i=0;i<x.length;i++)
        {
            xX.add(x[i]);
        }

        yS = intent.getIntExtra("y-axis-length",0);
        y = new double[yS];
        y = intent.getDoubleArrayExtra("y-axis");
        for(int i=0;i<y.length;i++)
        {
            yY.add(y[i]);
        }

        zS = intent.getIntExtra("z-axis-length",0);
        z = new double[zS];
        z = intent.getDoubleArrayExtra("z-axis");
        for(int i=0;i<z.length;i++)
        {
            zZ.add(z[i]);
        }

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

        speedS = intent.getIntExtra("speed-length",0);
        speed = new double[speedS];
        speed = intent.getDoubleArrayExtra("speed");
        for(int i=0;i<speed.length;i++)
        {
            v.add(speed[i]);
        }

        min = Math.min(tT.size(), Math.min(xX.size(), Math.min(yY.size(), Math.min(zZ.size(), Math.min(lati.size(), Math.min(longi.size(), v.size()))))));
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

    /**
     * Background Async Task to Create new product
     * */
    class saveData extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Activity3a_App1GoResult.this);
            pDialog.setMessage("Saving data to SD Card...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving data to SD Card
         * */
        protected String doInBackground(String... args) {
            Calendar c = Calendar.getInstance();
            int tahun = c.get(Calendar.YEAR);
            int bulan = c.get(Calendar.MONTH);
            int tanggal = c.get(Calendar.DATE);
            int jam = c.get(Calendar.HOUR);
            int menit = c.get(Calendar.MINUTE);
            int detik = c.get(Calendar.SECOND);
            String file = "This is record file\n";
            try {
//            File myFile = new File("/sdcard/pengukuran/p"+countPengukuran+".txt");
                String location = "/sdcard/surveyo"+tahun+bulan+tanggal+"_"+jam+menit+detik+".txt";
                File myFile = new File(location);
                myFile.createNewFile();
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                //isikan data
                file+="\nWaktu\n";
                for(int i = 0; i<tT.size(); i++){
                    file+=tT.get(i)+"\n";
                }
                file+="\nX-Axis\n";
                for(int i = 0; i<xX.size(); i++){
                    file+=xX.get(i)+"\n";
                }
                file+="\nY-Axis\n";
                for(int i = 0; i<yY.size(); i++){
                    file+=yY.get(i)+"\n";
                }
                file+="\nZ-Axis\n";
                for(int i = 0; i<zZ.size(); i++){
                    file+=zZ.get(i)+"\n";
                }
                file+="\nLatitude\n";
                for(int i = 0; i<lati.size(); i++){
                    file+=lati.get(i)+"\n";
                }
                file+="\nLongitude\n";
                for(int i = 0; i<longi.size(); i++){
                    file+=longi.get(i)+"\n";
                }
                file+="\nSpeed\n";
                for(int i = 0; i<v.size(); i++){
                    file+=v.get(i)+"\n";
                }
                file+="\n\nEnd of file";

                myOutWriter.append(file);
                myOutWriter.close();
                fOut.close();
            //    Toast.makeText(getBaseContext(),"Done save data to SD Card.",Toast.LENGTH_SHORT).show();
            //    countPengukuran++;
                Log.i("Save Data", "Data saved in SD Card.");
            } catch (Exception e) {
            //    Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("Save Data", "Error saving data!", e);

            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }


    /**
     * Background Async Task to Create new product
     * */
    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Activity3a_App1GoResult.this);
            pDialog.setMessage("Sending...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
        //    String p_xAxis = temp_parX;
        //    String p_yAxis = temp_parY;
        //    String p_zAxis = temp_parZ;
        //    String p_lati = temp_parLat;
        //    String p_longi = temp_parLong;
        //    String p_speed = temp_parV;
        //    String p_time = temp_parT;
        //    String p_timeEnd = timeEnd;

            /* JSONObject
            for(int i = 0; i<min;i++){
                temp_parT = Integer.toString(tT.get(i));
                temp_parX = Double.toString(xX.get(i));
                temp_parY = Double.toString(yY.get(i));
                temp_parZ = Double.toString(zZ.get(i));
                temp_parLat = Double.toString(lati.get(i));
                temp_parLong = Double.toString(longi.get(i));
                temp_parV = Double.toString(v.get(i));

                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("p_xAxis", temp_parX));
                params.add(new BasicNameValuePair("p_yAxis", temp_parY));
                params.add(new BasicNameValuePair("p_zAxis", temp_parZ));
                params.add(new BasicNameValuePair("p_lati", temp_parLat));
                params.add(new BasicNameValuePair("p_longi", temp_parLong));
                params.add(new BasicNameValuePair("p_speed", temp_parV));
                params.add(new BasicNameValuePair("p_time", temp_parT));
                params.add(new BasicNameValuePair("p_timeEnd", timeEnd));

                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_create_product, "POST", params);

            }
            */

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }


    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent = new Intent(Activity3a_App1GoResult.this, Activity2_MainMap.class);
        startActivity(intent);
        finish();
    }


}
