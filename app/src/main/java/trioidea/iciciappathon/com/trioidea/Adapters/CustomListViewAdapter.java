package trioidea.iciciappathon.com.trioidea.Adapters;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Paint;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import java.util.ArrayList;

import trioidea.iciciappathon.com.trioidea.DTO.ItemListDTO;
import trioidea.iciciappathon.com.trioidea.DTO.SingleItem;
import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by Harshal on 14-Apr-17.
 */
public class CustomListViewAdapter extends ArrayAdapter<ItemListDTO> {
    private ArrayList<ItemListDTO> itemArrayList;
    private Activity context;

    public CustomListViewAdapter(Activity context, ArrayList<ItemListDTO> singleItems) {
        super(context, R.layout.shopping_list_item, singleItems);
        this.context = context;

        this.itemArrayList = singleItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.shopping_list_item, null, true);
        TextView carName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView carMake = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        TextView carPrice = (TextView) listViewItem.findViewById(R.id.textViewsite);
        TextView carOfferPrice = (TextView) listViewItem.findViewById(R.id.textViewOffer);
        ImageView siteImage=(ImageView)listViewItem.findViewById(R.id.site_logo);
        final ImageView carImage = (ImageView) listViewItem.findViewById(R.id.imageView);
        if(itemArrayList.get(position).getSite().equals("Amazon"))
            siteImage.setImageResource(R.drawable.amazon);
        else
            siteImage.setImageResource(R.drawable.flipkart);
        carName.setText(itemArrayList.get(position).getTitle());
        if (itemArrayList.get(position).getPrice() != null)
            if (!itemArrayList.get(position).getPrice().isEmpty()) {
                carPrice.setText(itemArrayList.get(position).getPrice());
            }
        if (itemArrayList.get(position) != null)
            if (itemArrayList.get(position).getPublisher() != null && !itemArrayList.get(position).getPublisher().isEmpty())
                carMake.setText(itemArrayList.get(position).getPublisher());
        if (itemArrayList.get(position).getOfferPrice() != null) {
            carOfferPrice.setText(itemArrayList.get(position).getOfferPrice());
            carPrice.setPaintFlags(carPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (itemArrayList.get(position).getImageUrl() == null) {
            carImage.setImageResource(R.drawable.broken_image);

        } else {
            Picasso.with(context).load(itemArrayList.get(position).getImageUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    saveImage(bitmap);
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            carImage.setImageBitmap(bitmap);
                        }
                    });
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        }
        return listViewItem;
    }

    public ArrayList<ItemListDTO> getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(ArrayList<ItemListDTO> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public void addElementsInList(ArrayList<ItemListDTO> itemArrayList) {
        this.itemArrayList.addAll(itemArrayList);
    }

    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/icici_shopping/images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
