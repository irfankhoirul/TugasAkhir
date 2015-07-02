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

import proyekakhir.mapdemo.NonActivity.DatabaseHandler;
import proyekakhir.mapdemo.NonActivity.User;
import proyekakhir.mapdemo.NonActivity.UserFunctions;
import proyekakhir.mapdemo.R;

public class Activity7b_ResultTemukanLubang_Filter extends AppCompatActivity {
    TextView actResultFilter_filterAdded, daftarFilter, txt_dari, txt_sampai;
    Button actResultFilter_bt_addFilter, actResultFilter_bt_show, bt_begin, bt_end;
    String filter = "", filter1, filter2, filter3, filter4, filter5 = "", dateBegin, dateEnd, userID;
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

    List<String> FILTER_NAMA_JALAN = new ArrayList<String>();
    List<String> FILTER_KECAMATAN = new ArrayList<String>();
    List<String> FILTER_KOTA = new ArrayList<String>();
    List<String> FILTER_PROVINSI = new ArrayList<String>();
    List<String> FILTER_KUALITAS = new ArrayList<String>();
    List<String> FILTER_MERK_SMARTPHONE = new ArrayList<>();
    List<String> FILTER_TIPE_SMARTPHONE = new ArrayList<>();
    List<String> FILTER_MERK_MOTOR = new ArrayList<>();
    List<String> FILTER_TIPE_MOTOR = new ArrayList<>();

    ArrayAdapter<String>
            adapterNamaJalan, adapterKecamatan, adapterKota, adapterProvinsi,
            adapterKualitas,
            adapterMerkSmartphone, adapterTipeSmartphone, adapterMerkMotor,
            adapterTipeMotor;

