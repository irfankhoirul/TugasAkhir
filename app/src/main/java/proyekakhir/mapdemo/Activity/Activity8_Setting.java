package proyekakhir.mapdemo.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import proyekakhir.mapdemo.R;


public class Activity8_Setting extends Activity {

    LinearLayout p_lokasi, p_jarakNotifikasi, p_waktuPersiapan;
    CheckBox notifikasi;
    TextView jarakNotifikasi, waktuPersiapan;
    int temp_jarak, temp_waktu;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity8_setting);

        /// Drawer activity
//        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_frame);
//        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View activityView = layoutInflater.inflate(R.layout.activity8_setting, null,false);
//        frameLayout.addView(activityView);
        ///


        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();

        initializeComponents();

        p_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
            }

        });

        p_jarakNotifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                AlertDialog.Builder alert = new AlertDialog.Builder(Activity8_Setting.this);

                //    alert.setTitle("Title");
                alert.setMessage("Dalam Kilometer");

                final EditText input = new EditText(Activity8_Setting.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        jarakNotifikasi.setText(input.getText().toString()+" Kilometer");
                        temp_jarak = Integer.parseInt(input.getText().toString());
                        // Do something with value!
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }

        });

        p_waktuPersiapan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                AlertDialog.Builder alert = new AlertDialog.Builder(Activity8_Setting.this);

                //    alert.setTitle("Title");
                alert.setMessage("Dalam Detik");

                final EditText input = new EditText(Activity8_Setting.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        waktuPersiapan.setText(input.getText().toString()+" Detik");
                        temp_waktu = Integer.parseInt(input.getText().toString());
                        // Do something with value!
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }

        });

    }

    public void initializeComponents(){
        p_lokasi = (LinearLayout) findViewById(R.id.p_lokasi);
        p_jarakNotifikasi = (LinearLayout) findViewById(R.id.p_jarakNotifikasi);
        p_waktuPersiapan = (LinearLayout) findViewById(R.id.p_waktuPersiapan);
        notifikasi = (CheckBox) findViewById(R.id.notifikasi);
        waktuPersiapan = (TextView) findViewById(R.id.waktuPersiapan);
        jarakNotifikasi = (TextView) findViewById(R.id.jarakNotifikasi);

        //Get Shared Preference
        temp_jarak = pref.getInt("p_jarak", 0);
        temp_waktu = pref.getInt("p_waktu", 0);
        jarakNotifikasi.setText(pref.getInt("p_jarak", 0)+" KM");
        waktuPersiapan.setText(pref.getInt("p_waktu", 0)+" Detik");
        if(pref.getInt("p_notifikasi", 0)==1)
            notifikasi.setChecked(true);
        else
            notifikasi.setChecked(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu___setting, menu);
        return true;
    }


    @Override
    public void onBackPressed()
    {
        //Save Preference
        if(notifikasi.isChecked())
            editor.putInt("p_notifikasi", 1);
        else
            editor.putInt("p_notifikasi", 0);
        editor.putInt("p_jarak", temp_jarak);
        editor.putInt("p_waktu", temp_waktu);
        editor.commit();
        // code here to show dialog
        Intent intent = new Intent(Activity8_Setting.this, Activity2_MainMap.class);
        startActivity(intent);
        finish();
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
}
