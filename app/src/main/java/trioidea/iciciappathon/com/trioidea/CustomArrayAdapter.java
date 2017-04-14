package trioidea.iciciappathon.com.trioidea;

import android.widget.ArrayAdapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Created by Harshal on 14-Apr-17.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {

    private String[] names;
    private String[] desc;
    private String[] site;
    private Integer[] imageid;
    private Activity context;

    public CustomArrayAdapter(Activity context, String[] names, String[] desc,String[] site, Integer[] imageid) {
        super(context, R.layout.shopping_list_item, names);
        this.context = context;
        this.names = names;
        this.desc = desc;
        this.site = site;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.shopping_list_item, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        TextView textViewsite = (TextView) listViewItem.findViewById(R.id.textViewsite);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageView);

        textViewName.setText(names[position]);
        textViewDesc.setText(desc[position]);
        textViewsite.setText(site[position]);
        image.setImageResource(imageid[position]);
        return  listViewItem;
    }
}
