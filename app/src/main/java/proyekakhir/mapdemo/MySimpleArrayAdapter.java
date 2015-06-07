package proyekakhir.mapdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MySimpleArrayAdapter(Context context, String[] values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);


        // Change the icon for Windows and iPhone
        String s = values[position];
        if (s.startsWith("Informasi Kualitas Jalan")) {
            imageView.setImageResource(R.drawable.ic_map);
        }else if(s.startsWith("Informasi Kerusakan")) {
            imageView.setImageResource(R.drawable.ic_location);
        }else if(s.startsWith("Pengaturan")) {
            imageView.setImageResource(R.drawable.ic_setting);
        }else if(s.startsWith("Personal")){
            imageView.setImageResource(R.drawable.ic_accounts);
        }else if(s.startsWith("Bantuan")) {
            imageView.setImageResource(R.drawable.ic_help);
        }else if(s.startsWith("Tentang")) {
            imageView.setImageResource(R.drawable.ic_action);
        }else if(s.startsWith("Keluar")) {
            imageView.setImageResource(R.drawable.ic_logout);
        }else {
            imageView.setImageResource(R.drawable.bangladesh);
        }

        return rowView;
    }

}