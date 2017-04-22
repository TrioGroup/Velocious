package trioidea.iciciappathon.com.trioidea.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import trioidea.iciciappathon.com.trioidea.DTO.SingleItem;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;
import trioidea.iciciappathon.com.trioidea.Fragments.ItemDetailsFragment;
import trioidea.iciciappathon.com.trioidea.Fragments.ShoppingMainScreenFragment;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.RxBus;

/**
 * Created by Harshal on 14-Apr-17.
 */
public class ShoppingActivity extends AppCompatActivity implements Observer {
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    private SingleItem selectedItem;
    Subscription subscription;
    RxBus rxBus=RxBus.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + getString(R.string.app_name) + "</font>")));
        subscription=rxBus.toObserverable().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        startShoppingFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    public void startShoppingFragment() {
        addFragment(new ShoppingMainScreenFragment(), false);
    }

    public void replaceFragment(android.app.Fragment fragment, boolean addToBackStack) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    public void addFragment(Fragment fragment, boolean addToBackStack) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    public ProgressDialog getProgressBar(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public AlertDialog getAlertDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }

    }

    public SingleItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(SingleItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Object o) {
        EventResponse eventResponse = (EventResponse) o;
        if (o != null)
            switch (eventResponse.getEvent()) {
                case EventNumbers.START_ITEM_DETAILS_FRAGMENT:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            replaceFragment(new ItemDetailsFragment(),true);
                        }
                    });
                    break;
            }
    }
}
