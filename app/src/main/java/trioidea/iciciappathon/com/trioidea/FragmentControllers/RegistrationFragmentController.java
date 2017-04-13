package trioidea.iciciappathon.com.trioidea.FragmentControllers;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trioidea.iciciappathon.com.trioidea.Activities.MainScreen;
import trioidea.iciciappathon.com.trioidea.Fragments.RegistrationFragment;
import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by asus on 14/04/2017.
 */
public class RegistrationFragmentController implements View.OnClickListener {

   RegistrationFragment registrationFragment;
   public RegistrationFragmentController(RegistrationFragment registrationFragment)
   {
       this.registrationFragment=registrationFragment;

   }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                if(registrationFragment.validateData())
                {
                    registrationFragment.setDataInSharedPref();
                    ((MainScreen)registrationFragment.getActivity()).startFeaturesOptionFragment();
                }
                break;
        }
    }
}