    String where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity7b__result_temukan_lubang__filter);

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
                R.array.filterLubang, android.R.layout.simple_spinner_item);
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
                        spinner2.setAdapter(adapterNamaJalan);
                    else if (item == 1)
                        spinner2.setAdapter(adapterKecamatan);
                    else if (item == 2)
                        spinner2.setAdapter(adapterKota);
                    else if (item == 3)
                        spinner2.setAdapter(adapterProvinsi);
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
                    filter5 = "tanggal BETWEEN '" + dateBegin + "' AND '" + dateEnd + "' AND";
                    if (filterList.get("range") == null)
                        filterList.put("range", filter5);
                    else {
                        filterList.remove("range");
                        filterList.put("range", filter5);
                    }
                }
                if(spinner2.getSelectedItem()!=null) {
                    if (spinner.getSelectedItemPosition() == 0) {
                        filter1 = spinner2.getSelectedItem().toString();
                        if (filterList.get("nama_jalan") == null)
                            filterList.put("nama_jalan", filter1);
                        else {
                            filterList.remove("nama_jalan");
                            filterList.put("nama_jalan", filter1);
                        }
                    } else if (spinner.getSelectedItemPosition() == 1) {
                        filter2 = spinner2.getSelectedItem().toString();
                        if (filterList.get("kec") == null)
                            filterList.put("kec", filter2);
                        else {
                            filterList.remove("kec");
                            filterList.put("kec", filter2);
                        }
                    } else if (spinner.getSelectedItemPosition() == 2) {
                        filter3 = spinner2.getSelectedItem().toString();
                        if (filterList.get("kota") == null)
                            filterList.put("kota", filter3);
                        else {
                            filterList.remove("kota");
                            filterList.put("kota", filter3);
                        }
                    } else if (spinner.getSelectedItemPosition() == 3) {
                        filter4 = spinner2.getSelectedItem().toString();
                        if (filterList.get("prov") == null)
                            filterList.put("prov", filter4);
                        else {
                            filterList.remove("prov");
                            filterList.put("prov", filter4);
                        }
                    }
                }

                if(filterList.get("nama_jalan") != null) {
                    filter+="\nNama Jalan : "+filterList.get("nama_jalan");
                }
                if(filterList.get("kec") != null) {
                    filter+="\nKecamatan : "+filterList.get("kec");
                }
                if(filterList.get("kota") != null) {
                    filter+="\nKota : "+filterList.get("kota");
                }
                if(filterList.get("prov") != null) {
                    filter+="\nProvinsi : "+filterList.get("prov");
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
                if(filterList.get("nama_jalan") != null) {
                    finalFilterKey.add("nama_jalan");
                    finalFilterValue.add(filter1);
                }
                if(filterList.get("kec") != null) {
                    finalFilterKey.add("kec");
                    finalFilterValue.add(filter2);
                }
                if(filterList.get("kota") != null) {
                    finalFilterKey.add("kota");
                    finalFilterValue.add(filter3);
                }
                if(filterList.get("prov") != null) {
                    finalFilterKey.add("prov");
                    finalFilterValue.add(filter4);
                }

                where = "";
                for(int i = 0; i< finalFilterKey.size(); i++){
                    if(i>0)
                        where+=" AND ";
                    where+=" "+finalFilterKey.get(i)+" = \""+finalFilterValue.get(i)+"\"";
                }

                finalFilterKey.clear();
                finalFilterValue.clear();

                if(where.equals("") && filter5.equals("")){
                    Toast.makeText(getBaseContext(), "Pilih minimal 1 filter!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(where.equals(""))
                        where = "1";
                    Intent intent = new Intent(Activity7b_ResultTemukanLubang_Filter.this, Activity7_ResultTemukanLubang.class);
                    intent.putExtra("where", where);
                    intent.putExtra("start", 0);
                    intent.putExtra("end", 10);
                    intent.putExtra("caller", 1);
                    intent.putExtra("range", filter5);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent = new Intent(Activity7b_ResultTemukanLubang_Filter.this, Activity7_ResultTemukanLubang.class);
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
            nDialog = new ProgressDialog(Activity7b_ResultTemukanLubang_Filter.this);
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
                SnackbarManager.show(
                        Snackbar.with(Activity7b_ResultTemukanLubang_Filter.this)
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
                        , Activity7b_ResultTemukanLubang_Filter.this);
            }
        }
    }

    private class LoadFilter extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Activity7b_ResultTemukanLubang_Filter.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Loading Filter ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.getFilterLubang();
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                Log.v("Ini Json 2", json.toString());
                if (json.getString("success") != null) {

                    JSONObject jsonObj = new JSONObject(json.toString());

                    JSONArray nama_jalan = jsonObj.getJSONArray("nama_jalan");
                    for (int i = 0; i < nama_jalan.length(); i++) {
                        FILTER_NAMA_JALAN.add(nama_jalan.getJSONObject(i).getString("nama_jalan"));
                        //    Log.v("Filter1", FILTER_NAMA_JALAN.get(i));
                    }
                    adapterNamaJalan = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.custom_spinner_item,
                            FILTER_NAMA_JALAN);
                    adapterNamaJalan.setDropDownViewResource(R.layout.custom_spinner_item);

                    JSONArray kec = jsonObj.getJSONArray("kec");
                    for (int i = 0; i < kec.length(); i++) {
                        FILTER_KECAMATAN.add(kec.getJSONObject(i).getString("kec"));
                        //    Log.v("Filter2", FILTER_KECAMATAN.get(i));
                    }
                    adapterKecamatan = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.custom_spinner_item,
                            FILTER_KECAMATAN);
                    adapterKecamatan.setDropDownViewResource(R.layout.custom_spinner_item);

                    JSONArray kota = jsonObj.getJSONArray("kota");
                    for (int i = 0; i < kota.length(); i++) {
                        FILTER_KOTA.add(kota.getJSONObject(i).getString("kota"));
                        //    Log.v("Filter3", FILTER_KOTA.get(i));
                    }
                    adapterKota = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.custom_spinner_item,
                            FILTER_KOTA);
                    adapterKota.setDropDownViewResource(R.layout.custom_spinner_item);

                    JSONArray prov = jsonObj.getJSONArray("prov");
                    for (int i = 0; i < prov.length(); i++) {
                        FILTER_PROVINSI.add(prov.getJSONObject(i).getString("prov"));
                        //    Log.v("Filter4", FILTER_PROVINSI.get(i));
                    }
                    adapterProvinsi = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.custom_spinner_item,
                            FILTER_PROVINSI);
                    adapterProvinsi.setDropDownViewResource(R.layout.custom_spinner_item);

                }
                pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                pDialog.dismiss();
            }
        }
    }
}
