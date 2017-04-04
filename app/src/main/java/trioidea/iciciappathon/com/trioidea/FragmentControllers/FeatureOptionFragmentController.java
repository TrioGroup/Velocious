package trioidea.iciciappathon.com.trioidea.FragmentControllers;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import rx.Observer;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;
import trioidea.iciciappathon.com.trioidea.Fragments.FeatureOptionFragment;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.RxBus;

/**
 * Created by asus on 02/04/2017.
 */
public class FeatureOptionFragmentController implements Observer, View.OnClickListener{
    FeatureOptionFragment featureOptionFragment;
    public FeatureOptionFragmentController(FeatureOptionFragment fragment)
    {
        featureOptionFragment=fragment;
        RxBus rxBus = RxBus.getInstance();
        rxBus.toObserverable().subscribe(this);
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
            case EventNumbers.TIMER_EVENT:

                break;
            case EventNumbers.SIGN_IN_USER:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_offline_transaction:
                Toast.makeText(featureOptionFragment.getActivity(),"offline",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_shopping_assist:
                Toast.makeText(featureOptionFragment.getActivity(),"shopping",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
