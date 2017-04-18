package trioidea.iciciappathon.com.trioidea.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by Harshal on 18-Apr-17.
 */
public class CustomReceiverAdapter extends ArrayAdapter<String> {

    private ArrayList deviceName;
    private ArrayList deviceId;
    private Activity context;

    public CustomReceiverAdapter(Activity context, ArrayList name, ArrayList id) {
        super(context, R.layout.listview);
        this.context = context;
        this.deviceName = name;
        this.deviceId = id;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.listview, null, true);
        TextView carName = (TextView) listViewItem.findViewById(R.id.car_name);
        TextView carMake = (TextView) listViewItem.findViewById(R.id.car_make);

        carName.setText((String) deviceName.get(position));
        carMake.setText((String) deviceId.get(position));
        return  listViewItem;
    }
}
