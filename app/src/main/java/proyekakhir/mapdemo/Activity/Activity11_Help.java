package proyekakhir.mapdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import proyekakhir.mapdemo.DrawerActivity;
import proyekakhir.mapdemo.R;


public class Activity11_Help extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_activity11__help);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFA000")));

        /// Drawer activity
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_activity11__help, null, false);
        frameLayout.addView(activityView);
        ///

        String htmlText = "<html><body style=\"text-align:justify\"> %s </body></Html>";

        String Skenario1 = "Anda akan mengukur getaran yang diterima sepeda motor menggunakan smartphone dan akan diklasifikasikan menjadi 3 jenis yaitu baik, sedang, dan buruk berdasarkan histogram dari setiap 1 detik pengambilan data.";
        WebView webView1 = (WebView) findViewById(R.id.Skenario1);
        webView1.loadData(String.format(htmlText, Skenario1), "text/html", "utf-8");

        String Skenario2 = "Anda akan mengukur getaran yang diterima sepeda motor, dimana hanya getaran dengan nilai yang menandakan jalan rusak saja yang akan diproses.";
        WebView webView2 = (WebView) findViewById(R.id.Skenario2);
        webView2.loadData(String.format(htmlText, Skenario2), "text/html", "utf-8");

        String Skenario3a = "Untuk memulai, tap Action Button dengan icon SurveyoRider pada bagian kanan bawah. Kemudian tentukan skenario pengukuran yang akan dilakukan.";
        WebView webView3a = (WebView) findViewById(R.id.Skenario3a);
        webView3a.loadData(String.format(htmlText, Skenario3a), "text/html", "utf-8");

        String Skenario3b = "Pasang smartphone pada holder yang terpasang pada motor. Anda dapat memposisikan holder pada bagian manapun pada motor, namun pastikan smartphone dalam posisi vertikal ketika terpasang pada holder. Kemudian aplikasi akan menghitung mundur sesuai pengaturan yang anda tentukan, lalu pengukuran akan dimulai.";
        WebView webView3b = (WebView) findViewById(R.id.Skenario3b);
        webView3b.loadData(String.format(htmlText, Skenario3b), "text/html", "utf-8");

        String Skenario3c = "Perlu diingat, pengambilan data hanya dapat dilakukan ketika koneksi internet tersedia, akurasi GPS di bawah 50 meter, posisi smartphone vertikal (sumbu Y accelerometer tidak bernilai di bawah 9.3 m/s2 pada kondisi motor berhenti), dan anda memacu kendaraan dengan kecepatan lebih dari 10 KM/Jam.";
        WebView webView3c = (WebView) findViewById(R.id.Skenario3c);
        webView3c.loadData(String.format(htmlText, Skenario3c), "text/html", "utf-8");

        String Skenario4 = "Untuk pengakhiri pengukuran, tap Action Button stop pada bagian kanan bawah. Lalu aplikasi akan menampilkan hasil pengukuran yang telah anda lakukan.";
        WebView webView4 = (WebView) findViewById(R.id.Skenario4);
        webView4.loadData(String.format(htmlText, Skenario4), "text/html", "utf-8");

        String Skenario5a = "Setelah anda melakukan pengukuran, anda dapat mengirimkan hasil pengukuran anda ke server. Namun tidak ada paksaan bagi anda jika tidak berkeinginan untuk mengirimkannya. Tetapi, akan lebih baik jika anda mengirimkannya sehingga anda dapat berperan dalam monitoring kualitas jalan.";
        WebView webView5a = (WebView) findViewById(R.id.Skenario5a);
        webView5a.loadData(String.format(htmlText, Skenario5a), "text/html", "utf-8");

        String Skenario5b = "Ketika anda mengirimkan hasil skenario pencarian jalan rusak, server akan membroadcast hasil pengukuran anda ke user lain yang menginginkan, sehingga anda dapat membantu mereka dalam memberi peringatan akan kerusakan jalan yang mungkin bisa membahayakan.";
        WebView webView5b = (WebView) findViewById(R.id.Skenario5b);
        webView5b.loadData(String.format(htmlText, Skenario5b), "text/html", "utf-8");

        String Skenario6 = "Anda dapat melihat keseluruhan hasil pengukuran kualitas jalan yang dilakukan oleh setiap user pada menu Informasi Kualitas Jalan. Anda juga dapat melihat informasi spesifik dari pengukuran yang telah dilakukan.";
        WebView webView6 = (WebView) findViewById(R.id.Skenario6);
        webView6.loadData(String.format(htmlText, Skenario6), "text/html", "utf-8");

        String Skenario7 = "Anda dapat melihat keseluruhan hasil pencarian jalan rusak yang dilakukan oleh setiap user pada menu Informasi Kerusakan Jalan. Anda juga dapat melihat informasi spesifik dari pengukuran yang telah dilakukan.";
        WebView webView7 = (WebView) findViewById(R.id.Skenario7);
        webView7.loadData(String.format(htmlText, Skenario7), "text/html", "utf-8");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity11__help, menu);
        return true;
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

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent = new Intent(Activity11_Help.this, Activity2_MainMap.class);
        startActivity(intent);
        finish();
    }
}
