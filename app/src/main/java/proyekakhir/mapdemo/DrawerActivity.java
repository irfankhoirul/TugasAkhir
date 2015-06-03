package proyekakhir.mapdemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import proyekakhir.mapdemo.library.DatabaseHandler;


public class DrawerActivity extends AppCompatActivity implements AdapterViewCompat.OnItemClickListener {
    private DrawerLayout mDrawerLayout = null;
    private ListView mDrawerList = null;
    private String[] mDrawerItems;
    private ActionBarDrawerToggle mDrawerToggle = null;

    private LinearLayout mDrawer ;
    String Username, Email;

    private LinearLayout mDrawerRelativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        try {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            User user = db.getUser();
            Username = user.getUsername();
            Email = user.getEmail();


        }
        catch(Exception ex)
        {
            Toast.makeText(getBaseContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerItems = getResources().getStringArray(R.array.left_drawer_array);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new MySimpleArrayAdapter(this, mDrawerItems));
        View header = getLayoutInflater().inflate(R.layout.drawer_header, null);
        TextView u = (TextView) header.findViewById(R.id.drawer_username);
        u.setText(Username);
        TextView e = (TextView) header.findViewById(R.id.drawer_email);
        e.setText(Email);

        mDrawerList.addHeaderView(header);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View view) {
                invalidateOptionsMenu();
            }
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

        for (int index = 0; index < menu.size(); index++) {
            MenuItem menuItem = menu.getItem(index);
            if (menuItem != null) {
// hide the menu items if the drawer is open
                menuItem.setVisible(!drawerOpen);
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterViewCompat<?> adapterViewCompat, View view, int i, long l) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            switch (position) {
                case 1: {
                    Intent intent = new Intent(DrawerActivity.this, Activity6_App1ResultMap.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 2: {
                    Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(viewIntent);
                    break;
                }
                case 3: {
                    Intent intent = new Intent(DrawerActivity.this, Activity9_UserDetails.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 4: {
                    Intent intent = new Intent(DrawerActivity.this, Activity10_About.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 5: {
                    Intent intent = new Intent(DrawerActivity.this, Activity11_Help.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 6:{

                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    db.resetTables();

                    Intent i = new Intent (DrawerActivity.this, Activity1_Login.class);
                    startActivity(i);
                    finish();
                    break;
                }
                default:
                    break;
            }
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}