package proyekakhir.mapdemo.NonActivity;

/**
 * Created by Irfan on 17/06/2015.
 */
public class StaticPref {
    public static int showNotif, jarak;
    public static double last_lat, last_lon;

    public static int getJarak() {
        return jarak;
    }

    public static void setJarak(int jarak) {
        StaticPref.jarak = jarak;
    }

    public static int getShowNotif() {
        return showNotif;
    }

    public static void setShowNotif(int showNotif) {
        StaticPref.showNotif = showNotif;
    }

    public static double getLast_lat() {
        return last_lat;
    }

    public static void setLast_lat(double last_lat) {
        StaticPref.last_lat = last_lat;
    }

    public static double getLast_lon() {
        return last_lon;
    }

    public static void setLast_lon(double last_lon) {
        StaticPref.last_lon = last_lon;
    }
}
