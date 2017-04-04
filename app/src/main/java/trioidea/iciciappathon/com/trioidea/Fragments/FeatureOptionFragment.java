package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import trioidea.iciciappathon.com.trioidea.FragmentControllers.FeatureOptionFragmentController;
import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by asus on 02/04/2017.
 */
public class FeatureOptionFragment extends Fragment {

    FeatureOptionFragmentController mController;
    Button offline, shopping;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feature_option_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mController = new FeatureOptionFragmentController(this);
        init();
    }
    public void init()
    {
        offline=(Button)getActivity().findViewById(R.id.btn_offline_transaction);
        shopping=(Button)getActivity().findViewById(R.id.btn_shopping_assist);
        offline.setOnClickListener(mController);
        shopping.setOnClickListener(mController);
    }
}
