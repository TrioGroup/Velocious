package trioidea.iciciappathon.com.trioidea.Adapters;

import android.widget.ArrayAdapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import trioidea.iciciappathon.com.trioidea.DTO.SingleItem;
import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by Harshal on 14-Apr-17.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {

    ArrayList<SingleItem> itemList;
    private Integer[] imageid;
    private Activity context;

    public CustomArrayAdapter(Activity context, ArrayList<SingleItem> itemList,Integer[]imageid) {
        super(context, R.layout.listview);
        this.context = context;
        this.itemList = itemList;
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

        carName.setText(itemList.get(position).getItemAttributes().getTitle());
        carMake.setText(itemList.get(position).getItemAttributes().getPublisher());
        carPrice.setText(itemList.get(position).getItemAttributes().getListPrice().getFormattedPrice());
        carImage.setImageResource(imageid[position]);
        return  listViewItem;
    }
}
