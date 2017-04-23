package trioidea.iciciappathon.com.trioidea.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import trioidea.iciciappathon.com.trioidea.DTO.ItemListDTO;
import trioidea.iciciappathon.com.trioidea.DTO.ItemLookUpDto;
import trioidea.iciciappathon.com.trioidea.DTO.SingleItem;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.Services.ItemLookUp;

public class ImageAdapter extends PagerAdapter {
    Activity activity;
    ItemLookUpDto singleItem;
    ItemListDTO itemListDTO;
    boolean isAmazon;

    public ImageAdapter(Activity activity, Object singleItem, boolean flag) {
        this.activity = activity;
        if (flag)
            this.singleItem = (ItemLookUpDto) singleItem;
        else
            this.itemListDTO = (ItemListDTO) singleItem;
        this.isAmazon = flag;
    }

    @Override
    public int getCount() {
        if (isAmazon)
            return singleItem.getImageSets().getImageSet().size();
        else
            return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView imageView = new ImageView(activity);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        //imageView.setImageResource(R.drawable.loading_image);
       /* final Animation animLoad = AnimationUtils.loadAnimation(activity,
                R.anim.image_loader);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.startAnimation(animLoad);
            }
        });*/
        String imageUrl;
        if (isAmazon)
            imageUrl = singleItem.getImageSets().getImageSet().get(position).getLargeImage().getURL();
        else
            imageUrl = itemListDTO.getImageUrl();
        Picasso.with(activity).load(imageUrl).placeholder(R.drawable.loading_image).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(0);
                        imageView.setImageBitmap(bitmap);
                        imageView.bringToFront();
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (imageView.getDrawable() == null) {
                    if (imageView == null) {
                        Bitmap bitmap = ((BitmapDrawable) placeHolderDrawable).getBitmap();
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }

        });

        ((ViewPager) container).addView(imageView, position);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //((ViewPager) container).removeView((ImageView) object);
    }
}
