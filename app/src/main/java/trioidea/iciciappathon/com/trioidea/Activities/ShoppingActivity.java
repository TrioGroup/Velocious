package trioidea.iciciappathon.com.trioidea.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import trioidea.iciciappathon.com.trioidea.Fragments.ShoppingMainScreenFragment;
import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by Harshal on 14-Apr-17.
 */
public class ShoppingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_activity);
    }
    public void startShoppingFragment(){
        addFragment(new ShoppingMainScreenFragment(),true);
    }
    public void replaceFragment(android.app.Fragment fragment, boolean addToBackStack) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    public void addFragment(android.app.Fragment fragment,boolean addToBackStack) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }
}
