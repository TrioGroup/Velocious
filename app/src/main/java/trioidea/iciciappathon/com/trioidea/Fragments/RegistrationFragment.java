package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import trioidea.iciciappathon.com.trioidea.FragmentControllers.FeatureOptionFragmentController;
import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by asus on 13/04/2017.
 */
public class RegistrationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registration, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }
    public void init()
    {

    }

}


