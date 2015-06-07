package proyekakhir.mapdemo;

import android.app.Activity;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Irfan on 07/06/2015.
 */
public class MyExpandableListAdapter_LubangJalan extends BaseExpandableListAdapter {
    private final SparseArray<Group2> groups;
    public LayoutInflater inflater;
    public Activity activity;

    public MyExpandableListAdapter_LubangJalan(Activity act, SparseArray<Group2> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).namaJalan.get(childPosition);
    }

    public Object getNamaJalan(int groupPosition, int childPosition) {
        return groups.get(groupPosition).namaJalan.get(childPosition);
    }

    public Object getKec(int groupPosition, int childPosition) {
        return groups.get(groupPosition).kec.get(childPosition);
    }
    public Object getKota(int groupPosition, int childPosition) {
        return groups.get(groupPosition).kota.get(childPosition);
    }
    public Object getProv(int groupPosition, int childPosition) {
        return groups.get(groupPosition).prov.get(childPosition);
    }

    public Object getTanggal(int groupPosition, int childPosition) {
        return groups.get(groupPosition).tanggal.get(childPosition);
    }
    public Object getLat(int groupPosition, int childPosition) {
        return groups.get(groupPosition).lat.get(childPosition);
    }
    public Object getLon(int groupPosition, int childPosition) {
        return groups.get(groupPosition).lon.get(childPosition);
    }

//    public Object getNilaiKondisi(int groupPosition, int childPosition) {
//        return groups.get(groupPosition).nilaiKondisi.get(childPosition);
//    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String namaJalan = (String) getNamaJalan(groupPosition, childPosition);
        final String kec = (String) getKec(groupPosition, childPosition);
        final String kota = (String) getKota(groupPosition, childPosition);
        final String prov = (String) getProv(groupPosition, childPosition);
        final String tanggal = (String) getTanggal(groupPosition, childPosition);
        final String lat = (String) getLat(groupPosition, childPosition);
        final String lon = (String) getLon(groupPosition, childPosition);
//        final String nilaiKondisi = (String) getNilaiKondisi(groupPosition, childPosition);

        TextView nama_jalan = null;
        TextView alamat = null;
        TextView kondisi = null;
        ImageView imageView_kualitas;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_details, null);
        }
        imageView_kualitas = (ImageView) convertView.findViewById(R.id.imageView_kualitas);
        imageView_kualitas.setImageResource(R.drawable.ic_location);
//        if(nilaiKondisi.equalsIgnoreCase("1")){
//            imageView_kualitas.setImageResource(R.drawable.ic_marker_blue);
//        }
//        else if(nilaiKondisi.equalsIgnoreCase("2")){
//            imageView_kualitas.setImageResource(R.drawable.ic_marker_orange);
//        }
//        else {
//            imageView_kualitas.setImageResource(R.drawable.ic_marker_red);
//        }

        alamat = (TextView) convertView.findViewById(R.id.alamat);
        alamat.setText(kec+", "+kota+", "+prov);

        kondisi = (TextView) convertView.findViewById(R.id.kondisi);
        kondisi.setText(tanggal);

        nama_jalan = (TextView) convertView.findViewById(R.id.nama_jalan);
        nama_jalan.setText(namaJalan);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Toast.makeText(activity, namaJalan + ", " + kec + ", " + kota + ", " + prov, Toast.LENGTH_SHORT).show();
            //    Toast.makeText(activity, lat+" : "+lon, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(activity, Activity6d_PotholeDetails.class);
                i.putExtra("lat", lat);
                i.putExtra("lon", lon);
                activity.startActivity(i);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).namaJalan.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        Group2 group2 = (Group2) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group2.string);
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
