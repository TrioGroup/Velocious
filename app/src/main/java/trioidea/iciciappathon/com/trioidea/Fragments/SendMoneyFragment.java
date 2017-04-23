package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import rx.Observer;
import rx.Subscription;
import trioidea.iciciappathon.com.trioidea.Activities.TransferActivity;
import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;
import trioidea.iciciappathon.com.trioidea.DbHelper;
import trioidea.iciciappathon.com.trioidea.EncryptionClass;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.RxBus;
import trioidea.iciciappathon.com.trioidea.Services.FileClientAsyncTask;
import trioidea.iciciappathon.com.trioidea.Services.WiFiDirectBroadcastReceiver;

/**
 * Created by Harshal on 12-Apr-17.
 */
public class SendMoneyFragment extends Fragment implements Observer {

    ProgressDialog progressDialog;
    TransferActivity parentActivity;
    RxBus rxBus = RxBus.getInstance();
    Subscription subscription;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.send_money, viewGroup, false);

    }

    public void initUi()

    {
        parentActivity = (TransferActivity) SendMoneyFragment.this.getActivity();


        parentActivity.textView = (TextView) parentActivity.findViewById(R.id.balance);
        parentActivity.textView.setText(String.valueOf(DbHelper.getInstance(getActivity()).getBalance()));

        parentActivity.adapter = new ArrayAdapter(parentActivity, R.layout.activity_listview, parentActivity.mobileNames);
        parentActivity.listView = (ListView) parentActivity.findViewById(R.id.mobile_list);
        parentActivity.listView.setAdapter(parentActivity.adapter);

        parentActivity.amount = (EditText) parentActivity.findViewById(R.id.amount);
        parentActivity.amount.setText(null);

        parentActivity.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progressDialog = new ProgressDialog(parentActivity);
                progressDialog.setMessage("Sending money");
                progressDialog.setProgress(0);
                progressDialog.setCancelable(false);
                progressDialog.show();
                parentActivity.mWifiP2pManager.cancelConnect(parentActivity.mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.e("p2p", "searching cancelled");
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.e("p2p", "searching cancelled");
                    }
                });
                final String item = ((TextView) view).getText().toString();
//                Toast.makeText(getActivity().getApplicationContext(), item, Toast.LENGTH_LONG).show();
                WifiP2pConfig config = new WifiP2pConfig();
                int index = parentActivity.mobileNames.indexOf(item);
                config.deviceAddress = (String) parentActivity.mobiles.get(index);
                config.wps.setup = WpsInfo.PBC;
                config.groupOwnerIntent = 0;
                Log.e("p2p", "TextView value: " + parentActivity.amount.getText());
                if (parentActivity.amount.getText() == null)
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter amount", Toast.LENGTH_LONG).show();
                else {
                    parentActivity.mWifiP2pManager.connect(parentActivity.mChannel, config, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            Log.e("p2p", "connected to device");
                             parentActivity.passkey = new Random().nextInt(8999)+1000;
                            progressDialog.setMessage("Passkey : "+parentActivity.passkey);
                            Log.e("p2p","passkey displayed");
                        //    -----------------------
                            // new FileClientAsyncTask(getActivity().getApplicationContext(), parentActivity.address, parentActivity.amount.getText().toString(), parentActivity).execute();
                        }

                        @Override

                        public void onFailure(int reason) {
                            Log.e("p2p", "couldn't connect to device.. reason:" + reason);
                        }
                    });
                }
            }
        });
        parentActivity.adapter.notifyDataSetChanged();

    }


    @Override
    public void onResume() {
        super.onResume();
        initUi();
        parentActivity.mReceiver = new WiFiDirectBroadcastReceiver(parentActivity.mWifiP2pManager, parentActivity.mChannel, parentActivity);
        parentActivity.registerReceiver(parentActivity.mReceiver, parentActivity.mIntentFilter);
        subscription = rxBus.toObserverable().subscribe(SendMoneyFragment.this);

    }

    @Override
    public void onPause() {
        super.onPause();
        parentActivity.unregisterReceiver(parentActivity.mReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.subscription.unsubscribe();
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
        switch (((EventResponse) o).getEvent()) {
            case EventNumbers.CLIENT_ASYNC_EVENT:
                parentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog != null)
                            progressDialog.dismiss();
                        parentActivity.textView.setText(String.valueOf(parentActivity.balance));
                    }
                });

                // Adding data in database
                DbHelper db = DbHelper.getInstance(this.getActivity());
                TransactionDto transactionData = (TransactionDto) ((EventResponse) o).getResponse();
                db.insertTransaction(transactionData);
                TransactionDto[] transactionDtos = db.getAllTransaction();
                Log.e("p2p", "Entry Made");
                for (int i = 0; i < transactionDtos.length; i++)
                    Log.e("sender done", "length:" + transactionDtos.length + " first:" + transactionDtos[i].getAmount());
                getActivity().getFragmentManager().popBackStack();
        }
    }
}
