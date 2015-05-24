package proyekakhir.mapdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import proyekakhir.mapdemo.library.UserFunctions;


public class Activity6_App1ResultMap extends DrawerActivity implements SwipeRefreshLayout.OnRefreshListener {
    public boolean dataLoaded = false;
    List<String> resultNamaJalan;
    List<String> resultKualitas;
    List<String> resultKec;
    List<String> resultKota;
    List<String> resultProv;
    List<String> resultPersentase;

    private String SERVER_ADDRESS = "http://surveyorider.zz.mu/SurveyoRiderServices/";
    private SwipeRefreshLayout swipeLayout;

    SparseArray<Group> groups = new SparseArray<Group>();

    Button act6_bt_load;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    setContentView(R.layout.activity2_mainmap);

        /// Drawer activity
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity6_app1resultmap, null,false);
        frameLayout.addView(activityView);
        ///

        new NetCheck().execute();

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_purple,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        /*
        act6_bt_load = (Button) findViewById(R.id.act6_bt_load);
        act6_bt_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dataLoaded) {
                    Toast.makeText(getApplicationContext(), "Data Have Been Loaded!", Toast.LENGTH_SHORT).show();

                    //Get disict value of city
                    Set<String> uniqueCity = new HashSet<String>(resultKota);
                    for (int j = 0; j < uniqueCity.size(); j++) {
                        Group group = new Group("" + uniqueCity.toArray()[j]);
                        for (int i = 0; i < resultNamaJalan.size(); i++) {
                            if (uniqueCity.toArray()[j].toString().equalsIgnoreCase(resultKota.get(i))) {
                                group.namaJalan.add(resultNamaJalan.get(i));
                            //    group.alamatJalan.add(resultKec.get(i) + ", " + resultKota.get(i) + ", " + resultProv.get(i));
                                group.kondisiJalan.add("Kualitas jalan : " + resultKualitas.get(i) + ", Persentase : " + resultPersentase.get(i) + "%");
                                group.nilaiKondisi.add(resultKualitas.get(i));
                                group.kec.add(resultKec.get(i));
                                group.kota.add(resultKota.get(i));
                                group.prov.add(resultProv.get(i));
                            }
                        }
                        groups.append(j, group);
                    }

                    ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
                    MyExpandableListAdapter adapter = new MyExpandableListAdapter(Activity6_App1ResultMap.this, groups);
                    listView.setAdapter(adapter);

                }
                else
                    Toast.makeText(getApplicationContext(), "Data Have Not Been Loaded! Load First!", Toast.LENGTH_SHORT).show();
            }

        });
        */

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
                new NetCheck().execute();
            }
        }, 3000);

    }

    //Option Menu//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity6__app1_result_map, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent = new Intent(Activity6_App1ResultMap.this, Activity2_MainMap.class);
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
            nDialog = new ProgressDialog(Activity6_App1ResultMap.this);
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
            pDialog = new ProgressDialog(Activity6_App1ResultMap.this);
            pDialog.setTitle("Getting Data");
            pDialog.setMessage("Please Wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.getAllRoadData();
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try{
                if(Integer.parseInt(json.getString("success")) == 1){
                    JSONObject jsonObj = new JSONObject(json.toString());
                    JSONArray daftarJalan = jsonObj.getJSONArray("data");

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

                    //Get disict value of city
                    Set<String> uniqueCity = new HashSet<String>(resultKota);
                    for (int j = 0; j < uniqueCity.size(); j++) {
                        Group group = new Group("" + uniqueCity.toArray()[j]);
                        for (int i = 0; i < resultNamaJalan.size(); i++) {
                            if (uniqueCity.toArray()[j].toString().equalsIgnoreCase(resultKota.get(i))) {
                                group.namaJalan.add(resultNamaJalan.get(i));
                                //    group.alamatJalan.add(resultKec.get(i) + ", " + resultKota.get(i) + ", " + resultProv.get(i));
                                group.kondisiJalan.add("Kualitas jalan : " + resultKualitas.get(i) + ", Persentase : " + resultPersentase.get(i) + "%");
                                group.nilaiKondisi.add(resultKualitas.get(i));
                                group.kec.add(resultKec.get(i));
                                group.kota.add(resultKota.get(i));
                                group.prov.add(resultProv.get(i));
                            }
                        }
                        groups.append(j, group);
                    }

                    ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
                    MyExpandableListAdapter adapter = new MyExpandableListAdapter(Activity6_App1ResultMap.this, groups);
                    listView.setAdapter(adapter);
                }
            }catch(Exception ex)
            {
                Log.e("Error ehen getting json", ex.getMessage());
            }
            Log.v("Json Out", json.toString());
            pDialog.dismiss();
        }
    }
}