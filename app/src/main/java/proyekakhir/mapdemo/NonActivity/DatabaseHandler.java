package proyekakhir.mapdemo.NonActivity;

/**
 * Created by Raj Amal on 5/30/13.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "surveyoRiderDb";

    // Login table name
    private static final String TABLE_LOGIN = "login";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_NAMA_AWAL = "namaAwal";
    private static final String KEY_NAMA_BELAKANG = "namaBelakang";
    private static final String KEY_JENIS_USER = "jenisUser";
    private static final String KEY_MERK_SMARTPHONE = "merkSmartphone";
    private static final String KEY_TIPE_SMARTPHONE = "tipeSmartphone";
    private static final String KEY_MERK_MOTOR = "merkMotor";
    private static final String KEY_TIPE_MOTOR = "tipeMotor";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_VERIFIED = "verified";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
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
                + KEY_EMAIL + " TEXT,"
                + KEY_VERIFIED + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }

    // Add
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_USER_ID, user.getUser_id());
        values.put(KEY_NAMA_AWAL, user.getNama_awal());
        values.put(KEY_NAMA_BELAKANG, user.getNama_belakang());
        values.put(KEY_JENIS_USER, user.getJenis_user());
        values.put(KEY_MERK_SMARTPHONE, user.getMerk_smartphone());
        values.put(KEY_TIPE_SMARTPHONE, user.getTipe_smartphone());
        values.put(KEY_MERK_MOTOR, user.getMerk_motor());
        values.put(KEY_TIPE_MOTOR, user.getTipe_motor());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_VERIFIED, user.getVerified());
        Log.d("Inserting user",
                "[Username : "+user.getUsername()+
                        "][User Id : "+user.getUser_id()+
                        "][Nama Awal : "+user.getNama_awal()+
                        "][Nama Belakang : "+user.getNama_belakang()+
                        "][Jenis User : "+user.getJenis_user()+
                        "][Merk Smartphone : "+user.getMerk_smartphone()+
                        "][Tipe Smartphone : "+user.getTipe_smartphone()+
                        "][Merk Motor : "+user.getMerk_motor()+
                        "][Tipe Motor : "+user.getTipe_motor()+
                        "][Email : "+user.getEmail()+
                        "][Verified : "+user.getVerified()+"]"
        );

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }

    // Getting user
    public User getUser() {
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOGIN, new String[] {
                        KEY_ID,
                        KEY_USERNAME,
                        KEY_USER_ID,
                        KEY_NAMA_AWAL,
                        KEY_NAMA_BELAKANG,
                        KEY_JENIS_USER,
                        KEY_MERK_SMARTPHONE,
                        KEY_TIPE_SMARTPHONE,
                        KEY_MERK_MOTOR,
                        KEY_TIPE_MOTOR,
                        KEY_EMAIL,
                        KEY_VERIFIED }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11));

        return user;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
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
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

}