package proyekakhir.mapdemo.Activity.TemukanLubang;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

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

import proyekakhir.mapdemo.Activity.Activity2_MainMap;
import proyekakhir.mapdemo.NonActivity.DatabaseHandler;
import proyekakhir.mapdemo.NonActivity.Group2;
import proyekakhir.mapdemo.NonActivity.MyExpandableListAdapter_LubangJalan;
import proyekakhir.mapdemo.NonActivity.User;
import proyekakhir.mapdemo.NonActivity.UserFunctions;
import proyekakhir.mapdemo.R;


public class Activity7_ResultTemukanLubang extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public boolean dataLoaded = false;
    List<String> resultNamaJalan;
//    List<String> resultKualitas;
    List<String> resultKec;
    List<String> resultKota;
    List<String> resultProv;
    List<String> resultTanggal, resultLat, resultLon;
    String[] key;
    String[] filter;
    String where = "";
    int start, end;
    Boolean max = false;
    String userID, range = "";
    int caller;

    private String SERVER_ADDRESS = "http://surveyorider.zz.mu/SurveyoRiderServices/";
    private SwipeRefreshLayout swipeLayout;

    SparseArray<Group2> groups = new SparseArray<>();

    Button act6_bt_load;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity4c__show_pothole);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#303F9F")));

        /// Drawer activity
        //    FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_frame);
        //    LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //    View activityView = layoutInflater.inflate(R.layout.activity6_app1resultmap, null,false);
        //    frameLayout.addView(activityView);
        ///

        try {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            User user = db.getUser();
            userID = user.getUser_id();
            db.close();
        }
        catch(Exception ex)
        {
            Toast.makeText(getBaseContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

        Intent intent = getIntent();
        where = intent.getStringExtra("where");
        Log.v("Where Intent", where);
        start = intent.getIntExtra("start", 0);
        end = intent.getIntExtra("end", 0);
        caller = intent.getIntExtra("caller", 0);
        if(caller == 1){
            range = intent.getStringExtra("range");
        }
        else if (caller == 0) {
            range = "";
        }
        Log.v("Range Intent", range);


        new NetCheck().execute();

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_purple,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

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
        inflater.inflate(R.menu.menu_activity4c__show_pothole, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.filterQuery) {
            Intent i = new Intent(Activity7_ResultTemukanLubang.this, Activity7b_ResultTemukanLubang_Filter.class);
            startActivity(i);
            return(true);
        }
        else if (id == R.id.next) {
            if(!max) {
                Intent i = new Intent(Activity7_ResultTemukanLubang.this, Activity7_ResultTemukanLubang.class);
                i.putExtra("start", start + 10);
                i.putExtra("end", end);
                i.putExtra("where", where);
                i.putExtra("caller", caller);
                if(caller == 1)
                    i.putExtra("range", range);
                startActivity(i);
                finish();
                return (true);
            }
            else
                Toast.makeText(getBaseContext(), "Tidak ada data selanjutnya.", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.prev) {
            if(start>0) {
                Intent i = new Intent(Activity7_ResultTemukanLubang.this, Activity7_ResultTemukanLubang.class);
                i.putExtra("start", start - 10);
                i.putExtra("end", end);
                i.putExtra("where", where);
                i.putExtra("caller", caller);
                if(caller == 1)
                    i.putExtra("range", range);
                startActivity(i);
                finish();
                return (true);
            }
            else
                Toast.makeText(getBaseContext(), "Tidak ada data sebelumnya.", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent = new Intent(Activity7_ResultTemukanLubang.this, Activity2_MainMap.class);
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
            nDialog = new ProgressDialog(Activity7_ResultTemukanLubang.this);
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

            if(th == true){
                nDialog.dismiss();
                new LoadingResultData().execute();
            }
            else{
                nDialog.dismiss();
                //    Toast.makeText(getBaseContext(), "Error in Network Connection", Toast.LENGTH_SHORT).show();
                SnackbarManager.show(
                        Snackbar.with(Activity7_ResultTemukanLubang.this)
                                .text("Koneksi Gagal!")
                                .actionLabel("COBA LAGI") // action button label
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        new NetCheck().execute();
                                    }
                                }) // action button's ActionClickListener
                                .actionColor(Color.parseColor("#CDDC39"))
                        , Activity7_ResultTemukanLubang.this);
            }
        }
    }

    private class LoadingResultData extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Activity7_ResultTemukanLubang.this);
            pDialog.setTitle("Getting Data");
            pDialog.setMessage("Please Wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject json = null;
            UserFunctions userFunction = new UserFunctions();
            String tmpRange = range;
            if(caller == 1) {
                range = " AND " + range;
            }
            Log.v("Range Intent Now", range);
            json = userFunction.getAllPotholeData(where, Integer.toString(start), Integer.toString(end), userID, range);
            range = tmpRange;
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
        //    Log.v("Ini Json", json.toString());
            try {
                if (Integer.parseInt(json.getString("success")) == 1) {
                    JSONObject jsonObj = new JSONObject(json.toString());
                    JSONArray daftarTitik = jsonObj.getJSONArray("data");

                    if (daftarTitik.length() < 10) // 10 = jumlah data per activity
                        max = true;

                    resultNamaJalan = new ArrayList<>(daftarTitik.length());
                    resultKec = new ArrayList<>(daftarTitik.length());
                    resultKota = new ArrayList<>(daftarTitik.length());
                    resultProv = new ArrayList<>(daftarTitik.length());
                    resultLat = new ArrayList<>(daftarTitik.length());
                    resultLon = new ArrayList<>(daftarTitik.length());
                    resultTanggal = new ArrayList<>(daftarTitik.length());

                    for (int i = 0; i < daftarTitik.length(); i++) {
                        resultNamaJalan.add(daftarTitik.getJSONObject(i).getString("nama_jalan"));
                        resultKec.add(daftarTitik.getJSONObject(i).getString("kec"));
                        resultKota.add(daftarTitik.getJSONObject(i).getString("kota"));
                        resultProv.add(daftarTitik.getJSONObject(i).getString("prov"));
                        resultLat.add(daftarTitik.getJSONObject(i).getString("lat"));
                        resultLon.add(daftarTitik.getJSONObject(i).getString("lon"));
                        resultTanggal.add(daftarTitik.getJSONObject(i).getString("tanggal"));
                    }
                    dataLoaded = true;

                    //Get disict value of city
                    Set<String> uniqueCity = new HashSet<>(resultKota);
                    for (int j = 0; j < uniqueCity.size(); j++) {
                        Group2 group = new Group2("" + uniqueCity.toArray()[j]);
                        for (int i = 0; i < resultNamaJalan.size(); i++) {
                            if (uniqueCity.toArray()[j].toString().equalsIgnoreCase(resultKota.get(i))) {
                                group.namaJalan.add(resultNamaJalan.get(i));
                                group.kec.add(resultKec.get(i));
                                group.kota.add(resultKota.get(i));
                                group.prov.add(resultProv.get(i));
                                group.tanggal.add(resultTanggal.get(i));
                                group.lat.add(resultLat.get(i));
                                group.lon.add(resultLon.get(i));
                            }
                        }
                        groups.append(j, group);
                    }

                    ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
                    MyExpandableListAdapter_LubangJalan adapter = new MyExpandableListAdapter_LubangJalan(Activity7_ResultTemukanLubang.this, groups);
                    listView.setAdapter(adapter);
                } else if (Integer.parseInt(json.getString("success")) == 2) {
                    max = true;
                } else {
                    Toast.makeText(getBaseContext(), json.getString("data"), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
//                Log.e("Error when getting json", ex.getMessage());
                Toast.makeText(getBaseContext(), "Error : "+ex.getStackTrace(), Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
        }
    }
}
