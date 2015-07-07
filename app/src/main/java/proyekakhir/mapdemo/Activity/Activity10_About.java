package proyekakhir.mapdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import proyekakhir.mapdemo.DrawerActivity;
import proyekakhir.mapdemo.R;


public class Activity10_About extends DrawerActivity {

    ImageView im1, im2, im3, im4, im5, im6, im7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity10_about);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0097A7")));

        /// Drawer activity
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity10_about, null, false);
        frameLayout.addView(activityView);
        ///

        im1 = (ImageView) findViewById(R.id.imageView4); //Facebook
        im1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/irfan.khoirul.muhlishin"));
                startActivity(intent);
            }
        });
        im2 = (ImageView) findViewById(R.id.imageView5); //Gplus
        im2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://plus.google.com/103958664672541534270"));
                startActivity(intent);
            }
        });
        im3 = (ImageView) findViewById(R.id.imageView6); //Twitter
        im3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://twitter.com/muhlish_in"));
                startActivity(intent);
            }
        });
        im4 = (ImageView) findViewById(R.id.imageView7); //LinkedIn
        im4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.linkedin.com/pub/irfan-khoirul-muhlishin/78/7b/789"));
                startActivity(intent);
            }
        });
        im5 = (ImageView) findViewById(R.id.imageView8); //Github
        im5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/irfankhoirul"));
                startActivity(intent);
            }
        });
        im6 = (ImageView) findViewById(R.id.imageView10); //Gmail
        im6.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.fromParts("mailto","irfankhoirul@gmail.com", null));
                startActivity(i);
            }
        });
        im7 = (ImageView) findViewById(R.id.imageView9); //StackOverflow
        im7.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://stackoverflow.com/users/3081315/irfan-khoirul"));
                startActivity(intent);
            }
        });


        String htmlText = "<html><body style=\"text-align:justify\"> %s </body></Html>";

        String Skenario1 = "SurveyoRider adalah aplikasi pengukuran kualitas jalan dengan prinsip " +
                "memanfaatkan sensor accelerometer yang tertanam dalam smartphone untuk mengukur " +
                "getaran yang diterima sepeda motor. Aplikasi ini dikembangkan sebagai tugas akhir " +
                "untuk menyelesaikan studi D3 Teknik Informatika di Politeknik Elektronika Negeri " +
                "Surabaya. Jika Anda berminat untuk berkontribusi dan berkolaborasi untuk " +
                "mengembangkan aplikasi ini, silahkan hubungi melalui alamat email " +
                "irfankhoirul@gmail.com atau media di bawah ini.";
        WebView webView1 = (WebView) findViewById(R.id.webView);
        webView1.loadData(String.format(htmlText, Skenario1), "text/html", "utf-8");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity10__about, menu);
        return true;
    }


    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent = new Intent(Activity10_About.this, Activity2_MainMap.class);
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
