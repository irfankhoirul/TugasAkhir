package proyekakhir.mapdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import proyekakhir.mapdemo.library.UserFunctions;


public class Activity6a_App1ResultMapDetails extends AppCompatActivity {
    private GoogleMap mMap;
    public String fullAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity6a_app1_result_map_details);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#536DFE")));

        Intent intent = getIntent();
        fullAddress = intent.getStringExtra("fullAddress");

        new NetCheck().execute();
        setUpMapIfNeeded();
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
        mMap.setBuildingsEnabled(true); //add
        mMap.getUiSettings().setZoomControlsEnabled(true);

//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

//        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude())
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                //        Toast.makeText(getApplicationContext(), "Location Changed!", Toast.LENGTH_SHORT).show();
                //    mMap.clear();
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        //        if(mMap != null){
                    //    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        //        }
            }
        };

        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity6a__app1_result_map_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_mainMap_satellite) {
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

    /**
     * Async Task to check whether internet connection is working.
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Activity6a_App1ResultMapDetails.this);
            nDialog.setTitle("Contacting Server");
            nDialog.setMessage("Please Wait ...");
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
                new LoadingResultData().execute();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(getBaseContext(), "Error in Network Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LoadingResultData extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Activity6a_App1ResultMapDetails.this);
            pDialog.setTitle("Getting Data");
            pDialog.setMessage("Please Wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.getRoadDataDetails(fullAddress);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try{
                Log.v("Road Details", json.toString());

                if(Integer.parseInt(json.getString("success")) == 1){
                    JSONObject jsonObj = new JSONObject(json.toString());
                    JSONArray daftarTitik = jsonObj.getJSONArray("data");
                    Double lat = 0.0, lon = 0.0;

                    for(int i=0; i<daftarTitik.length(); i++){
                        lat = Double.parseDouble(daftarTitik.getJSONObject(i).getString("lat"));
                        lon = Double.parseDouble(daftarTitik.getJSONObject(i).getString("lon"));
                        int qual = Integer.parseInt(daftarTitik.getJSONObject(i).getString("kualitas"));
                        if(qual == 1) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title("Q1")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_blue)));
                        }
                        /*
                        else if(qual == 2) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title("Q2")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_light_blue)));
                        }
                        else if(qual == 3) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title("Q3")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_lime)));
                        }
                        else if(qual == 4) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title("Q4")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_yellow)));
                        }
                        */
                        else if(qual == 2) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title("Q2")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange)));
                        }
                        else if(qual == 3) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title("Q3")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_red)));
                        }
                    //    Log.v("Titik", "Titik Ke-"+i );
                    }
                    LatLng loc = new LatLng(lat, lon);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));

                    /*
                    resultNamaJalan = new ArrayList<String>(daftarJalan.length());
                    resultKualitas = new ArrayList<String>(daftarJalan.length());
                    resultKec = new ArrayList<String>(daftarJalan.length());
                    resultKota = new ArrayList<String>(daftarJalan.length());
                    resultProv = new ArrayList<String>(daftarJalan.length());
                    resultPersentase = new ArrayList<String>(daftarJalan.length());

                    for (int i = 0; i < daftarJalan.length(); i++) {
                        resultNamaJalan.add(daftarJalan.getJSONObject(i).getString("nama_jalan"));
                        resultKualitas.add(daftarJalan.getJSONObject(i).getString("kualitas"));
                        resultKec.add(daftarJalan.getJSONObject(i).getString("kec"));
                        resultKota.add(daftarJalan.getJSONObject(i).getString("kota"));
                        resultProv.add(daftarJalan.getJSONObject(i).getString("prov"));
                        resultPersentase.add(daftarJalan.getJSONObject(i).getString("persentase"));
                    }
                    dataLoaded = true;
                    */
                }
                else
                    Log.v("Hasil Success", "Success Not Detected!");

            }catch(Exception ex)
            {
                Log.e("Error ehen getting json", ex.getMessage());
            }
            Log.v("Json Out", json.toString());
            pDialog.dismiss();
        }
    }
}
