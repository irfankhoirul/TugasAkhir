package proyekakhir.mapdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import proyekakhir.mapdemo.library.DatabaseHandler;

public class Activity9_UserDetails extends DrawerActivity {

    Button _act9_bt_show;
    TextView _act9_txt_username, _act9_txt_userId, _act9_txt_namaAwal, _act9_txt_namaBelakang,
            _act9_txt_jenisUser, _act9_txt_merkSmartphone, _act9_txt_tipeSmartphone,
            _act9_txt_merkMotor, _act9_txt_tipeMotor, _act9_txt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity9_userdetails);

        /// Drawer activity
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity9_userdetails, null,false);
        frameLayout.addView(activityView);
        ///

        _act9_txt_username = (TextView)findViewById(R.id._act9_txt_username);
        _act9_txt_userId = (TextView)findViewById(R.id._act9_txt_userId);
        _act9_txt_namaAwal = (TextView)findViewById(R.id._act9_txt_namaAwal);
        _act9_txt_namaBelakang = (TextView)findViewById(R.id._act9_txt_namaBelakang);
        _act9_txt_jenisUser = (TextView)findViewById(R.id._act9_txt_jenisUser);
        _act9_txt_merkSmartphone = (TextView)findViewById(R.id._act9_txt_merkSmartphone);
        _act9_txt_tipeSmartphone = (TextView)findViewById(R.id._act9_txt_tipeSmartphone);
        _act9_txt_merkMotor = (TextView)findViewById(R.id._act9_txt_merkMotor);
        _act9_txt_tipeMotor = (TextView)findViewById(R.id._act9_txt_tipeMotor);
        _act9_txt_email = (TextView)findViewById(R.id._act9_txt_email);


        try {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            HashMap<String, String> user = db.getUserDetails();


        //    _act9_txt_username.append((user.get("username")));
        //    _act9_txt_userId.append((user.get("user_id")));
        //    _act9_txt_namaAwal.append((user.get("nama_awal")));
        //    _act9_txt_namaBelakang.append((user.get("nama_belakang")));
        //    _act9_txt_jenisUser.append((user.get("jenis_user")));
        //    _act9_txt_merkSmartphone.append((user.get("merk_smartphone")));
        //    _act9_txt_tipeSmartphone.append((user.get("tipe_smartphone")));
        //    _act9_txt_merkMotor.append((user.get("merk_motor")));
        //    _act9_txt_tipeMotor.append((user.get("tipe_motor")));
        //    _act9_txt_email.append((user.get("email")));
        }
        catch(Exception ex)
        {
            Toast.makeText(getBaseContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity9__user_details, menu);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent = new Intent(Activity9_UserDetails.this, Activity2_MainMap.class);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
