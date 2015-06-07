package proyekakhir.mapdemo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Activity6d_PotholeDetails extends ActionBarActivity {

    private GoogleMap mMap;
    public String fullAddress = "";
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity6a_app1_result_map_details);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#536DFE")));

        Intent intent = getIntent();
        lat = Double.parseDouble(intent.getStringExtra("lat"));
        lon = Double.parseDouble(intent.getStringExtra("lon"));

        setUpMapIfNeeded();

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title("Q2")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_light_blue)));
        LatLng loc= new LatLng(lat, lon);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
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
                if(mMap != null){
                    //    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }
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

}
