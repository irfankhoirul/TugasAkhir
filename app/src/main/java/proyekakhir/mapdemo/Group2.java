package proyekakhir.mapdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan on 07/06/2015.
 */
public class Group2 {
    public String string;
    public final List<String> namaJalan = new ArrayList<String>();
    public final List<String> kec = new ArrayList<String>();
    public final List<String> kota = new ArrayList<String>();
    public final List<String> prov = new ArrayList<String>();
    public final List<String> tanggal = new ArrayList<String>();
    public final List<String> lat = new ArrayList<String>();
    public final List<String> lon = new ArrayList<String>();

    public Group2(String string) {
        this.string = string;
    }
}
