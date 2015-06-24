package proyekakhir.mapdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import proyekakhir.mapdemo.library.DatabaseHandler;
import proyekakhir.mapdemo.library.UserFunctions;


public class Activity6c_App1SpecialResult extends AppCompatActivity {

    TextView actResultFilter_filterAdded, daftarFilter, txt_dari, txt_sampai;
    Button actResultFilter_bt_addFilter, actResultFilter_bt_show, bt_begin, bt_end;
    String filter = "", filter1, filter2, filter3, filter4, filter5, filter6 = "", filter7, filter8,
            filter9, dateBegin, dateEnd, userID;
    String[] finalfilterkeyArray, finalfiltervalueArray;
    DatePicker dp;

    boolean stat_namaJalan = false,
            stat_kec = false,
            stat_kota = false,
            stat_prov = false,
            stat_kualitas = false,
            stat_merkSmartphone = false,
            stat_tipeSmartphone = false,
            stat_merkMotor = false,
            stat_tipeMotor = false;
    LinearLayout timeFilter, spinnerValue;

//    List<String> FILTER_NAMA_JALAN = new ArrayList<String>();
//    List<String> FILTER_KECAMATAN = new ArrayList<String>();
//    List<String> FILTER_KOTA = new ArrayList<String>();
//    List<String> FILTER_PROVINSI = new ArrayList<String>();
//    List<String> FILTER_KUALITAS = new ArrayList<String>();
    List<String> FILTER_MERK_SMARTPHONE = new ArrayList<>();
    List<String> FILTER_TIPE_SMARTPHONE = new ArrayList<>();
    List<String> FILTER_MERK_MOTOR = new ArrayList<>();
    List<String> FILTER_TIPE_MOTOR = new ArrayList<>();

    ArrayAdapter<String>
       //     adapterNamaJalan, adapterKecamatan, adapterKota, adapterProvinsi,
       //     adapterKualitas,
            adapterMerkSmartphone, adapterTipeSmartphone, adapterMerkMotor,
            adapterTipeMotor;

