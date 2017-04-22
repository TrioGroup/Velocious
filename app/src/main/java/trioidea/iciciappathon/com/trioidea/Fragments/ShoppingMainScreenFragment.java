package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

    CustomListViewAdapter customListViewAdapter;
    ListView listView;
    Subscription subscription;
    RxBus rxBus = RxBus.getInstance();
    int paginationPageNo = 1;
    String searchString;
    int previousPage;
    private List<String> lastSearches;
    private MaterialSearchBar searchBar;


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

        listView = (ListView) getActivity().findViewById(R.id.searchedItem);
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
                ServiceLayer.getServiceLayer().getItemsFromAmazon(searchString, paginationPageNo);
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
                            previousPage=paginationPageNo;
                        }
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((ShoppingActivity)getActivity()).setSelectedItem(customListViewAdapter.getItemArrayList().get(position));
                EventResponse eventResponse =new EventResponse((Object)null, EventNumbers.START_ITEM_DETAILS_FRAGMENT);
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
                if (paginationPageNo == 1) {
                    final ArrayList<SingleItem> singleItems = (ArrayList<SingleItem>) ((EventResponse) o).getResponse();
                    Log.e("frag get Products", "" + singleItems.get(0).getASIN());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customListViewAdapter = new CustomListViewAdapter(getActivity(), singleItems);
                            listView.setAdapter(customListViewAdapter);
                            paginationPageNo++;
                        }
                    });
                } else {
                    final ArrayList<SingleItem> singleItems = (ArrayList<SingleItem>) ((EventResponse) o).getResponse();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customListViewAdapter.addElementsInList(singleItems);
                            customListViewAdapter.notifyDataSetChanged();
                            paginationPageNo++;
                        }
                    });
                }
                break;
        }
    }

}
