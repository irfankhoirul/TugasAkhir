package proyekakhir.mapdemo;

/**
 * Created by Irfan on 16/03/2015.
 */
public class User {
    private String nama;
    private String email;
    private String username;
//    private String password;
    private String merkKendaraan;
    private String tipeKendaraan;
    private String merkDevice;
    private String tipeDevice;
//    private String kondisiKendaraan;


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMerkKendaraan() {
        return merkKendaraan;
    }

    public void setMerkKendaraan(String merkKendaraan) {
        this.merkKendaraan = merkKendaraan;
    }

    public String getTipeKendaraan() {
        return tipeKendaraan;
    }

    public void setTipeKendaraan(String tipeKendaraan) {
        this.tipeKendaraan = tipeKendaraan;
    }

    public String getMerkDevice() {
        return merkDevice;
    }

    public void setMerkDevice(String merkDevice) {
        this.merkDevice = merkDevice;
    }

    public String getTipeDevice() {
        return tipeDevice;
    }

    public void setTipeDevice(String tipeDevice) {
        this.tipeDevice = tipeDevice;
    }
}
