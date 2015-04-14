package proyekakhir.mapdemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;


public class DrawerActivity extends ActionBarActivity implements AdapterViewCompat.OnItemClickListener {
    private DrawerLayout mDrawerLayout = null;
    private ListView mDrawerList = null;
    private String[] mDrawerItems;
    private ActionBarDrawerToggle mDrawerToggle = null;

    private LinearLayout mDrawer ;

    private LinearLayout mDrawerRelativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerItems = getResources().getStringArray(R.array.left_drawer_array);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
    //    mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, prgmNameList));
        mDrawerList.setAdapter(new MySimpleArrayAdapter(this, mDrawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, R.drawable.ic_drawer,
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
                case 0: {
                    Intent intent = new Intent(DrawerActivity.this, Activity6_App1ResultMap.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 1: {
                    Intent intent = new Intent(DrawerActivity.this, Activity4_App2Go.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 2: {
                    Intent intent = new Intent(DrawerActivity.this, Activity7_App2ResultMap.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 3: {
                    Intent intent = new Intent(DrawerActivity.this, Activity8_Setting.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 4: {
                    Intent intent = new Intent(DrawerActivity.this, Activity9_UserDetails.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 5: {
                    Intent intent = new Intent(DrawerActivity.this, Activity10_About.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 6: {

                    break;
                }
                default:
                    break;
            }
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}