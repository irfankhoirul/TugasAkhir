package proyekakhir.mapdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import proyekakhir.mapdemo.library.DatabaseHandler;

public class Activity2_MainMap extends DrawerActivity implements OnItemClickListener {

    //----MAP----//
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    //---------------------------------------------------------------------------------------------------
    //----ALL----//--------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------
    private ViewFlipper flipper1;

    //----AUTOCOMPLETE V2----//
    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyA_55wy9ntyhBOtzMz_9lfe0FVBfxC8sds";
//    private static String place_id = "ChIJkxoiIxP61y0RWLLtrZk8bDA";

    static ArrayList<String> resultList = null;
    static ArrayList<String> place_id_resultList = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity2_mainmap);

        /// Drawer activity
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity2_mainmap, null,false);
        frameLayout.addView(activityView);
        ///

        //////// flipper for other layout
        flipper1 = (ViewFlipper) findViewById(R.id.viewFlipper2);
        ////////

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.endPoint);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        //----MAP----//
        setUpMapIfNeeded();

        ////////////////////REG
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        /**
         * Hashmap to load data from the Sqlite database
         **/
//        HashMap<String,String> user = new HashMap<String, String>();
//        user = db.getUserDetails();


    }

    //----AUTOCOMPLETE location V2----//
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        String plId = place_id_resultList.get(resultList.indexOf(str));

    //    place_id = plId;
    //    String lct[] = location(plId);intent

    //    Toast.makeText(this, str +" Place Id : "+plId+" Position : "+lct[0]+","+lct[1], Toast.LENGTH_SHORT).show();
    }


    public static ArrayList<String> autocomplete(String input) {
    //    ArrayList<String> resultList = null;
    //    ArrayList<String> place_id_resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:id");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        //    return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        //    return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            place_id_resultList = new ArrayList<String>(predsJsonArray.length());
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

                place_id_resultList.add(predsJsonArray.getJSONObject(i).getString("place_id"));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    //----GET LatLong----//
    public static String[] location(String placeId) {
        String loc[] = new String[2];
        loc[0] = "lat";
        loc[1] = "lng";

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&placeid=" + placeId);

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return loc;
            //    return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return loc;
            //    return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());

            double lat;
            double lng;

            lat = jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            lng = jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lng");

            loc[0]= String.valueOf(lat);
            loc[1]= String.valueOf(lng);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return loc;
    }


    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    protected void onPause() {
        super.onPause();
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
        mMap.setBuildingsEnabled(true); //add

//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

//        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude())
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
        //        Toast.makeText(getApplicationContext(), "Location Changed!", Toast.LENGTH_SHORT).show();
                //    mMap.clear();
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                if(mMap != null){
                //    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }
            }
        };

        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

    }

    //Option Menu//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu___test_ride_app1_preparation, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu___test_ride_app1_preparation_start:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Intent i = new Intent (Activity2_MainMap.this,Activity3_App1Go.class);
                                startActivity(i);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Mulai Pengukuran?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener).show();

                return true;
            case R.id.menu___test_ride_app1_preparation_direction:

                flipper1.showNext();

                return true;
            case R.id.menu___test_ride_app1_preparation_logout:
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.resetTables();

                Intent i = new Intent (Activity2_MainMap.this, Activity1_Login.class);
                startActivity(i);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}