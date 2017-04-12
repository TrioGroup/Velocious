package trioidea.iciciappathon.com.trioidea.FragmentControllers;

import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

import rx.Observer;
import trioidea.iciciappathon.com.trioidea.Activities.MainScreen;
import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;
import trioidea.iciciappathon.com.trioidea.DbHelper;
import trioidea.iciciappathon.com.trioidea.EncryptionClass;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;
import trioidea.iciciappathon.com.trioidea.Fragments.FeatureOptionFragment;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.RxBus;
import trioidea.iciciappathon.com.trioidea.Services.ServiceLayer;

/**
 * Created by asus on 02/04/2017.
 */
public class FeatureOptionFragmentController implements Observer, View.OnClickListener {
    FeatureOptionFragment featureOptionFragment;
    String temp = null;

    public FeatureOptionFragmentController(FeatureOptionFragment fragment) {
        featureOptionFragment = fragment;
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

        switch (v.getId()) {
            case R.id.btn_offline_transaction:
                /*String encryptedValue1 = EncryptionClass.symmetricEncrypt("HI");
                String decryptedValue1 = EncryptionClass.symmetricDecrypt(encryptedValue1);
                System.out.println(decryptedValue1);
                Toast.makeText(featureOptionFragment.getActivity(), encryptedValue1 + ":" + decryptedValue1, Toast.LENGTH_SHORT).show();*/
                ((MainScreen)featureOptionFragment.getActivity()).startActivityTransferOffline();
                break;
            case R.id.btn_shopping_assist:
                //"t_id", "sender_id", "sender_name", "receiver_id", "receiver_name", "amount", "time", "balance", "sync_flag"
                TransactionDto transactionDto = new TransactionDto(1, 1, "sender", 2, "receiver", 2000, "12.00", 10000, false);
                DbHelper.getInstance(featureOptionFragment.getActivity()).insertTransaction(transactionDto);
                TransactionDto[] transactionDtos = DbHelper.getInstance(featureOptionFragment.getActivity()).getAllTransaction();
                Log.e("DBTEST", "incontroller" + transactionDtos[0].getTransactionId());
                //ServiceLayer serviceLayer=ServiceLayer.getServiceLayer();
                //serviceLayer.autheticateUser();
                //serviceLayer.userInfo();

                String timeSettings = android.provider.Settings.System.getString(featureOptionFragment.getActivity().getContentResolver(), android.provider.Settings.Global.AUTO_TIME);
                if(timeSettings.equals("0"))
                {
                    Settings.System.putString(featureOptionFragment.getActivity().getContentResolver(), android.provider.Settings.Global.AUTO_TIME,"1");
                 /*ntent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setClassName("com.android.settings", "com.android.settings.ACTION_DATE_SETTINGS");
                    featureOptionFragment.getActivity().startActivity(intent);
               */
                    featureOptionFragment.getActivity().startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                }
                Date now=new Date(System.currentTimeMillis());
                Log.e("Time ",now.toString());
                break;
        }
    }
}
