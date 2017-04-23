package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observer;
import rx.Subscription;
import trioidea.iciciappathon.com.trioidea.Activities.ShoppingActivity;
import trioidea.iciciappathon.com.trioidea.Adapters.CustomArrayAdapter;
import trioidea.iciciappathon.com.trioidea.Adapters.CustomListViewAdapter;
import trioidea.iciciappathon.com.trioidea.DTO.FlipkartProductInfoList;
import trioidea.iciciappathon.com.trioidea.DTO.ItemListDTO;
import trioidea.iciciappathon.com.trioidea.DTO.SingleItem;
import trioidea.iciciappathon.com.trioidea.EncryptionClass;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.RxBus;
import trioidea.iciciappathon.com.trioidea.Services.ServiceLayer;

/**
 * Created by Harshal on 14-Apr-17.
 */
public class ShoppingMainScreenFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener, PopupMenu.OnMenuItemClickListener, Observer {

    private static MaterialSearchBar searchBar;
    CustomListViewAdapter customListViewAdapter;
    ListView listView;
    Subscription subscription;
    ImageButton showCart;
    RxBus rxBus = RxBus.getInstance();
    int paginationPageNo = 1;
    String searchString;
    int previousPage;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        subscription = rxBus.toObserverable().subscribe(this);
        return layoutInflater.inflate(R.layout.shopping_activity, viewGroup, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
        Set<String> set = new HashSet<String>(searchBar.getLastSuggestions());
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE).edit();
        editor.putStringSet("searchHistory", set);
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.setRetainInstance(true);
        listView = (ListView) getActivity().findViewById(R.id.searchedItem);
        showCart=(ImageButton)getActivity().findViewById(R.id.show_cart);
        showCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShoppingActivity)getActivity()).showCart();
            }
        });
        searchBar = (MaterialSearchBar) getActivity().findViewById(R.id.searchBar);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
        Set<String> set = sharedPreferences.getStringSet("searchHistory", null);
        if (set != null) {
            List<String> list = new ArrayList<String>(set);
            searchBar.setLastSuggestions(list);
        }
        searchBar.setSpeechMode(false);
        searchBar.setHint("Search");
        searchBar.enableSearch();
        //enable searchbar callbacks
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (enabled)
                    searchBar.showSuggestionsList();
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                paginationPageNo = 1;
                searchString = text.toString();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);
                ServiceLayer.getServiceLayer().getItemsFromAmazon(searchString, paginationPageNo);
                ServiceLayer.getServiceLayer().getItemsFromFlipkart(searchString);
                Set<String> set = new HashSet<String>(searchBar.getLastSuggestions());
                SharedPreferences.Editor editor = ShoppingMainScreenFragment.this.getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE).edit();
                editor.putStringSet("searchHistory", set);
                editor.commit();
            }

            @Override
            public void onButtonClicked(int buttonCode) {


            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0) {
                    int lastInScreen = firstVisibleItem + visibleItemCount;
                    if (lastInScreen == totalItemCount) {
                        if (paginationPageNo != previousPage) {
                            ServiceLayer.getServiceLayer().getItemsFromAmazon(searchString, paginationPageNo);
                            previousPage = paginationPageNo;
                        }
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((ShoppingActivity) getActivity()).setSelectedItem(customListViewAdapter.getItemArrayList().get(position));
                EventResponse eventResponse = new EventResponse((Object) null, EventNumbers.START_ITEM_DETAILS_FRAGMENT);
                rxBus.send(eventResponse);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    @Override
    public void onSearchStateChanged(boolean enabled) {
        String s = enabled ? "enabled" : "disabled";
        Toast.makeText(getActivity(), "Search " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
//        startSearch(text.toString(), true, null, true);
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                //drawer.openDrawer(Gravity.LEFT);
                break;
            case MaterialSearchBar.BUTTON_SPEECH:
                //openVoiceRecognizer();
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
            case EventNumbers.AMAZON_GET_PRODUCTS:
                final ArrayList<SingleItem> singleItems = (ArrayList<SingleItem>) ((EventResponse) o).getResponse();
                final ArrayList<ItemListDTO> itemListDTOs = setSingleItem(singleItems);
                if (paginationPageNo == 1) {
                    Log.e("frag get Products", "" + singleItems.get(0).getASIN());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customListViewAdapter = new CustomListViewAdapter(getActivity(), itemListDTOs);
                            listView.setAdapter(customListViewAdapter);
                            paginationPageNo++;
                        }
                    });
                } else {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customListViewAdapter.addElementsInList(itemListDTOs);
                            customListViewAdapter.notifyDataSetChanged();
                            paginationPageNo++;
                        }
                    });
                }
                break;
            case EventNumbers.FLIPKART_GET_ITEM:
                final ArrayList<FlipkartProductInfoList> flipkartProductInfoLists = (ArrayList<FlipkartProductInfoList>) ((EventResponse) o).getResponse();
                Log.e("frag get Products", "" + flipkartProductInfoLists.get(0).getProductBaseInfoV1().getProductId());
                final ArrayList<ItemListDTO> flipkartItemListDTOs = setFlipkartItem(flipkartProductInfoLists);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        customListViewAdapter = new CustomListViewAdapter(getActivity(), flipkartItemListDTOs);
                        listView.setAdapter(customListViewAdapter);
                        paginationPageNo++;
                    }
                });
        }
    }

    public ArrayList<ItemListDTO> setSingleItem(ArrayList<SingleItem> singleItems) {
        ArrayList<ItemListDTO> itemListDTOs = new ArrayList<>();
        for (int position = 0; position < singleItems.size(); position++) {
            ItemListDTO itemListDTO = new ItemListDTO();
            itemListDTO.setId(singleItems.get(position).getASIN());
            itemListDTO.setSite("Amazon");
            itemListDTO.setTitle(singleItems.get(position).getItemAttributes().getTitle());
            if (singleItems.get(position).getItemAttributes().getListPrice() != null) {
                if (singleItems.get(position).getItemAttributes().getListPrice().getFormattedPrice() != null && !singleItems.get(position).getItemAttributes().getListPrice().getFormattedPrice().isEmpty()) {
                    itemListDTO.setPrice(singleItems.get(position).getItemAttributes().getListPrice().getFormattedPrice());
                } else {
                    itemListDTO = null;
                    continue;
                }
            }
            else
            {
                itemListDTO=null;
                continue;
            }
            if (singleItems.get(position).getItemAttributes() != null)
                if (singleItems.get(position).getItemAttributes().getPublisher() != null && !singleItems.get(position).getItemAttributes().getPublisher().isEmpty())
                    itemListDTO.setPublisher(singleItems.get(position).getItemAttributes().getPublisher());
            if (singleItems.get(position).getOfferSummary().getOffer() != null)
                if (singleItems.get(position).getOfferSummary().getOffer().getOfferListing().getPrice() != null) {
                    if (singleItems.get(position).getOfferSummary().getOffer().getOfferListing().getPrice().getFormattedPrice() != null && !singleItems.get(position).getOfferSummary().getOffer().getOfferListing().getPrice().getFormattedPrice().isEmpty()) {
                        if (singleItems.get(position).getOfferSummary().getOffer().getOfferListing().getAmountSaved() != null) {
                            itemListDTO.setOfferPrice(singleItems.get(position).getOfferSummary().getOffer().getOfferListing().getPrice().getFormattedPrice());
                        }
                    }
                }
            if (singleItems.get(position).getMediumImage() != null) {
                if (!singleItems.get(position).getMediumImage().getURL().isEmpty()) {
                    itemListDTO.setImageUrl(singleItems.get(position).getMediumImage().getURL());
                } else
                    itemListDTO.setImageUrl(null);
            }
            itemListDTOs.add(itemListDTO);
        }
        return itemListDTOs;
    }

    public ArrayList<ItemListDTO> setFlipkartItem(ArrayList<FlipkartProductInfoList> singleItems) {
        ArrayList<ItemListDTO> itemListDTOs = new ArrayList<>();
        for (int position = 0; position < singleItems.size(); position++) {
            ItemListDTO itemListDTO = new ItemListDTO();
            itemListDTO.setId(singleItems.get(position).getProductBaseInfoV1().getProductId());
            itemListDTO.setTitle(singleItems.get(position).getProductBaseInfoV1().getTitle());
            itemListDTO.setSite("Flipkart");
            if (singleItems.get(position).getProductBaseInfoV1().getMaximumRetailPrice() != null)
                if (singleItems.get(position).getProductBaseInfoV1().getMaximumRetailPrice().getAmount() != null && !singleItems.get(position).getProductBaseInfoV1().getMaximumRetailPrice().getAmount().isEmpty()) {
                    itemListDTO.setPrice(singleItems.get(position).getProductBaseInfoV1().getFlipkartSellingPrice().getCurrency() + " " + singleItems.get(position).getProductBaseInfoV1().getFlipkartSellingPrice().getAmount());
                }
            if (singleItems.get(position).getProductBaseInfoV1().getProductBrand() != null && !singleItems.get(position).getProductBaseInfoV1().getProductBrand().isEmpty())
                itemListDTO.setPublisher(singleItems.get(position).getProductBaseInfoV1().getProductBrand());
            if (singleItems.get(position).getProductBaseInfoV1().getFlipkartSpecialPrice() != null)
                if (!singleItems.get(position).getProductBaseInfoV1().getFlipkartSpecialPrice().getAmount().isEmpty())
                    if (!singleItems.get(position).getProductBaseInfoV1().getFlipkartSellingPrice().getAmount().equals(singleItems.get(position).getProductBaseInfoV1().getFlipkartSpecialPrice().getAmount()))
                        itemListDTO.setOfferPrice(singleItems.get(position).getProductBaseInfoV1().getFlipkartSpecialPrice().getCurrency() + " " + singleItems.get(position).getProductBaseInfoV1().getFlipkartSpecialPrice().getAmount());
            if (singleItems.get(position).getProductBaseInfoV1().getImageUrls() != null) {
                if (!singleItems.get(position).getProductBaseInfoV1().getImageUrls().getUnknown().isEmpty()) {
                    itemListDTO.setImageUrl(singleItems.get(position).getProductBaseInfoV1().getImageUrls().getUnknown().replace("original", "400x400"));
                } else
                    itemListDTO.setImageUrl(null);
            }
            if (singleItems.get(position).getCategorySpecificInfoV1().getKeySpecs() != null) {
                String feature = "";
                for (int i = 0; i < singleItems.get(position).getCategorySpecificInfoV1().getKeySpecs().size(); i++)
                    feature = feature + "> " + singleItems.get(position).getCategorySpecificInfoV1().getKeySpecs().get(i) + ".\n\n";
                itemListDTO.setDesc(feature);
            }
            itemListDTOs.add(itemListDTO);
        }
        return itemListDTOs;
    }


}
