package proyekakhir.mapdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.melnykov.fab.FloatingActionButton;

import proyekakhir.mapdemo.library.StaticPref;

public class Activity2_MainMap extends DrawerActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    int umumKhusus = 0;
    //----MAP----//
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    LatLng loc;
    FloatingActionButton actionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity2_mainmap);

        /// Drawer activity
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity2_mainmap, null, false);
        frameLayout.addView(activityView);
        ///
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#689F38")));

        final FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.fab);
        actionButton.setVisibility(View.INVISIBLE);
        actionButton.hide();

        setUpMapIfNeeded();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        StaticPref.setShowNotif(pref.getInt("p_notifikasi", 0));
        StaticPref.setJarak(pref.getInt("p_jarak", 0));

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                actionButton.setVisibility(View.VISIBLE);
                actionButton.show();
            }
        }, 1000);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Pengukuran Baru");
                builder.setMessage(
                        "Mulai Pengukuran?")
                        .setCancelable(false)
                        .setPositiveButton("Ya",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
                                        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                                            Toast.makeText(getApplicationContext(), "Please enable GPS your connection!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            if(umumKhusus == 1 ) {
                                                Intent i = new Intent(Activity2_MainMap.this, Activity3_App1Go.class);
                                                i.putExtra("Jenis", umumKhusus);
                                                startActivity(i);
                                                finish();
                                            }
                                            else if(umumKhusus == 2){
                                                Intent i = new Intent(Activity2_MainMap.this, Activity4_DiscoverPothole.class);
                                                i.putExtra("Jenis", umumKhusus);
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog dialog = builder.create();
                return dialog;

            case 2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Jenis Pengukuran");
                builder2.setMessage(
                        "Tentukan Jenis Pengukuran");
                builder2.setCancelable(false);
                builder2.setPositiveButton("Umum",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                umumKhusus = 1;
                                Activity2_MainMap.this.showDialog(1);
                            }
                        });
                builder2.setNegativeButton("Khusus",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                umumKhusus = 2;
                                Activity2_MainMap.this.showDialog(1);
                            }
                        });
                AlertDialog dialog2 = builder2.create();
                return dialog2;
        }
        return null;
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
//        mMap.setBuildingsEnabled(true); //add
//        mMap.getUiSettings().setZoomControlsEnabled(true);

//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                loc = new LatLng(location.getLatitude(), location.getLongitude());
                if(mMap != null){

                }
        //        editor.putString("p_lastLat", Double.toString(location.getLatitude()));
        //        editor.putString("p_lastLon", Double.toString(location.getLongitude()));
                StaticPref.setLast_lat(location.getLatitude());
                StaticPref.setLast_lon(location.getLongitude());
        //        editor.commit();
            }
        };

        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu___test_ride_app1_preparation, menu);
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