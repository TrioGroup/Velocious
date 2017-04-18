package trioidea.iciciappathon.com.trioidea.Adapters;

import android.widget.ArrayAdapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by Harshal on 14-Apr-17.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {

    private String[] names;
    private String[] make;
    private String[] price;
    private Integer[] imageid;
    private Activity context;

    public CustomArrayAdapter(Activity context, String[] names, String[] make, String[] price, Integer[] imageid) {
        super(context, R.layout.listview, names);
        this.context = context;
        this.names = names;
        this.make = make;
        this.price = price;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.listview, null, true);
        TextView carName = (TextView) listViewItem.findViewById(R.id.car_name);
        TextView carMake = (TextView) listViewItem.findViewById(R.id.car_make);
        TextView carPrice = (TextView) listViewItem.findViewById(R.id.car_price);
        ImageView carImage = (ImageView) listViewItem.findViewById(R.id.car_image);

        carName.setText(names[position]);
        carMake.setText(make[position]);
        carPrice.setText(price[position]);
        carImage.setImageResource(imageid[position]);
        return  listViewItem;
    }
}
