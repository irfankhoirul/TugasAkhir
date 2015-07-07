package proyekakhir.mapdemo.Activity.KualitasJalan;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import proyekakhir.mapdemo.R;


public class Activity3b_KualitasJalan_HasilPengukuran_Preview extends AppCompatActivity {

    //----MAP----//
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    double lat_prev[], lon_prev[];
    int qual_prev[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3b_app1_preview);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF5722")));

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
                        .title("Baik")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_blue)));
            }
            else if(qual_prev[i] == 2) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat_prev[i], lon_prev[i]))
                        .title("Sedang")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_orange)));
            }
            else if(qual_prev[i] == 3) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat_prev[i], lon_prev[i]))
                        .title("Buruk")
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
        mMap.getUiSettings().setZoomControlsEnabled(true);

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
        inflater.inflate(R.menu.menu_activity3b__app1_preview, menu);

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