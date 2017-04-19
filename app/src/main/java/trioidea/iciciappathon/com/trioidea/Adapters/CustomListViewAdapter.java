package trioidea.iciciappathon.com.trioidea.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by Harshal on 14-Apr-17.
 */
public class CustomListViewAdapter extends ArrayAdapter<String> {

    private String[] names;
    private String[] make;
    private String[] price;
    private Integer[] imageid;
    private Activity context;

    public CustomListViewAdapter(Activity context, String[] names, String[] make, String[] price, Integer[] imageid) {
        super(context, R.layout.shopping_list_item, names);
        this.context = context;
        this.names = names;
        this.make = make;
        this.price = price;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.shopping_list_item, null, true);
        TextView carName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView carMake = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        TextView carPrice = (TextView) listViewItem.findViewById(R.id.textViewsite);
        ImageView carImage = (ImageView) listViewItem.findViewById(R.id.imageView);

        carName.setText(names[position]);
        carMake.setText(make[position]);
        carPrice.setText(price[position]);
        carImage.setImageResource(imageid[position]);
        return  listViewItem;
    }
}