    String where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity6c__app1_special_result);

        try{
            android.support.v7.app.ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#536DFE")));
        } catch (NullPointerException ex){
        //    Log.e("Null", ex.getMessage());
        }

        try {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            User user = db.getUser();
            userID = user.getUser_id();
        }
        catch(Exception ex)
        {
            Toast.makeText(getBaseContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

        //Initialize Component
        dp =(DatePicker) findViewById(R.id.dp);
        txt_dari = (TextView) findViewById(R.id.txt_dari);
        txt_sampai = (TextView) findViewById(R.id.txt_sampai);
        bt_begin = (Button) findViewById(R.id.bt_begin);
        bt_end = (Button) findViewById(R.id.bt_end);
        spinnerValue = (LinearLayout) findViewById(R.id.spinnerValue);
        timeFilter = (LinearLayout) findViewById(R.id.timeFilter);
        timeFilter.setVisibility(View.GONE);

        bt_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bulan = dp.getMonth() + 1;
                dateBegin = dp.getYear()+"-"+bulan+"-"+dp.getDayOfMonth();
                txt_dari.setText(dateBegin);
            }
        });

        bt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bulan = dp.getMonth() + 1;
                dateEnd = dp.getYear()+ "-"+bulan+"-"+dp.getDayOfMonth();
                txt_sampai.setText(dateEnd);
            }
        });


        //Get Value for filter
        new NetCheck().execute();

        final Spinner spinner2 = (Spinner) findViewById(R.id.keywordValue);

        final Spinner spinner = (Spinner) findViewById(R.id.filterType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filterKhusus, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int item = spinner.getSelectedItemPosition();

                if(item == 4){
                    spinner2.setVisibility(View.GONE);
                    timeFilter.setVisibility(View.VISIBLE);
                }
                else {
                    timeFilter.setVisibility(View.GONE);
                    spinner2.setVisibility(View.VISIBLE);
                    if (item == 0)
                        spinner2.setAdapter(adapterMerkSmartphone);
                    else if (item == 1)
                        spinner2.setAdapter(adapterTipeSmartphone);
                    else if (item == 2)
                        spinner2.setAdapter(adapterMerkMotor);
                    else if (item == 3)
                        spinner2.setAdapter(adapterTipeMotor);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        daftarFilter = (TextView) findViewById(R.id.daftarFilter);

        final HashMap<String, String> filterList = new HashMap<>();

        //Button add filter diklik
        actResultFilter_bt_addFilter = (Button) findViewById(R.id.actResultFilter_bt_addFilter);
        actResultFilter_bt_addFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItemPosition() == 4) {
                    filter6 = "tanggal BETWEEN '" + dateBegin + "' AND '" + dateEnd + "' AND";
                    if (filterList.get("range") == null)
                        filterList.put("range", filter6);
                    else {
                        filterList.remove("range");
                        filterList.put("range", filter6);
                    }
                }
                if(spinner2.getSelectedItem()!=null) {
                    if (spinner.getSelectedItemPosition() == 0) {
                        filter1 = spinner2.getSelectedItem().toString();
                        if (filterList.get("merk_smartphone") == null)
                            filterList.put("merk_smartphone", filter1);
                        else {
                            filterList.remove("merk_smartphone");
                            filterList.put("merk_smartphone", filter1);
                        }
                    } else if (spinner.getSelectedItemPosition() == 1) {
                        filter2 = spinner2.getSelectedItem().toString();
                        if (filterList.get("tipe_smartphone") == null)
                            filterList.put("tipe_smartphone", filter2);
                        else {
                            filterList.remove("tipe_smartphone");
                            filterList.put("tipe_smartphone", filter2);
                        }
                    } else if (spinner.getSelectedItemPosition() == 2) {
                        filter3 = spinner2.getSelectedItem().toString();
                        if (filterList.get("merk_motor") == null)
                            filterList.put("merk_motor", filter3);
                        else {
                            filterList.remove("merk_motor");
                            filterList.put("merk_motor", filter3);
                        }
                    } else if (spinner.getSelectedItemPosition() == 3) {
                        filter4 = spinner2.getSelectedItem().toString();
                        if (filterList.get("tipe_motor") == null)
                            filterList.put("tipe_motor", filter4);
                        else {
                            filterList.remove("tipe_motor");
                            filterList.put("tipe_motor", filter4);
                        }
                    }
                }
        //        else {
                //    if(spinner.getSelectedItemPosition() != 4)
                    //    Toast.makeText(getBaseContext(), "Silakan pilih filter", Toast.LENGTH_SHORT).show();
        //        }

                if(filterList.get("merk_smartphone") != null) {
                    filter+="\nMerk Smartphone : "+filterList.get("merk_smartphone");
                }
                if(filterList.get("tipe_smartphone") != null) {
                    filter+="\nTipe Smartphone : "+filterList.get("tipe_smartphone");
                }
                if(filterList.get("merk_motor") != null) {
                    filter+="\nMerk Motor : "+filterList.get("merk_motor");
                }
                if(filterList.get("tipe_motor") != null) {
                    filter+="\nTipe Motor : "+filterList.get("tipe_motor");
                }
                if(filterList.get("range") != null) {
                    filter+="\nRange Waktu : "+filterList.get("range");
                }

                daftarFilter.setText(filter);
                filter="";
            }
        });

        final HashMap<String, String> finalFilter = new HashMap<>();
        final List<String> finalFilterKey = new ArrayList<>();
        final List<String> finalFilterValue = new ArrayList<>();
        actResultFilter_bt_show = (Button) findViewById(R.id.actResultFilter_bt_show);
        actResultFilter_bt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Final Filter
                if(filterList.get("merk_smartphone") != null) {
                    finalFilterKey.add("merk_smartphone");
                    finalFilterValue.add(filter1);
                }
                if(filterList.get("tipe_smartphone") != null) {
                    finalFilterKey.add("tipe_smartphone");
                    finalFilterValue.add(filter2);
                }
                if(filterList.get("merk_motor") != null) {
                    finalFilterKey.add("merk_motor");
                    finalFilterValue.add(filter3);
                }
                if(filterList.get("tipe_motor") != null) {
                    finalFilterKey.add("tipe_motor");
                    finalFilterValue.add(filter4);
                }

                where = "";
                for(int i = 0; i< finalFilterKey.size(); i++){
                    if(i>0)
                        where+=" AND ";
                    where+=" "+finalFilterKey.get(i)+" = \""+finalFilterValue.get(i)+"\"";
                }

                /*
                if(filterList.get("range") != null) {
                    if(pertama == false)
                        where += " AND "+filter6;
                    else
                        where += " "+filter6;
                }
                */

        //        Toast.makeText(getBaseContext(), "Tanggal : "+filter6, Toast.LENGTH_LONG).show();

                finalFilterKey.clear();
                finalFilterValue.clear();

                if(where.equals("") && filter6.equals("")){
                    Toast.makeText(getBaseContext(), "Pilih minimal 1 filter!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(where.equals(""))
                        where = "1";
                    Intent intent = new Intent(Activity6c_App1SpecialResult.this, Activity6_App1ResultMap.class);
                    intent.putExtra("where", where);
                    intent.putExtra("start", 0);
                    intent.putExtra("end", 10);
                    intent.putExtra("caller", 1);
                    intent.putExtra("range", filter6);
                    startActivity(intent);
                    finish();

                }

        //        Toast.makeText(getBaseContext(), "UserId : "+userID+"\n\n"+where, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent = new Intent(Activity6c_App1SpecialResult.this, Activity6_App1ResultMap.class);
        intent.putExtra("where", "1");
        intent.putExtra("start", 0);
        intent.putExtra("end", 10);
        intent.putExtra("caller", 0);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity6c__app1_special_result, menu);
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

    /**
     * Async Task to check whether internet connection is working.
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Activity6c_App1SpecialResult.this);
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
                new LoadFilter().execute();
            }
            else{
                nDialog.dismiss();
                //    Toast.makeText(getBaseContext(), "Error in Network Connection", Toast.LENGTH_SHORT).show();
                SnackbarManager.show(
                        Snackbar.with(Activity6c_App1SpecialResult.this)
                                .text("Koneksi Gagal!")
                                .actionLabel("COBA LAGI") // action button label
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    }
                                }) // action button's ActionClickListener
                                .actionColor(Color.parseColor("#CDDC39"))
                        , Activity6c_App1SpecialResult.this);
            }
        }
    }

    private class LoadFilter extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String p_username, p_password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Activity6c_App1SpecialResult.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Loading Filter ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.getFilter();
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString("success") != null) {
                    JSONObject jsonObj = new JSONObject(json.toString());

                    JSONArray merk_smartphone = jsonObj.getJSONArray("merk_smartphone");
                    for (int i = 0; i < merk_smartphone.length(); i++) {
                        FILTER_MERK_SMARTPHONE.add(merk_smartphone.getJSONObject(i).getString("merk_smartphone"));
                        //    Log.v("Filter1", FILTER_NAMA_JALAN.get(i));
                    }
                    adapterMerkSmartphone = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.custom_spinner_item,
                            FILTER_MERK_SMARTPHONE);
                    adapterMerkSmartphone.setDropDownViewResource(R.layout.custom_spinner_item);

                    JSONArray tipe_smartphone = jsonObj.getJSONArray("tipe_smartphone");
                    for (int i = 0; i < tipe_smartphone.length(); i++) {
                        FILTER_TIPE_SMARTPHONE.add(tipe_smartphone.getJSONObject(i).getString("tipe_smartphone"));
                        //    Log.v("Filter2", FILTER_KECAMATAN.get(i));
                    }
                    adapterTipeSmartphone = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.custom_spinner_item,
                            FILTER_TIPE_SMARTPHONE);
                    adapterTipeSmartphone.setDropDownViewResource(R.layout.custom_spinner_item);

                    JSONArray merk_motor = jsonObj.getJSONArray("merk_motor");
                    for (int i = 0; i < merk_motor.length(); i++) {
                        FILTER_MERK_MOTOR.add(merk_motor.getJSONObject(i).getString("merk_motor"));
                        //    Log.v("Filter3", FILTER_KOTA.get(i));
                    }
                    adapterMerkMotor = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.custom_spinner_item,
                            FILTER_MERK_MOTOR);
                    adapterMerkMotor.setDropDownViewResource(R.layout.custom_spinner_item);

                    JSONArray tipe_motor = jsonObj.getJSONArray("tipe_motor");
                    for (int i = 0; i < tipe_motor.length(); i++) {
                        FILTER_TIPE_MOTOR.add(tipe_motor.getJSONObject(i).getString("tipe_motor"));
                        //    Log.v("Filter4", FILTER_PROVINSI.get(i));
                    }
                    adapterTipeMotor = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.custom_spinner_item,
                            FILTER_TIPE_MOTOR);
                    adapterTipeMotor.setDropDownViewResource(R.layout.custom_spinner_item);

                }
                pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                pDialog.dismiss();
            }
        }
    }
}
