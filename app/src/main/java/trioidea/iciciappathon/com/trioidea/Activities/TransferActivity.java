package trioidea.iciciappathon.com.trioidea.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import trioidea.iciciappathon.com.trioidea.Fragments.SendMoneyFragment;
import trioidea.iciciappathon.com.trioidea.Fragments.TransactionMainScreenFragment;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.Services.WiFiDirectBroadcastReceiver;

public class TransferActivity extends AppCompatActivity {

    public WifiP2pManager mWifiP2pManager;
    public WifiP2pManager.Channel mChannel;
    public WiFiDirectBroadcastReceiver mReceiver;
    public IntentFilter mIntentFilter;
    public ListView listView;
    public TextView textView;
    public ArrayAdapter adapter;
    public EditText amount;
    public List mobiles = mobiles = new ArrayList();
    public boolean isSender=false;
    public InetAddress address;
    public double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        balance=1000;
        //textView = (TextView) findViewById(R.id.balance);
        //textView.setText( Double.toString(balance));

//        adapter = new ArrayAdapter(this, R.layout.activity_listview, mobiles);


        //amount = (EditText) findViewById(R.id.amount);
        //amount.clearFocus();

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                final String item = ((TextView) view).getText().toString();
//                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
//                WifiP2pConfig config = new WifiP2pConfig();
//                config.deviceAddress = item;
//                config.wps.setup = WpsInfo.PBC;
//                config.groupOwnerIntent = 0;
//                mWifiP2pManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
//                    @Override
//                    public void onSuccess() {
//                        Log.e("p2p", "connected to device");
//                        new FileClientAsyncTask(TransferActivity.this, address, amount.getText().toString(), TransferActivity.this).execute();
//                    }
//
//                    @Override
//                    public void onFailure(int reason) {
//                        Log.e("p2p", "couldn't connect to device.. reason:" + reason);
//                    }
//                });
//            }
//        });
//        adapter.notifyDataSetChanged();

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


        mWifiP2pManager = (WifiP2pManager)getSystemService(WIFI_P2P_SERVICE);
        mChannel = mWifiP2pManager.initialize(this, getMainLooper(), null);
        if(mWifiP2pManager == null)
            Log.e("p2p"," null----");
        else
            Log.e("p2p"," not null----");

        TransactionMainScreenFragment transactionMainScreenFragment=new TransactionMainScreenFragment();
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container,transactionMainScreenFragment);
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new WiFiDirectBroadcastReceiver(mWifiP2pManager, mChannel, this);
        registerReceiver(mReceiver, mIntentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

   public void updateBalance()
   {
       textView.setText(Double.toString(balance));
   }

    public WifiP2pManager getManager()
    {
        return mWifiP2pManager;
    }

    public WifiP2pManager.Channel getChannel()
    {
        return mChannel;
    }

    public void startSendScreenFragment()
    {
        SendMoneyFragment sendMoneyFragment=new SendMoneyFragment();
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, sendMoneyFragment);
        fragmentTransaction.addToBackStack("SendScreen");
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed()
    {
        if(getFragmentManager().getBackStackEntryCount() == 0)
        {
            super.onBackPressed();
        }
        else
            getFragmentManager().popBackStack();

    }


}


