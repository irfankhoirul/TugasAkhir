package proyekakhir.mapdemo;

/**
 * Created by Irfan on 19/05/2015.
 */
import java.util.ArrayList;
import java.util.List;

public class Group {

    public String string;
    public final List<String> namaJalan = new ArrayList<String>();
    public final List<String> alamatJalan = new ArrayList<String>();
    public final List<String> kondisiJalan = new ArrayList<String>();
    public final List<String> nilaiKondisi = new ArrayList<>();

    public Group(String string) {
        this.string = string;
    }

}