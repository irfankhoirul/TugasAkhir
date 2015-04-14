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
        if (s.startsWith("Show Road Test Results")) {
            imageView.setImageResource(R.drawable.ic_map);
        }else if(s.startsWith("Found Broken Road")){
            imageView.setImageResource(R.drawable.ic_camera);
        }else if(s.startsWith("Show Broken Road")) {
            imageView.setImageResource(R.drawable.ic_location);
        }else if(s.startsWith("Setting")) {
            imageView.setImageResource(R.drawable.ic_setting);
        }else if(s.startsWith("My Details")){
            imageView.setImageResource(R.drawable.ic_person);
        }else if(s.startsWith("Help")) {
            imageView.setImageResource(R.drawable.ic_help);
        }else if(s.startsWith("About")) {
            imageView.setImageResource(R.drawable.ic_action);
        }else {
            imageView.setImageResource(R.drawable.bangladesh);
        }

        return rowView;
    }
}