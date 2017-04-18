package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

import trioidea.iciciappathon.com.trioidea.Adapters.CustomArrayAdapter;
import trioidea.iciciappathon.com.trioidea.Adapters.CustomListViewAdapter;
import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by Harshal on 14-Apr-17.
 */
public class ShoppingMainScreenFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener,PopupMenu.OnMenuItemClickListener{

    private List<String> lastSearches;
    private MaterialSearchBar searchBar;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        return layoutInflater.inflate(R.layout.shopping_activity, viewGroup, false);

    }


    @Override
    public void onResume()
    {
        super.onResume();
        final String make[] = {"Abarth","BMW","Dodge","Ford",
                "Chevrolet","Cadillac","Lincoln"};

        final String names[] = {"Punto", "M5", "Charger SRT", "Mustang 500", "Camaro Z28", "Eldorado", "Continental"};

        final String price[] = {"$1", "$2", "$3", "$4", "$5", "$5", "$6", "$7"};

        final Integer images[] ={R.drawable.next, R.drawable.next, R.drawable.next, R.drawable.next, R.drawable.next,
                R.drawable.next, R.drawable.next};

//        TwoWayView lvTest = (TwoWayView) findViewById(R.id.lvItems);
//        TwoWayView lvCategories = (TwoWayView) findViewById(R.id.lvCategories);
        ListView listView = (ListView) getActivity().findViewById(R.id.searchedItem);

        CustomArrayAdapter customAdapter = new CustomArrayAdapter(getActivity(), names, make, price, images);

        CustomListViewAdapter customListViewAdapter = new CustomListViewAdapter(getActivity(),names,make,price,images);

//        lvTest.setAdapter(customAdapter);
//        lvTest.setItemMargin(30);

//        lvCategories.setAdapter(customAdapter);
//        lvCategories.setItemMargin(30);
        listView.setAdapter(customListViewAdapter);

//        lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("test", "Position : " + position + " id : " + id);
//                Toast.makeText(MainActivity.this, make[position] + " selected", Toast.LENGTH_SHORT).show();
//            }
//        });






        searchBar = (MaterialSearchBar) getActivity().findViewById(R.id.searchBar);
        searchBar.setHint("Custom hint");
        searchBar.setSpeechMode(false);
        searchBar.setHint("Search");
        //enable searchbar callbacks
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

            }

            @Override
            public void onButtonClicked(int buttonCode) {

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
        switch (buttonCode){
            case MaterialSearchBar.BUTTON_NAVIGATION:
                //drawer.openDrawer(Gravity.LEFT);
                break;
            case MaterialSearchBar.BUTTON_SPEECH:
                //openVoiceRecognizer();
        }
    }



}
