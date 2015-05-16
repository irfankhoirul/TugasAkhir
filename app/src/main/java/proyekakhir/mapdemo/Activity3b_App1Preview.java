package proyekakhir.mapdemo;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Activity3b_App1Preview extends ActionBarActivity {

    //----MAP----//
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    double lat_prev[], lon_prev[];
    int qual_prev[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3b_app1_preview);

        //----MAP----//
        setUpMapIfNeeded();

        getPreviewIntent();

    }

    public void getPreviewIntent(){
        Intent intent = getIntent();
        qual_prev = intent.getIntArrayExtra("qual-prev");
        lat_prev = intent.getDoubleArrayExtra("lat-prev");
        lon_prev = intent.getDoubleArrayExtra("lon-prev");

        showMarker();
    }

    public void showMarker(){
        for(int i = 0; i<qual_prev.length; i++){
            if(qual_prev[i] == 1) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat_prev[i], lon_prev[i]))
                        .title("Q1")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_blue)));
            }
            else if(qual_prev[i] == 2) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat_prev[i], lon_prev[i]))
                        .title("Q2")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_light_blue)));
            }
            else if(qual_prev[i] == 3) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat_prev[i], lon_prev[i]))
                        .title("Q3")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_lime)));
            }
            else if(qual_prev[i] == 4) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat_prev[i], lon_prev[i]))
                        .title("Q4")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_yellow)));
            }
            else if(qual_prev[i] == 5) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat_prev[i], lon_prev[i]))
                        .title("Q5")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange)));
            }
            else if(qual_prev[i] == 6) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat_prev[i], lon_prev[i]))
                        .title("Q6")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_red)));
            }
        }
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}