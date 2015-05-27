package proyekakhir.mapdemo;

/**
 * Created by Irfan on 25/04/2015.
 */
public class User {
    //private variables
    String username, user_id, nama_awal, nama_belakang, jenis_user, merk_smartphone, tipe_smartphone,
            merk_motor, tipe_motor, email, verified;
    // Empty constructor
    public User(){

    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    // constructor
    public User(String username, String user_id, String nama_awal, String nama_belakang,
                String jenis_user, String merk_smartphone, String tipe_smartphone, String merk_motor,
                String tipe_motor, String email, String verified){
        this.username = username;
        this.user_id = user_id;

        this.nama_awal = nama_awal;
        this.nama_belakang = nama_belakang;
        this.jenis_user = jenis_user;
        this.merk_smartphone = merk_smartphone;
        this.tipe_smartphone = tipe_smartphone;
        this.merk_motor = merk_motor;
        this.tipe_motor = tipe_motor;
        this.email = email;
        this.verified = verified;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNama_awal() {
        return nama_awal;
    }

    public void setNama_awal(String nama_awal) {
        this.nama_awal = nama_awal;
    }

    public String getNama_belakang() {
        return nama_belakang;
    }

    public void setNama_belakang(String nama_belakang) {
        this.nama_belakang = nama_belakang;
    }

    public String getJenis_user() {
        return jenis_user;
    }

    public void setJenis_user(String jenis_user) {
        this.jenis_user = jenis_user;
    }

    public String getMerk_smartphone() {
        return merk_smartphone;
    }

    public void setMerk_smartphone(String merk_smartphone) {
        this.merk_smartphone = merk_smartphone;
    }

    public String getTipe_smartphone() {
        return tipe_smartphone;
    }

    public void setTipe_smartphone(String tipe_smartphone) {
        this.tipe_smartphone = tipe_smartphone;
    }

    public String getMerk_motor() {
        return merk_motor;
    }

    public void setMerk_motor(String merk_motor) {
        this.merk_motor = merk_motor;
    }

    public String getTipe_motor() {
        return tipe_motor;
    }

    public void setTipe_motor(String tipe_motor) {
        this.tipe_motor = tipe_motor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
