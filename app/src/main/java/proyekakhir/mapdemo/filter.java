package proyekakhir.mapdemo;

/**
 * Created by Irfan on 01/06/2015.
 */
public class filter {
    String nama_jalan;
    String kec;
    String kota;
    String prov;
    String kualitas;
    String merk_smartphone;
    String tipe_smartphone;
    String merk_motor;
    String tipe_motor;
    String date_start;
    String date_end;

    public filter(String nama_jalan, String kec, String kota, String prov, String kualitas,
                  String merk_smartphone, String tipe_smartphone, String merk_motor,
                  String tipe_motor, String date_start, String date_end) {
        this.nama_jalan = nama_jalan;
        this.kec = kec;
        this.kota = kota;
        this.prov = prov;
        this.kualitas = kualitas;
        this.merk_smartphone = merk_smartphone;
        this.tipe_smartphone = tipe_smartphone;
        this.merk_motor = merk_motor;
        this.tipe_motor = tipe_motor;
        this.date_start = date_start;
        this.date_end = date_end;
    }

    public String getNama_jalan() {
        return nama_jalan;
    }

    public void setNama_jalan(String nama_jalan) {
        this.nama_jalan = nama_jalan;
    }

    public String getKec() {
        return kec;
    }

    public void setKec(String kec) {
        this.kec = kec;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getKualitas() {
        return kualitas;
    }

    public void setKualitas(String kualitas) {
        this.kualitas = kualitas;
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

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }
}
