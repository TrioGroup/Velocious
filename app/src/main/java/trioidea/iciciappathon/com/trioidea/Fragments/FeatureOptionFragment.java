package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import trioidea.iciciappathon.com.trioidea.Activities.MainScreen;
import trioidea.iciciappathon.com.trioidea.EncryptionClass;
import trioidea.iciciappathon.com.trioidea.FragmentControllers.FeatureOptionFragmentController;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.Services.WebService;

/**
 * Created by asus on 02/04/2017.
 */
public class FeatureOptionFragment extends Fragment {

    FeatureOptionFragmentController mController;
    ImageButton shopping;
    ImageButton offline;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mController.subscription.unsubscribe();
    }



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
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userData", 0);
        String receiverName = EncryptionClass.symmetricDecrypt(sharedPreferences.getString("name", "User"));
        TextView username = (TextView)getActivity().findViewById(R.id.username);
        String receiverNameFull[] = receiverName.split(" ");
        username.setText("Hello "+receiverNameFull[0]+" !");
        offline=(ImageButton)getActivity().findViewById(R.id.btn_offline_transaction);
        shopping=(ImageButton)getActivity().findViewById(R.id.btn_shopping_assist);
        offline.setOnClickListener(mController);
        shopping.setOnClickListener(mController);
    }
}
