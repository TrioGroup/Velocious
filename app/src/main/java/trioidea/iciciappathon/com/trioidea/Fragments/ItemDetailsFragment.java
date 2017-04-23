package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import rx.Observer;
import rx.Subscription;
import trioidea.iciciappathon.com.trioidea.Activities.ShoppingActivity;
import trioidea.iciciappathon.com.trioidea.Adapters.CustomListViewAdapter;
import trioidea.iciciappathon.com.trioidea.Adapters.ImageAdapter;
import trioidea.iciciappathon.com.trioidea.DTO.ItemListDTO;
import trioidea.iciciappathon.com.trioidea.DTO.ItemLookUpDto;
import trioidea.iciciappathon.com.trioidea.DTO.SingleItem;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;
import trioidea.iciciappathon.com.trioidea.FragmentControllers.FeatureOptionFragmentController;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.RxBus;
import trioidea.iciciappathon.com.trioidea.Services.ServiceLayer;

/**
 * Created by asus on 21/04/2017.
 */
public class ItemDetailsFragment extends Fragment implements Observer {
    ViewPager viewPager;
    ImageAdapter adapter;
    Subscription subscription;
    RxBus rxBus = RxBus.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        subscription = rxBus.toObserverable().subscribe(this);
        return inflater.inflate(R.layout.items_details_fragment, container, false);
    }

    public void initUi(Object object, boolean isAmazon) {
        if (isAmazon) {
            ItemLookUpDto singleItem = (ItemLookUpDto) object;
            TextView tvTitle = (TextView) getActivity().findViewById(R.id.tv_title);
            TextView tvAmount = (TextView) getActivity().findViewById(R.id.tv_amount);
            TextView tvSaleAmount = (TextView) getActivity().findViewById(R.id.tv_sale_amount);
            TextView tvSeller = (TextView) getActivity().findViewById(R.id.tv_seller);
            TextView tvProductDetail = (TextView) getActivity().findViewById(R.id.tv_product_details);
            tvTitle.setText(singleItem.getItemAttributes().getTitle());
            tvAmount.setText(singleItem.getItemAttributes().getListPrice().getFormattedPrice());
            if (singleItem.getOfferSummary().getOffer() != null) {
                tvAmount.setPaintFlags(tvAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //double salePrice=Double.parseDouble(singleItem.getItemAttributes().getListPrice().getAmount())-Double.parseDouble(singleItem.getOfferSummary().getOffer().getOfferListing().getAmountSaved().getAmount());
                // tvSaleAmount.setText("INR "+new DecimalFormat("##,##,##0").format(String.valueOf(salePrice/100)));
                tvSaleAmount.setText(singleItem.getOfferSummary().getOffer().getOfferListing().getPrice().getFormattedPrice());

            }
            tvSeller.setText(singleItem.getItemAttributes().getPublisher());
            String feature = "";
            for (int i = 0; i < singleItem.getItemAttributes().getFeature().size(); i++)
                feature = feature + "> " + singleItem.getItemAttributes().getFeature().get(i).getFeature() + ".\n\n";
            tvProductDetail.setText(feature);
        } else {
            ItemListDTO singleItem = (ItemListDTO) object;
            TextView tvTitle = (TextView) getActivity().findViewById(R.id.tv_title);
            TextView tvAmount = (TextView) getActivity().findViewById(R.id.tv_amount);
            TextView tvSaleAmount = (TextView) getActivity().findViewById(R.id.tv_sale_amount);
            TextView tvSeller = (TextView) getActivity().findViewById(R.id.tv_seller);
            TextView tvProductDetail = (TextView) getActivity().findViewById(R.id.tv_product_details);
            tvTitle.setText(singleItem.getTitle());
            tvAmount.setText(singleItem.getPrice());
            if (singleItem.getOfferPrice() != null) {
                tvAmount.setPaintFlags(tvAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //double salePrice=Double.parseDouble(singleItem.getItemAttributes().getListPrice().getAmount())-Double.parseDouble(singleItem.getOfferSummary().getOffer().getOfferListing().getAmountSaved().getAmount());
                // tvSaleAmount.setText("INR "+new DecimalFormat("##,##,##0").format(String.valueOf(salePrice/100)));
                tvSaleAmount.setText(singleItem.getOfferPrice());
            }
            tvSeller.setText(singleItem.getPublisher());
            String feature = "";
            if (singleItem.getDesc() != null)
                tvProductDetail.setText(singleItem.getDesc());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        ItemListDTO singleItem = ((ShoppingActivity) getActivity()).getSelectedItem();
        if (singleItem.getSite().equals("Amazon"))
            ServiceLayer.getServiceLayer().getItemDetails(singleItem.getId());
        else {
            viewPager = (ViewPager) getActivity().findViewById(R.id.view_pager);
            adapter = new ImageAdapter(getActivity(), (Object) singleItem, false);
            viewPager.setAdapter(adapter);
            initUi((Object) singleItem, false);
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Object o) {
        final EventResponse eventResponse = (EventResponse) o;
        switch (eventResponse.getEvent()) {
            case EventNumbers.AMAZON_GET_ITEM:
                final ItemLookUpDto singleItem = (ItemLookUpDto) ((EventResponse) o).getResponse();
                Log.e("frag get Products", "" + singleItem.getASIN());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager = (ViewPager) getActivity().findViewById(R.id.view_pager);
                        adapter = new ImageAdapter(getActivity(), (Object) singleItem, true);
                        viewPager.setAdapter(adapter);
                        initUi((Object) singleItem, true);
                    }
                });
                break;
        }
    }
}
