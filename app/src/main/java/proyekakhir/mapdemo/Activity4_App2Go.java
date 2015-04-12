package proyekakhir.mapdemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Activity4_App2Go extends DrawerActivity {
    Button _act4_bt_takePicture, _act4_bt_sendToServer;
    TextView _act4_txt_lat, _act4_txt_lon;
    ImageView _act4_image;
    private static final int CAMERA_REQUEST = 1888;
    public static int count=0;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int camera_capture_image_request_code = 100;
    private static final int media_type_image = 1;
    private static final String image_directory_name = "SurveyoRider";
    private Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity4_app2go);

        /// Drawer activity
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity4_app2go, null,false);
        frameLayout.addView(activityView);
        ///

        _act4_image = (ImageView)findViewById(R.id._act4_image);

        //here,we are making a folder named picFolder to store pics taken by the camera using this application
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();

        Button capture = (Button) findViewById(R.id._act4_bt_takePicture);
        capture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(media_type_image);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, camera_capture_image_request_code);
            }
        });
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), image_directory_name);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(image_directory_name, "Oops! Failed create " + image_directory_name + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",  Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == media_type_image) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator  + "IMG_" + timeStamp + ".jpg");
        }
        else  return null;

        return mediaFile;
    }

    private void previewCapturedImage() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            String filePath = fileUri.getPath();

            Bitmap road = BitmapFactory.decodeFile(filePath, options);
        //    wajah = ImageEdit.resizeLogo(wajah);
        //    Matrix matrix = new Matrix();
        //    matrix.postRotate(90);
        //    wajah = Bitmap.createBitmap(wajah , 0, 0, wajah.getWidth(), wajah.getHeight(), matrix, true);

            _act4_image.setImageBitmap(road);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == camera_capture_image_request_code) {
            if (resultCode == RESULT_OK) {
                previewCapturedImage();
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "You've cancelled image capture", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            _act4_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Bitmap road = BitmapFactory.decodeFile(picturePath);

            Matrix matrix = new Matrix();
            road = Bitmap.createBitmap(road , 0, 0, road.getWidth(), road.getHeight(), matrix, true);
            _act4_image.setImageBitmap(road);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity4__app2_go, menu);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        Intent intent = new Intent(Activity4_App2Go.this, Activity2_MainMap.class);
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
