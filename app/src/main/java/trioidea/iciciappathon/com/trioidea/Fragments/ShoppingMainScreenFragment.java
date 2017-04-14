package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by Harshal on 14-Apr-17.
 */
public class ShoppingMainScreenFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState)
    {
        return layoutInflater.inflate(R.layout.shopping_main_fragment,viewGroup,false);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        initUi();
    }
    public void initUi(){
        ListView listView=(ListView)getActivity().findViewById(R.id.shopping_list);

    }
}
