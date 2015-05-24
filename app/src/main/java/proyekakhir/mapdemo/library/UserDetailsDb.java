package proyekakhir.mapdemo.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by Irfan on 22/04/2015.
 */
public class UserDetailsDb extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "surveyoRiderDb";

    // Login table name
    private static final String TABLE_USER_DETAILS = "user_details";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NAMA_AWAL = "nama_awal";
    private static final String KEY_NAMA_BELAKANG = "nama_belakang";
    private static final String KEY_JENIS_USER = "jenis_user";
    private static final String KEY_MERK_SMARTPHONE = "merk_smartphone";
    private static final String KEY_TIPE_SMARTPHONE = "tipe_smartphone";
    private static final String KEY_MERK_MOTOR = "merk_motor";
    private static final String KEY_TIPE_MOTOR = "tipe_motor";
    private static final String KEY_EMAIL = "email";

    public UserDetailsDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_DETAILS_TABLE =
                "CREATE TABLE " + TABLE_USER_DETAILS + "("
                        + KEY_ID + " INTEGER PRIMARY KEY,"
                        + KEY_USERNAME + " TEXT,"
                        + KEY_USER_ID + " TEXT,"
                        + KEY_NAMA_AWAL + " TEXT,"
                        + KEY_NAMA_BELAKANG + " TEXT,"
                        + KEY_JENIS_USER + " TEXT,"
                        + KEY_MERK_SMARTPHONE + " TEXT,"
                        + KEY_TIPE_SMARTPHONE + " TEXT,"
                        + KEY_MERK_MOTOR + " TEXT,"
                        + KEY_TIPE_MOTOR + " TEXT,"
                        + KEY_EMAIL + " TEXT" + ")";
        db.execSQL(CREATE_USER_DETAILS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAILS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String username, String user_id, String nama_awal, String nama_belakang,
                        String jenis_user, String merk_smartphone, String tipe_smartphone, String merk_motor,
                        String tipe_motor, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_USERNAME, username);
        values.put(KEY_USER_ID, user_id);
        values.put(KEY_NAMA_AWAL, nama_awal);
        values.put(KEY_NAMA_BELAKANG, nama_belakang);
        values.put(KEY_JENIS_USER, "null");
        values.put(KEY_MERK_SMARTPHONE, merk_smartphone);
        values.put(KEY_TIPE_SMARTPHONE, tipe_smartphone);
        values.put(KEY_MERK_MOTOR, merk_motor);
        values.put(KEY_TIPE_MOTOR, tipe_motor);
        values.put(KEY_EMAIL, email);

        // Inserting Row
        db.insert(TABLE_USER_DETAILS, null, values);
        db.close(); // Clode db
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_DETAILS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("username", cursor.getString(1));
            user.put("user_id", cursor.getString(2));
            user.put("nama_awal", cursor.getString(3));
            user.put("nama_belakang", cursor.getString(4));
            user.put("jenis_user", cursor.getString(5));
            user.put("merk_smartphone", cursor.getString(6));
            user.put("tipe_smartphone", cursor.getString(7));
            user.put("merk_motor", cursor.getString(8));
            user.put("tipe_motor", cursor.getString(9));
            user.put("email", cursor.getString(10));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER_DETAILS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER_DETAILS, null, null);
        db.close();
    }
}